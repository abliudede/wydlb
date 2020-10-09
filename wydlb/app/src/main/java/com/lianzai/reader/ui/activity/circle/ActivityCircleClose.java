package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 圈子已关闭的提示页
 */

public class ActivityCircleClose extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_close;
    }

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityCircleClose.class);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }


    @Override
    public void gc() {
        RxToast.gc();
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
