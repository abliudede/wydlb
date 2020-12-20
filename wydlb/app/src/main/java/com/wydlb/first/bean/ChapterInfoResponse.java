package com.wydlb.first.bean;

/**
 * Created by lrz on 2018/3/27.
 */

public class ChapterInfoResponse {

    /**
     * status_code : 200
     * message : 信息获取成功
     * replyInfo : {"count":72,"postId":99216,"isReplyAble":true}
     * novel : {"genre":" 短篇","id":211,"title":"连载网官方指南","penName":"连载熊","cover":"https://static.lianzai.com/updatecover/211.jpg","intro":"连载网官方指南","shareUrl":"beta.m.lianzai.com/c/3735916174"}
     */

    private int status_code;
    private String message;
    private ReplyInfoBean replyInfo;
    private NovelBean novel;

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

    public ReplyInfoBean getReplyInfo() {
        return replyInfo;
    }

    public void setReplyInfo(ReplyInfoBean replyInfo) {
        this.replyInfo = replyInfo;
    }

    public NovelBean getNovel() {
        return novel;
    }

    public void setNovel(NovelBean novel) {
        this.novel = novel;
    }

    public static class ReplyInfoBean {
        /**
         * count : 72
         * postId : 99216
         * isReplyAble : true
         */

        private String count;
        private int postId;
        private String barId;
        private boolean isReplyAble;

        public String getBarId() {
            return barId;
        }

        public void setBarId(String barId) {
            this.barId = barId;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public boolean isIsReplyAble() {
            return isReplyAble;
        }

        public void setIsReplyAble(boolean isReplyAble) {
            this.isReplyAble = isReplyAble;
        }
    }

    public static class NovelBean {
        /**
         * genre :  短篇
         * id : 211
         * title : 连载网官方指南
         * penName : 连载熊
         * cover : https://static.lianzai.com/updatecover/211.jpg
         * intro : 连载网官方指南
         * shareUrl : beta.m.lianzai.com/c/3735916174
         */

        private String genre;
        private int id;
        private String title;
        private String penName;
        private String cover;
        private String intro;
        private String shareUrl;

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
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

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }
}
