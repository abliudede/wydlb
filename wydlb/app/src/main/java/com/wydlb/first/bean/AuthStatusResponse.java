package com.wydlb.first.bean;

/**
 * Created by lrz on 2018/4/9.
 */

public class AuthStatusResponse extends BaseBean {

    /**
     * data : {"backPic":"string","cardCode":"string","cardType":0,"facePic":"string","handPic":"string","name":"string","status":0,"type":0,"uid":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * backPic : string
         * cardCode : string
         * cardType : 0
         * facePic : string
         * handPic : string
         * name : string
         * status : 0
         * type : 0
         * uid : 0
         */

        private String backPic;
        private String cardCode;
        private int cardType;
        private String facePic;
        private String handPic;
        private String name;
        private int status;
        private int type;
        private int uid;

        public String getBackPic() {
            return backPic;
        }

        public void setBackPic(String backPic) {
            this.backPic = backPic;
        }

        public String getCardCode() {
            return cardCode;
        }

        public void setCardCode(String cardCode) {
            this.cardCode = cardCode;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public String getFacePic() {
            return facePic;
        }

        public void setFacePic(String facePic) {
            this.facePic = facePic;
        }

        public String getHandPic() {
            return handPic;
        }

        public void setHandPic(String handPic) {
            this.handPic = handPic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
