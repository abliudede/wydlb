package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/4/21.
 */

public class AccountBalanceList {


    /**
     * code : 0
     * msg : success
     * data : [{"balance":300,"balanceType":1,"balanceName":"金币余额","userId":476305},{"balance":0,"balanceType":2,"balanceName":"矿晶余额","userId":476305},{"balance":200,"balanceType":3,"balanceName":"阅点余额","userId":476305},{"balance":0,"balanceType":4,"balanceName":"阅券余额","userId":476305}]
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
         * balance : 300
         * balanceType : 1
         * balanceName : 金币余额
         * userId : 476305
         */

        private Double totalAmt;
        private Double balanceAmt;
        private Double lockAmt;
        private int balanceType;
        private String balanceName;
        private int userId;

        public Double getBalanceAmt() {
            return balanceAmt;
        }

        public Double getTotalAmt() {
            return totalAmt;
        }

        public Double getLockAmt() {
            return lockAmt;
        }

        public void setLockAmt(Double lockAmt) {
            this.lockAmt = lockAmt;
        }

        public void setTotalAmt(Double totalAmt) {
            this.totalAmt = totalAmt;
        }

        public void setBalanceAmt(Double balanceAmt) {
            this.balanceAmt = balanceAmt;
        }

        public int getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(int balanceType) {
            this.balanceType = balanceType;
        }

        public String getBalanceName() {
            return balanceName;
        }

        public void setBalanceName(String balanceName) {
            this.balanceName = balanceName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
