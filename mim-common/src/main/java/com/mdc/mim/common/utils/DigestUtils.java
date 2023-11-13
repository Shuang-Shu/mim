package com.mdc.mim.common.utils;

import java.security.MessageDigest;

public class DigestUtils {
    private static final String MD5 = "MD5";

    public static String md5(String pwd) {
        MessageDigest md = null;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance(MD5);
            var digest = md.digest(pwd.getBytes());
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
