package org.msyu.javautil.methods.implan;

import java.util.Collection;

public interface InterfaceAccessor<M extends MethodAccessor> {

	String getName();

	Collection<InterfaceAccessor<M>> getInterfaces();

	Collection<M> getMethods();

}
