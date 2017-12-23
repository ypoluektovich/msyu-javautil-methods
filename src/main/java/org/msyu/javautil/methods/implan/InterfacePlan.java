package org.msyu.javautil.methods.implan;

import org.msyu.javautil.cf.CopyMap;
import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.Map;

public final class InterfacePlan<M extends MethodAccessor> {

	final Map<MethodSignature, MethodPlan<M>> planByMethod;

	InterfacePlan(Map<MethodSignature, MethodPlan.Builder<M>> planByMethod) {
		this.planByMethod = CopyMap.immutableHashV(planByMethod, MethodPlan.Builder::build);
	}

	public final MethodPlan<M> getPlanFor(MethodSignature signature) {
		return planByMethod.get(signature);
	}

	public final Collection<MethodPlan<M>> getAllPlans() {
		return planByMethod.values();
	}

}
