package com.platform.hometown.proximity;

import com.platform.api.*;
import java.math.*;

public class processProximity {
	public void calculateProximity(Parameters p) throws Exception{
		
		if( (p.get("p_long")!=null) || (p.get("p_lat")!=null) || (p.get("a_long")!=null) || (p.get("a_lat")!=null) ) {
			
			//Functions.debug("In the calculateProximity method");
			
			double pageLong = p.getDouble("p_long");
			double pageLat = p.getDouble("p_lat");
			double addrLong = p.getDouble("a_long");
			double addrLat = p.getDouble("a_lat");
		
			double d = calculateDistance(pageLong, addrLong, pageLat, addrLat);
			int prox = assignProximity(d);
		
			
			Functions.debug("Proximity is " + prox + ", Distance is "+d);
			
			
			
			String recId = p.get("record_id");
			Result result = Functions.getRecord("Build", "prox_rating", recId);
			
			
			//Functions.debug("result of search is "+result.getCode());
			int resultCode = result.getCode();
			
			if(resultCode<0){
				
				Functions.debug("error in result");
			} else {
				
				//Functions.debug("result code is "+resultCode);
				Parameters params = result.getParameters();
				params.add("prox_rating", prox);
				params.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
				params.add(PLATFORM.PARAMS.RECORD.ENABLE_MULTIPART,"1");
				
				
				Result updateBuildResult = Functions.updateRecord("Build", recId, params);   
				//Functions.debug("updateBuildResult is "+updateBuildResult);
				
				if(updateBuildResult.getCode()<0){
					//there was an error updating the Build record
					throw new Exception("There was an error updating the build record with the proximity score.");
					
				
				}
				
			}
			
			
			
		} else {
			//if not all long/lat data is available then do nothing
		}
				
	}
	
	
	public static double calculateDistance(double long1, double long2, double lat1, double lat2){
		double distance;
		//Functions.debug("Long1 is "+long1);
		//Functions.debug("Long2 is "+long2);
	
		
		double radius = 3958.82; //earth's radius in miles
		double diffLat = Math.toRadians(lat2-lat1);
		double diffLong = Math.toRadians(long2-long1);
		double rLat1 = Math.toRadians(lat1);
		double rLat2 = Math.toRadians(lat2);
		
		
		
		double arg1 = Math.pow(Math.sin(diffLat/2), 2);
		double arg2 = Math.cos(rLat1) * Math.cos(rLat2) * Math.pow(Math.sin(diffLong/2), 2);
				
		distance = 2  * radius * Math.asin(Math.sqrt(arg1+arg2));
		
		return distance;
		
	}
	
	public static int assignProximity(double distance){
		int proximity;
		double absDistance;
				
		absDistance = Math.abs(distance);
			
		if(absDistance<8){
			proximity = 5;
		} else if (absDistance < 17){
			proximity = 4;
		} else if (absDistance <25){
			proximity = 3;
		} else if (absDistance <33){
			proximity =2;
		} else {
			proximity = 1;
		}
				
		return proximity;
		
	
	}
	
	
	@TestMethod
	public void testAssignProximity() throws Exception
	{
	    int d = 50;
	    String expect = "1";         
	    String actual = "" + assignProximity(d);          // Invoke the method you're testing

	    RunTest.assertEquals(expect, actual);
	    
	    d = 30;
	    expect = "2";
	    actual = "" + assignProximity(d);
	    RunTest.assertEquals(expect, actual);
	    
	    d = 22;
	    expect = "3";
	    actual = "" + assignProximity(d);
	    RunTest.assertEquals(expect, actual);
	    
	    d = 13;
	    expect = "4";
	    actual = "" + assignProximity(d);
	    RunTest.assertEquals(expect, actual);
	    
	    
	    d = 7;
	    expect = "5";
	    actual = "" + assignProximity(d);
	    RunTest.assertEquals(expect, actual);
	    
	    
	}
	
	
	@TestMethod
	public void testCalculateDistance() throws Exception
	{
	   
	    String expect = "12.387950326072586";         
	    String actual = "" + Math.abs(calculateDistance(42.9459, 42.907471, -85.613641, -85.792908));          // Invoke the method you're testing

	    RunTest.assertEquals(expect, actual);
	    
	    
	}
	

}