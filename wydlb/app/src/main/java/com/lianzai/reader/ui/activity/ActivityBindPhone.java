package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ControlBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.bean.WxLoginResponse;
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
 * 微信登录成功绑定手机页面，游客登录绑定手机页面。
 */

public class ActivityBindPhone extends BaseActivity implements SendSmsContract.View,TextWatcher{

    @Bind(R.id.register_account)
    TextView tv_area_code;
    String areaCode="86";//国际或地区号码

    String areaName="中国";//国际或地区号码

    @Bind(R.id.iv_bg_top)
    ImageView iv_bg_top;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;



    //注册文字
    @Bind(R.id.ed_register_phone)
    EditText ed_register_phone;
    @Bind(R.id.register_btn)
    Button register_btn;
    @Bind(R.id.cb_agreement)
    CheckBox cb_agreement;
    @Bind(R.id.ll_agreement)
    LinearLayout ll_agreement;


    @Inject
    SendSmsPresenter smsPresenter;

    ControlBean controlBean;

    //是否需要输入密码
    private boolean needPassword = true;

    private int flag;
    WxLoginResponse.DataBean wxData;//微信登录后返回的

    public static void  startActivity(Context context, ControlBean bean){
        Bundle bundle=new Bundle();
        if (null!=bean){
            bundle.putSerializable("control",bean);
        }
        RxActivityTool.skipActivity(context,ActivityBindPhone.class,bundle);
    }


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_bind_phone_new;
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
        SystemBarUtils.setStatusBarColor(this,getResources().getColor(R.color.white));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Bundle bundle=getIntent().getExtras();

        if (null!=bundle) {
            flag = bundle.getInt("flag");
          if (flag == Constant.RegisterOrPassword.ForgetPassword) {
              img_back.setImageResource(R.mipmap.v2_icon_back_black);
              tv_commom_title.setVisibility(View.VISIBLE);
              tv_commom_title.setText("找回密码");
              tv_commom_title.setTextColor(getResources().getColor(R.color.bluetext_color));
              tv_commom_title.setTextSize(26);
              iv_bg_top.setVisibility(View.GONE);
              ll_agreement.setVisibility(View.GONE);
            } else if (flag == Constant.RegisterOrPassword.BindAccount) {
              img_back.setImageResource(R.mipmap.v2_icon_back);
              tv_commom_title.setVisibility(View.GONE);
              iv_bg_top.setVisibility(View.VISIBLE);
              ll_agreement.setVisibility(View.VISIBLE);
                wxData=(WxLoginResponse.DataBean) bundle.getSerializable("wxData");
            }
          else if (flag == Constant.RegisterOrPassword.BindPhone) {
              //用户直接绑定手机号或者换绑手机号
              img_back.setImageResource(R.mipmap.v2_icon_back);
              tv_commom_title.setVisibility(View.GONE);
              iv_bg_top.setVisibility(View.VISIBLE);
              ll_agreement.setVisibility(View.GONE);
          }
        }


        if (null!=getIntent().getExtras()){
            controlBean=(ControlBean) getIntent().getExtras().getSerializable("control");
        }

        areaName=RxSharedPreferencesUtil.getInstance().getString("areaName","中国");
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");
        tv_area_code.setText("+"+areaCode);

        ed_register_phone.addTextChangedListener(this);
    }


    @Override
    public void gc() {
        RxToast.gc();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @OnClick(R.id.register_account)void areaCodeClick(){
        ActivityAreaCode.startActivityForResult(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ActivityAreaCode.AREA_CODE_REQUEST){//国际和地区编码
            if (null!=data&&null!=data.getExtras()){
                Bundle bundle=data.getExtras();
                areaName=bundle.getString("name");
                areaCode=bundle.getString("code");
                RxSharedPreferencesUtil.getInstance().putString("areaName",areaName);
                RxSharedPreferencesUtil.getInstance().putString("areaCode",areaCode);
                RxLogTool.e("areacode:"+areaName+"--code:"+areaCode);
                tv_area_code.setText("+"+areaCode);
            }
        }
    }


   //获取验证码按钮点击
    TokenBean tokenBean=null;
    @OnClick(R.id.register_btn)void registerClick(){
        if (RxDataTool.isEmpty(ed_register_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint)).show();
            return;
        }
        if (flag==Constant.RegisterOrPassword.BindAccount && !cb_agreement.isChecked()){
            RxToast.custom("阅读并同意《连载神器用户协议》才能注册哦").show();
            return;
        }

        //发送验证码流程
        ArrayMap<String,Object>maps=new ArrayMap<>();
        maps.put("mobile",ed_register_phone.getText().toString());
        maps.put("countryCode",areaCode);
        if (flag== Constant.RegisterOrPassword.REGISTER){
            maps.put("type", Constant.RegisterSmsType.REGISTER_SMS);
        }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
            maps.put("type", Constant.RegisterSmsType.RESET_LOGIN_PASSWORD_SMS);
        }else if(flag==Constant.RegisterOrPassword.BindAccount || flag==Constant.RegisterOrPassword.BindPhone){
            maps.put("type", Constant.RegisterSmsType.BIND_ACCOUNT_SMS);
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

                    }
                }
            });
        }

    }

    //注册协议点击
    @OnClick(R.id.tv_link_agreement)void agreementClick(){
        ActivityWebView.startActivity(ActivityBindPhone.this,Constant.H5_BASE_URL+"/#/agreement",1);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(ed_register_phone.getText().toString())){
            register_btn.setBackgroundResource(R.drawable.gradient_bg);
            register_btn.setEnabled(true);
        }else{
            register_btn.setEnabled(false);
            register_btn.setBackgroundResource(R.drawable.btn_disable_bg);
        }
    }

    @Override
    public void getSmsCodeSuccess(SendSmsResponse sendSmsResponse) {
        //跳往验证码输入页面，共有页面，带上type类型。
        needPassword = sendSmsResponse.isData();
        dismissDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putString("mobile", ed_register_phone.getText().toString());
        if (flag == Constant.RegisterOrPassword.ForgetPassword) {

        }else if (flag == Constant.RegisterOrPassword.BindPhone) {

        } else if (flag == Constant.RegisterOrPassword.BindAccount) {
            bundle.putSerializable("wxData", wxData);
            bundle.putBoolean("needPassword",needPassword);
        }
        RxActivityTool.skipActivity(ActivityBindPhone.this, ActivityVerificationCode.class, bundle);
    }

    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityVerification onEvent....");
        if (event.getTag() == Constant.EventTag.Forget_Password_Success) {//忘记密码成功
            finish();
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }

}
