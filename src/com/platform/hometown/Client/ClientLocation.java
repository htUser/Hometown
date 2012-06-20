package com.platform.hometown.Client;

import com.platform.api.*;
import com.platform.hometown.proximity.processProximity;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientLocation {
	
	Client c;
	String locationId = new String();
	Double locationLat;
	Double locationLong;
	
	String address = new String();
	ArrayList <Counties>countiesAvailable = new ArrayList<Counties>();
	

	
	/**
	 * 
	 * @param loc
	 * @param retrievePlaces
	 * @throws Exception
	 */
	public ClientLocation(String loc, Boolean retrievePlaces) throws Exception{
		//fill up the ClientLocation object
		Functions.debug("In the ClientLocation constructor");
		
		locationId = loc;
		
        
		//get the company info based on location id
		
		String sql = "SELECT   c.company_name, c.id, pl.client2 , pl.address , pl.latitude , pl.longitude, pl.id "+
				"FROM Primary_Locations AS pl INNER JOIN Clients as c ON c.id = pl.client2 "+
				"WHERE pl.id = '" + locationId +"' ";
		
		Result result = Functions.execSQL(sql);
		   int resultCode = result.getCode();
		   if (resultCode < 0)
		   {
		      // Error occurred
		      String msg = "Sample: Error during SQL search";
		      Functions.debug("Sample:\n" + result.getMessage());
		   }
		   else if (resultCode > 0)
		   {
			   
			   Functions.debug("got to the results set");
		      // A record was found. (Otherwise, resultCode == 0)                     
		      ParametersIterator it = result.getIterator();
		     
		      while(it.hasNext()){
		    	  Parameters params = it.next();
		    
					c = new Client((String)params.get("client2"));
					c.setName((String)params.get("company_name"));
					this.address = (String)params.get("address");
					//Functions.debug("2");
					
					
					//work around for the getFloat that is not working!!!!
					Result r = Functions.getRecord("Primary_Locations", "longitude, latitude",this.locationId );
					if(r.getCode()>0){
						Parameters p = r.getParameters();
						
						Float f = p.getFloat("longitude");
						this.locationLong = f.doubleValue();
						
						f = p.getFloat("latitude");
						this.locationLat = f.doubleValue();
					}
					
					
					//Double d = params.getDouble("longitude");
					//Float d = (Float)params.getFloat("longitude");
					
					//Float f = (Float)params.getFloat("longitude");
					//Functions.debug("2");
					//this.locationLong =  Double.valueOf(f);
				//	this.locationLong = f.doubleValue();
					
					
			//		Functions.debug("3");
				//	f = params.get("latitude");
				//	this.locationLat =  Double.valueOf(f);
					
				
		    }
					
		} else{
			throw new Exception("Error trying to retrieve location data");
		
		}
		
		//Functions.debug("end of constructor");
		this.setCountiesAvailable(fillCountiesAvailableSQL(retrievePlaces));
		
		
		//turn off the update functionality
		//this.retrieveLocationPlaceRecords(this.locationId);
		
		
	}
	
	private void retrieveLocationPlaceRecords(String locId) throws Exception{
		//call the db to retrieve all location-places records with this locationId
		String sql = "SELECT location, places, client_county, id " +
				"FROM Client_Places WHERE location = "+locId;
		
		
		 Result result = Functions.execSQL(sql);
		   int resultCode = result.getCode();
		   if (resultCode < 0)
		   {
		      // Error occurred
		      String msg = "Sample: Error during SQL search";
		      Functions.debug("Sample:\n" + result.getMessage());
		   }
		   else if (resultCode > 0)
		   {
			   
			   Functions.debug("got to the results set");
		      // A record was found. (Otherwise, resultCode == 0)                     
		      ParametersIterator it = result.getIterator();
		     
		      while(it.hasNext()){
		    	  Parameters params = it.next();
		    	  
		    	  //call a method to do the counties search set the values in the county record
		    	  populateLocationPlace((String)params.get("places"), (String)params.get("client_county"), (String)params.get("id"));
		    	  
		      }
		      
		   }
		
		
	}
	
	/**
	 * Loops thru the countiesAvailable array and finds the county that contains the place that has a location_place record in the db.
	 * @param placeId
	 * @param clientCountyId
	 * @param locationPlaceId
	 */
	private void populateLocationPlace(String placeId, String clientCountyId, String locationPlaceId){
		
		//loop thru the counties and find the one
		for(int i =0; i<this.getCounties().size(); i++){
		
			if(this.getCounties().get(i).getClientCountyId().equals(clientCountyId)){
				
				//now loop thru the places available and find the one
				for(int j =0; i<this.getCounties().get(i).getPlacesAvailable().size(); j++){
					
					if(this.getCounties().get(i).getPlacesAvailable().get(j).getPlaceId().equals(placeId)){
						
						//yeah, found it, now set the location_place id
						this.getCounties().get(i).getPlacesAvailable().get(j).setLocationPlaceId(locationPlaceId);
						this.getCounties().get(i).setSelected(true);
						this.getCounties().get(i).getPlacesAvailable().get(j).setSelected(true);
						break;
						
					}
				
				}
			
			}
			
		}
		
		
	}
	
	
	public void setCountiesAvailable(ArrayList<Counties> countiesAvailable) {
		this.countiesAvailable = countiesAvailable;
	}


	public Client getClient(){
		return c;
	}
	
	public String getLocationId(){
		return locationId;
	}
	
	public String getAddress(){
		return address;
	}
	
	public ArrayList<Counties> getCounties(){
		
		return countiesAvailable;
	}
	

	/**
	 * Retrieves all the Counties available for this location via the locationId.
	 * @param getPlaces	- Boolean, True if we populate the places available to this location 	
	 * @throws Exception
	 */
	private ArrayList<Counties> fillCountiesAvailableSQL(Boolean getPlaces) throws Exception{
		ArrayList <Counties> newCountiesArray= new ArrayList<Counties>();
		
		Functions.debug("in fillCountiesAvailable");
		//search for all the counties available for that location 
	
		
		String sql = "SELECT     c.state, c.county AS name, c.state_ab, c.id AS countyId, cc.id AS clientCountyId, cc.location, cc.county, cc.county_text, cc.client_text "+
				 "FROM      Client_Counties AS cc "+
				" INNER JOIN Counties AS c ON c.id = cc.county "+
				" WHERE cc.location = '" + this.locationId +"' ORDER BY c.state ASC, c.name ASC";
		
		Result result = Functions.execSQL(sql);
		   int resultCode = result.getCode();
		   if (resultCode < 0)
		   {
		      // Error occurred
		      String msg = "Sample: Error during SQL search";
		      Functions.debug("Sample:\n" + result.getMessage());
		   }
		   else if (resultCode > 0)
		   {
				   
			   Functions.debug("got to the results set");
		      // A record was found. (Otherwise, resultCode == 0)                     
		      ParametersIterator it = result.getIterator();
		     
		      while(it.hasNext()){
		    	  Parameters params = it.next();
				    //Functions.debug("params.get(county)" + (String)params.get("county"));
			    	//Functions.debug("params.get(county_text) " + (String)params.get("county_text"));
			    	//clientCountyId = (String)params.get("id");
			    	
			    	//Functions.debug("the client_county id is "+(String)params.get("id"));
			    	//Functions.debug("the location id is "+(String)params.get("location"));
			    	//Functions.debug("the state id is "+(String)params.get("state"));
			    	//Functions.debug("the county id is "+(String)params.get("county"));
				    	
				    	
		 			Counties c = new Counties((String)params.get("state"), (String)params.get("countyId"), (String)params.get("name"), (String)params.get("state_ab"),(String)params.get("clientCountyId"), (String)params.get("client_text"));
					c.setSelected(true);
					//Functions.debug("county is selected"+c.getSelected());
					
					if(getPlaces){
						newCountiesArray.add(fillPlacesAvailable(c));
					} else {
						
						newCountiesArray.add(c);
					}
					
			
		    
				  }
				
				
			}
		
		
		return newCountiesArray;
		
	}
	
	
	/**
	 * Retrieves all the Counties available for this location via the locationId.
	 * @param getPlaces	- Boolean, True if we populate the places available to this location 	
	 * @throws Exception
	 */
	private ArrayList<Counties> fillCountiesAvailable(Boolean getPlaces) throws Exception{
		ArrayList <Counties> newCountiesArray= new ArrayList<Counties>();
		
		Functions.debug("in fillCountiesAvailable");
		//search for all the counties available for that location 
		try{
			Result countiesResult = Functions.searchRecords("Client_Counties", "state, id, location, county, county_text, client_text","location equals '" + locationId + "'", "state", "asc","county_text", "asc", 0, 200);
			if(countiesResult.getCode()<0){
				throw new Exception("Error finding Counties 1");

			}else if(countiesResult.getCode()==0){
				//no records found
				throw new Exception("Error finding Counties 2");
				
			}else {
				//Success
				//Functions.debug("got the counties from the locations table");
				
				ParametersIterator iterator = countiesResult.getIterator();
				while(iterator.hasNext())
				  {
				    Parameters params = iterator.next();
				    	//lookup county name field from the counties table
				    	//Functions.debug("params.get(county)" + (String)params.get("county"));
				    	//Functions.debug("params.get(county_text) " + (String)params.get("county_text"));
				    	//clientCountyId = (String)params.get("id");
				    	
				    	//Functions.debug("the client_county id is "+(String)params.get("id"));
				    	//Functions.debug("the location id is "+(String)params.get("location"));
				    	//Functions.debug("the state id is "+(String)params.get("state"));
				    	//Functions.debug("the county id is "+(String)params.get("county"));
				    	
				    	
				    	Result cNameResult = Functions.getRecord("Counties", "state, county, state_ab, id",(String)params.get("county") );
						if(cNameResult.getCode()>0){
							
							//Functions.debug("got the county name");
							
							Parameters countyRecord = cNameResult.getParameters();
							//Functions.debug("searched Counties, county name is "+(String)countyRecord.get("county"));
							Counties c = new Counties((String)countyRecord.get("state"), (String)countyRecord.get("id"), (String)countyRecord.get("county"), (String)countyRecord.get("state_ab"),(String)params.get("id"), (String)params.get("client_text"));
							//c.setSelected(true);
							//Functions.debug("county is selected"+c.getSelected());
							
							if(getPlaces){
								newCountiesArray.add(fillPlacesAvailable(c));
							} else {
								
								newCountiesArray.add(c);
							}
							
						}else {
							throw new Exception("Error finding Counties 3");
						}
				    
				  }
				
				
			}
		} catch (Exception e){
			
			throw e;
		}
		
		return newCountiesArray;
		
	}
	
	
	
	private Counties fillPlacesAvailableSQL(Counties c) throws Exception{
		//Functions.debug("In the fillPlacesAvailableSQL()");
		//for each county find all the places available to choose
		
		
		ArrayList <Places> placesThisCounty = new ArrayList <Places> ();
		
		String sql = "SELECT  place, county_lookup, county, latitude, longitude, id, h_type FROM   Places "+
				"WHERE h_type != '' AND county_lookup = '"+c.getCountyId()+"' ORDER BY place ASC";
		

		Result result = Functions.execSQL(sql);
		   int resultCode = result.getCode();
		   if (resultCode < 0)
		   {
		      // Error occurred
		      String msg = "Sample: Error during SQL search";
		      Functions.debug("Sample:\n" + result.getMessage());
		   }
		   else if (resultCode > 0)
		   {
				   
			   Functions.debug("got to the results set");
		      // A record was found. (Otherwise, resultCode == 0)                     
		      ParametersIterator it = result.getIterator();
		     
		      while(it.hasNext()){
		    	  Parameters params = it.next();
			 	   
		    	  Float tempLat = params.getFloat("latitude");
		    	  Float tempLong = params.getFloat("longitude");
		    	  //Functions.debug("right before all Places Constructor");
		    	try{
		    		Places p = new Places((String)params.get("id"), (String)params.get("place"), (String)params.get("h_type"), tempLat.doubleValue(), tempLong.doubleValue());
		    		placesThisCounty.add(p);
		    	}catch (Exception e){
					//catch the null exception when lat/long data is missing
					//save it for a message for the user.
					
					
				}
		    	//Functions.debug("after places constructor");
				
				}
						
			  
		}
		
		Functions.debug("In the fillPlacesAvailable()2");
		c.setPlacesAvailable(placesThisCounty);
		return c;
		
		}
	
	
	private Counties fillPlacesAvailable(Counties c) throws Exception{
		Functions.debug("In the fillPlacesAvailable()");
		//for each county find all the places available to choose
		
		
		ArrayList <Places> placesThisCounty = new ArrayList <Places> ();
		Result placesResult = Functions.searchRecords("Places", "place, county_lookup, county, latitude, longitude, id, h_type","h_type contains 'ub' AND county_lookup equals '" + c.getCountyId()+"'", "place", "asc", "h_type", "desc", 0, 300);
		//Result placesResult = Functions.searchRecords("Places", "place, county_lookup, county, latitude, longitude, id, h_type"," county_lookup equals '" + c.getCountyId()+"'", "place", "asc", "h_type", "desc", 0, 300);
		
		// "place", "asc", "h_type", "desc", 0, 300);
		//Functions.debug("In the fillPlacesAvailable() 1");
		if(placesResult.getCode()<0){
			throw new Exception("Error finding Places"+placesResult.getCode());

		}else if(placesResult.getCode()==0){
			//no records found
			throw new Exception("Error finding Places 2");
				
		}else {
			//Success
			
			
				
			//Functions.debug("got the places");
			  ParametersIterator iterator = placesResult.getIterator();
			  while(iterator.hasNext())
			  {
				  
				 Parameters params = iterator.next();
				 
				// Functions.debug("place id "+(String)params.get("id")+"  place is "+(String)params.get("place"));
				   
			    Float tempLat = params.getFloat("latitude");
			    Float tempLong = params.getFloat("longitude");
			    //Functions.debug("right before all Places Constructor");
		    	try{
		    		Places p = new Places((String)params.get("id"), (String)params.get("place"), (String)params.get("h_type"), tempLat.doubleValue(), tempLong.doubleValue());
		    		placesThisCounty.add(p);
		    	}catch (Exception e){
					//catch the null exception when lat/long data is missing
					//save it for a message for the user.
					
					
				}
		    	//Functions.debug("after places constructor");
				
				}
						
			  
		}
		
		Functions.debug("In the fillPlacesAvailable()2");
		c.setPlacesAvailable(placesThisCounty);
		return c;
		
		}
	
	public double getLocationLong(){
		return locationLong;
		
	}
	
	public double getLocationLat(){
		return locationLat;
		
	}
	
	
	public void filterByMiles(String miles) {
		
		
		for(int i=0; i<this.getCounties().size();i++){
			for(int j=0; j<this.getCounties().get(i).getPlacesAvailable().size();j++){
				
				Places p = this.getCounties().get(i).getPlacesAvailable().get(j);
				
				double distance = processProximity.calculateDistance(p.getLongitude(), this.getLocationLong(), p.getLat(), this.getLocationLat());
				
				if(distance <= Double.parseDouble(miles)){
					
					this.getCounties().get(i).setSelected(true);
					this.getCounties().get(i).getPlacesAvailable().get(j).setSelected(true);
					//Functions.debug("place "+this.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName()+" is set to selected");
					
				}
			
			}
		}
	
	}
	
	public void filterByLL(int county, Boolean latDataHere, double latPage, Boolean longDataHere, double longPage, String latOperator, String longOperator){
		for(int j=0; j<this.getCounties().get(county).getPlacesAvailable().size();j++){
			
			
			//for each place set selected to true if either the place lat is greater than/less than the entered lat
			//or the place long is greater than/less than the entered long
			
			if(latDataHere==true){
				//Functions.debug("about the run lat data test");
				if(latOperator.equals("lessThan")==true){
					if(this.getCounties().get(county).getPlacesAvailable().get(j).getLat()<=latPage){
						//Functions.debug("place lat "+this.getCounties().get(county).getPlacesAvailable().get(j).getLat()+" is less than given page lat "+latPage);
						this.getCounties().get(county).setSelected(true);
						this.getCounties().get(county).getPlacesAvailable().get(j).setSelected(true);
						//Functions.debug("place "+this.getCounties().get(county).getPlacesAvailable().get(j).getPlaceName()+" is set to selected");
					}
				}else if(latOperator.equals("greaterThan")==true){ 
					if(this.getCounties().get(county).getPlacesAvailable().get(j).getLat()>=latPage){
						//Functions.debug("place lat "+this.getCounties().get(county).getPlacesAvailable().get(j).getLat()+" is greater than given page lat "+latPage);
						this.getCounties().get(county).setSelected(true);
						this.getCounties().get(county).getPlacesAvailable().get(j).setSelected(true);
						//Functions.debug("place "+this.getCounties().get(county).getPlacesAvailable().get(j).getPlaceName()+" is set to selected");
					}
				}
				
			}
			
			
				if(longDataHere==true){
					//Functions.debug("about the run long data test");
					if(longOperator.equals("lessThan")==true){
						if(this.getCounties().get(county).getPlacesAvailable().get(j).getLongitude()<=longPage){
							this.getCounties().get(county).setSelected(true);
							this.getCounties().get(county).getPlacesAvailable().get(j).setSelected(true);
							//Functions.debug("place "+cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName()+" is set to selected");
						}
					}else if(longOperator.equals("greaterThan")==true){
						if(this.getCounties().get(county).getPlacesAvailable().get(j).getLongitude()>=longPage){
							this.getCounties().get(county).setSelected(true);
							this.getCounties().get(county).getPlacesAvailable().get(j).setSelected(true);
							//Functions.debug("place "+cl.getCounties().get(i).getPlacesAvailable().get(j).getPlaceName()+" is set to selected");
						}
					}
					
				
				
			}
		
		}
	
		
		
	}
	
	
	}