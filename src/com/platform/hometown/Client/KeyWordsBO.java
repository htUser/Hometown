package com.platform.hometown.Client;

import java.util.ArrayList;
import java.util.HashMap;
import com.platform.api.*;
import com.platform.hometown.Client.*;

public class KeyWordsBO {
	String keyWordId = new String();
	String KeyWordName = new String();
	String campaignKeyWordsJunctionId = new String();
	String siteKeyWordsJunctionId = new String();
	public String getSiteKeyWordsJunctionId() {
		return siteKeyWordsJunctionId;
	}

	public void setSiteKeyWordsJunctionId(String siteKeyWordsJunctionId) {
		this.siteKeyWordsJunctionId = siteKeyWordsJunctionId;
	}
	ArrayList<Counties> countiesAvailable = new ArrayList<Counties>();
	
	
	public KeyWordsBO(String keyWordId, String keyWordName, String newCampaignKeyWordsJunctionId, String newSiteKeyWordsJunctionId) {
		super();
		this.keyWordId = keyWordId;
		this.KeyWordName = keyWordName;
		this.campaignKeyWordsJunctionId = newCampaignKeyWordsJunctionId;
		this.siteKeyWordsJunctionId = newSiteKeyWordsJunctionId;
	}
	
	public String getKeyWordId() {
		return keyWordId;
	}
	public void setKeyWordId(String keyWordId) {
		this.keyWordId = keyWordId;
	}
	public String getKeyWordName() {
		return KeyWordName;
	}
	public void setKeyWordName(String keyWordName) {
		KeyWordName = keyWordName;
	}
	public ArrayList<Counties> getCountiesAvailable() {
		return countiesAvailable;
	}
	public void setCountiesAvailable(ArrayList<Counties> countiesAvailable) {
		this.countiesAvailable = countiesAvailable;
	}
	
	public String getCampaignKeyWordsJunctionId() {
		return campaignKeyWordsJunctionId;
	}
	public void setCampaignKeyWordsJunctionId(String campaignKeyWordsJunctionId) {
		this.campaignKeyWordsJunctionId = campaignKeyWordsJunctionId;
	}
	
	

}