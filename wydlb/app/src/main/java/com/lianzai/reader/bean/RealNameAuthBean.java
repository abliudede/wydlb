package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/31.
 */

public class RealNameAuthBean extends BaseBean {


    /**
     * data : {"realName":"Jack","cardId":"43252419911014543X"}
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
         * realName : Jack
         * cardId : 43252419911014543X
         */

        private String realName;
        private String cardId;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }
    }
}
