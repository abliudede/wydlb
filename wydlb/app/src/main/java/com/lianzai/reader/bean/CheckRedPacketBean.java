package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxDataTool;

/**
 * Created by lrz on 2017/11/14.
 */

public class CheckRedPacketBean extends BaseBean {


    /**
     * data : {"currTime":"2017-11-15 14:59:09","currUid":16660,"tranInfo":{"id":29,"tran_no":"GB15279746317192","from_uid":25991,"to_uid":0,"amount":"5000.00000000","actual_amount":"1000.00000000","publish_num":5,"actual_num":1,"tran_type":2,"status":1,"mark":"恭喜发财，大吉大利","reason":null,"envelop":1,"validate_start":"2017-11-15 14:41:37","validate_end":"2017-11-15 14:41:37","created_at":"2017-11-15 14:39:34","updated_at":"2017-11-15 14:39:34","platform_amount":"0.00000000","deleted":0,"reception":null,"arbed":0,"refund":0,"avatar":"http://wx.qlogo.cn/mmhead/nBcYE97sU44iaN9ZHxLG1WVtiaZPDhAr6hkTLOpx9oHbyYnXhJWu8tdg/0","phone":"15974125876","nickName":"勿忘心安"},"redStatusInfo":{"hadReceive":true,"redInfo":{"id":50,"imp_uid":16660,"imped_uid":25991,"tran_id":29,"amount":"1000.00000000","detail_type":2,"tran_type":2,"memo":"领红包","created_at":"2017-11-15 14:41:37","updated_at":"2017-11-15 14:41:37"}}}
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
         * currTime : 2017-11-15 14:59:09
         * currUid : 16660
         * tranInfo : {"id":29,"tran_no":"GB15279746317192","from_uid":25991,"to_uid":0,"amount":"5000.00000000","actual_amount":"1000.00000000","publish_num":5,"actual_num":1,"tran_type":2,"status":1,"mark":"恭喜发财，大吉大利","reason":null,"envelop":1,"validate_start":"2017-11-15 14:41:37","validate_end":"2017-11-15 14:41:37","created_at":"2017-11-15 14:39:34","updated_at":"2017-11-15 14:39:34","platform_amount":"0.00000000","deleted":0,"reception":null,"arbed":0,"refund":0,"avatar":"http://wx.qlogo.cn/mmhead/nBcYE97sU44iaN9ZHxLG1WVtiaZPDhAr6hkTLOpx9oHbyYnXhJWu8tdg/0","phone":"15974125876","nickName":"勿忘心安"}
         * redStatusInfo : {"hadReceive":true,"redInfo":{"id":50,"imp_uid":16660,"imped_uid":25991,"tran_id":29,"amount":"1000.00000000","detail_type":2,"tran_type":2,"memo":"领红包","created_at":"2017-11-15 14:41:37","updated_at":"2017-11-15 14:41:37"}}
         */

        private String currTime;
        private int currUid;
        private TranInfoBean tranInfo;
        private RedStatusInfoBean redStatusInfo;

        public String getCurrTime() {
            return currTime;
        }

        public void setCurrTime(String currTime) {
            this.currTime = currTime;
        }

        public int getCurrUid() {
            return currUid;
        }

        public void setCurrUid(int currUid) {
            this.currUid = currUid;
        }

        public TranInfoBean getTranInfo() {
            return tranInfo;
        }

        public void setTranInfo(TranInfoBean tranInfo) {
            this.tranInfo = tranInfo;
        }

        public RedStatusInfoBean getRedStatusInfo() {
            return redStatusInfo;
        }

        public void setRedStatusInfo(RedStatusInfoBean redStatusInfo) {
            this.redStatusInfo = redStatusInfo;
        }

        public static class TranInfoBean {
            /**
             * id : 29
             * tran_no : GB15279746317192
             * from_uid : 25991
             * to_uid : 0
             * amount : 5000.00000000
             * actual_amount : 1000.00000000
             * publish_num : 5
             * actual_num : 1
             * tran_type : 2
             * status : 1
             * mark : 恭喜发财，大吉大利
             * reason : null
             * envelop : 1
             * validate_start : 2017-11-15 14:41:37
             * validate_end : 2017-11-15 14:41:37
             * created_at : 2017-11-15 14:39:34
             * updated_at : 2017-11-15 14:39:34
             * platform_amount : 0.00000000
             * deleted : 0
             * reception : null
             * arbed : 0
             * refund : 0
             * avatar : http://wx.qlogo.cn/mmhead/nBcYE97sU44iaN9ZHxLG1WVtiaZPDhAr6hkTLOpx9oHbyYnXhJWu8tdg/0
             * phone : 15974125876
             * nickName : 勿忘心安
             */

