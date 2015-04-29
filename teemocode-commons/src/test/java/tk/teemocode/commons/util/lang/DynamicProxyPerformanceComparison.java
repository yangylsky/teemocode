package tk.teemocode.commons.util.lang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * JDK，CgLib，JavaSsist三种动态代理方式速度比较
 *
 */
public class DynamicProxyPerformanceComparison {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Callable<Integer> jdkProxy = (Callable<Integer>) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[] {Callable.class}, new JdkHandler(new Counter()));


		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(new CglibInterceptor(new Counter()));
		enhancer.setInterfaces(new Class[] {Callable.class});
		Callable<Integer> cglibProxy = (Callable<Integer>) enhancer.create();

		ProxyFactory f = new ProxyFactory();
		f.setInterfaces(new Class[] {Callable.class});
		Class<?> c = f.createClass();

		Callable<Integer> javassistProxy = (Callable<Integer>) c.newInstance();
		((ProxyObject) javassistProxy).setHandler(new javaSsistInterceptor(new Counter()));

		for(int i = 0; i < 5; i++) {
			iterate(jdkProxy, "JDK Proxy:\t");
			iterate(cglibProxy, "CGLIB:\t\t");
			iterate(javassistProxy, "JAVASSIST:\t");

			System.err.println();
		}
	}

	static final DecimalFormat format = new DecimalFormat();

	static void iterate(Callable<Integer> callable, String label) throws Exception {
		int count = 10000000;
		long time = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			callable.call();
		}
		time = System.currentTimeMillis() - time;
		System.err.println(label + format.format(count * 1000 / time) + " calls/s");
	}

	static class JdkHandler implements InvocationHandler {
		final Object delegate;

		JdkHandler(Object delegate) {
			this.delegate = delegate;
		}

		@Override
		public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
			return method.invoke(delegate, objects);
		}
	}

	static class CglibInterceptor implements MethodInterceptor {
		final Object delegate;

		CglibInterceptor(Object delegate) {
			this.delegate = delegate;
		}

		@Override
		public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			return methodProxy.invoke(delegate, objects);
		}
	}

	static class javaSsistInterceptor implements MethodHandler {
		final Object delegate;

		javaSsistInterceptor(Object delegate) {
			this.delegate = delegate;
		}

		@Override
		public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {
			return m.invoke(delegate, args);
		}
	}

	static class Counter implements Callable<Integer> {
		int count = 0;

		@Override
		public Integer call() throws Exception {
			return count++;
		}
	}
}
