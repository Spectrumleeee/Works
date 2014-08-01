package com.tplink.cloud.lgp.NioClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ManagerClient {

	private static Logger log;
	private InetSocketAddress inetSocketAddress;
	
	public ManagerClient(String hostname, int port){
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(NioClient.class.getName());
		inetSocketAddress = new InetSocketAddress(hostname, port);
	}
	
	public void send(String requestDataType, String requestData){
		try {
			SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
			socketChannel.configureBlocking(false);
			ByteBuffer byteBuffer = ByteBuffer.allocate(512);

			String temp = requestDataType + requestData;
			
			socketChannel.write(ByteBuffer.wrap(temp.getBytes()));
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (true) {
				
				byteBuffer.clear();
				int readBytes = socketChannel.read(byteBuffer);
				
				if (readBytes > 0) {
					byteBuffer.flip();
					log.info("ManagerClient: readBytes = " + readBytes);
					log.info("ManagerClient: data = " + new String(byteBuffer.array(), 0, readBytes));
				}else{
					socketChannel.close();
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String hostname = "localhost";
//		String requestDataType = "11112AA";
		String requestDataType = "11112DD";
//		String requestData = "Actions speak louder than words! AA";
		String requestData = "No zuo No die!";
		
		int port = 8888;
		new NioClient(hostname, port).send(requestDataType, requestData);
	}

}
