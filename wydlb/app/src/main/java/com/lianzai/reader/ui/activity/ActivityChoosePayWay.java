package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GateWayResponse;
import com.lianzai.reader.bean.GetOrderByOrderNo;
import com.lianzai.reader.bean.GetRewaredPoolDetailBean;
import com.lianzai.reader.bean.IsNeedPasswordBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.contract.BountyHunterContract;
import com.lianzai.reader.ui.contract.WalletBalanceContract;
import com.lianzai.reader.ui.presenter.BountyHunterPresenter;
import com.lianzai.reader.ui.presenter.WalletBalancePresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;
import com.tencent.mm.opensdk.modelpay.PayReq;

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
 * Created by lrz on 2018/7/25
 * 选择支付方式页面
 */

public class ActivityChoosePayWay extends BaseActivity implements BountyHunterContract.View,WalletBalanceContract.View {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.amount_tv)
    TextView amount_tv;

    @Bind(R.id.type_tv)
    TextView type_tv;

    @Bind(R.id.object_tv)
    TextView object_tv;

    @Bind(R.id.balance_tv)
    TextView balance_tv;

    @Bind(R.id.balance_iv)
    ImageView balance_iv;

    @Bind(R.id.wx_iv)
    ImageView wx_iv;

    @Bind(R.id.save_btn)
    TextView save_btn;

    RxDialogPayment rxDialogPayment;//支付密码框
    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号


    //赏金猎人相关接口
    @Inject
    BountyHunterPresenter bountyHunterPresenter;
    //余额相关接口
    @Inject
    WalletBalancePresenter walletBalancePresenter;
    //金额字符串
    private String amountstr;
    //类型字符串，1 单本打赏 2一键赏 3赏金猎人
    private String typestr;
    //微信或者余额支付。余额支付1，微信支付2.
    private int wxOrBalance = 1;
    //余额数量
    private double banlanceamount;
    //当前订单号
    private String order_no;

    private boolean isSuccess = false;
    private AccountDetailBean accountDetailBean;


    public static void startActivity(Context context, String amount, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("amount", amount);
        bundle.putString("type", type);
        RxActivityTool.skipActivity(context, ActivityChoosePayWay.class, bundle);
    }


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_choose_pay_way;
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

        tv_commom_title.setText(getString(R.string.choosepayway));

        bountyHunterPresenter.attachView(this);
        walletBalancePresenter.attachView(this);

        accountDetailBean= RxTool.getAccountBean();
        if (null==accountDetailBean)
            finish();

        amountstr = getIntent().getExtras().getString("amount");
        typestr = getIntent().getExtras().getString("type");

        amount_tv.setText(amountstr + getString(R.string.goldcoin));
        type_tv.setText(getString(R.string.dashang));
        //根据类型判断是一键赏还是赏金猎人
        if("2".equals(typestr)){
            object_tv.setText(getString(R.string.onekey));
        }else if("3".equals(typestr)){
            object_tv.setText(getString(R.string.bountyhunter));
        }

        //生成订单号
        order_no = RxDataTool.getOrderNo();
        rxDialogPayment=new RxDialogPayment(ActivityChoosePayWay.this,R.style.OptionDialogStyle);

        //请求余额信息
        ArrayMap arrayMap=new ArrayMap();
        arrayMap.put("balanceType",Constant.WithdrawType.GOLD_TYPE);
        walletBalancePresenter.getWalletBalance(arrayMap);
        showDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();
        accountDetailBean= RxTool.getAccountBean();
        if (null==accountDetailBean)
            finish();

        //延迟1秒请求订单信息
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //假如activity还存在，则请求接口
              if(null != ActivityChoosePayWay.this && !ActivityChoosePayWay.this.isDestroy && !isSuccess){
                  ArrayMap arrayMap=new ArrayMap();
                  arrayMap.put("orderNo",order_no);
                  bountyHunterPresenter.getOrderByOrderNo(arrayMap);
                  showDialog();
              }else if(null != ActivityChoosePayWay.this && !ActivityChoosePayWay.this.isDestroy && isSuccess){
                  //支付成功，跳到支付成功页面。
                  RxToast.showToast("支付成功");
                  RxLogTool.e("ActivityChoosePayWay Success");
                  ActivityPaySuccess.startActivity(ActivityChoosePayWay.this,typestr);
                  finish();
              }

            }
        }, 500);

    }

    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @OnClick(R.id.balance_ly)void balanceClick(){
        //金币视图点击
        wxOrBalance = 1;
        balance_iv.setImageResource(R.mipmap.payicon_choose);
        wx_iv.setImageResource(R.mipmap.payicon_not_choose);

        //刷新订单号
        order_no = RxDataTool.getOrderNo();

    }

    @OnClick(R.id.wx_ly)void wxClick(){
        //刷新订单号
        order_no = RxDataTool.getOrderNo();
        //微信视图点击
        wxOrBalance = 2;
        balance_iv.setImageResource(R.mipmap.payicon_not_choose);
        wx_iv.setImageResource(R.mipmap.payicon_choose);

    }


    @OnClick(R.id.save_btn)void savePasswordClick(){
        //确认支付
        if(wxOrBalance == 1){
            //金币支付流程
            if(Integer.parseInt(amountstr) > banlanceamount){
                RxToast.custom("余额不足", Constant.ToastType.TOAST_ERROR).show();
                return;
            }else{
                //支付密码输入弹框
                rxDialogPayment.getTv_sure().setOnClickListener(
                        view->{
                            if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())){
                                RxToast.custom("请输入您的支付密码").show();
                                return;
                            }
                            rxDialogPayment.dismiss();
                            //支付密码输入完后执行下面操作
                            ArrayMap map = new ArrayMap();
                            map.put("service", "pay.account.coin");
                            map.put("order_no", order_no);
                            map.put("user_id", accountDetailBean.getData().getUid());
                            map.put("password", rxDialogPayment.getEd_pay_password().getText().toString());
//            map.put("user_id",19939);
                            map.put("terminal", "2");
                            map.put("trans_amt", amountstr);
//            map.put("trans_amt",0.01);
                            map.put("in_type", typestr);

                            if("2".equals(typestr)){
                                map.put("body", getString(R.string.onekey));
                            }else if("3".equals(typestr)){
                                map.put("body", getString(R.string.bountyhunter));
                            }
                            //打赏都传3
                            map.put("order_type", "3");
                            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                            parameters.putAll(map);
                            String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                            RxLogTool.e("sortUrl:" + sortUrl);
                            map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
                            bountyHunterPresenter.placeRewardOrder(map);
                            showDialog();
                            save_btn.setText(getString(R.string.paying));
                            //清除支付密码框
                            rxDialogPayment.getEd_pay_password().setText("");
                        }
                );
                isNeedPassword("1",amountstr);
            }
        } else if(wxOrBalance == 2){

            //判断微信是否已经安装
            if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                RxToast.custom("请先安装微信客户端",Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                return;
            }

            //微信支付流程
            ArrayMap map = new ArrayMap();
            map.put("service", "pay.weixin.native");
            map.put("order_no", order_no);
            map.put("user_id", accountDetailBean.getData().getUid());
//            map.put("user_id",19939);
            map.put("terminal", "2");
            map.put("trans_amt", amountstr);
//            map.put("trans_amt",0.01);
            map.put("in_type", typestr);

            if("2".equals(typestr)){
                map.put("body", getString(R.string.onekey));
            }else if("3".equals(typestr)){
                map.put("body", getString(R.string.bountyhunter));
            }
            //打赏都传3
            map.put("order_type", "3");
            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
            parameters.putAll(map);
            String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
            RxLogTool.e("sortUrl:" + sortUrl);
            map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
            bountyHunterPresenter.placeRewardOrder(map);
            save_btn.setText(getString(R.string.paying));
            showDialog();

        }else{
            RxToast.custom("请选择支付方式",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
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
                            ArrayMap map = new ArrayMap();
                            map.put("service", "pay.account.coin");
                            map.put("order_no", order_no);
                            map.put("user_id", accountDetailBean.getData().getUid());
                            map.put("terminal", "2");
                            map.put("trans_amt", amountstr);
                            map.put("in_type", typestr);

                            if("2".equals(typestr)){
                                map.put("body", getString(R.string.onekey));
                            }else if("3".equals(typestr)){
                                map.put("body", getString(R.string.bountyhunter));
                            }
                            //打赏都传3
                            map.put("order_type", "3");
                            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                            parameters.putAll(map);
                            String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                            RxLogTool.e("sortUrl:" + sortUrl);
                            map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
                            bountyHunterPresenter.placeRewardOrder(map);
                            showDialog();
                            save_btn.setText(getString(R.string.paying));
                            //清除支付密码框
                            rxDialogPayment.getEd_pay_password().setText("");
                        }
                    }
                }catch (Exception e){
                }
            }
        });
    }

    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        save_btn.setText(getString(R.string.surepay));
        if(null == message) return;
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }


    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
        save_btn.setText(getString(R.string.surepay));
    }


    @Override
    public void initToolBar() {

    }

    @Override
    public void getRewaredPoolDetailSuccess(GetRewaredPoolDetailBean bean) {

    }

    @Override
    public void getOrderByOrderNoSuccess(GetOrderByOrderNo bean) {
        dismissDialog();
        if(null != bean){
            if(null != bean.getData()){
                if(bean.getData().getPayStatus() == 1){
                    //支付成功，跳到支付成功页面。
                    isSuccess = true;
                    RxToast.showToast("支付成功");
                    RxLogTool.e("ActivityChoosePayWay Success");
                    ActivityPaySuccess.startActivity(ActivityChoosePayWay.this,typestr);
                    finish();

                }else{
                    save_btn.setText(getString(R.string.surepay));
                }
            }else{
                save_btn.setText(getString(R.string.surepay));
            }
        }else{
            save_btn.setText(getString(R.string.surepay));
        }

    }

    @Override
    public void placeRewardOrderSuccess(GateWayResponse bean) {
        if(wxOrBalance == 2){
            //微信支付获取到订单信息
            //获取支付data成功，接下来调起微信支付。
            dismissDialog();
            if(null == bean){
                save_btn.setText(getString(R.string.surepay));
//                RxToast.custom("网络错误").show();
                return;
            }
            if(null == bean.getPayInfo()){
                save_btn.setText(getString(R.string.surepay));
                RxToast.custom(bean.getMessage()).show();
                return;
            }


            PayReq request = new PayReq();
            request.appId = bean.getPayInfo().getAppid();
            request.partnerId = bean.getPayInfo().getPartnerid();
            request.prepayId = bean.getPayInfo().getPrepayid();
            request.packageValue = bean.getPayInfo().getPackageX();
            request.nonceStr = bean.getPayInfo().getNoncestr();
            request.timeStamp = bean.getPayInfo().getTimestamp();
            request.sign = bean.getPayInfo().getSign();
            BuglyApplicationLike.api.sendReq(request);
        }else if(wxOrBalance == 1){
            //金币支付获取结果
            dismissDialog();
            //判断是否支付成功
            if("0".equals(bean.getStatus()) && "success".equals(bean.getMessage())){
                //支付成功，跳到支付成功页面。
                isSuccess = true;
                RxToast.showToast("支付成功");
                RxLogTool.e("ActivityChoosePayWay Success");
                ActivityPaySuccess.startActivity(ActivityChoosePayWay.this,typestr);
                finish();

            }else{
                if(null != bean.getMessage()){
                    RxToast.custom(bean.getMessage()).show();
                }
                save_btn.setText(getString(R.string.surepay));
            }

            //再请求一次订单信息，貌似有点多余
            //请求订单信息
//            ArrayMap arrayMap=new ArrayMap();
//            arrayMap.put("orderNo",order_no);
//            bountyHunterPresenter.getOrderByOrderNo(arrayMap);
//            showDialog();


        }
    }

    @Override
    public void getWalletBalanceSuccess(AccountBalance bean) {
        //获取到金币余额
        balance_tv.setText("(" + RxDataTool.format2Decimals(String.valueOf(bean.getData().getBalanceAmt())) + getString(R.string.goldcoin) + ")");
        banlanceamount = bean.getData().getBalanceAmt();
        dismissDialog();
    }

    //事件监听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.WX_PAY) {
            //微信支付的成功回调后先默认改掉视图，然后再请求订单接口。返回成功后去往支付成功页面。
            RxToast.custom("支付成功").show();
            isSuccess = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

}
