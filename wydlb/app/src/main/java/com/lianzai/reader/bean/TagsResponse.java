package com.lianzai.reader.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: TagsResponse
 * Author: lrz
 * Date: 2018/10/18 15:08
 * Description: ${DESCRIPTION}
 */
public class TagsResponse {

    /**
     * code : 0
     * msg : success
     * data : [{"id":10,"name":"冷酷"},{"id":268,"name":"耽美"}]
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
         * id : 10
         * name : 冷酷
         */

        private int id;
        private String name;

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
