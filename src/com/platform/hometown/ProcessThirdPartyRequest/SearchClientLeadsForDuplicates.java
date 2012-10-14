package com.platform.hometown.ProcessThirdPartyRequest;

import com.platform.api.*;
import java.util.Date;

import java.text.SimpleDateFormat;


public class SearchClientLeadsForDuplicates
{
	public Integer search(Parameters requestParams) throws Exception
	{
		Result result = null;
		
		try
		{
			String leadType = (String)requestParams.get("lead_type");
			String phone = (String)requestParams.get("phone");
			
			//Get the date for 30 days ago
			String dateTimeStr = (String)requestParams.get("date_time");
			Date dateTime = requestParams.getDate("date_time");
			Long dateTimeInMillis = dateTime.getTime();
			Long _30DaysInMillis = 30*24*60*60*1000L;
			Date _30DaysAgoDate = new Date(dateTimeInMillis - _30DaysInMillis);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			String _30DaysDateTimeStr = sdf.format(_30DaysAgoDate);
			
			String client = (String)requestParams.get("client2");
			
			/*
			Functions.debug("Phone = " + phone + " date_time1 = "
			+ _30DaysDateTimeStr + " date_time2 = " + dateTimeStr
			+ " client = " + client );
                         */
			if(phone != null && phone.trim().length() == 0)
			{
				phone = null;
			}

			Integer duration = requestParams.getInteger("duration",0);
			
			if(leadType.equals("Call") && duration < 30)
			{	
				return new Integer(-2);
			}
			
			String trackerName = (String)requestParams.get("__Tracker_Name__");
			
			if(phone == null && duration >= 90 && trackerName.equals("DUMPSTER"))
			{
				return new Integer(0);
			}
			
			if(phone == null && duration > 30 && trackerName.equals("NODUMPSTER"))
			{
				return new Integer(-3);
			}
			
			if(phone == null)
			{
				return null;
			}
				
			
			//Form filter criteria for search
			String filter = "(phone equals '" + phone + "' or caller_id_phone equals '" + phone +"')"  
			                            + " AND date_time >= '" + _30DaysDateTimeStr + "'"
			                            + " AND date_time < '" + dateTimeStr +"'"
			                            + " AND client2 equals '" + client +"'"
			                            + " AND (duration > 30 or duration equals 'BLANK')"
			                            + " AND validate equals 'VALID LEAD'";
			
			result = Functions.searchRecords("Client_Leads", "record_id",filter,"", "", "", "", 0, 1);
			//result = Functions.searchRecords("Client_Leads", "record_id",filter); 
			
			//Functions.debug("result from search = " +result.getCode());
			
			return result.getCode();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		
	}
	
}