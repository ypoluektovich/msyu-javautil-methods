package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MethodPlan {

	static MethodPlan merge(MethodSignature signature, MethodPlan p1, MethodPlan p2, InterfaceAccessor currentIface) {
		List<InterfaceAccessor> defined =  Stream
				.concat(
						p1 == null ? Stream.empty() : p1.definedIn.stream(),
						p2 == null ? Stream.empty() : p2.definedIn.stream()
				)
				.distinct()
				.collect(Collectors.toList());
		List<InterfaceAccessor> implemented = Stream
				.concat(
						p1 == null ? Stream.empty() : p1.implementedIn.stream(),
						p2 == null ? Stream.empty() : p2.implementedIn.stream()
				)
				.distinct()
				.collect(Collectors.toList());
		if (implemented.contains(currentIface)) {
			return new MethodPlan(signature, currentIface, defined, implemented);
		} else if (implemented.size() == 1) {
			return new MethodPlan(signature, implemented.get(0), defined, implemented);
		} else {
			return new MethodPlan(signature, null, defined, implemented);
		}
	}

	final MethodSignature signature;
	final InterfaceAccessor chosenIface;
	final List<InterfaceAccessor> definedIn;
	final List<InterfaceAccessor> implementedIn;

	MethodPlan(MethodSignature signature, InterfaceAccessor iface, boolean hasImplementation) {
		this.signature = signature;
		this.chosenIface = hasImplementation ? iface : null;
		this.definedIn = Collections.singletonList(iface);
		this.implementedIn = hasImplementation ? Collections.singletonList(iface) : Collections.emptyList();
	}

	MethodPlan(MethodSignature signature, InterfaceAccessor chosenIface, List<InterfaceAccessor> definedIn, List<InterfaceAccessor> implementedIn) {
		this.signature = signature;
		this.chosenIface = chosenIface;
		this.definedIn = definedIn;
		this.implementedIn = implementedIn;
	}

}
