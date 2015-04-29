package tk.teemocode.module.search.service;

import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;
import tk.teemocode.commons.component.page.Page;

/**
 * 索引查询Service
 * @author yangylsky
 *
 */
public interface ESSearchService {
	/**
	 * 返回单个Item(Json格式的String)
	 * @param indexName
	 * @param id
	 * @return
	 */
	public String searchJsonById(String indexName, String id);

	public String searchJsonById(String indexName, String typeName, String id);

	public String searchJsonListByIds(String indexName, String typeName, Set<String> ids);

	/**
	 *
	 * @param indexName
	 * @param id
	 * @return
	 */
	public JsonObject searchJsonObjectById(String indexName, String id);

	/**
	 *
	 * @param clazz
	 * @param indexName
	 * @param id
	 * @return
	 */
	public <T> T searchObjectById(Class<T> clazz, String indexName, String id);

	public <T> T searchObjectById(Class<T> clazz, String indexName, String typeName, String id);

	/**
	 *
	 * @param clazz
	 * @param indexName
	 * @param typeName
	 * @param ids
	 * @return
	 */
	public <T> List<T> searchListByIds(Class<T> clazz, String indexName, String typeName, Set<String> ids);

	/**
	 * 根据传入的Page信息(含查询条件)返回匹配的结果数量
	 *
	 * @param page
	 * @return
	 */
	public int searchResultCount(Page<?> page);

	/**
	 * 根据传入的Page信息(含查询条件)返回匹配的Page(Json格式的String)
	 *
	 * @param page
	 * @return
	 */
	public String searchJsonPage(Page<?> page);

	/**
	 *
	 * @param page
	 * @return
	 */
	public <T> Page<T> searchObjectPage(Page<T> page);
}
