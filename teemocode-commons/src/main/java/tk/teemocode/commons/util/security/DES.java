package tk.teemocode.commons.util.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 实现DES对称可逆加密，明文经DES加密后再用Base64编码一遍后输出，解密反之
 * @author yangylsky
 *
 */
public class DES extends Encryptor {
	public static final String DefaultSeed = "QSCGYj1r2y3q4157687~!@#&";

	/**
	 * ALGORITHM 算法 <br>
	 * 可替换为以下任意一种算法，同时key值的size相应改变。
	 *
	 * <pre>
	 * DES                  key size must be equal to 56
	 * DESede(TripleDES)    key size must be equal to 112 or 168
	 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
	 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
	 * RC2                  key size must be between 40 and 1024 bits
	 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
	 * </pre>
	 *
	 * 在Key toKey(byte[] key)方法中使用下述代码 <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换 <code>
	 * DESKeySpec dks = new DESKeySpec(key);
	 * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
	 * SecretKey secretKey = keyFactory.generateSecret(dks);
	 * </code>
	 */
	public static final String ALGORITHM = "DES";

	public static String encrypt(String source) {
		return encrypt(source, DefaultSeed);
	}

	public static String encrypt(String source, String seed) {
		return encryptBASE64(encrypt(source.getBytes(), initKey(seed)));
	}

	public static String decrypt(String encryptData) {
		return decrypt(encryptData, DefaultSeed);
	}

	public static String decrypt(String encryptData, String seed) {
		return new String(decrypt(decryptBASE64(encryptData), initKey(seed)));
	}

	/**
	 * 转换密钥<br>
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) {
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
			// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

			return secretKey;
		} catch(InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String key) {
		try {
			Key k = toKey(decryptBASE64(key));
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, k);

			return cipher.doFinal(data);
		} catch(NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) {
		try {
			Key k = toKey(decryptBASE64(key));

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, k);

			return cipher.doFinal(data);
		} catch(InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
				| NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 生成密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static String initKey() {
		return initKey(DefaultSeed);
	}

	/**
	 * 生成密钥
	 *
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String initKey(String seed) {
		SecureRandom secureRandom = null;

		if(seed != null) {
			secureRandom = new SecureRandom(decryptBASE64(seed));
		} else {
			secureRandom = new SecureRandom();
		}

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(secureRandom);

			SecretKey secretKey = keyGenerator.generateKey();

			return encryptBASE64(secretKey.getEncoded());
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
