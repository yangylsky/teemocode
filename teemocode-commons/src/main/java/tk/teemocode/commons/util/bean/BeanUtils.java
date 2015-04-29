package tk.teemocode.commons.util.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.hibernate.EmbedObject;
import tk.teemocode.commons.component.hibernate.HibernateUtil;
import tk.teemocode.commons.exception.ReflectException;
import tk.teemocode.commons.util.reflect.ReflectionUtils;

@SuppressWarnings("unchecked")
public class BeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	/**
	 * 将New BO的property合并到Old BO对应的property<br/>
	 * 合并规则：<br/>
	 * 1.对于property为基本类型(Byte, Long, Float, Integer, String, [], 等)：<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;1) 忽略New BO为null的property;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;2) New BO的property非null且等于<code>NullValue</code>时,将Old BO对应的property设置为null;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;3) New BO的property非null且不等于<code>NullValue</code>时,将Old BO对应的property设置为New BO的property.<br/>
	 * 2.对于property为IBO类型：<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;1) 忽略New BO为null的property;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;2) New BO的property非null且id等于<code>NullValue.NullLong</code>时,将Old BO对应的property设置为null;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;3) New BO的property非null且id不等于<code>NullValue.NullLong</code>时,递归合并该property.<br/>
	 * 3.对于property为Collection<IBO>类型：<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;1) 将Old BO里没有的item加入到Old V;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;2) 将New BO里没有的item从Old BO里删除;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;3) 对于New BO和Old BO都有的的item,将New BO的property递归合并到Old BO里对应的item.<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;注：以上的判断参考 <code>BO.equals()</code>
	 * @see BO#equals()
	 * @see NullValue
	 * @param <B> - 传入的BO类型必须为IBO
	 * @param oldBO - Old V
	 * @param newBO - New V
	 */
	public static <B extends IdObject> void mergeObject(B oldBO, B newBO) {
		if(oldBO == null) {
			oldBO = newBO;
			return;
		}
		if(newBO == null) {
			return;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Begin merging " + HibernateUtil.getEntityClass(newBO.getClass()).getSimpleName() + "[id:" + newBO.getId()
					+ "] to " + HibernateUtil.getEntityClass(oldBO.getClass()).getSimpleName() + "[id:" + newBO.getId() + "]");
		}
		long start = System.currentTimeMillis();
		mergeObject(oldBO, newBO, new HashSet<Object>(), new HashSet<ClassField>());
		long past = System.currentTimeMillis() - start;
		logger.debug("mergeObject() process time(millisecond): " + past);
	}

	public static void mergeObject(Object oldObj, Object newObj, Set<ClassField> omitFields) {
		long start = System.currentTimeMillis();
		mergeObject(oldObj, newObj, new HashSet<Object>(), omitFields);
		long past = System.currentTimeMillis() - start;
		logger.debug("mergeObject() process time(millisecond): " + past);
	}

	@SuppressWarnings("rawtypes")
	private static void mergeObject(Object oldObj, Object newObj, Set<Object> mergedBOs, Set<ClassField> omitFields) {
		if(oldObj == null) {
			oldObj = newObj;
			return;
		}
		if(mergedBOs.contains(oldObj)) {
			return;
		} else {
			mergedBOs.add(oldObj);
		}
//		log.error("Begin merging " + HibernateUtil.getEntityClass(newObj.getClass()).getSimpleName() + "[id:" + ((IBO) newObj).getId()
//				+ "] to " + HibernateUtil.getEntityClass(oldObj.getClass()).getSimpleName() + "[id:" + ((IBO) newObj).getId() + "]");
		Class<?> entityClass = HibernateUtil.getEntityClass(oldObj.getClass());
		Set<PropertyDescriptor> properties = getProperties(newObj, IdObject.class);
		NextProperty:
			for(PropertyDescriptor property : properties) {
				String name = property.getName();
				ClassField ClassField = new ClassField(entityClass, name);
				if(omitFields != null && omitFields.contains(ClassField)) {
//					log.debug("Omit field:" + ClassField);
					continue;
				}
				Object newValue = getProperty(newObj, name);
				if(newValue != null) {
					for(NullValue nullValue : NullValue.values()) {
						if(nullValue.value.equals(newValue)) {
							setProperty(oldObj, name, null);
							continue NextProperty;
						}
					}
					if(newValue instanceof Collection) {
						Collection oldColl = (Collection) getProperty(oldObj, name);
						Collection newColl = (Collection) newValue;
						if(isSimpleType(oldColl, newColl)) {
							mergeCollection(oldColl, newColl);
						} else if(isBoType(oldColl, newColl)) {
							mergeBoCollection(oldColl, newColl, mergedBOs, omitFields);
						}
					} else {
						if(newValue instanceof IdObject) {
							IdObject newBo = (IdObject) newValue;
							if(newBo.getId() != null && newBo.getId().equals(NullValue.NullLong.value)) {
								setProperty(oldObj, name, null);
							} else {
								IdObject oldBo = (IdObject) getProperty(oldObj, name);
								if(oldBo == null) {
									setProperty(oldObj, name, newBo);
								} else {
									if(newBo.getId() != null && !newBo.getId().equals(oldBo.getId())) {
										setProperty(oldObj, name, newBo);
									} else {
										mergeObject(oldBo, newBo, mergedBOs, omitFields);
									}
								}
							}
						} else if(newValue instanceof EmbedObject) {
							EmbedObject oldValue = (EmbedObject) getProperty(oldObj, name);
							if(oldValue == null) {
								setProperty(oldObj, name, newValue);
							} else {
								mergeObject(oldValue, newValue, mergedBOs, omitFields);
							}
						} else {
							if(!name.equals("id") || ((Long) newValue) > 0) {
								Object oldValue = getProperty(oldObj, name);
								if(!newValue.equals(oldValue)) {
									setProperty(oldObj, name, newValue);
								}
							}
						}
					}
				}
			}
	}

	public static Object getProperty(Object o, String name) {
		try {
			return PropertyUtils.getProperty(o, name);
		} catch(IllegalAccessException e) {
			throw new ReflectException(e);
		} catch(InvocationTargetException e) {
			e.printStackTrace();
			throw new ReflectException(e.getTargetException());
		} catch(NoSuchMethodException e) {
			throw new ReflectException(e);
		}
	}

	public static void setProperty(Object o, String name, Object value) {
		try {
			PropertyUtils.setProperty(o, name, value);
		} catch(IllegalAccessException e) {
			logger.error("", e);
			throw new ReflectException(e);
		} catch(InvocationTargetException e) {
			logger.error("", e);
			throw new ReflectException(e);
		} catch(NoSuchMethodException e) {
			logger.error("", e);
			throw new ReflectException(e);
		}
	}

	/**
	 * 合并2个Collection对象,仅支持简单元素()
	 * @param oldColl
	 * @param newColl
	 */
	@SuppressWarnings("rawtypes")
	public static void mergeCollection(Collection oldColl, Collection newColl) {
		if(newColl == null) {
			return;
		}
		if(oldColl == null) {
			oldColl = new ArrayList();
		}
		for(Iterator<?> iter = oldColl.iterator(); iter.hasNext();) {
			Object o = iter.next();
			if(!newColl.contains(o)) {
				iter.remove();
			}
		}
		for(Object o : newColl) {
			if(!oldColl.contains(o)) {
				oldColl.add(o);
			}
		}
	}
	private static boolean isSimpleType(Collection<?> oldColl, Collection<?> newColl) {
		if(CollectionUtils.isNotEmpty(oldColl)) {
			return isSimpleType(oldColl.iterator().next());
		}
		if(CollectionUtils.isNotEmpty(newColl)) {
			return isSimpleType(newColl.iterator().next());
		}
		return false;
	}
	private static boolean isSimpleType(Object o) {
		return o != null &&
				(o instanceof String || o instanceof Boolean || o instanceof Number || o instanceof Date);
	}

	/**
	 * 合并2个Collection对象，并返回合并后的对象
	 * @param <B>
	 * @param oldColl
	 * @param newColl
	 * @return
	 */
	public static <B extends IdObject> void mergeBoCollection(Collection<B> oldColl, Collection<B> newColl) {
		mergeBoCollection(oldColl, newColl, new HashSet<ClassField>());
	}

	public static <B extends IdObject> void mergeBoCollection(Collection<B> oldColl, Collection<B> newColl,
			Set<ClassField> omitFields) {
		mergeBoCollection(oldColl, newColl, new HashSet<Object>(), omitFields);
	}

	private static <B extends IdObject> void mergeBoCollection(Collection<B> oldColl, Collection<B> newColl,
			Set<Object> mergedBOs, Set<ClassField> omitFields) {
		if(newColl == null) {
			return;
		}
		if(oldColl == null) {
			oldColl = new ArrayList<B>();
		}
		if(mergedBOs.contains(oldColl)) {
			return;
		} else {
			mergedBOs.add(oldColl);
		}
		for(Iterator<B> iter = oldColl.iterator(); iter.hasNext();) {
			B v = iter.next();
			if(newColl.contains(v)) {
				mergeObject(v, getIdObject(newColl, v.getId()), mergedBOs, omitFields);
			} else {
				iter.remove();
			}
		}
		for(B v : newColl) {
			if(!oldColl.contains(v)) {
				oldColl.add(v);
			}
		}
	}

	public static <B extends IdObject> B getIdObject(Collection<B> col, Long id) {
		if(col != null && id != null) {
			for(B b : col) {
				if(b != null && id.equals(b.getId())) {
					return b;
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前bean严格符合property定义的所有可用property(包含父类的property)<br/>
	 * 符合property定义的条件：<br/>
	 * 		1.有get和set方法；<br/>
	 * 		2.get方法上没有javax.persistence.Transient注解。
	 * @param bean - 需要获取property的bean
	 * @param baseClass - 当前bean上溯终止的基类
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Set<PropertyDescriptor> getProperties(Object bean, Class<?> baseClass) {
		if(bean == null) {
			return new HashSet<PropertyDescriptor>();
		} else {
			return getProperties(HibernateUtil.getEntityClass(bean.getClass()), baseClass);
		}
	}

	/**
	 * 获取当前bean Class严格符合property定义的所有可用property(包含父类的property)<br/>
	 * 符合property定义的条件：<br/>
	 * 		1.有get和set方法；<br/>
	 * 		2.get方法上没有javax.persistence.Transient注解。
	 * @param beanClass - 需要获取property的bean Class
	 * @param baseClass - 当前bean上溯终止的基类
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Set<PropertyDescriptor> getProperties(Class<?> beanClass, Class<?> baseClass) {
		Set<PropertyDescriptor> beanProperties = new HashSet<PropertyDescriptor>();
		appendProperties(beanClass, baseClass, beanProperties, false);
		return beanProperties;
	}

	private static void appendProperties(Class<?> beanClass, Class<?> baseClazz, Set<PropertyDescriptor> properties,
			boolean onlyBo) {
		Set<PropertyDescriptor> beanProperties = getRealProperties(PropertyUtils.getPropertyDescriptors(beanClass), onlyBo);
		properties.addAll(beanProperties);
		if(!beanClass.equals(baseClazz) && !beanClass.equals(Object.class)) {
			Class<?> superClass = beanClass.getSuperclass();
			appendProperties(superClass, baseClazz, properties, onlyBo);
		}
	}

	private static Set<PropertyDescriptor> getRealProperties(PropertyDescriptor[] oldProperties, boolean onlyBo) {
		Set<PropertyDescriptor> newProperties = new HashSet<PropertyDescriptor>();
		for(PropertyDescriptor property : oldProperties) {
			if(isRealProperty(property)) {
				if(onlyBo) {
					if(ClassUtils.isAssignable(property.getReadMethod().getReturnType(), IdObject.class)) {
						newProperties.add(property);
					}
				} else {
					newProperties.add(property);
				}
			}
		}
		return newProperties;
	}

	private static boolean isRealProperty(PropertyDescriptor property) {
		Method readMethod = property.getReadMethod();
		Method writeMethod = property.getWriteMethod();
		return readMethod != null && writeMethod != null && !readMethod.isAnnotationPresent(Transient.class);
	}

	private static boolean isBoType(Collection<?> oldColl, Collection<?> newColl) {
		if(CollectionUtils.isNotEmpty(oldColl)) {
			return (oldColl.iterator().next() instanceof IdObject);
		}
		if(CollectionUtils.isNotEmpty(newColl)) {
			return (newColl.iterator().next() instanceof IdObject);
		}
		return false;
	}

	/**
	 * oldObj和oldColl里的元素是双向关联，把oldColl里的元素对应oldObj的property置为null
	 *
	 * @param oldColl
	 * @param newColl
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unused")
	private static void setReferenceNull(Object oldObj, Collection<?> oldColl, Collection<?> newColl) {
		for(Object o : oldColl) {
			if(o != null && !newColl.contains(o)) {
				setReferenceNull(oldObj, (IdObject) o);
			}
		}
	}

	private static void setReferenceNull(Object oldObj, IdObject oldBo) {
		try {
			Set<PropertyDescriptor> properties = getProperties(oldBo, IdObject.class);
			for(PropertyDescriptor property : properties) {
				String name = property.getName();
				Object value = PropertyUtils.getProperty(oldBo, name);
				if(value != null
						&& HibernateUtil.getEntityClass(value.getClass()).equals(
								HibernateUtil.getEntityClass(oldObj.getClass()))) {
					PropertyUtils.setProperty(oldBo, name, null);
					return;
				}
			}
		} catch(IllegalAccessException e) {
			throw new ReflectException(e);
		} catch(InvocationTargetException e) {
			throw new ReflectException(e);
		} catch(NoSuchMethodException e) {
			throw new ReflectException(e);
		}
	}

	public static Set<PropertyDescriptor> getBoProperties(Object bean) {
		return getBoProperties(bean.getClass());
	}

	public static Set<PropertyDescriptor> getBoProperties(Class<?> beanClass) {
		Set<PropertyDescriptor> beanProperties = new HashSet<PropertyDescriptor>();
		appendProperties(beanClass, IdObject.class, beanProperties, true);
		return beanProperties;
	}

	/**
	 * 根据fieldDesc的描述创建新对象列表
	 * @param srcObjs
	 * @param fieldDescs - eg. {"id", "description", "group.id", "group.name", "group.location.country.name"}
	 * @return
	 */
	public static <B> List<B> createObjects(List<B> srcObjs, String[] fieldDescs) {
		List<B> toObjs = new ArrayList<B>();
		for(B obj : srcObjs) {
			toObjs.add(createObject(obj, fieldDescs));
		}
		return toObjs;
	}

	/**
	 * 根据fieldDesc的描述创建新对象
	 * @param srcObj
	 * @param fieldDescs - eg. {"id", "description", "group.id", "group.name", "group.location.country.name"}
	 * @return
	 */
	public static <B> B createObject(B srcObj, String[] fieldDescs) {
		Class<?> clazz = HibernateUtil.getEntityClass(srcObj.getClass());
		try {
			B toObj = (B) clazz.newInstance();
			for(String fieldName : fieldDescs) {
				updateProperty(toObj, srcObj, fieldName);
			}
			return toObj;
		} catch(InstantiationException e) {
			logger.error("", e);
			throw new ReflectException(e);
		} catch(IllegalAccessException e) {
			logger.error("", e);
			throw new ReflectException(e);
		}
	}

	/**
	 * 根据fieldDesc的描述复制值到对象
	 * @param toObj
	 * @param srcObj
	 * @param fieldDesc - eg. "group.name"
	 */
	public static <B> void updateProperty(B toObj, B srcObj, String fieldDesc) {
		if(StringUtils.contains(fieldDesc, '.')) {
			int idx = fieldDesc.indexOf('.');
			String propName1 = fieldDesc.substring(0, idx);
			String propName2 = fieldDesc.substring(idx + 1);
			Field field = ReflectionUtils.getDeclaredField(srcObj, propName1);
			if(field != null) {
				try {
					Object srcValue = getProperty(srcObj, propName1);
					if(srcValue != null) {
						Object newValue = getProperty(toObj, propName1);
						if(newValue == null) {
							newValue = field.getType().newInstance();
							setProperty(toObj, propName1, newValue);
						}
						updateProperty(newValue, srcValue, propName2);
					}
				} catch(InstantiationException e) {
					logger.error("", e);
					throw new ReflectException(e);
				} catch(IllegalAccessException e) {
					logger.error("", e);
					throw new ReflectException(e);
				}
			}
		} else {
			Object srcValue = getProperty(srcObj, fieldDesc);
			setProperty(toObj, fieldDesc, srcValue);
		}
	}

	/**
	 * 检查对象关联的实体(递归检查)，如果id无效，则把关联的field置为null
	 * @param obj
	 */
	public static <B> void updateNullRefObj(B obj) {
		updateNullRefObj(obj, new HashSet<>());
	}

	public static <B> void updateNullRefObj(B obj, Set<Object> checkedBOs) {
		if(checkedBOs.contains(obj)) {
			return;
		} else {
			checkedBOs.add(obj);
		}
		Set<PropertyDescriptor> properties = getProperties(obj, IdObject.class);
		for(PropertyDescriptor property : properties) {
			Class<?> propertyClass = property.getPropertyType();
			if(ClassUtils.isAssignable(propertyClass, IdObject.class)) {
				IdObject bo = (IdObject) getProperty(obj, property.getName());
				if(bo != null) {
					if(bo.isValidId()) {
						updateNullRefObj(bo, checkedBOs);
					} else {
						setProperty(obj, property.getName(), null);
					}
				}
			}
		}
	}
}
