package com.platform.hometown.Client;

public class State {
	String name;
	String abrv;
	String stateId;
	
	public State(String id){
		stateId = id;
	}
	
	public String getId(){
		
		return stateId;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
		
	}
	
	public void setAbrv(String a){
		
		abrv = a;
	}
	
	public String getAbrv(){
		return abrv;
	}
	
	public void fillState() throws Exception{
		if(stateId!=null){
			//make db call
			
			
		}else {
			throw new Exception("The stateid is null");
		}
		
	}

}