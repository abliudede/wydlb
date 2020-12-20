package com.wydlb.first.bean;

import com.wydlb.first.utils.RxTool;

import java.util.List;

/**
 * Created by lrz on 2018/1/9.
 */

public class BarPostFloorInFloorBean {


    /**
     * status_code : 200
     * comment : {"id":12754,"avatar":"","name":"连载用户355408","content":"我想跟平台谈合作的事，请问可以帮我对接吗","uid":80623,"barId":211,"postId":99216,"floor":2,"assistCount":5,"isAssist":false,"replyCount":4,"isDeleteAble":false,"createTime":1517962319}
     * replies : [{"id":12884,"avatar":"http://wx.qlogo.cn/mmopen/vi_32/ReJQKlym1Iicc2ic7QqefOLl1nbnXLicWj2KQDJQic1PYwrYicibajDOHFGozSUeQTMJjrNIypK3A14QZKiaf3vT0HxnA/0","isAssist":false,"fromUid":17947,"toUid":"80623","fromName":"里里","toName":"连载用户355408","isFloor":true,"content":"合作什么呢？可以发邮件到lili@lianzai.com","createTime":1517982489},{"id":19072,"avatar":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJYDTX98iaDyL4QzZN0Oy3uC3Mibzxz03bbicmYanZPLVC2B6DBp8QBfmrBYS4rtibPibyZsk82vadicv3g/132","isAssist":false,"fromUid":28165,"toUid":"17947","fromName":"zqzu","toName":"里里","isFloor":true,"content":"您好，婚链可以和你们合作吗？微信xjlhl2，跟您联系","createTime":1519526154},{"id":24748,"avatar":"https://static.lianzai.com/avatar/1520218351618.png","isAssist":false,"fromUid":36992,"toUid":"17947","fromName":"大狼狗1","toName":"里里","isFloor":true,"content":"打撒多撒多","createTime":1521880722},{"id":24777,"avatar":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epqvBriaiaOqyOwibOLPdh9uDiasdHTPicU7tK94adkQ3uYXW0g3x5pymJy9UkNQlF0TaV8eCgt37fb8IA/0","isAssist":false,"fromUid":18050,"toUid":"80623","fromName":"Cyril Van","toName":"连载用户355408","isFloor":true,"content":"你们都是这样做的","createTime":1522130946}]
     * last_page : 1
     * current_page : 1
     * prev_page_url : null
     * next_page_url : null
     * per_page : 10
     * total : 4
     */

    private int status_code;
    private CommentBean comment;
    private int last_page;
    private int current_page;
    private Object prev_page_url;
    private Object next_page_url;
    private String per_page;
    private int total;
    private List<RepliesBean> replies;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
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

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
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

    public List<RepliesBean> getReplies() {
        return replies;
    }

    public void setReplies(List<RepliesBean> replies) {
        this.replies = replies;
    }

    public static class CommentBean {
        /**
         * id : 12754
         * avatar :
         * name : 连载用户355408
         * content : 我想跟平台谈合作的事，请问可以帮我对接吗
         * uid : 80623
         * barId : 211
         * postId : 99216
         * floor : 2
         * assistCount : 5
         * isAssist : false
         * replyCount : 4
         * isDeleteAble : false
         * createTime : 1517962319
         */

        private String id;
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
        private int createTime;



        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }

    public static class RepliesBean {
        /**
         * id : 12884
         * avatar : http://wx.qlogo.cn/mmopen/vi_32/ReJQKlym1Iicc2ic7QqefOLl1nbnXLicWj2KQDJQic1PYwrYicibajDOHFGozSUeQTMJjrNIypK3A14QZKiaf3vT0HxnA/0
         * isAssist : false
         * fromUid : 17947
         * toUid : 80623
         * fromName : 里里
         * toName : 连载用户355408
         * isFloor : true
         * content : 合作什么呢？可以发邮件到lili@lianzai.com
         * createTime : 1517982489
         */

        private int id;
        private int commentId;
        private String avatar;
        private boolean isAssist;
        private int assistCount;
        private int fromUid;
        private String toUid;
        private String fromName;
        private String toName;
        private boolean isFloor;
        private String content;
        private int createTime;
        private boolean isDeleteAble;
        private int floor;

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public boolean isDeleteAble() {
            return isDeleteAble;
        }

        public void setDeleteAble(boolean deleteAble) {
            isDeleteAble = deleteAble;
        }

        public int getAssistCount() {
            return assistCount;
        }

        public void setAssistCount(int assistCount) {
            this.assistCount = assistCount;
        }

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

        public boolean isIsAssist() {
            return isAssist;
        }

        public void setIsAssist(boolean isAssist) {
            this.isAssist = isAssist;
        }

        public int getFromUid() {
            return fromUid;
        }

        public void setFromUid(int fromUid) {
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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }
}
