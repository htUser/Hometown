package com.platform.hometown.Client;

import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.PLATFORM;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;

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
	Boolean selected = false;
	
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
	
	public static void updateLinkbacks(Parameters p) throws Exception{
		
		String topPlaces = new String();
		String debug_category = "UpdateLinkbacks";
		
		//get info off of current page record
		String page = p.get("id");
		String territoryPlace = p.get("territory_place");
		String keywordGroup = p.get("keyword_group");
		String metroPlace = p.get("metro_place");
		String site = p.get("site");
		String h_type = p.get("h_type");
		
		Logger.info("page " + page, debug_category); 
		Logger.info("territory place " + territoryPlace, debug_category); 
		Logger.info("keywordGroup " + keywordGroup, debug_category); 
		Logger.info("metroPlace " + metroPlace, debug_category); 
		Logger.info("site " +site, debug_category);
		Logger.info("h_type "+h_type, debug_category);
		
		Parameters params = new Parameters();
		
		
		//create no linkbacks if h_type is state
		if(!h_type.equalsIgnoreCase("state")){
		
			
			if(((keywordGroup==null)||(keywordGroup.length()==0)) || ((site==null)||(site.length()==0))){
				String msg = "Error getting the keywordGroup/site from the parameters for page id : "+page;
				Logger.info(msg, debug_category); 
				Functions.throwError(msg + "."); 	
				
			}
			
			
			if((territoryPlace!=null)&&(territoryPlace.length()!=0)){
				
				String sqlT = "SELECT file_name, full_url FROM Pages WHERE place = '" + territoryPlace + "' AND keyword_group = '" + keywordGroup +"' AND site = '" + site +"'";
				
				//get the page record for the territory place that this page is related to
				Result result = Functions.execSQL(sqlT);
						
				if(result.getCode()<0){
					 //error with execute sql
					 String msg = "Error finding Page Record. Page id is "+page;
					 Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
					 Functions.throwError(msg + "."); 
	
				}else if(result.getCode()==0){
					//no records found
					
					String msg = "Error: No Page records Found for Territory Place. Page id is "+page;
					Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
					Functions.throwError(msg + "."); 
					
				}else {
					//Success
					Logger.info("Found Territory Place Page record.", debug_category);
					 ParametersIterator it = result.getIterator();
				     Parameters territoryParams = it.next();        
				    
				     
				     Logger.info("file_name is "+territoryParams.get("file_name"), debug_category);
				     Logger.info("full_url is "+territoryParams.get("full_url"), debug_category);
					    
					
					//add territory linkback info
					params.add("linkback1_file", territoryParams.get("file_name"));
					params.add("linkback1_url", territoryParams.get("full_url"));
					Logger.info("After added linkback1 to params", debug_category);
				}
				
				
			} else {
				String msg = "Error getting the territoryPlace id from the parameters for page id : "+page;
				Logger.info(msg, debug_category); 
				//Functions.throwError(msg + "."); 	
				
			}
			
			//if h_type is a hub, then no metro linkback
			if(!h_type.contains("h")){
				if((metroPlace!=null)&&(metroPlace.length()!=0)){
					
					String sqlM = "SELECT file_name, full_url FROM Pages WHERE place = '" + metroPlace + "' AND keyword_group = '" + keywordGroup +"' AND site = '" + site +"'";
					
					//get the page record for the territory place that this page is related to
					Result result = Functions.execSQL(sqlM);
							
					if(result.getCode()<0){
						 //error with execute sql
						 String msg = "Error finding Page Record. Page id is "+page;
						 Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
						 Functions.throwError(msg + "."); 
		
					}else if(result.getCode()==0){
						//no records found
						
						String msg = "Error: No Page records Found for Metro Place. Page id is "+page;
						Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
						Functions.throwError(msg + "."); 
						
					}else {
						//Success
						
						 ParametersIterator it = result.getIterator();
					     Parameters metroParams = it.next();        
					     Logger.info("file_name is "+metroParams.get("file_name"), debug_category);
					     Logger.info("full_url is "+metroParams.get("full_url"), debug_category);
						 
					     
					     
						
						//add territory linkback info
						params.add("linkback2_file", metroParams.get("file_name"));
						params.add("linkback2_url", metroParams.get("full_url"));
					}
					
					
				} else {
					//Metro Place does not exist 
					String msg = "No metroPlace id from the parameters for page id : "+page;
					Logger.info(msg, debug_category);
					Functions.throwError(msg + "."); 
						
					
				}
			}
			
			//update the record
			params.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
			params.add(PLATFORM.PARAMS.RECORD.ENABLE_MULTIPART,"1");
							
			Result updateResult = Functions.updateRecord("Pages", page, params);  
			if(updateResult.getCode()<0){
				String msg = "There was an error updating the page record with Linkbacks. Page Id is "+ page;
				Logger.info(msg + ":\n" + updateResult.getMessage(), debug_category); 
				Functions.throwError(msg + "."); 				
			}
		
		} else {
			String msg = "Page is a state page: no linkbacks updated. Page Id is "+ page;
			Logger.info(msg, debug_category); 
		}
		
	}

}