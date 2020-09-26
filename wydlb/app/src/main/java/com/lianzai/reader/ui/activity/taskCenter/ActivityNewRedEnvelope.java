package com.lianzai.reader.ui.activity.taskCenter;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/24.
 * 新人红包弹出页面
 */

public class ActivityNewRedEnvelope extends BaseActivityForTranslucent {

    @Bind(R.id.amount_tv)
    TextView amount_tv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_red_envelope;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        String str = RxSharedPreferencesUtil.getInstance().getString(Constant.TASK_SETTINGS_NEW_AMOUNT);
        if(null != str){
            amount_tv.setText("打开得" + str + "元");
        }
        else {
            amount_tv.setText("打开得10元");
        }
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.red_bg)void red_bgClick(){
        //跳登录页
        finish();
        ActivityLoginNew.startActivity(ActivityNewRedEnvelope.this);
    }

    @OnClick(R.id.xianquguangguang_tv)void xianquguangguang_tvClick(){
        //直接关闭
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.sendEvents(Constant.EventTag.SHOW_RED_PACKET);
    }
}
