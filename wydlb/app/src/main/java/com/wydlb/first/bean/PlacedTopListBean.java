package com.wydlb.first.bean;

import java.util.List;

public class PlacedTopListBean {


    /**
     * code : 0
     * data : [{"content":"string","postId":0}]
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
         * content : string
         * postId : 0
         */

        private String content;
        private int postId;
        private String urlTitle;//网页解析字符串


        public String getUrlTitle() {
            return urlTitle;
        }

        public void setUrlTitle(String urlTitle) {
            this.urlTitle = urlTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }
    }
}
