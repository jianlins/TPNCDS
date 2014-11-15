package edu.utah.bmi;

/**
 * This is the core class for TPN calculation, some of the functions are derived from WUSTL TPN Calculator
 * http://tpn.wustl.edu/calculator.html
 * electrolytes, protein and fat functions are based on literatures, including:
 * 
 * MacKay M, Farr F, Jones K. Pediatric parenteral nutrition via computerized worksheet and automated compounding. Nutr Clin Pract. 2000;15:130–137. et al.
 * and
 * Safe Practices for Parenteral Nutrition Formulations. National Advisory Group on Standards and Practice Guidelines for Parenteral Nutrition. JPEN J Parenter Enteral Nutr.
 * 1998;22(2):49–66. doi: 10.1177/014860719802200249. Available from: http://dx.doi.org/10.1177/014860719802200249. [PubMed] [Cross Ref]
 * and
 * http://www.who.int/nutrition/publications/guidelines/sodium_intake_printversion.pdf
 * 
 * @author Jianlin Shi
 * 
 */
public class TPN {
	public static final int FEMALE = 0;
	public static final int MALE = 1;
	// 1st input---extract from database
	public double age, weight, height, stressor;
	public int gender;
	// calculated results for 1st output
	public double surfaceArea, BMI, requiredfluid, requiredAdjustedKcal,idealBodyWeight,adjustedBodyWeight;
	// calculated results for 2nd input
	public double requiredSodiumPerKg, requiredPotassiumPerKg,
			requiredChloridePerKg, requiredAcetatePerKg, requiredCalciumPerKg,
			requiredMagnesiumPerKg, requiredPhosphorusPerKg,
			requiredProteinPerKg, requiredFatPerKg;
	// 2nd input----for users to adjust
	public double inputFluid, inputKcal, inputSodiumPerKg, inputPotassiumPerKg,
			inputChloridePerKg, inputAcetatePerKg, inputCalciumPerKg,
			inputMagnesiumPerKg, inputPhosphorusPerKg, inputProteinPerKg,
			inputFatPerKg;
	// intermediate output
	public double inputSodium_mEq, inputPotassium_mEq, inputChloride_mEq,
			inputAcetate_mEq, inputCalcium_mEq, inputPhosphorus_mEq,
			inputMagnesium_mEq, inputProtein_g, inputFat_g;
	// 2nd output
	public double actFluid, actKcal;

	/**
	 * calculate the ingredient, Kcal, fluid requirements without lab test input
	 * 
	 * stressor=1.0, when Ventilated/Sedated
	 * stressor=1.3, when No Stressors
	 * stressor=1.4, when Cardiac Disorder or Chronic Lung Disease
	 * stressor=1.7, when Burns
	 * 
	 * @return (in order) surfaceArea, BMI, fluid, kcal, sodium, potassium,chloride, acetate, calcium, magnesium, phosphorus, protein, fat
	 */

	public TPN(double age, int gender, double weight, double height,
			double stressor) {
		this.age = age;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
		this.stressor = stressor;
	}

	public void calWithoutLab() {		
		calBodyWeights(gender,weight,height);
		surfaceArea = calSurfaceArea(weight, height);
		BMI = calBMI(weight, height);
		requiredfluid = calRequiredFluid(weight, height);
		requiredAdjustedKcal = calRequiredAdjustedKcal(age, gender, weight,
				height, stressor);
		requiredSodiumPerKg = requiredSodiumPerKg(age, weight);
		requiredPotassiumPerKg = requiredPotassiumPerKg(age, weight);
		requiredChloridePerKg = requiredSodiumPerKg;
		requiredAcetatePerKg = requiredPotassiumPerKg;
		requiredCalciumPerKg = requiredCalciumPerKg(age, weight);
		requiredMagnesiumPerKg = requiredMagnesiumPerKg(age, weight);
		requiredPhosphorusPerKg = requiredPhosphorusPerKg(age, weight);
		requiredProteinPerKg = requiredProteinPerKg(age, weight);
		requiredFatPerKg = requiredFatPerKg(age, weight);
	}
	
	
	public void useRecommendedInput() {
		updateInput(requiredfluid, requiredAdjustedKcal, requiredSodiumPerKg,
				requiredPotassiumPerKg, requiredChloridePerKg,
				requiredAcetatePerKg, requiredCalciumPerKg,
				requiredMagnesiumPerKg, requiredPhosphorusPerKg,
				requiredProteinPerKg, requiredFatPerKg);
	}

