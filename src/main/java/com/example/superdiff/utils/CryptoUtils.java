package com.example.superdiff.utils;

import com.example.superdiff.exception.CryptoException;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class CryptoUtils {

    public static String md5(String filename) throws CryptoException {
        return md5(new File(filename));
    }

    public static String md5(File file) throws CryptoException {
        return messageDigest(file, "MD5");
    }

    public static String messageDigest(String filename, String algorithm) throws CryptoException {
        return messageDigest(new File(filename), algorithm);
    }

    public static String messageDigest(File file, String algorithm) throws CryptoException {
        DigestInputStream dis = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), messageDigest);
            final byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) ;
            return bytesToHex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CryptoException(e);
        } finally {
            FileUtils.closeStream(dis);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final String HEX = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            sb.append(HEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

}
