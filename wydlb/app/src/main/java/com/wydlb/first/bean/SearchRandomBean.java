package com.wydlb.first.bean;

public class SearchRandomBean {


    /**
     * code : 0
     * msg : success
     * data : {"isSkipSearchUrl":false,"skipSearchUrl":null}
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
         * isSkipSearchUrl : false
         * skipSearchUrl : null
         */

        private boolean isSkipSearchUrl;
        private String skipSearchUrl;

        public boolean isIsSkipSearchUrl() {
            return isSkipSearchUrl;
        }

        public void setIsSkipSearchUrl(boolean isSkipSearchUrl) {
            this.isSkipSearchUrl = isSkipSearchUrl;
        }

        public String getSkipSearchUrl() {
            return skipSearchUrl;
        }

        public void setSkipSearchUrl(String skipSearchUrl) {
            this.skipSearchUrl = skipSearchUrl;
        }
    }
}
