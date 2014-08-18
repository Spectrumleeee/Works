/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.testjunit4.lgp.TestJunit4;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

public class HttpServerMock {
    public final static int DEFAULT_PORT = 9191;
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static int DEFAULT_STATUS_CODE = 200;

    private Server _httpServer;
    private int _port;

    public HttpServerMock() {
        _port = DEFAULT_PORT;
    }

    public HttpServerMock(int port) {
        _port = port;
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"，content type为"application/json"。
     * @param content 响应内容
     */
    public void start(String content) throws Exception {
        start(content, DEFAULT_CONTENT_TYPE, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"。
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     */
    public void start(String content, String contentType) throws Exception {
        start(content, contentType, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器。
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     * @param statuCode 响应状态码
     */
    public void start(String content, String contentType, 
            int statuCode) throws Exception {
         
        _httpServer = new Server(_port);
        _httpServer.setHandler(createHandler(content, contentType, statuCode));
        _httpServer.start();
        
    }

    /**
     * 停止Jetty服务器。
     */
    public void stop() throws Exception {
        if (null != _httpServer) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    private Handler createHandler(final String content, final String contentType, 
            final int statusCode) {
        return new AbstractHandler() {

            @Override
            public void handle(String target, HttpServletRequest request,
                    HttpServletResponse response, int dispatch)
                    throws IOException, ServletException {
                // TODO Auto-generated method stub
                
                System.out.println("Hello");
                response.setContentType(contentType);
                response.setStatus(statusCode);
                response.getWriter().print(content);
                System.out.println(content);
            }
        };
    }
}
