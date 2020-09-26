package com.lianzai.reader.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lrz on 2018/4/26.
 */

public class GateWayResponse {


    /**
     * message : null
     * status : 0
     * resultCode : 0
     * payInfo : {"package":"Sign=WXPay","appid":"wx4e88f135a2279543","sign":"BB83E1DE69867D117DB9A2EC623C6857","partnerid":"1503740051","prepayid":"wx17181105717291f21ade27423678740980","noncestr":"d319e74378a54be4ad4243a07a30d322","timestamp":"1526551865"}
     */

    private String message;
    private String status;
    private String resultCode;
    private PayInfoBean payInfo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }

    public static class PayInfoBean {
        /**
         * package : Sign=WXPay
         * appid : wx4e88f135a2279543
         * sign : BB83E1DE69867D117DB9A2EC623C6857
         * partnerid : 1503740051
         * prepayid : wx17181105717291f21ade27423678740980
         * noncestr : d319e74378a54be4ad4243a07a30d322
         * timestamp : 1526551865
         */

        @SerializedName("package")
        private String packageX;
        private String appid;
        private String sign;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
