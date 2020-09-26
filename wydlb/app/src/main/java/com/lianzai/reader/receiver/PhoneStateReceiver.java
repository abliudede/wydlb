package com.lianzai.reader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;

/**
 * Copyright (C), 2018
 * FileName: PhoneStateReceiver
 * Author: lrz
 * Date: 2018/11/29 09:24
 * Description: ${DESCRIPTION}
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        RxLogTool.e("PhoneStateReceiver action: " + action);

        String resultData = this.getResultData();
        RxLogTool.e("PhoneStateReceiver getResultData: " + resultData);


        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 去电，可以用定时挂断
            // 双卡的手机可能不走这个Action
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            RxLogTool.e("PhoneStateReceiver EXTRA_PHONE_NUMBER: " + phoneNumber);
        } else {
            // 来电去电都会走
            // 获取当前电话状态
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            RxLogTool.e("PhoneStateReceiver onReceive state: " + state);

            // 获取电话号码
            String extraIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            RxLogTool.e("PhoneStateReceiver onReceive extraIncomingNumber: " + extraIncomingNumber);

            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                RxEventBusTool.sendEvents(Constant.EventTag.CALL_STATE_RINGING);
                RxLogTool.e("PhoneStateReceiver onReceive EXTRA_STATE_RINGING");
            }else if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                RxEventBusTool.sendEvents(Constant.EventTag.CALL_STATE_IDLE_TAG);
                RxLogTool.e("PhoneStateReceiver onReceive EXTRA_STATE_IDLE");
            }
        }
    }
}
