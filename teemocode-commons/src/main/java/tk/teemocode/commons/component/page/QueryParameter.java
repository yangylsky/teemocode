package tk.teemocode.commons.component.page;

import org.apache.commons.lang.StringUtils;

/**
 * 封装分页和排序查询请求参数.
 */
public class QueryParameter {
	public static final String ASC = "ASC";

	public static final String DESC = "DESC";

	protected String sort = "";

	protected String dir = "";

	/**
	 * ES查询指定的索引范围
	 */
	protected String[] indexNames;

	/**
	 * ES查询指定的type范围
	 */
	protected String[] typeNames;

	/**
	 * 查询返回的字段
	 */
	protected String[] fields;

	/**
	 * 查询条件
	 */
	protected String queryExpression;

	/**
	 * 当queryExpression为hql时，paramValues为对应参数的值
	 */
	protected Object[] paramValues;

	/**
	 * 获得排序字段,无默认值.
	 */
	public String getOrderBy() {
		return sort;
	}

	public void setOrderBy(String orderBy) {
		sort = orderBy;
	}

	/**
	 * 是否已设置排序字段.
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(sort);
	}

	/**
	 * 获得排序方向,默认为asc.
	 */
	public String getOrder() {
		return dir;
	}

	/**
	 * 设置排序方式向.
	 * @param order
	 *        可选值为desc或asc.
	 */
	public void setOrder(String order) {
		if(ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) {
			dir = order.toUpperCase();
		} else {
			throw new IllegalArgumentException("order should be 'DESC' or 'ASC'");
		}
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return StringUtils.isBlank(dir) ? ASC : dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String[] getIndexNames() {
		return indexNames == null ? new String[0] : indexNames;
	}

	public void setIndexNames(String[] indexNames) {
		this.indexNames = indexNames;
	}

	public String[] getTypeNames() {
		return typeNames == null ? new String[0] : typeNames;
	}

	public void setTypeNames(String[] typeNames) {
		this.typeNames = typeNames;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public static String getDesc() {
		return DESC;
	}

	public String getQueryExpression() {
		return queryExpression;
	}

	public void setQueryExpression(String queryExpression) {
		this.queryExpression = queryExpression;
	}

	public boolean hasExpression() {
		return StringUtils.isNotBlank(queryExpression);
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}

}
