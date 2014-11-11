package edu.utah.bmi;

/**
 * This is the core class for TPN calculation, some of the functions are derived from WUSTL TPN Calculator
 * http://tpn.wustl.edu/calculator.html
 * 
 * @author Jianlin Shi
 * 
 */
public class TPN {
	public static final int FEMALE = 0;
	public static final int MALE = 1;

	/**
	 * calculate the TPN without lab test input
	 * 
	 * stressor=1.0, when Ventilated/Sedated
	 * stressor=1.3, when No Stressors
	 * stressor=1.4, when Cardiac Disorder or Chronic Lung Disease
	 * stressor=1.7, when Burns
	 * 
	 * @return surfaceArea, BMI, fluid, kcal, sodium, potassium, calcium, magnesium, phosphorus, protein, fat
	 */
	public static double[] calWithoutLab(double age, int gender, double weight,
			double height, double stressor) {
		double[] output = new double[11];
		output[0] = calSurfaceArea(weight, height);
		output[1] = calBMI(weight, height);
		output[2] = calRequiredFluid(weight, height);
		output[3] = calRequiredAdjustedKcal(age, gender, weight, height,
				stressor);
		output[4] = requiredSodium(weight, height);
		output[5] = requiredPotassium(weight, height);
		output[6] = requiredCalcium(weight, height);
		output[7] = requiredMagnesium(weight, height);
		output[8] = requiredPhosphorus(weight, height);
		output[9] = requiredProtein(weight, height);
		output[10] = requiredFat(weight, height);
		return output;
	}

	/**
	 * calculate surface area based on Mosteller formula
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public static double calSurfaceArea(double weight, double height) {
		return Math.round(Math.sqrt(weight * height) / 0.6) / 100.0;
	}

	/**
	 * calculate Body Mass Index
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public static double calBMI(double weight, double height) {

		return Math.round(100000 * weight / (height * height)) / 10.0;
	}

	/**
	 * calculate total required fluid volume, derived from WUSTL TPN Calculator
	 * http://tpn.wustl.edu/calculator.html
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public static double calRequiredFluid(double weight, double height) {
		double fluid = 0;
		if (weight <= .75) {
			fluid = 80 * weight;
		}
		if (weight >= .75 && weight * 1 < 1.25) {
			fluid = 80 * weight;
		}
		if (weight >= 1.25 && weight * 1 < 2) {
			fluid = 100 * weight;
		}
		if (weight >= 2 && weight * 1 <= 3) {
			fluid = 100 * weight;
		}
		if (weight > 3 && weight * 1 <= 10) {
			fluid = 100 * weight;
		}
		if (weight * 1 > 10) {
			// if not light, use this instead:
			fluid = 2000 * calSurfaceArea(weight, height);
			// else we're still at our original default for very light patients
			fluid = Math.round(fluid);
		}
		return fluid;
	}

	/**
	 * calculate total required adjusted calorie, derived from WUSTL TPN Calculator
	 * http://tpn.wustl.edu/calculator.html
	 * 
	 * @param age
	 * @param gender
	 * @param weight
	 * @param height
	 * @param stressor
	 * 
	 *            stressor=1.0, when Ventilated/Sedated
	 *            stressor=1.3, when No Stressors
	 *            stressor=1.4, when Cardiac Disorder or Chronic Lung Disease
	 *            stressor=1.7, when Burns
	 * 
	 * @return
	 */
	public static double calRequiredAdjustedKcal(double age, int gender,
			double weight, double height, double stressor) {
		return Math.round(calRequiredRestKcal(age, gender, weight, height)
				* stressor);
	}

