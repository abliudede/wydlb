package com.wydlb.first.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: FeedBackTypeResponse
 * Author: lrz
 * Date: 2018/11/14 16:58
 * Description: ${DESCRIPTION}反馈类型
 */
public class FeedBackTypeResponse extends  BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 问题反馈
         */

        private int id;
        private String name;

        private int required;

        public int getRequired() {
            return required;
        }

        public void setRequired(int required) {
            this.required = required;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
