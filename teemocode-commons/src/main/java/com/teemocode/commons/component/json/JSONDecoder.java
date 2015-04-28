package com.teemocode.commons.component.json;

import org.apache.commons.lang.StringUtils;

public class JSONDecoder {
	public static <T> T decode(String value) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		return new JSONTokenizer(value).parse();
	}
	
	public static void main(String[] args) {
		printResult("[1,2,6,8]");
		printResult("[\"350600\",\"350200\"]");
		printResult("[]");
	}
	
	private static void printResult(String value) {
		Object result = JSONDecoder.decode(value);
		System.out.println(value + " -> " + result.getClass() + ": " + result);
	}
}
