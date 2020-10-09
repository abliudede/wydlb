package com.lianzai.reader.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.inner.MyCountDownTimer;
import com.lianzai.reader.model.bean.TokenBean;
import com.lianzai.reader.model.local.TokensRepository;
import com.lianzai.reader.ui.contract.FindPasswordContract;
import com.lianzai.reader.ui.contract.RegisterContract;
import com.lianzai.reader.ui.contract.SendSmsContract;
import com.lianzai.reader.ui.presenter.FindPasswordPresenter;
import com.lianzai.reader.ui.presenter.RegisterPresenter;
import com.lianzai.reader.ui.presenter.SendSmsPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 用户注册界面-找回密码，修改登录密码，修改支付密码
 */

public class ActivityRegisterAndPassword extends BaseActivity implements RegisterContract.View,SendSmsContract.View,FindPasswordContract.View,TextWatcher{

    @Bind(R.id.tv_get_sms)
    TextView tv_get_sms;

    @Bind(R.id.ed_phone)
    EditText ed_phone;

    @Bind(R.id.ed_sms_code)
    EditText ed_sms_code;

    @Bind(R.id.ed_password)
    EditText ed_password;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    MyCountDownTimer myCountDownTimer;

    boolean isTimerStart=false;

    @Inject
    RegisterPresenter registerPresenter;

    @Inject
    FindPasswordPresenter findPasswordPresenter;

    @Bind(R.id.cb_agreement)
    CheckBox cb_agreement;

    @Bind(R.id.ll_agreement)
    LinearLayout ll_agreement;

    @Inject
    SendSmsPresenter smsPresenter;

    @Bind(R.id.btn_register)
    Button btn_register;

    int flag=Constant.RegisterOrPassword.REGISTER;//区分是注册还是找回密码


    @Bind(R.id.tv_area_code)
    TextView tv_area_code;

    String areaCode="86";//国际或地区号码

    String areaName="中国";//国际或地区号码

    WxLoginResponse.DataBean wxData;//微信登录后返回的

    SendSmsResponse smsResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_and_password;
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
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        Bundle bundle=getIntent().getExtras();

