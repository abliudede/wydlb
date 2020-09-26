package com.lianzai.reader.bean;

public class GetReadingUrlBean {


    /**
     * code : 0
     * msg : success
     * data : {"chapter_url":"https://www.7dsw.com/book/36/36306/18524102.html"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * chapter_url : https://www.7dsw.com/book/36/36306/18524102.html
         */

        private String chapter_url;

        public String getChapter_url() {
            return chapter_url;
        }

        public void setChapter_url(String chapter_url) {
            this.chapter_url = chapter_url;
        }
    }
}
