package com.tplink.cloud.lgp.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

	private String name;
	private List<Observer> observers;
	
	public void SayHello(){
		System.out.println("Hello from Observer : " + name);
	}
	
	public Subject(String name){
		this.name = name;
		observers = new ArrayList<Observer>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Observer> getObservers(){
		return observers;
	}
	
    /// <summary>
    /// 增加观察者
    /// </summary>
    /// <param name="observer"></param>
    public void attach(Observer observer)
    {
        observers.add(observer);
    }

    /// <summary>
    /// 移除观察者
    /// </summary>
    /// <param name="observer"></param>
    public void detach(Observer observer)
    {
        observers.remove(observer);
    }
    
    public void notify(String mess){
    	
    	for(Observer ob : observers){
    		ob.update(name + ":" + mess);
    	}
    }
}
