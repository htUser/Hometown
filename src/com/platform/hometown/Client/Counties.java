package com.platform.hometown.Client;

import java.util.ArrayList;

public class Counties {
	
	String stateId = new String();
	String countyId = new String();
	String countyName = new String();
	String countyLookup = new String();//don't think we use this, might take out
	String clientCountyId = new String();	//id of the junction object record 
	String clientText = new String();
	
	
	public String getClientCountyId() {
		return clientCountyId;
	}

	String stateAB = new String();
	Boolean selected = false;
	
	ArrayList <Places>placesAvailable = new ArrayList<Places>();
	
	public Counties(){
		super();
	}
	
	public Counties(String newState, String newCountyId, String newName, String newStateAB, String newClientCountyId, String newClientText){
		
		this.stateId = newState;
		this.countyId = newCountyId;
	
		this.countyName = newName;
		this.stateAB = newStateAB;
		this.clientCountyId = newClientCountyId;
		this.countyLookup = newName + " - " + newStateAB;
		this.clientText = newClientText;
		
	}
	
	public String getClientText() {
		return clientText;
	}

	public void setClientText(String clientText) {
		this.clientText = clientText;
	}

	public String getCountyLookup() {
		return countyLookup;
	}

	public void setCountyLookup(String countyLookup) {
		this.countyLookup = countyLookup;
	}

	public String getCountyName(){
		return countyName;
		
	}
	
	public String getCountyId(){
		
		return countyId;
	}
	

	public String getStateId(){
		return stateId;
	
	}
	
	public String getStateAB(){
		return stateAB;
	
	}
	
	public ArrayList <Places> getPlacesAvailable(){
		
		return placesAvailable;
	}
	

	
	public void setPlacesAvailable(ArrayList <Places> newPlacesAvailable){
		this.placesAvailable = newPlacesAvailable;
		
	}
	
	public void setSelected(Boolean isSelected){
		this.selected = isSelected;
	}
	
	public Boolean getSelected(){
		return selected;
	}
	
	//Resets the setSelected to false for all places
	public void resetPlacesAvailable(){
		
		for(int i=0; i< this.placesAvailable.size();i++){
			
			this.placesAvailable.get(i).setSelected(false);
		}
		
	}
	
	
	
	

}