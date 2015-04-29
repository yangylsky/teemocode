package tk.teemocode.module.base.convertor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.page.Page;
import tk.teemocode.commons.exception.ReflectException;
import tk.teemocode.commons.util.reflect.PojoUtil;

public abstract class BaseConvertor {
	private static final Logger logger = LoggerFactory.getLogger(BaseConvertor.class);

	public static <C1, C2> C1 convert(C2 srcObj, Class<C1> destClass, String... fields) {
		try {
			if(srcObj == null) {
				return null;
			}
			C1 destObj = destClass.newInstance();
			if(ArrayUtils.isEmpty(fields)) {
				PojoUtil.simpleObjToObj(destObj, srcObj);
			} else {
				PojoUtil.objToObjByDescsWithNull(destObj, srcObj, fields);
			}

			return destObj;
		} catch(InstantiationException | IllegalAccessException e) {
			logger.error("", e);
			throw new ReflectException(e);
		}
	}

	public static <C1, C2> C1 convert(C1 destObj, C2 srcObj, String... fields) {
		if(ArrayUtils.isEmpty(fields)) {
			PojoUtil.simpleObjToObj(destObj, srcObj);
		} else {
			PojoUtil.objToObjByDescsWithNull(destObj, srcObj, fields);
		}

		return destObj;
	}

	public static <C1, C2> List<C1> convert(List<C2> srcObjs, Class<C1> destClass, String... fields) {
		List<C1> destObjs = new ArrayList<>();
		for(C2 srcObj : srcObjs) {
			destObjs.add(convert(srcObj, destClass, fields));
		}
		return destObjs;
	}

	public static <C1, C2> Set<C1> convert(Set<C2> srcObjs, Class<C1> destClass, String... fields) {
		Set<C1> destObjs = new HashSet<>();
		for(C2 srcObj : srcObjs) {
			destObjs.add(convert(srcObj, destClass, fields));
		}
		return destObjs;
	}

	public static <C1, C2> Page<C1> convert(Page<C2> c2Page, Class<C1> destClass) {
		return convert(c2Page, destClass, true);
	}

	public static <C1, C2> Page<C1> convert(Page<C2> c2Page, Class<C1> destClass, String... fields) {
		return convert(c2Page, destClass, true, fields);
	}

	public static <C1, C2> Page<C1> convert(Page<C2> c2Page, Class<C1> destClass, boolean convertResult, String... fields) {
		Page<C1> toPage = new Page<>();
		toPage.setStart(c2Page.getStart());
		toPage.setLimit(c2Page.getLimit());
		toPage.setQueryExpression(c2Page.getQueryExpression());
		toPage.setParamValues(c2Page.getParamValues());
		toPage.setSort(c2Page.getSort());
		toPage.setDir(c2Page.getDir());
		toPage.setPageNo(c2Page.getPageNo());
		toPage.setTotalCount(c2Page.getTotalCount());
		if(convertResult) {
			toPage.setResult(convert(c2Page.getResult(), destClass, fields));
		}
		return toPage;
	}
}
