package com.platform.hometown.Territory;

import java.util.ArrayList;

import com.platform.*;
import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.PLATFORM;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;
import com.platform.hometown.Client.*;


public class FillTopPlaces {
	public void fillTopPlaces(Parameters p) throws Exception{
		
		String topPlaces = new String();
		String debug_category = "FillTopPlaces";
		
		//get territory id
		String territory = p.get("id");
		
		if((territory!=null)&&(territory.length()!=0)){
			
			//get all places related to that territory
			Result result;
			result = Functions.searchRecords ("Places", "h_type, pop2009, place, territory, id", "territory equals '" + territory + "'", "h_type", "asc", "pop2009", "desc", 0,200);
			
			
			//loop thru and create the top places string
			if(result.getCode()<0){
				 
				 String msg = "Error finding Place Records. Territory id is "+territory;
				 Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				 Functions.throwError(msg + "."); 

			}else if(result.getCode()==0){
				//no records found
				
				String msg = "Error: No Place records Found. Territory id is "+territory;
				Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				Functions.throwError(msg + "."); 
				
			}else {
				//Success
				
				ArrayList<Places> orderedList = new ArrayList<Places>();
								
				Logger.info("got the place records. Territory id is "+territory, debug_category);
								
				ParametersIterator iterator = result.getIterator();
				while(iterator.hasNext())
				  {
				    Parameters params = iterator.next();
				    
				    //create object put in the list in the correct order
				    Places place = new Places();
				    place.setPlaceName((String)params.get("place"));
				    place.sethType((String)params.get("h_type"));
				    place.setPop2009((String)params.get("pop2009"));
				    
				   
				    
				    Boolean validPlace = true;
				   
				    //do not put in those places that do not have a h_type or a population or are a Hub
					if(((place.gethType()==null)||(place.gethType().length()==0)) && ((place.getPop2009()==null)||(place.getPop2009().length()==0))){
						
						validPlace = false;
					} else if((place.gethType()!=null)||(place.gethType().length()!=0)){ 
						
						if(place.gethType().contains("Hub")==true){
							
							validPlace = false;
						}
					}
					 
				    //only put in the list if it is a valid place
				    if(validPlace==true){
				    	
				    	//Functions.debug("place adding is"+place.getPlaceName());
				    	
				    	Boolean foundSpot = false;
			    						    		
				    	
			    		//loop thru and compare the new place with the places in the ordered list to see if the new place goes before any place already in the list
			    		for(int i=0; i<orderedList.size();i++){
			    			
			    			Places orderedItem = orderedList.get(i);
			    							    			
			    			if(goesBefore(orderedItem, place)==true){
			    				foundSpot = true;
			    				orderedList.add(i,place);
			    				break;
			    			}
			    		}
			    		
			    		//if the new place does not go before any items in the list, add it at the end
			    		if(foundSpot==false){
			    			//Functions.debug("adding new place at end of list");
			    			orderedList.add(place);
			    			
			    		}
				    }
				    
				}
				
				
				
				//loop thru and create 6 top places string 
				int placesSize = 0;
				
				if(orderedList.size()<6){
					placesSize = orderedList.size();
				} else {
					placesSize = 6;
				}
					
				
				for(int i = 0; i < placesSize; i++){
					if(i != 0){
						topPlaces = topPlaces.concat(", ");
					}
					
					topPlaces = topPlaces.concat(orderedList.get(i).getPlaceName());
					
			    }
			    
			    
				//update the territory record
				Parameters params = new Parameters();
				params.add("top_subs", topPlaces);
				params.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
				params.add(PLATFORM.PARAMS.RECORD.ENABLE_MULTIPART,"1");
								
				Result updateResult = Functions.updateRecord("Territories", territory, params);  
			
				if(updateResult.getCode()<0){
					String msg = "There was an error updating the territory record with top places. Territory Id is "+ territory;
					Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
					Functions.throwError(msg + "."); 				
				}
			
			}
				
		} else {
			String msg = "Error getting the territory id from the parameters. Territory id is "+territory;
			Logger.info(msg, debug_category); 
			Functions.throwError(msg + "."); 	
			
		}
		
		
		
		
	}
	
	
	/**
	 * Returns true when the new place goes before the place in the list.  The order is first by Sub01 to Sub09, 
	 * then by population with the greatest population first.
	 * @param placeInList
	 * @param newPlace
	 * @return
	 */
	private boolean goesBefore(Places placeInList, Places newPlace){
		
		String oldHType = placeInList.gethType();
		String newHType = newPlace.gethType();
		
		//item in the list has a h_type
		if((oldHType!=null)&&(oldHType.length()!=0)){
			
			//new item has an h_type
			if((newHType!=null)&&(newHType.length()!=0)){
				
				//compare the two h_types
				if(oldHType.compareToIgnoreCase(newHType) > 0){
					//new h_type is less than the old h_type so it goes before in the list
					//Functions.debug("new h_type is less than old so goes before, returning true");
					
					return true;
				} else{
					//new h_type is greater than the old h_type so keep looking for it's place
					//Functions.debug("new h_type is greater than so returning false");
					return false;
				}
				
			} else {
				//new item does not have an h_type, so it cannot go before the current item, keep looking for it's place
				//Functions.debug("New item does not have an h_type so keep looking");
				return false;
			}
			
			
		} else if((newHType!=null)&&(newHType.length()!=0)){
			//the item in the list does not have an h_type, but the new item does, so it must come before the old item
			//Functions.debug("item in the list does not have an h_type, so new comes before old");
			return true;
			
		} else {
			//neither item has an h_type, so we must compare population
			int listPop;
			int newPop;
			
			//compare population values
			if((placeInList.getPop2009()==null)||(placeInList.getPop2009().length()==0)){
				listPop = 0;
			} else {
				listPop = Integer.parseInt(placeInList.getPop2009());
			}
			
			if((newPlace.getPop2009()==null)||(newPlace.getPop2009().length()==0)){
				newPop = 0;
			} else {
				newPop = Integer.parseInt(newPlace.getPop2009());
			}
			
			
			if(newPop > listPop){
				//new item has a bigger population so it goes first
				//Functions.debug("new item has a bigger pop so it goes first");
				return true;
			} else {
				//new item has a smaller population so keep looking for it's place
				//Functions.debug("new item has a smaller population os keep looking for its place");
				return false;
			}
		}
		
	
	}

}