package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxTool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/3.
 */

public class ChasingBookListBean implements Serializable {

    /**
     * status_code : 200
     * message : 获取追书列表成功
     * data : {"followList":{"current_page":1,"from":1,"last_page":46,"next_page_url":"http://beta.m.lianzai.com/api/follow/list?page=2","path":"http://beta.m.lianzai.com/api/follow/list","per_page":10,"prev_page_url":null,"to":10,"total":452,"data":[{"novel_id":333,"is_mining":1,"is_reward":1,"novel_title":"寻人诀之修罗道","cover":"https://static.lianzai.com/cover//1511359007301.jpg","post_count":329,"member_count":174,"word_num":207882,"lastest_chapter_title":"第133章:登记","lastest_chapter_shelf_time":1515640260,"lastest_chapter_id":15120,"read_tag":"有更新","read_status":1},{"novel_id":614,"is_mining":0,"is_reward":0,"novel_title":"银河之灾","cover":"https://static.lianzai.com/cover//1513914740609.jpg","post_count":135,"member_count":44,"word_num":67416,"lastest_chapter_title":"第29章","lastest_chapter_shelf_time":1515638054,"lastest_chapter_id":15231,"read_tag":"有更新","read_status":1},{"novel_id":134,"is_mining":1,"is_reward":1,"novel_title":"银河纪元","cover":"http://xccover.b0.upaiyun.com/1509017748730.jpg","post_count":310,"member_count":167,"word_num":155297,"lastest_chapter_title":"第六十三章 切磋 指点（一）","lastest_chapter_shelf_time":1515637246,"lastest_chapter_id":15227,"read_tag":"有更新","read_status":1},{"novel_id":52,"is_mining":0,"is_reward":0,"novel_title":"无限异能探宝系统","cover":"http://xccover.b0.upaiyun.com/1508939052692.JPG","post_count":73,"member_count":49,"word_num":45445,"lastest_chapter_title":"第十六章 起来吧少年（二）","lastest_chapter_shelf_time":1515636610,"lastest_chapter_id":15100,"read_tag":"有更新","read_status":1},{"novel_id":535,"is_mining":1,"is_reward":1,"novel_title":"天醒时代","cover":"https://static.lianzai.com/cover//1513393505643.jpg","post_count":387,"member_count":197,"word_num":160435,"lastest_chapter_title":"第七十二章 鬼子进村","lastest_chapter_shelf_time":1515636385,"lastest_chapter_id":15223,"read_tag":"有更新","read_status":1},{"novel_id":59,"is_mining":1,"is_reward":1,"novel_title":"异案局事件簿","cover":"https://static.lianzai.com/cover//1509767778156.JPG","post_count":326,"member_count":216,"word_num":131591,"lastest_chapter_title":"070 禁界②","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":13392,"read_tag":"有更新","read_status":1},{"novel_id":564,"is_mining":1,"is_reward":1,"novel_title":"骄傲的少年","cover":"https://static.lianzai.com/cover//1513312398590.jpg","post_count":167,"member_count":87,"word_num":137212,"lastest_chapter_title":"第四十三章 李涛的收获（从未断更）","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":14655,"read_tag":"有更新","read_status":1},{"novel_id":375,"is_mining":0,"is_reward":1,"novel_title":"啸天劫","cover":"https://static.lianzai.com/cover//1511883611162.jpg","post_count":153,"member_count":74,"word_num":134960,"lastest_chapter_title":"第三十二章 无冤无仇（下）","lastest_chapter_shelf_time":1515635396,"lastest_chapter_id":15200,"read_tag":"有更新","read_status":1},{"novel_id":374,"is_mining":1,"is_reward":1,"novel_title":"龙逆","cover":"https://static.lianzai.com/cover//1511516344742.jpg","post_count":360,"member_count":189,"word_num":198712,"lastest_chapter_title":"第六十一章 神谕蔑死者","lastest_chapter_shelf_time":1515635314,"lastest_chapter_id":15197,"read_tag":"有更新","read_status":1},{"novel_id":580,"is_mining":1,"is_reward":1,"novel_title":"末化：始作之源","cover":"https://static.lianzai.com/cover//1513822138581.jpg","post_count":217,"member_count":124,"word_num":123812,"lastest_chapter_title":"第37章 要挟","lastest_chapter_shelf_time":1515635027,"lastest_chapter_id":15193,"read_tag":"有更新","read_status":1}]}}
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
         * followList : {"current_page":1,"from":1,"last_page":46,"next_page_url":"http://beta.m.lianzai.com/api/follow/list?page=2","path":"http://beta.m.lianzai.com/api/follow/list","per_page":10,"prev_page_url":null,"to":10,"total":452,"data":[{"novel_id":333,"is_mining":1,"is_reward":1,"novel_title":"寻人诀之修罗道","cover":"https://static.lianzai.com/cover//1511359007301.jpg","post_count":329,"member_count":174,"word_num":207882,"lastest_chapter_title":"第133章:登记","lastest_chapter_shelf_time":1515640260,"lastest_chapter_id":15120,"read_tag":"有更新","read_status":1},{"novel_id":614,"is_mining":0,"is_reward":0,"novel_title":"银河之灾","cover":"https://static.lianzai.com/cover//1513914740609.jpg","post_count":135,"member_count":44,"word_num":67416,"lastest_chapter_title":"第29章","lastest_chapter_shelf_time":1515638054,"lastest_chapter_id":15231,"read_tag":"有更新","read_status":1},{"novel_id":134,"is_mining":1,"is_reward":1,"novel_title":"银河纪元","cover":"http://xccover.b0.upaiyun.com/1509017748730.jpg","post_count":310,"member_count":167,"word_num":155297,"lastest_chapter_title":"第六十三章 切磋 指点（一）","lastest_chapter_shelf_time":1515637246,"lastest_chapter_id":15227,"read_tag":"有更新","read_status":1},{"novel_id":52,"is_mining":0,"is_reward":0,"novel_title":"无限异能探宝系统","cover":"http://xccover.b0.upaiyun.com/1508939052692.JPG","post_count":73,"member_count":49,"word_num":45445,"lastest_chapter_title":"第十六章 起来吧少年（二）","lastest_chapter_shelf_time":1515636610,"lastest_chapter_id":15100,"read_tag":"有更新","read_status":1},{"novel_id":535,"is_mining":1,"is_reward":1,"novel_title":"天醒时代","cover":"https://static.lianzai.com/cover//1513393505643.jpg","post_count":387,"member_count":197,"word_num":160435,"lastest_chapter_title":"第七十二章 鬼子进村","lastest_chapter_shelf_time":1515636385,"lastest_chapter_id":15223,"read_tag":"有更新","read_status":1},{"novel_id":59,"is_mining":1,"is_reward":1,"novel_title":"异案局事件簿","cover":"https://static.lianzai.com/cover//1509767778156.JPG","post_count":326,"member_count":216,"word_num":131591,"lastest_chapter_title":"070 禁界②","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":13392,"read_tag":"有更新","read_status":1},{"novel_id":564,"is_mining":1,"is_reward":1,"novel_title":"骄傲的少年","cover":"https://static.lianzai.com/cover//1513312398590.jpg","post_count":167,"member_count":87,"word_num":137212,"lastest_chapter_title":"第四十三章 李涛的收获（从未断更）","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":14655,"read_tag":"有更新","read_status":1},{"novel_id":375,"is_mining":0,"is_reward":1,"novel_title":"啸天劫","cover":"https://static.lianzai.com/cover//1511883611162.jpg","post_count":153,"member_count":74,"word_num":134960,"lastest_chapter_title":"第三十二章 无冤无仇（下）","lastest_chapter_shelf_time":1515635396,"lastest_chapter_id":15200,"read_tag":"有更新","read_status":1},{"novel_id":374,"is_mining":1,"is_reward":1,"novel_title":"龙逆","cover":"https://static.lianzai.com/cover//1511516344742.jpg","post_count":360,"member_count":189,"word_num":198712,"lastest_chapter_title":"第六十一章 神谕蔑死者","lastest_chapter_shelf_time":1515635314,"lastest_chapter_id":15197,"read_tag":"有更新","read_status":1},{"novel_id":580,"is_mining":1,"is_reward":1,"novel_title":"末化：始作之源","cover":"https://static.lianzai.com/cover//1513822138581.jpg","post_count":217,"member_count":124,"word_num":123812,"lastest_chapter_title":"第37章 要挟","lastest_chapter_shelf_time":1515635027,"lastest_chapter_id":15193,"read_tag":"有更新","read_status":1}]}
         */

