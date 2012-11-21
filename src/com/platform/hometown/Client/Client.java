package com.platform.hometown.Client;

public class Client {
	String name;
	String clientId;
	String email;
	String ccReviewsContact;
	
	public Client(String id){
		clientId = id;
		
	}
	
	public Client(){
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCcReviewsContact() {
		return ccReviewsContact;
	}

	public void setCcReviewsContact(String ccReviewsContact) {
		this.ccReviewsContact = ccReviewsContact;
	}

	
	
	public void setName(String name) {
		this.name = name;
	}

	public void location(String newName){
		name = newName;
		
	}
	
	//returns null if name has not been set
	public String getName(){
		return name;
	}
	
	public String getId(){
		return clientId;
	}

}