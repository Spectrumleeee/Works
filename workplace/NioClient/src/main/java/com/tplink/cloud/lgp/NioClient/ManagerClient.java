/**
 * ManagerClient entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 11, 2014
 */

package com.tplink.cloud.lgp.NioClient;

public class ManagerClient extends Client {

    public ManagerClient(String hostname, int port) {
        super(hostname, port);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String hostname = "localhost";
        int port = 8888;

        /**
         * the header consist of 7 bits, first 4 bits stand for the client id,
         * last 2 bits means subject id, the 5th bit means this is a connection
         * from the manager client.
         */
        String header = "11112DD";
        String requestData = "No zuo No die!";
        // String header = "11112DD";
        // String requestData = "No zuo No die!";

        new NioClient(hostname, port).send(header, requestData);
    }

}
