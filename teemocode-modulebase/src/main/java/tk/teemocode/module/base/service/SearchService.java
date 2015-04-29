package tk.teemocode.module.base.service;

import java.util.List;
import java.util.Set;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.search.Indexable;

/**
 * 搜索引擎查询API
 * @author yangylsky
 *
 */
public interface SearchService {
	/**
	 * 获取Json格式结果
	 * @param indexName
	 * @param id
	 * @return
	 */
	public String searchJsonById(String indexName, String id);

	/**
	 * 获取Json格式结果
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @return
	 */
	public String searchJsonById(String indexName, String typeName, String id);

	public String searchJsonByIds(String indexName, String typeName, Set<String> ids);

	/**
	 * 根据传入的Page信息(含查询条件)返回匹配的Page(Json格式的String)
	 * @param page
	 * @return
	 */
	public String searchJsonPage(Page<?> page);

	/**
	 * @param clazz
	 * @param indexName
	 * @param id
	 * @return
	 */
	public <T> T searchById(Class<T> clazz, String indexName, String id);

	/**
	 * @param clazz
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @return
	 */
	public <T> T searchById(Class<T> clazz, String indexName, String typeName, String id);

	/**
	 * @param clazz
	 * @param indexName
	 * @param typeName
	 * @param ids
	 * @return
	 */
	public <T> List<T> searchByIds(Class<T> clazz, String indexName, String typeName, Set<String> ids);

	/**
	 * 根据传入的Page信息(含查询条件)返回匹配的结果数量
	 * @param page
	 * @return
	 */
	public int searchResultNumbers(Page<?> page);

	/**
	 * 根据传入的Page信息(含查询条件)返回匹配的Page
	 * @param page
	 * @return
	 */
	public <T> Page<T> searchIndexItemPage(Page<T> page);

	/**
	 * 创建/更新索引项
	 * @param intexItem
	 */
	public <I extends Indexable> void createIndexItem(I intexItem);

	/**
	 * 批量创建/更新索引项
	 * @param clazz
	 */
	public <B extends IBO> void updateIndexItems(Class<B> clazz);

	/**
	 * 批量删除索引项
	 * @param indexName
	 * @param typeNames
	 */
	public void deleteIndexItems(String indexName, String... typeNames);

	/**
	 * 删除索引项
	 * @param intexItem
	 */
	public <I extends Indexable> void deleteIndexItem(I intexItem);

	/**
	 * 重建索引
	 * @param indexName
	 */
	public void rebuildAllIndex(String indexName);
}
