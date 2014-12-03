package edu.utah.bmi.tpn.test;

import java.lang.reflect.Field;

import edu.utah.bmi.tpn.functions.RecommendOrderGen;
import edu.utah.bmi.tpn.functions.TPNCalculator;
import edu.utah.bmi.tpn.objects.Alerts;
import edu.utah.bmi.tpn.objects.IVTYPE;
import edu.utah.bmi.tpn.objects.Patient;

/**
 * This is a demonstration of how to use
 * 
 * @author Jianlin Shi
 * 
 */
public class TestTPN {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		Patient pt = new Patient(1, 0, 10, 75);
		// set other fluids
		pt.otherFluid_ml = 0;

		TPNCalculator.calWithoutLab(pt);

		// calculateCrCl has two methods options: "CG" or "MDRD". If the patient is child, the option will be ignored and directly use Schwartz equation
		// For the interface, you can show these two options for adults, and one option: "Schwartz" for child.

		// use default input for following TPN calculation
		TPNCalculator.useRecommendedInput(pt);

		// *******you can adjust ingredients before calculate recommended orders through:*******

		/**
		 * you can set ivtype in updateInput:
		 * 
		 * 
		 * 
		 * updateInput(Patient pt, double inputProteinPerKg,
		 * double inputDextrose_perc, double inputCysMgPerg,
		 * double inputNaPerKg, double inputKPerKg, double inputClPerKg,
		 * double inputAcetPerKg, double inputMgPerKg, double inputCalcium,
		 * double inputPPerKg, double inputZn, double inputCu, double inputMn,
		 * double inputCr, double inputSel, double inputIo, double inputFe,
		 * double inputVitMix, double inputVitK, double inputVitC,
		 * double inputVolumePerKg, double inputLipidPerKg,
		 * double inputRanitidine, double inputInsulin, double otherFluid_ml,
		 * double pnhours, double lipidhours,int ivType) {
		 * 
		 * Or
		 * 
		 * patient1.ivType=IVTYPE.CentralLine;
		 */

		// you can just directly use the number into the "updateInput" function.
		// The variables here are to show which position each variable should fit in
		// the "updateInput" function.
		pt.inputProteinPerKg = 1;
		pt.inputDextrose_perc = 10;
		pt.inputNaPerKg = 3;
		pt.inputKPerKg = 3;
		pt.inputClPerKg = 2.5;
		pt.inputAcetPerKg = 2.5;
		pt.inputMgPerKg = 0.3;
		pt.inputCaPerKg = 1.5;
		pt.inputPPerKg = 0.5;
		pt.inputZn = 100;
		pt.inputCu = 0;
		pt.inputMn = 0;
		pt.inputCr = 0;
		pt.inputSel = 0;
		pt.inputIo = 0;
		pt.inputVitMix = 5;
		pt.inputVitK = 200;
		pt.inputVitC = 80;
		pt.inputLipidPerKg = 1.5;
		pt.inputVolumePerKg = 100;

		pt.pnhours = 24;
		pt.lipidhours = 24;

		// TPNCalculator.updateInput();
		TPNCalculator.updateInput(pt, pt.inputProteinPerKg,
				pt.inputDextrose_perc, pt.requiredCysMgPerg, pt.inputNaPerKg,
				pt.inputKPerKg, pt.inputClPerKg, pt.inputAcetPerKg,
				pt.inputMgPerKg, pt.inputCaPerKg, pt.inputPPerKg, pt.inputZn,
				pt.inputCu, pt.inputMn, pt.inputCr, pt.inputSel, pt.inputIo,
				pt.requiredFe, pt.inputVitMix, pt.inputVitK, pt.inputVitC,
				pt.inputVolumePerKg, pt.inputLipidPerKg, pt.requiredRanitidine,
				pt.requiredInsulin, pt.otherFluid_ml, 12, 2, 1);

		// calculate the needed medications and actual fluids, kcal.
		RecommendOrderGen.calculate(pt);

		printVariables(pt);
		// test warning:
		// pt.inputKPerKg = 3.5;

		// test unacceptable:
		// patient1.inputKPerKg =75;

		// Alerts alerts = RecommendOrderGen.dosageAlerts(pt);
		// RecommendOrderGen.printMeds();
		// System.out.println("\n\n" + alerts.toString());

	}

	private static void printVariables(Patient patient1) {
		System.out
				.println("\n********************************************************************************\n");
		Field[] fields = patient1.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				// if(field.get(patient1).equals(0) || field.get(patient1).equals(-1.0))
				System.out
						.println(field.getName() + "\t" + field.get(patient1));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
