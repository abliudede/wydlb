package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.IsNeedPasswordBean;
import com.lianzai.reader.bean.SaveReceiptAccountResponse;
import com.lianzai.reader.bean.WalletAccountBean;
import com.lianzai.reader.bean.WalletBindAccountResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.BindReceiptAccountContract;
import com.lianzai.reader.ui.contract.GateWayWithdrawContract;
import com.lianzai.reader.ui.contract.ReceiptAccountContract;
import com.lianzai.reader.ui.contract.SaveReceiptAccountContract;
import com.lianzai.reader.ui.contract.WalletBalanceContract;
import com.lianzai.reader.ui.presenter.GateWayWithdrawPresenter;
import com.lianzai.reader.ui.presenter.ReceiptAccountPresenter;
import com.lianzai.reader.ui.presenter.SaveBindReceiptAccountPresenter;
import com.lianzai.reader.ui.presenter.ThirdBindReceiptAccountPresenter;
import com.lianzai.reader.ui.presenter.WalletBalancePresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.NumberEditText;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindReceiptAccount;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包-金币提现
 */
public class ActivityWalletWithdrawGoldCoin extends BaseActivity implements BindReceiptAccountContract.View,GateWayWithdrawContract.View,WalletBalanceContract.View,ReceiptAccountContract.View,SaveReceiptAccountContract.View {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.ed_amount)
    NumberEditText ed_amount;

    @Inject
    GateWayWithdrawPresenter gateWayWithdrawPresenter;

    @Inject
    WalletBalancePresenter walletBalancePresenter;

    @Inject
    SaveBindReceiptAccountPresenter saveBindReceiptAccountPresenter;

    @Inject
    ReceiptAccountPresenter receiptAccountPresenter;//获取收款账号

    @Bind(R.id.tv_balance)
    TextView tv_balance;

    RxDialogBindReceiptAccount rxDialogBindReceiptAccount;

    @Inject
    ThirdBindReceiptAccountPresenter thirdBindReceiptAccountPresenter;

    @Bind(R.id.tv_receipt_account)
            TextView tv_receipt_account;


    AccountBalance.DataBean accountBalance;

    RxDialogPayment rxDialogPayment;//支付密码框

    boolean hasReceiptAccount=false;

    int withdrawId=0;
    private AccountDetailBean accountDetailBean;


    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletWithdrawGoldCoin.class,ActivityWalletDetail.WITHDRAW_GOLD_COIN_REQUEST_CODE);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_wallet_withdraw_gold_coin;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        requestBalance();

        receiptAccountPresenter.getReceiptAccount();
    }

    private  void requestBalance(){
        ArrayMap arrayMap=new ArrayMap();
        arrayMap.put("balanceType", Constant.WithdrawType.GOLD_TYPE);
        walletBalancePresenter.getWalletBalance(arrayMap);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        accountDetailBean= RxTool.getAccountBean();

        thirdBindReceiptAccountPresenter.attachView(this);
        saveBindReceiptAccountPresenter.attachView(this);

        receiptAccountPresenter.attachView(this);
        walletBalancePresenter.attachView(this);
        gateWayWithdrawPresenter.attachView(this);
        tv_commom_title.setText("金币提现");
        tv_options.setText("明细");

        tv_options.setOnClickListener(
                v->{
//                    setResult(RESULT_OK);
//                    finish();
                    ActivityWalletRecordList.startActivity(this,Constant.WalletAccoutType.GOLD_COIN,Constant.DetailType.GOLD_WITHDRAW_DETAIL);
                }
        );

        rxDialogBindReceiptAccount=new RxDialogBindReceiptAccount(ActivityWalletWithdrawGoldCoin.this,R.style.OptionDialogStyle);
        rxDialogPayment=new RxDialogPayment(ActivityWalletWithdrawGoldCoin.this,R.style.OptionDialogStyle);

        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    Double value=Long.parseLong(s.toString())*0.05d;
                    tv_balance.setText("额外扣除¥ "+RxDataTool.format2Decimals(String.valueOf(value))+"手续费(费率5%)");
                }else{
                    if (null!=accountBalance) {
                        tv_balance.setText("可提现金额¥ " + RxDataTool.format2Decimals(String.valueOf(accountBalance.getBalanceAmt())));
                    }
                    else{
                        tv_balance.setText("可提现金额¥ "+0);
                    }
                }
            }
        });

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
    public void gateWayWithdrawSuccess(BaseBean bean) {
        dismissDialog();
        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
            ed_amount.setText("");
            RxToast.custom("提现成功").show();
//            requestBalance();
            setResult(RESULT_OK);
            finish();
        }else{
            RxToast.custom(bean.getMsg()).show();
        }
    }

    @Override
    public void showError(String message) {
        RxToast.custom(message).show();
        dismissDialog();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }

    @OnClick(R.id.tv_withdraw)void withDrawClick(){
        if (!hasReceiptAccount){
            RxToast.custom("请先绑定收款账号后再提现").show();
            return;
        }

        if (null==accountBalance)return;
        if (TextUtils.isEmpty(ed_amount.getText().toString())){
            RxToast.custom("请输入提现金额").show();
            return;
        }
        if (null!=accountBalance&&(accountBalance.getBalanceAmt()-Double.parseDouble(ed_amount.getText().toString())<0)){
            RxToast.custom("提现金额超过可提现余额").show();
            return;
        }

        if (Double.parseDouble(ed_amount.getText().toString())==0){
            RxToast.custom("提现金额不能为0").show();
            return;
        }

        //支付弹框确认
        rxDialogPayment.getTv_sure().setOnClickListener(
                v->{
                    if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().toString().trim())){
                        RxToast.custom("请输入您的支付密码").show();
                        return;
                    }
                    rxDialogPayment.dismiss();

                    //支付密码输入完后执行下面操作
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("userId",accountDetailBean.getData().getUid());
                    jsonObject.addProperty("accountId",String.valueOf(withdrawId));
                    jsonObject.addProperty("balanceType", Constant.WithdrawType.GOLD_TYPE);//金币
                    jsonObject.addProperty("nonce",  RxDataTool.getWithdrawOrderNo());

                    jsonObject.addProperty("time",String.valueOf(System.currentTimeMillis()));
                    jsonObject.addProperty("transAmt",ed_amount.getText().toString());
                    jsonObject.addProperty("password",rxDialogPayment.getEd_pay_password().getText().toString());


                    SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                    parameters.put("userId",jsonObject.get("userId").getAsString());
                    parameters.put("accountId",jsonObject.get("accountId").getAsString());
                    parameters.put("balanceType",jsonObject.get("balanceType").getAsString());
                    parameters.put("nonce",jsonObject.get("nonce").getAsString());
                    parameters.put("time",jsonObject.get("time").getAsString());
                    parameters.put("transAmt",jsonObject.get("transAmt").getAsString());
                    parameters.put("password",jsonObject.get("password").getAsString());

                    String sortUrl=RxTool.sortMap(parameters,Constant.SIGN_KEY);
                    jsonObject.addProperty("sign",RxEncryptTool.encryptMD5ToString(sortUrl));

                    gateWayWithdrawPresenter.gateWayWithdraw(jsonObject.toString());
                    rxDialogPayment.getEd_pay_password().setText("");
                    showDialog();
                }
        );
        isNeedPassword("1",ed_amount.getText().toString());
    }

    //1金币 2无 3阅点 4阅券 5书籍币 6书籍券
    public void isNeedPassword(String type,String amount){
        Map<String, String> map=new HashMap<>();
        map.put("type",type);
        map.put("amount",amount);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/unpassword/isNeedPassword", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                }catch (Exception ex){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    IsNeedPasswordBean isNeedPasswordBean = GsonUtil.getBean(response, IsNeedPasswordBean.class);
                    if (isNeedPasswordBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isNeedPasswordBean.isData()){
                            rxDialogPayment.show();
                        }else {
                            //支付密码输入完后执行下面操作
                            JsonObject jsonObject=new JsonObject();
                            jsonObject.addProperty("userId",accountDetailBean.getData().getUid());
                            jsonObject.addProperty("accountId",String.valueOf(withdrawId));
                            jsonObject.addProperty("balanceType", Constant.WithdrawType.GOLD_TYPE);//金币
                            jsonObject.addProperty("nonce",  RxDataTool.getWithdrawOrderNo());

                            jsonObject.addProperty("time",String.valueOf(System.currentTimeMillis()));
                            jsonObject.addProperty("transAmt",ed_amount.getText().toString());


                            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                            parameters.put("userId",jsonObject.get("userId").getAsString());
                            parameters.put("accountId",jsonObject.get("accountId").getAsString());
                            parameters.put("balanceType",jsonObject.get("balanceType").getAsString());
                            parameters.put("nonce",jsonObject.get("nonce").getAsString());
                            parameters.put("time",jsonObject.get("time").getAsString());
                            parameters.put("transAmt",jsonObject.get("transAmt").getAsString());

                            String sortUrl=RxTool.sortMap(parameters,Constant.SIGN_KEY);
                            jsonObject.addProperty("sign",RxEncryptTool.encryptMD5ToString(sortUrl));

                            gateWayWithdrawPresenter.gateWayWithdraw(jsonObject.toString());
                            rxDialogPayment.getEd_pay_password().setText("");
                            showDialog();
                        }
                    }
                }catch (Exception e){
                }
            }
        });
    }

    @Override
    public void getWalletBalanceSuccess(AccountBalance bean) {
        accountBalance=bean.getData();
        tv_balance.setText("可提现金额¥ "+RxDataTool.format2Decimals(String.valueOf(accountBalance.getBalanceAmt())));
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

    //收款账号查询
    @Override
    public void getReceiptAccountSuccess(WalletAccountBean bean) {
        if (null!=bean.getData()&&bean.getData().size()>0){
            hasReceiptAccount=true;
            WalletAccountBean.DataBean receiptBean=bean.getData().get(0);
            withdrawId=receiptBean.getId();
            if (!TextUtils.isEmpty(receiptBean.getRealName())){
                int len=receiptBean.getRealName().length();
                String surname=receiptBean.getRealName().substring(0,1);
                StringBuffer sb=new StringBuffer(surname);
                for(int i=0;i<len-1;i++){
                    sb.append("*");
                }
                tv_receipt_account.setText(sb.toString()+"  微信");
            }else{
                tv_receipt_account.setText("  微信");
            }

            tv_receipt_account.setClickable(false);
        }else{
            tv_receipt_account.setClickable(true);
            hasReceiptAccount=false;
            tv_receipt_account.setText("绑定提现账号");
            tv_receipt_account.setOnClickListener(
                    v->bindWx()
            );

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityBindAccount onEvent....");
        if(event.getTag()== Constant.EventTag.WX_LOGIN&&null!=event.getObj()){//微信授权登录
            String code=event.getObj().toString();
            RxLogTool.e("-----wx code:"+code);
            ArrayMap<String,Object> params=new ArrayMap<>();
            params.put("code",code);
            params.put("thirdType","1");
            thirdBindReceiptAccountPresenter.bindReceiptUserAccount(params);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void bindReceiptAccountSuccess(WalletBindAccountResponse bean) {//授权成功之后
        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

            rxDialogBindReceiptAccount.getTv_sure().setOnClickListener(
                    v->{
                        RxLogTool.e("bind receipt .....");
                        if (TextUtils.isEmpty(rxDialogBindReceiptAccount.getEd_real_name().getText().toString())){
                            RxToast.custom("请输入您的真实姓名").show();
                            return;
                        }
                        if (TextUtils.isEmpty(rxDialogBindReceiptAccount.getEd_pay_password().getText().toString())){
                            RxToast.custom("请输入您的支付密码").show();
                            return;
                        }
                        ArrayMap<String,Object> params=new ArrayMap<>();
                        params.put("userId",accountDetailBean.getData().getUid());
                        params.put("mobile",accountDetailBean.getData().getMobile());
                        params.put("realName",rxDialogBindReceiptAccount.getEd_real_name().getText().toString());
                        params.put("isDefault","0");

                        params.put("payPwd",rxDialogBindReceiptAccount.getEd_pay_password().getText().toString());
                        params.put("accountType","1");
                        params.put("accId",bean.getData().getOpenId());

                        saveBindReceiptAccountPresenter.saveReceiptUserAccount(params);

                        showDialog();
                    }
            );
            rxDialogBindReceiptAccount.show();
        }

    }

    private void bindWx(){
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端").show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端").show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //向微信发送请求
        BuglyApplicationLike.api.sendReq(req);
    }


    @Override
    public void saveReceiptAccountSuccess(SaveReceiptAccountResponse bean) {
        dismissDialog();
        rxDialogBindReceiptAccount.dismiss();
        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE&&null!=bean.getData()){
            hasReceiptAccount=true;
            tv_receipt_account.setText(bean.getData().getRealName()+" "+bean.getData().getMobile());
        }else{
            RxToast.custom(bean.getMsg()).show();
        }
    }
}
