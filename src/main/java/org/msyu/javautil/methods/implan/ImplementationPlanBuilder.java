package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ImplementationPlanBuilder<M extends MethodAccessor> {

	public static <M extends MethodAccessor> InterfacePlan<M> planFor(Collection<InterfaceAccessor<M>> accessors) {
		Objects.requireNonNull(accessors);
		if (accessors.isEmpty()) {
			throw new IllegalArgumentException("no interfaces");
		}
		InterfaceAccessor<M> rootAccessor;
		if (accessors.size() == 1) {
			rootAccessor = accessors.iterator().next();
		} else {
			rootAccessor = new DummyRootInterfaceAccessor<>(accessors);
		}
		ImplementationPlanBuilder<M> builder = new ImplementationPlanBuilder<>();
		builder.planFor(rootAccessor);
		return builder.planByIface.get(rootAccessor);
	}


	private final Map<InterfaceAccessor, InterfacePlan<M>> planByIface = new HashMap<>();

	private InterfacePlan<M> planFor(InterfaceAccessor<M> iface) {
		InterfacePlan<M> plan = planByIface.get(iface);
		if (plan != null) {
			return plan;
		}

		Map<MethodSignature, MethodPlan.Builder<M>> builderByMethod = new HashMap<>();

		for (MethodAccessor method : iface.getMethods()) {
			MethodSignature signature = method.getSignature();
			MethodPlan.Builder builder = getBuilder(builderByMethod, signature);
			if (method.hasImplementation()) {
				builder.overrideAccessor = method;
			}
		}

		for (InterfaceAccessor<M> superIface : iface.getInterfaces()) {
			InterfacePlan<M> superPlan = planFor(superIface);
			planByIface.get(superIface);
			for (MethodPlan<M> methodPlan : superPlan.planByMethod.values()) {
				getBuilder(builderByMethod, methodPlan.signature).witnessSuperPlan(methodPlan);
			}
		}

		plan = new InterfacePlan<>(builderByMethod);
		planByIface.put(iface, plan);
		return plan;
	}

	private static <M extends MethodAccessor> MethodPlan.Builder<M> getBuilder(
			Map<MethodSignature, MethodPlan.Builder<M>> builderByMethod,
			MethodSignature signature
	) {
		return builderByMethod.computeIfAbsent(signature, MethodPlan.Builder::new);
	}

}
