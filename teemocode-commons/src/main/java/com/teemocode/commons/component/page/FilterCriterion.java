package com.teemocode.commons.component.page;

import java.util.List;

import org.hibernate.Criteria;

public class FilterCriterion implements Criterion {
	private static final long serialVersionUID = -5504774647458205551L;
	public static final String COMPARISON_EQ = "eq";//等于
	public static final String COMPARISON_GT = "gt";//大于
	public static final String COMPARISON_LT = "lt";//小于
	public static final String COMPARISON_IN = "in";//in
	public static final String COMPARISON_GE = "ge";//大于等于
	public static final String COMPARISON_LE = "le";//小于等于
	public static final String COMPARISON_NE = "ne";//不等于
	public static final String COMPARISON_LIKE = "like";//in
	public static final String COMPARISON_BETWEEN = "between";//between;
	public static final String COMPARISON_ISNULL = "isnull";//为null;
	public static final String COMPARISON_ISNOTTNULL = "isnotnull";//不为null
	public static final String COMPARISON_ISEMPTY = "isempty";//未空或者null
	public static final String COMPARISON_ISNOTEMPTY = "isnotempty";//不为空或者null
	public static final String TYPE_STRING = "string";
	public static final String TYPE_NUMBER = "number";
	public static final String TYPE_INT = "int";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_LONG = "long";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_BOOLEN = "boolean";
	public static final String TYPE_LIST = "list";
	public static final String TYPE_VARIABLE = "variable";
	public static final String TYPE_EXPRESSION = "expression";
	public static final String TYPE_ENUM = "enum";
	
	public static final String LIKE_FORMAT_ANYWHERE= "anywhere";
	public static final String LIKE_FORMAT_END= "end";
	public static final String LIKE_FORMAT_EXACT= "exact";
	public static final String LIKE_FORMAT_START= "start";
	
	private String field;
	private List<Object> value;
	private String comparison;
	private String type;
	private String format;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public String getComparison() {
		return comparison;
	}
	public void setComparison(String comparison) {
		this.comparison = comparison;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public Criteria getCriteria() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getHql() {
		return null;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public List<Object> getValue() {
		return value;
	}
	public void setValue(List<Object> value) {
		this.value = value;
	}

}
