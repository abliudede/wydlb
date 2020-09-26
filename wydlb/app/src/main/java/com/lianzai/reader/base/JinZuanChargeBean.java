package com.lianzai.reader.base;

public class JinZuanChargeBean {


    /**
     * code : 0
     * msg : success
     * data : {"platformId":62200,"userId":476370,"type":4,"objectId":482903,"kdiamCount":2}
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
         * platformId : 62200
         * userId : 476370
         * type : 4
         * objectId : 482903
         * kdiamCount : 2
         */

        private int platformId;
        private int userId;
        private int type;
        private int objectId;
        private int kdiamCount;
        private int approveType;

        public int getApproveType() {
            return approveType;
        }

        public void setApproveType(int approveType) {
            this.approveType = approveType;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }

        public int getKdiamCount() {
            return kdiamCount;
        }

        public void setKdiamCount(int kdiamCount) {
            this.kdiamCount = kdiamCount;
        }
    }
}
