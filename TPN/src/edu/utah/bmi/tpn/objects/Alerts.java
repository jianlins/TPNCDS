package edu.utah.bmi.tpn.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class is use to store the alerts information,
 * an instance of Alerts can be generated through "RecommendOrderGen.doseAlert(patient)"
 * 
 * @author Jianlin Shi
 * 
 */
public class Alerts {
	// List of the variable names of the ingredients that above warning level
	public ArrayList<String> warning = new ArrayList<String>();
	// List of the variable names of the ingredients that above unacceptable level
	public ArrayList<String> unacceptable = new ArrayList<String>();

	public void clear() {
		warning.clear();
		unacceptable.clear();
	}

	public void addWarning(String variableName) {
		warning.add(variableName);
	}
	public void addWarning(List<String> variableNames) {
		warning.addAll(variableNames);
	}

	public void addUnacceptable(String variableName) {
		unacceptable.add(variableName);
		// if the range is already unacceptable, it's not worthy to keep it in the warning list
		if (warning.contains(variableName)) {
			warning.remove(variableName);
		}
	}
	
	public void addUnacceptable(List<String> variableNames) {
		unacceptable.addAll(variableNames);
		// if the range is already unacceptable, it's not worthy to keep it in the warning list
		warning.removeAll(variableNames);
	}
	
	public String toString(){
		String wString="";
		String uString="";
		for(String w: warning){
			wString+=","+w;
		}
		if(wString.length()>0)
		wString="warning variables: "+wString.substring(1)+"\n";
		for(String u: unacceptable){
			uString+=","+u;
		}
		if(uString.length()>0)
		uString="unacceptable variables: "+uString.substring(1)+"\n";		
		return wString+uString;				
	}
	public void printAlerts(){
		System.out.println(this.toString());
	}
}