            private int id;
            private String tran_no;
            private int from_uid;
            private int to_uid;
            private String amount;
            private String actual_amount;
            private int publish_num;
            private int actual_num;
            private int tran_type;
            private int status;
            private String mark;
            private Object reason;
            private int envelop;
            private String validate_start;
            private String validate_end;
            private String created_at;
            private String updated_at;
            private String platform_amount;
            private int deleted;
            private Object reception;
            private int arbed;
            private int refund;
            private String avatar;
            private String phone;
            private String nickName;

            public String getId() {
                return String.valueOf(id);
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTran_no() {
                return tran_no;
            }

            public void setTran_no(String tran_no) {
                this.tran_no = tran_no;
            }

            public int getFrom_uid() {
                return from_uid;
            }

            public void setFrom_uid(int from_uid) {
                this.from_uid = from_uid;
            }

            public int getTo_uid() {
                return to_uid;
            }

            public void setTo_uid(int to_uid) {
                this.to_uid = to_uid;
            }

            public String getAmount() {
                return RxDataTool.getAmountValue(amount);
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getActual_amount() {
                return actual_amount;
            }

            public void setActual_amount(String actual_amount) {
                this.actual_amount = actual_amount;
            }

            public int getPublish_num() {
                return publish_num;
            }

            public void setPublish_num(int publish_num) {
                this.publish_num = publish_num;
            }

            public int getActual_num() {
                return actual_num;
            }

            public void setActual_num(int actual_num) {
                this.actual_num = actual_num;
            }

            public int getTran_type() {
                return tran_type;
            }

            public void setTran_type(int tran_type) {
                this.tran_type = tran_type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public Object getReason() {
                return reason;
            }

            public void setReason(Object reason) {
                this.reason = reason;
            }

            public int getEnvelop() {
                return envelop;
            }

            public void setEnvelop(int envelop) {
                this.envelop = envelop;
            }

            public String getValidate_start() {
                return validate_start;
            }

            public void setValidate_start(String validate_start) {
                this.validate_start = validate_start;
            }

            public String getValidate_end() {
                return validate_end;
            }

            public void setValidate_end(String validate_end) {
                this.validate_end = validate_end;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getPlatform_amount() {
                return platform_amount;
            }

            public void setPlatform_amount(String platform_amount) {
                this.platform_amount = platform_amount;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public Object getReception() {
                return reception;
            }

            public void setReception(Object reception) {
                this.reception = reception;
            }

            public int getArbed() {
                return arbed;
            }

            public void setArbed(int arbed) {
                this.arbed = arbed;
            }

            public int getRefund() {
                return refund;
            }

            public void setRefund(int refund) {
                this.refund = refund;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
        }

        public static class RedStatusInfoBean {
            /**
             * hadReceive : true
             * redInfo : {"id":50,"imp_uid":16660,"imped_uid":25991,"tran_id":29,"amount":"1000.00000000","detail_type":2,"tran_type":2,"memo":"领红包","created_at":"2017-11-15 14:41:37","updated_at":"2017-11-15 14:41:37"}
             */

            private boolean hadReceive;
            private RedInfoBean redInfo;

            public boolean isHadReceive() {
                return hadReceive;
            }

            public void setHadReceive(boolean hadReceive) {
                this.hadReceive = hadReceive;
            }

            public RedInfoBean getRedInfo() {
                return redInfo;
            }

            public void setRedInfo(RedInfoBean redInfo) {
                this.redInfo = redInfo;
            }

            public static class RedInfoBean {
                /**
                 * id : 50
                 * imp_uid : 16660
                 * imped_uid : 25991
                 * tran_id : 29
                 * amount : 1000.00000000
                 * detail_type : 2
                 * tran_type : 2
                 * memo : 领红包
                 * created_at : 2017-11-15 14:41:37
                 * updated_at : 2017-11-15 14:41:37
                 */

                private int id;
                private int imp_uid;
                private int imped_uid;
                private int tran_id;
                private String amount;
                private int detail_type;
                private int tran_type;
                private String memo;
                private String created_at;
                private String updated_at;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getImp_uid() {
                    return imp_uid;
                }

                public void setImp_uid(int imp_uid) {
                    this.imp_uid = imp_uid;
                }

                public int getImped_uid() {
                    return imped_uid;
                }

                public void setImped_uid(int imped_uid) {
                    this.imped_uid = imped_uid;
                }

                public int getTran_id() {
                    return tran_id;
                }

                public void setTran_id(int tran_id) {
                    this.tran_id = tran_id;
                }

                public String getAmount() {
                    return RxDataTool.getAmountValue(amount);
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public int getDetail_type() {
                    return detail_type;
                }

                public void setDetail_type(int detail_type) {
                    this.detail_type = detail_type;
                }

                public int getTran_type() {
                    return tran_type;
                }

                public void setTran_type(int tran_type) {
                    this.tran_type = tran_type;
                }

                public String getMemo() {
                    return memo;
                }

                public void setMemo(String memo) {
                    this.memo = memo;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }
            }
        }
    }
}
