/**
 * NioServer entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.NioServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.tplink.cloud.lgp.handler.Handler;
import com.tplink.cloud.lgp.handler.*;


public class NioServer 
{
    public static void main( String[] args )
    {
    	Thread thread = new NioTcpServer("127.0.0.1", 8888);
    	thread.start();
    }
}

class NioTcpServer extends Thread {

	private static final Logger log = Logger.getLogger(NioTcpServer.class.getName());
	private InetSocketAddress inetSocketAddress;
	private Handler handler ;

	public NioTcpServer(String hostname, int port) {
		inetSocketAddress = new InetSocketAddress(hostname, port);
		handler = new ObserverSubjectHandler();
	}
	
	@Override
	public void run() {
		try {
			// open a selector
			Selector selector = Selector.open(); 
			// open a server socket channel
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); 
			// set the channel not blocking
			serverSocketChannel.configureBlocking(false); 
			// bind the channel to a socket
			serverSocketChannel.socket().bind(inetSocketAddress);
			// register the channel to selector
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); 
			log.info("Server: socket server started.");
			// try polling
			while(true) { 
				int nKeys = selector.select();
				
				if(nKeys>0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedKeys.iterator();
					
					while(it.hasNext()) {
						SelectionKey key = it.next();
						
						if(key.isAcceptable()) {
							log.info("Server: SelectionKey is acceptable.");
							handler.handleAccept(key);
						} 
						else if(key.isReadable()) {
							log.info("Server: SelectionKey is readable.");
							handler.handleRead(key);
						} 
						else if(key.isWritable()) {
							log.info("Server: SelectionKey is writable.");
							handler.handleWrite(key);					
						}
						it.remove();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// NioTcpServer main , just for testing
	public static void main(String[] args) {
		NioTcpServer server = new NioTcpServer("localhost", 1000);
		server.start();
	}
}
