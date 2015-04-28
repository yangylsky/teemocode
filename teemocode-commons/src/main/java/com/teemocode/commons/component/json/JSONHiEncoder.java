package com.teemocode.commons.component.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONHiEncoder extends JSONEncoder {
	Logger logger = LoggerFactory.getLogger(JSONHiEncoder.class);

	private static JSONHiEncoder encoder = new JSONHiEncoder(false, 16);

	public JSONHiEncoder() {
		super();
	}

	public JSONHiEncoder(boolean emitClassName, int depth) {
		super(emitClassName, depth);
	}

	public static String encode(Object value) {
		StringWriter buf = new StringWriter();
		try {
			encoder.encode(value, buf, new HashSet<Object>(), new HashMap<String, Object>());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return buf.toString();
	}

	public static String encodeByParams(Object value, Map<String, Object> params) {
		StringWriter buf = new StringWriter();
		try {
			if(params == null) {
				params = new HashMap<String, Object>();
			}
			encoder.encode(value, buf, new HashSet<Object>(),params);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return buf.toString();
	}

	@Override
	protected void print(Object object, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		object = proxyCheck(object);
		super.print(object, out, cached, params);
	}

	@Override
	protected void printBean(Object object, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		Boolean exportValueNull = (Boolean) params.get("exportValueNull");
		if(exportValueNull == null) {
			exportValueNull = false;
		}
		out.write('{');
		BeanInfo info;
		boolean addedSomething = false;
		try {
			info = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for(int i = 0; i < props.length; ++i) {
				try {
					PropertyDescriptor prop = props[i];
					String name = prop.getName();
					Method accessor = prop.getReadMethod();
					if(accessor != null && (!"class".equals(name) || isPrintClassName())) {
						if(!accessor.isAccessible()) {
							accessor.setAccessible(true);
						}
						Object value = accessor.invoke(object);
						
						if ( exportValueNull ||  value != null )
						{
							if(addedSomething) {
								out.write(',');
							}
							print(name, out, params);
							out.write(':');
							print(value, out, cached, params);
							addedSomething = true;
						}
					}
				} catch(IllegalAccessException iae) {
					out.write("");
					logger.debug("encode bean property 出错", iae.getMessage());
				} catch(InvocationTargetException ite) {
					out.write("");
					logger.debug("encode bean property 出错", ite.getMessage());
				} catch(Exception e) {
					out.write("");
					logger.debug("encode bean property 出错", e.getMessage());
				}
			}
		} catch(IntrospectionException ie) {
			logger.error("encode bean 出错", ie);
		}
		out.write('}');
	}

	protected Object proxyCheck(Object bean) {
		//        System.out.println("Class is " + bean.getClass().getName());
		if(bean instanceof HibernateProxy) {
			LazyInitializer lazyInitializer = ((HibernateProxy) bean).getHibernateLazyInitializer();
			//            lazyInitializer.initialize();
			if(lazyInitializer.isUninitialized()) {
				//                System.out.println(">>>>>lazyInitializer.getIdentifier()="+ lazyInitializer.getIdentifier());
				return lazyInitializer.getIdentifier();
			}
		}
		if(bean instanceof PersistentSet) {
			return new String[] {}; //忽略hibernate one-to-many
		}
		return bean;
	}
}
