package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxActivityTool;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的钱包-选择资产
 */
public class ActivityWalletChoiceAssets extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    public static final int CHOICE_TOKEN=300;

    public static final String TOKEN_TYPE_KEY="tokenType";

    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletChoiceAssets.class,CHOICE_TOKEN);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_choice_assets;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("选择资产");
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

    @OnClick(R.id.tv_read_token)void readTokenClick(){
        Intent intent=new Intent();
        intent.putExtra(TOKEN_TYPE_KEY, Constant.TokenType.READ_TOKEN);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick(R.id.tv_pro_token)void proTokenClick(){
        Intent intent=new Intent();
        intent.putExtra(TOKEN_TYPE_KEY, Constant.TokenType.PRO_TOKEN);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick(R.id.tv_mcc_token)void mccTokenClick(){
        Intent intent=new Intent();
        intent.putExtra(TOKEN_TYPE_KEY, Constant.TokenType.MCC_TOKEN);
        setResult(RESULT_OK,intent);
        finish();
    }

}
