package com.lianzai.reader.bean;

public class UserDailyTicket {


    /**
     * code : 0
     * data : {"amt":0,"totalAmt":0,"useAmt":0,"userId":0}
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
         * amt : 0
         * totalAmt : 0
         * useAmt : 0
         * userId : 0
         */

        private int amt;
        private int totalAmt;
        private int useAmt;
        private int userId;

        public int getAmt() {
            return amt;
        }

        public void setAmt(int amt) {
            this.amt = amt;
        }

        public int getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(int totalAmt) {
            this.totalAmt = totalAmt;
        }

        public int getUseAmt() {
            return useAmt;
        }

        public void setUseAmt(int useAmt) {
            this.useAmt = useAmt;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
