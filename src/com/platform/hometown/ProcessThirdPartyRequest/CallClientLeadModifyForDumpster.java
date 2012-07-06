package com.platform.hometown.ProcessThirdPartyRequest;

import com.platform.api.*;

public class CallClientLeadModifyForDumpster 
{
	public void modifyClientForDumpster(Parameters p)
	{
		try
		{
			p.add("__Tracker_Name__","DUMPSTER");
		
			SearchClientLeadsForDuplicates s = new SearchClientLeadsForDuplicates();
			Integer result = s.search(p);
			
			if(result != null)
			{
				if(result > 0)
				{
					p.add("validate","NO CHARGE");
					p.add("reject_reason","Duplicate");
				}
				else if(result == 0)
				{
					p.add("validate","VALID LEAD");
				}
				else if(result == -2)
				{
					p.add("validate","NO CHARGE");
					p.add("reject_reason","Call Duration");
				}
			}
			else
			{
				p.add("validate","NO CHARGE");
				p.add("reject_reason","Lead Quality");
			}
		}
		catch(Exception e)
		{
			Functions.debug(e+"\n"+p);
			try
			{
				Result sendEmailResult = Functions.sendEmail("b4a004b7feba4f60b51428aa64703d61", "", "maustin@hometown.net,kpratt@hometown.net", 
    										"", "Error Processing Client Lead - Class: CallClientLeadModifyForDumpster", 
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