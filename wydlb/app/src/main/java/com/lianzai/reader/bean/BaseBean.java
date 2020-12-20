package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2017/10/20.
 */

public class BaseBean implements Serializable {
    private int code;

    private int status;

    private String message="";


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
}
