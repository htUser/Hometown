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
		Logger.info("in theSelectBuildByKeyWordController" , debug_category);
		
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
					
					if(((String)params.get(i+"page"+j)!=null) &&((String)params.get(i+"page"+j)).length()!=0){
							
						//first check if build is already in db
						//Result checkResult = Functions.searchRecords("Build", "id","page equals '" + campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getPageId() + "' AND campaign equals '" + campaign.getCampaignId() + "' AND csa_tracking equals '" + campaign.getCurrentListings().get(i).getListingId() + "'");
						Result checkResult = Functions.searchRecords("Build", "id","page equals '" + campaign.getCurrentListings().get(i).getPagesAvailable().get(j).getPageId() + "' AND csa_tracking equals '" + campaign.getCurrentListings().get(i).getListingId() + "'");
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
							//newBuildParams.add("campaign", campaign.getCampaignId());	
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
							    Functions.debug(msg + result.getId());  
							    resp.setTargetPage("success.jsp");
							}
						} else {
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