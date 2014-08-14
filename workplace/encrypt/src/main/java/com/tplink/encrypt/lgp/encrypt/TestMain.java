package com.tplink.encrypt.lgp.encrypt;

/**
 * Hello world!
 * 
 */
public class TestMain {
    public static void main(String[] args){

//        testEnDecryptString();
        testEnDecryptFile();
        
    }
    
    public static void testEnDecryptFile(){
        CryptTool tool = new CryptTool("AES");
        String fileIn = "d:/JUnit In Action.pdf";
        String password = "123456";
        
        try {
            tool.EncryptFile(fileIn, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to Encrypt file");
            return;
        }
        try {
            tool.DecryptFile(fileIn+"_enc", password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to Decrypt file");
            return;
        }
        
    }
    
    public static void testEnDecryptString(){
        CryptTool tool = new CryptTool("AES");
        String plainText = "Hello World ! Welcome SpectrumLeeee!";
        String password = "123456";
        
        byte[] cipherText = null;
        try {
            cipherText = tool.Encrypt(plainText.getBytes(), password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to Encrypt! ");
            return;
        }
        byte[] decryptText = null;
        try {
            decryptText = tool.Decrypt(cipherText, "123456");
        } catch (Exception e) {
            System.out.println("Failed to Decrypt! ");
            return;
        }
        
        System.out.println(new String(decryptText, 0, decryptText.length));
    }

}