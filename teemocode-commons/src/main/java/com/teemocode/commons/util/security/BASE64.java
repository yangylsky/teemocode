package com.teemocode.commons.util.security;

public class BASE64 extends Encryptor {
	public static String encodeBase64(String str)  {
        return encryptBASE64(str.getBytes());
    }

	public static String decodeBase64(String str) {
        return new String(decryptBASE64(str));
    }
}
