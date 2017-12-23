package org.msyu.javautil.methods.implan;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class ReflectionInterfaceAccessor implements InterfaceAccessor<ReflectionMethodAccessor> {

	public static Collection<InterfaceAccessor<ReflectionMethodAccessor>> reflect(Collection<Class<?>> classes) {
		List<InterfaceAccessor<ReflectionMethodAccessor>> accessors = new ArrayList<>();
		for (Class<?> klass : classes) {
			accessors.add(new ReflectionInterfaceAccessor(klass));
		}
		return accessors;
	}

	private final Class<?> iface;

	ReflectionInterfaceAccessor(Class<?> iface) {
		if (!iface.isInterface()) {
			throw new IllegalArgumentException("not an interface");
		}
		this.iface = iface;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReflectionInterfaceAccessor that = (ReflectionInterfaceAccessor) o;
		return Objects.equals(iface, that.iface);
	}

	@Override
	public int hashCode() {
		return Objects.hash(iface);
	}

	@Override
	public String getName() {
		return iface.getName();
	}

	@Override
	public Collection<InterfaceAccessor<ReflectionMethodAccessor>> getInterfaces() {
		return reflect(Arrays.asList(iface.getInterfaces()));
	}

	@Override
	public Collection<ReflectionMethodAccessor> getMethods() {
		Collection<ReflectionMethodAccessor> methods = new ArrayList<>();
		for (Method method : iface.getDeclaredMethods()) {
			if (Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			methods.add(new ReflectionMethodAccessor(method));
		}
		return methods;
	}

}
