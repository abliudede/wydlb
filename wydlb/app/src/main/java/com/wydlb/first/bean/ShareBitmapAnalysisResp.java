package com.wydlb.first.bean;

public class ShareBitmapAnalysisResp {


    /**
     * code : 0
     * data : {"objId":0,"type":0,"userId":0}
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
         * objId : 0
         * type : 0
         * userId : 0
         */

        private int objId;
        private int type;
        private int userId;
        private String ext;

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public int getObjId() {
            return objId;
        }

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
