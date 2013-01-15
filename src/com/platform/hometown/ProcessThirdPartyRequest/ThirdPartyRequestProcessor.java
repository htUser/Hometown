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
		
		Functions.debug("Form type is: "+formType);
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
					
					if(formType.equals(DataMapper.E_LEAD_POST)){
							Result result = addRecordToLJ(objectId, ljParams);
							
							if(result.getCode() < 0)
							{
								Functions.debug("Unable to add Record"+"\n"+requestData);
								sendErrorEmail("E Lead Post");
							}
					} else if (formType.equals(DataMapper.P_LEAD_POST)){
						Result result = addRecordToLJ(objectId, ljParams);
						
						if(result.getCode() < 0)
						{
							Functions.debug("Unable to add Record"+"\n"+requestData);
							sendErrorEmail("P Lead Post");
						}
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
						Functions.debug("Quality Rating Debug = " + ljParams.toString());
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
						//throw new Exception("Unable to add Record");
						Functions.debug("Unable to add Record"+"\n"+requestData);
						sendErrorEmail("Client Review Post");
						
					}
				
				} else if(formType.equals(DataMapper.REVIEW_RESPONSE))
				{
					//Logger.info("In the processRequest() for Client-Review-Response", "NewThirdPartyRequestProcessor");
					//Functions.debug("In the else of processRequest()");
					String objectId = getObjectId(this.formType);
					
					DataMapper dm = new DataMapper(this.formType, requestData);
					Parameters ljParams = dm.prepareLJParams();
					ljParams.add("formType",formType);
					
					
					//update record in LJ, not add a new record
					
					Result result = updateRecordToLJ(objectId, ljParams);
					if(result.getCode() < 0)
					{
						Functions.debug("Unable to update Record"+"\n"+requestData);
						sendErrorEmail("Client Review Response");
						
					}
					
				
				} else if(formType.equals(DataMapper.CLIENT_SURVEY_POST))
				{
					Functions.debug("Client Survey Post In the else of processRequest()");
					String objectId = getObjectId(this.formType);
					
					DataMapper dm = new DataMapper(this.formType, requestData);
					Parameters ljParams = dm.prepareLJParams();
					ljParams.add("formType",formType);
					
					Result result = addRecordToLJ(objectId, ljParams);
					if(result.getCode() < 0)
					{
						//throw new Exception("Unable to add Record");
						Functions.debug("Unable to add Record"+"\n"+requestData);
						sendErrorEmail("Client Survey Post");
						
					} else {
						Functions.debug("Survey Record Added");
					}
					
					
					
					
					
				}
				
			}	
			
		}catch(Exception e)
			{
				Functions.debug(e+"\n"+requestData);
			} 
			/*try
			{
				Result sendEmailResult = Functions.sendEmail("b4a004b7feba4f60b51428aa64703d61", "", "maustin@hometown.net,kpratt@hometown.net", 
    										"", "Error Processing Client Lead - Class: ThirdPartyRequestProcessor", 
    										e+"\n\n"+requestData,
    										"", "");
    			}
    			catch(Exception sendEmailException)
    			{
    				Functions.debug("Error in sending email \n" + sendEmailException);
    			}*/
	}
	
	
	public void sendErrorEmail(String formType){
		
		Functions.debug("\n"+requestData);
	
		try
		{
			Result sendEmailResult = Functions.sendEmail("b4a004b7feba4f60b51428aa64703d61", "", "maustin@hometown.net,kpratt@hometown.net", 
									"", "Error Processing "+formType, 
									"\n\n"+"Have a nice day.",
									"", "");
		}
		catch(Exception sendEmailException)
		{
			Functions.debug("Error in sending email \n" + sendEmailException);
		}
		
	}
	
	//Wrapper over LJ addRecord
	public Result addRecordToLJ(String objectId, Parameters p) throws Exception
	{
		Result result = Functions.addRecord(objectId, p);
        
		return result;
	}
	
	//Wrapper over LJ updateRecord
	public Result updateRecordToLJ(String objectId, Parameters p) throws Exception
	{
		//turn off any data policies that would fire on an update
		p.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
		String recordID = p.get("id");
		
		//Functions.debug("object id is "+ recordID);
		//p.remove("id");
		
		Result result = Functions.updateRecord(objectId, recordID, p);
        
		return result;
	}
	
	//Retrieves object Id to add record to, from form type query param	
	public static String getObjectId(String formType) throws Exception
	{
		if(formType.equals(DataMapper.E_LEAD_POST) || formType.equals(DataMapper.P_LEAD_POST))
		{
			return "Client_Leads";	
		
		} else if (formType.equals(DataMapper.REVIEW_POST) || formType.equals(DataMapper.REVIEW_RESPONSE))
		{
			return "Project_Costs";
		} else if (formType.equals(DataMapper.CLIENT_SURVEY_POST))
		{
			return "Surveys";
		}
		else
		{
			throw new Exception("Invalid object");
		}
	}
}