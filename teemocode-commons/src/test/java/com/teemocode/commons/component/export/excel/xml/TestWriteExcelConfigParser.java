package com.teemocode.commons.component.export.excel.xml;

import java.util.Map;

import com.teemocode.commons.component.export.excel.xml.WriteExcelConfigParser;

public class TestWriteExcelConfigParser {
	public static void main(String[] args) {
		WriteExcelConfigParser parser = new WriteExcelConfigParser();
		String configFileName = "C:/temp/testwriteexcel.xml";
		Map<String, Object> map = parser.load(configFileName);
		System.out.print(map);
	}
}
