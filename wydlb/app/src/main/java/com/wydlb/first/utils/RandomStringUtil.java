package com.wydlb.first.utils;

import java.util.Random;

/**
 * Copyright (C), 2018
 * FileName: RandomStringUtil
 * Author: lrz
 * Date: 2018/9/20 14:22
 * Description: ${DESCRIPTION}
 */
public class RandomStringUtil {
    public static final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    /**
     * Generate a random string.
     *
     * @param length the length of the generated string.
     * @return
     */
    public static String generateString(int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = SOURCES.charAt(new Random().nextInt(SOURCES.length()));
        }
        return new String(text);
    }
}
