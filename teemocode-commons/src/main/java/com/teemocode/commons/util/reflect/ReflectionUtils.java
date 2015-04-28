package com.teemocode.commons.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class ReflectionUtils {
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	private ReflectionUtils() {
	}

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 */
	public static <T> T getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if(field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		T result = null;
		try {
			result = (T) field.get(object);
		} catch(IllegalAccessException e) {
			logger.error("不可能抛出的异常{}" + e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if(field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch(IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}" + e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 */
	public static <T> T invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}

		method.setAccessible(true);

		try {
			return (T) method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod. 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch(NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	public static boolean hasField(final Object object, final String fieldName) {
		return getDeclaredField(object.getClass(), fieldName) != null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	public static Field getDeclaredField(final Object object, final String fieldName) {
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * 循环向上转型,获取类的DeclaredField.
	 */
	public static Field getDeclaredField(final Class<?> clazz, final String fieldName) {
		for(Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch(NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 强制转换fileld可访问.
	 */
	public static void makeAccessible(final Field field) {
		if(!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends HibernateDao<User>
	 * @param clazz
	 *        The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static Class<Object> getSuperClassGenricType(final Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends HibernateDao<User,Long>
	 * @param clazz
	 *        clazz The class to introspect
	 * @param index
	 *        the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class<Object> getSuperClassGenricType(final Class<?> clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();

		if(!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if(index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if(!(params[index] instanceof Class<?>)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class<Object>) params[index];
	}

	/**
	 * 提取集合中的对象的属性,组合成List.
	 * @param collection
	 *        来源集合.
	 * @param propertityName
	 *        要提取的属性名.
	 */
	public static <T> List<T> fetchElementPropertyToList(final Collection<?> collection, final String propertyName) {
		List<T> list = new ArrayList<>();

		try {
			for(Object obj : collection) {
				list.add((T) PropertyUtils.getProperty(obj, propertyName));
			}
		} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("", e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性,组合成由分割符分隔的字符串.
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String fetchElementPropertyToString(final Collection<?> collection, final String propertyName,
			final String separator) {
		List<?> list = fetchElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	public static <T> T invokeStaticMethod(Class<?> clazz, String methodName) {
		return invokeStaticMethod(clazz, methodName, null, null);
	}

	public static <T> T invokeStaticMethod(Class<?> clazz, String methodName, Class<?>[] clazzs, Object[] params) {
		T r = null;
		try {
			Method m = clazz.getMethod(methodName, clazzs);
			r = (T) m.invoke(clazz, params);
		} catch(Exception e) {
			logger.error(clazz + " has no the method: " + methodName);
		}
		return r;
	}

	public static <T> T getStaticField(Class<?> clazz, String fieldName) {
		T r = null;
		try {
			r = (T) clazz.getField(fieldName).get(clazz);
		} catch(NoSuchFieldException | IllegalAccessException e) {
			logger.warn(clazz + " has no the field: " + fieldName);
		}
		return r;
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if(e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if(e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if(e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Type> T getParamType(Type paramType) {
		if(paramType instanceof TypeVariable) {
			return (T) ((TypeVariable) paramType).getBounds()[0];
		}
		return (T) paramType;
	}
}
