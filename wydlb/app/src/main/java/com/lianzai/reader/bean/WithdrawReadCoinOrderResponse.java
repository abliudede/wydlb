package com.lianzai.reader.bean;

public class WithdrawReadCoinOrderResponse extends  BaseBean{


    /**
     * data : {"address":"string","exchangeDesc":"string","exchangeRatio":0,"rabbitAccount":"string"}
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
         * address : string
         * exchangeDesc : string
         * exchangeRatio : 0
         * rabbitAccount : string
         */

        private String address;
        private String exchangeDesc;
        private int exchangeRatio;
        private String rabbitAccount;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getExchangeDesc() {
            return exchangeDesc;
        }

        public void setExchangeDesc(String exchangeDesc) {
            this.exchangeDesc = exchangeDesc;
        }

        public int getExchangeRatio() {
            return exchangeRatio;
        }

        public void setExchangeRatio(int exchangeRatio) {
            this.exchangeRatio = exchangeRatio;
        }

        public String getRabbitAccount() {
            return rabbitAccount;
        }

        public void setRabbitAccount(String rabbitAccount) {
            this.rabbitAccount = rabbitAccount;
        }
    }
}
