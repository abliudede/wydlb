package com.lianzai.reader.bean;

public class AttentionBean {


    /**
     * code : 0
     * data : 0
     * msg : string
     */

    private int code;
    private UserAttentionBean.DataBean.ListBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserAttentionBean.DataBean.ListBean getData() {
        return data;
    }

    public void setData(UserAttentionBean.DataBean.ListBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
