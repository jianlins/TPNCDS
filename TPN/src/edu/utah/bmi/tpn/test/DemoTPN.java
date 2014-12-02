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
public class DemoTPN {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		Patient patient1 = new Patient(35, 1, 60, 175);
		// set other fluids
		patient1.otherFluid_ml = 1000;

		TPNCalculator.calWithoutLab(patient1);

		// calculateCrCl has two methods options: "CG" or "MDRD". If the patient is child, the option will be ignored and directly use Schwartz equation
		// For the interface, you can show these two options for adults, and one option: "Schwartz" for child.

		System.out.println("Patient's CrCl = "
				+ TPNCalculator.calculateCrCl(patient1, 1.1, "CG"));

		// use default input for following TPN calculation
		TPNCalculator.useRecommendedInput(patient1);

		printVariables(patient1);

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

		// TPNCalculator.updateInput();

		// calculate the needed medications and actual fluids, kcal.
		RecommendOrderGen.calculate(patient1);

		printVariables(patient1);
		// test warning:
		patient1.inputKPerKg = 3.5;

		// test unacceptable:
		// patient1.inputKPerKg =75;

		Alerts alerts = RecommendOrderGen.dosageAlerts(patient1);
		// RecommendOrderGen.printMeds();
		System.out.println("\n\n" + alerts.toString());

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
