package com.teemocode.commons.component.export.excel;

public class Type {
	public static final String DATA_TYPE_OBJECT = "object";
	
	public static final String DATA_TYPE_STRING = "string";
	
	public static final String DATA_TYPE_INTEGER = "integer";
	
	public static final String DATA_TYPE_DATE = "date";
	
	public static final String DATA_TYPE_LONG = "long";
	
	public static final String DATA_TYPE_DOUBLE = "double";
	
	public static final String DATA_TYPE_FLOAT = "float";
	
	public static final String DATA_TYPE_BOOLEAN = "boolean";
	
	private String dataType = DATA_TYPE_STRING;
	
	private String name = null;
	
	private String className = null;
	
	private String subDataType = null;
	
	private String format = null;
	
	public Type() {
	}
	
	public Type(String dataType, String name, String className, String format) {
		this.dataType = dataType;
		this.name = name;
		this.className = className;
		this.format = format;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getSubDataType() {
		return subDataType;
	}
	
	public void setSubDataType(String subDataType) {
		this.subDataType = subDataType;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
}
