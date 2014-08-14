/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 13, 2014
 */

package com.tplink.cloud.lgp.NioServer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.tplink.cloud.lgp.NioClient.Client;
import com.tplink.cloud.lgp.validate.ReturnType;

public class TestClient {

    protected final static String HOSTNAME = "127.0.0.1";
    protected final static int PORT = 8888;
    protected static String header = "";
    protected static String message = "";
    protected static String result = "";
    protected Client clt;
    protected NioServer thread;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("BeforeClass, run once before all");
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("AfterClass, run once after all");
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
//        System.out.println("start the Server!");
        thread = new NioServer();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
//        System.out.println("stop the Server!");
        thread.stop();
    }

    @Ignore
    public void test() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testInputError(){
        thread.start(6666);
        clt = new Client(HOSTNAME, 6666);
        
        header = "";
        message = "Client Regist!";
        result = clt.send(header, message);
        // test empty input error
        assertEquals(ReturnType.INPUT_ERROR, result);
        header = "0a0011010";
        result = clt.send(header, message);
        // test input error(first 4 bytes must be digit)
        assertEquals(ReturnType.INPUT_ERROR, result);
        header = "000031010";
        result = clt.send(header, message);
        // test input error(operation code(5th byte) can just be 0,1,2)
        assertEquals(ReturnType.INPUT_ERROR, result);
        header = "000011210";
        result = clt.send(header, message);
        // test input error(when the operation code is 1, last 4 bytes must be 0 or 1)
        assertEquals(ReturnType.INPUT_ERROR, result);
    }
    
    @Test
    public void testRegister() {
        thread.start(7777);
        clt = new Client(HOSTNAME, 7777);

        header = "000011010";
        message = "Client Regist!";
        result = clt.send(header, message);
        // test first register
        assertEquals(ReturnType.SUCCESS_REGISTERED, result);
        
        header = "000011010";
        result = clt.send(header, message);
        // test duplicated register
        assertEquals(ReturnType.DUP_REGISTERED, result);
        
        thread.stop();
    }

    @Test
    public void testPublisher() {
        
        thread.start(8888);
        clt = new Client(HOSTNAME, 8888);
        
        header = "000011111";
        message = "register a observer";
        result = clt.send(header, message);
        // first register a observer named '0000' and subscibe all subjects(AA,BB,CC,DD)
        assertEquals(ReturnType.SUCCESS_REGISTERED, result);
        header = "001111100";
        result = clt.send(header, message);
        // then register a observer named '0011' and subscibe subjects(AA,BB)
        assertEquals(ReturnType.SUCCESS_REGISTERED, result);
        
        header = "11112AA";
        message = "Manager Publish Message!";
        result = clt.send(header, message);
        // publish a subject AA
        assertEquals(ReturnType.SUCCESS_PUBLISHED, result);
        header = "11112CC";
        result = clt.send(header, message);
        // publish a subject CC
        assertEquals(ReturnType.SUCCESS_PUBLISHED, result);
        header = "11112EE";
        result = clt.send(header, message);
        // publish a subject DD which does not exist
        assertEquals(ReturnType.SUBJECT_NOT_EXISTED, result);

        header = "00000XXXX";
        result = clt.send(header, message);
        // test observer '0000' pull message(expected AA and CC)
        assertEquals("[OBSERVER] AA:Manager Publish Message! CC:Manager Publish Message!", result);
        header = "00110XXXX";
        result = clt.send(header, message);
        // test observer '0011' pull message(expected just AA)
        assertEquals("[OBSERVER] AA:Manager Publish Message!", result);
    }

    @Test
    public void testPullMessage() {
        thread.start(9999);
        clt = new Client(HOSTNAME, 9999);
        
        header = "00000XXXX";
        message = "Client Pull Message";
        result = clt.send(header, message);
        // test not register 
        assertEquals(ReturnType.NOT_REGISTERED, result);
        
        header = "000011111";
        result = clt.send(header, message);
        // then register it
        assertEquals(ReturnType.SUCCESS_REGISTERED, result);
        header = "00000XXXX";
        result = clt.send(header, message);
        // then pull message from the server
        assertEquals(ReturnType.DEFAULT, result);
    }

}
