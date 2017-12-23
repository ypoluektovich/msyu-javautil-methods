package org.msyu.javautil.methods.implan;

import org.msyu.javautil.cf.CopyList;
import org.msyu.javautil.methods.MethodSignature;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonList;

public final class MethodPlan {

	final static class Builder {
		private final MethodSignature signature;
		MethodAccessor overrideAccessor;
		private final Set<MethodAccessor> superAccessors = new LinkedHashSet<>();

		Builder(MethodSignature signature) {
			this.signature = signature;
		}

		final void witnessSuperPlan(MethodPlan plan) {
			superAccessors.addAll(plan.implementations);
		}

		final MethodPlan build() {
			return new MethodPlan(
					signature,
					overrideAccessor != null ?
							singletonList(overrideAccessor) :
							superAccessors
			);
		}
	}

	final MethodSignature signature;
	final List<MethodAccessor> implementations;

	MethodPlan(MethodSignature signature, Collection<MethodAccessor> implementations) {
		this.signature = signature;
		this.implementations = CopyList.immutable(implementations);
	}

	public final boolean hasOneImplementation() {
		return implementations.size() == 1;
	}

	public final MethodAccessor getTheImplementation() {
		if (hasOneImplementation()) {
			return implementations.get(0);
		} else {
			throw new IllegalStateException("unimplemented/ambiguous method");
		}
	}

	public final Optional<MethodAccessor> getTheImplementationIfExists() {
		return hasOneImplementation() ? Optional.of(implementations.get(0)) : Optional.empty();
	}

	public final boolean hasImplementationConflict() {
		return implementations.size() > 1;
	}

	/**
	 * Get all topmost implementations.
	 */
	public final List<MethodAccessor> getImplementations() {
		return implementations;
	}

}
