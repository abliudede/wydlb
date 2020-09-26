package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxDataTool;

import java.io.Serializable;

/**
 * Created by lrz on 2017/10/26.
 */

public class HongBaoDetailBean extends BaseBean {


    /**
     * data : {"isSend":true,"headInfo":{"redsList":{"phone":"15974125876","avatar":"","id":11,"tran_no":"GA23493936170825","from_uid":9,"to_uid":0,"amount":"2000.00000000","actual_amount":"0.00000000","publish_num":2,"actual_num":0,"tran_type":2,"status":1,"mark":"恭喜发财，大吉大利！","reason":null,"envelop":1,"validate_start":"2017-10-23 17:03:13","validate_end":"2017-10-24 17:03:13","created_at":"2017-10-23 17:03:13","updated_at":"2017-10-23 17:03:13","platform_amount":"0.00000000","deleted":0,"reception":null,"arbed":0,"refund":0}},"currTime":"2017-10-26 15:40:20"}
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
         * isSend : true
         * headInfo : {"redsList":{"phone":"15974125876","avatar":"","id":11,"tran_no":"GA23493936170825","from_uid":9,"to_uid":0,"amount":"2000.00000000","actual_amount":"0.00000000","publish_num":2,"actual_num":0,"tran_type":2,"status":1,"mark":"恭喜发财，大吉大利！","reason":null,"envelop":1,"validate_start":"2017-10-23 17:03:13","validate_end":"2017-10-24 17:03:13","created_at":"2017-10-23 17:03:13","updated_at":"2017-10-23 17:03:13","platform_amount":"0.00000000","deleted":0,"reception":null,"arbed":0,"refund":0}}
         * currTime : 2017-10-26 15:40:20
         */

        private boolean isSend;
        private HeadInfoBean headInfo;
        private String currTime;

        public boolean isIsSend() {
            return isSend;
        }

        public void setIsSend(boolean isSend) {
            this.isSend = isSend;
        }

        public HeadInfoBean getHeadInfo() {
            return headInfo;
        }

        public void setHeadInfo(HeadInfoBean headInfo) {
            this.headInfo = headInfo;
        }

        public String getCurrTime() {
            return currTime;
        }

        public void setCurrTime(String currTime) {
            this.currTime = currTime;
        }

        public static class HeadInfoBean implements Serializable{
            /**
             * redsList : {"phone":"15974125876","avatar":"","id":11,"tran_no":"GA23493936170825","from_uid":9,"to_uid":0,"amount":"2000.00000000","actual_amount":"0.00000000","publish_num":2,"actual_num":0,"tran_type":2,"status":1,"mark":"恭喜发财，大吉大利！","reason":null,"envelop":1,"validate_start":"2017-10-23 17:03:13","validate_end":"2017-10-24 17:03:13","created_at":"2017-10-23 17:03:13","updated_at":"2017-10-23 17:03:13","platform_amount":"0.00000000","deleted":0,"reception":null,"arbed":0,"refund":0}
             */

            private RedsListBean redsList;

            public RedsListBean getRedsList() {
                return redsList;
            }

            public void setRedsList(RedsListBean redsList) {
                this.redsList = redsList;
            }

            public static class RedsListBean {
                /**
                 * phone : 15974125876
                 * avatar :
                 * id : 11
                 * tran_no : GA23493936170825
                 * from_uid : 9
                 * to_uid : 0
                 * amount : 2000.00000000
                 * actual_amount : 0.00000000
                 * publish_num : 2
                 * actual_num : 0
                 * tran_type : 2
                 * status : 1
                 * mark : 恭喜发财，大吉大利！
                 * reason : null
                 * envelop : 1
                 * validate_start : 2017-10-23 17:03:13
                 * validate_end : 2017-10-24 17:03:13
                 * created_at : 2017-10-23 17:03:13
                 * updated_at : 2017-10-23 17:03:13
                 * platform_amount : 0.00000000
                 * deleted : 0
                 * reception : null
                 * arbed : 0
                 * refund : 0
                 */

                private String phone;
                private String avatar;
                private int id;
                private String tran_no;
                private int from_uid;
                private int to_uid;
                private String amount;
                private String actual_amount;
                private String publish_num;
                private String publish_amount;
                private String actual_num;
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

                private String nick_name;


                private  String detailAmount;

                public String getDetailAmount() {
                    return RxDataTool.isEmpty(detailAmount)?null:RxDataTool.getAmountValue(detailAmount);
                }

                public void setDetailAmount(String detailAmount) {
                    this.detailAmount = detailAmount;
                }

                public String getNick_name() {
                    return nick_name;
                }

                public void setNick_name(String nick_name) {
                    this.nick_name = nick_name;
                }

                public String getPhone() {
                    return RxDataTool.isEmpty(getNick_name())?RxDataTool.hideMobilePhone4(phone):getNick_name();
                }


                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

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

                public String getPublish_num() {
                    return publish_num;
                }

                public void setPublish_num(String publish_num) {
                    this.publish_num = publish_num;
                }

                public String getActual_num() {
                    return actual_num;
                }


                public String getPublish_amount() {
                    return RxDataTool.getAmountValue(publish_amount);
                }

                public void setPublish_amount(String publish_amount) {
                    this.publish_amount = publish_amount;
                }

                public void setActual_num(String actual_num) {
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
                    return RxDataTool.isEmpty(validate_end)?String.valueOf(System.currentTimeMillis()*1000):validate_end;
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
            }
        }
    }
}
