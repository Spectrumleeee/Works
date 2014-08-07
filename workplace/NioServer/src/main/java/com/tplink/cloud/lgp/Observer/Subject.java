/**
 * Subject entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private String name;
    private List<Observer> observers;

    public void SayHello() {
        System.out.println("Hello from Observer : " + name);
    }

    public Subject(String name) {
        this.name = name;
        observers = new ArrayList<Observer>();
    }

    public String getName() {
        return this.name;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notify(String mess) {
        for (Observer ob : observers) {
            ob.update(name + ":" + mess);
        }
    }
}
