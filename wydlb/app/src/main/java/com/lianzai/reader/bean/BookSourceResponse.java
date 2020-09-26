package com.lianzai.reader.bean;

import java.util.List;

public class BookSourceResponse {


    /**
     * code : 0
     * msg : 信息拉取成功
     * data : {"novel":{"id":30330,"penName":"不如喝水","title":"拳皇之石头门的选择"},"novelSource":[{"originId":1,"outId":"1011153684","url":"https://book.qidian.com/info/1011153684","cover":"http://qidian.qpic.cn/qdbimg/349573/1011153684/300","sourceCname":"起点网站","sourceUrl":"www.qidian.com","sourceName":"qidian"}]}
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
         * novel : {"id":30330,"penName":"不如喝水","title":"拳皇之石头门的选择"}
         * novelSource : [{"originId":1,"outId":"1011153684","url":"https://book.qidian.com/info/1011153684","cover":"http://qidian.qpic.cn/qdbimg/349573/1011153684/300","sourceCname":"起点网站","sourceUrl":"www.qidian.com","sourceName":"qidian"}]
         */

        private NovelBean novel;
        private List<NovelSourceBean> novelSource;

        public NovelBean getNovel() {
            return novel;
        }

        public void setNovel(NovelBean novel) {
            this.novel = novel;
        }

        public List<NovelSourceBean> getNovelSource() {
            return novelSource;
        }

        public void setNovelSource(List<NovelSourceBean> novelSource) {
            this.novelSource = novelSource;
        }

        public static class NovelBean {
            /**
             * id : 30330
             * penName : 不如喝水
             * title : 拳皇之石头门的选择
             */

            private int id;
            private String penName;
            private String title;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPenName() {
                return penName;
            }

            public void setPenName(String penName) {
                this.penName = penName;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class NovelSourceBean {
            /**
             * originId : 1
             * outId : 1011153684
             * url : https://book.qidian.com/info/1011153684
             * cover : http://qidian.qpic.cn/qdbimg/349573/1011153684/300
             * sourceCname : 起点网站
             * sourceUrl : www.qidian.com
             * sourceName : qidian
             */

            private int originId;
            private String outId;
            private String url;
            private String cover;
            private String sourceCname;
            private String sourceUrl;
            private String sourceName;
            private long shelfTime=0;

            private String chapterTitle;

            public String getChapterTitle() {
                return chapterTitle;
            }

            public void setChapterTitle(String chapterTitle) {
                this.chapterTitle = chapterTitle;
            }

            public long getShelfTime() {
                return shelfTime;
            }

            public void setShelfTime(long shelfTime) {
                this.shelfTime = shelfTime;
            }

            public int getOriginId() {
                return originId;
            }

            public void setOriginId(int originId) {
                this.originId = originId;
            }

            public String getOutId() {
                return outId;
            }

            public void setOutId(String outId) {
                this.outId = outId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getSourceCname() {
                return sourceCname;
            }

            public void setSourceCname(String sourceCname) {
                this.sourceCname = sourceCname;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }

            public void setSourceUrl(String sourceUrl) {
                this.sourceUrl = sourceUrl;
            }

            public String getSourceName() {
                return sourceName;
            }

            public void setSourceName(String sourceName) {
                this.sourceName = sourceName;
            }
        }
    }
}