	public void updateInput(double inputFluid, double inputKcal,
			double inputSodium, double inputPotassium, double inputChloride,
			double inputAcetate, double inputCalcium, double inputMagnesium,
			double inputPhosphorus, double inputProtein, double inputFat) {
		this.inputFluid = inputFluid;
		this.inputKcal = inputKcal;
		this.inputSodiumPerKg = inputSodium;
		this.inputPotassiumPerKg = inputPotassium;
		this.inputChloridePerKg = inputChloride;
		this.inputAcetatePerKg = inputAcetate;
		this.inputCalciumPerKg = inputCalcium;
		this.inputMagnesiumPerKg = inputMagnesium;
		this.inputPhosphorusPerKg = inputPhosphorus;
		this.inputProteinPerKg = inputProtein;
		this.inputFatPerKg = inputFat;

		inputSodium_mEq = inputSodiumPerKg * weight;
		inputPotassium_mEq = inputPotassiumPerKg * weight;
		inputChloride_mEq = inputChloridePerKg * weight;
		inputAcetate_mEq = inputAcetatePerKg * weight;
		inputCalcium_mEq = inputCalciumPerKg * weight;
		inputPhosphorus_mEq = inputPhosphorusPerKg * weight;
		inputMagnesium_mEq = inputMagnesiumPerKg * weight;
		inputProtein_g = inputProteinPerKg * weight;
		inputFat_g = inputFatPerKg * weight;
	}
	
	
	/**
	 * This formula is derived from Scott's notes, converted to metrics. 
	 * https://uofu.app.box.com/files/0/f/2672749645/1/f_22770958675
	 * @param gender
	 * @param weight
	 * @param height
	 * @return adjustedBodyWeight, idealBodyWeight
	 */
	public void calBodyWeights(int gender, double weight,double height){		
		if(gender==FEMALE){
			idealBodyWeight=(45.5+2.3*(height*0.39-60));
		}else{
			idealBodyWeight=(50+2.3*(height*0.39-60));
		}
		adjustedBodyWeight=0.6*idealBodyWeight+0.4*weight;
		
//		rule from Scott's note
		if(weight>1.3*idealBodyWeight){
			weight=adjustedBodyWeight;
		}
	}

	/**
	 * calculate surface area based on Mosteller formula
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public double calSurfaceArea(double weight, double height) {
		return Math.round(Math.sqrt(weight * height) / 0.6) / 100.0;
	}

	/**
	 * calculate Body Mass Index
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	public double calBMI(double weight, double height) {

		return Math.round(100000 * weight / (height * height)) / 10.0;
	}

	/**
	 * calculate total required fluid volume, derived from WUSTL TPN Calculator
	 * http://tpn.wustl.edu/calculator.html
	 * 
	 * @param weight
	 * @param height
	 * @return ml/day
	 */
	public double calRequiredFluid(double weight, double height) {
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
	 * @return kcal/day
	 */
	public double calRequiredAdjustedKcal(double age, int gender,
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
	 * @return kcal/day
	 */
	public double calRequiredRestKcal(double age, int gender, double weight,
			double height) {
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
	public double requiredSodiumPerKg(double age, double weight) {
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
		return sodiumInmEq;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public double requiredPotassiumPerKg(double age, double weight) {
		double potassiumInmEq = 0.0;
		if (weight < 50) {
			potassiumInmEq = 3.0;
		} else {
			potassiumInmEq = 1.5;
		}
		return potassiumInmEq;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public double requiredCalciumPerKg(double age, double weight) {
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
		return calciumInmEq;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public double requiredPhosphorusPerKg(double age, double weight) {
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
		return phosphorusInmEq;
	}

	/**
	 * @param age
	 * @param weight
	 * @return mEq/kg/day
	 */
	public double requiredMagnesiumPerKg(double age, double weight) {
		return 0.3;
	}

	/**
	 * @param age
	 * @param weight
	 * @return g/kg/day
	 */
	public double requiredProteinPerKg(double age, double weight) {
		double proteinInmg = 0.0;
		if (weight < 20) {
			proteinInmg = 2.0;
		} else if (weight < 50) {
			proteinInmg = 1.25;
		} else {
			proteinInmg = 0.75;
		}
		return proteinInmg;
	}

	/**
	 * @param age
	 * @param weight
	 * @return g/kg/day
	 */
	public double requiredFatPerKg(double age, double weight) {
		double fatInmg = 0.0;
		if (weight < 50) {
			fatInmg = 2.0;
		} else {
			fatInmg = 0.5;
		}
		return fatInmg;
	}

}
