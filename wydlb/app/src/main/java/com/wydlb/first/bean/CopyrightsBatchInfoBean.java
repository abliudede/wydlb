package com.wydlb.first.bean;

import java.util.List;

public class CopyrightsBatchInfoBean {


    /**
     * code : 0
     * msg : success
     * data : [60]
     */

    private int code;
    private String msg;
    private List<Integer> data;

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

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
