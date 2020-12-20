package com.wydlb.first.api.support;

/**
 * @author qicheng.qing
 * @description
 * @create 17/4/13,14:15
 */
public class ResultException extends RuntimeException {

    String message;

    public ResultException(String msg) {
        super(msg);
        this.message=msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
