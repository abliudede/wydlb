package com.wydlb.first.bean;

import java.util.List;

public class UnpasswordDetailBean {


    /**
     * code : 0
     * msg : success
     * data : {"isOpen":false,"list":[{"type":1,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200},{"type":2,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200},{"type":3,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200}]}
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
         * isOpen : false
         * list : [{"type":1,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200},{"type":2,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200},{"type":3,"amountConfig":"0,50,100,200,300,400,500,1000,5000,10000","defaultAmount":200}]
         */

        private boolean isOpen;
        private List<ListBean> list;

        public boolean isIsOpen() {
            return isOpen;
        }

        public void setIsOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * type : 1
             * amountConfig : 0,50,100,200,300,400,500,1000,5000,10000
             * defaultAmount : 200
             */

            private int type;
            private String amountConfig;
            private int defaultAmount;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getAmountConfig() {
                return amountConfig;
            }

            public void setAmountConfig(String amountConfig) {
                this.amountConfig = amountConfig;
            }

            public int getDefaultAmount() {
                return defaultAmount;
            }

            public void setDefaultAmount(int defaultAmount) {
                this.defaultAmount = defaultAmount;
            }
        }
    }
}
