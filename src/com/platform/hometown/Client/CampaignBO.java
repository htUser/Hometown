package com.platform.hometown.Client;

import java.util.ArrayList;
import java.util.HashMap;
import com.platform.api.*;
import com.platform.hometown.Client.*;


public class CampaignBO {
	
	
	ClientLocation clientLocation;
		
	
	String trackingNumber;
	String serviceType;
	String site;
	String campaignName;
	String campaignId;
	String campaignIdInTable;
	
	ArrayList<KeyWordsBO> keyWords = new ArrayList<KeyWordsBO>();		
	ArrayList<ListingBO> currentListings = new ArrayList<ListingBO>();	// ArrayList of all the ListingBO objects currently in the db
	
	/**
	 * Campaign constructor. Completely constructs the BO from the db using the campaignId. 
	 * Declares and calls the ClientLocation constructor which retrieves all the counties available to this 
	 * location, but does not build all the places available.  Also completely fills the keyWords available.  Will 
	 * fill the listings based on a boolean passed in. 
	 * 
	 * @param cId campaign id
	 * @param buildListings True if you want to build the listings associated with this campaign.
	 */
	public CampaignBO(String cId, Boolean buildKeyWords, Boolean buildListings) throws Exception{
		campaignId = cId;
		
        
		//get the campaign info based on campaign id
		Result result = Functions.getRecord("Campaigns", "client2, t_number, location, service_type, site, campaign_name, campaign_id", campaignId);
		//Functions.debug("after getRecord, code is "+result.getCode());
		
		if(result.getCode()>0){
			Parameters dbCampaign = result.getParameters();
			
			//create ClientLocation Object which populates the counties available during the constructor, do not construct the places available
			this.clientLocation = new ClientLocation((String)dbCampaign.get("location"), false);
			
			trackingNumber = (String)dbCampaign.get("t_number");
			serviceType = (String)dbCampaign.get("service_type");
			site = (String)dbCampaign.get("site"); 
			campaignName = (String)dbCampaign.get("campaign_name");
			campaignIdInTable = (String)dbCampaign.get("campaign_id");
			
		}
		
		//fill the key words available
		if(buildKeyWords==true){
			this.populateKeyWords(cId);
		}
		
		//populate the listings if true
		if(buildListings==true){
			this.populateListingWithJoin(cId);
		}
		
	}
	


