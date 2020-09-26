package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 实名认证审核中
 */

public class ActivityRealNameAuthInReview extends BaseActivity {

    @Bind(R.id.tv_auth_message)
    TextView tv_auth_message;

    @Override
    public int getLayoutId() {
        return R.layout.activity_real_name_auth_processing;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        if(null!=getIntent().getExtras()){
            String str=getIntent().getExtras().getString("data");
            if (!RxDataTool.isEmpty(str)){
                tv_auth_message.setText(str);
            }
        }
    }

    @OnClick(R.id.btn_know_it)void knowItClick(){
        finish();
    }

    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }
}
