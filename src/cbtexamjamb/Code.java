/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbtexamjamb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 *
 * @author HP
 */
public class Code {
    
    public static String getSecurePassword(String passwordToHash, String salt) throws NoSuchProviderException
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            
//            passwordToHash = "ideal".concat(passwordToHash).concat("programming");
            
            passwordToHash = passwordToHash + salt;
            
            System.out.println(passwordToHash);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
//            md.update(salt);
            //Get the hash's bytes 
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     
//    public static String gen(String passwordToHash, byte[] salt)
//    {
//        String generatedPassword = null;
//        try {
//            // Create MessageDigest instance for MD5
//            
//            passwordToHash = "ideal".concat(passwordToHash).concat("programming");
//            
////            passwordToHash = passwordToHash;
//            
//            System.out.println(passwordToHash);
//            
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            //Add password bytes to digest
//            md.update(salt);
//            //Get the hash's bytes 
//            byte[] bytes = md.digest(passwordToHash.getBytes());
//            //This bytes[] has bytes in decimal format;
//            //Convert it to hexadecimal format
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            //Get complete hashed password in hex format
//            generatedPassword = sb.toString();
//        } 
//        catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return generatedPassword;
//    }
    
//    public static String gen(String passwordToHash)
//    {
//        String generatedPassword = null;
//        try {
//            // Create MessageDigest instance for MD5
//            
//            String spl[] = passwordToHash.split("gNiDoCnE");
//            
//            passwordToHash = "ideal".concat(spl[0]).concat("programming");
//            
////            passwordToHash = passwordToHash;
//            
//            System.out.println(passwordToHash);
//            
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            //Add password bytes to digest
////            md.update(salt);
//            md.update(spl[1].getBytes());
//            //Get the hash's bytes 
//            byte[] bytes = md.digest(passwordToHash.getBytes());
//            //This bytes[] has bytes in decimal format;
//            //Convert it to hexadecimal format
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            //Get complete hashed password in hex format
//            generatedPassword = sb.toString();
//        } 
//        catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return generatedPassword;
//    }
    
    public static String gen(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            
//            String spl[] = passwordToHash.split("gNiDoCnE");
            
            passwordToHash = "ideal".concat(passwordToHash).concat("programming");
            
//            passwordToHash = passwordToHash;
            
//            System.out.println(passwordToHash);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
//            md.update(salt);
//            md.update(spl[1].getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    //Add salt
    public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
    
}
