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
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.model.local.MyBookListRepository;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityUserShieldingList;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.contract.LogoutContract;
import com.lianzai.reader.ui.presenter.LogoutPresenter;
import com.lianzai.reader.utils.GlideCacheUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 设置
 */
public class ActivitySettings extends BaseActivity implements  LogoutContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    RxDialogClearCache rxDialogClearCache;
    RxDialogConfirm rxDialogLogout;

    //新增几个视图变动
    @Bind(R.id.tv_userinfo_settings)
    TextView tv_userinfo_settings;
    @Bind(R.id.tv_account_settings)
    TextView tv_account_settings;
    @Bind(R.id.tv_reading_likes)
    TextView tv_reading_likes;
    @Bind(R.id.tv_user_shieldinglist)
    TextView tv_user_shieldinglist;
    @Bind(R.id.tv_push_setting)
    TextView tv_push_setting;


    @Bind(R.id.tv_logout)
    TextView tv_logout;

    @Bind(R.id.sb_msg_off)
    CheckBox sb_msg_off;

    @Bind(R.id.sb_auto_exchange_off)
    CheckBox sb_auto_exchange_off;

    @Inject
    LogoutPresenter logoutPresenter;

    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗

    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    public static void startActivity(Context context){
        Bundle bundle=new Bundle();
        RxActivityTool.skipActivity(context,ActivitySettings.class,bundle);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_settings;
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
        logoutPresenter.attachView(this);

        tv_userinfo_settings.setVisibility(View.GONE);
        tv_account_settings.setVisibility(View.GONE);
        tv_user_shieldinglist.setVisibility(View.GONE);
        tv_reading_likes.setVisibility(View.GONE);
        tv_logout.setVisibility(View.GONE);
        tv_push_setting.setVisibility(View.GONE);
        if(RxLoginTool.isLogin()){
            tv_userinfo_settings.setVisibility(View.VISIBLE);
            tv_account_settings.setVisibility(View.VISIBLE);
            tv_reading_likes.setVisibility(View.VISIBLE);
            tv_user_shieldinglist.setVisibility(View.VISIBLE);
            tv_logout.setVisibility(View.VISIBLE);
            tv_push_setting.setVisibility(View.VISIBLE);
        }

        rxDialogGoLogin = new RxDialogGoLogin(ActivitySettings.this, R.style.OptionDialogStyle);
        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.COULD_VIBRATOR,true)) {
            sb_msg_off.setChecked(true);
        }else {
            sb_msg_off.setChecked(false);
        }

        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
            sb_auto_exchange_off.setChecked(true);
        }else {
            sb_auto_exchange_off.setChecked(false);
        }


        tv_commom_title.setText("设置");

        initClearCacheDialog();
        initLogoutDialog();
    }

    @OnCheckedChanged(R.id.sb_msg_off)void sb_msg_offChanged(boolean isChecked){
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.COULD_VIBRATOR,isChecked);
    }

    @OnCheckedChanged(R.id.sb_auto_exchange_off)void sb_auto_exchange_offChanged(boolean isChecked){
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.AUTO_EXCHANGE,isChecked);
    }


    /**
     * 清除缓存
     */
    @OnClick(R.id.tv_clear_cache)void clearCacheClick(){
        if (null==rxDialogClearCache)
            return;
        rxDialogClearCache.getTv_sure1().setText("清除书籍缓存("+ GlideCacheUtil.getInstance().getBookCacheSize(this)+")");
        rxDialogClearCache.getTv_sure2().setText("清除图片缓存("+ GlideCacheUtil.getInstance().getPicCacheSize(this)+")");
        rxDialogClearCache.show();
    }


    private void initClearCacheDialog(){
        rxDialogClearCache=new RxDialogClearCache(this,R.style.BottomDialogStyle);
        rxDialogClearCache.getTv_sure1().setOnClickListener(
                v->{
                    try{
                        rxDialogClearCache.dismiss();
                        RxFileTool.cleanCustomCache(Constant.BOOK_CACHE_PATH);//本地书缓存
                        RxToast.custom("缓存已清除成功",Constant.ToastType.TOAST_SUCCESS).show();
                        //判断是否已登录，已登录情况，清除账户相关
                        if(RxLoginTool.isLogin()){
                            int uid = RxLoginTool.getLoginAccountToken().getData().getUid();
                            RxSharedPreferencesUtil.getInstance().remove(String.valueOf(uid) +"_WDSD");//清除书单缓存
                            RxSharedPreferencesUtil.getInstance().remove(String.valueOf(uid) + "_BQT");//清除书籍token
                            RxSharedPreferencesUtil.getInstance().remove(String.valueOf(uid) +  Constant.BQT_KEY);//清除书籍token
                            //数据库清除书架
                            BookStoreRepository.getInstance().deleteAllByUid(uid);
                        }
                    }catch (Exception e){
                        RxLogTool.e(e.toString());
                    }
                }
        );
        rxDialogClearCache.getTv_sure2().setOnClickListener(
                v->{
                    GlideCacheUtil.getInstance().clearImageAllCache(this);
                    RxToast.custom("缓存已清除成功",Constant.ToastType.TOAST_SUCCESS).show();
                    rxDialogClearCache.dismiss();
                }
        );
    }

    private void initLogoutDialog(){
        rxDialogLogout=new RxDialogConfirm(this,R.style.BottomDialogStyle);
        rxDialogLogout.getTv_title().setText("退出登录");
        rxDialogLogout.getTv_sure().setText("确认退出");
        rxDialogLogout.getTv_sure().setOnClickListener(
                v->{

                    rxDialogLogout.dismiss();
                    logoutPresenter.logout();

//                    RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
//                     RxLoginTool.removeToken();//清除本地token

//                    RxActivityTool.skipActivityAndFinishAll(this,SplashActivity.class);
                }
        );
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
    public void logoutSuccess(BaseBean bean) {
        RxLogTool.e("logout success...");


        RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);//账号退出
        finish();
        dismissDialog();
//
//        rxDialogLogout.dismiss();

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

    /**
     * 退出
     */
    @OnClick(R.id.tv_logout)void logoutClick(){
        rxDialogLogout.show();
    }

    @OnClick(R.id.tv_account_settings)void accountSettingsClick(){
        if(!RxLoginTool.isLogin()){
            RxToast.custom("未登录无法进行账号设置").show();
            return;
        }
        AccountDetailBean accountDetailBean = RxTool.getAccountBean();
        if(null == accountDetailBean){
            RxToast.custom("未登录无法进行账号设置").show();
            return;
        }
        if(TextUtils.isEmpty(accountDetailBean.getData().getMobile())){
            //绑定手机号弹窗
            if(null == rxDialogBindPhone) {
                rxDialogBindPhone = new RxDialogBindPhone(ActivitySettings.this, R.style.OptionDialogStyle);
                rxDialogBindPhone.setContent("您暂未绑定手机号，请先绑定手机号再使用相关功能。");
                rxDialogBindPhone.setTitle("绑定手机号提示");
                rxDialogBindPhone.getCancelView().setVisibility(View.VISIBLE);
                rxDialogBindPhone.setButtonText("立即绑定", "取消");
                rxDialogBindPhone.setSureListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        //跳转到绑定手机号页面
                        rxDialogBindPhone.dismiss();
                        dismissDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
                        RxActivityTool.skipActivity(ActivitySettings.this, ActivityBindPhone.class, bundle);
                    }
                });

                rxDialogBindPhone.setCancelListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        rxDialogBindPhone.dismiss();
                    }
                });
            }
            rxDialogBindPhone.show();
        }else {
            RxActivityTool.skipActivity(this,ActivityAccountSecuritySettings.class);
        }
    }

    //阅读喜好
    @OnClick(R.id.tv_reading_likes)void readingLikeClick(){
        if (RxLoginTool.isLogin()) {
            RxActivityTool.skipActivity(this, BookRecommendSettingActivity.class);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(this, ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    //黑名单
    @OnClick(R.id.tv_user_shieldinglist)
    void shieldinglistClick(){
        ActivityUserShieldingList.startActivity(ActivitySettings.this);
    }

    //推送设置点击
    @OnClick(R.id.tv_push_setting)
    void tv_push_settingClick(){
        ActivityPushSettings.startActivity(ActivitySettings.this);
    }





    @OnClick(R.id.tv_about_ours)
    void aboutUsClick() {
        RxActivityTool.skipActivity(this, ActivityAboutUs.class);
    }


    @OnClick(R.id.tv_userinfo_settings)
    void userinfoClick() {
        if (RxLoginTool.isLogin()) {
            RxActivityTool.skipActivity(ActivitySettings.this, ActivityUserProfile.class);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(ActivitySettings.this, ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
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
