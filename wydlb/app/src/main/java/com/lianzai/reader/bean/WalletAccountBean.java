package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/4/26.
 */

public class WalletAccountBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 9
         * userId : 16660
         * realName : 张学友
         * mobile : null
         * accountType : 1
         * accNo : oUgVH1GGE_Cf266Oo22cmK-TkKFQ
         * status : 0
         * hasDefault : 0
         */

        private int id;
        private int userId;
        private String realName;
        private String mobile;
        private int accountType;
        private String accNo;
        private int status;
        private int hasDefault;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getHasDefault() {
            return hasDefault;
        }

        public void setHasDefault(int hasDefault) {
            this.hasDefault = hasDefault;
        }
    }
}
