package com.platform.hometown.Place;
import com.platform.api.*;


public class ZipCode {
	
	/**
	 *  Fills the allZips field and the topZips field of the place record accordingly.
	 * @param p Parameters from the Place record page
	 * @throws Exception
	 */
	public void fillZipCodes(Parameters p) throws Exception{
		
		String topZips = new String();
		String allZips = new String();
		String debug_category = "All/Top Zip Codes";
		
		//get the place id 
		String place = p.get("id");
		
		if((place!=null)&&(place.length()!=0)){
			
			//get all the zip records related to this place
			Result result;
			result = Functions.searchRecords ("Zips", "scp_code, pop2010, zip_code, id", "scp_code equals '" + place + "'", "pop2010", "desc", "zip_code", "asc", 0,200);
			
			if(result.getCode()<0){
				 //throw new Exception("Error finding Zip Code Records");
				 String msg = "Error finding Zip Code Records";
				 Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				 Functions.throwError(msg + "."); 

			}else if(result.getCode()==0){
				//no records found
				//throw new Exception("Error: No Zip Code records Found");
				String msg = "Error: No Zip Code records Found";
				Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
				Functions.throwError(msg + "."); 
				
			}else {
				//Success
				
				Logger.info("got the zip code records", debug_category);
				int count = 0;
				
				
				ParametersIterator iterator = result.getIterator();
				while(iterator.hasNext())
				  {
				    Parameters params = iterator.next();
				    
				    
				    //loop thru and create all zips / top zips string - should be already ordered by the sql
					if(count<3){
				    	topZips = topZips.concat((String)params.get("zip_code") + " ");
				    }
				    
				    allZips = allZips.concat((String)params.get("zip_code") + " ");
				    
				    count++;
				  }
				
				//update place record 
				Parameters params = new Parameters();
				params.add("top_zips", topZips);
				params.add("zips", allZips);
				params.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
				params.add(PLATFORM.PARAMS.RECORD.ENABLE_MULTIPART,"1");
								
				Result updatePlaceResult = Functions.updateRecord("Places", place, params);  
			
				if(updatePlaceResult.getCode()<0){
					//there was an error updating the Place record
					//throw new Exception("There was an error updating the place record with all/top zip codes. Place Id is "+ place);
					String msg = "There was an error updating the place record with all/top zip codes. Place Id is "+ place;
					Logger.info(msg + ":\n" + result.getMessage(), debug_category); 
					Functions.throwError(msg + "."); 				
				}
			
			
			}
		
		
		} else {
			
			//throw new Exception("Error getting the place id from the parameters. Place id is "+place);
			String msg = "Error getting the place id from the parameters. Place id is "+place;
			Logger.info(msg, debug_category); 
			Functions.throwError(msg + "."); 	
			
			
		}
	
	}
}
