package com.teemocode.commons.component.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teemocode.commons.component.json.JSONDecoder;
import com.teemocode.commons.component.json.JSONEncoder;
import com.teemocode.commons.util.DateUtil;
import com.teemocode.commons.util.lang.EnumUtils;
import com.teemocode.commons.util.reflect.PojoUtil;

@SuppressWarnings("unchecked")
public class JSONHiExpression {
	private static Logger logger = LoggerFactory.getLogger(JSONHiExpression.class);

	public static final String EXPRESSION_ALIAS = "alias";

	public static final String EXPRESSION_EXP = "expression";

	public static final String EXPRESSION_CRITERIA = "criteria";

	public static final String EXPRESSION_DETACHEDCRITERIAS = "detachedCriterias";

	public static final String EXPRESSION_SUBCRITERIAS = "subCriterias";

	public static final String EXPRESSION_CRITERIONS = "criterions";

	public static final String EXPRESSION_AND = "and";

	public static final String EXPRESSION_OR = "or";

	public static final String EXPRESSION_NOT = "not";

	public static final String EXPRESSION_CONJUNCTION = "conjunction";

	public static final String EXPRESSION_DISJUNCTION = "disjunction";

	private static final String DateFormat = "yyyy-MM-dd";

	public static Map<String, String> parseAlias(Page<?> page) {
		String jsonExpression = page.getQueryExpression();
		List<String> list = new ArrayList<String>();
//		List<String> dataAuthoritys = page.getModelAuthorityFilterExpressions();
//		if(dataAuthoritys != null) {
//			list.addAll(dataAuthoritys);
//		}
		if(!StringUtils.isBlank(jsonExpression)) {
			list.add(jsonExpression);
		}
		Map<String, String> alias = new HashMap<String, String>();
		for(String expression : list) {
			Map<String, Object> expressionObj = JSONDecoder.decode(expression);
			if(expressionObj != null) {
				Set<String> set = expressionObj.keySet();
				Iterator<String> iter = set.iterator();
				while(iter.hasNext()) {
					String key = iter.next();
					if(key.equals(EXPRESSION_ALIAS)) {
						alias.putAll((Map<String, String>) expressionObj.get(key));
					}
				}
			}
		}
		logger.debug("alias=" + JSONEncoder.encode(alias));
		return alias;
	}

	public static Criterion parseExpressonForCriteria(Page<?> page) {
		String jsonExpression = page.getQueryExpression();
		List<String> list = new ArrayList<String>();
//		List<String> dataAuthoritys = page.getModelAuthorityFilterExpressions();
//		if(dataAuthoritys != null) {
//			list.addAll(dataAuthoritys);
//		}
		if(!StringUtils.isBlank(jsonExpression)) {
			list.add(jsonExpression);
		}
		Conjunction cj = Restrictions.conjunction();
		for(String expression : list) {
			if(logger.isDebugEnabled()) {
				logger.debug("json1=" + jsonExpression);
			}
			Map<String, Object> expressionObj = JSONDecoder.decode(expression);
			if(logger.isDebugEnabled()) {
				logger.debug("json2=" + JSONEncoder.encode(expressionObj));
			}
			Map<String, Object> crias = null;
			if(expressionObj != null) {
				Set<String> set = expressionObj.keySet();
				Iterator<String> iter = set.iterator();
				while(iter.hasNext()) {
					String key = iter.next();
					if(key.equals(EXPRESSION_CRITERIA)) {
						crias = (Map<String, Object>) expressionObj.get(key);
					}
				}
			}

			Criterion cri = null;
			if(crias != null) {
				cri = parseCriteria(crias);
			}

			if(cri != null) {
				cj.add(cri);
			}
		}
		return cj;
	}

	private static Criterion parseExpresson(String key, Object obj) {
		Criterion cri = null;
		try {
			if(key.startsWith(EXPRESSION_CONJUNCTION)) {
				cri = parseExpressonConjunction((Map<String, Object>) obj);
			} else if(key.startsWith(EXPRESSION_DISJUNCTION)) {
				cri = parseExpressonDisjunction((Map<String, Object>) obj);
			} else if(key.startsWith(EXPRESSION_CRITERIONS)) {
				cri = parseExpressonCriterions((List<Map<String, Object>>) obj);
			} else if(key.startsWith(EXPRESSION_AND)) {
				cri = parseExpressonAnd((Map<String, Object>) obj);
			} else if(key.startsWith(EXPRESSION_OR)) {
				cri = parseExpressonOr((Map<String, Object>) obj);
			} else if(key.startsWith(EXPRESSION_NOT)) {
				cri = parseExpressonNot((Map<String, Object>) obj);
			}

		} catch(Exception e) {
			String errMsg = "condition=" + JSONEncoder.encode(obj);
			logger.error(errMsg, e);
			throw new IllegalArgumentException("解析查询条件出错:" + errMsg);
		}
		return cri;
	}

