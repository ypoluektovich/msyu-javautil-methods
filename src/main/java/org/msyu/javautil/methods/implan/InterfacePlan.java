package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.util.Map;

public final class InterfacePlan {

	final Map<MethodSignature, MethodPlan> planByMethod;

	InterfacePlan(Map<MethodSignature, MethodPlan> planByMethod) {
		this.planByMethod = planByMethod;
	}

	public MethodPlan getPlanFor(MethodSignature signature) {
		return planByMethod.get(signature);
	}

}
