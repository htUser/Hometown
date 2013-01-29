package com.platform.hometown.ProcessThirdPartyRequest;

import java.util.Map;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.platform.api.*;

public class DataMapper
{
	public static String E_LEAD_POST = "elead-post";
	public static String P_LEAD_POST = "plead-post";
	public static String E_PROSPECT_POST = "eprospect-post";
	public static String REVIEW_POST = "review-post";
	public static String REVIEW_RESPONSE = "review-response-post";
	public static String CLIENT_SURVEY_POST = "client-survey-post";
	public static String EXIT_SURVEY_POST = "exit-survey-post";
	
	private String type;
	private Map<String,String[]> dataMap;
	
	public DataMapper()
	{
		super();
	}
	
	//	
	public DataMapper(String type, Map<String,String[]> dataMap)
	{
		this.type = type;
		this.dataMap = dataMap;
	}
	
	public Parameters prepareLJParams() throws Exception
	{
		
		Parameters ljParams = Functions.getParametersInstance();

		Map<String,String> thirdPartyToLjMap = getFieldsMap(this.type);

		for(Map.Entry<String,String> e : thirdPartyToLjMap.entrySet())
		{
			String thirdPartyField = e.getKey();
			String ljField = e.getValue();
			
			Object fieldValue = getFieldValue(this.dataMap.get(thirdPartyField));
			Functions.debug("Key = " + thirdPartyField + " Value = " + fieldValue);	
			 
			if(fieldValue != null)
			{
				ljParams.add(ljField, (String)fieldValue);

			}

		}
		
		ljParams = addAdditionalParams(ljParams);
		
		//Functions.debug(ljParams);
				
		return ljParams;
	}
	
	public Map<String,String> getEleadMap()
	{
		Map<String,String> eleadMap = new HashMap<String,String>();
		eleadMap.put("Field1","project_type");
		eleadMap.put("Field10","city");
		eleadMap.put("Field11","state");
		eleadMap.put("Field12","zip");
		eleadMap.put("Field13","first_name");
		eleadMap.put("Field14","last_name");
		eleadMap.put("Field15","phone");
		eleadMap.put("Field16","lead_email");
		eleadMap.put("Field19","project_start");
		eleadMap.put("Field21","description");
		eleadMap.put("Field38","tracker");
		eleadMap.put("Field37","client2");
		eleadMap.put("Field9","street_add");
		eleadMap.put("DateCreated","datetime_text");
		eleadMap.put("Field43","property_owner");
		eleadMap.put("Field45","service_type");
		eleadMap.put("Field47","user_note");
		eleadMap.put("Field49","prop_co");
		eleadMap.put("Field51","prop_rid");
		eleadMap.put("Field52","proplead_rid");
	
		
		return eleadMap;
	}
	
	public Map<String,String> getPleadMap()
	{
		Map<String,String> pleadMap = new HashMap<String,String>();
		pleadMap.put("acc","client2");
		pleadMap.put("call","call_id");
		pleadMap.put("call_end","call_end_text");
		pleadMap.put("call_start","datetime_text");
		pleadMap.put("called_number","tracker");
		pleadMap.put("caller_name","caller_id_name");
		pleadMap.put("cmp","camp_id");
		pleadMap.put("forward_no","forward_phone");
		pleadMap.put("grp","group_id");
		pleadMap.put("ring_duration","ring_duration");
		pleadMap.put("caller_number","caller_id_phone");
		pleadMap.put("answer_offset","answer_time");
		pleadMap.put("call_status","marchex_status");
		
		return pleadMap;
	}
	
	public Map<String,String> getProspectContactMap()
	{
		Map<String,String> prospectContactMap = new HashMap<String,String>();
		prospectContactMap.put("Field7","name");
		prospectContactMap.put("Field3","email_1157081194");
		prospectContactMap.put("Field6","phone");
		prospectContactMap.put("Field20","street");
		prospectContactMap.put("Field21","city");
		prospectContactMap.put("Field26","state");
		prospectContactMap.put("Field22","zip");
		prospectContactMap.put("Field24","description");
		prospectContactMap.put("Field12","service_types_1157081194");
		prospectContactMap.put("Field10","counties_1157081194");
		prospectContactMap.put("Field4","website");
		prospectContactMap.put("Field1","first_name");
		prospectContactMap.put("Field2","last_name");
		
		
				
		return prospectContactMap;
	}
	
