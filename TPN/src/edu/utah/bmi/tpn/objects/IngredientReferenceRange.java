package edu.utah.bmi.tpn.objects;

/**
 * A class that store the information of one ingredient reference range (one row in the KB csv table).
 * It will be initiated when RecommendOrderGen.dosageAlerts() is called for the 1st time, through reading
 * from resources/TPNRefereces.csv file;
 * 
 * @author Jianlin Shi
 * 
 */
public class IngredientReferenceRange implements Cloneable {
	public String description;
	// which variable of the patient should be check for this reference range
	public String checkingVariableName;
	// which variable of the patient should be alerted to make adjustment, these two is not the same sometimes
	public String alertingVariableName;

	// age is standardized to years, if in months divide the number by 12, if days divide the number by 365
	// if not specified, use -1
	public double ageLowerBound, ageHigherBound;
	// whether the 1 for male, 0 for female, -1 for not specified
	public int gender;
	// standards code, e.g. SNOMEDCT or LOINC
	public String code;
	// the lower bound of warning level
	public double warning;
	// the lower bound of unacceptable level
	public double unacceptable;
	// whether the reference range is for central line:1, peripheral line:0, not specified:-1.
	public int ivType;

	public IngredientReferenceRange() {
		System.out.println("all variables are not initiated yet");
	}

	public IngredientReferenceRange(String[] vars) {
		intitiate(vars[0], vars[1], vars[2], vars[3], vars[4], vars[5],
				vars[6], vars[7], vars[8], vars[9]);

	}

	public IngredientReferenceRange(String description,
			String checkingVariableName, String alertingVariableName,
			String ageLowerBound, String ageHigherBound, String gender,
			String code, String warning, String unacceptable, String ivType) {
		intitiate(description, checkingVariableName, alertingVariableName,
				ageLowerBound, ageHigherBound, gender, code, warning,
				unacceptable, ivType);

	}

	public void intitiate(String description, String checkingVariableName,
			String alertingVariableName, String ageLowerBound,
			String ageHigherBound, String gender, String code, String warning,
			String unacceptable, String centralLine) {
		this.description = description;
		this.checkingVariableName = checkingVariableName;
		
		if (!ageLowerBound.equals("")) {
			this.ageLowerBound = Double.parseDouble(ageLowerBound);
		} else {
			this.ageLowerBound = -1;
		}
		if (!ageHigherBound.equals("")) {
			this.ageHigherBound = Double.parseDouble(ageHigherBound);
		} else {
			this.ageHigherBound = -1;
		}
		if (!gender.equals("")) {
			this.gender = Integer.parseInt(gender);
		} else {
			this.gender = -1;
		}
		this.code = code;
		if (!warning.trim().equals("")) {
			this.warning = Double.parseDouble(warning);
		} else {
			this.warning = -1;
		}
		if (!unacceptable.trim().equals("")) {
			this.unacceptable = Double.parseDouble(unacceptable);
		} else {
			this.unacceptable = -1;
		}
		if (!centralLine.equals("")) {
			this.ivType = Integer.parseInt(centralLine);
		} else {
			this.ivType = -1;
		}
	}

	@Override
	public IngredientReferenceRange clone() {
		try {
			return (IngredientReferenceRange) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
