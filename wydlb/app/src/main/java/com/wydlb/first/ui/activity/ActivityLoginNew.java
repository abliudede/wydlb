package com.wydlb.first.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.collection.ArrayMap;

import android.view.View;
import android.widget.ImageView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountTokenBean;
import com.wydlb.first.bean.AwardsResponse;
import com.wydlb.first.bean.DataSynEvent;
import com.wydlb.first.bean.WxLoginResponse;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.ui.contract.LoginContract;
import com.wydlb.first.ui.presenter.LoginPresenter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.view.RxToast;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import okhttp3.Call;

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
//                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
//                    RxToast.custom("请先安装微信客户端",Constant.ToastType.TOAST_ERROR).show();
//                    return;
//                }
//                if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
//                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
//                    return;
//                }
//                SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "diandi_wx_login";
//                //像微信发送请求
//                BuglyApplicationLike.api.sendReq(req);
                //先暂时写死一个账号登录
                loginAndRegister();
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


    /**
     * 登录注册接口
     */
    private void loginAndRegister() {
        HashMap map = new HashMap();
        map.put("nickname", "123");
        map.put("password", "123");
            OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/api/user/login/minigame", map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                }

                @Override
                public void onResponse(String response) {
                    try {
                        AccountTokenBean accountTokenBean = GsonUtil.getBean(response, AccountTokenBean.class);
                        if (accountTokenBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            //登录成功直接跳转到主页
                            RxLoginTool.saveToken(accountTokenBean);
                            MainActivity.startActivity(ActivityLoginNew.this);
                        } else {
                        }
                    } catch (Exception e) {
                    }
                }
            });
    }

}
