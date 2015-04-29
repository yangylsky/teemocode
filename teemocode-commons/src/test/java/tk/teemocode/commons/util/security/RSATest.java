package tk.teemocode.commons.util.security;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import tk.teemocode.commons.util.security.RSA;

public class RSATest {
	private String publicKey;

	private String privateKey;

	@BeforeTest
	public void setUp() throws Exception {
		Map<String, Object> keyMap = RSA.initKey();

		publicKey = RSA.getPublicKey(keyMap);
		privateKey = RSA.getPrivateKey(keyMap);
		System.err.println("公钥: \n\r" + publicKey);
		System.err.println("私钥： \n\r" + privateKey);
	}

	@Test
	public void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String inputStr = "13800008888";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RSA.encryptByPublicKey(data, publicKey);
		System.err.println("加密后: " + new String(encodedData));
		byte[] decodedData = RSA.decryptByPrivateKey(encodedData, privateKey);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		Assert.assertEquals(inputStr, outputStr);
	}

	@Test
	public void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RSA.encryptByPrivateKey(data, privateKey);
		System.err.println("加密后: " + new String(encodedData));
		byte[] decodedData = RSA.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		Assert.assertEquals(inputStr, outputStr);

		System.err.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = RSA.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = RSA.verify(encodedData, publicKey, sign);
		System.err.println("状态:\r" + status);
		Assert.assertTrue(status);
	}
}
