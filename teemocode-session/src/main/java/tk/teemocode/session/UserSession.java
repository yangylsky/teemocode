package tk.teemocode.session;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import tk.teemocode.module.identity.UserInfo;
import tk.teemocode.module.util.SystemEnv;

public class UserSession implements Serializable, Cloneable {
	private static final long serialVersionUID = 4328587921325361357L;

	/**
	 * userSession 唯一标识
	 */
	private String sessionId = null;

	/**
	 * 用户 userName
	 */
	private String userName = null;

	/**
	 * 用户完整信息
	 */
	private UserInfo user = null;

	/**
	 * 用户变量
	 */
	private Map<String, Serializable> variables = null;

	/**
	 * 用户拥有的所有权限(权限uuid)
	 */
	private List<String> auths = null;

	/**
	 * 用户拥有的所有模块（模块uuid）
	 */
	private List<String> modules = null;

	/**
	 * 用户拥有的可访问的URL
	 */
	private List<String> urls = null;

	/**
	 * 用户拥有的所有角色
	 */
	private List<String> roles = null;

	/**
	 * 用户配置
	 */
	private Map<String, String> userConfig = null;

	/**
	 * 用户参数
	 */
	private Map<String, Serializable> paramsMap = null;

	public UserSession(String sessionId, UserInfo user) {
		this.sessionId = sessionId;
		this.userName = user.getName();
		this.user = user;
		init(user);
	}

	public void init(UserInfo userInfo) {
		variables = new LinkedHashMap<String, Serializable>();
		paramsMap = new Hashtable<String, Serializable>();
		auths = new ArrayList<String>();
		roles = new ArrayList<String>();
		modules = new ArrayList<String>();
		urls = new ArrayList<String>();
	}

	public void initUserTempFileDirecotry() {
		try {
			String direcotry = "";
			File fileDirecotry = new File(direcotry);
			if(!fileDirecotry.exists()) {
				fileDirecotry.mkdir();
			}
		} catch(Exception e) {
		}
	}

	public void destroyed() {
		user = null;
		if(variables != null) {
			variables.clear();
			variables = null;
		}

		if(auths != null) {
			auths.clear();
			auths = null;
		}

		if(roles != null) {
			roles.clear();
			roles = null;
		}

		if(modules != null) {
			modules.clear();
			modules = null;
		}

		if(urls != null) {
			urls.clear();
			urls = null;
		}

		if(paramsMap != null) {
			paramsMap.clear();
			paramsMap = null;
		}

	}

	public Map<String, Serializable> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Serializable> variables) {
		this.variables = variables;
	}

	public void addVariable(String key, Serializable value) {
		variables.put(key, value);
	}

	public void removeVariable(String key) {
		variables.remove(key);
	}

	public Map<String, Serializable> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Serializable> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public void addParam(String key, Serializable value) {
		paramsMap.put(key, value);
	}

	public void removeParam(String key) {
		paramsMap.remove(key);
	}

	public String getUserTempPath() throws IOException {
		String tempPath = SystemEnv.getInstance().getTempPath();
		tempPath = tempPath + File.separator + getUserName();
		File file = new File(tempPath);
		FileUtils.forceMkdir(file);
		return tempPath;
	}

	public Map<String, String> getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(Map<String, String> userConfig) {
		this.userConfig = userConfig;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public UserInfo getUser() {
		return user;
	}

	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
}
