/**
 * Client Abstract Class
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 11, 2014
 */

package com.tplink.cloud.lgp.NioClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.*;

/**
 * The class Client is a base class
 */
public class Client {

    protected static Logger log;
    protected InetSocketAddress inetSocketAddress;

    public Client(String hostname, int port) {
        PropertyConfigurator.configure("log4j.properties");
        log = Logger.getLogger(Client.class.getName());
        inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    /**
     * send the request data with different header type
     * @param requestData
     * @throws InterruptedException
     */
    public String send(String requestDataType, String requestData) {
        String messageReceived = "";
        
        try {
            SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
            socketChannel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            String message = requestDataType + requestData;
            // write the message to the socketChannel
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));

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
                    messageReceived = new String(byteBuffer.array(), 0, readBytes);
                    byteBuffer.flip();
                    log.info("Client: readBytes = " + readBytes);
                    log.info("Client: data = "
                            + new String(byteBuffer.array(), 0, readBytes));
                } else {
                    /**
                     * until here, socketChannel is
                     * connected(socketChannel.isConnected() == true), even
                     * though you invoke Thread.sleep(long) in this place,
                     * socketChannel will always be Connected.
                     * printSocketChannelStatus(socketChannel);
                     */
                    socketChannel.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageReceived;
    }

    public void printSocketChannelStatus(SocketChannel sc) {

        if (isSocketChannelConnected(sc)) {
            System.out.println("socketChannel is connected!");
        } else {
            System.out.println("socketChannel is closed!");
        }

        if (isSocketClosed(sc)) {
            System.out.println("socket is closed!");
        } else {
            System.out.println("socket is opened!");
        }
    }

    public boolean isSocketChannelConnected(SocketChannel sc) {
        return sc.isConnected();
    }

    public boolean isSocketClosed(SocketChannel sc) {
        return sc.socket().isClosed();
    }
    
}
