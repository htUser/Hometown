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
		String subject = "HometownRep: You Have a New Review";
		Client c;
				
		String pdId = p.get("id");
		String clientId = p.get("client2");
		
		if((pdId!=null)&&(pdId.length()!=0)){
			String msg = "The pd id is" +pdId; 
			Logger.debug(msg, debug_category);
		
			//check for null client id
			if((clientId!=null)&&(clientId.length()!=0)){
							
			//lookup client primary contact email and the cc_reviews_contact email from client record
				c = new Client(clientId);
				c = lookupClientInfo(clientId);
			
			
				bodyTemplateID = "5b96fc7c7fec4b01b1f5eb5efd891715";
			
			
				//send email
				Result sendEmailResult = Functions.sendEmailUsingTemplate("Project_Costs", pdId, c.getEmail(), c.getCcReviewsContact(), subject, bodyTemplateID, attachmentTemplateIdList, attachmentIdList);
				
				
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
			c.setEmail((String)p.get("email"));
			Functions.debug("email "+c.getEmail());
			
			//lookup the cc contact's email
			String ccId = (String)p.get("cc_reviews_contact");
			Functions.debug("ccReviews to contact id "+ccId);
			
			if((ccId!=null)&&(ccId.length()>0)){
			
				Result ccResult = Functions.getRecord("Contacts_HT", "email", ccId);
				Functions.debug("after getRecord, code is "+ccResult.getCode());
			
				if(ccResult.getCode()>0){
					Parameters ccP = ccResult.getParameters();
					c.setCcReviewsContact((String)ccP.get("email"));
				} else {
					Logger.info("No contact record found for cc: contact id " + ccId, debug_category);
					Logger.error("No contact record found for cc: contact id " + ccId, debug_category);
					throw new Exception();
				
				}
			}
				
			
			
		} else {
			Logger.info("No client record found: client id " + clientId, debug_category);
			Logger.error("No client record found: client id " + clientId, debug_category);
			throw new Exception();
			
		}
			
		
		
		return c;
	}

}