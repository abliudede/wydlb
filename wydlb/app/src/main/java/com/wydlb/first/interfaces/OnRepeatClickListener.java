package com.wydlb.first.interfaces;

import android.view.View;

import com.wydlb.first.utils.RxTool;


/**
 * Created by Vondear on 2017/7/24.
 * 重复点击的监听器
 */

public abstract class OnRepeatClickListener implements View.OnClickListener {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;

    public abstract void onRepeatClick(View v);

    @Override
    public void onClick(View v) {
        if (!RxTool.isFastClick(MIN_CLICK_DELAY_TIME)) {
            onRepeatClick(v);
        }else{
            return;
        }
    }

}
