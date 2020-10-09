package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.api.ReaderBookApiService;
import com.lianzai.reader.api.support.GsonConverterFactory;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.ControlBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.inner.MyCountDownTimer;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.TokenBean;
import com.lianzai.reader.model.local.TokensRepository;
import com.lianzai.reader.ui.contract.LoginContract;
import com.lianzai.reader.ui.contract.SendSmsContract;
import com.lianzai.reader.ui.presenter.LoginPresenter;
import com.lianzai.reader.ui.presenter.SendSmsPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
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
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import retrofit2.Retrofit;

/**
 * Created by lrz on 2017/10/14.
 * 用户登录界面
 */

public class ActivityLogin extends BaseActivity implements LoginContract.View,SendSmsContract.View,TextWatcher{


    @Bind(R.id.ed_login_phone)
    EditText ed_login_phone;

    @Bind(R.id.ed_login_password)
    EditText ed_login_password;

    @Bind(R.id.login_btn)
    Button login_btn;


    @Bind(R.id.iv_wechat)
    ImageView iv_wechat;

    //登录文字
    @Bind(R.id.tv_new_login)
    TextView tv_new_login;
    @Bind(R.id.rl_parent_login)
    RelativeLayout rl_parent_login;


    //注册文字
    @Bind(R.id.tv_new_register)
    TextView tv_new_register;
    @Bind(R.id.rl_parent_register)
    RelativeLayout rl_parent_register;


    @Bind(R.id.ed_register_phone)
    EditText ed_register_phone;
    @Bind(R.id.register_btn)
    Button register_btn;
    @Bind(R.id.cb_agreement)
    CheckBox cb_agreement;
    @Bind(R.id.ll_agreement)
    LinearLayout ll_agreement;


    @Inject
    LoginPresenter loginPresenter;
    @Inject
    SendSmsPresenter smsPresenter;

    ControlBean controlBean;

    //注册页挪过来
    private int loginOrRegister = 1;
    private Drawable whiteTriangle;
    private boolean needPassword = true;

    @Bind(R.id.register_account)
    TextView tv_area_code;
    @Bind(R.id.login_account)
    TextView login_account;
    String areaCode="86";//国际或地区号码
    String areaName="中国";//国际或地区号码


    boolean isLogin=true;

    //新增快速登录模式。
    //初始状态
    private boolean quick = false;
    //切换按钮
    @Bind(R.id.tv_quick_login)
    TextView tv_quick_login;
    //输入密码视图
    @Bind(R.id.view2)
    RelativeLayout view2;
    //快速登录视图
    @Bind(R.id.view3)
    RelativeLayout view3;
    //发送验证码按钮
    @Bind(R.id.quicklogin_get_sms_tv)
    TextView quicklogin_get_sms_tv;
    //验证码输入框
    @Bind(R.id.ed_quicklogin_sms)
    EditText ed_quicklogin_sms;
    private MyCountDownTimer myCountDownTimer;
    private boolean quickLoginSms = false;


    public static void  startActivity(Context context, ControlBean bean){
        Bundle bundle=new Bundle();
        if (null!=bean){
            bundle.putSerializable("control",bean);
        }
        RxActivityTool.skipActivity(context,ActivityLogin.class,bundle);
    }

    public static void  startActivity(Context context, boolean isLogin){
        Bundle bundle=new Bundle();
        bundle.putBoolean("isLogin",isLogin);
        RxActivityTool.skipActivity(context,ActivityLogin.class,bundle);
    }



    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_login;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        ed_login_phone.requestFocus();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        loginPresenter.attachView(this);
        smsPresenter.attachView(this);
        SystemBarUtils.setStatusBarColor(this,getResources().getColor(R.color.v2_blue_color));


