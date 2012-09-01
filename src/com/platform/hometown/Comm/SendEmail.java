package com.platform.hometown.Comm;

import com.platform.api.*;

public class SendEmail {
	
	public void sendPropCommEmail(Parameters p) throws Exception{
		String debug_category = "sendPropCommEmail";
		String bodyTemplateID = new String();
		Logger.info("In sendPropCommEmail", debug_category); 
		
		//get the info from the params 
		String propCommId = p.get("id");
		String subject = p.get("subject");
		String propId = p.get("prop");
		Logger.info("prop is "+ propId, debug_category);
		String docId = p.get("document");
		String cc = p.get("copy_to");
		String emailType = p.get("email_type");
		Logger.info("email type is "+emailType, debug_category);
		
		
		if((propCommId!=null)&&(propCommId.length()!=0)){
			String msg = "The comm id is" +propCommId; 
			Logger.info(msg, debug_category); 
				
			//configure cc
			if(cc.length()==0){
				cc = "notify@hometown.net";
			}else {
				cc = cc + ",  notify@hometown.net";
				
			}
			Logger.info(cc, debug_category);
			
			//choose template based on email type
			if(emailType.equalsIgnoreCase("Template")){
				bodyTemplateID = "462893e417f1438f97542b7016b4b24a";
			}else if (emailType.equalsIgnoreCase("Full Lead")){
				bodyTemplateID = "3cb956738df64afb8d38d8cee9840ae5";
			} else if(emailType.equalsIgnoreCase("Partial Lead")){
				//no template id yet - will change later
				bodyTemplateID = "462893e417f1438f97542b7016b4b24a";
			} else {
				//default to base template - double check this!!
				bodyTemplateID = "462893e417f1438f97542b7016b4b24a";
			}
			
			//create email method call
			String attachmentIdList = "";
			String attachmentTemplateIdList = docId;
			
			String propEmail = getPropEmail(propId);
			
			Result sendEmailResult = Functions.sendEmailUsingTemplate("P_Comm", propCommId, propEmail, cc, subject, bodyTemplateID, attachmentTemplateIdList, attachmentIdList);
			
			if(sendEmailResult.getCode()<0){
				//an error occurred
				Logger.error("Error sending email: " +sendEmailResult.getMessage(), debug_category);
				throw new Exception();
			}
			
		} else {
			Logger.error("No propCommId found.", debug_category);
		}
		
	}
	
	
String getPropEmail(String propId) throws Exception{
	String debug_category = "sendPropCommEmail";	
	String propEmail = new String();
		
		//search the table Props for the record
		Result result = Functions.searchRecords("Prop", "id, email","id equals '" + propId + "'");
		if(result.getCode()<0){
			
			 Functions.throwError("Error finding prop record."); 

		}else if(result.getCode()==0){
			//no records found 
			Functions.throwError("Error no prop record found.");
		}else {
			//Success
			
			ParametersIterator iterator = result.getIterator();
			while(iterator.hasNext())
			  {
			    Parameters params = iterator.next();
			    propEmail = params.get("email");
			    Logger.info(propEmail, debug_category);
			 	
			  }
				
			}
		return propEmail;

}
	
	
	public void sendEmail(Parameters p) throws Exception{
		String debug_category = "Comms/sendEmail";
		Logger.info("In sendEmail", debug_category); 
		
		//get the info from the params 
		String commId = p.get("id");
		String subject = p.get("subject");
		String contactEmail = p.get("contact_email");
		String body = p.get("main_message");
		String docId = p.get("document");
		String cc = p.get("blind_copy_to");
		
		
		if((commId!=null)&&(commId.length()!=0)){
			String msg = "The comm id is" +commId; 
			Logger.info(msg, debug_category); 
			//Logger.info(subject, debug_category);
			//Logger.info(contactEmail, debug_category);
			//Logger.info(body, debug_category);
			
			
			cc = generateCC(commId, cc);
			
			if(cc.length()==0){
				cc = "support@hometown.net";
			}else {
				cc = cc + ", support@hometown.net";
				
			}
			Logger.info(cc, debug_category);
			
			//create email method call
			String attachmentIdList = "";
			String attachmentTemplateIdList = docId;
			String bodyTemplateID = "6526b0cfbe9843cb90a3934dbdb9f662";
	
			
			Result sendEmailResult = Functions.sendEmailUsingTemplate("Comms", commId, contactEmail, cc, subject, bodyTemplateID, attachmentTemplateIdList, attachmentIdList);
			
			if(sendEmailResult.getCode()<0){
				//an error occurred
				Logger.error("Error sending email: " +sendEmailResult.getMessage(), debug_category);
				throw new Exception();
			}
			
		} else {
			Logger.error("No commId found.", debug_category);
		}
			
	}
		
	
	
	String generateCC(String commId, String cc) throws Exception{
		
		//search junction table of comm-contacts for all records that have this commId
		Result result = Functions.searchRecords("Comm_Contacts", "id, comm, contact","comm equals '" + commId + "'");
		if(result.getCode()<0){
			
			 Functions.throwError("Error finding comm_contacts."); 

		}else if(result.getCode()==0){
			//no records found - that is okay, there could be none selected
			
		}else {
			//Success
			
			ParametersIterator iterator = result.getIterator();
			while(iterator.hasNext())
			  {
			    Parameters params = iterator.next();
			    String contactId = params.get("contact");
			    //Logger.info(contactId, debug_category);
			    
			    //now search for that contact and get the email address
			    String sql = "SELECT id, email FROM Contacts where id = '"+ contactId +"'";
			    Result contactResult = Functions.execSQL(sql);
			    //Result contactResult = Functions.getRecord("Contacts", "id, email","id equals '" + contactId + "'");
			    if(contactResult.getCode()<0){
					
					 Functions.throwError("Error getting contact with id "+contactId+"."); 
					

				}else if(contactResult.getCode()==0){
					//no records found
					Functions.throwError("Error no contact found with id "+contactId+"."); 
					
					
				}else {
					//Success
					 ParametersIterator it = contactResult.getIterator();
				     Parameters contactParams = it.next();       
				     String email = contactParams.get("email");
				     	
					
					
					if(cc.length()==0){
						cc = email;
					} else {
						cc = cc + ", " + email;
					}
					
				}
				
			  }
				
			}
		return cc;

}
}

