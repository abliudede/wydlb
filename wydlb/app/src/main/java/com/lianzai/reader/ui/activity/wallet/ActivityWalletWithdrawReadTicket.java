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
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.NumberEditText;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogConfirmPayment;
import com.lianzai.reader.view.dialog.RxDialogPayment;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包-阅券兑换
 */
public class ActivityWalletWithdrawReadTicket extends BaseActivity implements GateWayExchangeContract.View,WalletBalanceContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;


    @Bind(R.id.ed_amount)
    NumberEditText ed_amount;

    @Inject
    GateWayExchangePresenter gateWayExchangePresenter;

    @Inject
    WalletBalancePresenter walletBalancePresenter;

    @Bind(R.id.tv_balance)
    TextView tv_balance;

    RxDialogPayment rxDialogPayment;//支付密码框


    RxDialogConfirmPayment rxDialogConfirmPayment;

    AccountBalance.DataBean accountBalance;
    private AccountDetailBean accountDetailBean;

    public static void startActivityForResult(Activity activity){
        RxActivityTool.skipActivityForResult(activity,ActivityWalletWithdrawReadTicket.class,ActivityWalletDetail.WITHDRAW_READ_TICKET_REQUEST_CODE);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_withdraw_read_ticket;
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
        arrayMap.put("balanceType", Constant.WithdrawType.READ_TICKET_TYPE);

        walletBalancePresenter.getWalletBalance(arrayMap);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("阅券兑换阅点");
        tv_options.setText("明细");
        accountDetailBean= RxTool.getAccountBean();

        walletBalancePresenter.attachView(this);
        gateWayExchangePresenter.attachView(this);

        rxDialogConfirmPayment=new RxDialogConfirmPayment(ActivityWalletWithdrawReadTicket.this,R.style.BottomDialogStyle);

        rxDialogPayment=new RxDialogPayment(ActivityWalletWithdrawReadTicket.this,R.style.OptionDialogStyle);

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


    /**
     * 兑换明细
     */
    @OnClick(R.id.tv_options)void ticketsListClick(){
        ActivityWalletReadLockTicketList.startActivity(this);
    }


    @Override
    public void exchageSuccess(BaseBean bean) {
        dismissDialog();
        if (bean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE){
            //支付确认框关闭
            rxDialogConfirmPayment.dismiss();
            RxToast.custom("兑换成功").show();
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
        if (Double.parseDouble(ed_amount.getText().toString())<1){
            RxToast.custom("兑换数量最低不能小于1个").show();
            return;
        }

        rxDialogConfirmPayment.setPaymentData("兑换阅点",accountDetailBean.getData().getNickName()+"的账户","阅券余额",ed_amount.getText().toString()+"阅券");
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
                                jsonObject.addProperty("targetType", Constant.WithdrawType.READ_TOKEN_TYPE);//阅点
                                jsonObject.addProperty("sourceType", Constant.WithdrawType.READ_TICKET_TYPE);//阅券
                                jsonObject.addProperty("serialNo", RxDataTool.getWithdrawOrderNo());
                                jsonObject.addProperty("nonce", RxDataTool.getWithdrawOrderNo());

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
                    isNeedPassword("4",ed_amount.getText().toString());
                }
        );
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
                            jsonObject.addProperty("targetType", Constant.WithdrawType.READ_TOKEN_TYPE);//阅点
                            jsonObject.addProperty("sourceType", Constant.WithdrawType.READ_TICKET_TYPE);//阅券
                            jsonObject.addProperty("serialNo", RxDataTool.getWithdrawOrderNo());
                            jsonObject.addProperty("nonce", RxDataTool.getWithdrawOrderNo());

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
        tv_balance.setText("可兑换阅券 "+ RxDataTool.format2Decimals(String.valueOf(bean.getData().getBalanceAmt())));

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
}
