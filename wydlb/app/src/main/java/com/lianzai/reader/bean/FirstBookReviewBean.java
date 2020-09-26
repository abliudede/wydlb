package com.lianzai.reader.bean;

public class FirstBookReviewBean {


    /**
     * code : 0
     * msg : success
     * data : {"firstBookReviewFlag":false,"rewardAmont":68}
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
         * firstBookReviewFlag : false
         * rewardAmont : 68
         */

        private boolean firstBookReviewFlag;
        private int rewardAmont;

        public boolean isFirstBookReviewFlag() {
            return firstBookReviewFlag;
        }

        public void setFirstBookReviewFlag(boolean firstBookReviewFlag) {
            this.firstBookReviewFlag = firstBookReviewFlag;
        }

        public int getRewardAmont() {
            return rewardAmont;
        }

        public void setRewardAmont(int rewardAmont) {
            this.rewardAmont = rewardAmont;
        }
    }
}
