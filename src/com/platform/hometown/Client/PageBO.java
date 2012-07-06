package com.platform.hometown.Client;

public class PageBO {
	
	String countyLookupId = new String();
	String keywordGroupId = new String();
	String placeId = new String();
	String placeName = new String();
	String fullURL = new String();
	String keyName = new String();
	String distance = new String();
	String proximity = new String();
	String pageId = new String();
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getProximity() {
		return proximity;
	}
	public void setProximity(String proximity) {
		this.proximity = proximity;
	}
	Boolean selected = false;
	
	
	
	public String getCountyLookupId() {
		return countyLookupId;
	}
	public void setCountyLookupId(String countyLookupId) {
		this.countyLookupId = countyLookupId;
	}
	public String getKeywordGroupId() {
		return keywordGroupId;
	}
	public void setKeywordGroupId(String keywordGroupId) {
		this.keywordGroupId = keywordGroupId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getFullURL() {
		return fullURL;
	}
	public void setFullURL(String fullURL) {
		this.fullURL = fullURL;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String place_name) {
		this.placeName = place_name;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean isSelected) {
		this.selected = isSelected;
	}
	
	

}