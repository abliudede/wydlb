package com.lianzai.reader.bean;

/**
 * Created by lrz on 2018/4/26.
 */

public class WalletBindAccountResponse {


    /**
     * code : 0
     * msg : success
     * data : {"openId":"oUgVH1GGE_Cf266Oo22cmK-TkKFQ","thirdType":1,"nickName":"hh"}
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
         * openId : oUgVH1GGE_Cf266Oo22cmK-TkKFQ
         * thirdType : 1
         * nickName : hh
         */

        private String openId;
        private int thirdType;
        private String nickName;

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public int getThirdType() {
            return thirdType;
        }

        public void setThirdType(int thirdType) {
            this.thirdType = thirdType;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
