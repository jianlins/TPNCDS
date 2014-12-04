package edu.utah.bmi.tpn.test;

import edu.utah.bmi.tpn.functions.RecommendOrderGen;
import edu.utah.bmi.tpn.functions.TPNCalculator;
import edu.utah.bmi.tpn.objects.Alerts;
import edu.utah.bmi.tpn.objects.Patient;

public class TestRepeat {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		Patient pt = new Patient(35, 1, 60, 175);
		// set other fluids
		pt.otherFluid_ml = 1000;

		TPNCalculator.calWithoutLab(pt);

		// use default input for following TPN calculation
		TPNCalculator.useRecommendedInput(pt);

		RecommendOrderGen.calculate(pt);

		System.out.println(pt.requiredDextrose_perc);
		System.out.println(pt.inputDextrose_perc);		
		System.out.println(pt.threeInOne_mosm_l+"\n");

		

		TPNCalculator.updateInput(pt, pt.inputProteinPerKg,
				pt.inputDextrose_perc, pt.inputCysMgPerg, pt.inputNaPerKg,
				pt.inputKPerKg, pt.inputClPerKg, pt.inputAcetPerKg,
				pt.inputMgPerKg, pt.inputCaPerKg, pt.inputPPerKg, pt.inputZn,
				pt.inputCu, pt.inputMn, pt.inputCr, pt.inputSel, pt.inputIo,
				pt.inputFe, pt.inputVitMix, pt.inputVitK, pt.inputVitC,
				pt.inputVolumePerKg, pt.inputLipidPerKg, pt.inputRanitidine,
				pt.inputInsulin, pt.otherFluid_ml, 12, 2, 1);
		
		RecommendOrderGen.calculate(pt);
		System.out.println(pt.requiredDextrose_perc);
		System.out.println(pt.inputDextrose_perc);		
		System.out.println(pt.threeInOne_mosm_l+"\n");

		
//		I assume you are using pt.inputDextrose_perc instead of -1 here. In fact the display of dextrose % should still use
//		pt.requiredDextrose_perc (-1 at this time)
		
//		You can only update the inputxxx variable from the user input, 
		
		TPNCalculator.updateInput(pt, pt.inputProteinPerKg,
				pt.requiredDextrose_perc, pt.inputCysMgPerg, pt.inputNaPerKg,
				pt.inputKPerKg, pt.inputClPerKg, pt.inputAcetPerKg,
				pt.inputMgPerKg, pt.inputCaPerKg, pt.inputPPerKg, pt.inputZn,
				pt.inputCu, pt.inputMn, pt.inputCr, pt.inputSel, pt.inputIo,
				pt.inputFe, pt.inputVitMix, pt.inputVitK, pt.inputVitC,
				pt.inputVolumePerKg, pt.inputLipidPerKg, pt.inputRanitidine,
				pt.inputInsulin, pt.otherFluid_ml, 12, 2, 1);
		
		RecommendOrderGen.calculate(pt);
		System.out.println(pt.requiredDextrose_perc);
		System.out.println(pt.inputDextrose_perc);		
		System.out.println(pt.threeInOne_mosm_l+"\n");
//		DemoTPN.printVariables(pt);
		
		TPNCalculator.updateInput(pt, pt.inputProteinPerKg,
				pt.inputDextrose_perc, pt.inputCysMgPerg, pt.inputNaPerKg,
				pt.inputKPerKg, pt.inputClPerKg, pt.inputAcetPerKg,
				pt.inputMgPerKg, pt.inputCaPerKg, pt.inputPPerKg, pt.inputZn,
				pt.inputCu, pt.inputMn, pt.inputCr, pt.inputSel, pt.inputIo,
				pt.inputFe, pt.inputVitMix, pt.inputVitK, pt.inputVitC,
				pt.inputVolumePerKg, pt.inputLipidPerKg, pt.inputRanitidine,
				pt.inputInsulin, pt.otherFluid_ml, 12, 2, 1);
		
		RecommendOrderGen.calculate(pt);
		System.out.println(pt.requiredDextrose_perc);
		System.out.println(pt.inputDextrose_perc);		
		System.out.println(pt.threeInOne_mosm_l+"\n");

		
		TPNCalculator.updateInput(pt, pt.inputProteinPerKg,
				pt.inputDextrose_perc, pt.inputCysMgPerg, pt.inputNaPerKg,
				pt.inputKPerKg, pt.inputClPerKg, pt.inputAcetPerKg,
				pt.inputMgPerKg, pt.inputCaPerKg, pt.inputPPerKg, pt.inputZn,
				pt.inputCu, pt.inputMn, pt.inputCr, pt.inputSel, pt.inputIo,
				pt.inputFe, pt.inputVitMix, pt.inputVitK, pt.inputVitC,
				pt.inputVolumePerKg, pt.inputLipidPerKg, pt.inputRanitidine,
				pt.inputInsulin, pt.otherFluid_ml, 12, 2, 1);
		
		RecommendOrderGen.calculate(pt);
		System.out.println(pt.requiredDextrose_perc);
		System.out.println(pt.inputDextrose_perc);		
		System.out.println(pt.threeInOne_mosm_l+"\n");
		
		

	}

}