	public Map<String,String> getReviewMap()
	{
		Map<String,String> reviewMap = new HashMap<String,String>();
		reviewMap.put("Field218","rating");
		reviewMap.put("Field3","completed");
		reviewMap.put("Field318","service_date");
		reviewMap.put("Field106","service_specs");
		reviewMap.put("Field319","cost");
		reviewMap.put("Field108","story");
		reviewMap.put("Field114","first_name");
		reviewMap.put("Field115","last_name");
		reviewMap.put("Field329","street");
		reviewMap.put("Field116","phone");
		reviewMap.put("Field327","email");
		reviewMap.put("Field325","client2");
		reviewMap.put("Field330","city_st");
				
		return reviewMap;
	}
	
	
	
	public Map<String,String> getClientResponseMap()
	{
		//Functions.debug("in getClientResponseMap()");
		
		Map<String,String> reviewMap = new HashMap<String,String>();
		reviewMap.put("Field225","id");
		reviewMap.put("Field105","response");
		reviewMap.put("Field107","responder_fname");
		reviewMap.put("Field108","responder_lname");
		
				
		return reviewMap;
	}
	
	public Map<String,String> getClientSurveyMap()
	{
		//Functions.debug("in getClientSurveyMap()");
		
		Map<String,String> reviewMap = new HashMap<String,String>();
		reviewMap.put("Field215","company_name");
		reviewMap.put("Field223","contact");
		reviewMap.put("Field225","comments_satisfaction");
		reviewMap.put("Field211","suggestions_for_improvement");
		reviewMap.put("Field227","ad_rating");
		reviewMap.put("Field228","customer_service_rating");
		reviewMap.put("Field229","price_rating");
		reviewMap.put("Field230","lead_quality_rating");
		reviewMap.put("Field231","ltc_rating");
		reviewMap.put("Field232","client_satisfaction_level");
		reviewMap.put("Field327","recommend_hometown");
		reviewMap.put("Field428","permission");
		reviewMap.put("Field213","client2");		
				
		return reviewMap;
	}
	
	public Map<String,String> getExitSurveyMap()
	{
		Functions.debug("in getExitSurveyMap()");
		
		Map<String,String> exitSurveyMap = new HashMap<String,String>();
		exitSurveyMap.put("Field233","company_name");
		exitSurveyMap.put("Field234","contact");
		exitSurveyMap.put("Field231","comments_satisfaction");
		exitSurveyMap.put("Field211","suggestions_for_improvement");
		exitSurveyMap.put("Field208","client_satisfaction_level");
		exitSurveyMap.put("Field222","recommend_hometown");
		exitSurveyMap.put("Field235","client2");
		exitSurveyMap.put("Field228","comments_cancellation");
		exitSurveyMap.put("Field227","reason_for_leaving");
		exitSurveyMap.put("Field220","re_sign_with_hometown");
		
				
		return exitSurveyMap;
	}
	

	Map<String,String> getFieldsMap(String type) throws Exception
	{
		
		
		if(type.equals(E_LEAD_POST))
			return getEleadMap();
		else if(type.equals(P_LEAD_POST))
			return getPleadMap();
		else if(type.equals(E_PROSPECT_POST))
			return getProspectContactMap();
		else if(type.equals(REVIEW_POST))
			return getReviewMap();
		else if(type.equals(REVIEW_RESPONSE))
			return getClientResponseMap();
		else if(type.equals(CLIENT_SURVEY_POST))
			return getClientSurveyMap();
		else if(type.equals(EXIT_SURVEY_POST))
			return getExitSurveyMap();
		else
		{
			Functions.debug("Invalid form type");
			throw new Exception("Invalid form type");
		}
	}

	
	
