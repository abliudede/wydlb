package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 账号与安全绑定
 */

public class ActivityAccountSecuritySettings extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.view_red)
    View view_red;

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_security_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {
        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SETTING_FIRST_CLICK,true)){
            view_red.setVisibility(View.VISIBLE);
        }else {
            view_red.setVisibility(View.GONE);
        }

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("账号与安全设置");
    }


    @OnClick(R.id.tv_edit_login_password)void editLoginPwdClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("flag", Constant.RegisterOrPassword.LoginPassword);
        RxActivityTool.skipActivity(this, ActivityEditPasswordShowPhone.class, bundle);
    }

    @OnClick(R.id.tv_edit_payment_password)void editPayPwdClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("flag", Constant.RegisterOrPassword.PayPassword);
        RxActivityTool.skipActivity(this, ActivityEditPasswordShowPhone.class, bundle);
    }

    @OnClick(R.id.tv_bind_account_setting)void bindAccountClick(){
        RxActivityTool.skipActivity(this,ActivityBindAccount.class);
    }

    @OnClick(R.id.rl_nopassword)void nopasswordClick(){
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.SETTING_FIRST_CLICK,false);
        view_red.setVisibility(View.GONE);
        RxActivityTool.skipActivity(this,ActivitySmallConfidentialPayment.class);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityLogin onEvent....");
        if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }
}
