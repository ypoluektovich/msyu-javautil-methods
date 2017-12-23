package org.msyu.javautil.methods.implan;

import org.msyu.javautil.cf.CopyMap;
import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.Map;

public final class InterfacePlan {

	final Map<MethodSignature, MethodPlan> planByMethod;

	InterfacePlan(Map<MethodSignature, MethodPlan.Builder> planByMethod) {
		this.planByMethod = CopyMap.immutableHashV(planByMethod, MethodPlan.Builder::build);
	}

	public final MethodPlan getPlanFor(MethodSignature signature) {
		return planByMethod.get(signature);
	}

	public final Collection<MethodPlan> getAllPlans() {
		return planByMethod.values();
	}

}
