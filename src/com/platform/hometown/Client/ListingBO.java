package com.platform.hometown.Client;

import com.platform.api.Functions;
import java.util.ArrayList;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;

public class ListingBO {
	String campaignId = new String();
	String campaignKeyWordId = new String();
	String clientId = new String();
	String locationCountyId = new String(); 
	String listingId = new String(); //will only be set if a listing exists in the db
	
	String countyName = new String();
	String keyWordName = new String(); 
	
	ArrayList <PageBO> pagesAvailable = new ArrayList<PageBO>();
	
	
	public String getKeyWordName() {
		return keyWordName;
	}






	public void setKeyWordName(String keyWordName) {
		this.keyWordName = keyWordName;
	}






	public String getCountyName() {
		return countyName;
	}






	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	
	
	
	
	
	
	public void populatePagesAvailable(){
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
					"WHERE cp.client_county = '" + this.getLocationCountyId() + "' AND ck.id = '" +this.getCampaignKeyWordId() + "' "+
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


	public ListingBO() throws Exception{
		
			 super();  
				
		
	}
	
	
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}


	public void setCampaignKeyWordId(String campaignKeyWord) {
		this.campaignKeyWordId = campaignKeyWord;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public void setLocationCountyId(String locationCountyId) {
		this.locationCountyId = locationCountyId;
	}


	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public String getCampaignKeyWordId() {
		return campaignKeyWordId;
	}
	public String getClientId() {
		return clientId;
	}
	public String getLocationCountyId() {
		return locationCountyId;
	}

	
	

}