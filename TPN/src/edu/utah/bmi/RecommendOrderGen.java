package edu.utah.bmi;

import java.util.ArrayList;
import java.util.TreeMap;

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
	// The csv file that store the reference range information;
	public static final String ReferenceFile = "";
	// When referenceRanges is called at the 1st time, it will be initiated through reading ReferenceFile .
	public static TreeMap<String, IngredientReferenceRange> referenceRanges = new TreeMap<String, IngredientReferenceRange>();

	public static void calculate(Patient patient) {
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
	public static void caclulateMedications(Patient patient) {
		// 27370 FAT EMULSION 20 % IV EMUL 0.2 g　2 kcal
		med27370 = patient.inputLipid_g / 0.2;

		// 17421 POTASSIUM PHOSPHATE DIBASIC 3 MMOLE/ML IV SOLN 3 mmol　4.4 mEq

		// 17371 POTASSIUM CHLORIDE 2 MEQ/ML IV SOLN 2 mEq

		// 12722 MAGNESIUM SULFATE 50 % IJ SOLN 500 mg 4.06 mEq
		med12722 = patient.inputMg_mEq / 4.06;
		// 3751 CALCIUM GLUCONATE 10 % IV SOLN 100 mg 0.465 mEq
		med3751 = patient.inputCa_mEq / 0.465;
		// 19699 SODIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq

		// 19769 SODIUM PHOSPHATE 3 MMOLE/ML IV SOLN 3 mmol 4 mEq
		med19769 = patient.inputP_mEq / 4;
		// 17358 POTASSIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq
		med17358 = patient.inputK_mEq / 2;
		// 251304 CLINISOL SF 15 % IV SOLN 0.15 g 0.6 kcal
		med251304 = patient.inputProtein_g / 0.15;

		// 19726 SODIUM CHLORIDE 4 MEQ/ML IV SOLN 0.234 g 4 mEq
		med19726 = patient.inputNa_mEq / 4 - med19769;

		// 6392 DEXTROSE 70 % IV SOLN 0.7 g 2.38 kcal
		med6392 = (patient.inputKcal - med27370 * 2) / 2.38;

		setPatientPrescription(patient);

	}

	private static void setPatientPrescription(Patient pt) {

		// calculate the first table;

		// need to be checked all following calculation is based on these three variables.
		pt.lipid_g_day = pt.inputLipid_g;
		pt.dextrose_g_day = med6392 * 0.7;
		pt.protein_g_day = med251304 * 0.15;

		pt.dextrose_g_kg = pt.dextrose_g_day / pt.weight;
		pt.protein_g_kg = pt.protein_g_day / pt.weight;
		pt.lipid_g_kg = pt.lipid_g_day / pt.weight;

		pt.dextrose_cal_kg = pt.dextrose_g_kg * 2.38;
		pt.protein_cal_kg = pt.protein_g_kg * 0.6;
		pt.lipid_cal_kg = pt.lipid_g_kg * 2;
		pt.total_cal_kg = pt.dextrose_cal_kg + pt.lipid_cal_kg
				+ pt.protein_cal_kg;

		pt.dextrose_perc_cal = pt.dextrose_cal_kg / pt.total_cal_kg * 100;
		pt.protein_perc_cal = pt.protein_cal_kg / pt.total_cal_kg * 100;
		pt.lipid_perc_cal = pt.lipid_cal_kg / pt.total_cal_kg * 100;
		pt.total_perc_cal = 100;

		// calculate the 2nd table;

		pt.na_meq_l = pt.inputNa_mEq / pt.inputFluid;
		pt.k_meq_kg_h = pt.inputKPerKg / pt.hours;
		pt.k_meq_l = pt.inputK_mEq / pt.inputFluid;

		// need to check if the factor is 6.25;
		pt.npcal_g_nit = (pt.lipid_g_day * 2 + pt.dextrose_g_day * 2.38)
				/ pt.protein_g_day * 6.25;
		// need to check (P*2 because NaPOS has been *2 when computing Na)
		pt.tpn_wo_lipid_mosm_l = (pt.protein_g_day * 10 + pt.dextrose_g_day * 5
				+ pt.inputNa_mEq * 2 + pt.inputP_mEq * 2 + pt.inputMg_mEq
				+ pt.inputK_mEq * 2 + pt.inputCa_mEq * 1.46)
				/ pt.inputFluid;

		pt.ca_mg_kg_day = pt.inputCaPerKg;
		pt.p_mg_kg_day = pt.inputPPerKg;
		pt.ca_mg_p_mg = pt.ca_mg_kg_day / pt.p_mg_kg_day;

		// need to check this function
		pt.ca_p_perc_aa = (pt.ca_mg_kg_day * pt.p_mg_kg_day) / pt.protein_g_kg;

		// pt.precip_limit=***
		// need to check????
		pt.al_mcg_kg_day = pt.requiredAlbumin * 1000000;
	}

	/**
	 * return warning ingredients (variable names)
	 * 
	 * @return
	 */
	public static Alerts dosageAlerts(Patient pt) {
		if (referenceRanges == null) {

		}
		Alerts alerts = new Alerts();

		return alerts;
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
