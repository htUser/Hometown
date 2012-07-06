package com.platform.hometown.processProspectNContact;

import com.platform.api.*;

import java.util.Map;
import java.util.HashMap;

import com.platform.hometown.ProcessThirdPartyRequest.DataMapper;

public class ProcessProspectContactImport
{
	public void process(Parameters p) throws Exception
	{
		PropsectContactDuplicateSearch dupSearch = new PropsectContactDuplicateSearch(p);
		
		Map<String,String> prospectResultMap = dupSearch.searchForDuplicates(PropsectContactDuplicateSearch.PROSPECT_SEARCH);
		
		String zipCode = p.get("zip");
			
		if(zipCode != null && zipCode.trim().length() > 0)
		{
			DataMapper dm = new DataMapper();
		
			String city = p.get("city");		
				
			String zipId = dm.processZipCode(zipCode ,city);
				
			Functions.debug("Imported Zip id = " + zipId);
			
			if(!zipId.equals(""))
				p.add("zip_code_1157081194",zipId);
		}
		
		if(prospectResultMap != null)
		{
			String prospectId = prospectResultMap.get(PropsectContactDuplicateSearch.PROSPECT_ID );
			
			Functions.debug("Imported Prospect Id = " + prospectId );
			p.add("id",prospectId);
			
		}
	}
}