	public Parameters addAdditionalParams(Parameters ljParams) throws Exception
	{
		String marchexAcct = "";
		if(type.equals(E_LEAD_POST) || type.equals(P_LEAD_POST))
		{			
			String client = ljParams.get("client2");
			
			marchexAcct = client;
			//Functions.debug("SSpClient " + client );
			String clientId = getLookupFieldRecordId("e24e83b6385d495c84ffe4597bef47c1","number",client);
			
			if(clientId.equals(""))
			{
				ljParams.remove("client2");
				ljParams.remove("client");
			}
			else
			{
				//Functions.debug("ClientId " + clientId );
				ljParams.remove("client");
				ljParams.add("client2",clientId);
				ljParams.add("acct_id",clientId);
			}
			
			String tracker = ljParams.get("tracker");
			tracker = processPhoneField(tracker).trim();
			//Functions.debug("Tracker " + tracker );
			String trackerId = getLookupFieldRecordId("Campaigns","t_number",tracker);
			if(trackerId.equals(""))
				ljParams.remove("tracker");
			else
			{
				//Functions.debug("TrackerId " + trackerId );			
				ljParams.add("tracker",trackerId);
			}
			
		}
		
		if(type.equals(E_LEAD_POST))
		{
			ljParams.add("lead_type","Email");
			ljParams.add("validation","PENDING VALIDATION");
			
			String date_time = ljParams.get("datetime_text");
			//Functions.debug("datetime_text " + date_time );			
			String dateTimeDate = processDateField(date_time);
			//Functions.debug("dateTimeDate = " + dateTimeDate );
			ljParams.add("datetime_text",dateTimeDate);
			ljParams.add("date_time",dateTimeDate);
			
		}
		
		if(type.equals(P_LEAD_POST))
		{
			ljParams.add("lead_type","Call");
			
			ljParams.add("marchex_acc",marchexAcct );
			
			
			
			String call_end = ljParams.get("call_end_text");
			//Functions.debug("call_end_text" + call_end );			
			String callEndDate = processUTCDateField(call_end );
			ljParams.add("call_end",callEndDate );
			ljParams.add("call_end_text",callEndDate );
			
			String date_time = ljParams.get("datetime_text");
			//Functions.debug("datetime_text " + date_time );			
			String dateTimeDate = processUTCDateField(date_time);
			ljParams.add("datetime_text",dateTimeDate.toString());
			ljParams.add("date_time",dateTimeDate);
			
			ljParams.add("phone",ljParams.get("caller_id_phone"));
			
		}
		
		if(type.equals(E_PROSPECT_POST ))
		{
			ljParams.add("zip_code_1157081194",processZipCode(ljParams.get("zip"),ljParams.get("city")));
			ljParams.add("email",ljParams.get("email_1157081194"));
			//ljParams.remove("zip");
		}
		
		if(type.equals(REVIEW_POST))
		{
			//Change service date to LJ date format
			String date_time = ljParams.get("service_date");
			//Functions.debug("service_date before" + date_time );			
			String dateTimeDate = processDateFieldNoTime(date_time);
			//Functions.debug("service_date after= " + dateTimeDate );
			ljParams.add("service_date",dateTimeDate);
			
			//map Rating to LJ 1-5 numeric value
			String rating = ljParams.get("rating");
			if(rating.equalsIgnoreCase("Very Poor"))
			{
				rating = "1";
			} else if(rating.equalsIgnoreCase("Poor"))
			{
				rating = "2";
			} else if(rating.equalsIgnoreCase("OK"))
			{
				rating="3";
			} else if(rating.equalsIgnoreCase("Good"))
			{
				rating="4";
			}else if(rating.equalsIgnoreCase("Very Good"))
			{
				rating = "5";
			} else
			{
				//default to 3 if rating not found
				rating="3";
			}
			
			ljParams.add("rating", rating);
				
		}if(type.equals(REVIEW_RESPONSE))
		{

    		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    		Date date = new Date();
    		//Functions.debug("resp_rec_date= " +dateFormat.format(date));
    		
    		ljParams.add("resp_rec_date",dateFormat.format(date));
				
		}
		
		if(type.equals(CLIENT_SURVEY_POST))
		{    		
    		ljParams.add("survey_type","Satisfaction");
    		ljParams.add("survey_status","New");
    		
    		String permission = ljParams.get("permission");
    		//Functions.debug("Permission is " + permission);
    		if((permission==null)||(permission.length()==0)){
    			//if permission variable was not sent over - then default to No
    			//Functions.debug("in the if");
    			ljParams.add("permission", "No");
    			
    		}
		}
		
		if(type.equals(EXIT_SURVEY_POST))
		{    		
    		ljParams.add("survey_type","Exit");
    		ljParams.add("survey_status","New");
    		
    		
		}
		
		return ljParams;
		
	}

	public Object getFieldValue(String[] s)
	{
		String field = "";
		
		if(s != null)
		{
			if(s.length > 0)
			{
				boolean isFirst = true;

				for(String str : s)
				{
					if(isFirst)
						field = str;
					else
						field = field + "," + str;
						
				}

				return field;

			}
			else
			{
				return null;	
			}
		}
		else
			return null;
	}
	
