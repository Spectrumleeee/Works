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

import com.tplink.cloud.lgp.Observer.ConcreteObserver;
import com.tplink.cloud.lgp.Observer.ConcreteSubject;
import com.tplink.cloud.lgp.Observer.Observer;
import com.tplink.cloud.lgp.Observer.Subject;

public class ObserverSubjectHandler implements Handler {

	private static final Logger log = Logger.getLogger(ServerHandler.class
			.getName());
	// in reality, this subjects must be persistent
	private static Map<String, Subject> subjects = new HashMap<String, Subject>();
	private static Map<String, Observer> observers = new HashMap<String, Observer>();

	public ObserverSubjectHandler() {

		synchronized (this) {
			
//			subjects.put("AA", ConcreteSubject.getSubject("AA"));
//			subjects.put("BB", ConcreteSubject.getSubject("BB"));
//			subjects.put("CC", ConcreteSubject.getSubject("CC"));
//			subjects.put("DD", ConcreteSubject.getSubject("DD"));

			subjects.put("AA", new ConcreteSubject("AA"));
			subjects.put("BB", new ConcreteSubject("BB"));
			subjects.put("CC", new ConcreteSubject("CC"));
			subjects.put("DD", new ConcreteSubject("DD"));
		}
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		log.info("Server: accept client socket " + socketChannel);
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		ByteBuffer byteBuffer = ByteBuffer.allocate(512);
		ByteBuffer clientId = ByteBuffer.allocate(4);
		ByteBuffer operation = ByteBuffer.allocate(1);
		ByteBuffer subscribeItem = ByteBuffer.allocate(4);

		SocketChannel socketChannel = (SocketChannel) key.channel();

		// read the Client Id
		int idLen = socketChannel.read(clientId);
		log.info("Client Id is:" + new String(clientId.array(), 0, idLen));
		clientId.clear();

		// read the Operation Code : 0 or 1 or 2
		int readBytes = socketChannel.read(operation);
		log.info("Client Operatioin Code is:"
				+ new String(operation.array(), 0, readBytes));

		if (operation.array()[0] == '1') {
			// if Operation Code is 1, we will create a concrete observer and
			// register it
			ConcreteObserver co = new ConcreteObserver(new String(
					clientId.array(), 0, idLen));
			synchronized (this) {
		
				observers.put(new String(clientId.array(), 0, idLen), co);

				// read subscribe item
				readBytes = socketChannel.read(subscribeItem);
				byte[] subscri = subscribeItem.array();

				int i = 0;
				for (String subjectId : subjects.keySet()) {

					if (subscri[i++] == '1') {
						System.out.println(new String(clientId.array(), 0,
								idLen)
								+ " has subscribed the subject : "
								+ subjectId);
						co.subscribe(subjects.get(subjectId));
					}
				}
			}
			socketChannel.write(ByteBuffer.wrap("Register Successful !!!"
					.getBytes()));

		} else if (operation.array()[0] == '0') {
			// send message to client

			String clientName = new String(clientId.array(), 0, idLen);
			synchronized (this) {
				if (observers.containsKey(clientName)) {
					ConcreteObserver co = (ConcreteObserver) observers
							.get(clientName);
					socketChannel.write(ByteBuffer.wrap(co.getInterest()
							.getBytes()));
				}
			}

		} else if (operation.array()[0] == '2') {
			// manager notify message
			readBytes = socketChannel.read(byteBuffer);
			String subjectId = new String(byteBuffer.array(), 0, 2);
			String subjectContent = new String(byteBuffer.array(), 2,
					readBytes - 2);
			synchronized (this) {
				ConcreteSubject cs = (ConcreteSubject) subjects.get(subjectId);
				cs.notify(subjectContent);
			}
		}

		socketChannel.close();
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
		byteBuffer.flip();
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.write(byteBuffer);
		if (byteBuffer.hasRemaining()) {
			key.interestOps(SelectionKey.OP_READ);
		}
		byteBuffer.compact();
	}

	public void initType(StringBuilder strb, String type) {

		if (type.equals("1111")) {
			strb.append("Yes, ");
		} else if (type.equals("0000")) {
			strb.append("No, ");
		}
	}
}
