package org.msyu.javautil.methods.implan.example;

import org.msyu.javautil.methods.MethodSignature;
import org.msyu.javautil.methods.implan.InterfacePlan;
import org.msyu.javautil.methods.implan.MethodPlan;
import org.msyu.javautil.methods.implan.ReflectionMethodAccessor;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.msyu.javautil.methods.implan.ImplementationPlanBuilder.planFor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.expectThrows;

public class BasicExample {

	public interface I {
		String a(Object o);
		default int b(Object o) {
			return System.identityHashCode(o);
		}
		Object c();
		default void d() {
			throw new RuntimeException("dead");
		}
	}

	@Test
	public void test() {
		InterfacePlan<ReflectionMethodAccessor> ifacePlan = planFor(I.class);
		I impl = (I) Proxy.newProxyInstance(
				BasicExample.class.getClassLoader(),
				new Class[]{I.class},
				(proxy, method, args) -> {
					MethodSignature signature = new MethodSignature(method);
					MethodPlan<ReflectionMethodAccessor> plan = ifacePlan.getPlanFor(signature);
					if (plan.hasOneImplementation()) {
						try {
							return plan.getTheImplementation().invokeDirectly(proxy, args);
						} catch (InvocationTargetException e) {
							throw e.getCause();
						}
					} else if (signature.name.equals("a")) {
						return args[0].toString();
					} else {
						throw new UnsupportedOperationException(method.getName());
					}
				}
		);
		assertEquals(impl.a("string"), "string");
		assertEquals(impl.b("string"), System.identityHashCode("string"));
		assertThrows(UnsupportedOperationException.class, impl::c);
		assertEquals(expectThrows(RuntimeException.class, impl::d).getMessage(), "dead");
	}

}
