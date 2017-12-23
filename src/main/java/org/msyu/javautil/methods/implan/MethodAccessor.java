package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

public interface MethodAccessor {

	MethodSignature getSignature();

	boolean hasImplementation();

}
