package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/31.
 */

public class UploadFileBean extends BaseBean {

    /**
     * data : {"imagePath":"http://authentication.b0.upaiyun.com/1509432835254.jpg"}
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
         * imagePath : http://authentication.b0.upaiyun.com/1509432835254.jpg
         */

        private String imagePath;

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
}
