/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 14, 2014
 */

package com.tplink.encrypt.lgp.encrypt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptTool {

    private final static int BUFFER_SIZE = 8192;
    private final static int ENCRYPT = 0;
    private final static int DECRYPT = 1;

    private Cipher ci;
    private KeyPair kp;
    private long randomSeed;

    public CryptTool(String alg) {
        try {
            ci = Cipher.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // encrypt with provided public key
    public byte[] Encrypt(PublicKey pub, byte[] plainText) throws Exception {
        ci.init(Cipher.ENCRYPT_MODE, pub);
        return ci.doFinal(plainText);
    }

    // decrypt with provided private key
    public byte[] Decrypt(PrivateKey pri, byte[] cipherText) throws Exception {
        ci.init(Cipher.DECRYPT_MODE, pri);
        return ci.doFinal(cipherText);
    }

    // encrypt with default public key
    public byte[] Encrypt(byte[] plainText) throws Exception {
        ci.init(Cipher.ENCRYPT_MODE, kp.getPublic());
        return ci.doFinal(plainText);
    }

    // decrypt with default private key
    public byte[] Decrypt(byte[] cipherText) throws Exception {
        ci.init(Cipher.DECRYPT_MODE, kp.getPrivate());
        return ci.doFinal(cipherText);
    }

    // encrypt with password
    public byte[] Encrypt(byte[] plainText, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        ci.init(Cipher.ENCRYPT_MODE, key);
        return ci.doFinal(plainText);
    }

    // decrypt with password
    public byte[] Decrypt(byte[] cipherText, String password) throws Exception {

        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        ci.init(Cipher.DECRYPT_MODE, key);
        return ci.doFinal(cipherText);
    }

    // encrypt file with password
    public void EncryptFile(String fileIn, String password) throws Exception {
        CryptFile(fileIn, password, ENCRYPT);
    }

    // decrypt file with password
    public void DecryptFile(String cipherFileIn, String password)
            throws Exception {
        CryptFile(cipherFileIn, password, DECRYPT);
    }

    // encrypt/decrypt file method
    private void CryptFile(String fileIn, String password, int encOrDec)
            throws Exception {
        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        FileInputStream fis = new FileInputStream(fileIn);
        FileOutputStream fos = null;

        if (ENCRYPT == encOrDec) {
            fos = new FileOutputStream(fileIn + "_enc");
            ci.init(Cipher.ENCRYPT_MODE, key);
        } else if(DECRYPT == encOrDec) {
            fos = new FileOutputStream(fileIn + "_dec");
            ci.init(Cipher.DECRYPT_MODE, key);
        }

        CipherOutputStream cos = new CipherOutputStream(
                new BufferedOutputStream(fos), ci);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int size = 0;
        while ((size = bis.read(readBuffer, 0, BUFFER_SIZE)) >= 0) {
            cos.write(readBuffer, 0, size);
        }
        fos.flush();
        cos.close();
        bis.close();
    }

    // transfer the byte array of encoded PublicKey to PublicKey
    public static PublicKey byte2PublicKey(byte[] pub) {
        PublicKey publickey = null;

        try {
            KeySpec keySpec = new X509EncodedKeySpec(pub);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publickey = factory.generatePublic(keySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return publickey;
    }

    public void setRandomSeed(long rs) {
        randomSeed = rs;
    }

    @SuppressWarnings("unused")
    private KeyPair getKeyPair() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecureRandom sr = new SecureRandom();
        sr.setSeed(randomSeed);
        keyGen.initialize(1024, sr);
        return keyGen.generateKeyPair();
    }

    public SecretKey getSecretKey(String password) {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        keyGen.init(128, new SecureRandom(password.getBytes()));
        return keyGen.generateKey();
    }
}
