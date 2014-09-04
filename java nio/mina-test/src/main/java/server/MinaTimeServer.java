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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaTimeServer {

    private static final int PORT = 9999;
    // default log4j.properties path is '/src/main/resources'
    private static Logger logger = LoggerFactory.getLogger(MinaTimeServer.class);
    
    public static void main(String[] args) throws IOException {
  
        IoAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset
                        .forName("UTF-8"))));

        acceptor.setHandler(new TimeServerHandler());

        acceptor.getSessionConfig().setReadBufferSize(2048);
        // every 10 seconds idle will execute method sessionIdle()
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);

        acceptor.bind(new InetSocketAddress(PORT));
        
        logger.info("server started! ");

    }

}
