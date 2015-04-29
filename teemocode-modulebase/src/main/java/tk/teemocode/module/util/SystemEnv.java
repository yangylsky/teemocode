package tk.teemocode.module.util;

import java.util.Locale;

import org.springframework.context.ApplicationContext;

public class SystemEnv {
	private static SystemEnv systemEnv = new SystemEnv();

	private String superPwd;

	private boolean initConvertorCache = false;

	private String contextPath = "/";

	private String homePath = "";

	private String tempPath = "";

	private String dateFormat = "yyyy-MM-dd";

	private String timeFormat = "yyyy-MM-dd HH:mm:ss";

	private String encoding = "utf-8";

	private Locale locale = null;

	private ApplicationContext springCtx = null;

	private SystemEnv() {
	}

	public static SystemEnv getInstance() {
		return systemEnv;
	}

	public void destroyed() {
	}

	public String getSuperPwd() {
		return superPwd;
	}

	public void setSuperPwd(String superPwd) {
		this.superPwd = superPwd;
	}

	public boolean isInitConvertorCache() {
		return initConvertorCache;
	}

	public void setInitConvertorCache(boolean initConvertorCache) {
		this.initConvertorCache = initConvertorCache;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public ApplicationContext getSpringCtx() {
		return springCtx;
	}

	public void setSpringCtx(ApplicationContext springCtx) {
		this.springCtx = springCtx;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) SystemEnv.getInstance().getSpringCtx().getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBeansOfType(Class<T> clazz) {
		return (T) SystemEnv.getInstance().getSpringCtx().getBeansOfType(clazz);
	}
}
