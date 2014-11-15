package edu.utah.bmi;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class TestAllFunctions {

	@Test
	public void testCalSurfaceArea() {
		TPN patient1 = new TPN(35, 1, 60, 175, 1.3);
		assertTrue(patient1.calSurfaceArea(56.0,173.0)==1.64);		
	}
	
	@Test
	public void testCalBMI() {
		TPN patient1 = new TPN(35, 1, 60, 175, 1.3);
		assertTrue(patient1.calBMI(56.0,173.0)==18.7);		
	}

	@Test
	public void testCal() {	
		TPN patient1 = new TPN(35, 1, 60, 175, 1.3);
		assertTrue(patient1.calRequiredRestKcal(35, 1, 65.0,175.0)==1601);		
	}

}
