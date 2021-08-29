package com.wydlb.first.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.ui.activity.ActivityGuide;
import com.wydlb.first.ui.activity.ActivityLoginNew;
import com.wydlb.first.ui.activity.MainActivity;
import com.wydlb.first.ui.activity.PermissionActivity;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.RxToast;

import org.hashids.Hashids;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: SplashActivity
 * Author: lrz
 * Date: 2018/9/28 22:08
 * Description: ${DESCRIPTION}
 */
public class SplashActivity extends PermissionActivity{

    Hashids hashids;

    private ImageView app_logo;
    private ViewGroup container;
    private ImageView splashHolder;
    private TextView skip_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemBarUtils.readStatusBar1(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        skip_view = (TextView) findViewById(R.id.skip_view);
        app_logo = (ImageView) findViewById(R.id.app_logo);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);

        hashids = new Hashids("ds$#SDa", 8);

        if (!isTaskRoot()) {//5.0以上机器
            RxLogTool.e("SplashActivity isTaskRoot....");
            finish();
            return;
        }
        //加载数据开始
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        RxLogTool.e("onNewIntent......");
        setIntent(intent);
    }



    //没登录不能进入主页
    private void enterMainActivity() {
        if(RxLoginTool.isLogin()){
            MainActivity.startActivity(SplashActivity.this);
        }else {
            ActivityLoginNew.startActivity(SplashActivity.this);
        }
        finish();
    }

    private void initData() {
        if (Constant.appMode == Constant.AppMode.DEV) {
            RxToast.custom(getResources().getString(R.string.app_name) + "【开发环境】", Constant.ToastType.TOAST_NORMAL).show();
        } else if (Constant.appMode == Constant.AppMode.BETA) {
            RxToast.custom(getResources().getString(R.string.app_name) + "【测试环境】", Constant.ToastType.TOAST_NORMAL).show();
        }

        String versionName = RxAppTool.getAppVersionName(this);
        //此处做分支
        boolean needShowGuide = RxSharedPreferencesUtil.getInstance().getBoolean(Constant.START_PAGE_VERSION + versionName, true);
        if (needShowGuide) {
            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.START_PAGE_VERSION + versionName, false);
            if("3.0.3".equals(versionName)) {
                //只有3.0.3版本的首次更新，才会有特殊更新一次云端记录。
                RxSharedPreferencesUtil.getInstance().putBoolean(Constant.NEEDGETCLOUDRECORD, true);
            }
            //跳转页面
            ActivityGuide.startActivity(SplashActivity.this);
            finish();
        } else {
            //没有成功跳转的话，还是跳主页
            enterMainActivity();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {

        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            //获取完权限再继续

        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            enterMainActivity();
        }
    }



    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
