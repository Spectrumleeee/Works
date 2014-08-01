package com.tplink.cloud.lgp.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ServerHandler implements Handler{

	private static final Logger log = Logger.getLogger(ServerHandler.class.getName());
	
	public void handleAccept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		log.info("Server: accept client socket " + socketChannel);
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);
	}

	
	public void handleRead(SelectionKey key) throws IOException {
			
		ByteBuffer byteBuffer = ByteBuffer.allocate(9);
		ByteBuffer type = ByteBuffer.allocate(4);
		StringBuilder strb = new StringBuilder();
		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		int readBytes = socketChannel.read(type);

		log.info("Message Type is:" + new String(type.array(), 0, readBytes));
		type.flip();
		initType(strb, new String(type.array(), 0, readBytes));
		
		while(true) {
			
			readBytes = socketChannel.read(byteBuffer);
			if(readBytes>0) {
//				log.info("Server: readBytes = " + readBytes);
//				log.info("Server: data = " + new String(byteBuffer.array(), 0, readBytes));				
				strb.append(new String(byteBuffer.array(), 0, readBytes));
				byteBuffer.clear();
//				byteBuffer.flip();
//				socketChannel.write(byteBuffer);
//				System.out.println("read!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+readBytes);
			}else{
				log.info(strb.toString());
				socketChannel.write(ByteBuffer.wrap(strb.toString().getBytes()));
				break;
			}
		}
		socketChannel.close();
	}

	public void handleWrite(SelectionKey key) throws IOException {
		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
		byteBuffer.flip();
		SocketChannel socketChannel = (SocketChannel)key.channel();
		socketChannel.write(byteBuffer);
		if(byteBuffer.hasRemaining()) {
			key.interestOps(SelectionKey.OP_READ);
		}
		byteBuffer.compact();
	}
	
	public void initType(StringBuilder strb, String type){
		
		if(type.equals("1111")){
			strb.append("Yes, ");
		}else if(type.equals("0000")){
			strb.append("No, ");
		}
	}

}
