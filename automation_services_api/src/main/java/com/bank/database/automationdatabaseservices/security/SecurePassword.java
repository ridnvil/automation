package com.bank.database.automationdatabaseservices.security;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurePassword {

    public static String getPassword(String password, String method){
        String passwordSecure = "";
        byte[] salt = new byte[5];

        if(method.equals("bcrypt")){
            passwordSecure = bcrypt(password);
        }else if(method.equals("SHA256")){
            passwordSecure = generateSHA256(password, salt);
        }else {
            passwordSecure = "unknown";
        }

        return passwordSecure;
    }

    public static boolean checkMatch(String password, String passwordencrupted){
        return BCrypt.checkpw(password, passwordencrupted);
    }

    public static String bcrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static String generateSHA256(String password, byte[] salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
