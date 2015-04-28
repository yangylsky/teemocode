package com.teemocode.commons.util.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teemocode.commons.exception.ReflectException;
import com.teemocode.commons.util.DateUtil;

public class PojoUtil {
	private static final Logger logger = LoggerFactory.getLogger(PojoUtil.class);

	public static List<Object> mapListToBeanList(List<Map<String, ?>> mapList, Class<?> cls) {
		List<Object> list = new ArrayList<Object>();
		for(Map<String, ?> map : mapList) {
			Object obj = mapToBean(map, cls);
			list.add(obj);
		}
		return list;
	}

	public static <T> T mapToBean(Map<String, ?> map, Class<T> cls) {
		if(logger.isDebugEnabled()) {
			logger.debug(map.toString());
		}
		T obj;
		try {
			obj = cls.newInstance();
		} catch(Exception e) {
			logger.error(map.toString());
			return null;
		}
		Iterator<?> names = map.keySet().iterator();
		while(names.hasNext()) {
			String name = (String) names.next();
			if(PropertyUtils.isWriteable(obj, name)) {
				Object value = map.get(name);
				try {
					if(value instanceof String && ReflectionUtils.getDeclaredField(cls, name).getType() == java.util.Date.class) {
						String aMask = DateUtil.getDatePattern();
						value = DateUtil.convertStringToDate(aMask, String.valueOf(value));
					}
					BeanUtils.copyProperty(obj, name, value);
				} catch(Exception e) {
					if(logger.isDebugEnabled()) {
						logger.error("", e);
					}
				}
			}
		}

		return obj;
	}

	public static void mapToObj(Map<?, ?> map, Object obj) {
		if(logger.isDebugEnabled()) {
			logger.debug(map.toString());
		}
		Iterator<?> names = map.keySet().iterator();
		while(names.hasNext()) {
			String name = (String) names.next();
			if(PropertyUtils.isWriteable(obj, name)) {
				Object value = map.get(name);
				try {
					BeanUtils.copyProperty(obj, name, value);
				} catch(Exception e) {
					if(logger.isDebugEnabled()) {
						logger.error("", e);
					}
				}
			}
		}
	}

	public static void objToMap(Object obj, Map<String, Object> map) {
		PojoUtil.objToMap(obj, map, false);
	}

