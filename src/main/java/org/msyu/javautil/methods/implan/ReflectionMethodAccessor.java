package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionMethodAccessor implements MethodAccessor {

	private static final Constructor<MethodHandles.Lookup> LOOKUP_CONSTRUCTOR;

	static {
		try {
			LOOKUP_CONSTRUCTOR = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
			if (!LOOKUP_CONSTRUCTOR.isAccessible()) {
				LOOKUP_CONSTRUCTOR.setAccessible(true);
			}
		} catch (Exception e) {
			throw mhFailure(e);
		}
	}

	private static RuntimeException mhFailure(Throwable cause) {
		return new RuntimeException("MethodHandle failure", cause);
	}

	private final Method method;

	ReflectionMethodAccessor(Method method) {
		this.method = method;
	}

	@Override
	public MethodSignature getSignature() {
		return new MethodSignature(method);
	}

	@Override
	public boolean hasImplementation() {
		return method.isDefault();
	}

	/**
	 * Delegates to the underlying {@link Method#invoke(Object, Object...)}.
	 */
	public Object invokeViaReflection(Object target, Object... args) throws InvocationTargetException, IllegalAccessException {
		return method.invoke(target, args);
	}

	/**
	 * Invokes the represented method without virtual dispatch.
	 */
	public Object invokeDirectly(Object target, Object... args) throws InvocationTargetException {
		// todo: optimize and/or expose facilities for user-side optimizations
		MethodHandles.Lookup lookup;
		MethodHandle getter;
		try {
			lookup = LOOKUP_CONSTRUCTOR.newInstance(method.getDeclaringClass());
			getter = lookup.unreflectSpecial(method, method.getDeclaringClass());
		} catch (Exception e) {
			throw mhFailure(e);
		}
		try {
			return getter.bindTo(target).invokeWithArguments(args);
		} catch (Throwable throwable) {
			throw new InvocationTargetException(throwable);
		}
	}

}
