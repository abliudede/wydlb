package com.wydlb.first.bean;

public class WebSocketBean {

    /**
     * data : {"price":"0.68","symbol":"sc-read","chg":"-0.035"}
     * type : latestPrice
     * error : 0
     * msg : Succss
     */

    private DataBean data;
    private String type;
    private int error;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * price : 0.68
         * symbol : sc-read
         * chg : -0.035
         */

        private String price;
        private String symbol;
        private String chg;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getChg() {
            return chg;
        }

        public void setChg(String chg) {
            this.chg = chg;
        }
    }
}
