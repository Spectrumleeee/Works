/**
 * ConcreteObserver entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.Observer;

public class ConcreteObserver extends Observer {

    private boolean observerStatus;

    public ConcreteObserver(String name) {
        super(name);
        // TODO Auto-generated constructor stub
        this.observerStatus = false;
    }

    public boolean getStatus() {
        return this.observerStatus;
    }

    @Override
    public void subscribe(Subject sub) {
        // TODO Auto-generated method stub
        sub.attach(this);
    }

    @Override
    public void cancell(Subject sub) {
        // TODO Auto-generated method stub
        sub.detach(this);
    }
}
