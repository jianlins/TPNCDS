package edu.utah.bmi.tpn.objects;

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
public class Patient {

	// 1st input---extract from database
	public double age, weight, height, stressor;
	public int gender;
	// calculated results for 1st output
	public double surfaceArea, BMI, requiredfluid, requiredAdjustedKcal,
			idealBodyWeight, adjustedBodyWeight;
	
	public double dextrose_perc, Cys;
	// calculated results for 2nd input
	public double requiredNaPerKg, requiredKPerKg, requiredClPerKg,
			requiredAcetPerKg, requiredCaPerKg, requiredMgPerKg,
			requiredPPerKg, requiredProteinPerKg, requiredLipidPerKg;
	// calculated (other ingredient)----for 2nd input
	public double requiredZn, requiredCu, requiredMn, requiredCr, requiredSel,
			requiredIo, requiredFe, requiredVitMix, requiredVitK, requiredVitC,
			requiredHeparin, requiredAlbumin, requiredInsulin;
	// 2nd input----for users to adjust
	public double inputFluid, inputKcal, inputNaPerKg, inputKPerKg,
			inputClPerKg, inputAcetPerKg, inputCaPerKg, inputMgPerKg,
			inputPPerKg, inputProteinPerKg, inputLipidPerKg, hours;
	// 2nd input (other ingredient)----for users to adjust
	public double inputZn, inputCu, inputMn, inputCr, inputSel, inputIo,
			inputFe, inputVitMix, inputVitK, inputVitC, inputHeparin,
			inputAlbumin, inputInsulin;

	// intermediate output
	public double inputNa_mEq, inputK_mEq, inputCl_mEq, inputAcet_mEq,
			inputCa_mEq, inputP_mEq, inputMg_mEq, inputProtein_g, inputLipid_g;

	// final output
	public double actFluid, actKcal;

	public double dextrose_g_day, protein_g_day, lipid_g_day, total_g_day;
	public double dextrose_g_kg, protein_g_kg, lipid_g_kg, total_g_kg;
	public double dextrose_cal_kg, protein_cal_kg, lipid_cal_kg, total_cal_kg;
	public double dextrose_perc_cal, protein_perc_cal, lipid_perc_cal,
			total_perc_cal;
	public double na_meq_l, k_meq_kg_h, k_meq_l, npcal_g_nit,
			tpn_wo_lipid_mosm_l;
	public double ca_mg_kg_day, p_mg_kg_day, ca_mg_p_mg, ca_p_perc_aa,
			precip_limit, al_mcg_kg_day;

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

	public Patient(double age, int gender, double weight, double height,
			double stressor) {
		this.age = age;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
		this.stressor = stressor;
	}

	public Patient(double age, int gender, double weight, double height) {
		this.age = age;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
		this.stressor = 1.3;
	}

}
