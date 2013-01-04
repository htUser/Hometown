package com.platform.hometown.Client;

import java.util.ArrayList;
import java.util.HashMap;
import com.platform.api.*;
import com.platform.hometown.Client.*;


public class CampaignBO {
	String debug_category = "CampaignBO";
	
	ClientLocation clientLocation;
	
	String serviceType;
	String site;
	String trackingNumber;
	String campaignName;
	String campaignId;
	
	ArrayList<KeyWordsBO> keywords = new ArrayList<KeyWordsBO>();
	
	
	/**
	 * Campaign constructor. Completely constructs the BO from the db using the campaignId. 
	 * Declares and calls the ClientLocation constructor which retrieves all the counties available to this 
	 * location, but does not build all the places available.  Fills the keyWords available (and their realted lisitng info) 
	 * based on a boolean passed in.
	 * 
	 * @param cId campaign id
	 * @param buildListings True if you want to build the listings associated with this campaign.
	 */
	public CampaignBO(String cId, String keyWordId) throws Exception{
		campaignId = cId;
		Functions.debug("in the CampaignBO");
		
		
		//get the campaign info based on campaign id
		Result result = Functions.getRecord("Campaigns", "client2, t_number, location, service_type, site, campaign_name", campaignId);
		Functions.debug("after getRecord, code is "+result.getCode());
		
		if(result.getCode()>0){
			Parameters dbCampaign = result.getParameters();
			
			//create ClientLocation Object which populates the counties available during the constructor, do not construct the places available,
			//but does construct all the pages available for each county
			this.clientLocation = new ClientLocation((String)dbCampaign.get("location"), false, keyWordId);
			
			trackingNumber = (String)dbCampaign.get("t_number");
			serviceType = (String)dbCampaign.get("service_type");
			site = (String)dbCampaign.get("site"); 
			campaignName = (String)dbCampaign.get("campaign_name");
			
			
		}
		
	
		this.populateKeyWords(cId, keyWordId);
				
		
	}
	

	public KeyWordsBO getSelectedKeyWord(){
		KeyWordsBO selectedKeyWord =null;
		
		for(int i=0; i<keywords.size(); i++){
			if(keywords.get(i).getSelected()){
				//Functions.debug("set selected is "+keywords.get(i).getSelected());
				 selectedKeyWord = keywords.get(i);
			}
			
		}
		
		return selectedKeyWord;
	}

	/**
	 * Search the db for all the keywords available for this Campaign. Populates the listingBO too.
	 * 
	 * @param cId The campaign id.
	 * @throws Exception
	 */
	private void populateKeyWords(String cId, String selectedKeyWordId) throws Exception{
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
			    String campaignKeywordJunctId = (String)params.get("id");
			    KeyWordsBO k;
			    
			     
			    //lookup keyword name field from the Site_Keywords table
			    Result oneKeyWordResult = Functions.getRecord("Site_Keywords", "keyword, keyword_text, site, site_text, id",(String)params.get("site_keyword") );
				 
			    if(oneKeyWordResult.getCode()>0){
			    	
			    	Parameters oneKeyWord = oneKeyWordResult.getParameters();
					k = new KeyWordsBO((String)oneKeyWord.get("keyword"), (String)oneKeyWord.get("keyword_text"), campaignKeywordJunctId, (String)oneKeyWord.get("id"));
				
					//if there is a keyword id selected, then set selected
					if((selectedKeyWordId!=null)&&(selectedKeyWordId.length()>0)){
						if(campaignKeywordJunctId.matches(selectedKeyWordId)){
							k.setSelected(true);
						}
					}
					
					//populate the listing information from the db
				    k.setListing(populateListing(cId, campaignKeywordJunctId, (String)oneKeyWord.get("keyword_text")));
				   Functions.debug("after the set Listing in CampaignBO");
			    
			    } else {
					throw new Exception("Error:  Cannot find the keyword reference record from Site_Keywords.");
				}
					
				 
				this.getKeywords().add(k);
		    
			  }
			
		}
		
	}
	
	

	private ListingBO populateListing(String cId, String keyWordId, String keyWordName) throws Exception{
		ListingBO aListing = new ListingBO();
		  	 
		
		String sql = "SELECT tracking_number, campaign_keyword, id " +
				"FROM CSA_Tracking " +
				"WHERE tracking_number = '" + cId + "' " +
				"AND campaign_keyword = '" + keyWordId + "'";
		try{
			
			
			 Result result = Functions.execSQL(sql);
			   int resultCode = result.getCode();
			   if (resultCode < 0)
			   {
			      // Error occurred
			      String msg = "Sample: Error during SQL search";
			      Functions.debug("Sample:\n" + result.getMessage());
			   } else if (resultCode == 0){
				   // No Records Found
				   String msg = "No records found";
				   Logger.info(msg + " Campaign id is "+cId+":\n" + result.getMessage(), debug_category); 
				   Functions.throwError(msg + "."); 
			   }	
			   else if (resultCode > 0)
			   {
			      // A record was found. (Otherwise, resultCode == 0)                     
			      ParametersIterator it = result.getIterator();
			   
			     while(it.hasNext()){
			    	  Parameters params = it.next();        
			    	
			    	  aListing.setCampaignId(cId);
			    	  aListing.setCampaignKeyWordId((String)params.get("campaignKeyword"));
			    	  aListing.setListingId((String)params.get("id"));
			    	  Functions.debug("aListing.getListingId is "+aListing.getListingId());
			    	  
			    	  aListing.setKeyWordName(keyWordName); 
			      }
			    
			   }
			} catch (Exception e) {
			   String msg = "Sample: Exception during SQL search";
			   Functions.debug("Sample:\n" + e.getMessage());        
			}
		
		 return aListing;
		}
		
	
	public ClientLocation getClientLocation() {
		return clientLocation;
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



	public ArrayList<KeyWordsBO> getKeywords() {
		return keywords;
	}



	public void setKeywords(ArrayList<KeyWordsBO> keywords) {
		this.keywords = keywords;
	}

}