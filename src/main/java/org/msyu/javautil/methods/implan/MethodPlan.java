package org.msyu.javautil.methods.implan;

import org.msyu.javautil.cf.CopyList;
import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonList;

public final class MethodPlan<M extends MethodAccessor> {

	final static class Builder<M extends MethodAccessor> {
		private final MethodSignature signature;
		M overrideAccessor;
		private final Set<M> superAccessors = new LinkedHashSet<>();

		Builder(MethodSignature signature) {
			this.signature = signature;
		}

		final void witnessSuperPlan(MethodPlan<M> plan) {
			superAccessors.addAll(plan.implementations);
		}

		final MethodPlan<M> build() {
			return new MethodPlan<M>(
					signature,
					overrideAccessor != null ?
							singletonList(overrideAccessor) :
							superAccessors
			);
		}
	}

	final MethodSignature signature;
	final List<M> implementations;

	MethodPlan(MethodSignature signature, Collection<M> implementations) {
		this.signature = signature;
		this.implementations = CopyList.immutable(implementations);
	}

	public final boolean hasOneImplementation() {
		return implementations.size() == 1;
	}

	public final M getTheImplementation() {
		if (hasOneImplementation()) {
			return implementations.get(0);
		} else {
			throw new IllegalStateException("unimplemented/ambiguous method");
		}
	}

	public final Optional<M> getTheImplementationIfExists() {
		return hasOneImplementation() ? Optional.of(implementations.get(0)) : Optional.empty();
	}

	public final boolean hasImplementationConflict() {
		return implementations.size() > 1;
	}

	/**
	 * Get all topmost implementations.
	 */
	public final List<M> getImplementations() {
		return implementations;
	}

}
