package com.wydlb.first.bean;

public class BookIsHiddenBean {


    /**
     * code : 0
     * msg : success
     * data : {"platformType":3,"isHidden":true}
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
         * platformType : 3
         * isHidden : true
         */

        private int platformType;
        private boolean isHidden;

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public boolean isIsHidden() {
            return isHidden;
        }

        public void setIsHidden(boolean isHidden) {
            this.isHidden = isHidden;
        }
    }
}
