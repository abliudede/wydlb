package com.wydlb.first.bean;

public class FindIntervalRewardBean {

    /**
     * code : 0
     * data : {"isReceive":0,"nextInterval":0,"rewardMsg":"string","rewardNum":0,"rewardType":0,"status":0,"userId":0}
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
         * isReceive : 0
         * nextInterval : 0
         * rewardMsg : string
         * rewardNum : 0
         * rewardType : 0
         * status : 0
         * userId : 0
         */

        private int isReceive;
        private long nextInterval;
        private String rewardMsg;
        private int rewardNum;
        private int rewardType;
        private int status;
        private int userId;

        public int getIsReceive() {
            return isReceive;
        }

        public void setIsReceive(int isReceive) {
            this.isReceive = isReceive;
        }

        public long getNextInterval() {
            return nextInterval;
        }

        public void setNextInterval(long nextInterval) {
            this.nextInterval = nextInterval;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
