package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ImplementationPlanBuilder {

	public static InterfacePlan planFor(Collection<InterfaceAccessor> accessors) {
		Objects.requireNonNull(accessors);
		if (accessors.isEmpty()) {
			throw new IllegalArgumentException("no interfaces");
		}
		InterfaceAccessor rootAccessor;
		if (accessors.size() == 1) {
			rootAccessor = accessors.iterator().next();
		} else {
			rootAccessor = new InterfaceAccessor.DummyRoot(accessors);
		}
		ImplementationPlanBuilder builder = new ImplementationPlanBuilder();
		builder.planFor(rootAccessor);
		return builder.planByIface.get(rootAccessor);
	}


	private final Map<InterfaceAccessor, InterfacePlan> planByIface = new HashMap<>();

	private void planFor(InterfaceAccessor iface) {
		if (planByIface.containsKey(iface)) {
			return;
		}

		for (InterfaceAccessor child : iface.getInterfaces()) {
			planFor(child);
		}

		Map<MethodSignature, MethodPlan> planByMethod = new HashMap<>();

		for (MethodAccessor method : iface.getMethods()) {
			MethodSignature signature = method.getSignature();
			planByMethod.put(signature, new MethodPlan(signature, iface, method.hasImplementation()));
		}

		for (InterfaceAccessor superIface : iface.getInterfaces()) {
			InterfacePlan superPlan = planByIface.get(superIface);
			for (MethodPlan methodPlan : superPlan.planByMethod.values()) {
				MethodSignature signature = methodPlan.signature;
				planByMethod.put(signature, MethodPlan.merge(signature, planByMethod.get(signature), methodPlan, iface));
			}
		}

		planByIface.put(iface, new InterfacePlan(planByMethod));
	}

}
