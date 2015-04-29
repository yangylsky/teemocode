package tk.teemocode.module.index.convertor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.module.base.bo.IBO;
import tk.teemocode.module.util.SystemEnv;

@SuppressWarnings("rawtypes")
public class ConvertorCache {
	private static final Logger logger = LoggerFactory.getLogger(ConvertorCache.class);

	private static Map<Class<IBO>, Set<IndexItemConvertor>> _convertorCache = new HashMap<>();

	private static boolean inited = false;

	@SuppressWarnings("unchecked")
	public static void init() {
		if(!inited) {
			try {
				Properties indexConvertorProps = SystemEnv.getBean("indexConvertorProps");
				for(String boClassName : indexConvertorProps.stringPropertyNames()) {
					Class<IBO> boClass = (Class<IBO>) ClassUtils.getClass(boClassName);
					String s = indexConvertorProps.getProperty(boClassName);
					for(String ss : StringUtils.split(s, '|')) {
						if(StringUtils.isNotBlank(ss)) {
							Class<IndexItemConvertor> convertorClass = (Class<IndexItemConvertor>) ClassUtils.getClass(ss);
							addConvertor(boClass, convertorClass.newInstance());
						}
					}
				}
				inited = true;
			} catch(Exception e) {
				logger.error("ConvertorCache初始化失败!", e);
				//				throw new ProjectException();
			}
		}
	}

	public static void reload() {
		inited = false;
		init();
	}

	public static synchronized void addConvertor(Class<IBO> boClass, IndexItemConvertor convertor) {
		Set<IndexItemConvertor> convertors = getConvertor(boClass);
		if(convertors == null) {
			convertors = new HashSet<>();
		}
		convertors.add(convertor);
		_convertorCache.put(boClass, convertors);
	}

	public static <B extends IBO> Set<IndexItemConvertor> getConvertor(Class<B> boClass) {
		Set<IndexItemConvertor> convertors = _convertorCache.get(boClass);
		if(convertors == null) {
			return new HashSet<>();
		}
		return convertors;
	}

	public static Set<Class<IBO>> getIndexableKeys() {
		return _convertorCache.keySet();
	}

	public static synchronized void clear() {
		_convertorCache.clear();
	}
}
