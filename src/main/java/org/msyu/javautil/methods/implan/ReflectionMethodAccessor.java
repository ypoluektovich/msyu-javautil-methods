package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.lang.reflect.Method;

public final class ReflectionMethodAccessor implements MethodAccessor {

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

}
