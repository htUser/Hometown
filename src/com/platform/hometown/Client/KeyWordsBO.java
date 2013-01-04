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
	Boolean selected = false;
	
	ListingBO listing;
	
	
	public KeyWordsBO(){
		super();
	}
	
	public KeyWordsBO(String keyWordId, String keyWordName, String newCampaignKeyWordsJunctionId, String newSiteKeyWordsJunctionId) {
		super();
		this.keyWordId = keyWordId;
		this.KeyWordName = keyWordName;
		this.campaignKeyWordsJunctionId = newCampaignKeyWordsJunctionId;
		this.siteKeyWordsJunctionId = newSiteKeyWordsJunctionId;
	}
	
	public ListingBO getListing() {
		return listing;
	}

	public void setListing(ListingBO listing) {
		this.listing = listing;
	}

	
	public String getSiteKeyWordsJunctionId() {
		return siteKeyWordsJunctionId;
	}

	public void setSiteKeyWordsJunctionId(String siteKeyWordsJunctionId) {
		this.siteKeyWordsJunctionId = siteKeyWordsJunctionId;
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
	
	public String getCampaignKeyWordsJunctionId() {
		return campaignKeyWordsJunctionId;
	}
	public void setCampaignKeyWordsJunctionId(String campaignKeyWordsJunctionId) {
		this.campaignKeyWordsJunctionId = campaignKeyWordsJunctionId;
	}
	
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	

}