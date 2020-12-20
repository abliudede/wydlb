package com.wydlb.first.bean;

import java.util.List;

public class LuckLaunchBean {


    /**
     * code : 0
     * msg : success
     * data : {"thresholdList":[5,10,50],"balance":99985,"rule":"规则：\r\n\r\n在发起游戏2分钟后，游戏结束。从参与玩家中，随机抽取1人作为中奖玩家，独得所有奖励。若1分钟后，没其余玩家参与，奖励返回到发起玩家钱包"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * thresholdList : [5,10,50]
         * balance : 99985.0
         * rule : 规则：

         在发起游戏2分钟后，游戏结束。从参与玩家中，随机抽取1人作为中奖玩家，独得所有奖励。若1分钟后，没其余玩家参与，奖励返回到发起玩家钱包
         */

        private double balance;
        private String rule;
        private String title;
        private List<Integer> thresholdList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public List<Integer> getThresholdList() {
            return thresholdList;
        }

        public void setThresholdList(List<Integer> thresholdList) {
            this.thresholdList = thresholdList;
        }
    }
}
