package com.teemocode.commons.util.security;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.util.security.DES;

public class DESTest {
	@Test
	public void testEncrypt() throws Exception {
		String inputStr = "admin";
		System.err.println("原文:\t" + inputStr);

		System.err.println("密钥:\t" + DES.initKey());

		String encryptData = DES.encrypt(inputStr);
		System.err.println("加密后:\t" + encryptData);

		String decryptData = DES.decrypt(encryptData);
		System.err.println("解密后:\t" + decryptData);

		Assert.assertEquals(inputStr, decryptData);
	}
}
