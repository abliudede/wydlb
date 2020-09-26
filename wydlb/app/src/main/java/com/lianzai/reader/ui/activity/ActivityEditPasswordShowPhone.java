package com.lianzai.reader.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.model.bean.TokenBean;
import com.lianzai.reader.model.local.TokensRepository;
import com.lianzai.reader.ui.contract.SendSmsContract;
import com.lianzai.reader.ui.presenter.SendSmsPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 修改登录密码或支付密码的展示手机页面
 */

public class ActivityEditPasswordShowPhone extends BaseActivity implements SendSmsContract.View{

    @Bind(R.id.register_account)
    TextView tv_area_code;
    String areaCode="86";//国际或地区号码
    String areaName="中国";//国际或地区号码

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;


    //注册文字
    @Bind(R.id.ed_register_phone)
    TextView ed_register_phone;
    @Bind(R.id.register_btn)
    Button register_btn;


    @Inject
    SendSmsPresenter smsPresenter;

    private int flag;


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_edit_password_showphone;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        ed_register_phone.requestFocus();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        smsPresenter.attachView(this);
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Bundle bundle=getIntent().getExtras();

        if (null!=bundle) {
            flag = bundle.getInt("flag");
          if (flag == Constant.RegisterOrPassword.LoginPassword) {
              tv_commom_title.setText("更改登录密码");
            } else if (flag == Constant.RegisterOrPassword.PayPassword) {
              tv_commom_title.setText("更改支付密码");
            }
        }

        if (!RxDataTool.isEmpty(RxSharedPreferencesUtil.getInstance().getString(Constant.LOGIN_ID))){
            ed_register_phone.setText(RxSharedPreferencesUtil.getInstance().getString(Constant.LOGIN_ID));
        }

        register_btn.setBackgroundResource(R.drawable.gradient_bg);
        register_btn.setEnabled(true);

        areaName=RxSharedPreferencesUtil.getInstance().getString("areaName","中国");
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");
        tv_area_code.setText("+"+areaCode);

    }


    @Override
    public void gc() {
        RxToast.gc();
        RxEventBusTool.unRegisterEventBus(this);
    }



    @Override
    public void showError(String message) {
        dismissDialog();
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
    }
    @Override
    public void initToolBar() {

    }


   //获取验证码按钮点击
    TokenBean tokenBean=null;
    @OnClick(R.id.register_btn)void registerClick(){
        if (RxDataTool.isEmpty(ed_register_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint)).show();
            return;
        }

        //发送验证码流程
        ArrayMap<String,Object>maps=new ArrayMap<>();
        maps.put("mobile",ed_register_phone.getText().toString());
        maps.put("countryCode",areaCode);
        if(flag == Constant.RegisterOrPassword.LoginPassword){
            maps.put("type", Constant.RegisterSmsType.EDIT_PASSWORD_SMS);
        }else if(flag == Constant.RegisterOrPassword.PayPassword){
            maps.put("type", Constant.RegisterSmsType.EDIT_PAY_PASSWORD_SMS);
        }


        tokenBean= TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="mobile=" + ed_register_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            sign= RxEncryptTool.encryptMD5ToString(sign);
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
            //移除token
            TokensRepository.getInstance().deleteTokenBean(tokenBean);
            showDialog();
            smsPresenter.getSmsCode(maps);//发送短信
        }else{//刷新token
            OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/security/genTokens", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    try{
                        MyTokensResponse myTokensResponse= GsonUtil.getBean(response,MyTokensResponse.class);
                        if (myTokensResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                            TokensRepository.getInstance().saveTokens(myTokensResponse.getData());
                            tokenBean=TokensRepository.getInstance().getToken();
                            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
                            String sign="mobile="+ed_register_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
                            sign=RxEncryptTool.encryptMD5ToString(sign);
                            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);

                            //移除token
                            TokensRepository.getInstance().deleteTokenBean(tokenBean);

                            showDialog();
                            smsPresenter.getSmsCode(maps);//发送短信
                        }
                    }catch (Exception e){
                        RxLogTool.e(e.toString());
                    }
                }
            });
        }

    }


    //关闭按钮
    @OnClick(R.id.img_back)void finishClick(){
      finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void getSmsCodeSuccess(SendSmsResponse sendSmsResponse) {
        //跳往验证码输入页面，共有页面，带上type类型。
        dismissDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putString("mobile", ed_register_phone.getText().toString());
        RxActivityTool.skipActivity(ActivityEditPasswordShowPhone.this, ActivityVerificationCode.class, bundle);
    }

    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityVerification onEvent....");
        if (event.getTag() == Constant.EventTag.REFRESH_ACCOUNT) {//重置支付密码成功
            finish();
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }
}
