/**
 * Interface Handler
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface Handler {

    /**
     * handler {@link SelectionKey#OP_ACCEPT} event
     * 
     * @param key
     * @throws IOException
     */
    void handleAccept(SelectionKey key) throws IOException;

    /**
     * handler {@link SelectionKey#OP_READ} event
     * 
     * @param key
     * @throws IOException
     */
    void handleRead(SelectionKey key) throws IOException;

    /**
     * handler {@link SelectionKey#OP_WRITE} event
     * 
     * @param key
     * @throws IOException
     */
    void handleWrite(SelectionKey key) throws IOException;

}
