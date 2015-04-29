package tk.teemocode.module.search.service.impl;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.Index;
import io.searchbox.indices.CloseIndex;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.OpenIndex;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tk.teemocode.commons.exception.ProjectException;
import tk.teemocode.module.search.Indexable;
import tk.teemocode.module.search.factory.ESFactory;
import tk.teemocode.module.search.handler.BulkCreateItemsHandler;
import tk.teemocode.module.search.handler.BulkDeleteItemsHandler;
import tk.teemocode.module.search.handler.CloseIndexHandler;
import tk.teemocode.module.search.handler.CreateOrUpdateIndexHandler;
import tk.teemocode.module.search.handler.DeleteIndexHandler;
import tk.teemocode.module.search.handler.IndexProcessException;
import tk.teemocode.module.search.handler.OpenIndexHandler;
import tk.teemocode.module.search.service.ESIndexService;

@Service
public class ESIndexServiceImpl implements ESIndexService {
	protected static final Logger logger = LoggerFactory.getLogger(ESIndexServiceImpl.class);

	@Override
	public void createIndex(String indexName) {
		CreateIndex createIndex = new CreateIndex.Builder(indexName).build();
		try {
			ESFactory.getClient().executeAsync(createIndex, new CreateOrUpdateIndexHandler());
		} catch(Exception e) {
			logger.error("Create index failed[indexName:" + indexName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void deleteIndex(String indexName) {
		DeleteIndex deleteIndex = new DeleteIndex.Builder(indexName).build();
		try {
			ESFactory.getClient().executeAsync(deleteIndex, new DeleteIndexHandler());
		} catch(Exception e) {
			logger.error("Delete index failed[indexName:" + indexName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void closeIndex(String indexName) {
		CloseIndex closeIndex = new CloseIndex.Builder(indexName).build();
		try {
			ESFactory.getClient().executeAsync(closeIndex, new CloseIndexHandler());
		} catch(Exception e) {
			logger.error("Close index failed[indexName:" + indexName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void openIndex(String indexName) {
		OpenIndex openIndex = new OpenIndex.Builder(indexName).build();
		try {
			ESFactory.getClient().executeAsync(openIndex, new OpenIndexHandler());
		} catch(Exception e) {
			logger.error("Open index failed[indexName:" + indexName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public boolean indexExists(String indexName) {
		IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
		try {
			return ESFactory.getClient().execute(indicesExists).getJsonObject().get("found").getAsBoolean();
		} catch(Exception e) {
			logger.error("Open index failed[indexName:" + indexName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void createItem(String json) {
		// TODO Auto-generated method stub
	}

	@Override
	public <I extends Indexable> void createItem(I item) {
		Index index = new Index.Builder(item).index(item.getIndexName()).type(item.getTypeName()).id(item.getId()).build();
		try {
			long start = System.currentTimeMillis();
			JestResult result = ESFactory.getClient().execute(index);
			if(result.isSucceeded()) {
				long past = System.currentTimeMillis() - start;
				boolean isCreate = (boolean) result.getValue("created");
				logger.info((isCreate ? "创建" : "更新") + "索引项成功[耗时:" + past + "ms] - " + result.getJsonString());
			} else {
				throw new IndexProcessException(result.getErrorMessage());
			}
//			ESFactory.getClient().executeAsync(index, new CreateOrUpdateItemHandler());
		} catch(Exception e) {
			logger.error("Create or update item failed[source:" + item + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public <I extends Indexable> void bulkCreateItems(List<I> items) {
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for(I source : items) {
			Index index = new Index.Builder(source).index(source.getIndexName()).type(source.getTypeName()).id(source.getId())
					.build();
			bulkBuilder.addAction(index);
		}
		try {
//			ESFactory.getClient().execute(bulkBuilder.build());
			ESFactory.getClient().executeAsync(bulkBuilder.build(), new BulkCreateItemsHandler());
		} catch(Exception e) {
			logger.error("Bulk create or update items failed[items:" + items + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void deleteItem(String indexName, String typeName, String id) {
		Delete delete = new Delete.Builder(id).index(indexName).type(typeName).build();
		try {
			JestResult result = ESFactory.getClient().execute(delete);
			if(result.isSucceeded()) {
				logger.info("删除索引项成功 - " + result.getJsonString());
			} else {
				throw new IndexProcessException(result.getErrorMessage());
			}
//    		ESFactory.getClient().executeAsync(delete, new DeleteItemHandler());
		} catch(Exception e) {
			logger.error("Delete item failed[indexName:" + indexName + ", typeName:" + typeName + ", id:" + id + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public void deleteByType(String indexName, String... typeNames) {
		if(ArrayUtils.isEmpty(typeNames)) {
			_deleteByType(indexName, null);
		} else {
			for(String typeName : typeNames) {
				_deleteByType(indexName, typeName);
			}
		}
	}

	private void _deleteByType(String indexName, String typeName) {
		String query = "{query:{\"match_all\":{}}}";
		DeleteByQuery.Builder deleteByQueryBuilder = new DeleteByQuery.Builder(query);
		if(StringUtils.isNotBlank(indexName)) {
			deleteByQueryBuilder.addIndex(indexName);
		}
		if(StringUtils.isNotBlank(typeName)) {
			deleteByQueryBuilder.addType(typeName);
		}
		try {
			JestResult result = ESFactory.getClient().execute(deleteByQueryBuilder.build());
			if(result.isSucceeded()) {
				logger.info("批量删除索引成功[index:" + indexName + ", type:" + typeName + "] - " + result.getJsonString());
			} else {
				throw new IndexProcessException(result.getErrorMessage());
			}
		} catch(Exception e) {
			logger.error("Delete item failed[indexName:" + indexName + ", typeName:" + typeName + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public <I extends Indexable> void bulkDeleteItems(List<I> toDeleteItems) {
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for(I item : toDeleteItems) {
			Delete delete = new Delete.Builder(item.getId()).index(item.getIndexName()).type(item.getTypeName()).build();
			bulkBuilder.addAction(delete);
		}
		try {
			ESFactory.getClient().executeAsync(bulkBuilder.build(), new BulkDeleteItemsHandler());
		} catch(Exception e) {
			logger.error("Bulk delete items failed[items:" + toDeleteItems + "]", e);
			throw new ProjectException(e);
		}
	}
}
