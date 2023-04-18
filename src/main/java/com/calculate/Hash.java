package com.calculate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {
    /**
     * hash 加密
     */
    public static byte[] hash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
    /**
     * 转换16进制
     */
    public static String hexString(byte[] digest) {
        String hex = String.format( "%064x", new BigInteger( 1, digest ) );
        return hex;
    }

    /**
     * 合并传入多个数据，并调用hash()生成哈希值
     */
    public static byte[] hash(byte[]... bytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for (byte[] bbb : bytes) {
            try {
                outputStream.write(bbb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hash(outputStream.toByteArray());
    }
}
