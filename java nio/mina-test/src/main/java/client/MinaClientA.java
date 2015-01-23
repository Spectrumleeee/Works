/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package client;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * MinaClientA.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 14, 2015
 */
public class MinaClientA {
    /*
     * a Client with auto-reconnect feature.
     */
    private SocketConnector socketConnector;
    private SocketSessionConfig sessionConfig;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private IoSession session;

    public MinaClientA() {
        init();
    }

    public void init() {

        socketConnector = new NioSocketConnector();
        sessionConfig = socketConnector.getSessionConfig();

        sessionConfig.setBothIdleTime(DEFAULT_CONNECT_TIMEOUT);
        // close the tcp connection without step into TIME_WAIT
        sessionConfig.setSoLinger(0);
        socketConnector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory()));

        ClientSessionHandler ioHandler = new ClientSessionHandler();
        socketConnector.setHandler(ioHandler);
        socketConnector.setDefaultRemoteAddress(new InetSocketAddress(HOST,PORT));
        
        socketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
        
        socketConnector.addListener(new IoListener(){ 
            // MAY DEADLOCK, it will capture all the IoProcessor threads
            public void sessionDestroyed(IoSession arg0) throws Exception {
                int num = (int) arg0.getAttribute("num");
                for (;;) {
                    try {
                        Thread.sleep(2000);
                        System.out.println("Session>> "+ num + " closed From IoListener!");
                        ConnectFuture future = socketConnector.connect();
                        System.out.println(Thread.currentThread().getStackTrace());
                        future.awaitUninterruptibly(10000);
                        session = future.getSession();
                        if (session.isConnected()) {
                            System.out.println(num + " Session<< " + session.getAttribute("num")  + " Reconnect");
                            break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(num + " Fail to Reconnect, retry after 2 second");
                    }
                }
            }
        });
        
//        socketConnector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
//            @Override
//            public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
//                for(;;){
//                    try{
//                        System.out.println("Session Closed From FilterChain");
//                        Thread.sleep(3000);
//                        System.out.println("Try To Reconnect");
//                        ConnectFuture future = socketConnector.connect();
//                        future.awaitUninterruptibly();// 等待连接创建成功
//                        session = future.getSession();// 获取会话
//                        if(session.isConnected()){
//                            break;
//                        }
//                    }catch(Exception ex){
//                    }
//                }
//            }
//        });
    }
    public static void main(String[] args) {
        MinaClientA client = new MinaClientA();
        client.continuousConnect();
//        System.exit(0);
    }

    public void continuousConnect() {
        int i = 0;
        while(i++ < 1){
            try {
                ConnectFuture future = socketConnector.connect();
                future.awaitUninterruptibly(); 
                session = future.getSession(); 
                mySleep(1);
            } catch (RuntimeIoException e) {
                mySleep(5);
            }
        }
    }
    
    public void mySleep(int secs){
        try {
            Thread.sleep(1000*secs);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

}

class IoListener implements IoServiceListener{
    @Override
    public void serviceActivated(IoService arg0) throws Exception {
    }
    @Override
    public void serviceDeactivated(IoService arg0) throws Exception {
    }
    @Override
    public void sessionCreated(IoSession arg0) throws Exception {
    }
    @Override
    public void sessionDestroyed(IoSession arg0) throws Exception {
    }
    @Override
    public void serviceIdle(IoService service, IdleStatus idleStatus)
            throws Exception {
    }
}
