package com.lianzai.reader.bean;

import java.util.List;

public class TeamChatShowPersonBean {


    /**
     * code : 0
     * data : [{"head":"string","nickName":"string","roleType":0}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<TeamChatInfoBean.DataBean.UserListBean> data;

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

    public List<TeamChatInfoBean.DataBean.UserListBean> getData() {
        return data;
    }

    public void setData(List<TeamChatInfoBean.DataBean.UserListBean> data) {
        this.data = data;
    }


}
