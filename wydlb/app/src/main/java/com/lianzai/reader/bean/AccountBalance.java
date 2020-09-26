package com.lianzai.reader.bean;

/**
 * Created by lrz on 2018/4/20.
 */

public class AccountBalance extends BaseBean {


    /**
     * data : {"balanceAmt":200.12,"lockAmt":0,"freezeAmt":0,"totalAmt":200.12}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * balanceAmt : 200.12
         * lockAmt : 0.0
         * freezeAmt : 0.0
         * totalAmt : 200.12
         */

        private Double balanceAmt;
        private Double lockAmt;
        private Double freezeAmt;
        private Double totalAmt;

        public Double getBalanceAmt() {
            return balanceAmt;
        }

        public void setBalanceAmt(Double balanceAmt) {
            this.balanceAmt = balanceAmt;
        }

        public Double getLockAmt() {
            return lockAmt;
        }

        public void setLockAmt(Double lockAmt) {
            this.lockAmt = lockAmt;
        }

        public Double getFreezeAmt() {
            return freezeAmt;
        }

        public void setFreezeAmt(Double freezeAmt) {
            this.freezeAmt = freezeAmt;
        }

        public Double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(Double totalAmt) {
            this.totalAmt = totalAmt;
        }
    }
}
