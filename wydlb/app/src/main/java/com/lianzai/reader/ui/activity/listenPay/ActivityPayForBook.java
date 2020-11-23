package com.lianzai.reader.ui.activity.listenPay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetBookRewardConfigsBean;
import com.lianzai.reader.bean.IsNeedPasswordBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRechargeGoldCoin;
import com.lianzai.reader.ui.adapter.PayForBookItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogPaySuccess;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/24.
 * 打赏书籍弹出页面
 */

public class ActivityPayForBook extends BaseActivityForTranslucent {

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Bind(R.id.tv_amount)
    TextView tv_amount;

    @Bind(R.id.iv_wenhao)
    ImageView iv_wenhao;

    @Bind(R.id.tv_sure)
    TextView tv_sure;


    private String bookId;

    RxDialogSureCancel rxDialogSureCancel;

    PayForBookItemAdapter payForBookItemAdapter;
    List<GetBookRewardConfigsBean.DataBean> arrayList = new ArrayList<>();
    private double amt;


    //当前选中的金额和礼物编号
    private String chooseCode;
    private double chooseAmt;

    private AccountDetailBean accountDetailBean;
    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号
    RxDialogPayment rxDialogPayment;//支付密码框
    private RxDialogPaySuccess rxDialogPaySuccess;

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_payfor_book;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
        accountDetailBean= RxTool.getAccountBean();
        if (null==accountDetailBean) {
            RxToast.custom("未登录").show();
            finish();
        }
    }

    public static void startActivity(Activity context, String bookId){
        Bundle intent=new Bundle();
        intent.putString("bookId",bookId);
        RxActivityTool.skipActivity(context,ActivityPayForBook.class,intent);
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
//        hideSystemBar();
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        bookId= intent.getStringExtra("bookId");

        payForBookItemAdapter= new PayForBookItemAdapter(arrayList,this);
        //GridLayoutManager列表设置
//        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dip2px(15),0 ,RxImageTool.dip2px(15),0));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setAdapter(payForBookItemAdapter);

        payForBookItemAdapter.setOnItemClickListener(
                (adapter, view, position) -> {
                    try{
                        GetBookRewardConfigsBean.DataBean tempData = arrayList.get(position);
                        chooseCode = tempData.getCode();
                        chooseAmt = tempData.getAmt();
                        payForBookItemAdapter.setChooseCode(chooseCode);
                        payForBookItemAdapter.notifyDataSetChanged();

                        if(chooseAmt > amt){
                            tv_sure.setText("充值");
                        }else {
                            tv_sure.setText("打赏：" + chooseAmt + "金币");
                        }
                        tv_sure.setBackgroundResource(R.color.v2_orange_color);

                    }catch (Exception e){

                    }


                }
        );

        rxDialogPayment=new RxDialogPayment(this,R.style.OptionDialogStyle);
        rxDialogSureCancel=new RxDialogSureCancel(this,R.style.OptionDialogStyle);
        rxDialogSureCancel.setContent("您还未设置支付密码，请先设置支付密码。");
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.setCancel("再看看");
        rxDialogSureCancel.setSure("立即设置");
        rxDialogSureCancel.setCancelable(false);

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", Constant.RegisterOrPassword.PayPassword);
            }
        });

        getBookRewardConfigs();
        getBalance(false);
    }

    private void hideSystemBar() {
        //隐藏
        SystemBarUtils.readStatusBar1(this);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }




    @OnClick(R.id.tv_sure)void tv_sureClick(){
        sure(false);
    }


    @OnClick(R.id.iv_wenhao)void iv_wenhaoClick(){
        //跳转到具体链接
        ActivityWebView.startActivity(ActivityPayForBook.this,Constant.H5_HELP_BASE_URL + "/helper/show/14");
    }

    @OnClick(R.id.view_bg)void view_bgClick(){
        finish();
    }



    @Override
    protected void onDestroy() {
        RxEventBusTool.unRegisterEventBus(this);
        super.onDestroy();
    }

    private void sure(boolean auto){
        if(chooseAmt > amt){
            if(auto){
                RxToast.custom("充值成功").show();
            }else {
                //跳往充值页面，并带入充值后发起打赏的参数
                ActivityWalletRechargeGoldCoin.startActivity(this);
            }
        }else {
            if(TextUtils.isEmpty(accountDetailBean.getData().getMobile())){
                //绑定手机号弹窗
                if (null == rxDialogBindPhone) {
                    rxDialogBindPhone = new RxDialogBindPhone(ActivityPayForBook.this, R.style.OptionDialogStyle);
                    rxDialogBindPhone.setContent("您暂未绑定手机号，请先绑定手机号再使用相关功能。");
                    rxDialogBindPhone.setTitle("绑定手机号提示");
                    rxDialogBindPhone.getCancelView().setVisibility(View.VISIBLE);
                    rxDialogBindPhone.setButtonText("立即绑定", "取消");
                    rxDialogBindPhone.setSureListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            //跳转到绑定手机号页面
                            rxDialogBindPhone.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
                        }
                    });

                    rxDialogBindPhone.setCancelListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            rxDialogBindPhone.dismiss();
                        }
                    });
                }
                rxDialogBindPhone.show();
            } else if (!accountDetailBean.getData().isHasPayPwd()){////先判断是否设置了支付密码
                rxDialogSureCancel.show();
            }else {
                //支付密码输入弹框
                rxDialogPayment.getTv_sure().setOnClickListener(
                        view->{
                            if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())){
                                RxToast.custom("请输入您的支付密码").show();
                                return;
                            }
                            rxDialogPayment.dismiss();
                            requestExchange(auto);
                        }
                );
                if(auto){
                    requestExchange(auto);
                }else {
                    isNeedPassword("1",String.valueOf(chooseAmt));
                }

            }
        }
    }

    //打赏接口
    private void requestExchange(boolean auto){
        //支付密码输入完后执行下面操作
        ArrayMap map = new ArrayMap();
        String password =  rxDialogPayment.getEd_pay_password().getText().toString();
        long timeStamp = System.currentTimeMillis();
        String uid = accountDetailBean.getData().getUid();
        map.put("amt", chooseAmt);
        map.put("balanceType", "1");
        map.put("bookId", bookId);
        if(!TextUtils.isEmpty(password))
        map.put("password",password);
        map.put("rewardCode",chooseCode);
        map.put("timestamp", timeStamp);
        map.put("userId", uid);
        if(auto){//自动模式下不需要输入支付密码
            map.put("type", "2");
        }else {
            map.put("type", "1");
        }

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.putAll(map);
        String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
        RxLogTool.e("sortUrl:" + sortUrl);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amt", chooseAmt);
        jsonObject.addProperty("balanceType", "1");
        jsonObject.addProperty("bookId", bookId);
        if(!TextUtils.isEmpty(password))
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("rewardCode", chooseCode);
        jsonObject.addProperty("timestamp", timeStamp);
        jsonObject.addProperty("userId", uid);
        if(auto){
            jsonObject.addProperty("type",  "2");
        }else {
            jsonObject.addProperty("type",  "1");
        }
        jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
        //清除支付密码框
        rxDialogPayment.getEd_pay_password().setText("");
        showDialog();
        OKHttpUtil.okHttpPostJson(Constant.GATEWAY_API_BASE_URL + "/api/bookRewardBalance", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(null == rxDialogPaySuccess){
                            rxDialogPaySuccess = new RxDialogPaySuccess(ActivityPayForBook.this,R.style.OptionDialogStyle);
                            rxDialogPaySuccess.getTv_sure().setOnClickListener(
                                    v -> {
                                        rxDialogPaySuccess.dismiss();
                                        finish();
                                    }
                            );
                        }
                        rxDialogPaySuccess.getTv_update_content().setText(String.valueOf(chooseAmt));
                        rxDialogPaySuccess.show();
                        //刷新圈子列表
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_CIRCLE_LIST);
                                } catch (Exception e) {
                                }
                            }
                        }, 1000);

                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });
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
                    rxDialogPayment.show();
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
                            requestExchange(false);
                        }
                    }else {
                        rxDialogPayment.show();
                    }
                }catch (Exception e){
                }
            }
        });
    }

    //余额类型1:金币 2:矿晶 3:阅点 4：阅券
    public void getBalance (boolean needSure){
        Map<String, String> map=new HashMap<>();
        map.put("balanceType","1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/balance/getBalance",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxToast.custom("获取账户余额失败").show();
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    AccountBalance accountBalance = GsonUtil.getBean(response, AccountBalance.class);
                    if (accountBalance.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                           amt = accountBalance.getData().getBalanceAmt();
                           tv_amount.setText( RxDataTool.format2Decimals(String.valueOf(amt)));
                           if(needSure){
                               sure(true);
                           }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getBookRewardConfigs(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/bookRewardConfig/getBookRewardConfigs", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxLogTool.e(e.toString());
                }catch (Exception ex){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    GetBookRewardConfigsBean getBookRewardConfigsBean = GsonUtil.getBean(response, GetBookRewardConfigsBean.class);
                    if (getBookRewardConfigsBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        arrayList.clear();
                        arrayList.addAll(getBookRewardConfigsBean.getData());
                        payForBookItemAdapter.replaceData(arrayList);
                    }
                }catch (Exception e){
                }
            }
        });
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag()==Constant.EventTag.WX_PAY_REFRESH_WEB){//微信支付成功
            getBalance(true);
        }
    }

}
