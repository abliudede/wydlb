package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;

public class ImLianzaihaoAttachment extends CustomAttachment {



    private final String KEY_BOOKID = "bookId";
    private final String KEY_PLATFORM_ID = "platformId";
    private final String KEY_PLATFORM_NAME = "platformName";
    private final String KEY_PLATFORM_COVER = "platformCover";
    private final String KEY_PEN_NAME = "penName";

    private String bookId;
    private String platformId;
    private String platformName;
    private String platformCover;
    private String penName;


    public ImLianzaihaoAttachment() {
        super(CustomAttachmentType.LIANZAIHAO);
    }

    public ImLianzaihaoAttachment(String bookId, String platformId,String platformName,String platformCover,String penName ) {
        this();
        this.bookId = bookId;
        this.platformId=platformId;
        this.platformName=platformName;
        this.platformCover=platformCover;
        this.penName=penName;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.bookId = data.getString(KEY_BOOKID);
        this.platformId = data.getString(KEY_PLATFORM_ID);
        this.platformName = data.getString(KEY_PLATFORM_NAME);
        this.platformCover = data.getString(KEY_PLATFORM_COVER);
        this.penName = data.getString(KEY_PEN_NAME);
    }


    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_BOOKID, bookId);
        data.put(KEY_PLATFORM_ID, platformId);
        data.put(KEY_PLATFORM_COVER, platformCover);
        data.put(KEY_PLATFORM_NAME, platformName);
        data.put(KEY_PEN_NAME, penName);
        return data;
    }

    public String getBookId() {
        return bookId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformCover() {
        return platformCover;
    }

    public String getPenName() {
        return penName;
    }
}