	public static void objToMap(Object obj, Map<String, Object> map, boolean nullValuetoBlank) {
		if(map == null) {
			map = new HashMap<String, Object>();
		}
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(obj);
		for(PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			try {
				if(descriptor.getReadMethod() != null) {
					map.put(name, PropertyUtils.getProperty(obj, name));
				}
			} catch(NullPointerException | IllegalArgumentException e) {
				if(nullValuetoBlank) {
					map.put(name, "");
				}
			} catch(Exception e) {
				if(logger.isDebugEnabled()) {
					logger.error("", e);
				}
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug(map.toString());
		}
	}

	public static void mapToMap(Map<Object, Object> source, Map<Object, Object> destination) {
		Set<Object> set = source.keySet();
		Iterator<Object> iter = set.iterator();
		while(iter.hasNext()) {
			Object key = iter.next();
			Object value = source.get(key);
			destination.put(key, value);
		}
	}

	public static void valueToObj(String name, Object value, Object obj) {
		if(PropertyUtils.isWriteable(obj, name)) {
			try {
				BeanUtils.copyProperty(obj, name, value);
			} catch(Exception e) {
				if(logger.isDebugEnabled()) {
					logger.error("", e);
				}
			}
		}
	}

	public static boolean equals(Collection<?> a, Collection<?> b) {
		return Arrays.equals(a.toArray(new Object[] {}), b.toArray(new Object[] {}));
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> list(T... items) {
		Collection<T> list = new ArrayList<T>();
		for(T i : items) {
			list.add(i);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> set(T... items) {
		Set<T> set = new HashSet<T>();
		for(T i : items) {
			set.add(i);
		}
		return set;
	}

	public static <T> T get(Collection<T> items, int index) {
		int i = 0;
		for(Iterator<T> iterator = items.iterator(); iterator.hasNext(); i++) {
			if(i == index) {
				return iterator.next();
			}
		}
		return null;
	}

	public static <T> T get(Collection<T> items, T item) {
		for(T iterItem : items) {
			if(iterItem.equals(item)) {
				return iterItem;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> c, Class<T> k) {
		return c.toArray((T[]) Array.newInstance(k, 0));
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[] destObj, T[] srcObj, String... mergeFields) {
		if(destObj.length <= 0 || srcObj.length <= 0) {
			return srcObj;
		}

		Collection<T> destObjs = Arrays.asList(destObj);
		Collection<T> srcObjs = Arrays.asList(srcObj);
		Collection<T> result = merge(destObjs, srcObjs, mergeFields);
		return toArray(result, (Class<T>) srcObj[0].getClass());
	}

	public static <T> Collection<T> merge(Collection<T> destObj, Collection<T> srcObj, String... mergeFields) {
		if(destObj.size() <= 0 || srcObj.size() <= 0) {
			return srcObj;
		}
		for(int i = 0; i < srcObj.size(); i++) {
			T srcItem = get(srcObj, i);
			for(T toItem : destObj) {
				if(srcItem.equals(toItem)) {
					T result = merge(toItem, srcItem, mergeFields);
					srcItem = result;
				}
			}
		}
		return srcObj;
	}

	public static <T1, T2> T1 merge(T1 destObj, T2 srcObj) {
		return merge(destObj, srcObj, "");
	}

	@SuppressWarnings("unchecked")
	public static <T1, T2> T1 merge(T1 destObj, T2 srcObj, String... mergeFields) {
		if(destObj == null) {
			//此种情况，T1和T2必须为相同类型
			return (T1) srcObj;
		}
		if(srcObj == null) {
			return destObj;
		}
		if(destObj.getClass().isArray()) {
			return (T1) merge((T1[]) destObj, (T1[]) srcObj, mergeFields);
		} else if(destObj instanceof Collection<?>) {
			return (T1) merge((Collection<T1>) destObj, (Collection<T1>) srcObj, mergeFields);
		}
		if(mergeFields != null && mergeFields.length == 1 && "".equals(mergeFields[0])) {
			try {
				PropertyUtils.copyProperties(destObj, srcObj);
			} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new ReflectException(e);
			}
			return destObj;
		} else {
			Set<String> subFs = new HashSet<String>();
			for(String field : list(mergeFields)) {
				if(subFs.contains(field)) {
					continue;
				}
				subFs.add(field);
				String f0 = StringUtils.split(field, '.')[0];
				List<String> subFileds = new ArrayList<String>();
				for(String f : list(mergeFields)) {
					if(f.startsWith(f0 + ".")) {
						subFileds.add(f.substring(f0.length() + 1));
						subFs.add(f);
					}
				}
				try {
					if(subFileds.size() > 0) {
						PropertyUtils.setProperty(
								destObj,
								f0,
								merge(PropertyUtils.getProperty(destObj, f0), PropertyUtils.getProperty(srcObj, f0),
										toArray(subFileds, String.class)));
					} else {
						if(ReflectionUtils.hasField(destObj, f0)) {
							PropertyUtils.setProperty(destObj, f0, PropertyUtils.getProperty(srcObj, f0));
						}
					}
				} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new ReflectException(e);
				}
			}
			return destObj;
		}
	}

	public static void objToObjByDescs(Object destObj, Object srcObj, String... fieldDescs) {
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(srcObj);
		for(PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			if(!ArrayUtils.contains(fieldDescs, name)) {
				continue;
			}
			try {
				if(descriptor.getReadMethod() != null) {
					Object value = descriptor.getReadMethod().invoke(srcObj);
					if (value == null){
						continue;
					}
					if(value instanceof String || value instanceof Boolean || value instanceof Number || value instanceof Date) {
						valueToObj(name, PropertyUtils.getProperty(srcObj, name), destObj);
					}
				}
			} catch(Exception e) {
				logger.error("", e);
			}
		}
	}

	public static void objToObjByDescsWithNull(Object destObj, Object srcObj, String... fieldDescs) {
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(srcObj);
		for(PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			if(!ArrayUtils.contains(fieldDescs, name)) {
				continue;
			}
			try {
				Object value = descriptor.getReadMethod().invoke(srcObj);//PropertyUtils.getProperty(srcObj, name);
				if (value == null && PropertyUtils.isWriteable(destObj, name)){
					PropertyUtils.getPropertyDescriptor(destObj, name).getWriteMethod().invoke(destObj, value);
					continue;
				}
				if(value instanceof String || value instanceof Boolean || value instanceof Number || value instanceof Date) {
					valueToObj(name, PropertyUtils.getProperty(srcObj, name), destObj);
				}
			} catch(Exception e) {
				logger.error("", e);
			}
		}
	}

	public static void simpleObjToObj(Object destObj, Object srcObj) {
		if(srcObj == null) {
			return;
		}
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(srcObj);
		for(PropertyDescriptor descriptor : descriptors) {
			String name = descriptor.getName();
			try {
				if(ReflectionUtils.hasField(destObj, name) && descriptor.getReadMethod() != null) {
					Object value = descriptor.getReadMethod().invoke(srcObj);//PropertyUtils.getProperty(srcObj, name);
					if(value instanceof String || value instanceof Boolean || value instanceof Number || value instanceof Date) {
						valueToObj(name, PropertyUtils.getProperty(srcObj, name), destObj);
					}
				}
			} catch(Exception e) {
				logger.error("", e);
			}
		}
	}
}
