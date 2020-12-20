package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;

import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/24.
 * 参与规则页面
 */

public class ActivityJoinRule extends BaseActivity {

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
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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


}
