package tk.teemocode.commons.util;

import java.util.Random;

public class RandomUtil {
	private static Random RN = new Random(System.currentTimeMillis());

	private static long seed = RN.nextLong();

	public static String getUniqueID() {
		long l = RN.nextLong();

		synchronized(RandomUtil.class) {
			seed++;
			l ^= seed;
		}

		StringBuffer buf = new StringBuffer();
		String tmp = Long.toHexString(l);
		int len = 16 - tmp.length();
		for(int i = 0; len > 0 && i < len; i++) {
			buf.append("0");
		}
		buf.append(tmp);
		return buf.toString();
	}
}
