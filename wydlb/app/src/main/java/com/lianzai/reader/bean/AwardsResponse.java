package com.lianzai.reader.bean;

/**
 * Copyright (C), 2018
 * FileName: AwardsResponse
 * Author: lrz
 * Date: 2018/10/22 10:55
 * Description: ${DESCRIPTION}
 */
public class AwardsResponse {

    /**
     * code : 0
     * data : {"award":0,"isAward":true,"tip":"string"}
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
         * award : 0
         * isAward : true
         * tip : string
         */

        private Double award;
        private boolean isAward;
        private String tip;

        public Double getAward() {
            return award;
        }

        public void setAward(Double award) {
            this.award = award;
        }

        public boolean isAward() {
            return isAward;
        }

        public void setAward(boolean award) {
            isAward = award;
        }

        public boolean isIsAward() {
            return isAward;
        }

        public void setIsAward(boolean isAward) {
            this.isAward = isAward;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }
}
