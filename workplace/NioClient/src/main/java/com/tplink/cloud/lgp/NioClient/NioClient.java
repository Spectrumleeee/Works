/**
 * NioClient entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.NioClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.*;

/**
 * The class creates a Client based on NIO TCP
 */
public class NioClient {

    private static Logger log;
    private InetSocketAddress inetSocketAddress;

    public NioClient(String hostname, int port) {
        PropertyConfigurator.configure("log4j.properties");
        log = Logger.getLogger(NioClient.class.getName());
        inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    /**
     * send the request data with different header type
     * 
     * @param requestData
     * @throws InterruptedException
     */
    public void send(String requestDataType, String requestData) {
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
                    byteBuffer.flip();
                    log.info("Client: readBytes = " + readBytes);
                    log.info("Client: data = "
                            + new String(byteBuffer.array(), 0, readBytes));
                } else {
                    socketChannel.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 8888;
        /**
         * The header is consist of 9 bits, the former 4 bits stand for the
         * client id, the laster 4 bits stand for the subject this client will
         * subscribe, the middle bit '1' means establish a connection and '0'
         * means get message from server
         */
        String header1 = "000011010";
        String header11 = "00000XXXX";
        String header2 = "001110011";
        String header22 = "00110XXXX";
        String requestData = "Words useless";
        //
        new NioClient(hostname, port).send(header1, requestData);
        new NioClient(hostname, port).send(header2, requestData);

        // new NioClient(hostname, port).send(header11, requestData);
        // new NioClient(hostname, port).send(header22, requestData);
    }
}
