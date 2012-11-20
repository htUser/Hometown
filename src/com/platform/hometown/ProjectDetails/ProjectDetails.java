package com.platform.hometown.ProjectDetails;

import com.platform.api.*;
import com.platform.hometown.Client.Client;
import com.platform.hometown.Client.ClientLocation;


public class ProjectDetails {
	public void sendReviewResponseEmail(Parameters p) throws Exception{
		
		String debug_category = "sendPropCommProjectDetailsEmail";
		Logger.info("In sendReviewResponseEmail", debug_category);
		
		
		String bodyTemplateID = new String();
		String attachmentTemplateIdList = "";
		String attachmentIdList = "";
		Client c;
				
		String pdId = p.get("id");
		String clientId = p.get("client2");
		
		if((pdId!=null)&&(pdId.length()!=0)){
			String msg = "The pd id is" +pdId; 
		
			//check for null client id
			if((clientId!=null)&&(clientId.length()!=0)){
							
			//lookup client primary contact email and the cc_reviews_contact email from client record
				c = new Client(clientId);
				c = lookupClientInfo(clientId);
			
			
				bodyTemplateID = "7b92889b3f6443849b4de82d4bfc30e0";
			
			
				//send email
				Result sendEmailResult = Functions.sendEmailUsingTemplate("Project_cost", pdId, c.getEmail(), c.getCcReviewsContact(), null, bodyTemplateID, attachmentTemplateIdList, attachmentIdList);
				
				
				if(sendEmailResult.getCode()<0){
					//an error occurred
					Logger.error("Error sending email: " +sendEmailResult.getMessage(), debug_category);
					throw new Exception();
				}
			} else {
				Logger.error("Error no client id. ", debug_category);
				throw new Exception();
			}
	
		
		} else {
			Logger.info("No project details Id found.", debug_category);
			Logger.error("No project details Id found.", debug_category);
		}
		
		
		
	}
	
	Client lookupClientInfo(String clientId) throws Exception{
		String debug_category = "sendPropCommProjectDetailsEmail:lookupClientInfo";
		
		Client c = new Client(clientId);
		

		Result result = Functions.getRecord("Clients", "email, cc_reviews_contact", clientId);
		Functions.debug("after getRecord, code is "+result.getCode());
		
		if(result.getCode()>0){
			Parameters p = result.getParameters();
			
			c.setCcReviewsContact((String)p.get("cc_reviews_contact"));
			c.setEmail((String)p.get("email"));
		} else {
			Logger.info("No client record found: client id " + clientId, debug_category);
			Logger.error("No client record found: client id " + clientId, debug_category);
			throw new Exception();
			
		}
			
		
		
		return c;
	}

}
