package com.wydlb.first.bean;

import java.util.List;

public class UrlKeyValueBean {


    private List<DataBean> list;

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public static class DataBean{
        private String url;
        private String title;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}
