package edu.utah.bmi.tpn.functions;

import edu.utah.bmi.tpn.objects.Patient;

/**
 * 
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
public class TPNCalculator {

	public static final int FEMALE = 0;
	public static final int MALE = 1;
	public static final int NotSpecified = -1;

	public static void calWithoutLab(Patient pt) {
		calBodyWeights(pt);
		pt.surfaceArea = calSurfaceArea(pt.weight, pt.height);
		pt.BMI = calBMI(pt.weight, pt.height);
		pt.requiredTotalVolume = calRequiredFluid(pt.weight, pt.height);
		pt.requiredVolumePerKg = pt.requiredTotalVolume / pt.weight;
		pt.requiredAdjustedKcal = calRequiredAdjustedKcal(pt.age, pt.gender,
				pt.weight, pt.height, pt.stressor);
		// main ingredient
		pt.requiredNaPerKg = requiredSodiumPerKg(pt.age, pt.weight);
		pt.requiredKPerKg = requiredPotassiumPerKg(pt.age, pt.weight);
		pt.requiredClPerKg = pt.requiredNaPerKg;
		pt.requiredAcetPerKg = pt.requiredKPerKg;
		pt.requiredCaPerKg = requiredCalciumPerKg(pt.age, pt.weight);
		pt.requiredMgPerKg = requiredMagnesiumPerKg(pt.age, pt.weight);
		pt.requiredPPerKg = requiredPhosphorusPerKg(pt.age, pt.weight);
		pt.requiredProteinPerKg = requiredProteinPerKg(pt.age, pt.weight);
		pt.requiredLipidPerKg = requiredFatPerKg(pt.age, pt.weight);
		// other ingredient (need to check the KB last column's unit)
		pt.requiredZn = requiredZn_mcg_PerKg(pt.weight);
		pt.requiredCu = requiredCu_mcg_PerKg(pt.weight);
		pt.requiredMn = requiredMn_mcg_PerKg(pt.weight);
		pt.requiredCr = requiredCr_mcg_PerKg(pt.weight);
		pt.requiredSel = requiredSel_mcg_PerKg(pt.weight);
		pt.requiredIo = requiredIo_mcg_PerKg(pt.weight);

		// -1 means not default value, use these ingredients as needed
		pt.requiredFe = NotSpecified;
		pt.requiredVitMix = NotSpecified;
		pt.requiredVitK = NotSpecified;
		pt.requiredVitC = NotSpecified;
		pt.requiredAlbumin = NotSpecified;
		pt.requiredInsulin = NotSpecified;
		pt.requiredRanitidine = requiredRanitidine_mg(pt.weight);
		pt.requiredVolumePerKg = pt.requiredTotalVolume / pt.weight;
		// check this
		pt.requiredDextrose_perc = -1;
	}

	/**
	 * Calculate the creatinine clearance (CrCl) [ml/min/1.73m2] for the patient using
	 * 
	 * Cockcroft and Gault equation for adults (default)
	 * CrCl = [(140 – age in years) x weight] / (sCr x 72) (x 0.85 for females)
	 * 
	 * MDRD study for adults and Schwartz equation for patients <18 years old
	 * CrCl = 186.3 x (sCr)^-1.154  x  (age in years)^-0.203  x  1.212 (if patient is black)  x 0.742 (if female)
	 * 
	 * Schwartz equation for patients <18 years old
	 * CrCl = (hight x K)/sCr
	 * k = 0.45 for infants 1 to 52 weeks oldk = 0.55 for children 1 to 13 years oldk = 0.55 for adolescent females 13-18 years oldk = 0.7 for adolescent males 13-18 years old
	 * 
	 * @param pt
	 * @param bun
	 * @param sCr
	 * @return
	 */
	public static double calculateCrCl(Patient pt, double sCr, String method) {
		double crcl = 0;
		pt.sCr = sCr;
		if (pt.age < 1) {
			crcl = pt.height * 0.45 / sCr;
		} else if (pt.age < 13) {
			crcl = pt.height * 0.55 / sCr;
		} else if (pt.age < 18) {
			crcl = pt.height * ((pt.gender == FEMALE) ? 0.55 : 0.7) / sCr;
		} else if (method.equals("MDRD")) {
			crcl = 186.3 / Math.pow(sCr, 1.154) / Math.pow(pt.age, 0.203)
					* ((pt.gender == FEMALE) ? 1.212 : 0.742);
		} else {
			crcl = (140 - pt.age) * pt.weight
					/ (sCr * 72 * (pt.gender == FEMALE ? 0.85 : 1));
		}
		pt.crcl = crcl;
		return crcl;
	}

	/**
	 * @param pt
	 *            default method for adults is CG(Cockcroft and Gault equation)
	 * @param sCr
	 * @return
	 */
	public static double calculateCrCl(Patient pt, double sCr) {
		return calculateCrCl(pt, sCr, "CG");
	}

	public static double calculateCrCl(Patient pt) {
		if (pt.sCr == NotSpecified) {
			throw new Error(
					"\n\n Patient serum creatinine has not been set yet.\n"
							+ " Use TPNCalCulator.calculateCrCl(Patient pt, double sCr);\n"
							+ " or patient.setSCr(double sCr);\n");
		}
		return calculateCrCl(pt, pt.sCr);
	}

	private static double requiredZn_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 300;
		} else if (weight < 25) {
			output = 100;
		} else {
			output = 2500 / weight;
		}
		return output;
	}

	private static double requiredCu_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 20;
		} else if (weight < 25) {
			output = 20;
		} else {
			output = 500 / weight;
		}
		return output;
	}

	private static double requiredMn_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 5;
		} else if (weight < 25) {
			output = 5;
		} else {
			output = 150 / weight;
		}
		return output;
	}

	private static double requiredCr_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 0.14;
		} else if (weight < 25) {
			output = 0.14;
		} else {
			output = 10 / weight;
		}
		return output;
	}

	private static double requiredSel_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 3;
		} else if (weight < 25) {
			output = 3;
		} else {
			output = 30 / weight;
		}
		return output;
	}

	private static double requiredIo_mcg_PerKg(double weight) {
		// TODO Auto-generated method stub
		double output = NotSpecified;
		if (weight < 3) {
			output = 3;
		} else if (weight < 25) {
			output = 3;
		} else {
			output = 2;
		}
		return output;
	}

	private static double requiredRanitidine_mg(double weight) {
		return 1 * weight;
	}

	/**
	 * 
	 * Use recommended default values for medication calculation
	 * 
	 * @param pt
	 *            default iv hour=12
	 * 
	 */
	public static void useRecommendedInput(Patient pt) {
		updateInput(pt, pt.requiredProteinPerKg, pt.requiredDextrose_perc,
				pt.requiredCysMgPerg, pt.requiredNaPerKg, pt.requiredKPerKg,
				pt.requiredClPerKg, pt.requiredAcetPerKg, pt.requiredMgPerKg,
				pt.requiredCaPerKg, pt.requiredPPerKg, pt.requiredZn,
				pt.requiredCu, pt.requiredMn, pt.requiredCr, pt.requiredSel,
				pt.requiredIo, pt.requiredFe, pt.requiredVitMix,
				pt.requiredVitK, pt.requiredVitC, pt.requiredVolumePerKg,
				pt.requiredLipidPerKg, pt.requiredRanitidine,
				pt.requiredInsulin, pt.otherFluid_ml, 12,2, NotSpecified);
	}

	public static void updateInput(Patient pt, double inputProteinPerKg,
			double inputDextrose_perc, double inputCysMgPerg,
			double inputNaPerKg, double inputKPerKg, double inputClPerKg,
			double inputAcetPerKg, double inputMgPerKg, double inputCalcium,
			double inputPPerKg, double inputZn, double inputCu, double inputMn,
			double inputCr, double inputSel, double inputIo, double inputFe,
			double inputVitMix, double inputVitK, double inputVitC,
			double inputVolumePerKg, double inputLipidPerKg,
			double inputRanitidine, double inputInsulin, double otherFluid_ml,
			double pnhours, double lipidhours,int ivType) {
		// update inputTotalVolume_ml and inputKcal based on which variable the user changes
		// !!!!!!make sure each time only one variable is changed!!!!
		if (pt.inputVolumePerKg != inputVolumePerKg) {
			// if only total volume changed, means only add/reduce water
			pt.inputVolumePerKg = inputVolumePerKg;
			pt.inputDextrose_perc = pt.inputTotalVolume_ml
					* pt.inputDextrose_perc / (inputVolumePerKg * pt.weight);
			pt.inputTotalVolume_ml = inputVolumePerKg * pt.weight;
		} else if (pt.inputLipidPerKg != inputLipidPerKg) {
			// if lipid changed, then total kcal change accordingly, total volume will be assumed as the same by adjust water accordingly
			pt.inputKcal = pt.inputKcal
					+ (inputLipidPerKg * pt.weight - pt.inputLipid_g) * 2;
			pt.inputLipidPerKg = inputLipidPerKg;
		} else if (pt.inputDextrose_perc != inputDextrose_perc) {
			// if inputDextrose_perc changes, then fix the inputTotalVolume_ml, dextrose and total kcal will change
			// dextrose amount will be calculated in RecommendOrderGen
			pt.inputKcal = pt.inputKcal + pt.inputTotalVolume_ml
					* (inputDextrose_perc - pt.inputDextrose_perc) * 2.38;
			pt.inputDextrose_perc = inputDextrose_perc;
		} else if (pt.inputProteinPerKg != inputProteinPerKg) {
			// if protein changes, then total kcal changes accordingly, total volume will be assumed as the same by adjust water accordingly
			pt.inputKcal = pt.inputKcal
					+ (inputProteinPerKg - pt.inputProteinPerKg) * pt.weight
					* 0.6;
		} else {
			if (pt.inputKcal == 0)
				pt.inputKcal = pt.requiredAdjustedKcal;
			if (pt.inputTotalVolume_ml == 0)
				pt.inputTotalVolume_ml = pt.requiredTotalVolume;
		}

		if (pt.inputVolumePerKg == 0)
			pt.inputVolumePerKg = pt.requiredVolumePerKg;
		if (pt.inputLipidPerKg == 0)
			pt.inputLipidPerKg = pt.requiredLipidPerKg;
		// there is no default value for inputDextrose_perc, till the medications is generated,
		// because the dextrose are computed through MacKay's table by subtracting the liqid kcal
		// from total kcal
		if (pt.inputDextrose_perc == 0 && pt.requiredDextrose_perc != -1) {
			pt.inputDextrose_perc = pt.requiredDextrose_perc;
		} else if (pt.inputDextrose_perc == 0 && pt.dextrose_g_day != 0) {
			pt.inputDextrose_perc = pt.dextrose_g_day / 0.7
					/ pt.inputTotalVolume_ml;
		}

		if (pt.inputProteinPerKg == 0)
			pt.inputProteinPerKg = pt.requiredProteinPerKg;
		if (pt.inputKcal == 0)
			pt.inputKcal = pt.requiredAdjustedKcal;

		pt.inputNaPerKg = inputNaPerKg;
		pt.inputKPerKg = inputKPerKg;
		pt.inputClPerKg = inputClPerKg;
		pt.inputAcetPerKg = inputAcetPerKg;
		pt.inputCaPerKg = inputCalcium;
		pt.inputMgPerKg = inputMgPerKg;
		pt.inputPPerKg = inputPPerKg;

		pt.inputZn = inputZn;
		pt.inputCu = inputCu;
		pt.inputMn = inputMn;
		pt.inputCr = inputCr;
		pt.inputSel = inputSel;
		pt.inputIo = inputIo;
		pt.inputFe = inputFe;
		pt.inputVitMix = inputVitMix;
		pt.inputVitK = inputVitK;
		pt.inputVitC = inputVitC;
		pt.inputInsulin = inputInsulin;
		pt.inputRanitidine = inputRanitidine;
		pt.otherFluid_ml = otherFluid_ml;

		pt.pnhours = pnhours;
		pt.lipidhours=lipidhours;

		pt.inputNa_mEq = pt.inputNaPerKg * pt.weight;
		pt.inputK_mEq = pt.inputKPerKg * pt.weight;
		pt.inputCl_mEq = pt.inputClPerKg * pt.weight;
		pt.inputAcet_mEq = pt.inputAcetPerKg * pt.weight;
		pt.inputCa_mEq = pt.inputCaPerKg * pt.weight;
		pt.inputP_mmol = pt.inputPPerKg * pt.weight;
		pt.inputMg_mEq = pt.inputMgPerKg * pt.weight;
		pt.inputProtein_g = pt.inputProteinPerKg * pt.weight;
		pt.inputLipid_g = pt.inputLipidPerKg * pt.weight;

	}

	/**
	 * This formula is derived from Scott's notes, converted to metrics.
	 * https://uofu.app.box.com/files/0/f/2672749645/1/f_22770958675
	 * 
	 * @param gender
	 * @param weight
	 * @param height
	 * @return adjustedBodyWeight, idealBodyWeight
	 */
	private static void calBodyWeights(Patient pt) {
		if (pt.gender == FEMALE) {
			pt.idealBodyWeight = (45.5 + 2.3 * (pt.height * 0.39 - 60));
		} else {
			pt.idealBodyWeight = (50 + 2.3 * (pt.height * 0.39 - 60));
		}
		pt.adjustedBodyWeight = 0.6 * pt.idealBodyWeight + 0.4 * pt.weight;

		// rule from Scott's note
		if (pt.weight > 1.3 * pt.idealBodyWeight) {
			pt.weight = pt.adjustedBodyWeight;
		}
	}

	/**
	 * calculate surface area based on Mosteller formula
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	private static double calSurfaceArea(double weight, double height) {
		return Math.round(Math.sqrt(weight * height) / 0.6) / 100.0;
	}

	/**
	 * calculate Body Mass Index
	 * 
	 * @param weight
	 * @param height
	 * @return
	 */
	private static double calBMI(double weight, double height) {

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
	private static double calRequiredFluid(double weight, double height) {
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
	private static double calRequiredAdjustedKcal(double age, int gender,
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
	private static double calRequiredRestKcal(double age, int gender,
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
	private static double requiredSodiumPerKg(double age, double weight) {
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
	private static double requiredPotassiumPerKg(double age, double weight) {
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
	private static double requiredCalciumPerKg(double age, double weight) {
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
	private static double requiredPhosphorusPerKg(double age, double weight) {
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
	private static double requiredMagnesiumPerKg(double age, double weight) {
		
			return 0.3;
	}

	/**
	 * @param age
	 * @param weight
	 * @return g/kg/day
	 */
	private static double requiredProteinPerKg(double age, double weight) {
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
	private static double requiredFatPerKg(double age, double weight) {
		double fatInmg = 0.0;
		if (weight < 50) {
			fatInmg = 2.0;
		} else {
			fatInmg = 0.5;
		}
		return fatInmg;
	}

}
