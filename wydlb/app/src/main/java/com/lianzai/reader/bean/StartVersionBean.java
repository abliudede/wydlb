package com.lianzai.reader.bean;

public class StartVersionBean {


    /**
     * code : 0
     * data : {"advertisement":{"accessMode":0,"configId":0,"imgUrl":"string","jumpUrl":"string","sdkChannel":"string"},"advertisementFlage":false}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * advertisement : {"accessMode":0,"configId":0,"imgUrl":"string","jumpUrl":"string","sdkChannel":"string"}
         * advertisementFlage : false
         */

        private AdvertisementBean advertisement;
        private boolean advertisementFlage;

        public AdvertisementBean getAdvertisement() {
            return advertisement;
        }

        public void setAdvertisement(AdvertisementBean advertisement) {
            this.advertisement = advertisement;
        }

        public boolean isAdvertisementFlage() {
            return advertisementFlage;
        }

        public void setAdvertisementFlage(boolean advertisementFlage) {
            this.advertisementFlage = advertisementFlage;
        }

        public static class AdvertisementBean {
            /**
             * accessMode : 0
             * configId : 0
             * imgUrl : string
             * jumpUrl : string
             * sdkChannel : string
             */

            private int accessMode;
            private int configId;
            private String imgUrl;
            private String jumpUrl;
            private String sdkChannel;
            private int appLimitMinute;

            public int getAppLimitMinute() {
                return appLimitMinute;
            }

            public void setAppLimitMinute(int appLimitMinute) {
                this.appLimitMinute = appLimitMinute;
            }

            public int getAccessMode() {
                return accessMode;
            }

            public void setAccessMode(int accessMode) {
                this.accessMode = accessMode;
            }

            public int getConfigId() {
                return configId;
            }

            public void setConfigId(int configId) {
                this.configId = configId;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getJumpUrl() {
                return jumpUrl;
            }

            public void setJumpUrl(String jumpUrl) {
                this.jumpUrl = jumpUrl;
            }

            public String getSdkChannel() {
                return sdkChannel;
            }

            public void setSdkChannel(String sdkChannel) {
                this.sdkChannel = sdkChannel;
            }
        }
    }
}
