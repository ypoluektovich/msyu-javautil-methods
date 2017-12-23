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

	private InterfacePlan planFor(InterfaceAccessor iface) {
		InterfacePlan plan = planByIface.get(iface);
		if (plan != null) {
			return plan;
		}

		Map<MethodSignature, MethodPlan.Builder> builderByMethod = new HashMap<>();

		for (MethodAccessor method : iface.getMethods()) {
			MethodSignature signature = method.getSignature();
			MethodPlan.Builder builder = getBuilder(builderByMethod, signature);
			if (method.hasImplementation()) {
				builder.overrideAccessor = method;
			}
		}

		for (InterfaceAccessor superIface : iface.getInterfaces()) {
			InterfacePlan superPlan = planFor(superIface);
			planByIface.get(superIface);
			for (MethodPlan methodPlan : superPlan.planByMethod.values()) {
				getBuilder(builderByMethod, methodPlan.signature).witnessSuperPlan(methodPlan);
			}
		}

		plan = new InterfacePlan(builderByMethod);
		planByIface.put(iface, plan);
		return plan;
	}

	private static MethodPlan.Builder getBuilder(
			Map<MethodSignature, MethodPlan.Builder> builderByMethod,
			MethodSignature signature
	) {
		return builderByMethod.computeIfAbsent(signature, MethodPlan.Builder::new);
	}

}
