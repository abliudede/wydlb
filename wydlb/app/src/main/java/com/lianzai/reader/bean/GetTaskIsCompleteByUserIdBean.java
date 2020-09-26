package com.lianzai.reader.bean;

public class GetTaskIsCompleteByUserIdBean {


    /**
     * code : 0
     * data : {"completeId":0,"conditionType":0,"dailyTicket":0,"goldCoin":0,"goldDrill":0,"headImg":"string","nickName":"string","reads":0,"status":0,"userId":0,"voucher":0}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * completeId : 0
         * conditionType : 0
         * dailyTicket : 0
         * goldCoin : 0
         * goldDrill : 0
         * headImg : string
         * nickName : string
         * reads : 0
         * status : 0
         * userId : 0
         * voucher : 0
         */

        private int completeId;
        private int conditionType;
        private int dailyTicket;
        private int goldCoin;
        private int goldDrill;
        private String headImg;
        private String nickName;
        private int reads;
        private int status;
        private int userId;
        private int voucher;

        public int getCompleteId() {
            return completeId;
        }

        public void setCompleteId(int completeId) {
            this.completeId = completeId;
        }

        public int getConditionType() {
            return conditionType;
        }

        public void setConditionType(int conditionType) {
            this.conditionType = conditionType;
        }

        public int getDailyTicket() {
            return dailyTicket;
        }

        public void setDailyTicket(int dailyTicket) {
            this.dailyTicket = dailyTicket;
        }

        public int getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(int goldCoin) {
            this.goldCoin = goldCoin;
        }

        public int getGoldDrill() {
            return goldDrill;
        }

        public void setGoldDrill(int goldDrill) {
            this.goldDrill = goldDrill;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getReads() {
            return reads;
        }

        public void setReads(int reads) {
            this.reads = reads;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getVoucher() {
            return voucher;
        }

        public void setVoucher(int voucher) {
            this.voucher = voucher;
        }
    }
}
