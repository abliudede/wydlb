package com.lianzai.reader.bean;

import java.util.List;

public class GetBookRewardDetailBean {


    /**
     * code : 0
     * data : [{"amt":0,"avatar":"string","balanceType":0,"nickName":"string","type":0,"userId":0}]
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
         * amt : 0
         * avatar : string
         * balanceType : 0
         * nickName : string
         * type : 0
         * userId : 0
         */

        private double amt;
        private String avatar;
        private int balanceType;
        private String nickName;
        private int type;
        private int userId;
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getAmt() {
            return amt;
        }

        public void setAmt(double amt) {
            this.amt = amt;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(int balanceType) {
            this.balanceType = balanceType;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
