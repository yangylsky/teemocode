package tk.teemocode.commons.util.security;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

import tk.teemocode.commons.util.security.Encryptor;

public class EncryptorTest {
	@Test
	public void test() throws Exception {
		String inputStr = "简单加密";
		System.err.println("原文:\n\t" + inputStr);

		byte[] inputData = inputStr.getBytes();
		String code = Encryptor.encryptBASE64(inputData);

		System.err.println("\nBASE64加密后:\n\t" + code);

		byte[] output = Encryptor.decryptBASE64(code);

		String outputStr = new String(output);

		System.err.println("BASE64解密后:\n\t" + outputStr);

		// 验证BASE64加密解密一致性
		Assert.assertEquals(inputStr, outputStr);

		// 验证MD5对于同一内容加密是否一致
		Assert.assertEquals(Encryptor.encryptMD5(inputData), Encryptor.encryptMD5(inputData));

		// 验证SHA对于同一内容加密是否一致
		Assert.assertEquals(Encryptor.encryptSHA(inputData), Encryptor.encryptSHA(inputData));

		String key = Encryptor.initMacKey();
		System.err.println("\nMac密钥:\n" + key);

		// 验证HMAC对于同一内容，同一密钥加密是否一致
		Assert.assertEquals(Encryptor.encryptHMAC(inputData, key), Encryptor.encryptHMAC(inputData, key));

		BigInteger md5 = new BigInteger(Encryptor.encryptMD5(inputData));
		System.err.println("MD5:\n" + md5.toString(16));

		BigInteger sha = new BigInteger(Encryptor.encryptSHA(inputData));
		System.err.println("\nSHA:\n" + sha.toString(32));

		BigInteger mac = new BigInteger(Encryptor.encryptHMAC(inputData, inputStr));
		System.err.println("\nHMAC:\n" + mac.toString(16));
	}
}
