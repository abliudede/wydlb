package com.lianzai.reader.bean;

public class GetReceiveFlagBean {


    /**
     * code : 0
     * msg : success
     * data : {"noticeFlag":false,"taskFlag":0}
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
         * noticeFlag : false
         * taskFlag : 0
         */

        private boolean noticeFlag;
        private int taskFlag;

        public boolean isNoticeFlag() {
            return noticeFlag;
        }

        public void setNoticeFlag(boolean noticeFlag) {
            this.noticeFlag = noticeFlag;
        }

        public int getTaskFlag() {
            return taskFlag;
        }

        public void setTaskFlag(int taskFlag) {
            this.taskFlag = taskFlag;
        }
    }
}
