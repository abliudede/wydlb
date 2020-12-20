package com.wydlb.first.bean;

import java.util.List;

public class ReceiveBean {


    /**
     * code : 0
     * msg : success
     * data : {"userId":476370,"rewardType":21014,"taskId":18,"rewardDetailList":[{"rewardType":20000,"sendType":null,"rewardAmount":38}]}
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
         * userId : 476370
         * rewardType : 21014
         * taskId : 18
         * rewardDetailList : [{"rewardType":20000,"sendType":null,"rewardAmount":38}]
         */

        private int userId;
        private int rewardType;
        private int taskId;
        private List<RewardDetailListBean> rewardDetailList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getRewardType() {
            return rewardType;
        }

        public void setRewardType(int rewardType) {
            this.rewardType = rewardType;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public List<RewardDetailListBean> getRewardDetailList() {
            return rewardDetailList;
        }

        public void setRewardDetailList(List<RewardDetailListBean> rewardDetailList) {
            this.rewardDetailList = rewardDetailList;
        }

        public static class RewardDetailListBean {
            /**
             * rewardType : 20000
             * sendType : null
             * rewardAmount : 38.0
             */

            private int rewardType;
            private Object sendType;
            private double rewardAmount;

            public int getRewardType() {
                return rewardType;
            }

            public void setRewardType(int rewardType) {
                this.rewardType = rewardType;
            }

            public Object getSendType() {
                return sendType;
            }

            public void setSendType(Object sendType) {
                this.sendType = sendType;
            }

            public double getRewardAmount() {
                return rewardAmount;
            }

            public void setRewardAmount(double rewardAmount) {
                this.rewardAmount = rewardAmount;
            }
        }
    }
}