	public String getLookupFieldRecordId(String objectId, String filterField, String value) throws Exception
	{
		try
		{
			if(objectId == null || objectId.trim().length() == 0)
			{
				throw new Exception("Invalid object Id of looked up object");
			}
			
			if(value == null || value.trim().length() == 0)
			{
				throw new Exception("Invalid search criteria for Looked up record");
			}
			
			Result result = Functions.searchRecords(objectId,"record_id",filterField+"="+"'"+value+"'");
			
			if(result.getCode() < 0)
			{
				//Functions.debug("SSp search Response= " +result.getCode());
				throw new Exception(result.getMessage());
			}
			
			if(result.getCode() == 0)
			{
				//Functions.debug("SSp search Response= " +result.getCode());
				throw new Exception("Looked up record not found");
			}
			
			ParametersIterator iterator = result.getIterator();
	  		while(iterator.hasNext())
			{
			    //Functions.debug("SSp search Response= " +result.getCode());
			    Parameters params = iterator.next();
			    String record_id = params.get("record_id");
			    //Functions.debug("SSp search Response params = " +params);
			    //Functions.debug("SSp search Response record_id= " +params.get("record_id"));
			    return record_id;
			}
		}
		catch(Exception e)
		{
			//Do something if needed
		}
		return "";
	}
	
	public String processPhoneField(String value) throws Exception
	{	
		if(value != null && !value.equals(""))
		{
			value = value.trim();
			
			String[] ary = value.split("");
			
			if(ary.length == 11)
			{
				//no longer split phone number up - keeping the general method as it validates phone number length
				/*String phoneNo = "("+ary[1]+ary[2]+ary[3]+")"
						+" "
						+ary[4]+ary[5]+ary[6]
						+"-"
						+ary[7]+ary[8]+ary[9]+ary[10];
						
				return phoneNo;*/
				return value;
			}
			else
			{
				throw new Exception("Invalid phone no.");
			}
		}
		
		return "";
		
	}
	
	public String processDateField(String dateStr) 
	{
		try
		{
			if(dateStr != null && !dateStr.equals(""))
			{
				String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    		//Functions.debug("date_str = " +dateStr);
		    		Date newDate = (Date)sdf.parse(dateStr.trim());
		    		DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
		    		sdf = new SimpleDateFormat(DATE_FORMAT);
		    		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		    		dateStr = sdf.format(newDate);
		    		//Functions.debug("newDate = "+newDate);
		    		//Functions.debug("dateStr = " + dateStr);
		    		return dateStr ;
	    		}
	    		
	    	}
	    	catch(Exception e)
	    	{
	    		Functions.debug(e);
	    		return "";
	    		
	    	}
	    	return "";
	}
	
	public String processDateFieldNoTime(String dateStr) 
	{
		try
		{
			if(dateStr != null && !dateStr.equals(""))
			{
				
				String DATE_FORMAT = "yyyyMMdd";
	    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    		//Functions.debug("date_str = " +dateStr);
	    		
	    		Date newDate = (Date)sdf.parse(dateStr.trim());
	    		DATE_FORMAT = "MM/dd/yyyy";
	    		sdf = new SimpleDateFormat(DATE_FORMAT);
	    		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    		dateStr = sdf.format(newDate);
	    		//Functions.debug("newDate = "+newDate);
	    		//Functions.debug("dateStr = " + dateStr);
	    		return dateStr ;
	    	}
	    		
	    }
	    catch(Exception e)
	    {
	    	Functions.debug(e);
	    	return "";
	    		
	    }
	    	return "";
	}
	
	public String processUTCDateField(String dateStr) 
	{
		try
		{	
			if(dateStr != null && !dateStr.equals(""))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date d = sdf.parse(dateStr);
				sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
				sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
				dateStr = sdf.format(d);
				return dateStr ;
			}
			
	    	}
	    	catch(Exception e)
	    	{
	    		return "";
	    	}
	    	
	    	return "";
	    	
	}
	
	public String processZipCode(String zipCode, String city) throws Exception
	{
		Result result = Functions.searchRecords("Zips","record_id,city,primary_record","zip_code equals '" + zipCode + "'");
		
		if(result.getCode() == 1)
		{
			ParametersIterator iterator = result.getIterator();
	  		while(iterator.hasNext())
			{
			    Parameters params = iterator.next();
			    String record_id = params.get("record_id");
			    return record_id;
			}
		}
		else if(result.getCode() > 1)
		{
			if(city != null && !city.equals(""))
			{
				ParametersIterator iterator = result.getIterator();
		  		while(iterator.hasNext())
				{
				    Parameters params = iterator.next();
				    String record_id = params.get("record_id");
				    String city1 = params.get("city");
				    
				    if(city.equals(city1))
				       return record_id;
				}
			}
			
			ParametersIterator iterator = result.getIterator();
		  	while(iterator.hasNext())
			{
			    Parameters params = iterator.next();
			    String record_id = params.get("record_id");
			    String primaryRec= params.get("primary_record");
			    
			    if("P".equals(primaryRec))
			       return record_id;
			}
		}
		
		return "";
		
		
	}
	
	public static boolean isNullOrBlank(String val)
	{
		if(val == null || val.trim().length() == 0)
		{
			return true;
		}
		
		return false;
	}
}