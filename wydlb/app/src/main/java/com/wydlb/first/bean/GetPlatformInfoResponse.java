package com.wydlb.first.bean;

import com.wydlb.first.base.Constant;
import com.wydlb.first.utils.RxTool;

import java.io.Serializable;

public class GetPlatformInfoResponse extends  BaseBean{


    /**
     * data : {"id":1,"platformType":1,"platformCover":"http://imgsrc.baidu.com/imgad/pic/item/34fae6cd7b899e51fab3e9c048a7d933c8950d21.jpg","platformName":"连载安全助手","platformNum":"LZblr12DBH","platformIntroduce":"陈默有一天得到了一只智能手表，水电费水电费沙发沙发第三方胜多负少发送到发的说法水电费安抚阿斯顿放撒旦法阿斯顿放撒旦法撒旦法撒旦法撒旦法撒旦法撒旦法是的发斯蒂芬第三方打饭但是他平凡而悲催的人生开始发生改变\u2026\u2026","isConcern":true,"bookId":0,"userType":0,"penName":"null","categoryName":"null","isMining":true,"source":"null","lastestChapterTitle":"null","lastestChapterUpdateAt":"null"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * platformType : 1
         * platformCover : http://imgsrc.baidu.com/imgad/pic/item/34fae6cd7b899e51fab3e9c048a7d933c8950d21.jpg
         * platformName : 连载安全助手
         * platformNum : LZblr12DBH
         * platformIntroduce : 陈默有一天得到了一只智能手表，水电费水电费沙发沙发第三方胜多负少发送到发的说法水电费安抚阿斯顿放撒旦法阿斯顿放撒旦法撒旦法撒旦法撒旦法撒旦法撒旦法是的发斯蒂芬第三方打饭但是他平凡而悲催的人生开始发生改变……
         * isConcern : true
         * bookId : 0
         * userType : 0
         * penName : null
         * categoryName : null
         * isMining : true
         * source : null
         * lastestChapterTitle : null
         * lastestChapterUpdateAt : null
         */

        private int id;
        private int platformType;
        private String platformCover;
        private String platformName;
        private String platformNum;
        private String platformIntroduce;
        private boolean isConcern;
        private int bookId;
        private int userType;
        private String penName;
        private String categoryName;
        private boolean isMining;
        private String typeText;

        private String source;
        private String lastestChapterTitle;
        private Long lastestChapterUpdateAt;

        private String lastestChapterId;
        private long teamId;
        private long teamId2;
        private boolean isInTeam;

        private boolean hidden;

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public long getTeamId() {
            return teamId;
        }

        public void setTeamId(long teamId) {
            this.teamId = teamId;
        }

        public long getTeamId2() {
            return teamId2;
        }

        public void setTeamId2(long teamId2) {
            this.teamId2 = teamId2;
        }

        public boolean isInTeam() {
            return isInTeam;
        }

        public void setInTeam(boolean inTeam) {
            isInTeam = inTeam;
        }

        public String getLastestChapterId() {
            return lastestChapterId;
        }

        public void setLastestChapterId(String lastestChapterId) {
            this.lastestChapterId = lastestChapterId;
        }

        private int roomId;

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        private String accid;
        private String shareUrl;

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
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

        public String getPlatformNum() {
            return platformNum;
        }

        public void setPlatformNum(String platformNum) {
            this.platformNum = platformNum;
        }

        public String getPlatformIntroduce() {
            return RxTool.filterChar(platformIntroduce);
        }

        public void setPlatformIntroduce(String platformIntroduce) {
            this.platformIntroduce = platformIntroduce;
        }

        public boolean isIsConcern() {
            return isConcern;
        }

        public void setIsConcern(boolean isConcern) {
            this.isConcern = isConcern;
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

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public boolean isIsMining() {
            return isMining;
        }

        public void setIsMining(boolean isMining) {
            this.isMining = isMining;
        }


        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getLastestChapterTitle() {
            return lastestChapterTitle;
        }

        public void setLastestChapterTitle(String lastestChapterTitle) {
            this.lastestChapterTitle = lastestChapterTitle;
        }

        public Long getLastestChapterUpdateAt() {
            return lastestChapterUpdateAt;
        }

        public void setLastestChapterUpdateAt(Long lastestChapterUpdateAt) {
            this.lastestChapterUpdateAt = lastestChapterUpdateAt;
        }
    }
}
