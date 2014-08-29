/**
 * Validate class, be used to validate request parameter
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 13, 2014
 */

package com.tplink.cloud.lgp.validate;

import java.nio.ByteBuffer;

public class Validate {
    
    /**
     * this method will validate the client id
     * @param clientId
     * @param len
     * @return boolean
     */
    public static boolean validateClientId(ByteBuffer clientId, int length){
        if(4 != length)
            return false;
        
        return isDigitByRegex(new String(clientId.array(), 0, length));
    }
    /**
     * this method will validate the operation code , here we define the 
     * operation code can just be 0,1 or 2.
     * @param oper
     * @param length
     * @return
     */
    public static boolean validateOperationCode(ByteBuffer oper, int length){
        if(1 != length || oper.get(0) < 48 || oper.get(0) > 50)
            return false;
        else
            return true;
    }
    
    /**
     * this method will validate subject id
     * @param subjectId
     * @param length
     * @return
     */
    public static boolean validateSubjectId(ByteBuffer subjectId, int length){
        if(length < 2)
            return false;
        
        return isUpperChar(subjectId, 2);
    }
    
    /**
     * this method will validate the subscribe information of client
     * @param info
     * @param length
     * @return
     */
    public static boolean validateSubscribeInfo(byte[] info, int length){
        if(4 != length)
            return false;
        return isOneOrZero(info);
    }
    
    public static boolean isDigit(String clientId){
        for(int i=0; i<clientId.length(); i++)
            if(!Character.isDigit(clientId.charAt(i)))
                return false;
        return true;
    }
    
    public static boolean isDigitByRegex(String clientId){
        if(!clientId.matches("[0-9]+"))
            return false;
        else
            return true;
          
            
    }
    
    public static boolean isDigit(byte[] clientId){
        for(int i=0; i<clientId.length; i++){
            if( clientId[i] < 48 || clientId[i] > 57)
                return false;
        }
        return true;
    }
    
    public static boolean isUpperChar(ByteBuffer bb, int length){
        for(int i=0; i<length; i++)
            if(!Character.isUpperCase(bb.array()[i]))
                return false;
        return true;   
    }
    
    public static boolean isOneOrZero(byte[] info){
        for(int i=0; i<info.length; i++)
            if(info[i]<48 || info[i]>49)
                return false;
        return true;
    }
    
}
