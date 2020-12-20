package com.wydlb.first.bean;

/**
 * Created by lrz on 2018/4/10.
 */

public class SendSmsResponse extends  BaseBean {
    private boolean data=false;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
