package com.wydlb.first.bean;

public class SearchBean {

    /**
     * code : 0
     * data : {"bookIds":"string","platformId":0,"type":0}
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
         * bookIds : string
         * platformId : 0
         * type : 0
         */

        private String bookIds;
        private int platformId;
        private int type;

        public String getBookIds() {
            return bookIds;
        }

        public void setBookIds(String bookIds) {
            this.bookIds = bookIds;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
