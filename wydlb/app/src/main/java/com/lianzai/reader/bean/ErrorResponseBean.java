package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * @author qicheng.qing
 * @description
 * @create 17/4/13,14:15
 */
public class ErrorResponseBean implements Serializable {

    protected String message;
    protected int status_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
}