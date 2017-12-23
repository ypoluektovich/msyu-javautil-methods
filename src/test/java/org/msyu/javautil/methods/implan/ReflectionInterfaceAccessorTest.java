package org.msyu.javautil.methods.implan;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ReflectionInterfaceAccessorTest {

	interface Iface {
		static void staticMethod() {}
		void normalMethod();
		default void defaultMethod() {}
	}

	InterfaceAccessor.Reflection accessor = new InterfaceAccessor.Reflection(Iface.class);

	@Test
	public void listsNormalMethods() {
		assertTrue(accessor.getMethods().stream().anyMatch(m -> m.getSignature().name.equals("normalMethod")));
	}

	@Test
	public void listsDefaultMethods() {
		assertTrue(accessor.getMethods().stream().anyMatch(m -> m.getSignature().name.equals("defaultMethod")));
	}

	@Test
	public void doesNotListStaticMethods() {
		assertFalse(accessor.getMethods().stream().anyMatch(m -> m.getSignature().name.equals("staticMethod")));
	}

}
