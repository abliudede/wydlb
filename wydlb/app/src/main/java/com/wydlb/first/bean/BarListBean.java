package com.wydlb.first.bean;

import android.text.SpannableString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/9.
 */

public class BarListBean implements Serializable {

    /**
     * status_code : 200
     * message : 信息获取成功
     * data : {"current_page":1,"data":[{"id":59347,"uid":19939,"novel_id":42,"chapter_id":13877,"post_bar_id":42,"title":"test111","intro":"昨天玄小妹在公众号上将《玄门志怪录》的设定发布以后，很高兴的看到有很多读者踊跃的参与。仅仅一天的时间，后台已经提交了四十多个故事，微信群也突破了一百人，大家的讨论很热烈，很积极。当然，这些故事当中，有","assist_count":0,"reply_count":0,"last_reply_time":1515465013,"avatar":"https://static.lianzai.com/avatar//1511145133549.jpg","bar_name":"小二说","name":"樱木子"},{"id":59346,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464878,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"},{"id":59345,"uid":36842,"novel_id":967,"chapter_id":13876,"post_bar_id":967,"title":"测试草稿（修改）","intro":"测试草稿内容测试草稿内容（修改）","assist_count":0,"reply_count":0,"last_reply_time":1515464869,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59344,"uid":36842,"novel_id":967,"chapter_id":13875,"post_bar_id":967,"title":"第2卷第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464867,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59343,"uid":36842,"novel_id":967,"chapter_id":13874,"post_bar_id":967,"title":"第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464866,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59342,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464535,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"},{"id":59341,"uid":36842,"novel_id":966,"chapter_id":13873,"post_bar_id":966,"title":"测试草稿（修改）","intro":"测试草稿内容测试草稿内容（修改）","assist_count":0,"reply_count":0,"last_reply_time":1515464525,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59340,"uid":36842,"novel_id":966,"chapter_id":13872,"post_bar_id":966,"title":"第2卷第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464524,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59339,"uid":36842,"novel_id":966,"chapter_id":13871,"post_bar_id":966,"title":"第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464523,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59338,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464064,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"}],"from":1,"last_page":5884,"next_page_url":"http://beta.m.lianzai.com/api/v2/post/recommends?page=2","path":"http://beta.m.lianzai.com/api/v2/post/recommends","per_page":10,"prev_page_url":null,"to":10,"total":58837}
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
         * current_page : 1
         * data : [{"id":59347,"uid":19939,"novel_id":42,"chapter_id":13877,"post_bar_id":42,"title":"test111","intro":"昨天玄小妹在公众号上将《玄门志怪录》的设定发布以后，很高兴的看到有很多读者踊跃的参与。仅仅一天的时间，后台已经提交了四十多个故事，微信群也突破了一百人，大家的讨论很热烈，很积极。当然，这些故事当中，有","assist_count":0,"reply_count":0,"last_reply_time":1515465013,"avatar":"https://static.lianzai.com/avatar//1511145133549.jpg","bar_name":"小二说","name":"樱木子"},{"id":59346,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464878,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"},{"id":59345,"uid":36842,"novel_id":967,"chapter_id":13876,"post_bar_id":967,"title":"测试草稿（修改）","intro":"测试草稿内容测试草稿内容（修改）","assist_count":0,"reply_count":0,"last_reply_time":1515464869,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59344,"uid":36842,"novel_id":967,"chapter_id":13875,"post_bar_id":967,"title":"第2卷第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464867,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59343,"uid":36842,"novel_id":967,"chapter_id":13874,"post_bar_id":967,"title":"第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464866,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59342,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464535,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"},{"id":59341,"uid":36842,"novel_id":966,"chapter_id":13873,"post_bar_id":966,"title":"测试草稿（修改）","intro":"测试草稿内容测试草稿内容（修改）","assist_count":0,"reply_count":0,"last_reply_time":1515464525,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59340,"uid":36842,"novel_id":966,"chapter_id":13872,"post_bar_id":966,"title":"第2卷第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464524,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59339,"uid":36842,"novel_id":966,"chapter_id":13871,"post_bar_id":966,"title":"第1章","intro":"这个是章节内容","assist_count":0,"reply_count":0,"last_reply_time":1515464523,"avatar":"","bar_name":"接口测试小说","name":"涔涔"},{"id":59338,"uid":36842,"novel_id":217,"chapter_id":0,"post_bar_id":217,"title":"这是帖子标题","intro":"这是帖子内容，接口自动发布#qq_嘿嘿#","assist_count":0,"reply_count":0,"last_reply_time":1515464064,"avatar":"","bar_name":"魂与魄之歌","name":"涔涔"}]
         * from : 1
         * last_page : 5884
         * next_page_url : http://beta.m.lianzai.com/api/v2/post/recommends?page=2
         * path : http://beta.m.lianzai.com/api/v2/post/recommends
         * per_page : 10
         * prev_page_url : null
         * to : 10
         * total : 58837
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
             * id : 59347
             * uid : 19939
             * novel_id : 42
             * chapter_id : 13877
             * post_bar_id : 42
             * title : test111
             * intro : 昨天玄小妹在公众号上将《玄门志怪录》的设定发布以后，很高兴的看到有很多读者踊跃的参与。仅仅一天的时间，后台已经提交了四十多个故事，微信群也突破了一百人，大家的讨论很热烈，很积极。当然，这些故事当中，有
             * assist_count : 0
             * reply_count : 0
             * last_reply_time : 1515465013
             * avatar : https://static.lianzai.com/avatar//1511145133549.jpg
             * bar_name : 小二说
             * name : 樱木子
             */

            private String id;
            private int uid;
            private String novel_id;
            private int chapter_id;
            private String post_bar_id;
            private String title;
            private String intro;
            private int assist_count=0;
            private int reply_count=0;
            private String last_reply_time;
            private String avatar;
            private String bar_name;
            private String name;

            private SpannableString spannableString;

            private boolean showMore=false;

            public boolean isShowMore() {
                return showMore;
            }

            public void setShowMore(boolean showMore) {
                this.showMore = showMore;
            }

            public SpannableString getSpannableString() {
                return spannableString;
            }

            public void setSpannableString(SpannableString spannableString) {
                this.spannableString = spannableString;
            }

            private boolean isExpand=false;

            public boolean isExpand() {
                return isExpand;
            }

            public void setExpand(boolean expand) {
                isExpand = expand;
            }

            private boolean is_assist;//是否点赞

            public boolean isIs_assist() {
                return is_assist;
            }

            public void setIs_assist(boolean is_assist) {
                this.is_assist = is_assist;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }


            public String getNovel_id() {
                return novel_id;
            }

            public void setNovel_id(String novel_id) {
                this.novel_id = novel_id;
            }

            public int getChapter_id() {
                return chapter_id;
            }

            public void setChapter_id(int chapter_id) {
                this.chapter_id = chapter_id;
            }

            public String getPost_bar_id() {
                return post_bar_id;
            }

            public void setPost_bar_id(String post_bar_id) {
                this.post_bar_id = post_bar_id;
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

            public int getAssist_count() {
                return assist_count;
            }

            public void setAssist_count(int assist_count) {
                this.assist_count = assist_count;
            }

            public int getReply_count() {
                return reply_count;
            }

            public void setReply_count(int reply_count) {
                this.reply_count = reply_count;
            }

            public String getLast_reply_time() {
                return last_reply_time;
            }

            public void setLast_reply_time(String last_reply_time) {
                this.last_reply_time = last_reply_time;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getBar_name() {
                return bar_name;
            }

            public void setBar_name(String bar_name) {
                this.bar_name = bar_name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
