/**
 * NioServer entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 4, 2014
 */

package com.tplink.cloud.lgp.NioServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.tplink.cloud.lgp.handler.*;
import com.tplink.cloud.lgp.thread.Reader;
import com.tplink.cloud.lgp.thread.Writer;

public class NioServer {

    private Thread thread = null;

    public static void main(String[] args) {
        Thread thread = new NioTcpServer("127.0.0.1", 8888);
        thread.start();
    }

    public void start(int port) {
        this.thread = new NioTcpServer("127.0.0.1", port);
        this.thread.start();
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }
    }
}

class NioTcpServer extends Thread {

    private static final Logger log = Logger.getLogger(NioTcpServer.class
            .getName());
    private InetSocketAddress inetSocketAddress;
    private Handler handler;

    private ExecutorService readPool = Executors.newFixedThreadPool(4);
    private ExecutorService writePool = Executors.newCachedThreadPool();

    public NioTcpServer(String hostname, int port) {
        inetSocketAddress = new InetSocketAddress(hostname, port);
        handler = new ObserverSubjectHandler();

    }

    @Override
    public void run() {
        try {
            // open a selector
            final Selector selector = Selector.open();
// *******************************Test Selector.wakeup()******************************************
            Thread innerWakeupThread = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        selector.wakeup(); // inner class can just access final
                                           // variable
                    }
                }
            };
            innerWakeupThread.start();

            try {
                for (int i = 1; i < 5; i++) {
                    Thread severalThreadCallSelect = new Thread() {
                        public void run() {
                            try {
                                selector.select();
                                System.out.println("thread was wakeup ");
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    };
                    severalThreadCallSelect.start();
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
// *******************************Test Selector.wakeup()******************************************
            // open a server socket channel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel
                    .open();
            // set the channel not blocking
            serverSocketChannel.configureBlocking(false);
            // bind the channel to a socket
            serverSocketChannel.socket().bind(inetSocketAddress);
            // register the channel to selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("Server: socket server started.");
            // try polling
            while (true) {
                int nKeys = selector.select();

                if (nKeys > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectedKeys.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();

                        if (key.isAcceptable()) {
                            log.info("Server: SelectionKey is acceptable.");
                            handler.handleAccept(key);
                        } else if (key.isReadable()) {
                            log.info("Server: SelectionKey is readable.");
                            // handler.handleRead(key);
                            readPool.execute(new Reader(key, handler));
                            // when we use threadPool to execute read operation,
                            // we must cancel the key, if not, selector.select()
                            // may get the same key, and we will make a mistake
                            key.cancel();
                        } else if (key.isWritable()) {
                            log.info("Server: SelectionKey is writable.");
                            // handler.handleWrite(key);
                            writePool.execute(new Writer(key, handler));
                            // multi-process need key.cancell();
                            key.cancel();
                        }
                        it.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // NioTcpServer main , just for testing
    public static void main(String[] args) {
        NioTcpServer server = new NioTcpServer("localhost", 1000);
        server.start();
    }
}
