package edu.utah.bmi;

/**
 * Based on ingradient requirment, generate recommend medication list,
 * calculate the actual Kcal and fluid volume
 * Data provided by David ElHalta
 * 
 * <p>27370 FAT EMULSION 20 % IV EMUL 0.2 g　2 kcal</p>
 * 
 * <p>19726 SODIUM CHLORIDE 4 MEQ/ML IV SOLN 0.234 g 4 mEq</p>
 * 
 * <p>17421 POTASSIUM PHOSPHATE DIBASIC 3 MMOLE/ML IV SOLN 3 mmol　4.4 mEq</p>
 * 
 * <p>17371 POTASSIUM CHLORIDE 2 MEQ/ML IV SOLN 2 mEq</p>
 * 
 * <p>12722 MAGNESIUM SULFATE 50 % IJ SOLN 500 mg 4.06 mEq</p>
 * 
 * <p>3751 CALCIUM GLUCONATE 10 % IV SOLN 100 mg 0.465 mEq</p>
 * 
 * <p>19699 SODIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq</p>
 * 
 * <p>19769 SODIUM PHOSPHATE 3 MMOLE/ML IV SOLN 3 mmol 4 mEq</p>
 * 
 * <p>17358 POTASSIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq</p>
 * 
 * <p>251304 CLINISOL SF 15 % IV SOLN 0.15 g 0.6 kcal</p>
 * 
 * <p>6392 DEXTROSE 70 % IV SOLN 0.7 g 2.38 kcal</p>
 * 
 * 
 * @author Jianlin Shi
 * 
 */
public class RecommendOrderGen {

	// The following medications are in "ml" unit.
	public static double med27370, med19726, med17421, med17371, med12722,
			med3751, med19699, med19769, med17358, med251304, med6392;
	

	public static void calculate(TPN patient) {
		caclulateMedications(patient);
		// calculate the actual volume of total fluid
		patient.actFluid = Math.round(med27370 + med12722 + med3751 + med19769
				+ med17358 + med251304 + med19726 + med6392);
		patient.actKcal = (med27370 * 2 + med251304 * 0.6 + med6392 * 2.38);
	}


	/**
	 * Based on the medications' ingredients, calculate the recommended dose for each medication.
	 * 
	 * @param patient
	 */
	public static void caclulateMedications(TPN patient) {
		// 27370 FAT EMULSION 20 % IV EMUL 0.2 g　2 kcal
		med27370 = patient.inputFat_g / 0.2;

		// 17421 POTASSIUM PHOSPHATE DIBASIC 3 MMOLE/ML IV SOLN 3 mmol　4.4 mEq

		// 17371 POTASSIUM CHLORIDE 2 MEQ/ML IV SOLN 2 mEq

		// 12722 MAGNESIUM SULFATE 50 % IJ SOLN 500 mg 4.06 mEq
		med12722 = patient.inputMagnesium_mEq / 4.06;
		// 3751 CALCIUM GLUCONATE 10 % IV SOLN 100 mg 0.465 mEq
		med3751 = patient.inputCalcium_mEq / 0.465;
		// 19699 SODIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq

		// 19769 SODIUM PHOSPHATE 3 MMOLE/ML IV SOLN 3 mmol 4 mEq
		med19769 = patient.inputPhosphorus_mEq / 4;
		// 17358 POTASSIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq
		med17358 = patient.inputPotassium_mEq / 2;
		// 251304 CLINISOL SF 15 % IV SOLN 0.15 g 0.6 kcal
		med251304 = patient.inputProtein_g / 0.15;

		// 19726 SODIUM CHLORIDE 4 MEQ/ML IV SOLN 0.234 g 4 mEq
		med19726 = (patient.inputSodium_mEq - med19769 * 4) / 4;

		// 6392 DEXTROSE 70 % IV SOLN 0.7 g 2.38 kcal
		med6392 = (patient.inputKcal - med27370 * 2) / 2.38;



	}

	public static void printMeds() {
		System.out
				.println("27370 FAT EMULSION 20 % IV EMUL 0.2 g　2 kcal\n"
						+ med27370
						+ "\n19726 SODIUM CHLORIDE 4 MEQ/ML IV SOLN 0.234 g 4 mEq\n"
						+ med19726
						+ "\n17421 POTASSIUM PHOSPHATE DIBASIC 3 MMOLE/ML IV SOLN 3 mmol　4.4 mEq\n"
						+ med17421
						+ "\n17371 POTASSIUM CHLORIDE 2 MEQ/ML IV SOLN 2 mEq\n"
						+ med17371
						+ "\n12722 MAGNESIUM SULFATE 50 % IJ SOLN 500 mg 4.06 mEq\n"
						+ med12722
						+ "\n3751 CALCIUM GLUCONATE 10 % IV SOLN 100 mg 0.465 mEq\n"
						+ med3751
						+ "\n19699 SODIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq\n"
						+ med19699
						+ "\n19769 SODIUM PHOSPHATE 3 MMOLE/ML IV SOLN 3 mmol 4 mEq\n"
						+ med19769
						+ "\n17358 POTASSIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq\n"
						+ med17358
						+ "\n251304 CLINISOL SF 15 % IV SOLN 0.15 g 0.6 kcal\n"
						+ med251304
						+ "\n6392 DEXTROSE 70 % IV SOLN 0.7 g 2.38 kcal\n"
						+ med6392);
	}

}
