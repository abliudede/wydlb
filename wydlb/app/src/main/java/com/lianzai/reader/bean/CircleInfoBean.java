package com.lianzai.reader.bean;

import com.lianzai.reader.base.Constant;

import java.util.List;

public class CircleInfoBean {

    /**
     * code : 0
     * msg : success
     * data : {"id":459003,"platformType":2,"platformCover":"https://static.lianzai.com/cover/1552276792432.jpg?W75XH100","platformName":"樱木子小说","platformIntroduce":"流莺舞蝶两相欺，不取花芳正结时。\n他日未开今日谢，嘉辰长短是参差。","penName":"樱木子","isEnter":true,"bookId":515611,"userType":3,"teamId":1519357637,"isInTeam":true,"memberCount":25,"categoryName":"青春校园","typeText":"连载中","chapterCount":251,"wordCount":28086,"hidden":false}
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
         * id : 459003
         * platformType : 2
         * platformCover : https://static.lianzai.com/cover/1552276792432.jpg?W75XH100
         * platformName : 樱木子小说
         * platformIntroduce : 流莺舞蝶两相欺，不取花芳正结时。
         他日未开今日谢，嘉辰长短是参差。
         * penName : 樱木子
         * isEnter : true
         * bookId : 515611
         * userType : 3
         * teamId : 1519357637
         * isInTeam : true
         * memberCount : 25
         * categoryName : 青春校园
         * typeText : 连载中
         * chapterCount : 251
         * wordCount : 28086
         * hidden : false
         */

        private int id;
        private int platformType;
        private String platformCover;
        private String platformName;
        private String platformIntroduce;
        private String penName;
        private boolean isEnter;
        private int bookId;
        private int userType;
        private int teamId;
        private boolean isInTeam;
        private int memberCount;
        private String categoryName;
        private String typeText;
        private int chapterCount;
        private int wordCount;
        private boolean hidden;
        private String accid;
        private int status;
        private boolean isShowBookCircle;
        private boolean isCopyright;
        private List<String> copyrightInfoList;
        private String skipUrl;
        private int mayApplyRole;
        private long bookReadTime;
        private String joinTime;
        private boolean isShowMining;
        private List<String> writeInfoList;
        private int recommendCount;
        private boolean isShowArticleTab;
        private int copyrightStatus;
        private boolean isSigned;//"是否签约"
        private String signUrl;//"签约地址查看"

        public boolean isSigned() {
            return isSigned;
        }

        public void setSigned(boolean signed) {
            isSigned = signed;
        }

        public String getSignUrl() {
            return signUrl;
        }

        public void setSignUrl(String signUrl) {
            this.signUrl = signUrl;
        }

        public int getCopyrightStatus() {
            return copyrightStatus;
        }

        public void setCopyrightStatus(int copyrightStatus) {
            this.copyrightStatus = copyrightStatus;
        }

        public boolean isShowArticleTab() {
            return isShowArticleTab;
        }

        public void setShowArticleTab(boolean showArticleTab) {
            isShowArticleTab = showArticleTab;
        }

        public int getRecommendCount() {
            return recommendCount;
        }

        public void setRecommendCount(int recommendCount) {
            this.recommendCount = recommendCount;
        }

        public boolean isShowMining() {
            return isShowMining;
        }

        public void setShowMining(boolean showMining) {
            isShowMining = showMining;
        }

        public long getBookReadTime() {
            return bookReadTime;
        }

        public void setBookReadTime(long bookReadTime) {
            this.bookReadTime = bookReadTime;
        }

        public String getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(String joinTime) {
            this.joinTime = joinTime;
        }

        public int getMayApplyRole() {
            return mayApplyRole;
        }

        public void setMayApplyRole(int mayApplyRole) {
            this.mayApplyRole = mayApplyRole;
        }

        public String getSkipUrl() {
            return skipUrl;
        }

        public void setSkipUrl(String skipUrl) {
            this.skipUrl = skipUrl;
        }

        public List<String> getCopyrightInfoList() {
            return copyrightInfoList;
        }

        public void setCopyrightInfoList(List<String> copyrightInfoList) {
            this.copyrightInfoList = copyrightInfoList;
        }

        public List<String> getWriteInfoList() {
            return writeInfoList;
        }

        public void setWriteInfoList(List<String> writeInfoList) {
            this.writeInfoList = writeInfoList;
        }

        public boolean isCopyright() {
            return isCopyright;
        }

        public void setCopyright(boolean copyright) {
            isCopyright = copyright;
        }

        public boolean isShowBookCircle() {
            return isShowBookCircle;
        }

        public void setShowBookCircle(boolean showBookCircle) {
            isShowBookCircle = showBookCircle;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformIntroduce() {
            return platformIntroduce;
        }

        public void setPlatformIntroduce(String platformIntroduce) {
            this.platformIntroduce = platformIntroduce;
        }

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
        }

        public boolean isIsEnter() {
            return isEnter;
        }

        public void setIsEnter(boolean isEnter) {
            this.isEnter = isEnter;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getUserType() {
            if(userType == 0){
                return Constant.Role.FANS_ROLE;
            }else {
                return userType;
            }
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public boolean isIsInTeam() {
            return isInTeam;
        }

        public void setIsInTeam(boolean isInTeam) {
            this.isInTeam = isInTeam;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }

        public int getChapterCount() {
            return chapterCount;
        }

        public void setChapterCount(int chapterCount) {
            this.chapterCount = chapterCount;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }
    }
}
