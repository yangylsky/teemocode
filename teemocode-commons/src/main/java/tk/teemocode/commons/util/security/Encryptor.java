package tk.teemocode.commons.util.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public abstract class Encryptor {
	public static final String KEY_SHA = "SHA";

	public static final String KEY_MD5 = "MD5";

	/**
	 * MAC算法可选以下多种算法
	 *
	 * <pre>
	 * HmacMD5
	 * HmacSHA1
	 * HmacSHA256
	 * HmacSHA384
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * BASE64解密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) {
		try {
			return (new BASE64Decoder()).decodeBuffer(key);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * BASE64加密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * MD5加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
			md5.update(data);
			return md5.digest();
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SHA加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) {
		try {
			MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(data);
			return sha.digest();
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化HMAC密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
			SecretKey secretKey = keyGenerator.generateKey();
			return encryptBASE64(secretKey.getEncoded());
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * HMAC加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) {
		try {
			SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return mac.doFinal(data);
		} catch(InvalidKeyException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
