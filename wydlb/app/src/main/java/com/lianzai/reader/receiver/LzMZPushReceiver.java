package com.lianzai.reader.receiver;

import android.content.Context;

import com.lianzai.reader.utils.RxLogTool;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * Copyright (C), 2018
 * FileName: LzMZPushReceiver
 * Author: lrz
 * Date: 2018/11/20 16:56
 * Description: ${DESCRIPTION}
 */
public class LzMZPushReceiver extends MzPushMessageReceiver {
    @Override
    public void onRegister(Context context, String s) {

    }

    @Override
    public void onUnRegister(Context context, boolean b) {

    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {

    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        RxLogTool.e("LzMZPushReceiver:"+mzPushMessage.getSelfDefineContentString());
    }
}
