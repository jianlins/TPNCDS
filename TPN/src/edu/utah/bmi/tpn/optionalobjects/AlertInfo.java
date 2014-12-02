package edu.utah.bmi.tpn.optionalobjects;

public class AlertInfo implements Cloneable{
	public String variableName;
	public String alertMessage;
	
	public AlertInfo(String variableName, String alertMessage){
		this.variableName=variableName;
		this.alertMessage=alertMessage;		
	}
	
	@Override
	public AlertInfo clone() {
		try {
			return (AlertInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
