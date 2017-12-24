package org.msyu.javautil.methods.implan;

import org.msyu.javautil.methods.MethodSignature;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.msyu.javautil.methods.implan.ImplementationPlanBuilder.planFor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ImplementationChoiceTest {

	private MethodSignature sig;

	@BeforeMethod
	public void beforeMethod(Method testMethod) {
		sig = new MethodSignature(testMethod);
	}

	interface Simple {
		void simpleUnimplemented();
		default void simpleImplemented() {}
	}

	private InterfacePlan simplePlan = planFor(Simple.class);

	@Test
	public void simpleUnimplemented() {
		MethodPlan m = simplePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);
		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void simpleImplemented() {
		MethodPlan m = simplePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);
		assertTrue(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
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

	private InterfacePlan inheritancePlan = planFor(I0.class);

	@Test
	public void d0() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void d0d1() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void d0d1d2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void i0() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertTrue(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void i0d1i2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertTrue(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void m0d1() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

	@Test
	public void m0d1d2() {
		MethodPlan m = inheritancePlan.getPlanFor(sig);
		assertNotNull(m);
		assertEquals(m.signature, sig);

		assertFalse(m.hasOneImplementation());
		assertFalse(m.hasImplementationConflict());
	}

}
