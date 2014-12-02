package edu.utah.bmi.tpn.functions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.utah.bmi.tpn.objects.Alerts;
import edu.utah.bmi.tpn.objects.IVTYPE;
import edu.utah.bmi.tpn.objects.IngredientReferenceRange;
import edu.utah.bmi.tpn.objects.Patient;

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
	public static final String ReferenceFile = "resources/TPNReferences.csv";

	public static final int NotSpecified = -1;

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
		med19769 = patient.inputP_mmol / 4;
		// 17358 POTASSIUM ACETATE 2 MEQ/ML IV SOLN 2 mEq
		med17358 = patient.inputK_mEq / 2;
		// 251304 CLINISOL SF 15 % IV SOLN 0.15 g 0.6 kcal
		med251304 = patient.inputProtein_g / 0.15;

		// 19726 SODIUM CHLORIDE 4 MEQ/ML IV SOLN 0.234 g 4 mEq
		med19726 = patient.inputNa_mEq / 4 - med19769;

		// 6392 DEXTROSE 70 % IV SOLN 0.7 g 2.38 kcal
		if (patient.inputDextrose_perc == 0) {
			med6392 = (patient.inputKcal - med27370 * 2) / 2.38;
			patient.inputDextrose_perc = med6392 / patient.inputVolumePerKg;
		} else {
			med6392 = patient.inputVolumePerKg * patient.weight
					* patient.inputDextrose_perc;
		}
		// (patient.inputKcal - med27370 * 2) / 2.38;

		outputMetrics(patient);

	}

	private static void outputMetrics(Patient pt) {

		// calculate the top left table;
		pt.lipid_ml_day = med27370;
		pt.total_ml_day = pt.inputVolumePerKg * pt.weight;
		pt.pn_ml_day = pt.total_ml_day - pt.lipid_ml_day;

		pt.lipid_ml_kg = pt.lipid_ml_day / pt.weight;
		pt.pn_ml_kg = pt.pn_ml_day / pt.weight;
		pt.total_ml_kg = pt.total_ml_day / pt.weight;

		// calculate the top right table;
		pt.twoInOne_mosm_l = (pt.protein_g_day * 10 + pt.dextrose_g_day * 5
				+ pt.inputNa_mEq * 2 + pt.inputP_mmol * 2 + pt.inputMg_mEq
				+ pt.inputK_mEq * 2 + pt.inputCa_mEq * 1.46)
				/ pt.inputTotalVolume_ml*1000;
		pt.lipid_mosm_l = pt.lipid_ml_day * 0.26 / pt.inputTotalVolume_ml*1000;
		pt.threeInOne_mosm_l = pt.twoInOne_mosm_l + pt.lipid_mosm_l;

		// calculate middle table
		pt.lipid_g_day = pt.inputLipid_g;
		pt.dextrose_g_day = med6392 * 0.7;
		pt.protein_g_day = med251304 * 0.15;
		pt.total_g_day = pt.lipid_g_day + pt.dextrose_g_day + pt.protein_g_day;

		pt.dextrose_g_kg = pt.dextrose_g_day / pt.weight;
		pt.protein_g_kg = pt.protein_g_day / pt.weight;
		pt.lipid_g_kg = pt.lipid_g_day / pt.weight;
		pt.total_g_kg = pt.total_g_day / pt.weight;

		pt.dextrose_cal_kg = pt.dextrose_g_kg * 2.38;
		pt.protein_cal_kg = pt.protein_g_kg * 0.6;
		pt.lipid_cal_kg = pt.lipid_g_kg * 2;
		pt.total_cal_kg = pt.dextrose_cal_kg + pt.lipid_cal_kg
				+ pt.protein_cal_kg;

		pt.dextrose_perc_cal = pt.dextrose_cal_kg / pt.total_cal_kg * 100;
		pt.protein_perc_cal = pt.protein_cal_kg / pt.total_cal_kg * 100;
		pt.lipid_perc_cal = pt.lipid_cal_kg / pt.total_cal_kg * 100;
		pt.total_perc_cal = 100;

		// calculate the 2nd middle table;
		pt.dex_mgKgMin = pt.dextrose_g_kg * 1000 / (pt.pnhours * 60);
		pt.na_meq_l = pt.inputNa_mEq / pt.inputTotalVolume_ml*1000;
		pt.k_meq_kg_h = pt.inputKPerKg / pt.pnhours;
		pt.k_meq_l = pt.inputK_mEq / pt.inputTotalVolume_ml*1000;

		pt.cl_mEq_l = pt.inputCl_mEq / pt.inputTotalVolume_ml*1000;

		pt.kcal_n_ratio = (pt.lipid_g_day * 2 + pt.dextrose_g_day * 2.38 + pt.protein_g_day * 0.6)
				/ pt.protein_g_day * 6.25;

		pt.ca_mEq_day = pt.inputCa_mEq;
		pt.p_mmol_day = pt.inputP_mmol;
		pt.ca_p_ratio = pt.ca_mEq_day / pt.p_mmol_day;
		pt.ca_mg_x_p_mg = pt.ca_mEq_day * 20 * pt.p_mmol_day / 31;

		// need to check if the factor is 6.25;
		pt.npcal_g_nit = (pt.lipid_g_day * 2 + pt.dextrose_g_day * 2.38)
				/ pt.protein_g_day * 6.25;
		// faked number for now
		pt.precip_limit = 78.0;
		
		pt.lipid_rate=pt.lipid_ml_day/pt.lipidhours;
		pt.pn_rate=pt.pn_ml_day/pt.lipidhours;
		
		
		

	}

	/**
	 * return warning ingredients (variable names)
	 * 
	 * @return
	 */
	public static Alerts dosageAlerts(Patient pt) {
		if (referenceRanges == null || referenceRanges.size() == 0) {
			initiateReferences(ReferenceFile);
		}
		calculation4Cheking(pt);
		Alerts alerts = new Alerts();
		alerts.clear();
		// iterate all applicable reference ranges to check the variables in Patient object
		for (Entry<String, IngredientReferenceRange> entry : referenceRanges
				.entrySet()) {
			IngredientReferenceRange irr = entry.getValue();

			String checkingVariableName = irr.checkingVariableName;

			System.out.print("checking ingredient: " + checkingVariableName
					+ "\t");
			try {
				// if the variable does exists in Patient class
				if (Patient.class.getField(checkingVariableName) != null) {
					// if the patient's age is within the range or the age range is not specified in knowledge base
					if ((irr.ageLowerBound == NotSpecified || pt.age > irr.ageLowerBound)
							&& (irr.ageHigherBound == NotSpecified || pt.age <= irr.ageHigherBound)
							// if the patient's iv method is the same as knowledge base or the iv method is not specified in knowledge base
							&& ((pt.ivType == irr.ivType || irr.ivType == IVTYPE.NotSpecified) && irr.ivType != IVTYPE.Deep)
							// if the patient's gender is the same as knowledge base or the gender is not specified in knowledge base
							&& (pt.gender == irr.gender || irr.gender == NotSpecified)) {
						
						
						System.out.println(Patient.class.getField(
								checkingVariableName).get(pt));

						// there can be a case that when one variable is checked, but several variables are involved. e.g. "Ca:P ratio"
						if ((Double) Patient.class.getField(
								checkingVariableName).get(pt) >= irr.unacceptable && irr.unacceptable!=-1) {

							alerts.addUnacceptable(Arrays
									.asList(checkingVariableName
											.split("[\\|;\\s]")));
							System.out.println("\t\t---unacceptable");
						} else if ((Double) Patient.class.getField(
								checkingVariableName).get(pt) >= irr.warning && irr.warning!=-1) {

							alerts.addWarning(Arrays
									.asList(checkingVariableName
											.split("[,;\\s]")));
							System.out.println("\t\t---warning");
						} else {
							// System.out
							// .println(". The dosage is within reference range.");
						}
					}
				} else {
					System.out
							.println("Class Patient doesn't have any field named: "
									+ checkingVariableName);
				}

			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return alerts;
	}

	/**
	 * Finish additional variable calculation for dosage checking
	 * 
	 * @param pt
	 */
	private static void calculation4Cheking(Patient pt) {
		pt.inputNa_mEq_l = pt.inputNa_mEq / pt.inputTotalVolume_ml;
		pt.inputK_mEq_l = pt.inputK_mEq / pt.inputTotalVolume_ml;
		pt.inputCl_mEq_l = pt.inputCl_mEq / pt.inputTotalVolume_ml;
		pt.inputAcet_mEq_l = pt.inputAcet_mEq / pt.inputTotalVolume_ml;
		pt.inputZn_mg_l = pt.inputZn / pt.inputTotalVolume_ml;
		pt.inputRanitidine_mg_Kg = pt.inputRanitidine / pt.weight;

	}

	/**
	 * Read reference range from ReferenceFile
	 * 
	 * @param ReferenceFile
	 * 
	 * @return
	 */
	public static void initiateReferences(String ReferenceFile) {
		BufferedReader csvReader = null;
		String line = "";
		try {

			csvReader = new BufferedReader(new FileReader(ReferenceFile));
			while ((line = csvReader.readLine()) != null) {
				// skip first title row and blank rows
				if (line.startsWith("description") || line.startsWith(",,"))
					continue;
				// use comma as separator, keep blank columns
				String[] reference = line.split(",", -1);

				IngredientReferenceRange irr = new IngredientReferenceRange(
						reference);
				// ignore information that don't have a variableName
				if (!irr.checkingVariableName.equals(""))
					referenceRanges.put(irr.checkingVariableName, irr.clone());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

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
