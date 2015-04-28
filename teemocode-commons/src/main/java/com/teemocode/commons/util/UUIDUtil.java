package com.teemocode.commons.util;

import java.util.UUID;

public class UUIDUtil {
	public static String generate() {
		return generate(false);
	}

	public static String generate(boolean withSeparator) {
		String uuid = UUID.randomUUID().toString();
		if(!withSeparator) {
			uuid = uuid.replace("-", "");
		}
		return uuid;
	}

	public static void main(String[] args) {
		for(int i = 0; i < 100; i++) {
			System.out.println(UUIDUtil.generate());
		}
		for(int i = 0; i < 100; i++) {
			System.out.println(UUIDUtil.generate(true));
		}
	}
}
