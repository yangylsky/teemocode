package com.teemocode.commons.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class ResourceHelper {
	private static final Logger logger = LoggerFactory.getLogger(ResourceHelper.class);

	public static final String Default_Encoding = "utf-8";

	private static Map<String, Properties> propertiesCache = new HashMap<String, Properties>();

	public static Resource[] getResources(String location) {
		Assert.notNull(location, "location must not be null");
		ResourcePatternResolver resourceLoader = null;
		Resource[] resources = null;
		try {
			resourceLoader = new PathMatchingResourcePatternResolver();
			resources = resourceLoader.getResources(location);
		} catch(Exception e) {
			logger.error("getResources(" + location + ") error!", e);
			resources = null;
		}
		return resources;
	}

	public static Resource getSingleResource(String location) {
		Resource[] resources = getResources(location);
		if(resources != null && resources.length == 1) {
			return resources[0];
		} else {
			logger.error("getSingleResource error[resources=" + resources + ";" + resources != null ? ("resources.length="
					+ resources.length + ";") : "");
			return null;
		}
	}

	public static InputStream getInputStream(String location) {
		try {
			return ResourceHelper.getSingleResource(location).getInputStream();
		} catch(IOException e) {
			logger.error("getInputStream error", e);
			throw new RuntimeException(e);
		}
	}

	public static String readResource(String location) {
		return readResource(location, Default_Encoding);
	}

	public static String readResource(String location, String encoding) {
		return readResource(getSingleResource(location), encoding);
	}

	public static String readResource(Resource resource, String encoding) {
		Assert.notNull(resource, "resource must not be null");
		StringBuffer resourcetext = new StringBuffer();
		InputStreamReader isr = null;
		char[] buffer = new char[4096];
		try {
			isr = new InputStreamReader(resource.getInputStream(), encoding);
			int len;
			while((len = isr.read(buffer)) != -1) {
				resourcetext.append(buffer, 0, len);
			}
		} catch(Exception e) {
			logger.error("", e);
		} finally {
			try {
				if(isr != null) {
					isr.close();
				}
			} catch(Exception e) {
				logger.error("", e);
			}
		}
		return resourcetext.toString();
	}

	public static File createNewFile(String filename) {
		File fp = new File(filename);
		if(fp.exists()) {
			return fp;
		}
		if(!fp.getParentFile().exists()) {
			createNewFile(fp.getParentFile().getAbsolutePath() + File.separator);
		}
		try {
			if(fp.isDirectory()) {
				fp.mkdir();
			} else if(fp.isFile()) {
				fp.createNewFile();
			} else if(filename.trim().endsWith(File.separator)) {
				fp.mkdir();
			}
		} catch(IOException e) {
			logger.error("", e);
			return null;
		}
		return fp;
	}

	public static Properties loadProperties(String location) {
		Properties properties = propertiesCache.get(location);
		if(properties == null) {
			Resource r = getSingleResource(location);
			properties = new Properties();
			propertiesCache.put(location, properties);

			InputStream is = null;
			InputStreamReader isr = null;
			try {
				is = r.getInputStream();
				isr = new InputStreamReader(is, "utf-8");
				properties.load(isr);
			} catch(IOException e) {
				logger.error("", e);
			} finally {
				if(isr != null) {
					try {
						isr.close();
					} catch(IOException e) {
						logger.error("", e);
					}
				}
				if(is != null) {
					try {
						is.close();
					} catch(IOException e) {
						logger.error("", e);
					}
				}
			}
		}
		return properties;
	}

	public static String correctWinPath(String winPath) {
		return winPath.replace('\\', File.separatorChar);
	}
}
