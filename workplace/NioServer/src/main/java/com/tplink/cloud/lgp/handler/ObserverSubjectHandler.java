/**
 * ObserverSubjectHandler entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.tplink.cloud.lgp.Observer.ConcreteSubject;
import com.tplink.cloud.lgp.Observer.Observer;
import com.tplink.cloud.lgp.Observer.Subject;
import com.tplink.cloud.lgp.validate.ReturnType;
import com.tplink.cloud.lgp.validate.Validate;

public class ObserverSubjectHandler implements Handler {

    // message type flags
    final static byte CLIENT_PULL_MESSAGE = '0';
    final static byte CLIENT_REGISTER = '1';
    final static byte MANAGER_PUBLISH = '2';

    private static final Logger log = Logger.getLogger(ServerHandler.class
            .getName());
    // in reality, this subjects must be persistent
    private  Map<String, Subject> subjects = new HashMap<String, Subject>();
    private  Map<String, Observer> observers = new HashMap<String, Observer>();

    public ObserverSubjectHandler() {
            subjects.put("AA", new ConcreteSubject("AA"));
            subjects.put("BB", new ConcreteSubject("BB"));
            subjects.put("CC", new ConcreteSubject("CC"));
            subjects.put("DD", new ConcreteSubject("DD"));
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
                .channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        log.info("Server: accept client socket " + socketChannel);
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        ByteBuffer clientId = ByteBuffer.allocate(4);
        ByteBuffer operation = ByteBuffer.allocate(1);
        String clientName;

        SocketChannel socketChannel = (SocketChannel) key.channel();
        // read the Client Id
        int idLen = socketChannel.read(clientId);
        if (!Validate.validateClientId(clientId, idLen)) {
            // input error, then return
            socketChannel.write(ByteBuffer.wrap(ReturnType.INPUT_ERROR
                    .getBytes()));
            return;
        }
        clientName = new String(clientId.array(), 0, idLen);
        log.info("Client Id is:" + clientName);
        clientId.clear();

        // read the Operation Code : 0 or 1 or 2
        int readBytes = socketChannel.read(operation);
        if (!Validate.validateOperationCode(operation, readBytes)) {
            // input operation code error!
            socketChannel.write(ByteBuffer.wrap(ReturnType.INPUT_ERROR
                    .getBytes()));
            return;
        }
        log.info("Client Operatioin Code is:"
                + new String(operation.array(), 0, readBytes));
        // use HandlerProcess to process the concrete request!
        HandlerProcess handerProcess = new HandlerProcess(socketChannel, clientName, observers, subjects);
        if (CLIENT_PULL_MESSAGE == operation.array()[0]) {
            // client pull message from server
            handerProcess.pullMessage();
        }
        else if (MANAGER_PUBLISH == operation.array()[0]) {
            // manager publish message
            handerProcess.publishMessage();
        }
        else if (CLIENT_REGISTER == operation.array()[0]) {
            // if Operation Code is 1, we will create a concrete observer and
            // register it while it has not been registered before
            handerProcess.clientRegister();
        }
        socketChannel.socket().close();
        socketChannel.close();
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.flip();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(byteBuffer);
        if (byteBuffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        byteBuffer.compact();
    }

}
