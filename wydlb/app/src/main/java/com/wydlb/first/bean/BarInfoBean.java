package com.wydlb.first.bean;

import com.wydlb.first.utils.RxTool;

import java.io.Serializable;

/**
 * Created by lrz on 2018/1/3.
 */

public class BarInfoBean implements Serializable {
    /**
     * status_code : 200
     * message : 信息获取成功
     * novel : {"barId":211,"title":"连载网官方指南","novelId":211,"penName":"连载熊","cover":"https://static.lianzai.com/updatecover/211.jpg!W150XH200","intro":"以下连载网官方指南中的全部问题，今后可能会有所调整！不定期更新哟~欢迎大家积极踊跃的留言沟通建议，我们将在第一时间处理您的建议和问题。","memberCount":6941,"isReward":false,"wordNum":6032,"genre":" 短篇","chapterCount":8}
     * isLogin : false
     * uid : 19382
     * isFollow : false
     * isSign : false
     */

    private int status_code;
    private String message;
    private NovelBean novel;
    private boolean isLogin;
    private int uid;
    private boolean isFollow;
    private boolean isSign;

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

    public NovelBean getNovel() {
        return novel;
    }

    public void setNovel(NovelBean novel) {
        this.novel = novel;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public boolean isIsSign() {
        return isSign;
    }

    public void setIsSign(boolean isSign) {
        this.isSign = isSign;
    }

    public static class NovelBean {
        /**
         * barId : 211
         * title : 连载网官方指南
         * novelId : 211
         * penName : 连载熊
         * cover : https://static.lianzai.com/updatecover/211.jpg!W150XH200
         * intro : 以下连载网官方指南中的全部问题，今后可能会有所调整！不定期更新哟~欢迎大家积极踊跃的留言沟通建议，我们将在第一时间处理您的建议和问题。
         * memberCount : 6941
         * isReward : false
         * wordNum : 6032
         * genre :  短篇
         * chapterCount : 8
         */

        private int barId;
        private String title;
        private int novelId;
        private String penName;
        private String cover;
        private String intro;
        private int memberCount;
        private boolean isReward;
        private int wordNum;
        private String genre;
        private int chapterCount;
        private String shareUrl;
        private String numberDays;

        private String reportUrl;

        public String getNumberDays() {
            return numberDays;
        }

        public void setNumberDays(String numberDays) {
            this.numberDays = numberDays;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }

        public int getBarId() {
            return barId;
        }

        public void setBarId(int barId) {
            this.barId = barId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNovelId() {
            return String.valueOf(novelId);
        }

        public void setNovelId(int novelId) {
            this.novelId = novelId;
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

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public boolean isIsReward() {
            return isReward;
        }

        public void setIsReward(boolean isReward) {
            this.isReward = isReward;
        }

        public String getWordNum() {
            return RxTool.getWordNumFormat(String.valueOf(wordNum));
        }

        public void setWordNum(int wordNum) {
            this.wordNum = wordNum;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getChapterCount() {
            return chapterCount;
        }

        public void setChapterCount(int chapterCount) {
            this.chapterCount = chapterCount;
        }
    }
}
