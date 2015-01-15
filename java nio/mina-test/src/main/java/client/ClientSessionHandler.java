/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSessionHandler extends IoHandlerAdapter {
    
    private Logger logger = LoggerFactory.getLogger(ClientSessionHandler.class);
    private static int num = 1;
    
    public void sessionCreated(IoSession session) throws Exception {
        // Empty handler
        logger.info("Session " + num + " created");
        session.setAttribute("num", num++);
    }
    
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // session.getConfig().setBothIdleTime(10);

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("Session " + session.getAttribute("num") + " closed From IoHandler");
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.info("Session " + session.getAttribute("num") + " IDLE");
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        logger.info("Receive Server message " + message);

        super.messageReceived(session, message);
//        releaseSession(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.info("exceptionCaught");
        cause.printStackTrace();
//        releaseSession(session);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("messageSent");
        super.messageSent(session, message);
    }
}