	/**
	 * Search the db for all the keywords available for this Campaign.
	 * 
	 * @param cId The campaign id.
	 * @throws Exception
	 */
	private void populateKeyWords(String cId) throws Exception{
		//get the keywords available based on campaign id from the Campaign_Keywords table
		
		Result allKeyWordsResult = Functions.searchRecords("Campaign_Keywords", "campaign, site_keyword, id","campaign equals '" + cId + "'", "site_keyword", "asc","cId", "asc", 0, 200);
		if(allKeyWordsResult.getCode()<0){
			throw new Exception("Error finding campaign keywords");

		}else if(allKeyWordsResult.getCode()==0){
			//no records found
			throw new Exception("There are no keywords associated with this campaign.");
			
		}else {
			//Success
			
			  ParametersIterator iterator = allKeyWordsResult.getIterator();
			  
			  //for each keyword,
			  while(iterator.hasNext())
			  {
			    Parameters params = iterator.next();
			    KeyWordsBO k;
			    
			    //Functions.debug("site_keyword id using to look up is "+(String)params.get("site_keyword") );
			    
			    //lookup keyword name field from the Site_Keywords table
			    Result oneKeyWordResult = Functions.getRecord("Site_Keywords", "keyword, keyword_text, site, site_text, id",(String)params.get("site_keyword") );
				//Result r = Functions.searchRecords("Site_Keywords", "keyword, keyword_text, site, site_text", "keyword equals '"+(String)params.get("site_keyword") +"'");
			    
			    if(oneKeyWordResult.getCode()>0){
			    	
			    	Parameters oneKeyWord = oneKeyWordResult.getParameters();
						
					//Functions.debug("got the keyword "+(String)oneKeyWord.get("keyword")+", "+(String)oneKeyWord.get("keyword_text")+", "+(String)oneKeyWord.get("site")+", "+(String)oneKeyWord.get("site_text"));
			    	
					k = new KeyWordsBO((String)oneKeyWord.get("keyword"), (String)oneKeyWord.get("keyword_text"), (String)params.get("id"), (String)oneKeyWord.get("id"));
				} else {
					throw new Exception("Error:  Cannot find the keyword reference record from Site_Keywords.");
				}
					
				//get the countiesAvailable from the ClientLocation, loop thru and create a new county array and add that to each KeyWord
				ArrayList<Counties> a = new ArrayList<Counties>();
				
				
				
				for(int i =0; i<this.clientLocation.getCounties().size();i++){
					Counties c = this.clientLocation.getCounties().get(i);
					a.add(c);
					//Functions.debug("adding a county to the new arraylist"+c.getCountyName());
				}
				
			    k.setCountiesAvailable(a);
				this.getKeyWords().add(k);
		    
			  }
			
		}
		
	}
	
	
	private void populateListingWithJoin(String cId){
		//try the sql join to get all the data needed
		String latest_value;
		try{
			String sql = "SELECT kg.key_name AS kName, kg.id, sk.keyword, sk.id, ck.site_keyword, ck.id, c.county AS name, c.id, cc.id, cc.county, " +
					"listing.tracking_number, listing.client2, listing.county_lookup, listing.campaign_keyword AS campaignKeyword, listing.id AS l, listing.location_county "+ 
					"FROM CSA_Tracking AS listing  "+
					"INNER JOIN Client_Counties AS cc ON cc.id = listing.location_county "+
					"INNER JOIN Counties AS c ON c.id = cc.county "+
					"INNER JOIN Campaign_Keywords AS ck ON ck.id = listing.campaign_keyword "+
					"INNER JOIN Site_Keywords AS sk ON sk.id = ck.site_keyword "+
					"INNER JOIN Keyword_Groups AS kg ON kg.id = sk.keyword "+
					"WHERE listing.tracking_number= '" + cId + "' ORDER BY name ASC";
		      
		    
       
			
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
			      // A record was found. (Otherwise, resultCode == 0)                     
			      ParametersIterator it = result.getIterator();
			   
			      int count = 0;
			      while(it.hasNext()){
			    	  Parameters params = it.next();        
			    	 latest_value = params.get("campaignKeyword");
			    	  //Functions.debug("Sample: campaignKeyword = " + latest_value);
			    	  
			    	  
			    	  ListingBO aListing = new ListingBO();
				  aListing.setCampaignId(cId);
				  aListing.setCampaignKeyWordId((String)params.get("campaignKeyword"));
				  aListing.setLocationCountyId((String)params.get("location_county"));
				  aListing.setCountyName((String)params.get("name"));
				  aListing.setListingId((String)params.get("l"));	//id from the CSA_Tracking record
				  aListing.setKeyWordName((String)params.get("kName")); 
				  aListing.setClientId((String)params.get("client2"));
				   
					
					  //get the pages available  for this Listing based on the location_county id
					  aListing.populatePagesAvailable();
					    
					  this.getCurrentListings().add(aListing);
					  
			    	  
			      }
			    
			   }
			} catch (Exception e) {
			   String msg = "Sample: Exception during SQL search";
			   Functions.debug("Sample:\n" + e.getMessage());        
			}
		
		
		
	}
	
	
	
	
	public String getCampaignIdInTable() {
		return campaignIdInTable;
	}

	public void setCampaignIdInTable(String campaignIdInTable) {
		this.campaignIdInTable = campaignIdInTable;
	}
	
	public ArrayList<ListingBO> getCurrentListings() {
		return currentListings;
	}

	public void setCurrentListings(ArrayList<ListingBO> currentListings) {
		this.currentListings = currentListings;
	}	
	
	public ClientLocation getClientLocation() {
		return clientLocation;
	}

	public ArrayList<KeyWordsBO> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(ArrayList<KeyWordsBO> keyWords) {
		this.keyWords = keyWords;
	}
	
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public String getServiceType() {
		return serviceType;
	}
	public String getSite() {
		return site;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public String getCampaignId() {
		return campaignId;
	}

}