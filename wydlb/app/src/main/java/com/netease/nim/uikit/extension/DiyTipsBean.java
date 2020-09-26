package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class DiyTipsBean extends CustomAttachment {

    private final String ACTION = "action";
    private final String HEAD = "head";
    private final String NICKNAME = "nickName";
    private final String REWARDVALUE = "rewardValue";
    private final String REWARDNAME = "rewardName";
    private final String REWARDIMG = "rewardImg";
    private final String COUNT = "count";
    private final String TIME = "time";
    private final String URL = "url";
//    {"endTime":1541498365380,"gameId":15,"threshold":50}

    private String action;
    private String head;
    private String nickName;
    private String rewardValue;
    private String rewardName;
    private String rewardImg;
    private int count;
    private String time;
    private String url;

    public DiyTipsBean(int type) {
        super(type);
    }

    public DiyTipsBean(String action,int type,String head, String nickName,String rewardValue,String rewardName,String rewardImg
    ,int count,String time,String url) {
        this(type);
        this.action = action;
        this.head = head;
        this.nickName = nickName;
        this.rewardValue = rewardValue;
        this.rewardName = rewardName;
        this.rewardImg = rewardImg;
        this.count = count;
        this.time = time;
        this.url = url;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.action = data.getString(ACTION);
        this.head = data.getString(HEAD);
        this.nickName = data.getString(NICKNAME);
        this.rewardValue = data.getString(REWARDVALUE);
        this.rewardName = data.getString(REWARDNAME);
        this.rewardImg = data.getString(REWARDIMG);
        this.count = data.getInteger(COUNT);
        this.time = data.getString(TIME);
        this.url = data.getString(URL);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(ACTION, action);
        data.put(HEAD, head);
        data.put(NICKNAME, nickName);
        data.put(REWARDVALUE, rewardValue);
        data.put(REWARDNAME, rewardName);
        data.put(REWARDIMG, rewardImg);
        data.put(COUNT, count);
        data.put(TIME, time);
        data.put(URL, url);
        return data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardImg() {
        return rewardImg;
    }

    public void setRewardImg(String rewardImg) {
        this.rewardImg = rewardImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}