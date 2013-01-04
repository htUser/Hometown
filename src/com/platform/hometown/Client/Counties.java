package com.platform.hometown.Client;

import com.platform.api.Result;
import com.platform.api.Functions;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;

import java.util.ArrayList;


public class Counties {
	
	String stateId = new String();
	String countyId = new String();
	String countyName = new String();
	String countyLookup = new String();//don't think we use this, might take out
	String clientCountyId = new String();	//id of the junction object record, table should be location-county
	String clientText = new String();
	String stateAB = new String();
	
	Boolean selected = false;
	ArrayList <Places> placesAvailable = new ArrayList<Places>();  //filled via a method in ClientLocation
	ArrayList <PageBO> pagesAvailable = new ArrayList<PageBO>();
	
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
	
	
	public void fillPagesAvailable(String campaignKeyWordId){
		
		//select all the pages where the place is chosen for the location_county and the campaign_keyword is the same
		String latest_value;
		try{
			String sql = "SELECT pl.place as pName, p.place AS placeId, k.key_name, p.keyword_group AS keyWordGroupId, p.full_url, "+ 
					"cp.id, cp.places, cp.client_county, k.id, cp.distance, cp.proximity, p.id AS pageId "+
					"FROM    "+ 
					"Client_Places AS cp  "+
					"INNER JOIN Pages AS p ON p.place = cp.places "+
					"INNER JOIN Places AS pl ON pl.id = p.place "+
					"INNER JOIN Keyword_Groups AS k ON p.keyword_group = k.id "+
					"INNER JOIN Site_Keywords AS sk ON sk.keyword = k.id "+
					"INNER JOIN Campaign_Keywords as ck ON ck.site_keyword = sk.id "+
					"WHERE cp.client_county = '" + this.getClientCountyId() + 
					"' AND ck.id = '" +campaignKeyWordId + "' "+
					"ORDER BY pName ASC ";
		      
		    
       
			   Result result = Functions.execSQL(sql);
			   int resultCode = result.getCode();
			   if (resultCode < 0)
			   {
			      // Error occurred
			      String msg = "Sample: Error during SQL search";
			      Functions.debug("Sample:\n" + result.getMessage());
			   }
			   else if (resultCode > 0)
			   {
				   
				  // Functions.debug("got to the results set");
			      // A record was found. (Otherwise, resultCode == 0)                     
			      ParametersIterator it = result.getIterator();
			     
			      while(it.hasNext()){
			    	  Parameters params = it.next();        
			    	  latest_value = params.get("pName");
			    	  PageBO aPage = new PageBO();
			    	  aPage.setPlaceName((String)params.get("pName"));
			    	  aPage.setPageId((String)params.get("pageId"));
			    	  aPage.setPlaceId((String)params.get("placeId"));
			    	  aPage.setKeywordGroupId((String)params.get("keyWordGroupId"));
			    	  aPage.setKeyName((String)params.get("key_name"));	
			    	  aPage.setFullURL((String)params.get("full_url"));
			    	  aPage.setDistance((String)params.get("distance"));
			    	  aPage.setProximity((String)params.get("proximity"));
			  	    
					  this.getPagesAvailable().add(aPage);
			      }
			   }
			} catch (Exception e) {
			   String msg = "Sample: Exception during SQL search";
			   Functions.debug("Sample:\n" + e.getMessage());        
			}
		
		
	}



	public ArrayList<PageBO> getPagesAvailable() {
		return pagesAvailable;
	}

	public void setPagesAvailable(ArrayList<PageBO> pagesAvailable) {
		this.pagesAvailable = pagesAvailable;
	}

	public String getClientCountyId() {
		return clientCountyId;
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