/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package server;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);
    private static int count = 1;
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.info("session closed " + count++);
//        cause.printStackTrace();
    }
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        logger.info("receive a message from client : " + session.getRemoteAddress());
        String str = message.toString();
        if (str.trim().equalsIgnoreCase("quit")) {
            session.close(true);
            return;
        }
        Date date = new Date();
        session.write(date.toString());
        logger.info("Message written...");
    }
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.info("IDLE " + session.getIdleCount(status));
    }
}
