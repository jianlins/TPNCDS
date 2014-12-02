package edu.utah.bmi.tpn.optionalobjects;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * 
 * This class is use to store the alerts information, including alert messages
 * an instance of Alerts can be generated through "RecommendOrderGen.doseAlert(patient)"
 * 
 * @author Jianlin Shi
 * 
 */
public class Alerts2 {
	// List of the variable names of the ingredients that above warning level
	public TreeMap<String, AlertInfo> warning = new TreeMap<String, AlertInfo>();
	// List of the variable names of the ingredients that above unacceptable level
	public TreeMap<String, AlertInfo> unacceptable = new TreeMap<String, AlertInfo>();

	public void clear() {
		warning.clear();
		unacceptable.clear();
	}

	public void addWarning(String variableName, String alertMessage) {
		warning.put(variableName, new AlertInfo(variableName, alertMessage));
	}

	public void addUnacceptable(String variableName, String alertMessage) {
		unacceptable.put(variableName,
				new AlertInfo(variableName, alertMessage));
		// if the range is already unacceptable, it's not worthy to keep it in the warning list
		if (warning.containsKey(variableName)) {
			warning.remove(variableName);
		}
	}

	// Given a variable name, return the alert message if there is any
	public String getMessage(String variableName) {
		String msg = "";
		if (warning.containsKey(variableName)) {
			msg = warning.get(variableName).alertMessage;
		}
		if (unacceptable.containsKey(variableName)) {
			msg = unacceptable.get(variableName).alertMessage;
		}
		return msg;
	}
}