        private FollowListBean followList;

        public FollowListBean getFollowList() {
            return followList;
        }

        public void setFollowList(FollowListBean followList) {
            this.followList = followList;
        }

        public static class FollowListBean {
            /**
             * current_page : 1
             * from : 1
             * last_page : 46
             * next_page_url : http://beta.m.lianzai.com/api/follow/list?page=2
             * path : http://beta.m.lianzai.com/api/follow/list
             * per_page : 10
             * prev_page_url : null
             * to : 10
             * total : 452
             * data : [{"novel_id":333,"is_mining":1,"is_reward":1,"novel_title":"寻人诀之修罗道","cover":"https://static.lianzai.com/cover//1511359007301.jpg","post_count":329,"member_count":174,"word_num":207882,"lastest_chapter_title":"第133章:登记","lastest_chapter_shelf_time":1515640260,"lastest_chapter_id":15120,"read_tag":"有更新","read_status":1},{"novel_id":614,"is_mining":0,"is_reward":0,"novel_title":"银河之灾","cover":"https://static.lianzai.com/cover//1513914740609.jpg","post_count":135,"member_count":44,"word_num":67416,"lastest_chapter_title":"第29章","lastest_chapter_shelf_time":1515638054,"lastest_chapter_id":15231,"read_tag":"有更新","read_status":1},{"novel_id":134,"is_mining":1,"is_reward":1,"novel_title":"银河纪元","cover":"http://xccover.b0.upaiyun.com/1509017748730.jpg","post_count":310,"member_count":167,"word_num":155297,"lastest_chapter_title":"第六十三章 切磋 指点（一）","lastest_chapter_shelf_time":1515637246,"lastest_chapter_id":15227,"read_tag":"有更新","read_status":1},{"novel_id":52,"is_mining":0,"is_reward":0,"novel_title":"无限异能探宝系统","cover":"http://xccover.b0.upaiyun.com/1508939052692.JPG","post_count":73,"member_count":49,"word_num":45445,"lastest_chapter_title":"第十六章 起来吧少年（二）","lastest_chapter_shelf_time":1515636610,"lastest_chapter_id":15100,"read_tag":"有更新","read_status":1},{"novel_id":535,"is_mining":1,"is_reward":1,"novel_title":"天醒时代","cover":"https://static.lianzai.com/cover//1513393505643.jpg","post_count":387,"member_count":197,"word_num":160435,"lastest_chapter_title":"第七十二章 鬼子进村","lastest_chapter_shelf_time":1515636385,"lastest_chapter_id":15223,"read_tag":"有更新","read_status":1},{"novel_id":59,"is_mining":1,"is_reward":1,"novel_title":"异案局事件簿","cover":"https://static.lianzai.com/cover//1509767778156.JPG","post_count":326,"member_count":216,"word_num":131591,"lastest_chapter_title":"070 禁界②","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":13392,"read_tag":"有更新","read_status":1},{"novel_id":564,"is_mining":1,"is_reward":1,"novel_title":"骄傲的少年","cover":"https://static.lianzai.com/cover//1513312398590.jpg","post_count":167,"member_count":87,"word_num":137212,"lastest_chapter_title":"第四十三章 李涛的收获（从未断更）","lastest_chapter_shelf_time":1515636000,"lastest_chapter_id":14655,"read_tag":"有更新","read_status":1},{"novel_id":375,"is_mining":0,"is_reward":1,"novel_title":"啸天劫","cover":"https://static.lianzai.com/cover//1511883611162.jpg","post_count":153,"member_count":74,"word_num":134960,"lastest_chapter_title":"第三十二章 无冤无仇（下）","lastest_chapter_shelf_time":1515635396,"lastest_chapter_id":15200,"read_tag":"有更新","read_status":1},{"novel_id":374,"is_mining":1,"is_reward":1,"novel_title":"龙逆","cover":"https://static.lianzai.com/cover//1511516344742.jpg","post_count":360,"member_count":189,"word_num":198712,"lastest_chapter_title":"第六十一章 神谕蔑死者","lastest_chapter_shelf_time":1515635314,"lastest_chapter_id":15197,"read_tag":"有更新","read_status":1},{"novel_id":580,"is_mining":1,"is_reward":1,"novel_title":"末化：始作之源","cover":"https://static.lianzai.com/cover//1513822138581.jpg","post_count":217,"member_count":124,"word_num":123812,"lastest_chapter_title":"第37章 要挟","lastest_chapter_shelf_time":1515635027,"lastest_chapter_id":15193,"read_tag":"有更新","read_status":1}]
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

            public String getTotal() {
                return String.valueOf(total);
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
                 * novel_id : 333
                 * is_mining : 1
                 * is_reward : 1
                 * novel_title : 寻人诀之修罗道
                 * cover : https://static.lianzai.com/cover//1511359007301.jpg
                 * post_count : 329
                 * member_count : 174
                 * word_num : 207882
                 * lastest_chapter_title : 第133章:登记
                 * lastest_chapter_shelf_time : 1515640260
                 * lastest_chapter_id : 15120
                 * read_tag : 有更新
                 * read_status : 1
                 */

