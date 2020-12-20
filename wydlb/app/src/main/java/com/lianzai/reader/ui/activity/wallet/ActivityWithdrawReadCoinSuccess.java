package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 阅点提现成功
 */
public class ActivityWithdrawReadCoinSuccess extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;


    @Bind(R.id.tv_result)
    TextView tv_result;

    public static void startActivity(Context context,int value){
        Bundle bundle=new Bundle();
        bundle.putInt("value",value);
        RxActivityTool.skipActivity(context,ActivityWithdrawReadCoinSuccess.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_read_coin_success;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("划转成功");
        int value=getIntent().getExtras().getInt("value");
        tv_result.setText(value+"阅点已经成功划转至BTZ钱包");
    }


    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @OnClick(R.id.tv_close)void closeClick2(){
        finish();
    }

}
