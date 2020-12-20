package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxTool;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的钱包-阅点充值
 */
public class ActivityWalletRechargeReadCoin extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.tv1)
            TextView tv1;


    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletRechargeReadCoin.class,ActivityWalletDetail.RECHARGE_READ_TOKEN_REQUEST_CODE);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_recharge_read_coin;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_options.setText("明细");
        RxTool.stringFormat(tv_commom_title,R.string.wallet_account_title,Constant.WalletAccoutType.READ_COIN,this);
        RxTool.stringFormat(tv1,R.string.wallet_recharge_tip1,Constant.WalletAccoutType.READ_COIN,this);

        tv_options.setOnClickListener(
                v->{
//                    setResult(RESULT_OK);
//                    finish();
                    ActivityWalletRecordList.startActivity(this,Constant.WalletAccoutType.READ_COIN,Constant.DetailType.READ_TOKEN_RECHARGE_DETAIL);

                }
        );
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

}
