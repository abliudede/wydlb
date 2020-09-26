package com.lianzai.reader.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.UnpasswordDetailBean;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
import com.lianzai.reader.ui.contract.EditPayPasswordContract;
import com.lianzai.reader.ui.contract.FindPasswordContract;
import com.lianzai.reader.ui.contract.RegisterContract;
import com.lianzai.reader.ui.contract.ResetPasswordContract;
import com.lianzai.reader.ui.presenter.EditPayPasswordPresenter;
import com.lianzai.reader.ui.presenter.FindPasswordPresenter;
import com.lianzai.reader.ui.presenter.RegisterPresenter;
import com.lianzai.reader.ui.presenter.ResetPasswordPresenter;
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
import com.lianzai.reader.utils.UserPreferences;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.yuyh.library.imgsel.bean.Image;

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
 * 输入密码界面-注册、找回密码，修改登录密码，修改支付密码
 */

public class ActivitySetPassword extends BaseActivity implements RegisterContract.View,FindPasswordContract.View,ResetPasswordContract.View,EditPayPasswordContract.View {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.ed_sms_code1)
    EditText ed_sms_code1;

    @Bind(R.id.ed_sms_code2)
    EditText ed_sms_code2;

    @Bind(R.id.btn_register)
    Button btn_register;

    @Bind(R.id.ll_agreement)
    LinearLayout ll_agreement;
    @Bind(R.id.cb_agreement)
    CheckBox cb_agreement;
    @Bind(R.id.iv_wenhao)
    ImageView iv_wenhao;
    //什么是金钻弹窗
    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;


    //类别type
    int flag=Constant.RegisterOrPassword.REGISTER;//区分是注册还是找回密码
    private String mobile;
    private String verificationcode;
    WxLoginResponse.DataBean wxData;//微信登录后返回的

    @Inject
    RegisterPresenter registerPresenter;
    @Inject
    FindPasswordPresenter findPasswordPresenter;

    @Inject
    ResetPasswordPresenter resetPasswordPresenter;

    @Inject
    EditPayPasswordPresenter editPayPasswordPresenter;
    private String areaCode = "86";
    private AccountDetailBean accountDetailBean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_set_password;
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
        findPasswordPresenter.attachView(this);
        editPayPasswordPresenter.attachView(this);
        resetPasswordPresenter.attachView(this);

        areaCode=RxSharedPreferencesUtil.getInstance().getString("areaCode","86");

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            flag = bundle.getInt("flag");
            mobile = bundle.getString("mobile");
            verificationcode = bundle.getString("verificationcode");

            if (flag == Constant.RegisterOrPassword.REGISTER) {
                btn_register.setText("完成");
                tv_commom_title.setText("设置您的登录密码");
            } else if (flag == Constant.RegisterOrPassword.ForgetPassword) {
                btn_register.setText("完成");
                tv_commom_title.setText("设置您的登录密码");
            } else if (flag == Constant.RegisterOrPassword.BindAccount) {
                btn_register.setText("完成");
                tv_commom_title.setText("设置您的登录密码");
                wxData=(WxLoginResponse.DataBean) bundle.getSerializable("wxData");
            } else if (flag == Constant.RegisterOrPassword.PayPassword) {
                btn_register.setText("完成");
                tv_commom_title.setText("设置您的支付密码");
                requestDetail();
            }else if (flag == Constant.RegisterOrPassword.LoginPassword) {
                btn_register.setText("完成");
                tv_commom_title.setText("设置您的登录密码");
            }

        }
        ed_sms_code1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!TextUtils.isEmpty(ed_sms_code1.getText().toString()) && !TextUtils.isEmpty(ed_sms_code2.getText().toString())){
                    btn_register.setBackgroundResource(R.drawable.gradient_bg);
                    btn_register.setEnabled(true);
                }else{
                    btn_register.setEnabled(false);
                    btn_register.setBackgroundResource(R.drawable.btn_disable_bg);
                }
            }
        });


        ed_sms_code2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!TextUtils.isEmpty(ed_sms_code1.getText().toString()) && !TextUtils.isEmpty(ed_sms_code2.getText().toString())){
                    btn_register.setBackgroundResource(R.drawable.gradient_bg);
                    btn_register.setEnabled(true);
                }else{
                    btn_register.setEnabled(false);
                    btn_register.setBackgroundResource(R.drawable.btn_disable_bg);
                }
            }
        });

    }

    /**
     * 获取详情,type 1金币 2无 3阅点 4阅券 5书籍币 6书籍券
     */
    public void requestDetail() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/unpassword/detail", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e(e.toString());
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    UnpasswordDetailBean baseBean = GsonUtil.getBean(response, UnpasswordDetailBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (!baseBean.getData().isIsOpen()) {
                            ll_agreement.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.iv_wenhao)void iv_wenhaoClick(){
        if(null == rxDialogWhatIsJinzuan){
            rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(this,R.style.OptionDialogStyle);
            rxDialogWhatIsJinzuan.getTv_title().setText("小额免密支付说明");
            rxDialogWhatIsJinzuan.getTv_description().setText("在连载中，默认低于200金币、阅点、阅券的使用或消费都无须密码即可完成。用户可在我的钱包中针对小额免密支付进行设置");
            rxDialogWhatIsJinzuan.getTv_showmore().setVisibility(View.GONE);
        }
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.btn_register)void registerClick(){

        if (RxDataTool.isEmpty(ed_sms_code2.getText().toString())){
            RxToast.custom("请完善输入内容").show();
            return;
        }
        if (RxDataTool.isEmpty(ed_sms_code1.getText().toString())){
            RxToast.custom("请完善输入内容").show();
            return;
        }
        if(!ed_sms_code1.getText().toString().equals(ed_sms_code2.getText().toString())){
            RxToast.custom("两次输入不一致").show();
            return;
        }

        if (flag== Constant.RegisterOrPassword.REGISTER){
            doRegisterRequest();
        }else if(flag==Constant.RegisterOrPassword.ForgetPassword){
            doFindPasswordRequest();
        }else if(flag==Constant.RegisterOrPassword.BindAccount){
            doBindPhoneRequest();
        }else if(flag==Constant.RegisterOrPassword.PayPassword){
           doResetPayPasswordRequest();
        }else if(flag==Constant.RegisterOrPassword.LoginPassword){
            doResetPasswordRequest();
        }
        showDialog();
    }

    private void doResetPasswordRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("verificationCode",verificationcode);
        maps.put("password", ed_sms_code1.getText().toString());
        maps.put("option_type",Constant.SmsType.RESET_LOGIN_PASSWORD_SMS);
        resetPasswordPresenter.resetPassword(maps);
    }

    private void doResetPayPasswordRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("verificationCode",verificationcode);
        maps.put("password", ed_sms_code1.getText().toString());

        editPayPasswordPresenter.editAccountPayPassword(maps);
    }

    private void doRegisterRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
        maps.put("password", ed_sms_code1.getText().toString());
        maps.put("countryCode",areaCode);
        maps.put("channel",String.valueOf(Constant.Channel.ANDROID));
        maps.put("from","3");
        registerPresenter.register(maps);
    }

    private void doBindPhoneRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
        maps.put("password", ed_sms_code1.getText().toString());
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
        maps.put("mobile",mobile);
        maps.put("verificationCode",verificationcode);
        maps.put("password", ed_sms_code1.getText().toString());
        maps.put("countryCode",areaCode);
        findPasswordPresenter.findAccountPassword(maps);
    }



    @Override
    public void gc() {
        RxToast.gc();
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



    private  void imLogin(String imaccount,String imtoken){
        NimUIKit.login(new LoginInfo(imaccount, imtoken), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                dismissDialog();
                DemoCache.setAccount(imaccount);
                RxTool.saveLoginInfo(imaccount,imtoken);//保存imtoken
                RxLogTool.e("NimUIKit.....onSuccess");

//                RxActivityTool.skipActivityAndFinishAll(ActivitySetPassword.this,MainActivity.class);
                RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_REFRESH_TAG);//登录刷新，不重新开页面
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

//                RxActivityTool.skipActivityAndFinishAll(ActivitySetPassword.this,MainActivity.class);
            }

            @Override
            public void onException(Throwable throwable) {
                dismissDialog();
                RxLogTool.e("NimUIKit.....onException-----"+throwable.getMessage());
//                RxToast.custom("IM登录异常").show();

//                RxActivityTool.skipActivityAndFinishAll(ActivitySetPassword.this,MainActivity.class);
                RxToast.custom("登录失败,请重试",Constant.ToastType.TOAST_ERROR).show();
                RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
                RxLoginTool.removeToken();//清除本地token
                finish();

            }
        });

        RxLogTool.e("start nim login......"+imaccount+"--nim token--"+imtoken);
    }


    @Override
    public void findAccountPasswordSuccess() {
        dismissDialog();
        RxToast.custom(getResources().getString(R.string.find_password_success)).show();
        RxEventBusTool.sendEvents(Constant.EventTag.Forget_Password_Success);
        finish();
    }

    @Override
    public void registerSuccess(AccountTokenBean bean) {
        RxToast.custom(getResources().getString(R.string.register_success)).show();

        RxLoginTool.saveToken(bean);
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());

        imLogin(bean.getData().getImAccount(),bean.getData().getImToken());
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
    public void editAccountPayPasswordSuccess() {
        dismissDialog();
        RxToast.custom("支付密码设置成功").show();
        accountDetailBean= RxTool.getAccountBean();
        if(null != accountDetailBean) {
            accountDetailBean.getData().setHasPayPwd(true);
            RxSharedPreferencesUtil.getInstance().putObject(Constant.ACCOUNT_CACHE, accountDetailBean);//缓存账户信息
        }
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_ACCOUNT);

        //是否勾选了开启小额免密支付
        if (ll_agreement.getVisibility() == View.VISIBLE && cb_agreement.isChecked()){
            unpasswordOpen(ed_sms_code1.getText().toString());
        }else {
            finish();
        }
    }

    /**
     * 打开
     */
    public void unpasswordOpen(String password) {
        //支付密码输入完后执行下面操作
        HashMap map = new HashMap();
        map.put("password", password);

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("password", password);
        String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
        RxLogTool.e("sortUrl:" + sortUrl);
        map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

        showDialog();
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/unpassword/open", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        finish();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void resetPasswordSuccess(BaseBean bean) {
        dismissDialog();
        RxToast.custom(getResources().getString(R.string.login_pwd_reset_success)).show();
//        //缓存失效
//        RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
//         RxLoginTool.removeToken();//清除本地token
//        accountDetailBean = null;

        //缓存失效
        RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
//        RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
        Intent intent=new Intent(ActivitySetPassword.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityLogin onEvent....");
        if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }

}