                private String novel_id;
                private int is_mining;
                private int is_reward;
                private String novel_title;
                private String bar_id;
                private String cover;
                private int post_count;
                private int member_count;
                private String word_num;
                private String lastest_chapter_title;
                private String lastest_chapter_shelf_time;
                private int lastest_chapter_id;
                private String read_tag;
                private int read_status;
                private String share_url;

                public String getShareUrl() {
                    return share_url;
                }

                public void setShareUrl(String shareUrl) {
                    this.share_url = shareUrl;
                }

                public String getBar_id() {
                    return bar_id;
                }

                public void setBar_id(String bar_id) {
                    this.bar_id = bar_id;
                }

                public String getBarId() {
                    return bar_id;
                }

                public void setBarId(String barId) {
                    this.bar_id = barId;
                }

                public String getNovel_id() {
                    return novel_id;
                }

                public void setNovel_id(String novel_id) {
                    this.novel_id = novel_id;
                }

                public int getIs_mining() {
                    return is_mining;
                }

                public void setIs_mining(int is_mining) {
                    this.is_mining = is_mining;
                }

                public int getIs_reward() {
                    return is_reward;
                }

                public void setIs_reward(int is_reward) {
                    this.is_reward = is_reward;
                }

                public String getNovel_title() {
                    return novel_title;
                }

