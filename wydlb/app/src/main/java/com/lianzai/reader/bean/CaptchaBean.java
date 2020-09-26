package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/20.
 */

public class CaptchaBean extends BaseBean {


    /**
     * data : {"captchaBase64":"data:image/jpeg;base64,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","uuid":"DbdeAdB1"}
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
         * captchaBase64 : data:image/jpeg;base64,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
         * uuid : DbdeAdB1
         */

        private String captchaBase64;
        private String uuid;

        public String getCaptchaBase64() {
            return captchaBase64;
        }

        public void setCaptchaBase64(String captchaBase64) {
            this.captchaBase64 = captchaBase64;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}