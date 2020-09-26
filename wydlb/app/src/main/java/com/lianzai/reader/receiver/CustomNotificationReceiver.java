package com.lianzai.reader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.CustomNotificationCache;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNotificationUntils;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

import java.util.HashMap;

/**
 * 自定义通知消息广播接收器
 */
public class CustomNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = context.getPackageName() + NimIntent.ACTION_RECEIVE_CUSTOM_NOTIFICATION;
        if (action.equals(intent.getAction())) {

            // 从intent中取出自定义通知
            CustomNotification notification = (CustomNotification) intent.getSerializableExtra(NimIntent.EXTRA_BROADCAST_MSG);
            try {
                JSONObject obj = JSONObject.parseObject(notification.getContent());
                if (obj != null && obj.getIntValue("id") == 2) {
                    // 加入缓存中
                    CustomNotificationCache.getInstance().addCustomNotification(notification);

                    // Toast
                    String content = obj.getString("content");
                    String tip = String.format("自定义消息[%s]：%s", notification.getFromAccount(), content);
                    RxToast.showToast(tip);
                }
            } catch (JSONException e) {
                RxLogTool.e("demo", e.getMessage());
            }

            try{
                //不在前台才会展示通知栏
                if(!RxAppTool.isAppForeground(context)){
                    HashMap map = (HashMap) notification.getPushPayload().get("ext");
                    if(map.get("action").equals(Constant.PushOpenAction.ENTERMYINFORM)){
                        RxNotificationUntils.showshowNotification(notification.getContent(),context,"notice");
                    }
                }
            }catch (Exception e){

            }

            // 处理自定义通知消息
            RxLogTool.e("demo", "receive custom notification: " + notification.getContent() + " from :" + notification.getSessionId() + "/" + notification.getSessionType());
        }
    }



}
