package com.wydlb.first.bean;

/**
 * Copyright (C), 2018
 * FileName: CanSpeakResponse
 * Author: lrz
 * Date: 2018/9/21 16:50
 * Description: ${DESCRIPTION}
 */
public class CanSpeakResponse extends BaseBean {


    /**
     * data : {"canSpeak":true,"msg":"string"}
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
         * canSpeak : true
         * msg : string
         */

        private boolean canSpeak;
        private String msg;
        private boolean luckGameEnable;
        private String operateMsg;

        public boolean isLuckGameEnable() {
            return luckGameEnable;
        }

        public void setLuckGameEnable(boolean luckGameEnable) {
            this.luckGameEnable = luckGameEnable;
        }

        public String getOperateMsg() {
            return operateMsg;
        }

        public void setOperateMsg(String operateMsg) {
            this.operateMsg = operateMsg;
        }

        public boolean isCanSpeak() {
            return canSpeak;
        }

        public void setCanSpeak(boolean canSpeak) {
            this.canSpeak = canSpeak;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
