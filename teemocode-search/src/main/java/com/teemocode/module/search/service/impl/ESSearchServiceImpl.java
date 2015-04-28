package com.teemocode.module.search.service.impl;

import io.searchbox.client.JestResult;
import io.searchbox.core.Count;
import io.searchbox.core.Get;
import io.searchbox.core.MultiGet;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.teemocode.commons.component.page.Page;
import com.teemocode.commons.exception.ProjectException;
import com.teemocode.module.search.factory.ESFactory;
import com.teemocode.module.search.service.ESSearchService;

@Service
public class ESSearchServiceImpl implements ESSearchService {
	protected static final Logger logger = LoggerFactory.getLogger(ESSearchServiceImpl.class);

	@Override
	public String searchJsonById(String indexName, String id) {
        return searchJsonById(indexName, null, id);
	}

	@Override
	public String searchJsonById(String indexName, String typeName, String id) {
        return searchJestResult(indexName, typeName, id).getJsonString();
	}

	@Override
	public String searchJsonListByIds(String indexName, String typeName, Set<String> ids) {
		return searchJestResult(indexName, typeName, ids).getJsonString();
	}

	@Override
	public JsonObject searchJsonObjectById(String indexName, String id) {
        return searchJestResult(indexName, null, id).getJsonObject();
	}

	@Override
	public <T> T searchObjectById(Class<T> clazz, String indexName, String id) {
		return searchObjectById(clazz, indexName, null, id);
	}

	@Override
	public <T> T searchObjectById(Class<T> clazz, String indexName, String typeName, String id) {
		return searchJestResult(indexName, typeName, id).getSourceAsObject(clazz);
	}

	private JestResult searchJestResult(String indexName, String typeName, String id) {
		if(StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("参数[id]不能为空");
		}
		Get.Builder getBuilder = new Get.Builder(indexName, id);
		if(StringUtils.isNotBlank(typeName)) {
			getBuilder.type(typeName);
		}
        try {
        	return ESFactory.getClient().execute(getBuilder.build());
        } catch(Exception e) {
        	logger.error("Get JestResult failed[indexName:" + indexName + ", id:" + id + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public <T> List<T> searchListByIds(Class<T> clazz, String indexName, String typeName, Set<String> ids) {
		return searchJestResult(indexName, typeName, ids).getSourceAsObjectList(clazz);
	}

	private JestResult searchJestResult(String indexName, String typeName, Set<String> ids) {
		if(CollectionUtils.isEmpty(ids)) {
			throw new IllegalArgumentException("参数[ids]不能为空");
		}
		MultiGet multiGet = new MultiGet.Builder.ById(indexName, typeName).addId(ids).build();
        try {
        	return ESFactory.getClient().execute(multiGet);
        } catch(Exception e) {
        	logger.error("Get Objects failed[indexName:" + indexName + ", typeName:" + typeName + ", ids:" + ids + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public int searchResultCount(Page<?> page) {
		Count count = new Count.Builder().query(page.getQueryExpression()).build();
        try {
        	return ESFactory.getClient().execute(count).getJsonObject().getAsInt();
        } catch(Exception e) {
        	logger.error("Get ResultCount failed[queryExpression:" + page.getQueryExpression() + "]", e);
			throw new ProjectException(e);
		}
	}

	@Override
	public String searchJsonPage(Page<?> page) {
		return getJestResult(page).getJsonString();
	}

	@Override
	public <T> Page<T> searchObjectPage(Page<T> page) {
		if(page.getTClass() == null) {
			throw new IllegalArgumentException("没有指定tClass，请使用public Page(Class<T> tClass)构造函数.");
		}
		long start = System.currentTimeMillis();
		JestResult jestResult = getJestResult(page);
		List<T> result = jestResult.getSourceAsObjectList(page.getTClass());
		page.setResult(result);
		page.setTotalCount(result.size());
		logger.info("Search page[" + result.size() + " results] in " + (System.currentTimeMillis() - start) + "ms.");
		return page;
	}

	private JestResult getJestResult(Page<?> page) {
		if(StringUtils.isBlank(page.getQueryExpression())) {
			throw new IllegalArgumentException("参数[page.queryExpression]不能为空");
		}
		if(ArrayUtils.isEmpty(page.getIndexNames())) {
			throw new IllegalArgumentException("参数[page.indexNames]不能为空");
		}
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.query(page.getQueryExpression())
				.from(page.getStart())
				.size(page.getPageSize());
		if(ArrayUtils.isNotEmpty(page.getFields())) {
			searchSourceBuilder.fields(page.getFields());
		}
		if(StringUtils.isNotBlank(page.getSort())) {
			if(StringUtils.isNotBlank(page.getDir())) {
				SortOrder sortOrder = SortOrder.valueOf(page.getDir());
				searchSourceBuilder.sort(page.getSort(),  sortOrder);
			} else {
				searchSourceBuilder.sort(page.getSort());
			}
		}
		Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
		searchBuilder.addIndex(Arrays.asList(page.getIndexNames()));
		if(ArrayUtils.isNotEmpty(page.getTypeNames())) {
			searchBuilder.addType(Arrays.asList(page.getTypeNames()));
		}
		try {
			return ESFactory.getClient().execute(searchBuilder.build());
		} catch(Exception e) {
        	logger.error("Get ResultCount failed[searchSource:" + searchSourceBuilder.toString() + "]", e);
			throw new ProjectException(e);
		}
	}
}
