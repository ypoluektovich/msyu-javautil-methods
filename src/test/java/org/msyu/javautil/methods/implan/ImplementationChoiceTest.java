package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static java.util.Collections.singleton;
import static org.msyu.javautil.methods.implan.ImplementationPlanBuilder.planFor;
import static org.msyu.javautil.methods.implan.InterfaceAccessor.reflect;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ImplementationChoiceTest {

	MethodSignature sig;

	@BeforeMethod
	public void beforeMethod(Method testMethod) {
		sig = new MethodSignature(testMethod);
	}

	interface Simple {
		void simpleUnimplemented();
		default void simpleImplemented() {}
	}

	InterfacePlan simplePlan = planFor(reflect(singleton(Simple.class)));

	@Test
	public void simpleUnimplemented() {
		MethodPlan m = simplePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);
		assertNull(m.chosenIface);
		assertFalse(m.definedIn.isEmpty());
		assertTrue(m.implementedIn.isEmpty());
	}

	@Test
	public void simpleImplemented() {
		MethodPlan m = simplePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);
		assertNotNull(m.chosenIface);
		assertFalse(m.definedIn.isEmpty());
		assertFalse(m.implementedIn.isEmpty());
	}


	interface I0 extends I1, I2 {
		void d0();
		void d0d1();
		void d0d1d2();
		default void i0() {}
		default void i0d1i2() {}
	}

	interface I1 {
		void d0d1();
		void d0d1d2();
		void m0d1();
		void m0d1d2();
		void i0d1i2();
	}

	interface I2 {
		void d0d1d2();
		void m0d1d2();
		default void i0d1i2() {}
	}

	InterfacePlan inheritancePlan = planFor(reflect(singleton(I0.class)));

	@Test
	public void d0() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 1);
		assertEquals(m.implementedIn.size(), 0);
	}

	@Test
	public void d0d1() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 2);
		assertEquals(m.implementedIn.size(), 0);
	}

	@Test
	public void d0d1d2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 3);
		assertEquals(m.implementedIn.size(), 0);
	}

	@Test
	public void i0() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNotNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 1);
		assertEquals(m.implementedIn.size(), 1);
	}

	@Test
	public void i0d1i2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNotNull(m.chosenIface);
		assertTrue(m.chosenIface.getName().endsWith("I0"));
		assertEquals(m.definedIn.size(), 3);
		assertEquals(m.implementedIn.size(), 2);
	}

	@Test
	public void m0d1() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 1);
		assertEquals(m.implementedIn.size(), 0);
	}

	@Test
	public void m0d1d2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertNull(m.chosenIface);
		assertEquals(m.definedIn.size(), 2);
		assertEquals(m.implementedIn.size(), 0);
	}

}
