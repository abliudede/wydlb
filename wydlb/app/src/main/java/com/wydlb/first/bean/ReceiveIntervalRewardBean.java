package com.wydlb.first.bean;

public class ReceiveIntervalRewardBean {


    /**
     * code : 0
     * data : {"remarks":"string","rewardMsg":"string","rewardNum":0,"rewardType":0}
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
         * remarks : string
         * rewardMsg : string
         * rewardNum : 0
         * rewardType : 0
         */

        private String remarks;
        private String rewardMsg;
        private int rewardNum;
        private int rewardType;

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getRewardMsg() {
            return rewardMsg;
        }

        public void setRewardMsg(String rewardMsg) {
            this.rewardMsg = rewardMsg;
        }

        public int getRewardNum() {
            return rewardNum;
        }

        public void setRewardNum(int rewardNum) {
            this.rewardNum = rewardNum;
        }

        public int getRewardType() {
            return rewardType;
        }

        public void setRewardType(int rewardType) {
            this.rewardType = rewardType;
        }
    }
}
