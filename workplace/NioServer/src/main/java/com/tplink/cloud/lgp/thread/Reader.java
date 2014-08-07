/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.thread;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.tplink.cloud.lgp.handler.Handler;

public class Reader implements Runnable {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    private SelectionKey sk;
    private Handler osh;

    public Reader(SelectionKey key, Handler osh) {
        this.sk = key;
        this.osh = osh;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            osh.handleRead(sk);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
