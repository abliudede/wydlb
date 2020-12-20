package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.IsNeedPasswordBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.GateWayExchangeContract;
import com.lianzai.reader.ui.contract.WalletBalanceContract;
import com.lianzai.reader.ui.presenter.GateWayExchangePresenter;
import com.lianzai.reader.ui.presenter.WalletBalancePresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.NumberEditText;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogConfirmPayment;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogRechargeReadTicket;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包-阅券充值
 */
public class ActivityWalletRechargeReadTicket extends BaseActivity implements GateWayExchangeContract.View,WalletBalanceContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.tv1)
            TextView tv1;

    @Bind(R.id.ed_amount)
    NumberEditText ed_amount;

    @Inject
    GateWayExchangePresenter gateWayExchangePresenter;

    @Inject
    WalletBalancePresenter walletBalancePresenter;

    @Bind(R.id.tv_available)
    TextView tv_available;

    RxDialogPayment rxDialogPayment;//支付密码框

    RxDialogRechargeReadTicket rxDialogRechargeReadTicket;


    RxDialogConfirmPayment rxDialogConfirmPayment;
    AccountBalance.DataBean accountBalance;
    private AccountDetailBean accountDetailBean;


    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletRechargeReadTicket.class,ActivityWalletDetail.RECHARGE_READ_TICKET_REQUEST_CODE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_recharge_read_ticket;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        requestBalance();
    }

    private void requestBalance(){
        ArrayMap arrayMap=new ArrayMap();
        arrayMap.put("balanceType", Constant.WithdrawType.READ_TOKEN_TYPE);
        walletBalancePresenter.getWalletBalance(arrayMap);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("兑换阅券");
        tv_options.setText("明细");
        accountDetailBean= RxTool.getAccountBean();

        walletBalancePresenter.attachView(this);
        gateWayExchangePresenter.attachView(this);

        rxDialogRechargeReadTicket=new RxDialogRechargeReadTicket(this,R.style.OptionDialogStyle);
        rxDialogConfirmPayment=new RxDialogConfirmPayment(this,R.style.BottomDialogStyle);
        rxDialogPayment=new RxDialogPayment(ActivityWalletRechargeReadTicket.this,R.style.OptionDialogStyle);

        tv_options.setOnClickListener(
                v->{
//                    setResult(RESULT_OK);
//                    finish();

                    ActivityWalletRecordList.startActivity(this,Constant.WalletAccoutType.READ_TICKET,Constant.DetailType.READ_TICKET_EXCHARGE_DETAIL);
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
    public void exchageSuccess(BaseBean bean) {
        dismissDialog();

        if (bean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE){
            //支付确认框关闭
            rxDialogConfirmPayment.dismiss();
            RxToast.custom("兑换阅券成功").show();
//            requestBalance();//重新获取余额
//            ed_amount.setText("");
            setResult(RESULT_OK);
            finish();

        }else{
            RxToast.custom(bean.getMsg()).show();
        }


    }

    @Override
    public void showError(String message) {

        dismissDialog();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }

    @OnClick(R.id.tv_withdraw)void withDrawClick(){
        if (null==accountBalance)return;
        if (TextUtils.isEmpty(ed_amount.getText().toString()))return;
        if (null!=accountBalance&&(accountBalance.getBalanceAmt()-Double.parseDouble(ed_amount.getText().toString())<0)){
            RxToast.custom("余额不足", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (Double.parseDouble(ed_amount.getText().toString())==0){
            RxToast.custom("兑换数量不能为0", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        rxDialogRechargeReadTicket.getSureView().setOnClickListener(
                v->{
                    //操作提示框关闭
                    rxDialogRechargeReadTicket.dismiss();

                    rxDialogConfirmPayment.setPaymentData("兑换阅券",accountDetailBean.getData().getNickName()+"的账户","阅点余额",ed_amount.getText().toString()+"阅点");
                    rxDialogConfirmPayment.show();
                    rxDialogConfirmPayment.getTv_confirm_payment().setOnClickListener(
                            viewPayment->{//支付弹框确认
                                //支付密码输入弹框
                                rxDialogPayment.getTv_sure().setOnClickListener(
                                        view->{
                                            if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())){
                                                RxToast.custom("请输入您的支付密码").show();
                                                return;
                                            }
                                            rxDialogPayment.dismiss();

                                            //支付密码输入完后执行下面操作
                                            JsonObject jsonObject=new JsonObject();
                                            jsonObject.addProperty("userId",accountDetailBean.getData().getUid());
                                            jsonObject.addProperty("sourceType", Constant.WithdrawType.READ_TOKEN_TYPE);//阅点
                                            jsonObject.addProperty("targetType", Constant.WithdrawType.READ_TICKET_TYPE);//阅券
                                            jsonObject.addProperty("serialNo",  RxDataTool.getRechargeOrderNo());
                                            jsonObject.addProperty("nonce",  RxDataTool.getRechargeOrderNo());

                                            jsonObject.addProperty("time",String.valueOf(System.currentTimeMillis()));
                                            jsonObject.addProperty("amount",ed_amount.getText().toString());
                                            jsonObject.addProperty("password",rxDialogPayment.getEd_pay_password().getText().toString());


                                            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                                            parameters.put("userId",jsonObject.get("userId").getAsString());
                                            parameters.put("amount",jsonObject.get("amount").getAsString());
                                            parameters.put("sourceType",jsonObject.get("sourceType").getAsString());
                                            parameters.put("targetType",jsonObject.get("targetType").getAsString());

                                            parameters.put("serialNo",jsonObject.get("serialNo").getAsString());
                                            parameters.put("nonce",jsonObject.get("nonce").getAsString());
                                            parameters.put("time",jsonObject.get("time").getAsString());

                                            parameters.put("password",jsonObject.get("password").getAsString());

                                            String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
                                            RxLogTool.e("sortUrl:"+sortUrl);
                                            jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

                                            gateWayExchangePresenter.exchage(jsonObject.toString());
                                            rxDialogPayment.getEd_pay_password().setText("");

                                            showDialog();
                                        }
                                );
                                isNeedPassword("3",ed_amount.getText().toString());
                            }
                    );
                }
        );
        rxDialogRechargeReadTicket.show();
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
                            jsonObject.addProperty("sourceType", Constant.WithdrawType.READ_TOKEN_TYPE);//阅点
                            jsonObject.addProperty("targetType", Constant.WithdrawType.READ_TICKET_TYPE);//阅券
                            jsonObject.addProperty("serialNo",  RxDataTool.getRechargeOrderNo());
                            jsonObject.addProperty("nonce",  RxDataTool.getRechargeOrderNo());

                            jsonObject.addProperty("time",String.valueOf(System.currentTimeMillis()));
                            jsonObject.addProperty("amount",ed_amount.getText().toString());

                            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                            parameters.put("userId",jsonObject.get("userId").getAsString());
                            parameters.put("amount",jsonObject.get("amount").getAsString());
                            parameters.put("sourceType",jsonObject.get("sourceType").getAsString());
                            parameters.put("targetType",jsonObject.get("targetType").getAsString());

                            parameters.put("serialNo",jsonObject.get("serialNo").getAsString());
                            parameters.put("nonce",jsonObject.get("nonce").getAsString());
                            parameters.put("time",jsonObject.get("time").getAsString());

                            String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
                            RxLogTool.e("sortUrl:"+sortUrl);
                            jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

                            gateWayExchangePresenter.exchage(jsonObject.toString());
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
        tv_available.setText("可兑换阅点 "+ RxDataTool.format2Decimals(String.valueOf(bean.getData().getBalanceAmt())));

    }

    /**
     * 全部充值
     */
    @OnClick(R.id.tv_all_recharge)void allRechargeClick(){
        if (null!=accountBalance&&accountBalance.getBalanceAmt()>0){
            ed_amount.setText(RxDataTool.format2Decimals(String.valueOf(accountBalance.getBalanceAmt())));
            ed_amount.setSelection(ed_amount.getText().toString().length());
        }
    }

}
