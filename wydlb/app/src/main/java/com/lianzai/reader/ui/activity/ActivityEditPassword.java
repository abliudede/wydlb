package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.inner.MyCountDownTimer;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.TokenBean;
import com.lianzai.reader.model.local.TokensRepository;
import com.lianzai.reader.ui.contract.EditPayPasswordContract;
import com.lianzai.reader.ui.contract.ResetPasswordContract;
import com.lianzai.reader.ui.contract.SendSmsContract;
import com.lianzai.reader.ui.presenter.EditPayPasswordPresenter;
import com.lianzai.reader.ui.presenter.ResetPasswordPresenter;
import com.lianzai.reader.ui.presenter.SendSmsPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/
 * 修改密码包括支付密码跟登录密码(没有用到)
 */

public class ActivityEditPassword extends BaseActivity implements ResetPasswordContract.View,SendSmsContract.View,EditPayPasswordContract.View {


    @Bind(R.id.tv_edit_password_tip)
    TextView tv_edit_password_tip;

    private int editType=0;//0修改登录密码1修改支付密码

    @Bind(R.id.tv_get_sms)
    TextView tv_get_sms;

    MyCountDownTimer myCountDownTimer;

    @Inject
    ResetPasswordPresenter resetPasswordPresenter;

    @Inject
    EditPayPasswordPresenter editPayPasswordPresenter;

    @Inject
    SendSmsPresenter smsPresenter;


    @Bind(R.id.ed_edit_sms_code)
    EditText ed_sms_code;

    @Bind(R.id.ed_edit_pwd)
    EditText ed_edit_pwd;

    @Bind(R.id.ed_edit_confirm_pwd)
    EditText ed_edit_confirm_pwd;

    @Bind(R.id.tv_phone)
    TextView tv_phone;

    @Bind(R.id.ll_password)
    LinearLayout ll_password;

    @Bind(R.id.ll_confirm_password)
    LinearLayout ll_confirm_password;

    @Bind(R.id.ll_sms)
    LinearLayout ll_sms;

    @Bind(R.id.save_btn)
    TextView save_btn;


    RxDialogSureCancel rxDialogSureCancel;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    boolean isTimerStart=false;
    private AccountDetailBean accountDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_password;
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
        accountDetailBean= RxTool.getAccountBean();

        editPayPasswordPresenter.attachView(this);

        editType=getIntent().getExtras().getInt("editType",0);
        tv_edit_password_tip.setText(editType==0?getResources().getString(R.string.security_login_pwd_reset_tip):getResources().getString(R.string.security_pay_pwd_reset_tip));

        ed_edit_pwd.setHint(editType==0?getResources().getString(R.string.login_pwd_hint):getResources().getString(R.string.payment_pwd_hint));
        ed_edit_confirm_pwd.setHint(editType==0?getResources().getString(R.string.register_confirm_by_set_pwd_hint):getResources().getString(R.string.register_confirm_by_set_payment_pwd_hint));
        RxTool.stringFormat(tv_phone,R.string.security_edit_password_send_sms_tip,RxDataTool.hideMobilePhone4(accountDetailBean.getData().getMobile()),false,this);


        tv_commom_title.setText(editType==0?R.string.security_login_pwd_edit:R.string.security_pay_pwd_edit);

        resetPasswordPresenter.attachView(this);

        smsPresenter.attachView(this);

