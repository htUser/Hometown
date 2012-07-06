package com.platform.hometown.assignToDo;

import com.platform.api.*;

public class assignToDo {
	
	public void assignToDo(Parameters p) throws Exception{
		
		String assignedTo = p.get("todo_owner");
		String owner = p.get("owner_id");
		Boolean sendEmail = p.getBoolean("send_email");
		
		//Functions.debug("AssignedTo is "+assignedTo+", owner is "+owner);
		//Functions.debug("Send email is "+sendEmail);
		
		
		if(assignedTo!=null){
			
			owner=assignedTo;
			Functions.changeOwnerShipInfo(p.get("object_id"), p.get("id"), owner, sendEmail);
		}
		
	}

}