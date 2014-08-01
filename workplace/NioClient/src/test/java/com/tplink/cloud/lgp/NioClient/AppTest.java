package com.tplink.cloud.lgp.NioClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

//	public void testMain() {
//		
//		String hostname = "localhost";
//		String requestDataType = "1111";
//		String requestData = "Actions speak louder than words!";
//		int port = 8888;
//		
//		for (int i = 0; i < 5000; i++) {
//			new NioClient(hostname, port).send(requestDataType, requestData);
//		}
//	}
}
