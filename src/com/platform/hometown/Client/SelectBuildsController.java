package com.platform.hometown.Client;

import java.util.HashMap;


import com.platform.api.Controller;
import com.platform.api.ControllerResponse;
import com.platform.api.Functions;
import com.platform.api.Parameters;
import com.platform.api.Result;
import com.platform.hometown.proximity.processProximity;

import com.platform.api.Controller;
import com.platform.api.ControllerResponse;

public class SelectBuildsController implements Controller {

	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		
		//Functions.debug("In the SelectBuildsController execute method");
		String myAction = (String)params.get("action");
		//Functions.debug("The action is "+myAction);
		
		String cId; //campaign id
		if(params.containsKey("myValue")){
			cId = (String)params.get("myValue");
			//Functions.debug("cId is "+cId);
				
		} else {
			throw new Exception("Cannot retrieve campaign id from the parameters");
		}
		
		ControllerResponse resp = new ControllerResponse();
		
		Functions.debug("Reached before CampaignBo");
		//Create the entire CampaignBO - fills the ClientLocation info, the Counties available info, not the places available, all the 
		//current listings and all the pages available.
		CampaignBO campaign = new CampaignBO(cId, false, true);
		
		Functions.debug("Reached before CampaignBo");
		if(myAction.equals("create")==true){
			
			//return to the page to display the choices
			resp.setTargetPage("builds.jsp");
			
			
		} else if(myAction.equals("save")==true){
			//for each page set selected if it is selected on the page
			
			
			for(int i=0; i<campaign.getCurrentListings().size();i++){
				
				for(int j=0; j<campaign.getCurrentListings().get(i).getPagesAvailable().size();j++){
					
					//Bo defaults to selected true, so reset to false first, then check page and set
					campaign.getCurrentListings().get(i).getPagesAvailable().get(j).setSelected(false);
					
					//Functions.debug("page value is "+(String)params.get(i+"page"+j));
					
					
					
					if(((String)params.get(i+"page"+j)!=null) &&((String)params.get(i+"page"+j)).length()!=0){
						//Functions.debug("Inside the save if");
						campaign.getCurrentListings().get(i).getPagesAvailable().get(j).setSelected(true);	
						
						
						//Functions.debug("campaign to save campaignId "+campaign.getCampaignId());
						Functions.debug("listing id is "+campaign.getCurrentListings().get(i).getListingId());
							
						//save to db
						Parameters newBuildParams = Functions.getParametersInstance();
						newBuildParams.add("page", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getPageId());
						newBuildParams.add("campaign", campaign.getCampaignId());	
						newBuildParams.add("csa_tracking", campaign.getCurrentListings().get(i).getListingId());	
						
						//Functions.debug("distance"+campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getDistance());
						//Functions.debug("proximity"+campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getProximity());
						
						newBuildParams.add("distance", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getDistance());
						newBuildParams.add("prox_rating", campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getProximity());
						
						
						
						Result result = Functions.addRecord("Build", newBuildParams);
						int resultCode = result.getCode();
						if(resultCode < 0)
						{
						    // Some error happened.
						    String msg = "Build could not be added";
						    Functions.debug(msg + ":\n" + result.getMessage());  // Log details
						    resp.setTargetPage("success.jsp");
						    //Functions.throwError(msg + ".");                     // Ignore error
						}
						else
						{
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
		return resp;
		
	}

}