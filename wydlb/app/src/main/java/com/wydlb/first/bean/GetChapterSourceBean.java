package com.wydlb.first.bean;

import java.util.List;

public class GetChapterSourceBean {


    /**
     * code : 0
     * data : [{"chapter_url":"string","source":"string"}]
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
         * chapter_url : string
         * source : string
         */

        private String chapter_url;
        private String source;

        public String getChapter_url() {
            return chapter_url;
        }

        public void setChapter_url(String chapter_url) {
            this.chapter_url = chapter_url;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
