/**
 * HandlerProcess class, be used to process the concrete request from the client
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 13, 2014
 */

package com.tplink.cloud.lgp.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.tplink.cloud.lgp.Observer.ConcreteObserver;
import com.tplink.cloud.lgp.Observer.ConcreteSubject;
import com.tplink.cloud.lgp.Observer.Observer;
import com.tplink.cloud.lgp.Observer.Subject;
import com.tplink.cloud.lgp.validate.ReturnType;
import com.tplink.cloud.lgp.validate.Validate;

public class HandlerProcess {

    private SocketChannel sc;
    private String clientName;
    private Map<String, Observer> observers;
    private Map<String, Subject> subjects;

    public HandlerProcess(SocketChannel sc, String clientName,
            Map<String, Observer> observers, Map<String, Subject> subjects) {
        this.sc = sc;
        this.clientName = clientName;
        this.observers = observers;
        this.subjects = subjects;
    }

    /**
     * be used to process the pull message request from the client
     * @throws IOException
     */
    public void pullMessage() throws IOException {
        synchronized (this) {
            if (observers.containsKey(clientName)) {
                ConcreteObserver co = (ConcreteObserver) observers
                        .get(clientName);
                sc.write(ByteBuffer.wrap(co.getInterest().getBytes()));
            } else {
                // client is not registered!
                sc.write(ByteBuffer.wrap(ReturnType.NOT_REGISTERED.getBytes()));
                return;
            }
        }
    }

    /**
     * be used to process the publish request from the client
     * @throws IOException
     */
    public void publishMessage() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        int readBytes = sc.read(byteBuffer);
        if (!Validate.validateSubjectId(byteBuffer, readBytes)) {
            sc.write(ByteBuffer.wrap(ReturnType.INPUT_ERROR.getBytes()));
            return;
        }

        String subjectId = new String(byteBuffer.array(), 0, 2);
        String subjectContent = "Nothing!";
        if (readBytes > 2)
            subjectContent = new String(byteBuffer.array(), 2, readBytes - 2);

        synchronized (this) {
            ConcreteSubject cs = (ConcreteSubject) subjects.get(subjectId);

            if (null == cs) {
                sc.write(ByteBuffer.wrap(ReturnType.SUBJECT_NOT_EXISTED
                        .getBytes()));
                return;
            }
            cs.notify(subjectContent);
        }
    }

    /**
     * be used to process the client register request from the client
     * @throws IOException
     */
    public void clientRegister() throws IOException {
        ByteBuffer subscribeItem = ByteBuffer.allocate(4);

        if (observers.containsKey(clientName)) {
            sc.write(ByteBuffer.wrap(ReturnType.DUP_REGISTERED.getBytes()));
            return;
        }
        ConcreteObserver co = new ConcreteObserver(clientName);
        observers.put(clientName, co);

        synchronized (this) {
            // read subscribe item
            int readBytes = sc.read(subscribeItem);
            byte[] subscri = subscribeItem.array();

            if (!Validate.validateSubscribeInfo(subscri, readBytes)) {
                sc.write(ByteBuffer.wrap(ReturnType.INPUT_ERROR.getBytes()));
                return;
            }

            int i = 0;
            for (String subjectId : subjects.keySet()) {
                if (subscri[i++] == '1') {
                    System.out.println(clientName
                            + " has subscribed the subject : " + subjectId);
                    co.subscribe(subjects.get(subjectId));
                }
            }
        }
        sc.write(ByteBuffer.wrap("Register Successful !!!".getBytes()));
    }
}
