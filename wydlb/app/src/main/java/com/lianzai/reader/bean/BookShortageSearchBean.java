package com.lianzai.reader.bean;

import java.util.List;

public class BookShortageSearchBean {


    /**
     * code : 0
     * data : [{"autherName":"string","bookId":0,"bookInfo":"string","bookName":"string","cover":"string","platformId":0}]
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
         * autherName : string
         * bookId : 0
         * bookInfo : string
         * bookName : string
         * cover : string
         * platformId : 0
         */

        private String autherName;
        private int bookId;
        private String bookInfo;
        private String bookName;
        private String cover;
        private int platformId;
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAutherName() {
            return autherName;
        }

        public void setAutherName(String autherName) {
            this.autherName = autherName;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(String bookInfo) {
            this.bookInfo = bookInfo;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }
    }
}