                public void setNovel_title(String novel_title) {
                    this.novel_title = novel_title;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getPost_count() {
                    return post_count;
                }

                public void setPost_count(int post_count) {
                    this.post_count = post_count;
                }

                public int getMember_count() {
                    return member_count;
                }

                public void setMember_count(int member_count) {
                    this.member_count = member_count;
                }

                public String getWord_num() {
                    return RxTool.getWordNumFormat(word_num);
                }

                public void setWord_num(String word_num) {
                    this.word_num = word_num;
                }

                public String getLastest_chapter_title() {
                    return lastest_chapter_title;
                }

                public void setLastest_chapter_title(String lastest_chapter_title) {
                    this.lastest_chapter_title = lastest_chapter_title;
                }

                public String getLastest_chapter_shelf_time() {
                    return lastest_chapter_shelf_time;
                }

                public void setLastest_chapter_shelf_time(String lastest_chapter_shelf_time) {
                    this.lastest_chapter_shelf_time = lastest_chapter_shelf_time;
                }

                public int getLastest_chapter_id() {
                    return lastest_chapter_id;
                }

                public void setLastest_chapter_id(int lastest_chapter_id) {
                    this.lastest_chapter_id = lastest_chapter_id;
                }

                public String getRead_tag() {
                    return read_tag;
                }

                public void setRead_tag(String read_tag) {
                    this.read_tag = read_tag;
                }

                public int getRead_status() {
                    return read_status;
                }

                public void setRead_status(int read_status) {
                    this.read_status = read_status;
                }
            }
        }
    }
}
