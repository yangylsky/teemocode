package com.teemocode.commons.component.page;

public class FilterLogic {
	public static String LOGIC_OR = "OR";
	public static String LOGIC_AND = "AND";
	private Object[][] fields;
	private String[] logic;
	public String[] getLogic() {
		return logic;
	}
	public void setLogic(String[] logic) {
		this.logic = logic;
	}
	public Object[][] getFields() {
		return fields;
	}
	public void setFields(Object[][] fields) {
		this.fields = fields;
	}
}
