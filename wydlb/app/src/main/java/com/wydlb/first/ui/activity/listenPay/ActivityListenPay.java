package com.wydlb.first.ui.activity.listenPay;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountBalance;
import com.wydlb.first.bean.AccountDetailBean;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.bean.IsNeedPasswordBean;
import com.wydlb.first.bean.ListenDetailBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.ui.activity.ActivityWebView;
import com.wydlb.first.ui.adapter.ListenPayItemAdapter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxDataTool;
import com.wydlb.first.utils.RxEncryptTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.utils.TimeFormatUtil;
import com.wydlb.first.view.CircleImageView;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogBindPhone;
import com.wydlb.first.view.dialog.RxDialogConfirmPayment;
import com.wydlb.first.view.dialog.RxDialogPayment;
import com.wydlb.first.view.dialog.RxDialogSureCancel;

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
 * Created by lrz on 2018/07/23
 * 听书支付页
 */

public class ActivityListenPay extends BaseActivity {


    @Bind(R.id.iv_user_logo)
    CircleImageView iv_user_logo;

    @Bind(R.id.tv_nickname)
    TextView tv_nickname;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    ListenPayItemAdapter listenPayItemAdapter;
    List<String> arrayList = new ArrayList<>();
    RxLinearLayoutManager manager;

