package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;
import com.lianzai.reader.utils.RxLogTool;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public abstract class CustomAttachment implements MsgAttachment {

    protected int type;

    CustomAttachment(int type) {
        this.type = type;
    }

    private int newsType;

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            RxLogTool.e("CustomAttachment fromJson:"+data.toJSONString());
            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        RxLogTool.e("CustomAttachment source:"+ packData().toJSONString());
        return CustomAttachParser.packData(type, packData());
    }

    public int getType() {
        return type;
    }

    protected abstract void parseData(JSONObject data);

    protected abstract JSONObject packData();
}
