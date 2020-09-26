package com.lianzai.reader.utils;


import android.util.Base64;


public class RxBase64Util {

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {

        return new String(Base64.decode(inputData,Base64.DEFAULT));
    }

    public static byte[] decodeDataToByte(String inputData) {

        return Base64.decode(inputData,Base64.DEFAULT);
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        return Base64.encodeToString(inputData.getBytes(), Base64.DEFAULT);
    }

    public static String encodeData(byte[] inputData) {
        String data=Base64.encodeToString(inputData, Base64.DEFAULT);
        return data;
    }
}
