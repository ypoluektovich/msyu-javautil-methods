package org.msyu.javautil.methods.implan;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class DummyRootInterfaceAccessor<M extends MethodAccessor> implements InterfaceAccessor<M> {

	private final Set<InterfaceAccessor<M>> children;

	DummyRootInterfaceAccessor(Collection<? extends InterfaceAccessor<M>> children) {
		this.children = new HashSet<>(children);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DummyRootInterfaceAccessor dummyRoot = (DummyRootInterfaceAccessor) o;
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
	public Collection<InterfaceAccessor<M>> getInterfaces() {
		return children;
	}

	@Override
	public Collection<M> getMethods() {
		return Collections.emptyList();
	}

}
