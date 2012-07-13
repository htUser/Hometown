package com.platform.hometown.Client;

import java.util.HashMap;
import java.util.ArrayList;

import com.platform.api.Controller;
import com.platform.api.ControllerResponse;
import com.platform.api.Functions;
import com.platform.api.Logger;
import com.platform.api.Parameters;
import com.platform.api.ParametersIterator;
import com.platform.api.Result;
import com.platform.api.RunTest;
import com.platform.api.TestMethod;

public class SelectKeyWordsController implements Controller {
	String debug_category = "SelectKeyWordsController";
	

	@Override
	public ControllerResponse execute(HashMap params) throws Exception {
		String cId; //campaign id
		ControllerResponse resp = new ControllerResponse();
		
		String myAction = (String)params.get("action");
		Logger.info("The action is "+myAction, debug_category); 
	      
		if(params.containsKey("myValue")){
			cId = (String)params.get("myValue");
			Logger.info("The cId is "+cId, debug_category); 
		   	
		} else {
			throw new Exception("Cannot retrieve campaign id from the parameters");
		}
		
		
		
		if(myAction.equals("fill")){
			
			//get possible keywords available by campaign id
			ArrayList<KeyWordsBO> keyWords = fillKeyWordsAvailable(cId);
			
			params.put("keyWords",keyWords);
			resp.setTargetPage("selectKeyWords.jsp");
			
		
		} else if(myAction.equals("select")){
			String keyWordId;
			
			
			if(params.containsKey("keyWordId")){
				keyWordId = (String)params.get("keyWordId");
				Logger.info("The keyWordId is "+keyWordId, debug_category); 
			   	
			} else {
				throw new Exception("Cannot retrieve keyWordId from the parameters");
			}
			
			params.put("keyword",keyWordId);
			params.put("myValue", "fill");
			resp.setTargetPage("buildsByKeyword.jsp");
			
		} else {
			
			resp.setTargetPage("error.jsp");
		}
		
		params.put("cId", cId);
	    resp.setData(params);
		return resp;
		
	}
			   
	public ArrayList<KeyWordsBO>  fillKeyWordsAvailable(String cId) throws Exception{
		
		ArrayList<KeyWordsBO> keyWords = new ArrayList<KeyWordsBO>();
		
		String sqlString = "SELECT campaign, keyword_text, id FROM Campaign_Keywords WHERE campaign ='"+cId+"'";
		Result result = Functions.execSQL(sqlString);
		   int resultCode = result.getCode();
		   if (resultCode < 0)
		   {
		      // Error occurred
			   String msg = "Error with Keyword search";
			   Logger.info(msg + " Campaign id is "+cId+":\n" + result.getMessage(), debug_category); 
			   Functions.throwError(msg + "."); 
		   }
		   else if (resultCode == 0){
			   // No Records Found
			   String msg = "No KeyWord records found";
			   Logger.info(msg + " Campaign id is "+cId+":\n" + result.getMessage(), debug_category); 
			   Functions.throwError(msg + "."); 
		   }			   
		   else if (resultCode > 0)
		   {	
			   //Success
			   Logger.info("Found Keywords" + ":\n" + result.getMessage(), debug_category); 
			   ParametersIterator it = result.getIterator();
		     
			   while(it.hasNext()){
		    	  Parameters params = it.next();
		    	  
		    	  KeyWordsBO k = new KeyWordsBO();
		    	  k.setKeyWordName((String)params.get("keyword_text"));
		    	  k.setKeyWordId((String)params.get("id"));
		    	  
		    	  keyWords.add(k);
		    	  
		      }
		   }
			 	   
		return keyWords;
	}
			   
	@TestMethod
	public void testFillKeyWordsAvailableRecordsSuccess() throws Exception{
		int actual = 0;
		int expect = 4;
		
		try{
			ArrayList<KeyWordsBO> keyWords = fillKeyWordsAvailable("1761321266");
			actual = keyWords.size();
		
		} catch (Exception e){
			
		}
		
		RunTest.assertEquals(String.valueOf(expect), String.valueOf(actual));
		
	}
	
	@TestMethod
	public void testFillKeyWordsAvailableNoRecords() throws Exception{
		String actual = new String();
		String expect = "No KeyWord records found.";
		
		try{
			fillKeyWordsAvailable("000133");
		} catch (Exception e){
			actual = "" + e.getMessage();
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
	@TestMethod
	public void testFillKeyWordsAvailableErrorRecords() throws Exception{
		String actual = new String();
		String expect = "Error with Keyword search.";
		
		try{
			fillKeyWordsAvailable("000133' ORDERJJN");
		} catch (Exception e){
			actual = "" + e.getMessage();
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
	@TestMethod
	public void testExecuteNoCampaignId() throws Exception{
		String actual = new String();
		String expect = "Cannot retrieve campaign id from the parameters";
				
		HashMap params = new HashMap();
					
		try{
			execute(params);
		} catch (Exception e){
			actual = "" + e.getMessage();
					
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
	@TestMethod
	public void testExecuteNoKeyWordId() throws Exception{
		String actual = new String();
		String expect = "Cannot retrieve keyWordId from the parameters";
				
		HashMap params = new HashMap();
		params.put("action", "select");
		params.put("myValue", "1761321266");
		
					
		try{
			execute(params);
		} catch (Exception e){
			actual = "" + e.getMessage();
					
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
	@TestMethod
	public void testExecuteFill() throws Exception{
		int actual = 0;
		int expect = 4;
				
		HashMap params = new HashMap();
		params.put("action", "fill");
		params.put("myValue", "1761321266");
		
					
		try{
			ControllerResponse cr = execute(params);
			java.util.HashMap params2 = (java.util.HashMap)cr.getData();
			ArrayList<KeyWordsBO> keyWordsArray = (ArrayList<KeyWordsBO>)params2.get("keyWords");
			actual = keyWordsArray.size();
			
			
		} catch (Exception e){
			
					
		}
		
		RunTest.assertEquals(String.valueOf(expect), String.valueOf(actual));
		}
	
	@TestMethod
	public void testExecuteSelect() throws Exception{
		String actual = new String();
		String expect = "buildsByKeyword.jsp";
				
		HashMap params = new HashMap();
		params.put("action", "select");
		params.put("myValue", "1761321266");
		params.put("keyWordId", "160914743");
		
					
		try{
			ControllerResponse cr = execute(params);
			actual = cr.getTargetPage();
			
			
			
		} catch (Exception e){
								
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
	@TestMethod
	public void testExecuteOther() throws Exception{
		String actual = new String();
		String expect = "error.jsp";
				
		HashMap params = new HashMap();
		params.put("action", "kdkdk");
		params.put("myValue", "1761321266");
		params.put("keyWordId", "160914743");
		
					
		try{
			ControllerResponse cr = execute(params);
			actual = cr.getTargetPage();
			
			
			
		} catch (Exception e){
								
		}
		
		RunTest.assertEquals(expect, actual);
		
	}
	
}
