package com.wydlb.first.bean;

import com.wydlb.first.utils.RxTool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/9.
 */

public class BookListCategoryBean implements Serializable {
    /**
     * status_code : 200
     * message : 获取分类书籍列表成功
     * data : {"bookList":{"current_page":1,"data":[{"max_shelf_time":"2018-01-07 12:25:39","novel_id":944,"title":"吾心悦君","cover":"https://static.lianzai.com/cover/book_title_1515298821_1181.png","intro":"那一年，桃花树下，他一袭红衣倾了天下那一年，清心池旁，他微扬唇角风华绝代为了家族，他们缔结一段佳缘意外之下，他们竟有了爱情的结晶？！但在这场爱情的游戏中，是谁失了心？嘴上不饶人，谁又知道心中是否爱到深处？命的枷锁让两人不得不分离，再次相见，他们又将何去何从？陌上人如玉，公子世无双，再次相见，我想告诉你，我爱你。------笙景云云，我们......回不去了......-------篱青","word_num":2217,"member_count":1}],"from":1,"last_page":672,"next_page_url":"http://beta.m.lianzai.com/api/v2/genres/books?page=2","path":"http://beta.m.lianzai.com/api/v2/genres/books","per_page":1,"prev_page_url":null,"to":1,"total":672},"genreInfo":{"title":"新书上架","desc":"最新书籍列表"}}
     */

    private int status_code;
    private String message;
    private DataBeanX data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * bookList : {"current_page":1,"data":[{"max_shelf_time":"2018-01-07 12:25:39","novel_id":944,"title":"吾心悦君","cover":"https://static.lianzai.com/cover/book_title_1515298821_1181.png","intro":"那一年，桃花树下，他一袭红衣倾了天下那一年，清心池旁，他微扬唇角风华绝代为了家族，他们缔结一段佳缘意外之下，他们竟有了爱情的结晶？！但在这场爱情的游戏中，是谁失了心？嘴上不饶人，谁又知道心中是否爱到深处？命的枷锁让两人不得不分离，再次相见，他们又将何去何从？陌上人如玉，公子世无双，再次相见，我想告诉你，我爱你。------笙景云云，我们......回不去了......-------篱青","word_num":2217,"member_count":1}],"from":1,"last_page":672,"next_page_url":"http://beta.m.lianzai.com/api/v2/genres/books?page=2","path":"http://beta.m.lianzai.com/api/v2/genres/books","per_page":1,"prev_page_url":null,"to":1,"total":672}
         * genreInfo : {"title":"新书上架","desc":"最新书籍列表"}
         */

        private BookListBean bookList;
        private GenreInfoBean genreInfo;

        public BookListBean getBookList() {
            return bookList;
        }

        public void setBookList(BookListBean bookList) {
            this.bookList = bookList;
        }

        public GenreInfoBean getGenreInfo() {
            return genreInfo;
        }

        public void setGenreInfo(GenreInfoBean genreInfo) {
            this.genreInfo = genreInfo;
        }

        public static class BookListBean {
            /**
             * current_page : 1
             * data : [{"max_shelf_time":"2018-01-07 12:25:39","novel_id":944,"title":"吾心悦君","cover":"https://static.lianzai.com/cover/book_title_1515298821_1181.png","intro":"那一年，桃花树下，他一袭红衣倾了天下那一年，清心池旁，他微扬唇角风华绝代为了家族，他们缔结一段佳缘意外之下，他们竟有了爱情的结晶？！但在这场爱情的游戏中，是谁失了心？嘴上不饶人，谁又知道心中是否爱到深处？命的枷锁让两人不得不分离，再次相见，他们又将何去何从？陌上人如玉，公子世无双，再次相见，我想告诉你，我爱你。------笙景云云，我们......回不去了......-------篱青","word_num":2217,"member_count":1}]
             * from : 1
             * last_page : 672
             * next_page_url : http://beta.m.lianzai.com/api/v2/genres/books?page=2
             * path : http://beta.m.lianzai.com/api/v2/genres/books
             * per_page : 1
             * prev_page_url : null
             * to : 1
             * total : 672
             */

            private int current_page;
            private int from;
            private int last_page;
            private String next_page_url;
            private String path;
            private int per_page;
            private Object prev_page_url;
            private int to;
            private int total;
            private List<DataBean> data;

            public int getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(int current_page) {
                this.current_page = current_page;
            }

            public int getFrom() {
                return from;
            }

            public void setFrom(int from) {
                this.from = from;
            }

            public int getLast_page() {
                return last_page;
            }

            public void setLast_page(int last_page) {
                this.last_page = last_page;
            }

            public String getNext_page_url() {
                return next_page_url;
            }

            public void setNext_page_url(String next_page_url) {
                this.next_page_url = next_page_url;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getPer_page() {
                return per_page;
            }

            public void setPer_page(int per_page) {
                this.per_page = per_page;
            }

            public Object getPrev_page_url() {
                return prev_page_url;
            }

            public void setPrev_page_url(Object prev_page_url) {
                this.prev_page_url = prev_page_url;
            }

            public int getTo() {
                return to;
            }

            public void setTo(int to) {
                this.to = to;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * max_shelf_time : 2018-01-07 12:25:39
                 * novel_id : 944
                 * title : 吾心悦君
                 * cover : https://static.lianzai.com/cover/book_title_1515298821_1181.png
                 * intro : 那一年，桃花树下，他一袭红衣倾了天下那一年，清心池旁，他微扬唇角风华绝代为了家族，他们缔结一段佳缘意外之下，他们竟有了爱情的结晶？！但在这场爱情的游戏中，是谁失了心？嘴上不饶人，谁又知道心中是否爱到深处？命的枷锁让两人不得不分离，再次相见，他们又将何去何从？陌上人如玉，公子世无双，再次相见，我想告诉你，我爱你。------笙景云云，我们......回不去了......-------篱青
                 * word_num : 2217
                 * member_count : 1
                 */

                private String max_shelf_time;
                private int novel_id;
                private String title;
                private String bar_id;
                private String cover;
                private String intro;
                private int post_count;
                private int word_num;
                private int member_count;

                public String getBarId() {
                    return bar_id;
                }

                public void setBarId(String barId) {
                    this.bar_id = barId;
                }

                public int getPost_count() {
                    return post_count;
                }

                public void setPost_count(int post_count) {
                    this.post_count = post_count;
                }

                public String getMax_shelf_time() {
                    return max_shelf_time;
                }

                public void setMax_shelf_time(String max_shelf_time) {
                    this.max_shelf_time = max_shelf_time;
                }

                public int getNovel_id() {
                    return novel_id;
                }

                public void setNovel_id(int novel_id) {
                    this.novel_id = novel_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public String getIntro() {
                    return intro;
                }

                public void setIntro(String intro) {
                    this.intro = intro;
                }

                public String getWord_num() {
                    return RxTool.getWordNumFormat(String.valueOf(word_num));
                }

                public void setWord_num(int word_num) {
                    this.word_num = word_num;
                }

                public int getMember_count() {
                    return member_count;
                }

                public void setMember_count(int member_count) {
                    this.member_count = member_count;
                }
            }
        }

        public static class GenreInfoBean {
            /**
             * title : 新书上架
             * desc : 最新书籍列表
             */

            private String title;
            private String desc;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
