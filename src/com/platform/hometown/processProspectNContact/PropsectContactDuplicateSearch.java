package com.platform.hometown.processProspectNContact;

import com.platform.hometown.ProcessThirdPartyRequest.DataMapper;
import com.platform.api.*;

import java.util.Map;
import java.util.HashMap;

public class PropsectContactDuplicateSearch
{
	static int PROSPECT_SEARCH = 1;
	static int CONTACT_SEARCH = 2;
	
	static String PROSPECT_ID = "PROSPECT_ID";
	static String CONTACT_ID = "CONTACT_ID";
	static String PRIMARY_CONTACT_ID = "PRIMARY_CONTACT_ID";
	static String IS_PRIMARY_CONTACT = "IS_PRIMARY_CONTACT";

	String phoneNo = "";
	String emailAddr = "";
	String firstName = "";
	String lastName = "";
	public String prospectId = "";
	
	public PropsectContactDuplicateSearch(Map<String,String> p) throws Exception
	{
		
		this.phoneNo = p.get("phone");
		DataMapper dm = new DataMapper();
		//this.phoneNo = dm.processPhoneField(this.phoneNo);
		//Functions.debug(this.phoneNo);
		this.emailAddr = p.get("email_1157081194");
		this.firstName = p.get("first_name");
		this.lastName = p.get("last_name");
		this.prospectId = p.get("prospectId");
		
		if(DataMapper.isNullOrBlank(this.phoneNo))
		{
			this.phoneNo = null;
		}
		
		if(DataMapper.isNullOrBlank(this.emailAddr))
		{
			this.emailAddr = null;
		}
		
		if(DataMapper.isNullOrBlank(this.firstName))
		{
			this.firstName = null;
		}
		
		if(DataMapper.isNullOrBlank(this.lastName))
		{
			this.lastName = null;
		}
		
		if(DataMapper.isNullOrBlank(this.prospectId))
		{
			this.prospectId = null;
		}
	}
	
	public Map<String,String> searchForDuplicates(int type) throws Exception
	{
		Result result = null;
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		if(type == PROSPECT_SEARCH)
		{
			result = search(PROSPECT_SEARCH);
			
			//Functions.debug("Code = " + result.getCode());
			
			if(result.getCode() >= 1)
			{
				ParametersIterator iterator = result.getIterator();
		  		while(iterator.hasNext())
				{
				    Parameters params = iterator.next();
				  
				    String prospectId = params.get("record_id");
				    String primaryContactId = params.get("primary_contact_id");
				    
				    resultMap.put(PROSPECT_ID,prospectId);
				    resultMap.put(PRIMARY_CONTACT_ID,primaryContactId);
				    
				    return resultMap;
				}
			}
			/*else if(result.getCode() == 0)
			{
				result = search(CONTACT_SEARCH);
				
				if(result.getCode() >= 1)
				{
					ParametersIterator iterator = result.getIterator();
			  		while(iterator.hasNext())
					{
					    Parameters params = iterator.next();
					  
					    String contactId = params.get("record_id");
					    String prospectId = params.get("related_to_id");
					    String isPrimaryContact = params.get("flag_primary_contact");
					    
				    	    resultMap.put(CONTACT_ID ,contactId );
				    	    resultMap.put(PROSPECT_ID,prospectId);
				    	    resultMap.put(IS_PRIMARY_CONTACT ,isPrimaryContact );
				    
					    return resultMap;
					}
				}		
			}*/
			else if(result.getCode() < 0)
			{
				//Functions.debug(result.getMessage());
				throw new Exception("Error in ProspectDuplicateSearch - searchForDuplicates() : " + result.getMessage());
			}
		}
		else if(type == CONTACT_SEARCH)
		{
		
			result = search(CONTACT_SEARCH);
				
			if(result !=null && result.getCode() >= 1)
			{
				ParametersIterator iterator = result.getIterator();
				while(iterator.hasNext())
				{
				    Parameters params = iterator.next();
				   
				    String contactId = params.get("record_id");
				    String prospectId = params.get("account_id");
				    String isPrimaryContact = params.get("flag_primary_contact");
					    
				    resultMap.put(CONTACT_ID ,contactId );
				    resultMap.put(PROSPECT_ID,prospectId);
				    resultMap.put(IS_PRIMARY_CONTACT ,isPrimaryContact );
				    
				    return resultMap;
				}
			}
		}
		
		return null;
	}
	
