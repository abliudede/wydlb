package com.lianzai.reader.bean;

public class GetUserPersonalStatisticsBean {


    /**
     * code : 0
     * data : {"attentionSum":0,"bookListSum":0,"fansSum":0,"readTime":0}
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
         * attentionSum : 0
         * bookListSum : 0
         * fansSum : 0
         * readTime : 0
         */

        private int attentionSum;
        private int bookListSum;
        private int fansSum;
        private int readTime;

        public int getAttentionSum() {
            return attentionSum;
        }

        public void setAttentionSum(int attentionSum) {
            this.attentionSum = attentionSum;
        }

        public int getBookListSum() {
            return bookListSum;
        }

        public void setBookListSum(int bookListSum) {
            this.bookListSum = bookListSum;
        }

        public int getFansSum() {
            return fansSum;
        }

        public void setFansSum(int fansSum) {
            this.fansSum = fansSum;
        }

        public int getReadTime() {
            return readTime;
        }

        public void setReadTime(int readTime) {
            this.readTime = readTime;
        }
    }
}
