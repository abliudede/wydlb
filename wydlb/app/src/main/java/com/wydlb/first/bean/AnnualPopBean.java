package com.wydlb.first.bean;

public class AnnualPopBean {


    /**
     * code : 0
     * data : {"isPopup":true,"url":"string"}
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
         * isPopup : true
         * url : string
         */

        private boolean isPopup;
        private String url;

        public boolean isIsPopup() {
            return isPopup;
        }

        public void setIsPopup(boolean isPopup) {
            this.isPopup = isPopup;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
