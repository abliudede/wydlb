package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.collection.ArrayMap;

import android.view.View;
import android.widget.ImageView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.WxLoginResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.contract.LoginContract;
import com.lianzai.reader.ui.presenter.LoginPresenter;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by lrz on 2017/10/14.
 * 用户登录界面1
 */

public class ActivityLoginNew extends BaseActivity implements LoginContract.View{


    @Bind(R.id.rl_wx_login)
    ImageView rl_wx_login;



    @Inject
    LoginPresenter loginPresenter;


    public static void  startActivity(Context context){
        Bundle bundle=new Bundle();
        RxActivityTool.skipActivity(context,ActivityLoginNew.class,bundle);
    }


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_login_new;
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
        loginPresenter.attachView(this);

        rl_wx_login.setOnClickListener(new OnRepeatClickListener() {
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

    }



    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void loginSuccess(AccountTokenBean bean) {
        RxLoginTool.saveToken(bean);
    }


    @Override
    public void wxLoginSuccess(WxLoginResponse bean) {

        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
           //直接登录
                AccountTokenBean accountTokenBean=new AccountTokenBean();
                AccountTokenBean.DataBean dataBean=new AccountTokenBean.DataBean();
                dataBean.setToken(bean.getData().getToken());

                accountTokenBean.setData(dataBean);

                RxLoginTool.saveToken(accountTokenBean);
                RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,bean.getData().getMobile());
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
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

}
