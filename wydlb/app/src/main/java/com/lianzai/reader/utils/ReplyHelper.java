package com.lianzai.reader.utils;

import com.netease.nimlib.sdk.msg.model.IMMessage;

public class ReplyHelper {

    public ReplyHelper(){

    }

    private IMMessage message;

    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }
}
