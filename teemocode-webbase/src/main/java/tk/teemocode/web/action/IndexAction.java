package tk.teemocode.web.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.base.service.SearchService;
import tk.teemocode.module.search.Indexable;

/**
 * 搜索引擎索引Action
 * @author yangylsky
 *
 */
@Controller
@RequestMapping("/index")
public class IndexAction extends SimpleActionSupport {
	@Resource
	protected SearchService searchService;

	@RequestMapping("create_index_item")
	public String createIndexItem(ModelMap map, Indexable indexItem) {
		try {
			searchService.createIndexItem(indexItem);
			return renderJsonMsg("索引项创建成功", map);
		} catch(Exception e) {
			logger.error("索引项创建失败", e);
			return renderJsonError("索引项创建失败" + e.getMessage(), map);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("update_index_items")
	public String updateIndexItems(ModelMap map, String className) {
		try {
			Class<? extends IBO> boClass = (Class<? extends IBO>) Class.forName(className);
			searchService.updateIndexItems(boClass);
			return renderJsonMsg("索引项创建/更新成功", map);
		} catch(Exception e) {
			logger.error("索引项创建/更新失败", e);
			return renderJsonError("索引项创建/更新失败" + e.getMessage(), map);
		}
	}

	@RequestMapping("delete_index_items")
	public String deleteIndexItems(ModelMap map, String indexName, String[] typeName) {
		try {
			searchService.deleteIndexItems(indexName, typeName);
			return renderJsonMsg("索引项删除成功", map);
		} catch(Exception e) {
			logger.error("索引项删除失败", e);
			return renderJsonError("索引项删除失败" + e.getMessage(), map);
		}
	}

	@RequestMapping("rebuild_all_index")
	public String rebuildAllIndex(ModelMap map, String indexName) {
		try {
			searchService.rebuildAllIndex(indexName);
			return renderJsonMsg("索引项重建成功", map);
		} catch(Exception e) {
			logger.error("索引项重建失败", e);
			return renderJsonError("索引项重建失败" + e.getMessage(), map);
		}
	}
}