	/**
	 * calculate total required basic calorie, derived from WUSTL TPN Calculator
	 * http://tpn.wustl.edu/calculator.html
	 * 
	 * @param age
	 * @param gender
	 * @param weight
	 * @param height
	 * @return
	 */
	public static double calRequiredRestKcal(double age, int gender,
			double weight, double height) {
		double kcal = 0;
		if (weight <= .75) {
			kcal = 30 * weight;
		}
		if (weight >= .75 && weight * 1 < 1.25) {
			kcal = 40 * weight;
		}
		if (weight >= 1.25 && weight * 1 < 2) {
			kcal = 50 * weight;
		}
		if (weight >= 2 && weight * 1 <= 3) {
			kcal = 50 * weight;
		}
		if (weight > 3 && weight * 1 <= 10) {
			kcal = 60 * weight;
		}
		if (weight * 1 > 10) {
			kcal = 60 * weight;
			// now replace kcal, depending on age:
		}
		if (age > 3) {
			// ree varies by age and sex:
			if (age * 1 > 10) {
				if (gender == FEMALE) {
					kcal = (655 + (1.85 * height) + (9.6 * weight) - (4.7 * age));
				} else { // "male"
					kcal = (66.5 + (5.00 * height) + (13.8 * weight) - (6.8 * age));
				}
			} else { // age between first and second max
				if (gender == FEMALE) {
					kcal = 499 + (22.5 * weight);
				} else { // "male"
					kcal = 495 + (22.7 * weight);
				}
			}
		}
		return Math.round(kcal);
	}

	/**
	 * The following electrolytes, protein and fat requirement equations are based on
	 * MacKay M, Farr F, Jones K. Pediatric parenteral nutrition via computerized worksheet and automated compounding. Nutr Clin Pract. 2000;15:130–137. et al.
	 * and
	 * Safe Practices for Parenteral Nutrition Formulations. National Advisory Group on Standards and Practice Guidelines for Parenteral Nutrition. JPEN J Parenter Enteral Nutr.
	 * 1998;22(2):49–66. doi: 10.1177/014860719802200249. Available from: http://dx.doi.org/10.1177/014860719802200249. [PubMed] [Cross Ref]
	 * and
	 * http://www.who.int/nutrition/publications/guidelines/sodium_intake_printversion.pdf
	 */

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public static double requiredSodium(double age, double weight) {
		double sodiumInmEq = 0.0;
		if (weight <= 2.5) {
			sodiumInmEq = 4.0;
		} else if (weight < 5) {
			sodiumInmEq = 3.0;
		} else if (weight < 10) {
			sodiumInmEq = 3.0;
		} else if (weight < 20) {
			sodiumInmEq = 3.0;
		} else if (weight < 50) {
			sodiumInmEq = 2.5;
		} else {
			sodiumInmEq = 2.0;
		}
		return sodiumInmEq * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public static double requiredPotassium(double age, double weight) {
		double potassiumInmEq = 0.0;
		if (weight < 50) {
			potassiumInmEq = 3.0;
		} else {
			potassiumInmEq = 1.5;
		}
		return potassiumInmEq * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public static double requiredCalcium(double age, double weight) {
		double calciumInmEq = 0.0;
		if (weight <= 2.5) {
			calciumInmEq = 2.8;
		} else if (weight < 5) {
			calciumInmEq = 2.5;
		} else if (weight < 10) {
			calciumInmEq = 1.5;
		} else if (weight < 20) {
			calciumInmEq = 1.0;
		} else if (weight < 50) {
			calciumInmEq = 0.5;
		} else {
			calciumInmEq = 0.3;
		}
		return calciumInmEq * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public static double requiredPhosphorus(double age, double weight) {
		double phosphorusInmEq = 0.0;
		if (weight <= 2.5) {
			phosphorusInmEq = 1.2;
		} else if (weight < 5) {
			phosphorusInmEq = 1.0;
		} else if (weight < 10) {
			phosphorusInmEq = 0.5;
		} else if (weight < 20) {
			phosphorusInmEq = 0.5;
		} else if (weight < 50) {
			phosphorusInmEq = 0.5;
		} else {
			phosphorusInmEq = 0.3;
		}
		return phosphorusInmEq * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public static double requiredMagnesium(double age, double weight) {
		return 0.3 * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mg/kg/day
	 */
	public static double requiredProtein(double age, double weight) {
		double proteinInmg = 0.0;
		if (weight < 50) {
			proteinInmg = 2.0;
		} else {
			proteinInmg = 1.0;
		}
		return proteinInmg * weight;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mg/kg/day
	 */
	public static double requiredFat(double age, double weight) {
		double fatInmg = 0.0;
		if (weight < 50) {
			fatInmg = 2.0;
		} else {
			fatInmg = 1.0;
		}
		return fatInmg * weight;
	}

}
