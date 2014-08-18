/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.testjunit4.lgp.TestJunit4;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHttpGet {
   
    private HttpServerMock _mock;
    private HttpGet _httpGet = new HttpGet();
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */

    @Before
    public void setUp() throws Exception {
        _mock = new HttpServerMock();
    }

    @After
    public void tearDown() throws Exception {
        if (null != _mock) {
            _mock.stop();
        }
    }

    /**
     * 用例：响应状态码为200且有响应内容。
     */
    @Test
    public void testGetSomeThing4Success() throws Exception {
        String response = "Hello, The World!";
        _mock.start(response, "text/plain");

        String content = _httpGet.getSomeThing("http://localhost:9191/hello");
        assertEquals(response, content);
    }

    /**
     * 用例：响应状态码为非200。
     */
    @Test
    public void testGetSomeThing4Fail() throws Exception {
        String response = "Hello, The World!";
        _mock.start(response, "text/plain", 500);

        String content = _httpGet.getSomeThing("http://localhost:9191/hello");
        assertNull(content);
    }

}
