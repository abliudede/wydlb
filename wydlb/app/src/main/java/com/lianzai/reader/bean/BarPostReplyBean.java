package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxTool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/9.
 */

public class BarPostReplyBean implements Serializable {
    /**
     * status_code : 200
     * message : 信息获取成功
     * data : [{"id":12754,"avater":"","name":"连载用户355408","content":"我想跟平台谈合作的事，请问可以帮我对接吗","uid":80623,"barId":211,"postId":99216,"floor":2,"assistCount":4,"isAssist":false,"replyCount":3,"isDeleteAble":false,"createTime":1517962319,"replies":[{"id":12884,"fromUid":17947,"toUid":"80623","fromName":"里里","toName":"连载用户355408","isFloor":true,"content":"合作什么呢？可以发邮件到lili@lianzai.com"},{"id":19072,"fromUid":28165,"toUid":"17947","fromName":"zqzu","toName":"里里","isFloor":true,"content":"您好，婚链可以和你们合作吗？微信xjlhl2，跟您联系"},{"id":24748,"fromUid":36992,"toUid":"17947","fromName":"大狼狗1","toName":"里里","isFloor":true,"content":"打撒多撒多"}]},{"id":12875,"avater":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epXAA0HFLYcwAqAQ72xFq8mtMkQM8EOUOVLKFDbWEkyf0Hnl9yylouT2zSkYodl93icfKuu0ibhNWHQ/132","name":"四叶草听雨","content":"你好，怎样把阅点提到平台？","uid":76609,"barId":211,"postId":99216,"floor":3,"assistCount":4,"isAssist":false,"replyCount":6,"isDeleteAble":false,"createTime":1517980254,"replies":[{"id":12885,"fromUid":17947,"toUid":"76609","fromName":"里里","toName":"四叶草听雨","isFloor":true,"content":"直接在白兔子钱包转帐"},{"id":20916,"fromUid":20706,"toUid":"17947","fromName":"连载用户484841","toName":"里里","isFloor":true,"content":"站内转账是不是就是往交易所转账"},{"id":21170,"fromUid":17947,"toUid":"20706","fromName":"里里","toName":"连载用户484841","isFloor":true,"content":"复制交易所的READ地址"}]},{"id":12891,"avater":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqxjK5MdOLfofHEFFSUmiagaVdNiaW8XLn1YQDEHicW0AgYbgAdEkxKMuw4Ydaia0ibeVmvxVx4QXuXhUw/132","name":"暗礁","content":"imtokem可以收发阅点吗<br/>im钱包里貌似没有阅点啊？代码不是read吗&nbsp;里面有个readcoin是阅点吗","uid":90351,"barId":211,"postId":99216,"floor":4,"assistCount":2,"isAssist":false,"replyCount":1,"isDeleteAble":false,"createTime":1517984282,"replies":[{"id":18188,"fromUid":29312,"toUid":"90351","fromName":"新力奥电脑刘炳","toName":"暗礁","isFloor":true,"content":"可以到IMTOKEN钱包，我试过的"}]},{"id":12893,"avater":"","name":"连载用户543067","content":"白兔子钱包，在哪下载，怎么转币？","uid":23040,"barId":211,"postId":99216,"floor":5,"assistCount":2,"isAssist":false,"replyCount":1,"isDeleteAble":false,"createTime":1517985661,"replies":[{"id":12956,"fromUid":79598,"toUid":"23040","fromName":"慎","toName":"连载用户543067","isFloor":true,"content":"兄弟去官网下载吧&nbsp;&nbsp;&nbsp;READ.io"}]},{"id":12958,"avater":"http://wx.qlogo.cn/mmopen/vi_32/CEWufvUZXsUEtJzARSxmLiblZic3BvZKgntqFhgoEs0wVUpsr7hfem0HkCs2CtDNiciauBCf76bFQ6iaWJeicZaV90Kg/132","name":"一菲宝宝","content":"认证失败，重新申请。还不审核呢，都马上过年了。你们不放假吗？","uid":40325,"barId":211,"postId":99216,"floor":6,"assistCount":3,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518002409,"replies":[]},{"id":12959,"avater":"http://wx.qlogo.cn/mmopen/vi_32/lYcrGBKvCibcwibY3NTKZ0qW3t5NbibUY0hiamqyUsbsSMU4D8zheDpkeNX3QImT6yWcx2fr6ZA5yHqVtGibICic86Aw/132","name":"慎","content":"阅读没有保存进度的吗？每次都要找看到哪里了！希望快点解决不然怎么看书","uid":79598,"barId":211,"postId":99216,"floor":7,"assistCount":4,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518002442,"replies":[]},{"id":13078,"avater":"","name":"正在思想","content":"能不能增加离线听书啊！连载很多小说文笔真的不错，但是现在区块链太多文章和资讯要看、还有部分资金玩短线；闲下来偏向听书，","uid":21920,"barId":211,"postId":99216,"floor":8,"assistCount":9,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518016920,"replies":[]},{"id":13354,"avater":"","name":"天霜雪影","content":"矿场等级到底怎么计算的？","uid":37049,"barId":211,"postId":99216,"floor":9,"assistCount":7,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518071349,"replies":[]},{"id":13563,"avater":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqzZCn4vRLElzrkweUQWE2icqA3Q3XIXCKe8Uhk09tqKl3scTNFNIqQFUlA5J0hqyeKE9R56XLUp2w/132","name":"༺总有刁民想害朕༻","content":"能把一键收取的按钮换一个位置吗？，老是掉在矿机的详情中","uid":16658,"barId":211,"postId":99216,"floor":10,"assistCount":1,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518140162,"replies":[]},{"id":14746,"avater":"","name":"连载用户699347","content":"想问下read在哪些平台有交易，还有现在支持转币吗","uid":79850,"barId":211,"postId":99216,"floor":11,"assistCount":2,"isAssist":false,"replyCount":0,"isDeleteAble":false,"createTime":1518349112,"replies":[]}]
     * isLogin : false
     * last_page : 5
     * current_page : 1
     * prev_page_url : null
     * next_page_url : http://beta.m.lianzai.com/api/v3/post/replylist?page=2
     * per_page : 10
     * total : 47
     */

