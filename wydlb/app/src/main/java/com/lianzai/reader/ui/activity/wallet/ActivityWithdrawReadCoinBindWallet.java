package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.collection.ArrayMap;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
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
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 阅点提现绑定BTZ钱包
 */
public class ActivityWithdrawReadCoinBindWallet extends BaseActivity implements SendSmsContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_phone)
    TextView tv_phone;

    @Bind(R.id.ed_sms_code1)
    EditText ed_sms_code1;

    @Bind(R.id.ed_sms_code2)
    EditText ed_sms_code2;

    @Bind(R.id.ed_sms_code3)
    EditText ed_sms_code3;

    @Bind(R.id.ed_sms_code4)
    EditText ed_sms_code4;

    @Bind(R.id.tv_bind_submit)
    TextView tv_bind_submit;


    private String verificationcode;

    @Inject
    SendSmsPresenter smsPresenter;


    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityWithdrawReadCoinBindWallet.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_login;
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

        smsPresenter.attachView(this);

        tv_commom_title.setText("绑定币兔子账号");

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
                    tv_bind_submit.setBackgroundResource(R.drawable.gradient_bg);
                    tv_bind_submit.setEnabled(true);
                }else{
                    tv_bind_submit.setEnabled(false);
                    tv_bind_submit.setBackgroundResource(R.drawable.btn_disable_bg);
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

        AccountDetailBean accountDetail = RxTool.getAccountBean();
        if(null != accountDetail && null != accountDetail.getData()){
            String phone = RxDataTool.hideMobilePhone4(accountDetail.getData().getMobile());
            SpannableString mString = new SpannableString("我们已给手机号码" + phone + "发送了一个4位数验证码");
            mString.setSpan(new TextAppearanceSpan(this, R.style.BlueNormalStyle), 8, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_phone.setText(mString);
        }

        //发送验证码
        sendCode();
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


    @OnClick(R.id.tv_bind_submit)void submitClick(){
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

        bindThirdUserBySmsCode(verificationcode);
    }

    //获取验证码按钮点击
    TokenBean tokenBean=null;
    private void sendCode(){
        showDialog();
        //发送验证码流程
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("type", Constant.RegisterSmsType.BIND_BTZ);
        tokenBean= TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="type="+ String.valueOf(Constant.RegisterSmsType.BIND_BTZ) +"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            sign= RxEncryptTool.encryptMD5ToString(sign);
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
            //移除token
            TokensRepository.getInstance().deleteTokenBean(tokenBean);
            showDialog();
            smsPresenter.sendBindThirdCode(maps);//发送短信
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
                            String sign="type="+ String.valueOf(Constant.RegisterSmsType.BIND_BTZ) +"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
                            sign=RxEncryptTool.encryptMD5ToString(sign);
                            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth_sign",sign);
                            //移除token
                            TokensRepository.getInstance().deleteTokenBean(tokenBean);

                            showDialog();
                            smsPresenter.sendBindThirdCode(maps);//发送短信
                        }
                    }catch (Exception e){

                    }
                }
            });
        }
    }

    /**
     * 绑定币兔子账号
     * 3：币兔子钱包
     */
    private void bindThirdUserBySmsCode (String smsCode){
        Map<String, String> map = new HashMap<>();
        map.put("smsCode", smsCode);
        map.put("thirdType", "3");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/third/bindThirdUserBySmsCode",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                dismissDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("BTZ钱包绑定成功").show();
                        finish();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }

                }catch (Exception e){
                }
            }
        });
    }

    @Override
    public void getSmsCodeSuccess(SendSmsResponse sendSmsResponse) {
        dismissDialog();
        RxToast.custom("短信发送成功").show();
    }

    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        RxToast.custom(message).show();
    }

//    @OnClick(R.id.tv_wallet_description)void registerWalletClick(){
//        ActivityWebView.startActivity(ActivityWithdrawReadCoinBindWallet.this,Constant.H5_HELP_BASE_URL + "/helper/show/31");
//    }

}