	public String createSearchString(int type)
	{
		String searchStr = "";
		
		if(type == PROSPECT_SEARCH)
		{
			
			if(this.phoneNo != null)
			{
				searchStr = "phone equals '"+ this.phoneNo + "'";
			}
			
			if(this.emailAddr != null)
			{
				if(!searchStr.equals(""))
				{
					searchStr = searchStr + " or ";
				}
				
				searchStr = searchStr + " email_1157081194 equals '" + this.emailAddr + "'";
			}
			
		}
		else if(type == CONTACT_SEARCH)
		{
			
		/*
			searchStr = "(phone equals '"+ this.phoneNo + "'" 
					+ " or mobile_phone equals '" + this.phoneNo+ "'" 
					+ " or other_phone equals '" + this.phoneNo + "'"
					+ " or email equals '" + this.emailAddr + "')"
					+ " and first_name equals '" + this.firstName + "'" 
					+ " and last_name equals '" + this.lastName + "'"
					+ (this.prospectId == null 
						|| this.prospectId.equals("") ? "" : " and related_to_id equals '" + this.prospectId+"'") ;
		*/	
		/*	StringBuilder sb = new StringBuilder();
			if(this.phoneNo != null)
			{
				sb.append("phone equals '"+ this.phoneNo + "'" 
					+ " or mobile_phone equals '" + this.phoneNo+ "'" 
					+ " or other_phone equals '" + this.phoneNo + "'");
					
			}
			
			if(this.emailAddr != null)
			{
				if((sb.toString()).length() > 0)
				{
					sb.append(" or ");
				}
				
				sb.append(" email equals '" + this.emailAddr + "'");
			}
			
			if((sb.toString()).length() > 0)
			{
				sb.insert(0,"(");
				sb.append(")");
			}
			
			if((sb.toString()).length() > 0)
			{
				sb.append(" and ");
			}
			
			sb.append(" first_name equals '" + (this.firstName == null ? "blank" : this.firstName) + "'" 
					+ " and last_name equals '" + (this.lastName == null ? "blank" : this.lastName) + "'");
					
			if(this.prospectId != null )
			{
				sb.append(" and related_to_id equals '" + this.prospectId+"'" );
			}
			
			if((sb.toString()).length() > 0)
			{
				sb.append(" and related_to_type equals 'LEAD' ");
			}
			*/
			StringBuilder sb = new StringBuilder();
			
			//Functions.debug("Inside createSearchString ProspectId =" + this.prospectId + " lastname = " + this.lastName);
			
			if(this.lastName != null && this.prospectId != null )
			{
				sb.append(" first_name equals '" + (this.firstName == null ? "blank" : this.firstName) + "'" 
						+ " and last_name equals '" + (this.lastName == null ? "blank" : this.lastName) + "'");
				
				//Functions.debug("this.phoneNo" + this.phoneNo);
				
				/*
				if(this.phoneNo != null)
				{		
					sb.append( " and ");
					sb.append( "(phone equals '"+ this.phoneNo + "'" 
							+ " or mobile_phone equals '" + this.phoneNo+ "'" 
							+ " or other_phone equals '" + this.phoneNo + "')");
				}
				*/
				
				sb.append(" and related_to_id equals '" + this.prospectId+"' and related_to_type equals 'LEAD'" );
			}
			else
			{
				return "";
			}
			
			//Functions.debug("SearchString = " + sb.toString());
			
			searchStr = sb.toString();
			 
		}
		//Functions.debug(searchStr);
		return searchStr;
	}
	
	public Result search(int type) throws Exception
	{
		//Functions.debug("Inside Search");
		String searchString = createSearchString(type);
		
		
		
		if(searchString.equals(""))
			return null ;
		
		String objectId = "";
		String fieldList = "";
		
		if(type == PROSPECT_SEARCH)
		{
			objectId = "LEAD";
			fieldList = "record_id,primary_contact_id";
		}
		else if(type == CONTACT_SEARCH)
		{
			objectId = "CONTACT";
			fieldList = "record_id,related_to_id,flag_primary_contact";
		}
		
		Result result = Functions.searchRecords(objectId, fieldList, searchString);
		
		return result;
	}
}