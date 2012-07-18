package com.platform.hometown.Client;

import java.util.ArrayList;
import java.util.HashMap;

import com.platform.api.Controller;
import com.platform.api.ControllerResponse;
import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.Parameters;
import com.platform.api.Result;

public class SelectBuildByKeyWordController implements Controller {
	String debug_category = "SelectBuildByKeyWordController";
	
	
	
	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		Logger.info("in theSelectBuildByKeyWordController" , "debug");
		
		String cId; //campaign id
		String keyWordId;
		String myAction;
		
		ControllerResponse resp = new ControllerResponse();
		
		if(params.containsKey("action")){
			myAction = (String)params.get("action");
			Logger.info("The action is "+myAction, debug_category); 
		} else {
			throw new Exception("Cannot retrieve action from the parameters");
		}
		
	      
		if(params.containsKey("myValue")){
			cId = (String)params.get("myValue");
			Logger.info("The cId is "+cId, debug_category); 
		   	
		} else {
			throw new Exception("Cannot retrieve campaign id from the parameters");
		}
		
		if(params.containsKey("keyWord")){
			keyWordId = (String)params.get("keyWord");
			Logger.info("The keyWordId is "+keyWordId, debug_category); 
		   	
		} else {
			throw new Exception("Cannot retrieve keyWord id from the parameters");
		}
		
		CampaignBO campaign = new CampaignBO(cId, false, true, keyWordId);
		
		
		if(myAction.equals("save")==true){
			
			//for each page set selected if it is selected on the page
			for(int i=0; i<campaign.getCurrentListings().size();i++){
				
				for(int j=0; j<campaign.getCurrentListings().get(i).getPagesAvailable().size();j++){
					
					//Bo defaults to selected true, so reset to false first, then check page and set
					campaign.getCurrentListings().get(i).getPagesAvailable().get(j).setSelected(false);
					
					if(((String)params.get(i+"page"+j)!=null) &&((String)params.get(i+"page"+j)).length()!=0){
						//Functions.debug("Inside the save if");
						campaign.getCurrentListings().get(i).getPagesAvailable().get(j).setSelected(true);	
												
						//save to db
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
						    resp.setTargetPage("success.jsp");
						    //Functions.throwError(msg + ".");                     // Ignore error
						}
						else
						{	
							String msg = "New build id is";
						    Functions.debug(msg + result.getId());  
						    
							resp.setTargetPage("success.jsp");
						}
						
					}
				
				}
			}
		}else {
			
			resp.setTargetPage("error.jsp");
		}
		
		
		params.put("campaign", campaign);
		params.put("cId", cId);
		
	    resp.setData(params);
		return resp;
		
	}
	
	
	
	
	
	}


