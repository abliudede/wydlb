package com.wydlb.first.utils;

import com.wydlb.first.bean.DataSynEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lrz on 2017/10/21.
 */

public class RxEventBusTool {
    public  static void registerEventBus(Object obj){
        EventBus.getDefault().register(obj);
    }
    public  static void unRegisterEventBus(Object obj){
        EventBus.getDefault().unregister(obj);
    }
    public static  void sendEvents(Object obj){
        EventBus.getDefault().post(new DataSynEvent(obj));
    }
    public static  void sendEvents(DataSynEvent dataSynEvent){
        EventBus.getDefault().post(dataSynEvent);
    }
    public static  void sendEvents(int tag){
        EventBus.getDefault().post(new DataSynEvent(null,tag));
    }
    public static  void sendEvents(String obj,int tag){
        EventBus.getDefault().post(new DataSynEvent(obj,tag));
    }

    public static  void sendEvents(int obj,int tag){
        EventBus.getDefault().post(new DataSynEvent(obj,tag));
    }
    public static  void cancelDeliveryEvents(Object obj){
        EventBus.getDefault().post(new DataSynEvent(obj));
    }

}
