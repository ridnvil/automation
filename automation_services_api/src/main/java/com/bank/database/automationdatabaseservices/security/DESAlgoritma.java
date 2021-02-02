package com.bank.database.automationdatabaseservices.security;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class DESAlgoritma {
    private static Cipher ecipher;
    private static Cipher dcipher;
    private static SecretKey key;

    public static String encryped(String msg) {
        try{
            key = KeyGenerator.getInstance("DES").generateKey();
            ecipher = Cipher.getInstance("DES");

            ecipher.init(Cipher.ENCRYPT_MODE, key);
            return encrypt(msg);
        }catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static String decrypted(String msg) {
        try{
            key = KeyGenerator.getInstance("DES").generateKey();
            dcipher = Cipher.getInstance("DES");

            dcipher.init(Cipher.DECRYPT_MODE, key);
            return decrypt(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
            byte[] enc = ecipher.doFinal(utf8);
            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String decrypt(String str) {
        try {
            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, StandardCharsets.UTF_8);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
