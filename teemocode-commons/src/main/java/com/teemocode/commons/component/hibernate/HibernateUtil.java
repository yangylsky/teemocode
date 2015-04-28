package com.teemocode.commons.component.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.teemocode.commons.util.bean.IdObject;
import com.teemocode.commons.util.reflect.ReflectionUtils;

public class HibernateUtil {
	public static final int Query_Hql = 1;

	public static final int Query_Sql = 2;

	public static String removeRedundancy(String hsql) {
		String[] redundancyWords = new String[] {"order by", "group by"};
		if(hsql != null) {
			for(String s : redundancyWords) {
				int index = hsql.indexOf(s);
				if(index > 0) {
					String tmphql = hsql.substring(0, index - 1);
					tmphql = removeEndRedundancy(tmphql);
					hsql = tmphql + " " + hsql.substring(index);
				}
			}
		}
		hsql = removeEndRedundancy(hsql);
		return hsql;
	}

	public static String removeEndRedundancy(String hsql) {
		String[] redundancyWords = new String[] {",", " and", " or", " where"};
		if(hsql != null) {
			hsql = hsql.trim();
			for(String s : redundancyWords) {
				if(hsql.trim().endsWith(s)) {
					hsql = hsql.substring(0, hsql.length() - s.length()) + " ";
				}
			}
		}
		return hsql;
	}

	/**
	 * 获取被CGLib包装的代理类的实际类型
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <B> Class<B> getEntityClass(Class<B> clazz) {
		if(Enhancer.isEnhanced(clazz)) {
			return (Class<B>) clazz.getGenericSuperclass();
		} else {
			return clazz;
		}
	}

	/**
	 *
	 * @param type - 1: HQL, 2: SQL
	 * @param queryText
	 * @param session
	 * @return
	 */
	public static String[] getQueryFieldNames(int type, String queryText, Session session) {
		Query q = null;
		if(type == Query_Hql) {
			q = session.createQuery(queryText);
		} else if(type == Query_Sql) {
			q = session.createSQLQuery(queryText);
		}
		return q.getReturnAliases();
	}

	public static String hqlIn(String prefix, int[] values, List<Object> params) {
		return hqlIn(prefix, ArrayUtils.toObject(values), params);
	}

	public static String hqlIn(String prefix, long[] values, List<Object> params) {
		return hqlIn(prefix, ArrayUtils.toObject(values), params);
	}

	public static String hqlIn(String prefix, Object[] values, List<Object> params) {
		String hql = " ";
		if(!ArrayUtils.isEmpty(values)) {
			hql += prefix + " in(?";
			if(params != null) {
				params.add(values[0]);
			}
			for(int i = 1; i < values.length; i++) {
				hql += ",?";
				if(params != null) {
					params.add(values[i]);
				}
			}
			hql += ") ";
		}
		return hql + "and ";
	}

	public static String hqlWithinDate(String field, int days) {
		return " datediff(str_to_date(concat(year(curdate()), ' ', month(" + field + "), ' ', day(" + field +
				")), '%Y %m %d'), curdate()) BETWEEN 0 AND " + days + " and ";
	}

	protected String hqlBetweenDate(String field) {
		return "(" + field + ">=? and " + field + "<=?)";
	}

	/**
	 * 根据对象ID集合, 整理合并集合.
	 *
	 * 页面发送变更后的子对象id列表时,在Hibernate中删除整个原来的子对象集合再根据页面id列表创建一个全新的集合这种看似最简单的做法是不行的.
	 * 因此采用如此的整合算法：在源集合中删除id不在目标集合中的对象,根据目标集合中的id创建对象并添加到源集合中.
	 * 因为新建对象只有ID被赋值, 因此本函数不适合于cascade-save-or-update自动持久化子对象的设置.
	 *
	 * @param srcObjects 源集合,元素为对象.
	 * @param checkedIds  目标集合,元素为ID.
	 * @param clazz  集合中对象的类型,必须为IdEntity子类
	 */
	public static <T extends IdObject> void mergeByCheckedIds(final Collection<T> srcObjects,
			final Collection<Long> checkedIds, final Class<T> clazz) {
		//目标集合为空, 删除源集合中所有对象后直接返回.
		if(checkedIds == null) {
			srcObjects.clear();
			return;
		}

		//遍历源对象集合,如果其id不在目标ID集合中的对象删除.
		//同时,在目标集合中删除已在源集合中的id,使得目标集合中剩下的id均为源集合中没有的id.
		Iterator<T> srcIterator = srcObjects.iterator();
		try {

			while (srcIterator.hasNext()) {
				T element = srcIterator.next();
				Long id = element.getId();

				if (!checkedIds.contains(id)) {
					srcIterator.remove();
				} else {
					checkedIds.remove(id);
				}
			}

			//ID集合目前剩余的id均不在源集合中,创建对象,为id属性赋值并添加到源集合中.
			for (Long id : checkedIds) {
				T element = clazz.newInstance();
				element.setId(id);
				srcObjects.add(element);
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 获取类名的缩写值(取大写字母)，最多3个字符
	 * @param clazz
	 * @return
	 */
	public static String getAbbreviation(Class<?> clazz) {
		return getAbbreviation(HibernateUtil.getEntityClass(clazz).getSimpleName());
	}

	/**
	 * 获取字符串的缩写值(取大写字母)，最多3个字符
	 * @param text
	 * @return
	 */
	public static String getAbbreviation(String text) {
		String s = "";
		for(int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(ch >= 'A' && ch <= 'Z') {
				s += ch;
			}
		}
		return StringUtils.substring(StringUtils.isBlank(s) ? text : s, 0, 3);
	}
}
