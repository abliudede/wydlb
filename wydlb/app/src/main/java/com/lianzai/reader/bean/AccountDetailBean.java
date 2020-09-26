package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2017/10/21.
 */

public class AccountDetailBean extends BaseBean implements Serializable{


    /**
     * data : {"authStatus":0,"authorStatus":0,"avatar":"string","birthday":"2019-01-07T05:56:59.696Z","cardNo":"string","countryCode":"string","email":"string","gender":0,"hasPayPwd":false,"introduce":"string","invitationCode":"string","isHideReadLike":0,"isHideRecentRead":0,"isLimitChat":0,"isMember":false,"isShowRed":false,"mineStatus":0,"mineTime":"2019-01-07T05:56:59.696Z","mobile":"string","modifyNickname":false,"modifyNumber":0,"nickName":"string","realName":"string","role":0,"signature":"string","site":"string","status":0,"type":0,"uid":0}
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
         * authStatus : 0
         * authorStatus : 0
         * avatar : string
         * birthday : 2019-01-07T05:56:59.696Z
         * cardNo : string
         * countryCode : string
         * email : string
         * gender : 0
         * hasPayPwd : false
         * introduce : string
         * invitationCode : string
         * isHideReadLike : 0
         * isHideRecentRead : 0
         * isLimitChat : 0
         * isMember : false
         * isShowRed : false
         * mineStatus : 0
         * mineTime : 2019-01-07T05:56:59.696Z
         * mobile : string
         * modifyNickname : false
         * modifyNumber : 0
         * nickName : string
         * realName : string
         * role : 0
         * signature : string
         * site : string
         * status : 0
         * type : 0
         * uid : 0
         */

        private int authStatus;
        private int authorStatus;
        private String avatar;
        private String birthday;
        private String cardNo;
        private String countryCode;
        private String email;
        private int gender;
        private boolean hasPayPwd;
        private String introduce;
        private String invitationCode;
        private int isHideReadLike;
        private int isHideRecentRead;
        private int isLimitChat;
        private boolean isMember;
        private boolean isShowRed;
        private int mineStatus;
        private String mineTime;
        private String mobile;
        private boolean modifyNickname;
        private int modifyNumber;
        private String nickName;
        private String realName;
        private int role;
        private String signature;
        private String site;
        private int status;
        private int type;
        private String uid;
        private int isStartReplyPush;

        public int getIsStartReplyPush() {
            return isStartReplyPush;
        }

        public void setIsStartReplyPush(int isStartReplyPush) {
            this.isStartReplyPush = isStartReplyPush;
        }

        public int getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(int authStatus) {
            this.authStatus = authStatus;
        }

        public int getAuthorStatus() {
            return authorStatus;
        }

        public void setAuthorStatus(int authorStatus) {
            this.authorStatus = authorStatus;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public boolean isHasPayPwd() {
            return hasPayPwd;
        }

        public void setHasPayPwd(boolean hasPayPwd) {
            this.hasPayPwd = hasPayPwd;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }

        public int getIsHideReadLike() {
            return isHideReadLike;
        }

        public void setIsHideReadLike(int isHideReadLike) {
            this.isHideReadLike = isHideReadLike;
        }

        public int getIsHideRecentRead() {
            return isHideRecentRead;
        }

        public void setIsHideRecentRead(int isHideRecentRead) {
            this.isHideRecentRead = isHideRecentRead;
        }

        public int getIsLimitChat() {
            return isLimitChat;
        }

        public void setIsLimitChat(int isLimitChat) {
            this.isLimitChat = isLimitChat;
        }

        public boolean isIsMember() {
            return isMember;
        }

        public void setIsMember(boolean isMember) {
            this.isMember = isMember;
        }

        public boolean isIsShowRed() {
            return isShowRed;
        }

        public void setIsShowRed(boolean isShowRed) {
            this.isShowRed = isShowRed;
        }

        public int getMineStatus() {
            return mineStatus;
        }

        public void setMineStatus(int mineStatus) {
            this.mineStatus = mineStatus;
        }

        public String getMineTime() {
            return mineTime;
        }

        public void setMineTime(String mineTime) {
            this.mineTime = mineTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isModifyNickname() {
            return modifyNickname;
        }

        public void setModifyNickname(boolean modifyNickname) {
            this.modifyNickname = modifyNickname;
        }

        public int getModifyNumber() {
            return modifyNumber;
        }

        public void setModifyNumber(int modifyNumber) {
            this.modifyNumber = modifyNumber;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
