package com.lianzai.reader.bean;

public class BaseCountBean {


    /**
     * code : 0
     * msg : success
     * data : {"noticeFlag":false,"taskFlag":0}
     */

    private int code;
    private String msg;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
