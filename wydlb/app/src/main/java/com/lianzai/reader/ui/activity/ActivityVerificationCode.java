package com.lianzai.reader.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.contract.RegisterContract;
import com.lianzai.reader.ui.presenter.RegisterPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogCheckBox;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 输入验证码界面-注册、找回密码，修改登录密码，修改支付密码
 */

public class ActivityVerificationCode extends BaseActivity implements RegisterContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.ed_sms_code1)
    EditText ed_sms_code1;

    @Bind(R.id.ed_sms_code2)
    EditText ed_sms_code2;

    @Bind(R.id.ed_sms_code3)
    EditText ed_sms_code3;

    @Bind(R.id.ed_sms_code4)
    EditText ed_sms_code4;

    @Bind(R.id.btn_register)
    Button btn_register;

    //类别type
    int flag=Constant.RegisterOrPassword.REGISTER;//区分是注册还是找回密码
    private String mobile;
    private boolean needPassword = true;
    private String verificationcode;
    WxLoginResponse.DataBean wxData;//微信登录后返回的
    String areaCode="86";//国际或地区号码

    @Inject
    RegisterPresenter registerPresenter;

    RxDialogCheckBox rxDialogSureCancelTip;//提示

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_verification_code;
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

        registerPresenter.attachView(this);
        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            flag = bundle.getInt("flag");
            mobile = bundle.getString("mobile");
            needPassword = bundle.getBoolean("needPassword");
            if (flag == Constant.RegisterOrPassword.REGISTER) {
                btn_register.setText("下一步");
                tv_commom_title.setText("手机验证码");
            } else if (flag == Constant.RegisterOrPassword.ForgetPassword) {
                btn_register.setText("下一步");
                tv_commom_title.setText("手机验证码");
            } else if (flag == Constant.RegisterOrPassword.BindAccount) {
                if(needPassword){
                    btn_register.setText("下一步");
                }else{
                    btn_register.setText("完成");
                }
                tv_commom_title.setText("手机验证码");
                wxData=(WxLoginResponse.DataBean) bundle.getSerializable("wxData");
//                ed_password.setVisibility(View.GONE);
            }else if (flag == Constant.RegisterOrPassword.BindPhone) {
                btn_register.setText("完成");
                tv_commom_title.setText("手机验证码");
            }else if (flag == Constant.RegisterOrPassword.PayPassword) {
                btn_register.setText("下一步");
                tv_commom_title.setText("手机验证码");
            }else if (flag == Constant.RegisterOrPassword.LoginPassword) {
                btn_register.setText("下一步");
                tv_commom_title.setText("手机验证码");
            }

        }
        ed_sms_code1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (arg0.length() > 0) {
                    ed_sms_code2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        ed_sms_code2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (arg0.length() > 0) {
                    ed_sms_code3.requestFocus();
                }
                if (arg0.length() == 0) {
                    ed_sms_code1.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        ed_sms_code3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (arg0.length() > 0) {
                    ed_sms_code4.requestFocus();
                }
                if (arg0.length() == 0) {
                    ed_sms_code2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        ed_sms_code4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (arg0.length() == 0) {
                    ed_sms_code3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!TextUtils.isEmpty(ed_sms_code4.getText().toString())){
                    btn_register.setBackgroundResource(R.drawable.gradient_bg);
                    btn_register.setEnabled(true);
                }else{
                    btn_register.setEnabled(false);
                    btn_register.setBackgroundResource(R.drawable.btn_disable_bg);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    RxKeyboardTool.showSoftInput(BuglyApplicationLike.getContext(),ed_sms_code1);
                }catch (Exception e){
                }
            }
        },100);

    }


    @OnClick(R.id.btn_register)void registerClick(){

        if (RxDataTool.isEmpty(ed_sms_code4.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1)).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_sms_code3.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1)).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_sms_code2.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1)).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_sms_code1.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1)).show();
            return;
        }

        StringBuilder sb = new StringBuilder(ed_sms_code1.getText().toString());
        sb.append(ed_sms_code2.getText().toString());
        sb.append(ed_sms_code3.getText().toString());
        sb.append(ed_sms_code4.getText().toString());
        verificationcode = sb.toString();

        if (flag== Constant.RegisterOrPassword.REGISTER){
            //跳转到注册流程的设置登录密码页面
            Bundle bundle = new Bundle();
            bundle.putInt("flag", flag);
            bundle.putString("mobile", mobile);
            bundle.putString("verificationcode", verificationcode);
            RxActivityTool.skipActivity(ActivityVerificationCode.this, ActivitySetPassword.class, bundle);
        }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
            //跳转到忘记密码流程的设置登录密码页面
            Bundle bundle = new Bundle();
            bundle.putInt("flag",flag);
            bundle.putString("mobile", mobile);
            bundle.putString("verificationcode", verificationcode);
            RxActivityTool.skipActivity(ActivityVerificationCode.this, ActivitySetPassword.class, bundle);
        }else if(flag==Constant.RegisterOrPassword.BindAccount){
            if(needPassword){
                //跳转到绑定流程的设置登录密码页面
                Bundle bundle = new Bundle();
                bundle.putInt("flag", flag);
                bundle.putString("mobile", mobile);
                bundle.putString("verificationcode", verificationcode);
                bundle.putSerializable("wxData", wxData);
                RxActivityTool.skipActivity(ActivityVerificationCode.this, ActivitySetPassword.class, bundle);
            }else{
                doBindPhoneRequest();
            }
        }else if(flag==Constant.RegisterOrPassword.BindPhone){
            doBindPhoneRequestNew();
        }else if(flag==Constant.RegisterOrPassword.PayPassword){
            //跳转到设置支付密码页面
            Bundle bundle = new Bundle();
            bundle.putInt("flag", flag);
            bundle.putString("mobile", mobile);
            bundle.putString("verificationcode", verificationcode);
            RxActivityTool.skipActivity(ActivityVerificationCode.this, ActivitySetPassword.class, bundle);
        }else if(flag==Constant.RegisterOrPassword.LoginPassword){
            //跳转到设置登录密码页面
            Bundle bundle = new Bundle();
            bundle.putInt("flag",flag);
            bundle.putString("mobile", mobile);
            bundle.putString("verificationcode", verificationcode);
            RxActivityTool.skipActivity(ActivityVerificationCode.this, ActivitySetPassword.class, bundle);
        }
    }

    private void doBindPhoneRequest(){
        showDialog();
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
//        if (needPassword){//true要传密码
//            maps.put("password", ed_password.getText().toString());
//        }
        maps.put("nickname",wxData.getNickName());
        maps.put("headimgurl",wxData.getAvatar());
        maps.put("countryCode",areaCode);
        maps.put("thirdType","1");
        maps.put("unionid",wxData.getUnionid());
        maps.put("channel",String.valueOf(Constant.Channel.ANDROID));
        maps.put("from","3");
        registerPresenter.bindPhone(maps);
    }

    private void doBindPhoneRequestNew(){
        showDialog();
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
        maps.put("countryCode",areaCode);
        registerPresenter.bindMobile(maps);
    }

    @Override
    public void gc() {
        RxToast.gc();
        RxEventBusTool.unRegisterEventBus(this);
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

    @Override
    public void registerSuccess(AccountTokenBean bean) {

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
        dismissDialog();
        if(bean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE ){
            RxToast.custom(getResources().getString(R.string.bind_success)).show();
            RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面
            finish();
        }else if(bean.getCode()== 10081){
            //手机号已绑定，弹框
            showchangeSourceDialog();
        }
    }

    private void showchangeSourceDialog() {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogCheckBox(this);
            rxDialogSureCancelTip.setTitle("换绑手机");
            rxDialogSureCancelTip.setContent("检测到该手机号已绑定过其他账号，是否继续绑定？若继续绑定成功，可能导致原绑定的账号无法登录，请先提取原绑定的账号资产。");
            rxDialogSureCancelTip.getSureView().setText("确定");
            rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_gray_leftbottomyuanjiao);
            rxDialogSureCancelTip.getCancelView().setText("取消");
            rxDialogSureCancelTip.getCheckbox_ly().setVisibility(View.VISIBLE);
            rxDialogSureCancelTip.getCb_nomore_tip().setChecked(false);
            rxDialogSureCancelTip.getTv_checkbox().setText("我已了解并同意");

            rxDialogSureCancelTip.getCb_nomore_tip().setOnCheckedChangeListener(
                    (buttonView, isChecked1) -> {
                        if(isChecked1){
                            rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_leftbottomyuanjiao);
                        }else {
                            rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_gray_leftbottomyuanjiao);
                        }
                    }
            );

            rxDialogSureCancelTip.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    //强制绑定手机
                    if(rxDialogSureCancelTip.getCb_nomore_tip().isChecked()) {
                        requestForceUpdateMobile();
                        rxDialogSureCancelTip.dismiss();
                    }else {
//                        RxToast.custom("请先了解并同意").show();
                    }
                }
            });
        }
        rxDialogSureCancelTip.show();
    }

    //确定强制绑定
    private void requestForceUpdateMobile() {
        //支付密码输入完后执行下面操作
        ArrayMap<String,String> maps=new ArrayMap<>();
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
        maps.put("countryCode",areaCode);

        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("mobile",mobile);
        parameters.put("verificationCode",verificationcode);
        parameters.put("countryCode",areaCode);
        String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
        RxLogTool.e("sortUrl:"+sortUrl);

        maps.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

        showDialog();

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/forceUpdateMobile",maps, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxToast.custom("绑定失败").show();
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
                        RxToast.custom("绑定成功").show();
                        RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面
                        finish();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void showError(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }


    private  void imLogin(String imaccount,String imtoken){
        NimUIKit.login(new LoginInfo(imaccount, imtoken), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                dismissDialog();
                DemoCache.setAccount(imaccount);
                RxTool.saveLoginInfo(imaccount,imtoken);//保存imtoken
                RxLogTool.e("NimUIKit.....onSuccess");

//                RxActivityTool.skipActivityAndFinishAll(ActivityVerificationCode.this,MainActivity.class);
                RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面

//                Intent intent=new Intent(ActivityVerificationCode.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int i) {
                RxLogTool.e("NimUIKit.....onFailedi--code"+i);
                dismissDialog();

//                RxToast.custom("IM登录失败,请联系客服").show();

                RxToast.custom("IM登录失败,请重新登录",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
                finish();
//                RxActivityTool.skipActivityAndFinishAll(ActivityVerificationCode.this,MainActivity.class);
            }

            @Override
            public void onException(Throwable throwable) {
                dismissDialog();
                RxLogTool.e("NimUIKit.....onException-----"+throwable.getMessage());
//                RxToast.custom("IM登录异常").show();
                RxToast.custom("IM登录失败,请重新登录",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
                finish();
//                RxActivityTool.skipActivityAndFinishAll(ActivityVerificationCode.this,MainActivity.class);

            }
        });
        RxLogTool.e("start nim login......"+imaccount+"--nim token--"+imtoken);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityVerification onEvent....");
       if (event.getTag() == Constant.EventTag.Forget_Password_Success) {//忘记密码成功
         finish();
        } else if (event.getTag() == Constant.EventTag.REFRESH_ACCOUNT) {//重置支付密码成功
            finish();
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
           finish();
       }

    }


}
