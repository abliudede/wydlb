package com.lianzai.reader.bean;

import java.util.List;

public class BookShortAgeArtifact {

    /**
     * code : 0
     * data : [{"bookCover":"string","bookId":0,"bookName":"string","recommendBookName":["string"]}]
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
         * bookCover : string
         * bookId : 0
         * bookName : string
         * recommendBookName : ["string"]
         */

        private String bookCover;
        private int bookId;
        private String bookName;
        private String msg;
        private List<String> recommendBookName;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getBookCover() {
            return bookCover;
        }

        public void setBookCover(String bookCover) {
            this.bookCover = bookCover;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public List<String> getRecommendBookName() {
            return recommendBookName;
        }

        public void setRecommendBookName(List<String> recommendBookName) {
            this.recommendBookName = recommendBookName;
        }
    }
}
