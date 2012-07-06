package com.platform.hometown.processProspectNContact;

import com.platform.api.*;

import java.util.Map;
import java.util.HashMap;

import com.platform.hometown.ProcessThirdPartyRequest.DataMapper;


public class ProspectContactMergeProcessor
{
	public Map process(Map<String,String> p) throws Exception
	{
		PropsectContactDuplicateSearch dupSearch = new PropsectContactDuplicateSearch(p);
		
		Parameters prospectParams = getProspectParams(p);
		
		Parameters contactParams = getContactParams(p);
		
		//Functions.debug("Contact params wufoo" + contactParams);
		
		Map<String,String> returnMap = new HashMap<String,String>();
		
		Map<String,String> prospectResultMap = dupSearch.searchForDuplicates(PropsectContactDuplicateSearch.PROSPECT_SEARCH);
		
		//Functions.debug("Here1");
		
		if(prospectResultMap != null)
		{
			//Functions.debug("Here2");
		
			String prospectId = prospectResultMap.get(PropsectContactDuplicateSearch.PROSPECT_ID );
			String primaryContactId = prospectResultMap.get(PropsectContactDuplicateSearch.PRIMARY_CONTACT_ID);
			
			
			
			Result result = Functions.updateRecord("LEAD",prospectId,prospectParams);
			
			if(result.getCode() < 0)
				throw new Exception(result.getMessage());
			
			returnMap.put("prospectId","Merged with Prospect - " + prospectId);
			
			//returnMap.put("prospectId",prospectId);
			
			//Functions.debug("Prospect Id " +prospectId);
			
			if(!DataMapper.isNullOrBlank(prospectId))
				dupSearch.prospectId = prospectId;
			
			Map<String,String> contactResultMap = dupSearch.searchForDuplicates(PropsectContactDuplicateSearch.CONTACT_SEARCH);
			
			if(contactResultMap != null)
			{
				//Functions.debug("Inside here");
			
				String contactId = contactResultMap.get(PropsectContactDuplicateSearch.CONTACT_ID);
			
				result = Functions.updateRecord("CONTACT",contactId,contactParams);	
				
				returnMap.put("contactId","Merged with Contact - "  + contactId );				
			}
			else
			{
				if(!DataMapper.isNullOrBlank(contactParams.get("last_name")))
				{
					if(primaryContactId == null || primaryContactId.equals("") || primaryContactId.equals("0"))
					{
						contactParams.add("related_to_id",prospectId);
						contactParams.add("related_to_type","LEAD");
						contactParams.add("flag_primary_contact","1");
						result = Functions.addRecord("CONTACT",contactParams);
						String contactId = result.getID();
						
						returnMap.put("contactId","Added Primary Contact - " + contactId );
					}
					else
					{
						contactParams.add("related_to_id",prospectId);
						contactParams.add("related_to_type","LEAD");
						result = Functions.addRecord("CONTACT",contactParams);
						String contactId = result.getID();
						
						returnMap.put("contactId","Added Contact - " + contactId);
					}
				}
			}
			
			//Functions.debug("Here5");
			
			returnMap.put("status","EXIST");
		}
		else
		{
			//Functions.debug("Here3");
			returnMap.put("status","DOESNOTEXIST");
		}
		
		return returnMap;
	}
	
	public Parameters getProspectParams(Map<String,String> p) throws Exception
	{
		Parameters prospectParams = Functions.getParametersInstance();
			
		prospectParams.add("name",p.get("name"));
		prospectParams.add("email_1157081194",p.get("email_1157081194"));
		prospectParams.add("phone",p.get("phone"));
		prospectParams.add("street",p.get("street"));
		prospectParams.add("city",p.get("city"));
		prospectParams.add("state",p.get("state"));
		prospectParams.add("zip",p.get("zip"));
		prospectParams.add("zip_code_1157081194",p.get("zip_code_1157081194"));
		prospectParams.add("description",p.get("description"));
		prospectParams.add("service_types_1157081194",p.get("service_types_1157081194"));
		//Functions.debug("service_types_1157081194= " + p.get("service_types_1157081194"));
		prospectParams.add("counties_1157081194",p.get("counties_1157081194"));
		prospectParams.add("website",p.get("website"));
		
		return prospectParams;
	}
	
	public Parameters getContactParams(Map<String,String> p) throws Exception
	{
		Parameters contactParams = Functions.getParametersInstance();
			
		contactParams.add("street",p.get("street"));
		contactParams.add("city",p.get("city"));
		contactParams.add("state",p.get("state"));
		contactParams.add("zip",p.get("zip"));
		contactParams.add("first_name",p.get("first_name"));
		contactParams.add("last_name",p.get("last_name"));
		contactParams.add("title",p.get("title"));
		contactParams.add("phone",p.get("contactPhone"));
		contactParams.add("mobile_phone",p.get("mobile_phone"));
		contactParams.add("fax",p.get("fax"));
		contactParams.add("email",p.get("email"));
		
		
		return contactParams;
	}
}