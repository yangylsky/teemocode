package tk.teemocode.web.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Resource;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.module.base.service.SearchService;
import tk.teemocode.web.util.SearchParams;

public abstract class EsSearchAction<P extends SearchParams> extends SimpleActionSupport {
	private Class<P> paramClass;

	@Resource
	protected SearchService searchService;

	@SuppressWarnings("unchecked")
	protected EsSearchAction() {
		if(!this.getClass().equals(EsSearchAction.class)) {
			Type classType = this.getClass().getGenericSuperclass();
			if(classType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) classType).getActualTypeArguments();
				paramClass = (Class<P>) paramTypes[0];
			}
		}
	}

	public Class<P> getParamClass() {
		return paramClass;
	}

	/**
	 * 构建参数序列为ES查询的QueryExpression json格式
	 * @param params
	 * @return
	 */
	protected abstract String buildQueryExpression(P params);

	@RequestMapping("search_page")
	public String searchPage(ModelMap map, P params, Page<?> page) {
		try {
			page.setQueryExpression(buildQueryExpression(params));
			String esResult =  searchService.searchJsonPage(page);
			//解析es result格式的json，封装为Page格式的json
			//String json = parseEsResult(esResult);
			map.put("json", esResult);
			return "jsonlist";
		} catch(Exception e) {
			logger.error("搜索数据失败", e);
			return renderJsonError("搜索数据失败" + e.getMessage(), map);
		}
	}
}
