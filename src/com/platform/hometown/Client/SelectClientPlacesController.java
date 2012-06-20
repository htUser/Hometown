package com.platform.hometown.Client;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;

import com.platform.api.*;

import com.platform.hometown.proximity.*;

public class SelectClientPlacesController implements Controller {
	
	

	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		Functions.debug("In the execute method");
		String myAction = (String)params.get("action");
		//Functions.debug("The action is "+myAction);
		
		String locId;
		if(params.containsKey("myValue")){
			locId = (String)params.get("myValue");
			Functions.debug("locId is "+locId);
				
		} else {
			throw new Exception("Cannot retrieve location_id from the parameters");
		}
		
		
		ControllerResponse resp = new ControllerResponse();
		ClientLocation cl = new ClientLocation(locId,true);
		
		
		//Functions.debug("after cl is built");
		
		if(myAction.equals("getCounties")==true){		
			resp.setTargetPage("selectClientPlaces.jsp");
			
			
			
		} else if(myAction.equals("filterByCounty")==true){
			
			//remove later, never going to go thru this anymore
			
			String count = (String)params.get("numOfCounties");
			String selectedValue;
			
			
			for(int i=0; i<Integer.parseInt(count); i++){
				
				try{
					selectedValue = (String)params.get("selectedCounties");
					
				} catch (Exception e){
					
					String[] multiSelect = (String[])params.get("selectedCounties");
					selectedValue = multiSelect[i];
				}
					
				for(int j=0; j<cl.getCounties().size();j++){
					String testCId = cl.getCounties().get(j).getCountyId();
					//Functions.debug("value from object is "+testCId);
					
					if(testCId.equals(selectedValue)==true){
							
							cl.getCounties().get(j).setSelected(true);
							//Functions.debug("getSelected is "+cl.getCounties().get(j).getSelected());
							
						}
				}
				
			}
			
					
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
			
			//Functions.debug("In filterByLL");
			//Functions.debug("llButton pushed is "+(String)params.get("llCounty"));
			
			if(((String)params.get("llCounty")!=null)&&(((String)params.get("llCounty")).length()!=0)){	
					
				String llCount = (String)params.get("llCounty");
			
				//get Long/Lat from page, they both could be null
				Boolean latDataHere = false;
				Boolean longDataHere = false;
				double latPage = 0;
				double longPage = 0;
				String latOperator = new String();
				String longOperator = new String();
			
			
				
				if(((String)params.get("filterLat"+llCount)).length()!=0){
		
					latDataHere = true;
					latPage = Double.parseDouble((String)params.get("filterLat"+llCount));
					latOperator = (String)params.get("latOperator"+llCount);
					//Functions.debug("latDataHere"+latPage);
				}
							
			
				if(((String)params.get("filterLong"+llCount)).length()!=0){
		
					longDataHere = true;
					longPage = Double.parseDouble((String)params.get("filterLong"+llCount));
					longOperator = (String)params.get("longOperator"+llCount);
					//Functions.debug("longDataHere"+longPage);
				}
			
				//reset all the places to unselected first, then filter by LL to set selected
				cl.getCounties().get(Integer.parseInt(llCount)).resetPlacesAvailable();			
				cl.filterByLL(Integer.parseInt(llCount), latDataHere, latPage, longDataHere, longPage, latOperator, longOperator);
				
			}
				
				
				
						
			resp.setTargetPage("selectClientPlaces.jsp");
			
		
		} else if(myAction.equals("save")==true){
			
			Functions.debug("In the save");
			
			
			
			int countAdds = 0;
			//for each county, for each place, add a record if it is selected
			for(int i=0; i<cl.getCounties().size();i++){
				
				
				for(int j=0; j<cl.getCounties().get(i).getPlacesAvailable().size();j++){
					
					if(((String)params.get(i+"place"+j)!=null) &&((String)params.get(i+"place"+j)).length()!=0){
							
						double distance = processProximity.calculateDistance(cl.getCounties().get(i).getPlacesAvailable().get(j).getLongitude(), cl.getLocationLong(), cl.getCounties().get(i).getPlacesAvailable().get(j).getLat(), cl.getLocationLat());
						int prox = processProximity.assignProximity(distance);
						
						//save to db
						Functions.debug("place adding is "+cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName());
						
						Parameters newClientPlaceParams = Functions.getParametersInstance();
						newClientPlaceParams.add("location", cl.getLocationId());
						newClientPlaceParams.add("client_county", cl.getCounties().get(i).getClientCountyId());
						newClientPlaceParams.add("places", cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceId());
						newClientPlaceParams.add("distance", distance);
						newClientPlaceParams.add("proximity", prox);
						
						Result result = Functions.addRecord("Client_Places", newClientPlaceParams);
						countAdds++;
						int resultCode = result.getCode();
						Functions.debug("Result code is "+resultCode);
						
						if(resultCode < 0)
						{
						    // Some error happened.
						    String msg = "\n Place "+ cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName() + " could not be added";
						    Functions.debug(msg + ":\n" + result.getMessage());  // Log details
						   // Functions.throwError(msg + ".");                   // Do not throw exception, catch 
						    //adding a comment to test git
						 
						} else {
							Functions.debug("Add a success. Record id "+result.getID());
							Functions.debug("msg "+result.getMessage());
						}
										
					}
				
				}
			Functions.debug("Number of adds is "+countAdds);
				
				resp.setTargetPage("success.jsp");
			}
			
			
			
			
			
		} else {
			//if none of my actions are called, then what error to throw?
		}
		
		
		//always put the filled cl object back in the request and then return
		params.put("clientLocation",cl);
	    resp.setData(params);
		return resp;
	}
	
	
	//after the BO has been completely built - this updates it with the current page selections
	//returns the ClientLocation object updated
	private ClientLocation setSelectedFromPage(HashMap params, ClientLocation cl){
	
		//for each county set selected if it is selected on the page
		for(int i=0; i<cl.getCounties().size();i++){
			
			for(int j=0; j<cl.getCounties().get(i).getPlacesAvailable().size();j++){
				//Functions.debug("placej "+(String)params.get("place"+j));
				
				if(((String)params.get(i+"place"+j)!=null) &&((String)params.get(i+"place"+j)).length()!=0){
					cl.getCounties().get(i).getPlacesAvailable().get(j).setSelected(true);	
					//Functions.debug("Place "+cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName()+" is selected");
					
				}
			
			}
		}
		
		
		
		
		return cl;
		
		
	}
	

}