        long millisInFuture;
        if (RxDataTool.isEmpty(RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME))){
            millisInFuture=60000l;
            myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
        }else{
            if (RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME)>System.currentTimeMillis()){//倒计时还在进行，获取短信验证码灰色
                millisInFuture=RxSharedPreferencesUtil.getInstance().getLong(Constant.SMS_TIME)-System.currentTimeMillis();
                myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
                myCountDownTimer.start();
                isTimerStart=true;
            }else{
                millisInFuture=60000l;//倒计时完毕，获取短信验证码按钮可点
                myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
            }

        }

        tv_phone.setVisibility(View.VISIBLE);
        tv_edit_password_tip.setVisibility(View.GONE);
        ll_sms.setVisibility(View.VISIBLE);
        ll_password.setVisibility(View.VISIBLE);
        ll_confirm_password.setVisibility(View.GONE);
        save_btn.setVisibility(View.VISIBLE);

        RxTool.stringFormat(tv_phone,R.string.security_edit_password_setting_tip,"+"+accountDetailBean.getData().getCountryCode()+" "+RxDataTool.hideMobilePhone4(accountDetailBean.getData().getMobile()),false,this);

    }

    TokenBean tokenBean;


    @OnClick(R.id.tv_get_sms)void getSmsCodeClick(){

        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("mobile",accountDetailBean.getData().getMobile());
        maps.put("countryCode",accountDetailBean.getData().getCountryCode());
        maps.put("type",editType==0? Constant.RegisterSmsType.EDIT_PASSWORD_SMS: Constant.RegisterSmsType.EDIT_PAY_PASSWORD_SMS);

        tokenBean= TokensRepository.getInstance().getToken();
        if (null!=tokenBean){
            RxSharedPreferencesUtil.getInstance().putString("lz_token_auth",tokenBean.getToken());
            String sign="mobile="+accountDetailBean.getData().getMobile()+"&countryCode="+accountDetailBean.getData().getCountryCode()+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
            RxLogTool.e("sign:"+sign);
            sign= RxEncryptTool.encryptMD5ToString(sign);
            RxLogTool.e("encrypt sign:"+sign);
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
                            String sign="mobile="+accountDetailBean.getData().getMobile()+"&countryCode="+accountDetailBean.getData().getCountryCode()+"&token="+tokenBean.getToken()+"&key="+ Constant.SMS_KEY;
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

    @OnClick(R.id.save_btn)void savePasswordClick(){
        if (RxDataTool.isEmpty(ed_sms_code.getText().toString())){
            RxToast.custom(getResources().getString(R.string.login_sms_hint1),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_edit_pwd.getText().toString())){
            RxToast.custom(getResources().getString(R.string.register_by_set_pwd_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (ed_edit_pwd.getText().toString().length()<6){
            RxToast.custom(getResources().getString(R.string.register_by_set_pwd_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        //无需再次验证
//        if (!ed_edit_confirm_pwd.getText().toString().trim().equals(ed_edit_pwd.getText().toString().trim())){
//            RxToast.custom(getResources().getString(R.string.register_pwd_error)).show();
//            return;
//        }
        if (editType==0){
            doResetPasswordRequest();
        }else{
            doResetPayPasswordRequest();
        }
        showDialog();
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
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message,Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void resetPasswordSuccess(BaseBean bean) {
        dismissDialog();

        RxToast.custom(getResources().getString(R.string.login_pwd_reset_success),Constant.ToastType.TOAST_SUCCESS).show();
        //缓存失效
        RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
        finish();
    }

    @Override
    public void editAccountPayPasswordSuccess() {
        dismissDialog();
        RxToast.custom(getResources().getString(R.string.payment_pwd_reset_success),Constant.ToastType.TOAST_SUCCESS).show();
        accountDetailBean.getData().setHasPayPwd(true);

        RxSharedPreferencesUtil.getInstance().putObject(Constant.ACCOUNT_CACHE,accountDetailBean);//缓存账户信息

        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_ACCOUNT);
        finish();
    }

    @Override
    public void complete(String message) {
        showSeverErrorDialog(message);
    }

    @Override
    public void getSmsCodeFailed(String message) {
        dismissDialog();
        RxToast.custom(message,Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void getSmsCodeSuccess(SendSmsResponse sendSmsResponse) {
        dismissDialog();
        if (isTimerStart){
            long millisInFuture=60000l;//倒计时完毕，获取短信验证码按钮可点
            myCountDownTimer=new MyCountDownTimer(millisInFuture,1000l,this,tv_get_sms,getResources().getString(R.string.login_sms_get));
        }
        myCountDownTimer.start();
        tv_phone.setVisibility(View.VISIBLE);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    protected void backClick() {
        super.backClick();
//        if (save_btn.getVisibility()==View.VISIBLE){//已经进行到了下一步，点了退出就弹框确认下，防止误操作
//            showInterruptExitDialog();
//        }else{
//            super.backClick();
//        }
    }

    private void doResetPasswordRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("verificationCode",ed_sms_code.getText().toString());
        maps.put("password", ed_edit_pwd.getText().toString());
        maps.put("option_type",Constant.SmsType.RESET_LOGIN_PASSWORD_SMS);
        resetPasswordPresenter.resetPassword(maps);
    }

    private void doResetPayPasswordRequest(){
        ArrayMap<String,Object> maps=new ArrayMap<>();
        maps.put("verificationCode",ed_sms_code.getText().toString());
        maps.put("password", ed_edit_pwd.getText().toString());

        editPayPasswordPresenter.editAccountPayPassword(maps);
    }





    /**
     * 中断操作弹框提示
     */
    private void showInterruptExitDialog(){
        if (null==rxDialogSureCancel){
            rxDialogSureCancel=new RxDialogSureCancel(this);
            rxDialogSureCancel.setContent(getResources().getString(R.string.set_password_interrupt_dialog_tip));
            rxDialogSureCancel.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogSureCancel.dismiss();
                    finish();
                }
            });
            rxDialogSureCancel.setCancelListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogSureCancel.dismiss();
                }
            });
        }
        rxDialogSureCancel.show();
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityLogin onEvent....");
        if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }

}
