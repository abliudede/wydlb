package com.wydlb.first.utils;

import com.google.gson.Gson;

public class GsonUtil {
    static Gson gson = new Gson();

    public static <T> T getBean(String jsonStr, Class<T> className) {
        try {
            T t = gson.fromJson(jsonStr, className);
            return t;
        } catch (Exception e) {
            RxLogTool.e("failed"+ e.toString());
            return null;
        }
    }

    public static <T> String toJsonString(T object) {
        return gson.toJson(object);
    }

}
