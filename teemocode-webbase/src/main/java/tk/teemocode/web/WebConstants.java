package tk.teemocode.web;

public class WebConstants {
	public static final String UPLOADFILE = "uploadFile";
	public static final String SENDFILE = "sendFile";
	public static final String MIME_TYPE = "mime";
	public static final String CHARSET = "charset";
	
	/**
	 * cookie中的JSESSIONID名称
	 */
	public static final String JSESSION_COOKIE = "JSESSIONID";
	/**
	 * url中的jsessionid名称
	 */
	public static final String JSESSION_URL = "jsessionid";
	/**
	 * HTTP POST请求
	 */
	public static final String POST = "POST";
	/**
	 * HTTP GET请求
	 */
	public static final String GET = "GET";
	
	/**
	 * SESSION 里的userSession名称
	 */
	public static final String USERSESSION = "userSession";
	
	/**
	 *  为了方便测试不启用权限控制
	 */
	public static boolean ACCESS_CONTROL = true;
}
