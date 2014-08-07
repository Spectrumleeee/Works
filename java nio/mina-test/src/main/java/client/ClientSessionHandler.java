/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package client;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ClientSessionHandler extends IoHandlerAdapter {

    private void releaseSession(IoSession session) throws Exception {
        System.out.println("releaseSession");
        if (session.isConnected()) {
            session.close();
        }
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // session.getConfig().setBothIdleTime(10);
        System.out.println("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("sessionIdle");
        try {
            releaseSession(session);
        } catch (RuntimeIoException e) {
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        System.out.println("Receive Server message " + message);

        super.messageReceived(session, message);

        releaseSession(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.println("exceptionCaught");
        cause.printStackTrace();
        releaseSession(session);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("messageSent");
        super.messageSent(session, message);
    }
}
