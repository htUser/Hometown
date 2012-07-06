package com.platform.hometown.ProcessThirdPartyRequest;

import com.platform.api.*;

public class EmailClientLeadModify
{
	public void modifyClientLead(Parameters p)
	{
		try
		{
			p.add("__Tracker_Name__","");
			SearchClientLeadsForDuplicates s = new SearchClientLeadsForDuplicates();
			Integer result = s.search(p);
			
			//Functions.debug("result = " + result);
			
			if(result != null)
			{
				if(result > 0)
				{
					p.add("validate","NO CHARGE");
					p.add("reject_reason","Duplicate");
				}
				else if(result == 0)
				{
					p.add("validate","PENDING VALIDATION");
				}
			}
		}
		catch(Exception e)
		{
			Functions.debug(e+"\n"+p);
			try
			{
				Result sendEmailResult = Functions.sendEmail("b4a004b7feba4f60b51428aa64703d61", "", "maustin@hometown.net,kpratt@hometown.net", 
    										"", "Error Processing Client Lead - Class: EmailClientLeadModify", 
    										e+"\n\n"+p,
    										"", "");
    			}
    			catch(Exception sendEmailException)
    			{
    				Functions.debug("Error in sending email \n" + sendEmailException);
    			}
			
		}
	}
}