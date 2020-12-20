package com.wydlb.first.bean;

public class GetOrderByOrderNo {


    /**
     * code : 0
     * msg : success
     * data : {"paymentNo":"LZP1532596567000100082","orderNo":"Android1532596536835885524","transAmt":0.01,"outUid":18050,"payStatus":0,"body":"一键赏"}
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
         * paymentNo : LZP1532596567000100082
         * orderNo : Android1532596536835885524
         * transAmt : 0.01
         * outUid : 18050
         * payStatus : 0
         * body : 一键赏
         */

        private String paymentNo;
        private String orderNo;
        private double transAmt;
        private int outUid;
        private int payStatus;
        private String body;

        public String getPaymentNo() {
            return paymentNo;
        }

        public void setPaymentNo(String paymentNo) {
            this.paymentNo = paymentNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public double getTransAmt() {
            return transAmt;
        }

        public void setTransAmt(double transAmt) {
            this.transAmt = transAmt;
        }

        public int getOutUid() {
            return outUid;
        }

        public void setOutUid(int outUid) {
            this.outUid = outUid;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
