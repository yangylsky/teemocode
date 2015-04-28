package com.teemocode.module.search.service;

import java.util.List;

import com.teemocode.module.search.Indexable;

/**
 * 索引操作Service
 * @author yangylsky
 *
 */
public interface ESIndexService {
	/**
	 * 创建索引
	 * @param indexName
	 */
	public void createIndex(String indexName);

	/**
	 * 删除索引
	 * @param indexName
	 */
	public void deleteIndex(String indexName);

	/**
	 * 关闭索引
	 * @param indexName
	 */
	public void closeIndex(String indexName);

	/**
	 * 打开索引
	 * @param indexName
	 */
	public void openIndex(String indexName);

	/**
	 * 判断索引是否存在
	 * @param indexName
	 * @return
	 */
	public boolean indexExists(String indexName);

	/**
	 * 根据传入的Json创建或更新索引项
	 * @param json
	 */
	public void createItem(String json);

	/**
	 * 根据传入的对象创建或更新索引项
	 * @param source - 传入对象需带@JestId注解
	 */
	public <I extends Indexable> void createItem(I item);

	/**
	 * 批量创建索引项
	 * @param sources - 传入对象需带@JestId注解
	 */
	public <I extends Indexable> void bulkCreateItems(List<I> items);

	/**
	 * 删除索引项
	 * @param indexName
	 * @param typeName
	 * @param id
	 */
	public void deleteItem(String indexName, String typeName, String id);

	/**
	 * 删除索引<br/>
	 * 警告 - indexName或typeNames为空表示所有
	 * @param indexName
	 * @param typeNames
	 */
	public void deleteByType(String indexName, String... typeNames);

	/**
	 * 批量删除索引项
	 * @param toDeleteItems - 传入对象需包含indexName，typeName，id属性
	 */
	public <I extends Indexable> void bulkDeleteItems(List<I> toDeleteItems);
}