    RxDialogConfirmPayment rxDialogConfirmPayment;
    RxDialogPayment rxDialogPayment;//支付密码框
    //设置支付密码弹框
    RxDialogSureCancel rxDialogSureCancel;
    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    private AccountDetailBean accountDetailBean;
    private ListenDetailBean listenDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_listen_pay;
    }

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityListenPay.class);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        listensDetail();
        if (null==accountDetailBean) {
            RxToast.custom("未登录").show();
            finish();
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        listenPayItemAdapter= new ListenPayItemAdapter(arrayList);

        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityListenPay.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(listenPayItemAdapter);

        listenPayItemAdapter.setContentClickListener(new ListenPayItemAdapter.ContentClickListener() {
            @Override
            public void payClick(View v, int pos) {
                try {
                    //弹出付款弹窗
                    getBalance(arrayList.get(pos));
                } catch (Exception e) {
                }
            }
        });

        rxDialogConfirmPayment=new RxDialogConfirmPayment(this,R.style.BottomDialogStyle);
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
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }


    @Override
    public void gc() {
        RxToast.gc();
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void listensDetail (){
//        Map<String, String> map=new HashMap<>();
//        map.put("pageNumber",String.valueOf(pageNumber));
//        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/listens/detail", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxToast.custom("加载失败").show();
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    listenDetailBean = GsonUtil.getBean(response, ListenDetailBean.class);
                    if (listenDetailBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxImageTool.loadLogoImage(ActivityListenPay.this,listenDetailBean.getData().getHead(),iv_user_logo);
                        tv_nickname.setText(listenDetailBean.getData().getNickName());
                        tv_des.setText(TimeFormatUtil.getFormatTimeDHM(listenDetailBean.getData().getRemainSecond()));

                        arrayList.clear();
                        List<String> listBeans = listenDetailBean.getData().getReadDayList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            arrayList.addAll(listBeans);
                            listenPayItemAdapter.replaceData(arrayList);
                        }
                        listenPayItemAdapter.notifyDataSetChanged();
                    }else{//加载失败
                        RxToast.custom(listenDetailBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    //余额类型1:金币 2:矿晶 3:阅点 4：阅券
    public void getBalance (String str){
        Map<String, String> map=new HashMap<>();
        map.put("balanceType","3");
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
                        String[] list = str.split("-");
                        if(list.length > 1) {
                            String day = list[0];
                            String readCount = list[1];
                            rxDialogConfirmPayment.setPaymentData("开通功能",day +"日听书特权","阅点余额（" + RxDataTool.format2Decimals(String.valueOf(accountBalance.getData().getBalanceAmt())) + "）",readCount + "阅点");
                            rxDialogConfirmPayment.show();

                            Double readAmount = Double.valueOf(readCount);
                            if(readAmount <= accountBalance.getData().getBalanceAmt()){

                                rxDialogConfirmPayment.getTv_confirm_payment().setBackgroundResource(R.drawable.blue_corner_card_21dp);
                                rxDialogConfirmPayment.getTv_confirm_payment().setOnClickListener(
                                        viewPayment->{

                                            if(TextUtils.isEmpty(accountDetailBean.getData().getMobile())){
                                                //绑定手机号弹窗
                                                if (null == rxDialogBindPhone) {
                                                    rxDialogBindPhone = new RxDialogBindPhone(ActivityListenPay.this, R.style.OptionDialogStyle);
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
                                                            requestListenPayment(str,rxDialogPayment.getEd_pay_password().getText().toString());
                                                        }
                                                );
                                                isNeedPassword("3",readCount,str);
                                            }
                                        }
                                );


                            }else {

                                rxDialogConfirmPayment.getTv_confirm_payment().setText("余额不足，前往闪兑");
                                rxDialogConfirmPayment.getTv_confirm_payment().setBackgroundResource(R.drawable.red_corner_card_21dp);
                                rxDialogConfirmPayment.getTv_confirm_payment().setOnClickListener(
                                        viewPayment->{//跳往闪兑

                                            if(TextUtils.isEmpty(accountDetailBean.getData().getMobile())){
                                                //绑定手机号弹窗
                                                if (null == rxDialogBindPhone) {
                                                    rxDialogBindPhone = new RxDialogBindPhone(ActivityListenPay.this, R.style.OptionDialogStyle);
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
                                            } else if (!accountDetailBean.getData().isHasPayPwd()){//未设置//先判断是否设置了支付密码
                                                rxDialogSureCancel.show();
                                            }else {
                                                ActivityWebView.startActivity(ActivityListenPay.this, Constant.H5_BASE_URL + "/quick-exchange/#/exchange?type=GoldTOPoint", 1);
                                            }
                                        }
                                );
                            }
                        }

                    }else{
                        RxToast.custom(accountBalance.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //1金币 2无 3阅点 4阅券 5书籍币 6书籍券
    public void isNeedPassword(String type,String amount,String str){
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
                            requestListenPayment(str,"");
                        }
                    }
                }catch (Exception e){
                }
            }
        });
    }

    //请求支付
    private void requestListenPayment (String str,String password) {
        //支付密码输入完后执行下面操作
        HashMap map = new HashMap();
        map.put("paymentType", str);
        map.put("password", password);

        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("paymentType",str);
        parameters.put("password",password);
        String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
        RxLogTool.e("sortUrl:"+sortUrl);
        map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

        rxDialogPayment.getEd_pay_password().setText("");
        showDialog();

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/listens/listenPayment",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    if(null == listenDetailBean){
                        RxToast.custom("开通失败").show();
                    }else if(null == listenDetailBean.getData()){
                        RxToast.custom("开通失败").show();
                    }else if(listenDetailBean.getData().getRemainSecond() <= 0){
                        RxToast.custom("开通失败").show();
                    }else {
                        RxToast.custom("续费失败").show();
                    }
                    dismissDialog();
                }catch (Exception ee){

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {//成功
                        rxDialogConfirmPayment.dismiss();

                        StringBuilder sb = new StringBuilder();
                        if(null == listenDetailBean){
                            sb.append("开通");
                        }else if(null == listenDetailBean.getData()){
                            sb.append("开通");
                        }else if(listenDetailBean.getData().getRemainSecond() <= 0){
                            sb.append("开通");
                        }else {
                            sb.append("续费");
                        }
                        String[] list = str.split("-");
                        if(list.length > 1) {
                            String day = list[0];
                            sb.append(day);
                            sb.append("日听书特权成功");
                        }else {
                            sb.append("听书特权成功");
                        }
                        RxToast.custom(sb.toString()).show();
                        listensDetail();
                        //刷新书籍信息
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_INFO);
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




}
