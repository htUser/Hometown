//This class handles, adding of record to longjump from external source

package com.platform.hometown.ProcessThirdPartyRequest;

import java.util.Map;
import java.util.HashMap;

import com.platform.api.*;

import com.platform.hometown.processProspectNContact.ProspectContactMergeProcessor;

public class ThirdPartyRequestProcessor
{
	private static final String FORM_TYPE = "form_type";

	private String formType;
	
	private Map<String,String[]> requestData = new HashMap<String,String[]>();
	
	//Constructor
	public ThirdPartyRequestProcessor(String formType, Map<String,String[]> requestData) throws Exception
	{
		
		
		this.formType = formType;
		this.requestData = requestData;
		if(formType == null || formType.trim().equals(""))
		{
			throw new Exception("Invalid form type");
		}
	}
	
	//Main processor method
	public void processRequest()
	{
		try
		{
			if(formType != null)
			{
				if(formType.equals(DataMapper.E_LEAD_POST) || formType.equals(DataMapper.P_LEAD_POST))
				{
					String objectId = getObjectId(this.formType);
					
					DataMapper dm = new DataMapper(this.formType, requestData);
					Parameters ljParams = dm.prepareLJParams();
					ljParams.add("formType",formType);
					
					//Functions.debug("before sub dt = " + ljParams.get("date_time"));
					//if(formType.equals(DataMapper.E_LEAD_POST))
					//{
					//	Functions.debug(ljParams);
					//}
					
					Result result = addRecordToLJ(objectId, ljParams);
					if(result.getCode() < 0)
					{
						throw new Exception("Unable to add Record");
					}
				}
				else if(formType.equals(DataMapper.E_PROSPECT_POST ))
				{
					//Functions.debug("Wufoo ProsCont Map " + requestData);
					DataMapper dm = new DataMapper(this.formType, requestData);
					Parameters ljParams = dm.prepareLJParams();
					
					//Functions.debug("LJParams = " + ljParams);
					
					ProspectContactMergeProcessor processor = new ProspectContactMergeProcessor();
					Map m = processor.process(ljParams);			
					//Functions.debug(m);
					
					String status = (String)m.get("status");
					
					Result result = null;
					
					if(status != null )
					{
						if(status.equals("DOESNOTEXIST"))
						{
							result = addRecordToLJ("LEAD", ljParams);
						}
					}
					
					if(result !=null && result.getCode()< 0)
					{
						throw new Exception("Unable to process Prospect");
					}
					
				}
				else if(formType.equals(DataMapper.REVIEW_POST))
				{
					//Logger.info("In the processRequest() for Review-post", "NewThirdPartyRequestProcessor");
					String objectId = getObjectId(this.formType);
					
					DataMapper dm = new DataMapper(this.formType, requestData);
					Parameters ljParams = dm.prepareLJParams();
					ljParams.add("formType",formType);
					
					Result result = addRecordToLJ(objectId, ljParams);
					if(result.getCode() < 0)
					{
						throw new Exception("Unable to add Record");
					}
				
				}
				
				
			}
		}
		catch(Exception e)
		{
			Functions.debug(e+"\n"+requestData);
			try
			{
				Result sendEmailResult = Functions.sendEmail("b4a004b7feba4f60b51428aa64703d61", "", "maustin@hometown.net,kpratt@hometown.net", 
    										"", "Error Processing Client Lead - Class: ThirdPartyRequestProcessor", 
    										e+"\n\n"+requestData,
    										"", "");
    			}
    			catch(Exception sendEmailException)
    			{
    				Functions.debug("Error in sending email \n" + sendEmailException);
    			}
		}
	}
	
	//Wrapper over LJ addRecord
	public Result addRecordToLJ(String objectId, Parameters p) throws Exception
	{
		Result result = Functions.addRecord(objectId, p);
		return result;
	}
	
	//Retrieves object Id to add record to, from form type query param	
	public static String getObjectId(String formType) throws Exception
	{
		if(formType.equals(DataMapper.E_LEAD_POST) || formType.equals(DataMapper.P_LEAD_POST))
		{
			return "Client_Leads";	
		
		} else if (formType.equals(DataMapper.REVIEW_POST))
		{
			return "Project_Costs";
		}
		else
		{
			throw new Exception("Invalid object");
		}
	}
}