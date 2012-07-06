package com.platform.hometown.Client;

public class Client {
	String name;
	String clientId;
	
	
	
	public Client(String id){
		clientId = id;
		
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