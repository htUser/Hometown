package com.platform.hometown.Client;

import java.util.HashMap;


import com.platform.api.Controller;
import com.platform.api.ControllerResponse;
import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.Parameters;
import com.platform.api.Result;
import com.platform.hometown.proximity.processProximity;

import com.platform.api.Controller;
import com.platform.api.ControllerResponse;

public class SelectBuildsController implements Controller {

	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		String debug_category = "SelectBuildsController";
		Logger.info("In SelectBuildsController", debug_category); 
		
		String myAction = (String)params.get("action");
		String cId; //campaign id
		ControllerResponse resp = new ControllerResponse();
		
		if(params.containsKey("myValue")){
			cId = (String)params.get("myValue");
			
			//Create the entire CampaignBO - fills the ClientLocation info, the Counties available info, not the places available, all the 
			//current listings and all the pages available.
			CampaignBO campaign = new CampaignBO(cId, false, true, null);
			
			if(myAction.equals("create")==true){
				
				//return to the page to display the choices
				resp.setTargetPage("builds.jsp");
				
				
			} else if(myAction.equals("save")==true){
				//for each page set selected if it is selected on the page
				for(int i=0; i<campaign.getCurrentListings().size();i++){
					
					for(int j=0; j<campaign.getCurrentListings().get(i).getPagesAvailable().size();j++){
						
						
						if(((String)params.get(i+"page"+j)!=null) &&((String)params.get(i+"page"+j)).length()!=0){
							Logger.info("listing id is "+campaign.getCurrentListings().get(i).getListingId(), debug_category); 
							
							//first check if build is already in db
							Result checkResult = Functions.searchRecords("Build", "id","page equals '" + campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getPageId() + "' AND campaign equals '" + campaign.getCampaignId() + "' AND csa_tracking equals '" + campaign.getCurrentListings().get(i).getListingId() + "'");
							if(checkResult.getCode()<0){
								String msg = "Error searching for current build.";
								Logger.info(msg + ":\n" + checkResult.getMessage(), debug_category); 
								Functions.throwError(msg);
								resp.setTargetPage("error.jsp");
		
							}else if(checkResult.getCode()==0){
								Functions.debug("adding the build");
								//only if no records found save to db
								Parameters newBuildParams = Functions.getParametersInstance();
								newBuildParams.add("page", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getPageId());
								newBuildParams.add("campaign", campaign.getCampaignId());	
								newBuildParams.add("csa_tracking", campaign.getCurrentListings().get(i).getListingId());	
								newBuildParams.add("distance", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getDistance());
								newBuildParams.add("prox_rating", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getProximity());
								
								
								
								Result result = Functions.addRecord("Build", newBuildParams);
								int resultCode = result.getCode();
								if(resultCode < 0)
								{
								    // Some error happened.
								    String msg = "Build could not be added";
								    Functions.debug(msg + ":\n" + result.getMessage());  // Log details
								    resp.setTargetPage("error.jsp");
								    Functions.throwError(msg + ".");                     
								}
								else
								{	
									String msg = "New build id is";
									Logger.info(msg + result.getId(), debug_category);
								   	resp.setTargetPage("success.jsp");
								}
							} else {
								resp.setTargetPage("success.jsp");	
							}	
						}
					
					}
				}
				
			
			} else if(myAction.equals("update")==true){
				resp.setTargetPage("builds.jsp");
				
			}
			
			
			
			//always put the filled campaign object back in the request and then return
			params.put("campaign",campaign);
		    resp.setData(params);
		} else {
			resp.setTargetPage("error.jsp");
			Functions.throwError("Cannot retrieve campaign id from the parameters");
			
		}
		
		return resp;
		
	}

}