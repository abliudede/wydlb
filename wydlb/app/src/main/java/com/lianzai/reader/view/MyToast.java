package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.lianzai.reader.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (C), 2018
 * FileName: MyToast
 * Author: lrz
 * Date: 2018/10/13 15:27
 * Description: ${DESCRIPTION}
 */
public class MyToast {
    private WindowManager.LayoutParams mParams;
    private boolean mShowTime;
    private boolean mIsShow;
    private WindowManager mWdm;
    private TextView mToastView;
    private Timer mTimer;

    private MyToast(Context context, String text, boolean showTime) {
        mShowTime = showTime;//记录Toast的显示长短类型
        mIsShow = false;//记录当前Toast的内容是否已经在显示
        mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //通过Toast实例获取当前android系统的默认Toast的View布局
        mToastView =(TextView) LayoutInflater.from(context).inflate(R.layout.toast_layout,null);
        mToastView.setText(text);
        mTimer = new Timer();
        //设置布局参数
        setParams();
    }

    public static MyToast makeText(Context context, String text, boolean showTime) {
        MyToast result = new MyToast(context, text, showTime);
        return result;
    }

    private void setParams() {
        // TODO Auto-generated method stub
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.MyToast_Anim;//设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mParams.y = 200;
    }

    public void show() {
        if (!mIsShow) {//如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);//将其加载到windowManager上
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mWdm.removeView(mToastView);
                    mIsShow = false;
                }
            }, (long) (mShowTime ? 3500 : 2000));
        }
    }
}
