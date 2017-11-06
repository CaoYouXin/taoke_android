package com.github.caoyouxin.taoke.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cls on 2017/11/6.
 */

public class StringUtils {
    private static char[] hexTable = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toMD5HexString(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("US-ASCII"));
            return toHexString(md.digest());
        } catch (UnsupportedEncodingException var2) {
            ;
        } catch (NoSuchAlgorithmException var3) {
            ;
        }

        return null;
    }

    public static String toHexString(byte b) {
        char[] chars = new char[2];
        int d = (b & 240) >> 4;
        int m = b & 15;
        chars[0] = hexTable[d];
        chars[1] = hexTable[m];
        return new String(chars);
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            char[] chars = new char[2];
            int d = (bytes[i] & 240) >> 4;
            int m = bytes[i] & 15;
            chars[0] = hexTable[d];
            chars[1] = hexTable[m];
            sb.append(chars);
        }

        return sb.toString();
    }

}
