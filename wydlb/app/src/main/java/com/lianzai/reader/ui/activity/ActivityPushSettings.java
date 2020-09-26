package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityUserShieldingList;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.contract.AccountContract;
import com.lianzai.reader.ui.contract.LogoutContract;
import com.lianzai.reader.ui.presenter.AccountPresenter;
import com.lianzai.reader.ui.presenter.LogoutPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GlideCacheUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogClearCache;
import com.lianzai.reader.view.dialog.RxDialogConfirm;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置
 */
public class ActivityPushSettings extends BaseActivity implements AccountContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.sb_msg_off)
    CheckBox sb_msg_off;

    private AccountDetailBean accountDetailBean;

    @Inject
    AccountPresenter accountPresenter;


    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityPushSettings.class);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_push_settings;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        accountPresenter.getAccountDetail();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        tv_commom_title.setText("推送设置");

        accountDetailBean= RxTool.getAccountBean();


        if (null!=accountDetailBean){
            //是否回复动态推送 1是 0否
            if(accountDetailBean.getData().getIsStartReplyPush() == 0) {
                sb_msg_off.setChecked(false);
            }else {
                sb_msg_off.setChecked(true);
            }
        }

    }

    @OnCheckedChanged(R.id.sb_msg_off)void sb_msg_offChanged(boolean isChecked){
        if(isChecked){
            editReadLikeRequest(1);
        }else {
            editReadLikeRequest(0);
        }
    }


    private void editReadLikeRequest(int isOpen){
        Map map=new HashMap();
        map.put("isStartReplyPush",String.valueOf(isOpen));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updateMemberInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                    if(isOpen == 1){
                        sb_msg_off.setChecked(false);
                    }else {
                        sb_msg_off.setChecked(true);
                    }
                }catch (Exception ee){

                }

            }

            @Override
            public void onResponse(String response) {
                try{
                    dismissDialog();
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //存入本地数据
                        accountDetailBean.getData().setIsStartReplyPush(isOpen);
                        RxSharedPreferencesUtil.getInstance().putObject(Constant.ACCOUNT_CACHE,accountDetailBean);//缓存账户信息
                    }else{
                        if(isOpen == 1){
                            sb_msg_off.setChecked(false);
                        }else {
                            sb_msg_off.setChecked(true);
                        }
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }


    @Override
    public void showError(String message) {
        RxToast.custom(message,Constant.ToastType.TOAST_ERROR).show();
        dismissDialog();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }


    @Override
    public void gc() {
        RxEventBusTool.unRegisterEventBus(this);
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


    @Override
    public void getAccountDetailSuccess(AccountDetailBean bean) {
        accountDetailBean = bean;
        if (null!=accountDetailBean){
            //是否回复动态推送 1是 0否
            if(accountDetailBean.getData().getIsStartReplyPush() == 0) {
                sb_msg_off.setChecked(false);
            }else {
                sb_msg_off.setChecked(true);
            }
        }
    }
}
