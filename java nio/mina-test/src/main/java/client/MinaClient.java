/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package client;

import java.net.ConnectException;
import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaClient {
    
    private static Logger logger = LoggerFactory.getLogger(MinaClient.class);
    private SocketConnector socketConnector;
    private SocketSessionConfig sessionConfig;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final String HOST = "localhost";
    private static final int PORT = 9999;

    public MinaClient() {
        init();
    }

    public void init() {

        socketConnector = new NioSocketConnector();
        sessionConfig = socketConnector.getSessionConfig();
        // sessionConfig.setKeepAlive(true);
        // socketConnector.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);

        // sessionConfig.setReaderIdleTime(DEFAULT_CONNECT_TIMEOUT);
        // sessionConfig.setWriterIdleTime(DEFAULT_CONNECT_TIMEOUT);
        sessionConfig.setBothIdleTime(DEFAULT_CONNECT_TIMEOUT);
        // close the tcp connection without step into TIME_WAIT
        sessionConfig.setSoLinger(0);
        socketConnector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory()));

        ClientSessionHandler ioHandler = new ClientSessionHandler();
        socketConnector.setHandler(ioHandler);
    }

    @SuppressWarnings("deprecation")
    public void sendMessage(final String msg) {

        InetSocketAddress addr = new InetSocketAddress(HOST, PORT);
        ConnectFuture cf = socketConnector.connect(addr);
        try {
            cf.awaitUninterruptibly();
            cf.getSession().write(msg);
            logger.info("send message " + msg);
        } catch (RuntimeIoException e) {
            if (e.getCause() instanceof ConnectException) {
                try {
                    if (cf.isConnected()) {
                        cf.getSession().close();
                    }
                } catch (RuntimeIoException e1) {
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MinaClient clent = new MinaClient();
        for (int i = 0; i < 100; i++) {
            clent.sendMessage("Hello World " + i);
        }
        // clent.getSocketConnector().dispose();
    }

    public SocketConnector getSocketConnector() {
        return socketConnector;
    }

    public void setSocketConnector(SocketConnector socketConnector) {
        this.socketConnector = socketConnector;
    }

}
