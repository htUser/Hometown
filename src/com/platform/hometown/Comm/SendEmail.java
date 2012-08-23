package com.platform.hometown.Comm;

import com.platform.api.*;

public class SendEmail {
	
	public void sendEmail(Parameters p) throws Exception{
		
		String debug_category = "Comm/sendEmail";
		
		//get the info from the params 
		String commId = p.get("id");
		String subject = p.get("subject");
		String contactEmail = p.get("contact_email");
		String body = p.get("main_message");
		String cc = new String();
		
		
		if((commId!=null)&&(commId.length()!=0)){
			String msg = "The comm id is" +commId; 
			//Logger.info(msg, debug_category); 
			//Logger.info(subject, debug_category);
			//Logger.info(contactEmail, debug_category);
			//Logger.info(body, debug_category);
			
		
			
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
			
			
		
			
			
			//create email method call
			String attachmentIdList = "";
			String attachmentTemplateIdList = "";
	
			
			Result sendEmailResult = Functions.sendEmail("Comms", commId, contactEmail, cc, subject, body, attachmentTemplateIdList, attachmentIdList);
			
			
			
		}
		
	}

}

