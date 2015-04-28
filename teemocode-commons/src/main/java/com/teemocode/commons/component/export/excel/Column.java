package com.teemocode.commons.component.export.excel;

import org.apache.commons.lang.StringUtils;

public class Column {
	private String name;

	private Type type;

	private boolean isMapKey = false;

	private String mapKeyValue;

	public Column() {
	}

	public Column(final String name, final Type type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isMapKey() {
		return isMapKey;
	}

	public void setMapKey(boolean isMapKey) {
		this.isMapKey = isMapKey;
	}

	public String getMapKeyValue() {
		return mapKeyValue;
	}

	public void setMapKeyValue(String mapKeyValue) {
		this.mapKeyValue = mapKeyValue;
	}

	public boolean isValidColumn() {
		return StringUtils.isNotEmpty(name) || (isMapKey && StringUtils.isNotEmpty(mapKeyValue));
	}
}
