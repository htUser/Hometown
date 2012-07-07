package com.platform.hometown.Territory;

import com.platform.*;
import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.PLATFORM;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;


public class FillTopPlaces {
	public void fillTopPlaces(Parameters p) throws Exception{
		String topPlaces = new String();
		String debug_category = "FillTopPlaces";
		
		//get territory id
		String territory = p.get("id");
		
		if((territory!=null)&&(territory.length()<0)){
			
			//get all places related to that territory
			Result result;
			result = Functions.searchRecords ("Places", "h_type, pop2009, place, territory, id", "territory equals '" + territory + "'", "h_type", "asc", "pop2009", "desc", 0,6);
			
			
			//loop thru and create the top places string
			if(result.getCode()<0){
				 
				 String msg = "Error finding Place Records";
				 Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				 Functions.throwError(msg + "."); 

			}else if(result.getCode()==0){
				//no records found
				
				String msg = "Error: No Place records Found";
				Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				Functions.throwError(msg + "."); 
				
			}else {
				//Success
				
				Logger.info("got the place records", debug_category);
				int count = 0;
				
				
				ParametersIterator iterator = result.getIterator();
				while(iterator.hasNext())
				  {
				    Parameters params = iterator.next();
				    
				    
				    //loop thru and create 6 top places string - should be already ordered by the sql
					if(count<6){
				    	topPlaces = topPlaces.concat((String)params.get("place") + " ");
				    }
				    
				    count++;
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

}
