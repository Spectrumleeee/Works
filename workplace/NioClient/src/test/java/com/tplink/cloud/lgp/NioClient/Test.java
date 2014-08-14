/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 13, 2014
 */

package com.tplink.cloud.lgp.NioClient;

public class Test {

    private String header;
    private String message;
    private Client clt;
    
    
    public Test(){
        clt = new Client("127.0.0.1", 8888);
    }
    
    public void register2publish2pull(){
        
        header = "000011010";
        message = "Client Regist!";
        clt.send(header, message);
        header = "001110011";
        clt.send(header, message);
        
        header = "11112DD";
        message = "Manager Publish Message!";
        clt.send(header, message);
        
        header = "00000XXXX";
        message = "Client Pull Message";
        clt.send(header, message);
        header = "00110XXXX";
        clt.send(header, message);   
    }
    
    public void test(){
        header = "111111010";
        message = "Manager Publish Message!";
        clt.send(header, message);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Test().register2publish2pull();
//        new Test().test();
    }

}
