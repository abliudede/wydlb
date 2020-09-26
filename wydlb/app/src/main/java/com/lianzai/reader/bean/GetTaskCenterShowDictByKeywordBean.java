package com.lianzai.reader.bean;

import java.util.List;

public class GetTaskCenterShowDictByKeywordBean {


    /**
     * code : 0
     * data : [{"confName":"string","groupCode":"string","groupName":"string","keyword":0,"value":"string"}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * confName : string
         * groupCode : string
         * groupName : string
         * keyword : 0
         * value : string
         */

        private String confName;
        private String groupCode;
        private String groupName;
        private int keyword;
        private String value;

        public String getConfName() {
            return confName;
        }

        public void setConfName(String confName) {
            this.confName = confName;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getKeyword() {
            return keyword;
        }

        public void setKeyword(int keyword) {
            this.keyword = keyword;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
