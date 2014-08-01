package com.tplink.cloud.lgp.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface Handler {

	/**
	 * 处理{@link SelectionKey#OP_ACCEPT}事件
	 * @param key 
	 * @throws IOException
	 */
	void handleAccept(SelectionKey key) throws IOException;
	/**
	 * 处理{@link SelectionKey#OP_READ}事件
	 * @param key 
	 * @throws IOException
	 */
	void handleRead(SelectionKey key) throws IOException;
	/**
	 * 处理{@link SelectionKey#OP_WRITE}事件
	 * @param key 
	 * @throws IOException
	 */
	void handleWrite(SelectionKey key) throws IOException;
	
}
