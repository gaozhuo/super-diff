package com.example.superdiff.utils;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class CryptoUtils {

    public static String md5(String filename) {
        return md5(new File(filename));
    }

    public static String md5(File file) {
        return messageDigest(file, "MD5");
    }

    public static String messageDigest(String filename, String algorithm) {
        return messageDigest(new File(filename), algorithm);
    }

    public static String messageDigest(File file, String algorithm) {
        DigestInputStream dis = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), messageDigest);
            final byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) ;
            return bytesToHex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
