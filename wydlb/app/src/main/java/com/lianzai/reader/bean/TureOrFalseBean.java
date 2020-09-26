package com.lianzai.reader.bean;

public class TureOrFalseBean {


    /**
     * code : 0
     * msg : success
     * data : {"flag":true,"url":"https://h5.dev.lianzai.com/treasureBox/#/"}
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
         * flag : true
         * url : https://h5.dev.lianzai.com/treasureBox/#/
         */

        private boolean flag;
        private String url;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
