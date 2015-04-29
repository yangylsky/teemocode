package tk.teemocode.web.util;

import java.util.HashMap;
import java.util.Map;

public class WebBaseUtil {
	public static final String Fun_Menu = "fun_menu";

	public static final String Key_User = "k_user";

	public static final String Remember_Login_Cookie_Name = "r_v";

	/**
	 * 2周内自动登录
	 */
	public static final int Remember_Login_Mode_1 = 101;

	/**
	 * 1月内自动登录
	 */
	public static final int Remember_Login_Mode_2 = 102;

	/**
	 * 对于同一个用户名，某些方法需要同步锁，如创建UserSession
	 * 格式<encryptUsername, lock>
	 */
	private static final Map<String, Object> UserLocks = new HashMap<>();

	/**
	 * 获得同步锁
	 * @param username
	 * @return
	 */
	public static Object getLock(String encryptUsername) {
		Object lock = UserLocks.get(encryptUsername);
		if(lock == null) {
			synchronized(UserLocks) {
				lock = new Object();
				UserLocks.put(encryptUsername, lock);
			}
		}
		return lock;
	}

	/**
	 * 移除同步锁
	 * @param username
	 */
	public static void removeLock(String encryptUsername) {
		Object lock = UserLocks.get(encryptUsername);
		if(lock != null) {
			synchronized(UserLocks) {
				UserLocks.remove(encryptUsername);
			}
		}
	}
}
