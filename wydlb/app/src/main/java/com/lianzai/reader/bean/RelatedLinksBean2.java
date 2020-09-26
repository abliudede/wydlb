package com.lianzai.reader.bean;

import java.util.List;

public class RelatedLinksBean2 {


    /**
     * code : 0
     * msg : success
     * data : [{"author_name":"本宫嗑瓜子","platform_introduce":"王小萌是一个祖传了二十八代的捉妖师、她是第二十九代，她的画风跟家里的长辈都不一样，长辈们以降妖除魔为己任，有报酬就收，没报酬也不要。王小萌不一样，她是以挣钱为目的的降妖除魔，没错她喜欢钱！","first_chapter_url":"https://m.qidian.com/book/1011225593/397096801","catalog_url":"https://m.qidian.com/book/1011225593/catalog","chapters":[{"chapter_title":"第十三章 招工","chapter_url":"https://m.qidian.com/book/1011225593/399351774","updated_time":"2019-04-29 23:38:29"},{"chapter_title":"第十二章 回京","chapter_url":"https://m.qidian.com/book/1011225593/398296918","updated_time":"2019-04-29 23:38:29"},{"chapter_title":"第十一章 继续前进","chapter_url":"https://m.qidian.com/book/1011225593/397781049","updated_time":"2019-04-29 23:38:29"}],"source":"qidian.com","book_id":1283,"book_name":"祖传捉妖"}]
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
         * author_name : 本宫嗑瓜子
         * platform_introduce : 王小萌是一个祖传了二十八代的捉妖师、她是第二十九代，她的画风跟家里的长辈都不一样，长辈们以降妖除魔为己任，有报酬就收，没报酬也不要。王小萌不一样，她是以挣钱为目的的降妖除魔，没错她喜欢钱！
         * first_chapter_url : https://m.qidian.com/book/1011225593/397096801
         * catalog_url : https://m.qidian.com/book/1011225593/catalog
         * chapters : [{"chapter_title":"第十三章 招工","chapter_url":"https://m.qidian.com/book/1011225593/399351774","updated_time":"2019-04-29 23:38:29"},{"chapter_title":"第十二章 回京","chapter_url":"https://m.qidian.com/book/1011225593/398296918","updated_time":"2019-04-29 23:38:29"},{"chapter_title":"第十一章 继续前进","chapter_url":"https://m.qidian.com/book/1011225593/397781049","updated_time":"2019-04-29 23:38:29"}]
         * source : qidian.com
         * book_id : 1283
         * book_name : 祖传捉妖
         */

        private String author_name;
        private String platform_introduce;
        private String first_chapter_url;
        private String catalog_url;
        private String source;
        private int book_id;
        private String book_name;
        private List<ChaptersBean> chapters;
        private int randomInt;
        private String sourceName;

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public int getRandomInt() {
            return randomInt;
        }

        public void setRandomInt(int randomInt) {
            this.randomInt = randomInt;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getPlatform_introduce() {
            return platform_introduce;
        }

        public void setPlatform_introduce(String platform_introduce) {
            this.platform_introduce = platform_introduce;
        }

        public String getFirst_chapter_url() {
            return first_chapter_url;
        }

        public void setFirst_chapter_url(String first_chapter_url) {
            this.first_chapter_url = first_chapter_url;
        }

        public String getCatalog_url() {
            return catalog_url;
        }

        public void setCatalog_url(String catalog_url) {
            this.catalog_url = catalog_url;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getBook_id() {
            return book_id;
        }

        public void setBook_id(int book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public List<ChaptersBean> getChapters() {
            return chapters;
        }

        public void setChapters(List<ChaptersBean> chapters) {
            this.chapters = chapters;
        }

        public static class ChaptersBean {
            /**
             * chapter_title : 第十三章 招工
             * chapter_url : https://m.qidian.com/book/1011225593/399351774
             * updated_time : 2019-04-29 23:38:29
             */

            private String chapter_title;
            private String chapter_url;
            private String updated_time;

            public String getChapter_title() {
                return chapter_title;
            }

            public void setChapter_title(String chapter_title) {
                this.chapter_title = chapter_title;
            }

            public String getChapter_url() {
                return chapter_url;
            }

            public void setChapter_url(String chapter_url) {
                this.chapter_url = chapter_url;
            }

            public String getUpdated_time() {
                return updated_time;
            }

            public void setUpdated_time(String updated_time) {
                this.updated_time = updated_time;
            }
        }
    }
}
