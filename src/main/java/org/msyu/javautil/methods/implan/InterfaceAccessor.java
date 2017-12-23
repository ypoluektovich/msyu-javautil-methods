package org.msyu.javautil.methods.implan;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface InterfaceAccessor {

	String getName();

	Collection<InterfaceAccessor> getInterfaces();

	Collection<MethodAccessor> getMethods();


	static Collection<InterfaceAccessor> reflect(Collection<Class<?>> classes) {
		List<InterfaceAccessor> accessors = new ArrayList<>();
		for (Class<?> klass : classes) {
			accessors.add(new Reflection(klass));
		}
		return accessors;
	}


	final class Reflection implements InterfaceAccessor {

		private final Class<?> iface;

		Reflection(Class<?> iface) {
			if (!iface.isInterface()) {
				throw new IllegalArgumentException("not an interface");
			}
			this.iface = iface;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Reflection that = (Reflection) o;
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
		public Collection<InterfaceAccessor> getInterfaces() {
			return InterfaceAccessor.reflect(Arrays.asList(iface.getInterfaces()));
		}

		@Override
		public Collection<MethodAccessor> getMethods() {
			Collection<MethodAccessor> methods = new ArrayList<>();
			for (Method method : iface.getDeclaredMethods()) {
				if (Modifier.isStatic(method.getModifiers())) {
					continue;
				}
				methods.add(new MethodAccessor.Reflection(method));
			}
			return methods;
		}

	}

	final class DummyRoot implements InterfaceAccessor {

		private final Set<InterfaceAccessor> children;

		DummyRoot(Collection<InterfaceAccessor> children) {
			this.children = new HashSet<>(children);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			DummyRoot dummyRoot = (DummyRoot) o;
			return Objects.equals(children, dummyRoot.children);
		}

		@Override
		public int hashCode() {
			return Objects.hash(children);
		}

		@Override
		public String getName() {
			return "<dummy_root>";
		}

		@Override
		public Collection<InterfaceAccessor> getInterfaces() {
			return children;
		}

		@Override
		public Collection<MethodAccessor> getMethods() {
			return Collections.emptyList();
		}

	}

}
