/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.client;

import java.util.HashMap;
import java.util.Map;

public class TestStatic {

	
	private static Map<String, Object> mp = new HashMap<String, Object>();
	
	public Map getMap(){
		return mp;
	}
	
	public void change(){
		System.out.println(mp.size());
		mp.put("1", new Object());
		System.out.println(mp.size());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
