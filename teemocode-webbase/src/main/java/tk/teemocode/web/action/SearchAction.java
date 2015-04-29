package tk.teemocode.web.action;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.util.reflect.PojoUtil;
import tk.teemocode.module.base.service.SearchService;
import tk.teemocode.module.index.SearchConstant;
import tk.teemocode.module.search.Indexable;

/**
 * 搜索引擎查询Action
 * @author yangylsky
 *
 */
@Controller
@RequestMapping("/search")
public class SearchAction extends SimpleActionSupport {
	@Resource
	protected SearchService searchService;

	/**
	 * 查询单个结果，格式为搜索引擎返回的json
	 * @param map
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @return
	 */
	@RequestMapping("search_single_json")
	public String searchJson(ModelMap map, String indexName, String typeName, String id) {
		try {
			String json = searchService.searchJsonById(getIndexName(indexName), typeName, id);
			map.put("json", json);
			return "jsonobj";
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败:" + e.getMessage(), map);
		}
	}

	/**
	 * 查询分页的多个结果，格式为搜索引擎返回的json
	 * @param map
	 * @param page
	 * @return
	 */
	@RequestMapping("search_page_json")
	public String searchJson(ModelMap map, Page<?> page) {
		try {
			String json =  searchService.searchJsonPage(page);
			map.put("json", json);
			return "jsonlist";
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}

	@RequestMapping("search_list_json")
	public String searchJson(ModelMap map, String indexName, String typeName, Set<String> ids) {
		try {
			String json = searchService.searchJsonByIds(indexName, typeName, ids);
			map.put("json", json);
			return "jsonlist";
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}

	/**
	 * 查询单个结果，格式为指定类型的Indexable转化的json
	 * @param map
	 * @param className
	 * @param indexName
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("search_single")
	public <I extends Indexable> String searchSingle(ModelMap map, String className, String indexName, String id) {
		try {
			Class<I> indexItemClass = (Class<I>) Class.forName(className);
			Indexable indexItem = searchService.searchById(indexItemClass, getIndexName(indexName), id);
			return renderJsonObj(indexItem, map);
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}

	/**
	 * 查询分页的多个结果，格式为指定类型的Indexable转化的json
	 * @param map
	 * @param className
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("search_page")
	public <I extends Indexable> String searchPage(ModelMap map, String className, Page<I> page) {
		try {
			Class<I> indexItemClass = (Class<I>) Class.forName(className);
			Page<I> newPage = new Page<>(indexItemClass);
			page = PojoUtil.merge(newPage, page);
			page = searchService.searchIndexItemPage(page);
			return renderJsonPage(page, map);
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("search_list")
	public <I extends Indexable> String searchList(ModelMap map, String className, String indexName, String typeName, Set<String> ids) {
		try {
			Class<I> indexItemClass = (Class<I>) Class.forName(className);
			List<I> resultList = searchService.searchByIds(indexItemClass, indexName, typeName, ids);
			map.put("json", resultList);
			return "jsonlist";
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}

	protected String getIndexName(String indexName) {
		return StringUtils.isBlank(indexName) ? SearchConstant.DEFAULT_INDEX_NAME : indexName;
	}
}
