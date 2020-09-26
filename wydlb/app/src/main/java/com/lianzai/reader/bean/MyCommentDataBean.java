package com.lianzai.reader.bean;

import java.util.List;

public class MyCommentDataBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":4,"list":[{"id":13,"type":1,"platformId":459003,"platformName":"樱木子小说","postId":485173,"commentId":null,"postType":30,"postContent":"这是一条带有黄图的动态","picturesShow":"https://static.lianzai.com/static/violatePlaceholder.png","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"兔兔我做最做最我1","createTime":1553052239000},{"id":12,"type":1,"platformId":459003,"platformName":"樱木子小说","postId":485173,"commentId":null,"postType":30,"postContent":"这是一条带有黄图的动态","picturesShow":"https://static.lianzai.com/static/violatePlaceholder.png","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"qutututututututututututuj图兔兔8啦咯啦咯心咯我小李子1","createTime":1552981851000},{"id":11,"type":1,"platformId":459414,"platformName":"古诗词","postId":691391,"commentId":null,"postType":30,"postContent":"故园今日海棠开，梦入江西锦绣堆。<p>万物皆春人独老，一年过社燕方回。<p>似青似白天浓淡，欲堕还飞絮往来。<p>无那风光餐不得，遣诗招入翠琼杯。<p><p>竹边台榭水边亭，不要人随只独行。<p>乍暖柳条无气力，淡晴花影不分明。<p>一番过雨来幽径，无数新禽有喜声。<p>只欠翠纱红映肉，两年寒食负先生。<p><p>故园今日海棠开，梦入江西锦绣堆。<p>万物皆春人独老，一年过社燕方回。<p>似青似白天浓淡，欲堕还飞絮往来。<p>无那风光餐不得，遣诗招入翠琼杯。<p><p>竹边台榭水边亭，不要人随只独行。<p>乍暖柳条无气力，淡晴花影不分明。<p><p><p><p><p><p>一番过雨来幽径，无数新禽有喜声。<p>只欠翠纱红映肉，两年寒食负先生。","picturesShow":"","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"图兔子我也","createTime":1552981659000},{"id":10,"type":1,"platformId":459414,"platformName":"古诗词","postId":718804,"commentId":null,"postType":30,"postContent":"今天天气很好哦🍀🍀🍀🌴🌴","picturesShow":"","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"旅途兔兔","createTime":1552972583000}],"pageNum":1,"pageSize":10,"pages":1,"size":4}
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
         * total : 4
         * list : [{"id":13,"type":1,"platformId":459003,"platformName":"樱木子小说","postId":485173,"commentId":null,"postType":30,"postContent":"这是一条带有黄图的动态","picturesShow":"https://static.lianzai.com/static/violatePlaceholder.png","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"兔兔我做最做最我1","createTime":1553052239000},{"id":12,"type":1,"platformId":459003,"platformName":"樱木子小说","postId":485173,"commentId":null,"postType":30,"postContent":"这是一条带有黄图的动态","picturesShow":"https://static.lianzai.com/static/violatePlaceholder.png","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"qutututututututututututuj图兔兔8啦咯啦咯心咯我小李子1","createTime":1552981851000},{"id":11,"type":1,"platformId":459414,"platformName":"古诗词","postId":691391,"commentId":null,"postType":30,"postContent":"故园今日海棠开，梦入江西锦绣堆。<p>万物皆春人独老，一年过社燕方回。<p>似青似白天浓淡，欲堕还飞絮往来。<p>无那风光餐不得，遣诗招入翠琼杯。<p><p>竹边台榭水边亭，不要人随只独行。<p>乍暖柳条无气力，淡晴花影不分明。<p>一番过雨来幽径，无数新禽有喜声。<p>只欠翠纱红映肉，两年寒食负先生。<p><p>故园今日海棠开，梦入江西锦绣堆。<p>万物皆春人独老，一年过社燕方回。<p>似青似白天浓淡，欲堕还飞絮往来。<p>无那风光餐不得，遣诗招入翠琼杯。<p><p>竹边台榭水边亭，不要人随只独行。<p>乍暖柳条无气力，淡晴花影不分明。<p><p><p><p><p><p>一番过雨来幽径，无数新禽有喜声。<p>只欠翠纱红映肉，两年寒食负先生。","picturesShow":"","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"图兔子我也","createTime":1552981659000},{"id":10,"type":1,"platformId":459414,"platformName":"古诗词","postId":718804,"commentId":null,"postType":30,"postContent":"今天天气很好哦🍀🍀🍀🌴🌴","picturesShow":"","comUserId":476370,"comUserName":"你自信给你洗影子明嘻嘻嘻太嗯一","comUsrePic":null,"contentShow":"旅途兔兔","createTime":1552972583000}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 4
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> getList() {
            return list;
        }

        public void setList(List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> list) {
            this.list = list;
        }

    }
}
