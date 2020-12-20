package com.wydlb.first.ui.activity;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.component.AppComponent;

import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/24.
 * 透明背景的锁屏页面
 */

public class ActivityLockScreen extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_join_rule;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        //解锁系统屏幕以及显示在系统锁屏之上
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.tv_options)void closeClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    //不响应系统back按键
    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext()
                .getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
//        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }
}
