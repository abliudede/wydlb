package com.lianzai.reader.bean;

import java.util.List;

public class GetBookRewardConfigsBean {

    /**
     * code : 0
     * data : [{"amt":0,"balanceType":0,"code":"string","icon":"string","name":"string"}]
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
         * balanceType : 0
         * code : string
         * icon : string
         * name : string
         */

        private double amt;
        private int balanceType;
        private String code;
        private String icon;
        private String name;

        public double getAmt() {
            return amt;
        }

        public void setAmt(double amt) {
            this.amt = amt;
        }

        public int getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(int balanceType) {
            this.balanceType = balanceType;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
