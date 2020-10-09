package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.WalletBalanceContract;
import com.lianzai.reader.ui.presenter.WalletBalancePresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.view.NumberEditText;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxBindWalletDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包-阅点生成充值码
 */
public class ActivityWalletWithdrawReadCoin extends BaseActivity implements  WalletBalanceContract.View {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;


    @Bind(R.id.ed_amount)
    NumberEditText ed_amount;

    @Bind(R.id.tv_balance)
    TextView tv_balance;

    RxBindWalletDialog rxBindWalletDialog;

    AccountBalance.DataBean accountBalance;



    @Inject
    WalletBalancePresenter walletBalancePresenter;

    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletWithdrawReadCoin.class,ActivityWalletDetail.WITHDRAW_READ_TOKEN_REQUEST_CODE);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_withdraw_read_coin;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        requestBalance();
        checkBindWhitWalletRabbitRequest();
    }

    private void requestBalance(){
        ArrayMap arrayMap=new ArrayMap();
        arrayMap.put("balanceType", Constant.WithdrawType.READ_TOKEN_TYPE);

        walletBalancePresenter.getWalletBalance(arrayMap);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("划转到BTZ");
        tv_options.setText("明细");
        tv_options.setOnClickListener(
                v->{
                    ActivityWalletRecordList.startActivity(this,Constant.WalletAccoutType.READ_COIN,"2");//提取
                }
        );

        walletBalancePresenter.attachView(this);

        rxBindWalletDialog=new RxBindWalletDialog(ActivityWalletWithdrawReadCoin.this,R.style.OptionDialogStyle);
        rxBindWalletDialog.setCancelable(false);
        rxBindWalletDialog.setCanceledOnTouchOutside(false);
        rxBindWalletDialog.getTv_cancel().setOnClickListener(
                v->{
                    rxBindWalletDialog.dismiss();
                    finish();
                }
        );
        rxBindWalletDialog.getTv_sure().setOnClickListener(
                v->{
                    rxBindWalletDialog.dismiss();
                    ActivityWithdrawReadCoinBindWallet.startActivity(ActivityWalletWithdrawReadCoin.this);
                }
        );

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
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


    @Override
    public void getWalletBalanceSuccess(AccountBalance bean) {
        accountBalance=bean.getData();
        tv_balance.setText("可划转阅点 "+ RxDataTool.format2Decimals(String.valueOf(bean.getData().getBalanceAmt())));
    }

    @Override
    public void showError(String message) {
        dismissDialog();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }


    private void checkBindWhitWalletRabbitRequest(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/balance/checkBindingWhiteRabbit", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()!=Constant.ResponseCodeStatus.SUCCESS_CODE){
                        rxBindWalletDialog.show();
                    }
                }catch (Exception e){

                }
            }
        });
    }

    /**
     * 全部提现
     */
    @OnClick(R.id.tv_all_withdraw)void allWithdrawClick(){
        if (null!=accountBalance&&accountBalance.getBalanceAmt()>0){
            ed_amount.setText(RxDataTool.format2Decimals(String.valueOf(accountBalance.getBalanceAmt())));
            ed_amount.setSelection(ed_amount.getText().toString().length());
        }
    }


    @OnClick(R.id.tv_withdraw)void withDrawClick(){

        if (null==accountBalance)return;
        if (TextUtils.isEmpty(ed_amount.getText().toString())){
            RxToast.custom("请输入划转金额").show();
            return;
        }
        if (null!=accountBalance&&(accountBalance.getBalanceAmt()-Double.parseDouble(ed_amount.getText().toString())<0)){
            RxToast.custom("划转金额超过可划转余额").show();
            return;
        }

        if (Double.parseDouble(ed_amount.getText().toString())==0){
            RxToast.custom("划转金额不能为0").show();
            return;
        }

        ActivityWithdrawReadCoinConfirmOrder.startActivity(this,Integer.parseInt(ed_amount.getText().toString()));

    }
}
