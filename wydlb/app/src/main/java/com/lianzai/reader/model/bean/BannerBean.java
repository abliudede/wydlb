package com.lianzai.reader.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/2/2.
 */
public class BannerBean implements Serializable{

    private static final long serialVersionUID = -2334091146445087917L;


    /**
     * code : 0
     * data : [{"bannerPhoto":"string","bannerUrl":"string"}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bannerPhoto : string
         * bannerUrl : string
         */

        private String bannerPhoto;
        private String bannerUrl;
        private String headSlogan;
        private String tailSlogan;
        //新增倒计时
        private int countdown;
        private int showTimeLimit;
        private int isSpecialGame;
        private int needLogin;
        private String sdkChannel;
        private int type;
        private int id;
        private int putPosition;
        private int configId;

        public String getHeadSlogan() {
            return headSlogan;
        }

        public void setHeadSlogan(String headSlogan) {
            this.headSlogan = headSlogan;
        }

        public String getTailSlogan() {
            return tailSlogan;
        }

        public void setTailSlogan(String tailSlogan) {
            this.tailSlogan = tailSlogan;
        }

        public int getPutPosition() {
            return putPosition;
        }

        public void setPutPosition(int putPosition) {
            this.putPosition = putPosition;
        }

        public int getConfigId() {
            return configId;
        }

        public void setConfigId(int configId) {
            this.configId = configId;
        }

        public int getCountdown() {
            return countdown;
        }

        public void setCountdown(int countdown) {
            this.countdown = countdown;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsSpecialGame() {
            return isSpecialGame;
        }

        public void setIsSpecialGame(int isSpecialGame) {
            this.isSpecialGame = isSpecialGame;
        }

        public int getNeedLogin() {
            return needLogin;
        }

        public void setNeedLogin(int needLogin) {
            this.needLogin = needLogin;
        }

        public String getBannerPhoto() {
            return bannerPhoto;
        }

        public void setBannerPhoto(String bannerPhoto) {
            this.bannerPhoto = bannerPhoto;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }

        public String getSdkChannel() {
            return sdkChannel;
        }

        public void setSdkChannel(String sdkChannel) {
            this.sdkChannel = sdkChannel;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
