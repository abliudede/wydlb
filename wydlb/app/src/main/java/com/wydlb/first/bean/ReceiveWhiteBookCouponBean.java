package com.wydlb.first.bean;

import java.util.List;

public class ReceiveWhiteBookCouponBean {


    /**
     * code : 0
     * msg : success
     * data : {"isSuccess":false,"showText":"该功能暂时还未开放","balances":[{"amt":1,"balanceType":1,"title":"金币标题","subtitle":"金币副标题","icon":"icon2"},{"amt":3,"balanceType":3,"title":"阅点标题","subtitle":"阅点副标题","icon":null}]}
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
         * isSuccess : false
         * showText : 该功能暂时还未开放
         * balances : [{"amt":1,"balanceType":1,"title":"金币标题","subtitle":"金币副标题","icon":"icon2"},{"amt":3,"balanceType":3,"title":"阅点标题","subtitle":"阅点副标题","icon":null}]
         */

        private boolean isSuccess;
        private String showText;
        private List<BalancesBean> balances;

        public boolean isIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getShowText() {
            return showText;
        }

        public void setShowText(String showText) {
            this.showText = showText;
        }

        public List<BalancesBean> getBalances() {
            return balances;
        }

        public void setBalances(List<BalancesBean> balances) {
            this.balances = balances;
        }

        public static class BalancesBean {
            /**
             * amt : 1
             * balanceType : 1
             * title : 金币标题
             * subtitle : 金币副标题
             * icon : icon2
             */

            private int amt;
            private int balanceType;
            private String title;
            private String subtitle;
            private String icon;

            public int getAmt() {
                return amt;
            }

            public void setAmt(int amt) {
                this.amt = amt;
            }

            public int getBalanceType() {
                return balanceType;
            }

            public void setBalanceType(int balanceType) {
                this.balanceType = balanceType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
