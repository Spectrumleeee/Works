/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 14, 2014
 */

package com.tplink.cloud.lgp.NioServer;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.tplink.cloud.lgp.validate.Validate;

public class TestValidate {

    private String testString;
    private byte[] byteArray;
    private ByteBuffer byteBuffer;

    @Test
    public void testIsStringDigit() {
        testString = "asdfewsdfwe";
        assertEquals(false, Validate.isDigit(testString));

        testString = "12596131241548";
        assertEquals(true, Validate.isDigit(testString));

        testString = "123456a1234565";
        assertEquals(false, Validate.isDigit(testString));
    }

    @Test
    public void testIsByteArrayDigit() {
        byteArray = "sdfwesdfwesdfwe".getBytes();
        assertEquals(false, Validate.isDigit(byteArray));

        byteArray = "123456121321548".getBytes();
        assertEquals(true, Validate.isDigit(byteArray));

        byteArray = "12345d126452123".getBytes();
        assertEquals(false, Validate.isDigit(byteArray));
    }

    @Test
    public void testIsByteBufferUpperChar() {
        byteBuffer = ByteBuffer.wrap("ABDFESFDFEDF".getBytes());
        assertEquals(true,
                Validate.isUpperChar(byteBuffer, "ABDFESFDFEDF".length()));

        byteBuffer = ByteBuffer.wrap("SDKLFJdfeSDKFJE".getBytes());
        assertEquals(false,
                Validate.isUpperChar(byteBuffer, "SDKLFJdfeSDKFJE".length()));

        byteBuffer = ByteBuffer.wrap("12lk234DSFJK54".getBytes());
        assertEquals(false,
                Validate.isUpperChar(byteBuffer, "12lk234DSFJK54".length()));
    }

    @Test
    public void testIsByteArrayOneOrZero() {
        byteArray = "01001001110101010100000101".getBytes();
        assertEquals(true, Validate.isOneOrZero(byteArray));

        byteArray = "010010201110103010000001".getBytes();
        assertEquals(false, Validate.isOneOrZero(byteArray));
    }

    @Test
    public void testValidateClientId() {
        // client id must be a byteBuffer with 4 bytes, each byte is digital
        byteBuffer = ByteBuffer.wrap("1234".getBytes());
        assertEquals(true,
                Validate.validateClientId(byteBuffer, "1234".length()));

        byteBuffer = ByteBuffer.wrap("a231".getBytes());
        assertEquals(false,
                Validate.validateClientId(byteBuffer, "a231".length()));

        byteBuffer = ByteBuffer.wrap("12".getBytes());
        assertEquals(false,
                Validate.validateClientId(byteBuffer, "12".length()));
    }

    @Test
    public void testValidateOperationCode() {
        // operation code is a bytebuffer '0' '1' or '2';
        byteBuffer = ByteBuffer.wrap("1".getBytes());
        assertEquals(true,
                Validate.validateOperationCode(byteBuffer, "1".length()));

        byteBuffer = ByteBuffer.wrap("3".getBytes());
        assertEquals(false,
                Validate.validateOperationCode(byteBuffer, "3".length()));

        byteBuffer = ByteBuffer.wrap("A".getBytes());
        assertEquals(false,
                Validate.validateOperationCode(byteBuffer, "A".length()));
    }

    @Test
    public void testValidateSubjectId() {
        // a bytebuffer with at least 2 bytes and each byte is upper character
        byteBuffer = ByteBuffer.wrap("AA".getBytes());
        assertEquals(true, Validate.validateSubjectId(byteBuffer, "AA".length()));
        
        byteBuffer = ByteBuffer.wrap("ABC".getBytes());
        assertEquals(true, Validate.validateSubjectId(byteBuffer, "ABC".length()));
        
        byteBuffer = ByteBuffer.wrap("abcd".getBytes());
        assertEquals(false, Validate.validateSubjectId(byteBuffer, "abcd".length()));
    }

    @Test
    public void testValidateSubscribeInfo() {
        // subscribe information is a byte array with 4 bytes each is '0' or '1'
        byteArray = "0101".getBytes();
        assertEquals(true, Validate.validateSubscribeInfo(byteArray, "0101".length()));
        
        byteArray = "010".getBytes();
        assertEquals(false, Validate.validateSubscribeInfo(byteArray, "010".length()));
        
        byteArray = "abcd".getBytes();
        assertEquals(false, Validate.validateSubscribeInfo(byteArray, "abcd".length()));
        
        byteArray = "010101".getBytes();
        assertEquals(false, Validate.validateSubscribeInfo(byteArray, "010101".length()));
    }

}
