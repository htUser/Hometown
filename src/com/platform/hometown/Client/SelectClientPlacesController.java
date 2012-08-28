package com.platform.hometown.Client;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;

import com.platform.api.*;

import com.platform.hometown.proximity.*;

public class SelectClientPlacesController implements Controller {
	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		String debug_category = "SelectClientPlacesController";
		Logger.info("In selectClientPlacesController()", debug_category); 
		
		
		String myAction = (String)params.get("action");
		String locId;
		ControllerResponse resp = new ControllerResponse();
		
		if(params.containsKey("myValue")){
			locId = (String)params.get("myValue");
			Logger.info("locId is "+locId, debug_category); 
			
			//Build BO
			ClientLocation cl = new ClientLocation(locId,true);
			
			
			if(myAction.equals("getCounties")==true){		
				
				resp.setTargetPage("selectClientPlaces.jsp");
			
			} else if(myAction.equals("filterByMiles")==true){
				String miles = (String)params.get("miles");
				
				if((miles!=null)&&(((String)params.get("miles")).length()!=0)){
							
					try{
											
						cl.filterByMiles(miles);
					} catch (Exception e){
						//miles value is not parsable - redisplay the page with error
						params.put("miles", "0");
						params.put("errorMsg", "Please use a numeric value.");
						cl = this.setSelectedFromPage(params, cl);
					}
				} else {
					//no value in the miles, so just set the previously selected and return to page
					cl = this.setSelectedFromPage(params, cl);
					
				}
				
				resp.setTargetPage("selectClientPlaces.jsp");
				
				
			} else if(myAction.equals("filterByLL")==true){
				//find the county we are filtering on - all rest of data stays the same
				//get LL filtering data off the page and then filer the places for that county
				
				cl = this.setSelectedFromPage(params, cl);
				
				
				if(((String)params.get("llCounty")!=null)&&(((String)params.get("llCounty")).length()!=0)){	
						
					String llCount = (String)params.get("llCounty");
				
					//get Long/Lat from page, they both could be null
					Boolean latDataHere = false;
					Boolean longDataHere = false;
					double latPage = 0;
					double longPage = 0;
					String latOperator = new String();
					String longOperator = new String();
					Boolean errorOnLatLong = false;
				
				
					try{
						if(((String)params.get("filterLat"+llCount)).length()!=0){
			
							latDataHere = true;
							latPage = Double.parseDouble((String)params.get("filterLat"+llCount));
							latOperator = (String)params.get("latOperator"+llCount);
							
						}
					} catch (Exception e){
						//lat/long value is not parsable - redisplay the page with error
						params.put("filterLat"+llCount, "0");
						params.put("LLerrorMsg", "Please use a numeric value.");
						params.put("LLerrorCounty", llCount);
						cl = this.setSelectedFromPage(params, cl);
						errorOnLatLong = true;
						
					}
				
								
					try{
						if(((String)params.get("filterLong"+llCount)).length()!=0){
			
							longDataHere = true;
							longPage = Double.parseDouble((String)params.get("filterLong"+llCount));
							longOperator = (String)params.get("longOperator"+llCount);
							
						}
					} catch (Exception e){
						//lat/long value is not parsable - redisplay the page with error
						params.put("filterLong"+llCount, "0");
						params.put("LLerrorMsg", "Please use a numeric value.");
						params.put("LLerrorCounty", llCount);
						cl = this.setSelectedFromPage(params, cl);
						errorOnLatLong = true;
						
					}
				
					if(errorOnLatLong!=true){
						//reset all the places to unselected first, then filter by LL to set selected
						cl.getCounties().get(Integer.parseInt(llCount)).resetPlacesAvailable();			
						cl.filterByLL(Integer.parseInt(llCount), latDataHere, latPage, longDataHere, longPage, latOperator, longOperator);
					}
				}
					
					
					
							
				resp.setTargetPage("selectClientPlaces.jsp");
				
			
			} else if(myAction.equals("save")==true){
				Logger.info("In the save", debug_category); 
				
				
				//for each county, for each place, add a record if it is selected
				for(int i=0; i<cl.getCounties().size();i++){
						
					for(int j=0; j<cl.getCounties().get(i).getPlacesAvailable().size();j++){
						
						if(((String)params.get(i+"place"+j)!=null) &&((String)params.get(i+"place"+j)).length()!=0){
							
							
							//first check if place is already in db
							Result checkResult = Functions.searchRecords("Client_Places", "id","location equals '" + cl.getLocationId() + "' AND client_county equals '" + cl.getCounties().get(i).getClientCountyId() + "' AND places equals '" + cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceId() + "'");
							if(checkResult.getCode()<0){
								String msg = "Error searching for current client place.";
								Logger.info(msg + ":\n" + checkResult.getMessage(), debug_category); 
								Functions.throwError(msg);
								resp.setTargetPage("error.jsp");
		
							}else if(checkResult.getCode()==0){
								
								
								//only if no records found save to db
								double distance = processProximity.calculateDistance(cl.getCounties().get(i).getPlacesAvailable().get(j).getLongitude(), cl.getLocationLong(), cl.getCounties().get(i).getPlacesAvailable().get(j).getLat(), cl.getLocationLat());
								int prox = processProximity.assignProximity(distance);
								
								Parameters newClientPlaceParams = Functions.getParametersInstance();
								newClientPlaceParams.add("location", cl.getLocationId());
								newClientPlaceParams.add("client_county", cl.getCounties().get(i).getClientCountyId());
								newClientPlaceParams.add("places", cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceId());
								newClientPlaceParams.add("distance", distance);
								newClientPlaceParams.add("proximity", prox);
								
								Result result = Functions.addRecord("Client_Places", newClientPlaceParams);
								
								int resultCode = result.getCode();
								if(resultCode < 0)
								{
									// Some error happened.
									String msg = "\n Place "+ cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName() + " could not be added";
								    Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
									Functions.throwError(msg);
								    resp.setTargetPage("error.jsp");
																	
								 
								} else {
									resp.setTargetPage("success.jsp");
									Logger.info("Add a success. Record id "+result.getID(), debug_category); 
				
								}
							}else {
								Functions.debug("record already in the db " + cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName());
								resp.setTargetPage("success.jsp");	
							}					
						}
					
					}
				
					
					resp.setTargetPage("success.jsp");
				}
				
				
				
				
				
			} else {
				//if none of my actions are called, then what error to throw?
			}
			
			
			//always put the filled cl object back in the request and then return
			params.put("clientLocation",cl);
		    resp.setData(params);
		} else {
			resp.setTargetPage("error.jsp");
			Functions.throwError("Cannot retrieve location_id from the parameters");
			
		}
			
		return resp;
	}
	
	
	//after the BO has been completely built - this updates it with the current page selections
	//returns the ClientLocation object updated
	private ClientLocation setSelectedFromPage(HashMap params, ClientLocation cl){
	
		//for each county set selected if it is selected on the page
		for(int i=0; i<cl.getCounties().size();i++){
			
			for(int j=0; j<cl.getCounties().get(i).getPlacesAvailable().size();j++){
				
				if(((String)params.get(i+"place"+j)!=null) &&((String)params.get(i+"place"+j)).length()!=0){
					cl.getCounties().get(i).getPlacesAvailable().get(j).setSelected(true);	
					
				}
			
			}
		}
		
		return cl;
		
		
	}
	

}