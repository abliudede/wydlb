package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class UrlBookBean extends CustomAttachment {

    private final String BOOKID = "bookId";
    private final String PLATFORMCOVER = "platformCover";
    private final String PLATFORMNAME = "platformName";
    private final String INTRO = "intro";
    private final String URL = "url";

    private int bookId;
    private String platformCover;
    private String platformName;
    private String intro;
    private String url;

    public UrlBookBean() {
        super(CustomAttachmentType.URLBOOK);
    }

    public UrlBookBean(int bookId,  String platformCover, String platformName, String intro, String url) {
        this();
        this.bookId = bookId;
        this.platformCover = platformCover;
        this.platformName = platformName;
        this.intro = intro;
        this.url = url;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.bookId = data.getInteger(BOOKID);
        this.platformCover = data.getString(PLATFORMCOVER);
        this.platformName = data.getString(PLATFORMNAME);
        this.intro = data.getString(INTRO);
        this.url = data.getString(URL);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(BOOKID, bookId);
        data.put(PLATFORMCOVER, platformCover);
        data.put(PLATFORMNAME, platformName);
        data.put(INTRO, intro);
        data.put(URL, url);
        return data;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getPlatformCover() {
        return platformCover;
    }

    public void setPlatformCover(String platformCover) {
        this.platformCover = platformCover;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}