        if(null!=getIntent().getExtras()){
            isLogin=getIntent().getExtras().getBoolean("isLogin",true);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //实现状态栏图标和文字颜色为暗色
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        //初始视图
        whiteTriangle = getResources().getDrawable(R.mipmap.white_sanjiao);
        whiteTriangle.setBounds(0,0,whiteTriangle.getIntrinsicWidth(),whiteTriangle.getIntrinsicHeight());
        showChangeView();


        areaName=RxSharedPreferencesUtil.getInstance().getString("areaName","中国");
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");
        tv_area_code.setText("+"+areaCode);
        login_account.setText("+"+areaCode);


        if (null!=getIntent().getExtras()){
            controlBean=(ControlBean) getIntent().getExtras().getSerializable("control");
        }



        if (!RxDataTool.isEmpty(RxSharedPreferencesUtil.getInstance().getString(Constant.LOGIN_ID))){
            ed_login_phone.setText(RxSharedPreferencesUtil.getInstance().getString(Constant.LOGIN_ID));
        }

        areaName=RxSharedPreferencesUtil.getInstance().getString("areaName","中国");
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");

        ed_login_password.addTextChangedListener(this);
        ed_quicklogin_sms.addTextChangedListener(this);
        ed_login_phone.addTextChangedListener(this);
        ed_register_phone.addTextChangedListener(this);

        iv_wechat.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                    RxToast.custom("请先安装微信客户端",Constant.ToastType.TOAST_ERROR).show();
                    return;
                }
                if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "diandi_wx_login";
                //像微信发送请求
                BuglyApplicationLike.api.sendReq(req);
            }
        });

        if (!isLogin){
            //切换到注册模式
            newRegisterClick();
        }

    }
    @OnClick(R.id.login_btn) void loginClick(){
        if(quick){
            //验证码快速登录
            if (RxDataTool.isEmpty(ed_login_phone.getText().toString())){
                RxToast.custom( getResources().getString(R.string.login_phone_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            if (RxDataTool.isEmpty(ed_quicklogin_sms.getText().toString())){
                RxToast.custom( getResources().getString(R.string.login_sms_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            ArrayMap<String,Object> params=new ArrayMap<>();
            params.put("mobile",ed_login_phone.getText().toString());
            params.put("channel",Constant.Channel.ANDROID);
            params.put("from",Constant.Platform.APP);
            params.put("verificationCode", ed_quicklogin_sms.getText().toString());
            getDialog().setCanceledOnTouchOutside(false);
            showDialog();
            loginPresenter.quickLogin(params);

        }else {
            //普通密码登录
            if (RxDataTool.isEmpty(ed_login_phone.getText().toString())){
                RxToast.custom( getResources().getString(R.string.login_phone_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            if (RxDataTool.isEmpty(ed_login_password.getText().toString())){
                RxToast.custom( getResources().getString(R.string.login_pwd_hint),Constant.ToastType.TOAST_ERROR).show();
                return;
            }
            ArrayMap<String,Object> params=new ArrayMap<>();
            params.put("mobile",ed_login_phone.getText().toString());
            params.put("channel",Constant.Channel.ANDROID);
            params.put("from",Constant.Platform.APP);
            params.put("password", ed_login_password.getText().toString());
            getDialog().setCanceledOnTouchOutside(false);
            showDialog();
            loginPresenter.normalLogin(params);
        }
    }

    @OnClick(R.id.register_account)void areaCodeClick(){
        ActivityAreaCode.startActivityForResult(this);
    }

    @OnClick(R.id.login_account)void areaCodeClick2(){
        ActivityAreaCode.startActivityForResult(this);
    }

    @OnClick(R.id.iv_close)void closeClick(){
       finish();
    }


    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void loginSuccess(AccountTokenBean bean) {

        RxLoginTool.saveToken(bean);
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());

        //登录im账号
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

//                RxActivityTool.skipActivityAndFinishAll(ActivityLogin.this,MainActivity.class);
                RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面
                finish();
            }

            @Override
            public void onFailed(int i) {
                RxLogTool.e("NimUIKit.....onFailedi--code"+i);
                dismissDialog();

                RxToast.custom("登录失败,请重试",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
//                RxActivityTool.skipActivityAndFinishAll(ActivityLogin.this,MainActivity.class);
            }

            @Override
            public void onException(Throwable throwable) {
                dismissDialog();
                RxLogTool.e("NimUIKit.....onException-----"+throwable.getMessage());
                RxToast.custom("登录失败,请重试",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
//                RxActivityTool.skipActivityAndFinishAll(ActivityLogin.this,MainActivity.class);
            }
        });

        RxLogTool.e("start nim login......"+imaccount+"--nim token--"+imtoken);
    }


    @Override
    public void wxLoginSuccess(WxLoginResponse bean) {

        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
            if (TextUtils.isEmpty(bean.getData().getToken())){//该微信号还未绑定过手机号，跳转到绑定手机号页面
                dismissDialog();
                RxToast.custom("请先绑定您的手机号码",Constant.ToastType.TOAST_ERROR).show();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", Constant.RegisterOrPassword.BindAccount);
                bundle.putSerializable("wxData", bean.getData());
                RxActivityTool.skipActivity(this, ActivityBindPhone.class, bundle);
            }else{//已绑定过，直接登录
                AccountTokenBean accountTokenBean=new AccountTokenBean();
                AccountTokenBean.DataBean dataBean=new AccountTokenBean.DataBean();
                dataBean.setAvatar(bean.getData().getAvatar());
                dataBean.setMobile(bean.getData().getMobile());
                dataBean.setNickName(bean.getData().getNickName());
                dataBean.setImAccount(bean.getData().getImAccount());
                dataBean.setImToken(bean.getData().getImToken());
                try{
                    dataBean.setStatus(Integer.parseInt(bean.getData().getStatus()));
                }catch (Exception e){

                }
                dataBean.setUid(Integer.parseInt(bean.getData().getUid()));
                dataBean.setToken(bean.getData().getToken());

                accountTokenBean.setData(dataBean);
                RxLoginTool.saveToken(accountTokenBean);
                RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());

                //登录im账号
                imLogin(bean.getData().getImAccount(),bean.getData().getImToken());
            }
        }else if(bean.getCode()==Constant.ResponseCodeStatus.BIND_PHONE||bean.getCode()==Constant.ResponseCodeStatus.BIND_PHONE2){//需要绑定手机号
            dismissDialog();

            RxToast.custom("请先绑定您的手机号码",Constant.ToastType.TOAST_ERROR).show();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", Constant.RegisterOrPassword.BindAccount);
            bundle.putSerializable("wxData", bean.getData());
            RxActivityTool.skipActivity(this, ActivityBindPhone.class, bundle);
        }
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }

        RxToast.custom(message,Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }
    @Override
    public void initToolBar() {

    }

    private void showChangeView(){
        if(loginOrRegister == 1){
            //登录视图
            tv_new_login.setCompoundDrawables(null,null,null,whiteTriangle);
            tv_new_register.setCompoundDrawables(null,null,null,null);
            rl_parent_login.setVisibility(View.VISIBLE);
            rl_parent_register.setVisibility(View.GONE);

        }else{
            //注册视图
            tv_new_login.setCompoundDrawables(null,null,null,null);
            tv_new_register.setCompoundDrawables(null,null,null,whiteTriangle);
            rl_parent_login.setVisibility(View.GONE);
            rl_parent_register.setVisibility(View.VISIBLE);
        }
    }

    private void changeLoginView(){
        if(quick){
            quick = false;
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.GONE);
            tv_quick_login.setText("验证码登录");
        }else {
            quick = true;
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.VISIBLE);
            tv_quick_login.setText("密码登录");
        }
        //判断按钮是否可点击
        if (!ed_login_phone.getText().toString().equals("")&&((!ed_login_password.getText().toString().equals("")&& !quick )||(!ed_quicklogin_sms.getText().toString().equals("") && quick))){
            login_btn.setBackgroundResource(R.drawable.gradient_bg);
            login_btn.setEnabled(true);
        }else{
            login_btn.setEnabled(false);
            login_btn.setBackgroundResource(R.drawable.btn_disable_bg);
        }
    }

    //快速登录发送验证码
    @OnClick(R.id.quicklogin_get_sms_tv)void getSmsClick(){
        if (TextUtils.isEmpty(ed_login_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        //改变发送按钮状态
        if(null != myCountDownTimer)
            myCountDownTimer.cancel();
        myCountDownTimer=new MyCountDownTimer(60*1000 ,1000l,this,quicklogin_get_sms_tv,"获取验证码");
        myCountDownTimer.start();


        //发送验证码流程
        ArrayMap<String,Object>maps=new ArrayMap<>();
        maps.put("mobile",ed_login_phone.getText().toString());
        maps.put("countryCode",areaCode);
        maps.put("type", Constant.RegisterSmsType.QUICK_LOGIN_SMS);
        tokenBean= TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="mobile=" + ed_login_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            sign= RxEncryptTool.encryptMD5ToString(sign);
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
            //移除token
            TokensRepository.getInstance().deleteTokenBean(tokenBean);

            showDialog();
            quickLoginSms = true;
            smsPresenter.getSmsCode(maps);//发送短信
        }else{//刷新token
            OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/security/genTokens", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
//                    RxToast.custom("网络错误",Constant.ToastType.TOAST_ERROR).show();
                }

                @Override
                public void onResponse(String response) {
                    try{
                        MyTokensResponse myTokensResponse= GsonUtil.getBean(response,MyTokensResponse.class);
                        if (myTokensResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                            TokensRepository.getInstance().saveTokens(myTokensResponse.getData());
                            tokenBean=TokensRepository.getInstance().getToken();
                            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
                            String sign="mobile="+ed_login_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
                            sign=RxEncryptTool.encryptMD5ToString(sign);
                            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
                            //移除token
                            TokensRepository.getInstance().deleteTokenBean(tokenBean);
                            showDialog();
                            quickLoginSms = true;
                            smsPresenter.getSmsCode(maps);//发送短信
                        }
                    }catch (Exception e){
//                        RxToast.custom("网络错误",Constant.ToastType.TOAST_ERROR).show();
                    }
                }
            });
        }
    }



   //获取验证码按钮点击
    TokenBean tokenBean=null;
    @OnClick(R.id.register_btn)void registerClick(){
        if (RxDataTool.isEmpty(ed_register_phone.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_phone_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (!cb_agreement.isChecked()){
            RxToast.custom("阅读并同意《连载神器用户协议》才能注册哦",Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        //发送验证码流程
        ArrayMap<String,Object>maps=new ArrayMap<>();
        maps.put("mobile",ed_register_phone.getText().toString());
        maps.put("countryCode",areaCode);
        maps.put("type", Constant.RegisterSmsType.REGISTER_SMS);
        tokenBean= TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="mobile=" + ed_register_phone.getText().toString()+"&countryCode="+areaCode+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            sign= RxEncryptTool.encryptMD5ToString(sign);
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
            //移除token
            TokensRepository.getInstance().deleteTokenBean(tokenBean);
            showDialog();
            quickLoginSms = false;
            smsPresenter.getSmsCode(maps);//发送短信
        }else{//刷新token
            OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/security/genTokens", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
//                    RxToast.custom("网络错误",Constant.ToastType.TOAST_ERROR).show();
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
                            quickLoginSms = false;
                            smsPresenter.getSmsCode(maps);//发送短信
                        }
                    }catch (Exception e){
//                        RxToast.custom("网络错误",Constant.ToastType.TOAST_ERROR).show();
                    }
                }
            });
        }

    }

    //注册协议点击
    @OnClick(R.id.tv_link_agreement)void agreementClick(){
        ActivityWebView.startActivity(ActivityLogin.this,Constant.H5_BASE_URL+"/#/agreement",1);
    }

    @OnClick(R.id.tv_new_login)void newLoginClick(){
        //切换成登录状态
        loginOrRegister = 1;
        showChangeView();
    }

    @OnClick(R.id.tv_new_register)void newRegisterClick(){
        //切换成注册状态
        loginOrRegister = 2;
        showChangeView();
    }

    @OnClick(R.id.tv_forget_password)void forgetPasswordClick(){
        //忘记密码按钮
        Bundle bundle=new Bundle();
        bundle.putInt("flag",Constant.RegisterOrPassword.ForgetPassword);
        RxActivityTool.skipActivity(this,ActivityBindPhone.class,bundle);
    }

    //注册协议点击
    @OnClick(R.id.tv_quick_login)void changeLoginClick(){
       changeLoginView();
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
                login_account.setText("+"+areaCode);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityLogin onEvent....");
        if(event.getTag()==Constant.EventTag.WX_LOGIN&&null!=event.getObj()){//微信授权登录
            String code=event.getObj().toString();
            RxLogTool.e("-----wx code:"+code);
            ArrayMap<String,Object> params=new ArrayMap<>();
            params.put("code",code);
            params.put("thirdType","1");
            params.put("channel","3");
            params.put("from","3");
            showDialog();
            loginPresenter.wxLogin(params);

        }else if (event.getTag() == Constant.EventTag.BIND_PHONE) {//绑定手机
            WxLoginResponse bean = (WxLoginResponse) event.getObj();
            RxToast.custom("请先绑定您的手机号码",Constant.ToastType.TOAST_ERROR).show();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", Constant.RegisterOrPassword.BindAccount);
            bundle.putSerializable("wxData", bean.getData());
            RxActivityTool.skipActivity(this, ActivityBindPhone.class, bundle);
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }


    private void getWeixinRequest(String code){
        Retrofit retrofit = new Retrofit.Builder()
                //配置BaseUrl
                .baseUrl("https://api.weixin.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReaderBookApiService readerBookApiService=retrofit.create(ReaderBookApiService.class);
        ArrayMap<String,Object> params=new ArrayMap<>();
        params.put("appid",Constant.APP_ID);
        params.put("secret",Constant.APP_SECRET);
        params.put("code",code);
        params.put("grant_type","authorization_code");
        readerBookApiService.getWeixinInfo(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!ed_login_phone.getText().toString().equals("")&&((!ed_login_password.getText().toString().equals("")&& !quick )||(!ed_quicklogin_sms.getText().toString().equals("") && quick))){
            login_btn.setBackgroundResource(R.drawable.gradient_bg);
            login_btn.setEnabled(true);
        }else{
            login_btn.setEnabled(false);
            login_btn.setBackgroundResource(R.drawable.btn_disable_bg);
        }

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
        if(quickLoginSms){
            //当前页面停留，输入验证码
            dismissDialog();
        }else {
            //跳往验证码输入页面，共有页面，带上type类型。
            needPassword = sendSmsResponse.isData();
            dismissDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", Constant.RegisterOrPassword.REGISTER);
            bundle.putString("mobile", ed_register_phone.getText().toString());
            bundle.putBoolean("needPassword",needPassword);
            RxActivityTool.skipActivity(ActivityLogin.this, ActivityVerificationCode.class, bundle);
        }
    }

    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }
}
