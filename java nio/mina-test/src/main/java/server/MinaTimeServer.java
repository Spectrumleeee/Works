/**
 * MinaTimeServer entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTimeServer {

	/**
	 * @param args
	 */
	private static final int PORT = 9999;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// If use relative address, "log4j.properties" must be put in "src/main/"
		System.setProperty("log4j.configuration", "log4j.properties"); 
		
		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		
		acceptor.setHandler(new TimeServerHandler());
		
		acceptor.getSessionConfig().setReadBufferSize(2048);
		// every 10 seconds idle will execute method sessionIdle()
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		acceptor.bind(new InetSocketAddress(PORT));
		
	}

}
