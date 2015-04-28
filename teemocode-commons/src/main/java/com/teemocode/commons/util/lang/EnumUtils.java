package com.teemocode.commons.util.lang;

public class EnumUtils {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Enum getEnumByName(String className, String enumName) {
		try {
			Class enumClass = Class.forName(className);
			return Enum.valueOf(enumClass, enumName);
		} catch(ClassNotFoundException e) {
			throw new IllegalArgumentException("未找到类:" + className);
		}
	}

	public static ValuableEnum getEnumByValue(String className, String enumValue) {
		try {
			Class<?> enumClass = Class.forName(className);
			ValuableEnum[] enums = (ValuableEnum[]) enumClass.getEnumConstants();
			for(ValuableEnum  e : enums) {
				if(e.value().equals(enumValue)) {
					return e;
				}
			}
			throw new IllegalArgumentException("未找到Enum[className=" + className + ", enumValue=" + enumValue + "]");
		} catch(ClassNotFoundException e) {
			throw new IllegalArgumentException("未找到类:" + className);
		}
	}
}
