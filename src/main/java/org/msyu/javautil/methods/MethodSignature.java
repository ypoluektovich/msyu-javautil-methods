package org.msyu.javautil.methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class MethodSignature {

	public final String name;

	public final List<Class<?>> parameterTypes;

	public MethodSignature(String name, List<Class<?>> parameterTypes) {
		this.name = name;
		this.parameterTypes = Collections.unmodifiableList(new ArrayList<>(parameterTypes));
	}

	public MethodSignature(Method method) {
		this(method.getName(), Arrays.asList(method.getParameterTypes()));
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MethodSignature that = (MethodSignature) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(parameterTypes, that.parameterTypes);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(name, parameterTypes);
	}

	@Override
	public final String toString() {
		StringJoiner stringJoiner = new StringJoiner(",", name + "(", ")");
		for (Class<?> parameterType : parameterTypes) {
			stringJoiner.add(parameterType.getName());
		}
		return stringJoiner.toString();
	}

}
