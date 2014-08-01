package com.tplink.cloud.lgp.Observer;

public abstract class Observer {

	private String ObserverName;
	
	private String interest;
	
	public Observer(String name){
		
		this.ObserverName = name;
		this.interest = "";
	}
	
	public void setInterset(String info){
		this.interest += info;
	}
	
	public String getInterest(){
		String temp = this.interest;
		this.interest = "";
		return temp;
	}
	
	public String getObserverName(){
		return this.ObserverName;
	}
	
	public abstract void subscribe(Subject sub);
	public abstract void cancell(Subject sub);
	
	public void update(String mess){
		this.interest += mess + "\n";
	}
}
