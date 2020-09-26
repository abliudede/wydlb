package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/31.
 */

public class AuthRealNamesSubmitBean extends BaseBean {

    /**
     * data : {"status":422}
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
         * status : 422
         */

        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
