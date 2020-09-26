package com.lianzai.reader.bean;

import com.lianzai.reader.model.bean.BookStoreBeanN;

import java.util.List;

public class GetUserPersonalBookListInfoBean {


    /**
     * code : 0
     * data : [{"bookId":0,"categoryName":"string","isConcern":false,"isUnread":false,"penName":"string","platformCover":"string","platformId":0,"platformIntroduce":"string","platformName":"string","platformType":0,"readSecond":0,"shareUrl":"string","source":"string","totalMinute":0,"yxAccid":"string"}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<BookStoreBeanN> data;

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

    public List<BookStoreBeanN> getData() {
        return data;
    }

    public void setData(List<BookStoreBeanN> data) {
        this.data = data;
    }

}
