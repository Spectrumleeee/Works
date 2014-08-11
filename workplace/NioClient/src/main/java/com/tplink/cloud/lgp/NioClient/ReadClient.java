/**
 * NioClient entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.NioClient;

/**
 * The class creates a Client based on NIO TCP
 */
public class ReadClient extends Client{

    public ReadClient(String hostname, int port) {
        super(hostname, port);
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
//        String header1 = "000011010";
        String header11 = "00000XXXX";
//        String header2 = "001110011";
        String header22 = "00110XXXX";
        String requestData = "Words useless";

//         new NioClient(hostname, port).send(header1, requestData);
//         new NioClient(hostname, port).send(header2, requestData);

        new ReadClient(hostname, port).send(header11, requestData);
        new ReadClient(hostname, port).send(header22, requestData);
    }
}
