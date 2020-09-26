package com.lianzai.reader.bean;

public class GetCommonShareUrlBean {


    /**
     * code : 0
     * data : {"contentVal":"string","headVal":"string","id":0,"oneUrlVal":"string","remark":"string","titleVal":"string","twoUrlVal":"string"}
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
         * contentVal : string
         * headVal : string
         * id : 0
         * oneUrlVal : string
         * remark : string
         * titleVal : string
         * twoUrlVal : string
         */

        private String contentVal;
        private String headVal;
        private int id;
        private String oneUrlVal;
        private String remark;
        private String titleVal;
        private String twoUrlVal;

        public String getContentVal() {
            return contentVal;
        }

        public void setContentVal(String contentVal) {
            this.contentVal = contentVal;
        }

        public String getHeadVal() {
            return headVal;
        }

        public void setHeadVal(String headVal) {
            this.headVal = headVal;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOneUrlVal() {
            return oneUrlVal;
        }

        public void setOneUrlVal(String oneUrlVal) {
            this.oneUrlVal = oneUrlVal;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitleVal() {
            return titleVal;
        }

        public void setTitleVal(String titleVal) {
            this.titleVal = titleVal;
        }

        public String getTwoUrlVal() {
            return twoUrlVal;
        }

        public void setTwoUrlVal(String twoUrlVal) {
            this.twoUrlVal = twoUrlVal;
        }
    }
}
