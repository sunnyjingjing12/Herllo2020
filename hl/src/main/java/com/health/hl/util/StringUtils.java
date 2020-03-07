package com.health.hl.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by xavi
 * 2014/6/12.
 */
public class StringUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0 || string.trim().equals("null");
    }

    public static boolean notEmpty(String string) {
        return !isEmpty(string);
    }

    public static String makeSHA1(String strings) {

        if (StringUtils.isEmpty(strings))
            return null;
        //MD5加密，32位
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = strings.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


}
