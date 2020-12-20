package com.wydlb.first.bean;

public class UrlRecognitionBean {


    /**
     * code : 0
     * msg : success
     * data : {"novel_id":50000003,"chapter_url":"https://m.qidian.com/book/1010981643/0","title":"开天录","intro":"生存，很容易。生活，很艰难。我族，要的不是卑下的生存，而是昂首、高傲的生活。我族，誓不为奴！","cover":"https://bookcover.yuewen.com/qdbimg/349573/1010981643/180"}
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
         * novel_id : 50000003
         * chapter_url : https://m.qidian.com/book/1010981643/0
         * title : 开天录
         * intro : 生存，很容易。生活，很艰难。我族，要的不是卑下的生存，而是昂首、高傲的生活。我族，誓不为奴！
         * cover : https://bookcover.yuewen.com/qdbimg/349573/1010981643/180
         */

        private int novel_id;
        private String chapter_url;
        private String title;
        private String intro;
        private String cover;

        public int getNovel_id() {
            return novel_id;
        }

        public void setNovel_id(int novel_id) {
            this.novel_id = novel_id;
        }

        public String getChapter_url() {
            return chapter_url;
        }

        public void setChapter_url(String chapter_url) {
            this.chapter_url = chapter_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