	private static Criterion parseExpressonNot(Map<String, Object> obj) {

		Criterion cri = null;
		Iterator<String> iter = obj.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object ex = obj.get(key);
			cri = parseExpresson(key, ex);
		}

		return Restrictions.not(cri);
	}

	private static Criterion parseExpressonOr(Map<String, Object> obj) {
		Criterion cri = null;
		Iterator<String> iter = obj.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object ex = obj.get(key);
			cri = parseDisExpressonCriterions((List<Map<String, Object>>) ex);
		}

		return cri;
	}

	private static Criterion parseExpressonAnd(Map<String, Object> obj) {

		Criterion cri = null;
		Iterator<String> iter = obj.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object ex = obj.get(key);
			cri = parseConExpressonCriterions((List<Map<String, Object>>) ex);
		}

		return cri;
	}

	private static Criterion parseConExpressonCriterions(List<Map<String, Object>> list) {
		if(list.size() == 0) {
			return null;
		}
		Conjunction cj = Restrictions.conjunction();
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> child = list.get(i);
			FilterCriterion condition = PojoUtil.mapToBean(child, FilterCriterion.class);
			if(condition != null) {
				Criterion exp = parseCondition(condition);
				if(exp != null) {
					cj.add(exp);
				}
			}
		}
		return cj;
	}

	private static Criterion parseDisExpressonCriterions(List<Map<String, Object>> list) {
		if(list.size() == 0) {
			return null;
		}
		Disjunction dj = Restrictions.disjunction();
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> child = list.get(i);
			FilterCriterion condition = PojoUtil.mapToBean(child, FilterCriterion.class);
			if(condition != null) {
				Criterion exp = parseCondition(condition);
				if(exp != null) {
					dj.add(exp);
				}
			}
		}
		return dj;
	}

	private static Criterion parseExpressonCriterions(List<Map<String, Object>> list) {
		return parseConExpressonCriterions(list);
	}

	private static Criterion parseCondition(FilterCriterion condition) {
		String comparison = condition.getComparison();
		Object value = null;
		if(condition.getValue().size() == 0) {
			if(condition.getType().equals(FilterCriterion.TYPE_STRING)) {
				if(comparison.equals(FilterCriterion.COMPARISON_NE)) {
					comparison = FilterCriterion.COMPARISON_ISNOTTNULL;
				} else if(comparison.equals(FilterCriterion.COMPARISON_EQ)
						|| comparison.equals(FilterCriterion.COMPARISON_LIKE)) {
					comparison = FilterCriterion.COMPARISON_ISNULL;
				}
			} else {
//				return null;
			}
		}
		List<Object> values = new ArrayList<>();
		if(condition.getType().equals(FilterCriterion.TYPE_DATE)) {
			String dateFormat = DateFormat;
			if(condition.getFormat() != null) {
				dateFormat = condition.getFormat();
			}
			if(condition.getValue().size() == 1) {
				value = DateUtil.convertStringToDate(dateFormat, (String) condition.getValue().get(0));
				values.add(value);
			} else {
				for(int i = 0; i < condition.getValue().size(); i++) {
					values.add(i, DateUtil.convertStringToDate(dateFormat, (String) condition.getValue().get(i)));
				}
			}
		} else {
			if(condition.getValue().size() == 1) {
				value = pareseValue(condition, condition.getValue().get(0));
				values.add(value);
			} else {
				for(int i = 0; i < condition.getValue().size(); i++) {
					values.add(pareseValue(condition, condition.getValue().get(i)));
				}
			}
		}

		if(comparison.equals(FilterCriterion.COMPARISON_EQ)) {
			if(condition.getType().equals(FilterCriterion.TYPE_LIST)) {
				return Restrictions.in(condition.getField(), (Collection<?>) value);
			} else {
				return Restrictions.eq(condition.getField(), value);
			}
		} else if(comparison.equals(FilterCriterion.COMPARISON_GT)) {
			return Restrictions.gt(condition.getField(), value);
		} else if(comparison.equals(FilterCriterion.COMPARISON_GE)) {
			return Restrictions.ge(condition.getField(), value);
		} else if(comparison.equals(FilterCriterion.COMPARISON_LT)) {
			return Restrictions.lt(condition.getField(), value);
		} else if(comparison.equals(FilterCriterion.COMPARISON_LE)) {
			return Restrictions.le(condition.getField(), value);
		} else if(comparison.equals(FilterCriterion.COMPARISON_NE)) {
			return Restrictions.ne(condition.getField(), value);
		} else if(comparison.equals(FilterCriterion.COMPARISON_BETWEEN)) {
			return Restrictions.between(condition.getField(), values.get(0), values.get(1));
		} else if(comparison.equals(FilterCriterion.COMPARISON_ISNULL)) {
			return Restrictions.isNull(condition.getField());
		} else if(comparison.equals(FilterCriterion.COMPARISON_ISNOTTNULL)) {
			return Restrictions.isNotNull(condition.getField());
		} else if(comparison.equals(FilterCriterion.COMPARISON_ISEMPTY)) {
			return Restrictions.isEmpty(condition.getField());
		} else if(comparison.equals(FilterCriterion.COMPARISON_ISNOTEMPTY)) {
			return Restrictions.isNotEmpty(condition.getField());
		} else if(comparison.equals(FilterCriterion.COMPARISON_LIKE)) {
			if(condition.getType().equals(FilterCriterion.TYPE_STRING)) {
				MatchMode mode = MatchMode.ANYWHERE;
				String format = condition.getFormat();
				if(format != null && !format.equals("")) {
					if(format.equals(FilterCriterion.LIKE_FORMAT_END)) {
						mode = MatchMode.END;
					} else if(format.equals(FilterCriterion.LIKE_FORMAT_START)) {
						mode = MatchMode.START;
					} else if(format.equals(FilterCriterion.LIKE_FORMAT_EXACT)) {
						mode = MatchMode.EXACT;
					}
				}

				return Restrictions.like(condition.getField(), (String) value, mode);
			}
		} else if(comparison.equals(FilterCriterion.COMPARISON_IN)) {
			return Restrictions.in(condition.getField(), values.toArray());
		}

		logger.error("condition 错误 : " + JSONEncoder.encode(condition));
		return null;
	}

	private static Object pareseValue(FilterCriterion condition, Object value) {
		Object v = value;
		if(value != null) {
			if(value instanceof String
					&& (condition.getType().equals(FilterCriterion.TYPE_INT)
							|| condition.getType().equals(FilterCriterion.TYPE_FLOAT)
							|| condition.getType().equals(FilterCriterion.TYPE_LONG)
							|| condition.getType().equals(FilterCriterion.TYPE_DOUBLE))) {
				value = NumberUtils.createNumber((String) value);
			}
			if(condition.getType().equals(FilterCriterion.TYPE_INT)) {
				v = ((Number) value).intValue();
			} else if(condition.getType().equals(FilterCriterion.TYPE_FLOAT)) {
				v = ((Number) value).floatValue();
			} else if(condition.getType().equals(FilterCriterion.TYPE_LONG)) {
				v = ((Number) value).longValue();
			} else if(condition.getType().equals(FilterCriterion.TYPE_DOUBLE)) {
				v = ((Number) value).doubleValue();
			} else if(condition.getType().equals(FilterCriterion.TYPE_ENUM)) {
				v = EnumUtils.getEnumByName(condition.getFormat(), (String) v);
			}
		}
		return v;
	}

	private static Criterion parseExpressonDisjunction(Map<String, Object> obj) {

		Iterator<String> iter = obj.keySet().iterator();
		Disjunction dj = Restrictions.disjunction();
		while(iter.hasNext()) {
			String key = iter.next();
			Object cri = obj.get(key);
			Criterion subCri = parseExpresson(key, cri);
			if(subCri != null) {
				dj.add(subCri);
			}
		}
		return dj;
	}

	private static Criterion parseExpressonConjunction(Map<String, Object> obj) {
		Iterator<String> iter = obj.keySet().iterator();
		Conjunction cj = Restrictions.conjunction();
		while(iter.hasNext()) {
			String key = iter.next();
			Object cri = obj.get(key);
			Criterion subCri = parseExpresson(key, cri);
			if(subCri != null) {
				cj.add(subCri);
			}
		}
		return cj;
	}

	@SuppressWarnings("rawtypes")
	private static void parseExpressonExp(Map<String, Object> map, Junction c) {
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext()) {
			String key = (String) iter.next();
			Object obj = map.get(key);
			Criterion cri = parseExpresson(key, obj);
			if(cri != null) {
				c.add(cri);
			}
		}
	}

	public static Criterion parseCriteria(Map<String, Object> crias) {
		Conjunction cj = Restrictions.conjunction();
		Iterator<String> iter = crias.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object obj = crias.get(key);
			if(key.equals(EXPRESSION_ALIAS)) {
				continue;
			}
			if(key.equals(EXPRESSION_EXP)) {
				parseExpressonExp((Map<String, Object>) obj, cj);
			} else {
				Criterion cri = parseExpresson(key, obj);
				if(cri != null) {
					cj.add(cri);
				}
			}
		}
		return cj;
	}
}
