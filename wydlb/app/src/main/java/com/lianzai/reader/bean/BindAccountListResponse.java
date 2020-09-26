package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/2/26.
 */

public class BindAccountListResponse extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mobile : 6666
         * bind : false
         * uid : 476194
         * type : 1
         */

        private String mobile;
        private boolean bind;
        private int uid;
        private int type;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isBind() {
            return bind;
        }

        public void setBind(boolean bind) {
            this.bind = bind;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
