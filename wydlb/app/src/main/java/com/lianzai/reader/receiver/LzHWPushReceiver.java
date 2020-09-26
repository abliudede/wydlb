package com.lianzai.reader.receiver;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.lianzai.reader.utils.RxLogTool;

/**
 * Copyright (C), 2018
 * FileName: LzHWPushReceiver
 * Author: lrz
 * Date: 2018/11/20 17:05
 * Description: ${DESCRIPTION}
 */
public class LzHWPushReceiver extends PushReceiver {
    public LzHWPushReceiver() {
        super();
    }


    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        super.onEvent(context, event, extras);
        RxLogTool.e("LzHWPushReceiver: "+event.name());
    }

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        super.onToken(context, token, extras);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
        return super.onPushMsg(context, msgBytes, extras);
    }

    @Override
    public void onPushMsg(Context context, byte[] msg, String token) {
        super.onPushMsg(context, msg, token);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        super.onPushState(context, pushState);
    }

    @Override
    public void onToken(Context context, String token) {
        super.onToken(context, token);
    }




}
