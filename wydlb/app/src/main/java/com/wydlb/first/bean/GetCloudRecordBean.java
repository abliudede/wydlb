package com.wydlb.first.bean;

import com.wydlb.first.model.bean.CloudRecordBean;

import java.util.List;

public class GetCloudRecordBean {


    /**
     * code : 0
     * data : [{"bookId":0,"chapterId":0,"createdAt":"2018-12-12T03:24:09.721Z","id":0,"updatedAt":"2018-12-12T03:24:09.721Z","userId":0}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<CloudRecordBean> data;

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

    public List<CloudRecordBean> getData() {
        return data;
    }

    public void setData(List<CloudRecordBean> data) {
        this.data = data;
    }


}
