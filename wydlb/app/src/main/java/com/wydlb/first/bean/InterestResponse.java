package com.wydlb.first.bean;

import java.util.List;

public class InterestResponse {


    /**
     * code : 0
     * msg : success
     * data : [{"bookId":28,"platformId":3806,"platformName":"诛天荒圣","platformCover":"https://static.lianzai.com/updatecover/28.jpg","penName":null,"platformIntroduce":null,"isUnread":false,"isConcern":false,"yxAccid":"bf5fd7c98fd348f480737651365d3477"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bookId : 28
         * platformId : 3806
         * platformName : 诛天荒圣
         * platformCover : https://static.lianzai.com/updatecover/28.jpg
         * penName : null
         * platformIntroduce : null
         * isUnread : false
         * isConcern : false
         * yxAccid : bf5fd7c98fd348f480737651365d3477
         */

        private int bookId;
        private int platformId;
        private String platformName;
        private String platformCover;
        private String penName;
        private Object platformIntroduce;
        private boolean isUnread;
        private boolean isConcern;
        private String yxAccid;
        private String source;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
        }

        public Object getPlatformIntroduce() {
            return platformIntroduce;
        }

        public void setPlatformIntroduce(Object platformIntroduce) {
            this.platformIntroduce = platformIntroduce;
        }

        public boolean isIsUnread() {
            return isUnread;
        }

        public void setIsUnread(boolean isUnread) {
            this.isUnread = isUnread;
        }

        public boolean isIsConcern() {
            return isConcern;
        }

        public void setIsConcern(boolean isConcern) {
            this.isConcern = isConcern;
        }

        public String getYxAccid() {
            return yxAccid;
        }

        public void setYxAccid(String yxAccid) {
            this.yxAccid = yxAccid;
        }
    }
}
