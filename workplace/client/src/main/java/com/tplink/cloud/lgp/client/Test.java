/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestStatic ts = new TestStatic();
		ts.change();
		TestStatic ts1 = new TestStatic();
		ts1.change();
		
		ExecutorService readPool = Executors.newFixedThreadPool(4);
		readPool.execute(new abc());
		readPool.execute(new abc());
	}

}

class abc implements Runnable{

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private TestStatic ts;
	
	public abc(){
		ts = new TestStatic();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		ts.change();
	}
	
}
