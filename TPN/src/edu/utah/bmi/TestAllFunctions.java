package edu.utah.bmi;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class TestAllFunctions {

	@Test
	public void testCalSurfaceArea() {
		assertTrue(TPN.calSurfaceArea(56.0,173.0)==1.64);		
	}
	
	@Test
	public void testCalBMI() {
		assertTrue(TPN.calBMI(56.0,173.0)==18.7);		
	}

	@Test
	public void testCal() {		
		assertTrue(TPN.calRequiredRestKcal(35, 1, 65.0,175.0)==1601);		
	}

}
