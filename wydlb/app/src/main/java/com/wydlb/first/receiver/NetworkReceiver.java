package com.wydlb.first.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.wydlb.first.base.Constant;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLogTool;

/**
 * Copyright (C), 2018
 * FileName: NetworkReceiver
 * Author: lrz
 * Date: 2018/10/31 11:43
 * Description: ${DESCRIPTION}
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            RxLogTool.e("NetworkReceiver, wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        RxLogTool.e("NetworkReceiver, getConnectionType(info.getType()) 连上");
                        RxEventBusTool.sendEvents(Constant.EventTag.NETWORK_CONNECT_TAG);
                    }
                } else {
                    RxLogTool.e("NetworkReceiver,getConnectionType(info.getType()) 断开");
                    RxEventBusTool.sendEvents(Constant.EventTag.NETWORK_DISCONNECT_TAG);
                }
            }
        }
    }
}
