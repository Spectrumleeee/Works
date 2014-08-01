package com.tplink.cloud.lgp.NioClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;



//import java.util.logging.Logger;
import org.apache.log4j.*;

public class NioClient {

	private static Logger log;
	private InetSocketAddress inetSocketAddress;
	
	public NioClient(String hostname, int port) {
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(NioClient.class.getName());
		inetSocketAddress = new InetSocketAddress(hostname, port);
	}
	
	/**
	 * 发送请求数据
	 * @param requestData
	 * @throws InterruptedException 
	 */
	public void send(String requestDataType, String requestData){
		try {
			SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
			socketChannel.configureBlocking(false);
			ByteBuffer byteBuffer = ByteBuffer.allocate(512);
			
//			socketChannel.write(ByteBuffer.wrap(requestDataType.getBytes()));
//			byteBuffer.clear();
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
					log.info("Client: readBytes = " + readBytes);
					log.info("Client: data = " + new String(byteBuffer.array(), 0, readBytes));
				}else{
					socketChannel.close();
					break;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String hostname = "localhost";
		
		/**
		 * Here define two message types
		 * when server get '1111' , it will return yes and repeat the message from client
		 * if server get '0000' , it will return no and repeat the message.
		 * others(but must be for bytes ) it'll just repeat the message.
		 * 
		 * sometimes we just need 8 bits can make it 
		 */
		
//		String requestDataType = "1010";
		String create1 = "000011010";
		String read1 = "00000XXXX";
		String create2 = "001110011";
		String read2 = "00110XXXX";
//		String requestDataType = "1111";
		String requestData = "Actions speak louder than words!";
		
		int port = 8888;
//		new NioClient(hostname, port).send(create1, requestData);
//		new NioClient(hostname, port).send(create2, requestData);
		
		new NioClient(hostname, port).send(read1, requestData);
		new NioClient(hostname, port).send(read2, requestData);
	}
}
