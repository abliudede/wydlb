package com.wydlb.first.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2017/11/29.
 */

public class UnReadCountBean extends BaseBean {

    /**
     * data : {"public_unread_count":0,"private_unread_count":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * public_unread_count : 0
         * private_unread_count : 0
         */

        private int public_unread_count;
        private int private_unread_count;

        public int getPublic_unread_count() {
            return public_unread_count;
        }

        public void setPublic_unread_count(int public_unread_count) {
            this.public_unread_count = public_unread_count;
        }

        public int getPrivate_unread_count() {
            return private_unread_count;
        }

        public void setPrivate_unread_count(int private_unread_count) {
            this.private_unread_count = private_unread_count;
        }
    }
}
