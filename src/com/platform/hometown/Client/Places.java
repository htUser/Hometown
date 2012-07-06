package com.platform.hometown.Client;

import com.platform.api.Functions;



public class Places {
	
	Double lat;
	Double longitude;
	String placeId = new String();
	String placeName = new String();

	Boolean selected= false;
	String hType = new String();
	String locationPlaceId = new String(); 	//location_place record id, is filled if this exists in the database
	
	/**
	 * Returns true if the locationPlaceId exists, thus it is a record in the database.  False if not.
	 * @return
	 */
	public Boolean isInDB(){
		
		
		if((locationPlaceId!=null)&&(locationPlaceId.length()>0)){
			return true;
		} else {
			return false;
		}
	}
	
	public String getLocationPlaceId() {
		return locationPlaceId;
	}

	public void setLocationPlaceId(String locationPlaceId) {
		this.locationPlaceId = locationPlaceId;
	}

	/**
	Places constructor that does not include the hub/sub data
	
	*/
	public Places(String newPlaceId, String newPlaceName, double newLat, double newLong){
		//Functions.debug("In the Places Constructor");
		
		placeId = newPlaceId;
		placeName = newPlaceName;
	
		
		
		lat = newLat;
		longitude = newLong;
		
	}
	
	/**
	Places constructor that includes the hub/sub data
	
	*/
	public Places(String newPlaceId, String newPlaceName, String newHType, double newLat, double newLong){
		//Functions.debug("In the Places Constructor");
		
		placeId = newPlaceId;
		placeName = newPlaceName;
		hType = newHType;
		lat = newLat;
		longitude = newLong;
		
	}
	
	
	public String getPlaceId(){
		return placeId;
	}
	
	public String getPlaceName(){
		return placeName;
		
	}
		
	public double getLat(){
		return lat;
	}
	
	public double getLongitude(){
		return longitude;
	}
	public void setSelected(Boolean isSelected){
		selected = isSelected;
	}
	public Boolean getSelected(){
		return selected;
	}
	
}