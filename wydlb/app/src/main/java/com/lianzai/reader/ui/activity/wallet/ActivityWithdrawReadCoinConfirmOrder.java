package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.IsNeedPasswordBean;
import com.lianzai.reader.bean.WithdrawReadCoinOrderResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.contract.WithdrawReadCoinContract;
import com.lianzai.reader.ui.presenter.WithdrawReadCoinPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
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
 * 阅点提现订单确认
 */
public class ActivityWithdrawReadCoinConfirmOrder extends BaseActivity implements WithdrawReadCoinContract.View {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_wallet_address)
            TextView tv_wallet_address;


    @Bind(R.id.tv_value)
    TextView tv_value;

    @Bind(R.id.tv_account)
            TextView tv_account;
    RxDialogPayment rxDialogPayment;//支付密码框

    WithdrawReadCoinOrderResponse withdrawReadCoinOrderResponse;

    @Bind(R.id.tv_confirm_payment)
            TextView tv_confirm_payment;

    @Inject
    WithdrawReadCoinPresenter withdrawReadCoinPresenter;

    int value;

    @Bind(R.id.tv5)
    TextView tv5;
    private AccountDetailBean accountDetailBean;

    public static void startActivity(Context context,int value){
        Bundle bundle=new Bundle();
        bundle.putInt("value",value);
        RxActivityTool.skipActivity(context,ActivityWithdrawReadCoinConfirmOrder.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_read_coin_order_confirm;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("支付确认");
        withdrawReadCoinPresenter.attachView(this);

        value=getIntent().getExtras().getInt("value");
        rxDialogPayment=new RxDialogPayment(ActivityWithdrawReadCoinConfirmOrder.this,R.style.OptionDialogStyle);
        accountDetailBean= RxTool.getAccountBean();
        getOrderDetailRequest();

        tv_confirm_payment.setText("确认支付 "+value+" 阅点");

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
     * 订单确认
     */
    @OnClick(R.id.tv_confirm_payment)void paymentClick(){
        rxDialogPayment.getTv_sure().setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().toString().trim())){
                    RxToast.custom("请输入您的支付密码").show();
                    return;
                }
                rxDialogPayment.dismiss();
                //支付密码输入完后执行下面操作
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("outRemark","划转到BTZ");
                jsonObject.addProperty("payPassword",rxDialogPayment.getEd_pay_password().getText().toString());
                jsonObject.addProperty("serailNo", RxDataTool.getWithdrawOrderNo());
                jsonObject.addProperty("sourceType","1007");//阅点
                jsonObject.addProperty("targetType","1");//READ
                jsonObject.addProperty("transAmt",String.valueOf(value));//阅点
                jsonObject.addProperty("uid",accountDetailBean.getData().getUid());


                SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                parameters.put("outRemark",jsonObject.get("outRemark").getAsString());
                parameters.put("payPassword",jsonObject.get("payPassword").getAsString());
                parameters.put("serailNo",jsonObject.get("serailNo").getAsString());

                parameters.put("sourceType",jsonObject.get("sourceType").getAsString());
                parameters.put("targetType",jsonObject.get("targetType").getAsString());
                parameters.put("transAmt",jsonObject.get("transAmt").getAsString());

                parameters.put("uid",jsonObject.get("uid").getAsString());

                String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
                RxLogTool.e("sortUrl:"+sortUrl);
                jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

                withdrawReadCoinPresenter.withDrawReadCoin(jsonObject.toString());
                showDialog();
            }
        });

        isNeedPassword("3",String.valueOf(value));
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
                            jsonObject.addProperty("outRemark","划转到BTZ");
                            jsonObject.addProperty("serailNo", RxDataTool.getWithdrawOrderNo());
                            jsonObject.addProperty("sourceType","1007");//阅点
                            jsonObject.addProperty("targetType","1");//READ
                            jsonObject.addProperty("transAmt",String.valueOf(value));//阅点
                            jsonObject.addProperty("uid",accountDetailBean.getData().getUid());


                            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                            parameters.put("outRemark",jsonObject.get("outRemark").getAsString());
                            parameters.put("serailNo",jsonObject.get("serailNo").getAsString());

                            parameters.put("sourceType",jsonObject.get("sourceType").getAsString());
                            parameters.put("targetType",jsonObject.get("targetType").getAsString());
                            parameters.put("transAmt",jsonObject.get("transAmt").getAsString());

                            parameters.put("uid",jsonObject.get("uid").getAsString());

                            String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
                            RxLogTool.e("sortUrl:"+sortUrl);
                            jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

                            withdrawReadCoinPresenter.withDrawReadCoin(jsonObject.toString());
                            showDialog();
                        }
                    }
                }catch (Exception e){
                }
            }
        });
    }

    private void getOrderDetailRequest(){
        Map map=new HashMap();
        map.put("sourceType","1007");
        map.put("targetType","1");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/balance/getWhiteRabbitExchangeInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    withdrawReadCoinOrderResponse= GsonUtil.getBean(response,WithdrawReadCoinOrderResponse.class);
                    if (withdrawReadCoinOrderResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if (null!=tv_wallet_address&&null!=tv5&&null!=tv_value&&null!=tv_account){
                            if (!TextUtils.isEmpty(withdrawReadCoinOrderResponse.getData().getAddress())){
                                tv_wallet_address.setText(withdrawReadCoinOrderResponse.getData().getAddress());
                                tv_wallet_address.setVisibility(View.VISIBLE);
                            }else{
                                tv_wallet_address.setVisibility(View.GONE);
                            }
                            tv5.setVisibility(tv_wallet_address.getVisibility());

                            tv_value.setText(value+"阅点");
                            tv_account.setText("BTZ账号："+withdrawReadCoinOrderResponse.getData().getRabbitAccount());
                        }

                    }else {
                        RxToast.custom(withdrawReadCoinOrderResponse.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void withDrawReadCoinSuccess(BaseBean bean) {
        dismissDialog();
        ActivityWithdrawReadCoinSuccess.startActivity(ActivityWithdrawReadCoinConfirmOrder.this,value);
        finish();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        RxToast.custom(message).show();

    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }
}