        if (null!=bundle){
            flag=bundle.getInt("flag");

            if (flag== Constant.RegisterOrPassword.REGISTER){
                btn_register.setText(getResources().getString(R.string.register_title));
                tv_commom_title.setText(btn_register.getText().toString());
                ll_agreement.setVisibility(View.VISIBLE);
            }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
                btn_register.setText(getResources().getString(R.string.forget_login_pwd1));
                tv_commom_title.setText(getResources().getString(R.string.reset_password1));
                ll_agreement.setVisibility(View.GONE);
            }else if(flag==Constant.RegisterOrPassword.BindAccount){
                ll_agreement.setVisibility(View.GONE);
                btn_register.setText(getResources().getString(R.string.bind_account_btn));
                tv_commom_title.setText(getResources().getString(R.string.bind_account_phone));
                wxData=(WxLoginResponse.DataBean) bundle.getSerializable("wxData");

                ed_password.setVisibility(View.GONE);
            }

        }


        areaName=RxSharedPreferencesUtil.getInstance().getString("areaName","中国");
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");
        tv_area_code.setText(areaName+"(+"+areaCode+")");


        registerPresenter.attachView(this);
        smsPresenter.attachView(this);
        findPasswordPresenter.attachView(this);

        long millisInFuture;
        if (RxDataTool.isEmpty(RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME))){
            millisInFuture=Constant.SMS_TIME_MAX;
            myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
        }else{
            if (RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME)>System.currentTimeMillis()){//倒计时还在进行，获取短信验证码灰色
                millisInFuture=RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME)-System.currentTimeMillis();
                myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
                myCountDownTimer.start();
                isTimerStart=true;
            }else{
                millisInFuture=Constant.SMS_TIME_MAX;//倒计时完毕，获取短信验证码按钮可点
                myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
            }

        }

        ed_sms_code.addTextChangedListener(this);
        ed_password.addTextChangedListener(this);
        ed_phone.addTextChangedListener(this);
    }

    //注册协议
    @OnClick(R.id.tv_link_agreement)void agreementClick(){
        ActivityWebView.startActivity(ActivityRegisterAndPassword.this,Constant.H5_BASE_URL+"/#/agreement",1);
    }

    @OnClick(R.id.btn_register)void registerClick(){


        if (RxDataTool.isEmpty(ed_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_sms_code.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1),Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        if (flag==Constant.RegisterOrPassword.BindAccount&&null==smsResponse){
            RxToast.custom("请先获取短信验证码后再提交",Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        if (flag!=Constant.RegisterOrPassword.BindAccount||(flag==Constant.RegisterOrPassword.BindAccount&&smsResponse.isData())){
            if (RxDataTool.isEmpty(ed_password.getText().toString())){
                RxToast.custom(getResources().getString(R.string.register_by_set_pwd_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            if (ed_password.getText().toString().length()<6){
                RxToast.custom(getResources().getString(R.string.register_by_set_pwd_length_less_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
        }

        if (flag== Constant.RegisterOrPassword.REGISTER&&!cb_agreement.isChecked()){
            RxToast.custom("阅读并同意《连载神器用户协议》才能注册哦").show();
            return;
        }
        if (flag== Constant.RegisterOrPassword.REGISTER){
            doRegisterRequest();
        }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
            doFindPasswordRequest();
        }else if(flag==Constant.RegisterOrPassword.BindAccount){
            doBindPhoneRequest();
        }
        showDialog();
    }

    private void doRegisterRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",ed_phone.getText().toString());
        maps.put("verificationCode",ed_sms_code.getText().toString());
        maps.put("password", ed_password.getText().toString());
        maps.put("countryCode",areaCode);
        maps.put("channel",String.valueOf(Constant.Channel.ANDROID));
        maps.put("from","3");
        registerPresenter.register(maps);
    }

    private void doBindPhoneRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",ed_phone.getText().toString());
        maps.put("verificationCode",ed_sms_code.getText().toString());
        if (null!=smsResponse&&smsResponse.isData()){//true要传密码
            maps.put("password", ed_password.getText().toString());
        }
        maps.put("nickname",wxData.getNickName());
        maps.put("headimgurl",wxData.getAvatar());
        maps.put("countryCode",areaCode);
        maps.put("thirdType","1");
        maps.put("unionid",wxData.getUnionid());
        maps.put("channel",String.valueOf(Constant.Channel.ANDROID));
        maps.put("from","3");

        registerPresenter.bindPhone(maps);
    }



    private void doFindPasswordRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",ed_phone.getText().toString());
        maps.put("verificationCode",ed_sms_code.getText().toString());
        maps.put("password", ed_password.getText().toString());
        maps.put("countryCode",areaCode);
        findPasswordPresenter.findAccountPassword(maps);
    }

    TokenBean tokenBean=null;
    @OnClick(R.id.tv_get_sms)void getSmsCodeClick(){
        if (RxDataTool.isEmpty(ed_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint)).show();
            return;
        }
        ArrayMap<String,Object>maps=new ArrayMap<>();
        maps.put("mobile",ed_phone.getText().toString());
        maps.put("countryCode",areaCode);
        if (flag== Constant.RegisterOrPassword.REGISTER){
            maps.put("type", Constant.RegisterSmsType.REGISTER_SMS);
        }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
            maps.put("type", Constant.RegisterSmsType.RESET_LOGIN_PASSWORD_SMS);
        }else if(flag==Constant.RegisterOrPassword.BindAccount){
            maps.put("type", Constant.RegisterSmsType.BIND_ACCOUNT_SMS);
        }
       tokenBean=TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="mobile="+ed_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            sign=RxEncryptTool.encryptMD5ToString(sign);
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
                            String sign="mobile="+ed_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
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

    @Override
    public void gc() {
        if (null!=myCountDownTimer){
            RxSharedPreferencesUtil.getInstance().putLong(Constant.SMS_TIME,System.currentTimeMillis()+myCountDownTimer.getCurrentMillisUntilFinished());//倒计时结束时间
            myCountDownTimer.cancel();
        }

        RxToast.gc();
    }

    @Override
    public void registerSuccess(AccountTokenBean bean) {
        RxToast.custom(getResources().getString(R.string.register_success)).show();

        RxLoginTool.saveToken(bean);
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());

        imLogin(bean.getData().getImAccount(),bean.getData().getImToken());


    }


    private  void imLogin(String imaccount,String imtoken){
        NimUIKit.login(new LoginInfo(imaccount, imtoken), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                dismissDialog();
                RxTool.saveLoginInfo(imaccount,imtoken);//保存imtoken
                DemoCache.setAccount(imaccount);
                RxLogTool.e("NimUIKit.....onSuccess");

//                RxActivityTool.skipActivityAndFinishAll(ActivityRegisterAndPassword.this,MainActivity.class);
                RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面

//                Intent intent=new Intent(ActivityRegisterAndPassword.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int i) {
                RxLogTool.e("NimUIKit.....onFailedi--code"+i);
                dismissDialog();

//                RxToast.custom("IM登录失败,请联系客服",Constant.ToastType.TOAST_ERROR).show();
                RxToast.custom("IM登录失败,请重新登录",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
                finish();

//                RxActivityTool.skipActivityAndFinishAll(ActivityRegisterAndPassword.this,MainActivity.class);
            }

            @Override
            public void onException(Throwable throwable) {
                dismissDialog();
                RxLogTool.e("NimUIKit.....onException-----"+throwable.getMessage());
//                RxToast.custom("IM登录异常",Constant.ToastType.TOAST_ERROR).show();
                RxToast.custom("IM登录失败,请重新登录",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
                finish();

//                RxActivityTool.skipActivityAndFinishAll(ActivityRegisterAndPassword.this,MainActivity.class);
            }
        });

        RxLogTool.e("start nim login......"+imaccount+"--nim token--"+imtoken);
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
    public void complete(String message)
    {
        dismissDialog();
        showSeverErrorDialog(message);
    }


    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Override
    public void getSmsCodeSuccess(SendSmsResponse sendSmsResponse) {
        smsResponse=sendSmsResponse;
        dismissDialog();
//        ed_phone.setEnabled(false);
        tv_get_sms.setEnabled(false);
        if (isTimerStart){
            long millisInFuture=60000l;//倒计时完毕，获取短信验证码按钮可点
            myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
        }
        myCountDownTimer.start();

        if(flag==Constant.RegisterOrPassword.BindAccount){
            if (sendSmsResponse.isData()){//true 需要传密码
                ed_password.setVisibility(View.VISIBLE);
            }else{
                ed_password.setVisibility(View.GONE);//不需要设置密码
            }
        }

    }

    @Override
    public void findAccountPasswordSuccess() {
        hideDialog();
        RxToast.custom(getResources().getString(R.string.find_password_success)).show();

        finish();
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    @OnClick(R.id.tv_area_code)void areaCodeClick(){
        ActivityAreaCode.startActivityForResult(this);
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

                tv_area_code.setText(areaName+"(+"+areaCode+")");
            }

        }
    }

    @Override
    public void bindPhoneSuccess(AccountTokenBean bean) {
        dismissDialog();
        RxToast.custom(getResources().getString(R.string.bind_success)).show();

        //绑定成功，发送通知
        RxLoginTool.saveToken(bean);
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());


        imLogin(bean.getData().getImAccount(),bean.getData().getImToken());
    }

    @Override
    public void bindMobileSuccess(BaseBean bean) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!ed_sms_code.getText().toString().equals("")&&!ed_phone.getText().toString().equals("")&&(ed_password.getVisibility()==View.VISIBLE?!ed_password.getText().toString().equals(""):true)){
            btn_register.setBackgroundResource(R.drawable.gradient_bg);
            btn_register.setEnabled(true);
        }else{
            btn_register.setEnabled(false);
            btn_register.setBackgroundResource(R.drawable.btn_disable_bg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityLogin onEvent....");
        if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }

}
