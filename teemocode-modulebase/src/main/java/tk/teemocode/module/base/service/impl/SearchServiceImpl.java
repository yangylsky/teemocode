package tk.teemocode.module.base.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.service.LocalService;
import tk.teemocode.module.base.service.SearchService;
import tk.teemocode.module.index.SearchConstant;
import tk.teemocode.module.index.convertor.ConvertorCache;
import tk.teemocode.module.search.Indexable;
import tk.teemocode.module.search.service.ESIndexService;
import tk.teemocode.module.search.service.ESSearchService;
import tk.teemocode.module.util.CommonUtil;

/**
 * 搜索引擎查询API实现
 * @author yangylsky
 *
 */
@Service
public class SearchServiceImpl implements SearchService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ESSearchService esSearchService;

	@Resource
	private ESIndexService esIndexService;

	@SuppressWarnings("rawtypes")
	@Resource(name = "base/LocalService")
	private LocalService localService;

	/**
	 * indexName为空时，自动设置为{@link tk.teemocode.module.index.SearchConstant#DEFAULT_INDEX_NAME
	 * 	SearchConstant.DEFAULT_INDEX_NAME}
	 * @param page
	 */
	protected <T> Page<T> checkIndexNames(Page<T> page) {
		if(ArrayUtils.isEmpty(page.getIndexNames())) {
//			page.setIndexNames(new String[] {SearchConstant.DEFAULT_INDEX_NAME});
		}
		return page;
	}

	@Override
	public String searchJsonById(String indexName, String id) {
		return esSearchService.searchJsonById(indexName, id);
	}

	@Override
	public String searchJsonById(String indexName, String typeName, String id) {
		return esSearchService.searchJsonById(indexName, typeName, id);
	}

	@Override
	public String searchJsonByIds(String indexName, String typeName, Set<String> ids) {
		return esSearchService.searchJsonListByIds(indexName, typeName, ids);
	}

	@Override
	public String searchJsonPage(Page<?> page) {
		return esSearchService.searchJsonPage(checkIndexNames(page));
	}

	@Override
	public <T> T searchById(Class<T> clazz, String indexName, String id) {
		return esSearchService.searchObjectById(clazz, indexName, id);
	}

	@Override
	public <T> T searchById(Class<T> clazz, String indexName, String typeName, String id) {
		return esSearchService.searchObjectById(clazz, indexName, typeName, id);
	}

	@Override
	public <T> List<T> searchByIds(Class<T> clazz, String indexName, String typeName, Set<String> ids) {
		return esSearchService.searchListByIds(clazz, indexName, typeName, ids);
	}

	@Override
	public int searchResultNumbers(Page<?> page) {
		return esSearchService.searchResultCount(checkIndexNames(page));
	}

	@Override
	public <T> Page<T> searchIndexItemPage(Page<T> page) {
		return esSearchService.searchObjectPage(checkIndexNames(page));
	}

	@Override
	public <I extends Indexable> void createIndexItem(I intexItem) {
		esIndexService.createItem(intexItem);
	}

	@Override
	public <B extends IBO> void updateIndexItems(Class<B> clazz) {
		Page<B> page = new Page<>(clazz, 20);
		int start = 0;
		do {
			page.clearResult();
			page.setStart(start);
			page = CommonUtil.getPageBos(page);
			updateIndex(page);
			start += 20;
		} while(CollectionUtils.isNotEmpty(page.getResult()));
	}

	private <B extends IBO> void updateIndex(Page<B> page) {
		for(B bo : page.getResult()) {
			try {
				CommonUtil.createIndexItem(bo);
			} catch(Exception e) {
				logger.error("创建索引项失败[" + bo.getClass() + ":" + bo.getUuid() + "]", e);
			}
		}
	}

	@Override
	public void deleteIndexItems(String indexName, String... typeNames) {
		esIndexService.deleteByType(indexName, typeNames);
	}

	@Override
	public <I extends Indexable> void deleteIndexItem(I intexItem) {
		esIndexService.deleteItem(intexItem.getIndexName(), intexItem.getTypeName(), intexItem.getId());
	}

	@Override
	public void rebuildAllIndex(String indexName) {
		indexName = indexName == null ? SearchConstant.DEFAULT_INDEX_NAME : indexName;
		if(esIndexService.indexExists(indexName)) {
			esIndexService.deleteByType(indexName, new String[0]);
		} else {
			esIndexService.createIndex(indexName);
		}

		for(Class<IBO> clazz : ConvertorCache.getIndexableKeys()) {
			try {
				updateIndexItems(clazz);
			} catch(Throwable t) {
				logger.error("索引项创建失败:" + clazz.getName(), t);
			}
		}
	}
}
