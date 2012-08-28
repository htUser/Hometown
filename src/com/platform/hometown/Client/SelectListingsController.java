package com.platform.hometown.Client;

import java.util.HashMap;

import com.platform.api.Controller;
import com.platform.api.ControllerResponse;
import com.platform.api.Functions;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;
import com.platform.hometown.proximity.processProximity;

public class SelectListingsController implements Controller {

	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		
		Functions.debug("Inside SelectListingController");
		String myAction = (String)params.get("action");
		String cId; //campaign id
		
		
		if(params.containsKey("myValue")){
			
			cId = (String)params.get("myValue");
			//Functions.debug("cId is "+cId);
				
		} else {
			throw new Exception("Cannot retrieve campaign id from the parameters");
		}
		
		
		ControllerResponse resp = new ControllerResponse();
		CampaignBO campaign = new CampaignBO(cId, true, false, null);
		
		if(myAction.equals("create")==true){
			
			resp.setTargetPage("listings.jsp");
			
		} else if(myAction.equals("save")==true){
			Functions.debug("Saving Listings");
			//for each keyword set selected if it is selected on the page
			for(int i=0; i<campaign.getKeyWords().size();i++){
				
				for(int j=0; j<campaign.getKeyWords().get(i).getCountiesAvailable().size();j++){
					
					
					if(((String)params.get(i+"county"+j)!=null) &&((String)params.get(i+"county"+j)).length()!=0){
						
						//first check if listing is already in db
						Result checkResult = Functions.searchRecords("CSA_Tracking", "id","client2 equals '" + campaign.getClientLocation().getClient().getId() + "' AND county_lookup equals '" + campaign.getKeyWords().get(i).getCountiesAvailable().get(j).getCountyId() + "' AND tracking_number equals '" + campaign.getCampaignId() + "' AND campaign_keyword equals '" + campaign.getKeyWords().get(i).getCampaignKeyWordsJunctionId() + "' AND location_county equals '" +campaign.getKeyWords().get(i).getCountiesAvailable().get(j).getClientCountyId() + "'");
						if(checkResult.getCode()<0){
							throw new Exception("Error searching for current listing. ");

						}else if(checkResult.getCode()==0){
							Functions.debug("Adding listing to db");
							
							//only if no records found save to db
							Parameters newListingParams = Functions.getParametersInstance();
							newListingParams.add("service_t", campaign.getServiceType());
							newListingParams.add("location_county", campaign.getKeyWords().get(i).getCountiesAvailable().get(j).getClientCountyId());	
							newListingParams.add("campaign_keyword", campaign.getKeyWords().get(i).getCampaignKeyWordsJunctionId());				
							newListingParams.add("tracking_number", campaign.getCampaignId());
							newListingParams.add("county_lookup", campaign.getKeyWords().get(i).getCountiesAvailable().get(j).getCountyId());
							newListingParams.add("campaign_name", campaign.getCampaignName());
							newListingParams.add("client2", campaign.getClientLocation().getClient().getId());
							
							Result result = Functions.addRecord("CSA_Tracking", newListingParams);
							
							int resultCode = result.getCode();
							
							if(resultCode < 0)
							{
							    // Some error happened.
							    String msg = "Listing could not be added";
							    Functions.debug(msg + ":\n" + result.getMessage());  	// Log details 
							    Functions.throwError(msg);
							    resp.setTargetPage("error.jsp");
							    
							   
							}
							else
							{
								resp.setTargetPage("success.jsp");
							}
						} else {
							resp.setTargetPage("success.jsp");	
						}
						
					}
				}
			}
			
				
				
			} else if(myAction.equals("update")==true){
				
				resp.setTargetPage("listings.jsp");
					
					
				
			}  else {
				
				resp.setTargetPage("listings.jsp");
			}
		
		
		//always put the filled campaign object back in the request and then return
		params.put("campaign",campaign);
	    resp.setData(params);
		return resp;
		
			
	}
	
	
	

}