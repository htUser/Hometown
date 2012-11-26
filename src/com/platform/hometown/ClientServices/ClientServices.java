package com.platform.hometown.ClientServices;
import com.platform.api.*;
import com.platform.hometown.Client.Client;

public class ClientServices {
	public void updateClientServices(Parameters p) throws Exception{
		
		String debug_category = "updateClientServices";
		Logger.info("In updateClientServices", debug_category);
		Functions.debug("In updateClientServices");
					
		String csId = p.get("id");
		String clientId = p.get("client2");
		String serviceType = p.get("service_type");
		String status = p.get("campaign_status");
		
		Functions.debug("service type "+serviceType);
		Functions.debug("status "+status);
		
		
		if((csId!=null)&&(csId.length()!=0)){
			String msg = "The cs id is" +csId; 
			Logger.debug(msg, debug_category);
		
			//check for null client id
			if((clientId!=null)&&(clientId.length()!=0)){
				String serviceTypeText = new String();
				Boolean inCurrentServices = false;
				Boolean inOppServices = false;
				
				
				//Get service type text
				Result stResult = Functions.getRecord("Service_Type", "type", serviceType);
				if(stResult.getCode()>0){
					Parameters stParams = stResult.getParameters();
					serviceTypeText = stParams.get("type");
					Functions.debug("service type text "+serviceTypeText);
					
				}	
				
				
				
				Result result = Functions.getRecord("Clients", "current_services, opp_services", clientId);
				if(result.getCode()>0){
					Parameters params = result.getParameters();
					String cServices = (String)params.get("current_services");
					String oServices = (String)params.get("opp_services");
					
					Functions.debug("current services on client "+cServices);
					Functions.debug("opp services on client "+oServices);
					
					//check if the new service type is in current services and opp services
					if(cServices.contains(serviceTypeText)){
						inCurrentServices = true;
					}
					
					if(oServices.contains(serviceTypeText)){
						inOppServices = true;
					}
					
					
					Functions.debug("inCurrentServices is "+inCurrentServices);
					Functions.debug("inOppServices is "+inOppServices);
					
					//Needs to be in current services
					if(status.equals("Yes")){
						if (!inCurrentServices){
							//add to current services
							if(cServices.length()==0)
								cServices = serviceTypeText;
							else
								cServices = cServices + ", " +serviceTypeText;
						}
						
						Functions.debug("1 cServices now is: "+cServices);
						
						if(inOppServices){
							//remove from opp services
							//3 cases to check for removal of coma too
							if(oServices.contains(serviceTypeText+",")){
								oServices = oServices.replace(serviceTypeText+",", "");
								
							} else if(oServices.contains(", "+serviceTypeText)){
								oServices = oServices.replace(", "+serviceTypeText, "");
								
							}else {
								oServices = oServices.replace(serviceTypeText, "");
							}
							
																
						}
						
						Functions.debug("1 oServices now is: "+oServices);
						
						
					} else {
						if(inCurrentServices){
							//remove it from current services
							//3 cases to check for removal of coma too
							if(cServices.contains(serviceTypeText+",")){
								cServices = cServices.replace(serviceTypeText+",", "");
								
							} else if(cServices.contains(", "+serviceTypeText)){
								cServices = cServices.replace(", "+serviceTypeText, "");
								
							}else {
								cServices = cServices.replace(serviceTypeText, "");
							}
							
						}
						
						Functions.debug("2 cServices now is: "+cServices);
						
					
						if(!inOppServices){
							//add it to opp services
							if(oServices.length()==0)
								oServices = serviceTypeText;
							else
								oServices = oServices + ", " +serviceTypeText;
						}
						
						Functions.debug("2 oServices now is: "+oServices);
					}
					
					
					
					
					//update the client record with correct cServices and oServices
					Parameters updateParams = new Parameters();
					updateParams.add("current_services", cServices);
					updateParams.add("opp_services", oServices);
					updateParams.add(PLATFORM.PARAMS.RECORD.DO_NOT_EXEC_DATA_POLICY,"1");
					updateParams.add(PLATFORM.PARAMS.RECORD.ENABLE_MULTIPART,"1");
									
					Result updateResult = Functions.updateRecord("Clients", clientId, updateParams);  
				
					if(updateResult.getCode()<0){
						String msg1 = "There was an error updating the client record with the new/changes client services record. Client Services Id is "+ csId;
						Logger.info(msg1 + ":\n" + result.getMessage(), debug_category); 
						Functions.throwError(msg1 + "."); 				
					}
				
				
				} else {
					Logger.info("No client record found: client id " + clientId, debug_category);
					Logger.error("No client record found: client id " + clientId, debug_category);
					throw new Exception();
					
				}
				
			
			} else {
				Logger.error("Error no client id. ", debug_category);
				throw new Exception();
			}
		
		}		
	
	}
		
}
