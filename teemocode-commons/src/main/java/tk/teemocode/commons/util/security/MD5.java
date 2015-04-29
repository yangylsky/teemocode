package tk.teemocode.commons.util.security;

public class MD5 extends Encryptor {
	public static String crypt(String str) {
		if(str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}

		byte[] hash = encryptMD5(str.getBytes());
		String hexString = "";
		for(byte element : hash) {
			if((0xff & element) < 0x10) {
				hexString += "0" + Integer.toHexString((0xFF & element));
			} else {
				hexString += Integer.toHexString(0xFF & element);
			}
		}

		return hexString;
	}
}
