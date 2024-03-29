package com.wydlb.first.ui.activity;

import android.content.Context;
import android.os.Bundle;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxEventBusTool;

import butterknife.OnClick;

/**
 * Created by lrz on 2018/7/25
 * 选择支付方式页面
 */

public class ActivityPaySuccess extends BaseActivity {


    //类型字符串，1 单本打赏 2一键赏 3赏金猎人
    private String typestr;


    public static void startActivity(Context context, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        RxActivityTool.skipActivity(context, ActivityPaySuccess.class, bundle);
    }


    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        typestr = getIntent().getExtras().getString("type");

    }


    @OnClick(R.id.save_btn)
    void continueClick() {
        finish();
    }


    @OnClick(R.id.return_btn)
    void returnClick(){

        RxEventBusTool.sendEvents(Constant.EventTag.Close_oldPage);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
