package com.lianzai.reader.bean;

import java.util.List;

public class ListenDetailBean {


    /**
     * code : 0
     * data : {"head":"string","nickName":"string","readDayList":["string"],"remainSecond":0}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * head : string
         * nickName : string
         * readDayList : ["string"]
         * remainSecond : 0
         */

        private String head;
        private String nickName;
        private long remainSecond;
        private List<String> readDayList;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getRemainSecond() {
            return remainSecond;
        }

        public void setRemainSecond(long remainSecond) {
            this.remainSecond = remainSecond;
        }

        public List<String> getReadDayList() {
            return readDayList;
        }

        public void setReadDayList(List<String> readDayList) {
            this.readDayList = readDayList;
        }
    }
}
