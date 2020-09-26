package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.common.util.file.FileUtil;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class FightLuckBean extends CustomAttachment {

    private final String GAMEID = "gameId";
    private final String ENDTIME = "endTime";
    private final String THRESSHOLD = "threshold";
    private final String TITLE = "title";
//    {"endTime":1541498365380,"gameId":15,"threshold":50}

    private int gameId;
    private long endTime;
    private int threshold;
    private String title;

    public FightLuckBean() {
        super(CustomAttachmentType.FIGHTLUCK);
    }

    public FightLuckBean(int gameId, long endTime,int threshold,String title) {
        this();
        this.gameId = gameId;
        this.endTime = endTime;
        this.threshold = threshold;
        this.title = title;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.gameId = data.getInteger(GAMEID);
        this.endTime = data.getLong(ENDTIME);
        this.threshold = data.getInteger(THRESSHOLD);
        this.title = data.getString(TITLE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(GAMEID, gameId);
        data.put(ENDTIME, endTime);
        data.put(THRESSHOLD, threshold);
        data.put(TITLE, title);
        return data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}