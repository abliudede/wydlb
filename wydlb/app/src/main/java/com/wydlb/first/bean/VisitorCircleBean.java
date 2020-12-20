package com.wydlb.first.bean;

import java.util.List;

public class VisitorCircleBean {


    /**
     * code : 0
     * data : [{"bookId":0,"id":0,"platformCover":"string","platformName":"string","platformType":0}]
     * msg : string
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
         * bookId : 0
         * id : 0
         * platformCover : string
         * platformName : string
         * platformType : 0
         * time : 2019-07-01T01:23:28.286Z
         * title : string
         * url : string
         */

        private int bookId;
        private int id;
        private String platformCover;
        private String platformName;
        private int platformType;
        private String time;
        private String title;
        private String url;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
