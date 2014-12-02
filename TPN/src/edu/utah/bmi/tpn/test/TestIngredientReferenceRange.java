package edu.utah.bmi.tpn.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.utah.bmi.tpn.objects.IngredientReferenceRange;

/**
 * Test the clonable class IngredientReferenceRange
 * 
 * @author Jianlin Shi
 * 
 */
public class TestIngredientReferenceRange {

	@org.junit.Test
	public void test() {

		IngredientReferenceRange irr = new IngredientReferenceRange();
		irr.ageHigherBound = 10;
		// if using clone
		IngredientReferenceRange irr2 = irr.clone();
		irr2.ageHigherBound = 20;
		assertTrue(irr.ageHigherBound == 10);
		irr.ageHigherBound = 30;
		assertTrue(irr2.ageHigherBound == 20);
		assertTrue(irr.ageHigherBound == 30);
		// if not using clone
		irr2 = irr;
		irr.ageHigherBound = 40;
		assertTrue(irr2.ageHigherBound == 40);		
		

	}

}