    private int status_code;
    private String message;
    private boolean isLogin;
    private int last_page;
    private int current_page;
    private Object prev_page_url;
    private String next_page_url;
    private String per_page;
    private int total;
    private List<DataBean> data;

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

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
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
         * id : 12754
         * avater :
         * name : 连载用户355408
         * content : 我想跟平台谈合作的事，请问可以帮我对接吗
         * uid : 80623
         * barId : 211
         * postId : 99216
         * floor : 2
         * assistCount : 4
         * isAssist : false
         * replyCount : 3
         * isDeleteAble : false
         * createTime : 1517962319
         * replies : [{"id":12884,"fromUid":17947,"toUid":"80623","fromName":"里里","toName":"连载用户355408","isFloor":true,"content":"合作什么呢？可以发邮件到lili@lianzai.com"},{"id":19072,"fromUid":28165,"toUid":"17947","fromName":"zqzu","toName":"里里","isFloor":true,"content":"您好，婚链可以和你们合作吗？微信xjlhl2，跟您联系"},{"id":24748,"fromUid":36992,"toUid":"17947","fromName":"大狼狗1","toName":"里里","isFloor":true,"content":"打撒多撒多"}]
         */

        private int id;
        private String avatar;
        private String name;
        private String content;
        private int uid;
        private int barId;
        private int postId;
        private int floor;
        private int assistCount;
        private boolean isAssist;
        private int replyCount;
        private boolean isDeleteAble;
        private String createTime;
        private List<RepliesBean> replies;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return RxTool.filterContent(content);
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getBarId() {
            return barId;
        }

        public void setBarId(int barId) {
            this.barId = barId;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public int getAssistCount() {
            return assistCount;
        }

        public void setAssistCount(int assistCount) {
            this.assistCount = assistCount;
        }

        public boolean isIsAssist() {
            return isAssist;
        }

        public void setIsAssist(boolean isAssist) {
            this.isAssist = isAssist;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public boolean isIsDeleteAble() {
            return isDeleteAble;
        }

        public void setIsDeleteAble(boolean isDeleteAble) {
            this.isDeleteAble = isDeleteAble;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<RepliesBean> getReplies() {
            return replies;
        }

        public void setReplies(List<RepliesBean> replies) {
            this.replies = replies;
        }

        public static class RepliesBean {
            /**
             * id : 12884
             * fromUid : 17947
             * toUid : 80623
             * fromName : 里里
             * toName : 连载用户355408
             * isFloor : true
             * content : 合作什么呢？可以发邮件到lili@lianzai.com
             */

            private int id;
            private String fromUid;
            private String toUid;
            private String fromName;
            private String toName;
            private boolean isFloor;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFromUid() {
                return fromUid;
            }

            public void setFromUid(String fromUid) {
                this.fromUid = fromUid;
            }

            public String getToUid() {
                return toUid;
            }

            public void setToUid(String toUid) {
                this.toUid = toUid;
            }

            public String getFromName() {
                return fromName;
            }

            public void setFromName(String fromName) {
                this.fromName = fromName;
            }

            public String getToName() {
                return toName;
            }

            public void setToName(String toName) {
                this.toName = toName;
            }

            public boolean isIsFloor() {
                return isFloor;
            }

            public void setIsFloor(boolean isFloor) {
                this.isFloor = isFloor;
            }

            public String getContent() {
                return RxTool.filterContent(content);
            }

            public void setContent(String content) {
                this.content = content;
            }

            @Override
            public String toString() {
                return "RepliesBean{" +
                        "id=" + id +
                        ", fromUid='" + fromUid + '\'' +
                        ", toUid='" + toUid + '\'' +
                        ", fromName='" + fromName + '\'' +
                        ", toName='" + toName + '\'' +
                        ", isFloor=" + isFloor +
                        ", content='" + content + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", avatar='" + avatar + '\'' +
                    ", name='" + name + '\'' +
                    ", content='" + content + '\'' +
                    ", uid=" + uid +
                    ", barId=" + barId +
                    ", postId=" + postId +
                    ", floor=" + floor +
                    ", assistCount=" + assistCount +
                    ", isAssist=" + isAssist +
                    ", replyCount=" + replyCount +
                    ", isDeleteAble=" + isDeleteAble +
                    ", createTime='" + createTime + '\'' +
                    ", replies=" + replies +
                    '}';
        }
    }
}
