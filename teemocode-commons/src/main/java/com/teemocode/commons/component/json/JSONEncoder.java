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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.teemocode.commons.util.DateUtil;

/**
 * 改造自stringtree，将类改成线程安全的方式。 并提供简单的静态编码方法{@see JSONEncoder#encode(Object)}
 */
public class JSONEncoder {
	private static final String TimeFormat = "yyyy-MM-dd";

	private static JSONEncoder encoder = new JSONEncoder();

	private final boolean printClassName;

	private final int depth;

	public JSONEncoder(boolean emitClassName, int depth) {
		printClassName = emitClassName;
		this.depth = depth;
	}

	public JSONEncoder() {
		this(false, 16);
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

	public void encode(Object value, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		print(value, out, cached, params);
		if(cached != null) {
			cached.clear();
		}
	}

	protected void print(Object object, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		if(object == null) {
			out.write("\"null\"");
		} else if(object instanceof Boolean) {
			out.write(String.valueOf(object));
		} else if(object instanceof Number) {
			out.write(String.valueOf(object));
		} else if(object instanceof Class) {
			//Class 系列化容易导致死循环
			print(((Class<?>) object).getName(), out, params);
		} else if(object instanceof String) {
			print((String) object, out, params);
		} else if(object instanceof Character) {
			print(String.valueOf(object), out, params);
		} else if(object instanceof Date) {
			print((Date) object, out, params);
		} else {
			if(cached != null) {
				if(cached.contains(object)) {
					print(object.toString(), out, params);
					return;
				} else {
					if(cached.size() > depth) {
						throw new RuntimeException("深度超出许可范围：" + cached);
					}
					cached.add(object);
				}
			}
			if(object instanceof Map) {
				print((Map<?, ?>) object, out, cached, params);
			} else if(object instanceof Object[]) {
				print((Object[]) object, out, cached, params);
			} else if(object instanceof Iterator) {
				print((Iterator<?>) object, out, cached, params);
			} else if(object instanceof Collection) {
				print(((Collection<?>) object).iterator(), out, cached, params);
			} else {
				printBean(object, out, cached, params);
			}
			if(cached != null) {
				cached.remove(object);
			}
		}
	}

	private void print(Date object, Writer out, Map<String, Object> params) throws IOException {
		String dateFormat = (String) params.get("dateFormat");
		if(dateFormat == null) {
			dateFormat = TimeFormat;
		}
		out.write('"');
		out.write(DateUtil.getDateTime(dateFormat, object));
		out.write('"');
	}

	protected void print(String text, Writer out, Map<String, Object> params) throws IOException {
		out.write('"');
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch(c) {
				case '"':
					// case '\'':
					// case '/':
				case '\\':
					out.write('\\');
					out.write(c);
					break;
				case '\b':// \u0008
					out.write("\\b");
					break;
				case '\n'://

					// case '\v'://\u000b
					out.write("\\n");
					break;
				// case '\f'://\u000c
				// out.write("\\f");
				// break;
				case '\r'://

					out.write("\\r");
					break;
				case '\t':// \u0009
					out.write("\\t");
					break;
				default:
					if(Character.isISOControl(c)) {
						// if ((c >= 0x0000 && c <= 0x001F)|| (c >= 0x007F && c <=
						// 0x009F)) {
						out.write("\\u");
						out.write(Integer.toHexString(0x10000 + c), 1, 5);
					} else {
						out.write(c);
					}
			}
		}
		out.write('"');
	}

	protected void printBean(Object object, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		out.write('{');
		BeanInfo info;
		boolean addedSomething = false;
		try {
			info = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for(int i = 0; i < props.length; ++i) {
				PropertyDescriptor prop = props[i];
				String name = prop.getName();
				Method accessor = prop.getReadMethod();
				if(accessor != null && (!"class".equals(name) || printClassName)) {
					if(!accessor.isAccessible()) {
						accessor.setAccessible(true);
					}
					Object value = accessor.invoke(object);
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
			iae.printStackTrace();
		} catch(InvocationTargetException ite) {
			ite.getCause().printStackTrace();
			ite.printStackTrace();
		} catch(IntrospectionException ie) {
			ie.printStackTrace();
		}
		out.write('}');
	}

	protected void print(Map<?, ?> map, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		out.write('{');
		Iterator<?> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) it.next();
			print(String.valueOf(e.getKey()), out, params);
			out.write(':');
			print(e.getValue(), out, cached, params);
			if(it.hasNext()) {
				out.write(',');
			}
		}
		out.write('}');
	}

	protected void print(Object[] object, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		out.write('[');
		for(int i = 0; i < object.length; ++i) {
			if(i > 0) {
				out.write(',');
			}
			print(object[i], out, cached, params);
		}
		out.write(']');
	}

	protected void print(Iterator<?> it, Writer out, Collection<Object> cached, Map<String, Object> params) throws IOException {
		out.write('[');
		while(it.hasNext()) {
			print(it.next(), out, cached, params);
			if(it.hasNext()) {
				out.write(',');
			}
		}
		out.write(']');
	}

	public boolean isPrintClassName() {
		return printClassName;
	}
}
