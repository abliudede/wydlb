package com.wydlb.first.bean;

/**
 * Copyright (C), 2018
 * FileName: HobbySettingResponse
 * Author: lrz
 * Date: 2018/10/23 14:26
 * Description: ${DESCRIPTION}
 */
public class HobbySettingResponse {

    /**
     * code : 0
     * msg : success
     * data : 1;10,11,15,237,241,250,254,258
     */

    private int code;
    private String msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
