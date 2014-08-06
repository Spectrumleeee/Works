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
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {
	
	public SocketConnector socketConnector;

	/**
	 * 缺省连接超时时间
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 5;

	public static final String HOST = "localhost";

	public static final int PORT = 9999;

	public MinaClient() {
		init();
	}

	public void init() {
		
		socketConnector = new NioSocketConnector();

//		socketConnector.getSessionConfig().setKeepAlive(true);

		socketConnector.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);

		socketConnector.getSessionConfig().setReaderIdleTime(DEFAULT_CONNECT_TIMEOUT);
		socketConnector.getSessionConfig().setWriterIdleTime(DEFAULT_CONNECT_TIMEOUT);
		socketConnector.getSessionConfig().setBothIdleTime(DEFAULT_CONNECT_TIMEOUT);

		socketConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
		
		ClientSessionHandler ioHandler = new ClientSessionHandler();
		socketConnector.setHandler(ioHandler);
		
	}

	public void sendMessage(final String msg) {
		
		InetSocketAddress addr = new InetSocketAddress(HOST, PORT);
		ConnectFuture cf = socketConnector.connect(addr);
		try {
			cf.awaitUninterruptibly();
			cf.getSession().write(msg);
			System.out.println("send message " + msg);
		} 
		catch (RuntimeIoException e) {
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
		for (int i = 0; i < 3; i++) {
			System.err.println(i);
			clent.sendMessage("Hello World " + i);
		}
		
		clent.getSocketConnector().dispose();
		
	}

	public SocketConnector getSocketConnector() {
		return socketConnector;
	}

	public void setSocketConnector(SocketConnector socketConnector) {
		this.socketConnector = socketConnector;
	}
	
	
}


