package edu.utah.bmi.tpn.objects;

/**
 * This the class to store all the patient information that needed for TPN calculation
 * and the information that the TPN calculator outputs.
 * 
 * @author Jianlin Shi
 * 
 */
public class Patient {


	public final int NotSpecified = -1;
	// 1st input---extract from database
	public double age, weight, height, stressor, sCr;
	public int gender;

	// *********************************************************** //

	// calculated results for 1st output
	public double surfaceArea, BMI, requiredTotalVolume, requiredAdjustedKcal,
			idealBodyWeight, adjustedBodyWeight, crcl;

	public double requiredDextrose_perc, requiredCysMgPerg;
	// calculated results for 2nd input
	public double requiredNaPerKg, requiredKPerKg, requiredClPerKg,
			requiredAcetPerKg, requiredCaPerKg, requiredMgPerKg,
			requiredPPerKg, requiredProteinPerKg, requiredLipidPerKg;
	// calculated (other ingredient)----for 2nd input
	public double requiredZn, requiredCu, requiredMn, requiredCr, requiredSel,
			requiredIo, requiredFe, requiredVitMix, requiredVitK, requiredVitC,
			requiredAlbumin, requiredInsulin,requiredRanitidine,requiredVolumePerKg;

	// *********************************************************** //
	
	// whether the reference range is for central line 1 for yes, 0 for no, -1 for not specified (default)
	public int ivType;
	
	// Extract from EMR, other fluids include other IV drugs that not included in TPN.
	public double otherFluid_ml;
	
	// 2nd input----for users to adjust
	public double inputProteinPerKg,inputDextrose_perc,inputCysMgPerg;
	public double inputTotalVolume_ml, inputKcal, inputNaPerKg, inputKPerKg,
			inputClPerKg, inputAcetPerKg, inputCaPerKg, inputMgPerKg,
			inputPPerKg, inputLipidPerKg;

	// 2nd input (other ingredient)----for users to adjust
	public double inputZn, inputCu, inputMn, inputCr, inputSel, inputIo,
			inputFe, inputVitMix, inputVitK, inputVitC, 
			inputAlbumin, inputInsulin,inputRanitidine,inputVolumePerKg;

	// how many hours are planned to iv
	public double pnhours,lipidhours;
	


	// intermediate output
	public double inputNa_mEq, inputK_mEq, inputCl_mEq, inputAcet_mEq,
			inputCa_mEq, inputP_mmol, inputMg_mEq, inputProtein_g,
			inputLipid_g;

	// *********************************************************** //

	// final output
	public double actFluid, actKcal;

	public double pn_ml_kg, pn_ml_day, lipid_ml_kg, lipid_ml_day, total_ml_kg,
			total_ml_day;

	public double twoInOne_mosm_l,lipid_mosm_l,threeInOne_mosm_l;

	public double dextrose_g_day, protein_g_day, lipid_g_day, total_g_day;
	public double dextrose_g_kg, protein_g_kg, lipid_g_kg, total_g_kg;
	public double dextrose_cal_kg, protein_cal_kg, lipid_cal_kg, total_cal_kg;
	public double dextrose_perc_cal, protein_perc_cal, lipid_perc_cal,
			total_perc_cal;
	public double dex_mgKgMin, na_meq_l, k_meq_kg_h, k_meq_l, cl_mEq_l, npcal_g_nit,
			kcal_n_ratio;

	public double ca_mEq_day, p_mmol_day, ca_p_ratio, ca_mg_x_p_mg,
			precip_limit;
	
	public double lipid_rate, pn_rate;

	// *********************************************************** //

	// additional variables needed for dosage check
	public double inputNa_mEq_l, inputK_mEq_l, inputCl_mEq_l, inputAcet_mEq_l,
			inputZn_mg_l;
	public double inputRanitidine_mg_Kg;
	
	/**
	 * 
	 * stressor=1.0, when Ventilated/Sedated
	 * stressor=1.3, when No Stressors
	 * stressor=1.4, when Cardiac Disorder or Chronic Lung Disease
	 * stressor=1.7, when Burns *
	 * 
	 */

	public Patient(double age, int gender, double weight, double height) {
		initiatePatient(age, gender, weight, height, 1.3, NotSpecified);
	}

	public Patient(double age, int gender, double weight, double height,
			double stressor) {
		initiatePatient(age, gender, weight, height, stressor, NotSpecified);
	}

	public Patient(double age, int gender, double weight, double height,
			int ivType) {
		initiatePatient(age, gender, weight, height, 1.3, ivType);
	}

	public Patient(double age, int gender, double weight, double height,
			double stressor, int ivType) {
		initiatePatient(age, gender, weight, height, stressor, ivType);
	}

	public void initiatePatient(double age, int gender, double weight,
			double height, double stressor, int ivType) {
		this.age = age;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
		this.stressor = stressor;
		this.ivType = ivType;
		this.sCr = NotSpecified;
	}

	// Extract from EMR, other fluids include other IV drugs that not included in TPN.
	public void setOtherFluidVolume(double ml) {
		this.otherFluid_ml = ml;
	}

	/**
	 * Set the serum creatinine result (extract from EMR), this is optional
	 * It can also be set when call RecommendOrderGen.calculateCrCl(Patient pt, double sCr);
	 * 
	 * @param sCr
	 */
	public void setSCr(double sCr) {
		this.sCr = sCr;
	}

}
