package com.wydlb.first.bean;

import java.io.Serializable;

public class CheckUpdateResponse implements Serializable{


    /**
     * code : 0
     * msg : success
     * data : {"version":"V2.0.5","type":1,"url":"https://static.lianzai.com/download/lianzai_reader_2.0.5.apk","status":2,"intro":"2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新"}
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
         * version : V2.0.5
         * type : 1
         * url : https://static.lianzai.com/download/lianzai_reader_2.0.5.apk
         * status : 2
         * intro : 2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新2.0.5=普通更新
         */

        private String version;
        private int type;
        private String url;
        private int status;
        private String intro;

        private int versionCode;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }
    }
}
