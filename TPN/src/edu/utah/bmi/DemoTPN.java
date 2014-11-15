package edu.utah.bmi;

import java.lang.reflect.Field;

/**
 * This is a demonstration of how to use
 * 
 * @author Jianlin Shi
 * 
 */
public class DemoTPN {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		TPN patient1 = new TPN(35, 1, 60, 175, 1.3);
		patient1.calWithoutLab();

		// use default input for following TPN calculation
		patient1.useRecommendedInput();

		// *******you can adjust ingredients before calculate recommended orders through:*******
		// patient1.updateInput(.....);

		// calculate the needed medications and actual fluids, kcal.
		RecommendOrderGen.calculate(patient1);

		// print out the results:
		Field[] fields = patient1.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getName() + "\t" + field.get(patient1));
		}

		RecommendOrderGen.printMeds();
	}

}
