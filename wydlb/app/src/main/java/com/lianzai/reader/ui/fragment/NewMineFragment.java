package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetUserPersonalStatisticsBean;
import com.lianzai.reader.bean.StartVersionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityAboutUs;
import com.lianzai.reader.ui.activity.ActivityBindPhone;
import com.lianzai.reader.ui.activity.ActivityLogin;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivitySafety;
import com.lianzai.reader.ui.activity.ActivitySettings;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.BookRecommendSettingActivity;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityAttentionPersonList;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityMyDynamic;
import com.lianzai.reader.ui.activity.circle.ActivityMyNotice;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
import com.lianzai.reader.ui.contract.AccountContract;
import com.lianzai.reader.ui.presenter.AccountPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;
import com.lianzai.reader.view.dialog.RxDialogSelectImage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 新版我的
 */
public class NewMineFragment extends BaseFragment{

    RxDialogSelectImage rxDialogSelectImage;

    @Bind(R.id.tv_username)
    TextView tv_username;

    @Bind(R.id.iv_logo)
    CircleImageView iv_logo;

    @Bind(R.id.tv_user_intro)
    TextView tv_user_intro;

    @Bind(R.id.ly_user_information)
    LinearLayout ly_user_information;

    @Bind(R.id.tv_read_time)
    TextView tv_read_time;

//    @Bind(R.id.tv_read_book)
//    TextView tv_read_book;

    @Bind(R.id.tv_follow)
    TextView tv_follow;

    @Bind(R.id.tv_follower)
    TextView tv_follower;

    int totalReadTime = 0;//阅读时长

    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗

    @Bind(R.id.rl_top_view)
    RelativeLayout rl_top_view;

    @Bind(R.id.tv_author_center)
    View tv_author_center;

    @Bind(R.id.tv_help)
    View tv_help;

    @Bind(R.id.notice_red)
    View notice_red;



    @Bind(R.id.task_count)
    TextView task_count;

    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.fragment_new_mine;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        refreshView();

        rxDialogSelectImage = new RxDialogSelectImage(getContext(), R.style.BottomDialogStyle);
//        totalReadTime=RxSharedPreferencesUtil.getInstance().getInt("readTotalTime",0);

        rxDialogGoLogin = new RxDialogGoLogin(getActivity(), R.style.OptionDialogStyle);


        iv_logo.setOnClickListener(
                v -> {
                    if (RxLoginTool.isLogin()) {
                        PerSonHomePageActivity.startActivity(getActivity(),String.valueOf(RxLoginTool.getLoginAccountToken().getData().getUid()));
//                        PerSonHomePageActivity.startActivity(getActivity(),"499382");
                    } else {
                        //弹登录提示
                        rxDialogGoLogin.getTv_sure().setOnClickListener(
                                view -> {
                                    rxDialogGoLogin.dismiss();
                                    //直接跳登录页，不关闭之前页面
                                    RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                                }
                        );
                        rxDialogGoLogin.show();
                    }
                }
        );
        tv_username.setOnClickListener(
                v -> {
                    if (RxLoginTool.isLogin()) {
                        PerSonHomePageActivity.startActivity(getActivity(),String.valueOf(RxLoginTool.getLoginAccountToken().getData().getUid()));
                    } else {
                        //弹登录提示
                        rxDialogGoLogin.getTv_sure().setOnClickListener(
                                view -> {
                                    rxDialogGoLogin.dismiss();
                                    //直接跳登录页，不关闭之前页面
                                    RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                                }
                        );
                        rxDialogGoLogin.show();
                    }
                }
        );

        //作家专区
        tv_author_center.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                if (RxLoginTool.isLogin()) {
                    ActivityWebView.startActivity(getActivity(), Constant.H5_BASE_URL + "/author",1);
                } else {
                    //弹登录提示
                    rxDialogGoLogin.getTv_sure().setOnClickListener(
                            view -> {
                                rxDialogGoLogin.dismiss();
                                //直接跳登录页，不关闭之前页面
                                RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                            }
                    );
                    rxDialogGoLogin.show();
                }
            }
        });

        //帮助与反馈
        tv_help.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                //跳帮助与反馈圈子
                ActivityCircleDetail.startActivity(getActivity(),"23");
//                ActivityWebView.startActivity(getActivity(), Constant.H5_HELP_BASE_URL);
            }
        });

        //刚建立时要主动去拿一次。
        reSetRed();
    }

    public void reSetRed(int str,boolean noticeFlag){
        if(str > 0) {
            task_count.setVisibility(View.VISIBLE);
            task_count.setText(String.valueOf(str));
        }else {
            task_count.setVisibility(View.GONE);
            task_count.setText("");
        }
        if(noticeFlag){
            notice_red.setVisibility(View.VISIBLE);
        }else {
            notice_red.setVisibility(View.GONE);
        }
    }

    private void reSetRed(){
        if(null != getActivity()){
            int str = 0;
            boolean notice = false;
            try{
                str = ((MainActivity)getActivity()).getNogetNumberAwardStr();
                notice = ((MainActivity)getActivity()).isNoticeFlag();
            }catch (Exception e){

            }
           reSetRed(str,notice);
        }
    }

    private void refreshView(){
        if (RxLoginTool.isLogin()) {
            AccountDetailBean accountDetailBean = RxTool.getAccountBean();
            if(null != accountDetailBean){
                tv_username.setText(accountDetailBean.getData().getNickName());
                if(!TextUtils.isEmpty(accountDetailBean.getData().getIntroduce())) {
                    tv_user_intro.setText(accountDetailBean.getData().getIntroduce());
                }else {
                    tv_user_intro.setText("");
                }
                RxImageTool.loadLogoImage(getActivity(), accountDetailBean.getData().getAvatar(), iv_logo);
                requestReadTotalTime();
            }else {
                tv_username.setText("登录/注册");
                iv_logo.setImageResource(R.mipmap.default_fangxing_logo);
                tv_user_intro.setText("登录后就可以追看喜爱的小说啦！");
                ly_user_information.setVisibility(View.GONE);
                tv_read_time.setText("0时");
//                tv_read_book.setText("0");
                tv_follow.setText("0人");
                tv_follower.setText("0人");
            }
        }else {
            tv_username.setText("登录/注册");
            iv_logo.setImageResource(R.mipmap.default_fangxing_logo);
            tv_user_intro.setText("登录后就可以追看喜爱的小说啦！");
            ly_user_information.setVisibility(View.GONE);
            tv_read_time.setText("0时");
//            tv_read_book.setText("0");
            tv_follow.setText("0人");
            tv_follower.setText("0人");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }


    @Override
    public void fetchData() {


    }

    /**
     * 我的动态
     */
    @OnClick(R.id.tv_dongtai)
    void tv_dongtaiClick() {
        if (RxLoginTool.isLogin()) {
            ActivityMyDynamic.startActivity(getActivity());
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    /**
     * 我的通知
     */
    @OnClick(R.id.tv_tongzhi)
    void tv_tongzhiClick() {
        if (RxLoginTool.isLogin()) {
            ActivityMyNotice.startActivity(getActivity());
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }


    /**
     * 邀请好友
     */
    @OnClick(R.id.lv_invite)
    void lv_inviteClick() {
        if (RxLoginTool.isLogin()) {
            ActivityTaskCenter.startActivity(getActivity(),1);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    /**
     * 任务中心
     */
    @OnClick(R.id.lv_taskcenter)
    void lv_taskcenterClick() {
        if (RxLoginTool.isLogin()) {
            ActivityTaskCenter.startActivity(getActivity(),0);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }




    /**
     * 我的钱包
     */
    @OnClick(R.id.tv_wallet)
    void walletClick() {
        if (RxLoginTool.isLogin()) {
            AccountDetailBean accountDetailBean = RxTool.getAccountBean();
            if (null != accountDetailBean && !TextUtils.isEmpty(accountDetailBean.getData().getMobile())) {
                ActivityWalletMain.startActivity(getActivity());
            } else {
                //绑定手机号弹窗
                if (null == rxDialogBindPhone) {
                    rxDialogBindPhone = new RxDialogBindPhone(getActivity(), R.style.OptionDialogStyle);
                    rxDialogBindPhone.setContent("您暂未绑定手机号，请先绑定手机号再使用相关功能。");
                    rxDialogBindPhone.setTitle("绑定手机号提示");
                    rxDialogBindPhone.getCancelView().setVisibility(View.VISIBLE);
                    rxDialogBindPhone.setButtonText("立即绑定", "取消");
                    rxDialogBindPhone.setSureListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            //跳转到绑定手机号页面
                            rxDialogBindPhone.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
                            RxActivityTool.skipActivity(getActivity(), ActivityBindPhone.class, bundle);
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
            }
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    /**
     * 跳转到实名认证
     */
    @OnClick(R.id.tv_auth_real_name)
    void realNameAuthClick() {
        if (RxLoginTool.isLogin()) {
            RxActivityTool.skipActivity(getActivity(), ActivitySafety.class);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }

    }


    /**
     * 设置
     */
    @OnClick(R.id.tv_setting)
    void settingsClick() {
//        if (RxLoginTool.isLogin()) {
            RxActivityTool.skipActivity(getActivity(), ActivitySettings.class);
//        } else {
//            //弹登录提示
//            rxDialogGoLogin.getTv_sure().setOnClickListener(
//                    view -> {
//                        rxDialogGoLogin.dismiss();
//                        //直接跳登录页，不关闭之前页面
//                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
//                    }
//            );
//            rxDialogGoLogin.show();
//        }
    }

    /**
     * 参与内测
     */
    @OnClick(R.id.tv_join)
    void joinClick() {
        ActivityWebView.startActivity(getActivity(),"https://fx.bendixing.net/taskCenter/#/joinbeta");
    }

//    /**
//     * 在看的书点击
//     */
//    @OnClick(R.id.tv_read_book)
//    void tv_read_bookClick() {
//        RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_SHELF);
//    }

    /**
     * 我的关注列表跳转
     */
    @OnClick(R.id.tv_follow)
    void tv_followClick() {
        if (RxLoginTool.isLogin()) {
            ActivityAttentionPersonList.startActivity(getActivity(),0,1);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }
    /**
     * 我的关注列表跳转
     */
    @OnClick(R.id.tv_follow2)
    void tv_follow2Click() {
        if (RxLoginTool.isLogin()) {
            ActivityAttentionPersonList.startActivity(getActivity(),0,1);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    /**
     * 我的粉丝列表跳转
     */
    @OnClick(R.id.tv_follower)
    void tv_followerClick() {
        if (RxLoginTool.isLogin()) {
            ActivityAttentionPersonList.startActivity(getActivity(),0,2);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }
    /**
     * 我的粉丝列表跳转
     */
    @OnClick(R.id.tv_follower2)
    void tv_follower2Click() {
        if (RxLoginTool.isLogin()) {
            ActivityAttentionPersonList.startActivity(getActivity(),0,2);
        } else {
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                    }
            );
            rxDialogGoLogin.show();
        }
    }



    private void requestReadTotalTime() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/getUserPersonalStatistics", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    ly_user_information.setVisibility(View.GONE);
                    tv_read_time.setText("0时");
//                    tv_read_book.setText("0");
                    tv_follow.setText("0人");
                    tv_follower.setText("0人");
                }catch (Exception ee){

                }

            }

            @Override
            public void onResponse(String response) {

                try {
                    GetUserPersonalStatisticsBean getUserPersonalStatisticsBean = GsonUtil.getBean(response, GetUserPersonalStatisticsBean.class);
                    if (getUserPersonalStatisticsBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        totalReadTime = getUserPersonalStatisticsBean.getData().getReadTime();
                        RxSharedPreferencesUtil.getInstance().putInt("readTotalTime", totalReadTime);

                        ly_user_information.setVisibility(View.VISIBLE);
                        tv_read_time.setText(RxTimeTool.getHourTime(totalReadTime));
//                        tv_read_book.setText(String.valueOf(getUserPersonalStatisticsBean.getData().getBookListSum()));
                        tv_follow.setText(String.valueOf(getUserPersonalStatisticsBean.getData().getAttentionSum())+ "人");
                        tv_follower.setText(String.valueOf(getUserPersonalStatisticsBean.getData().getFansSum())+ "人");

                    }else {
                        ly_user_information.setVisibility(View.GONE);
                        tv_read_time.setText("0时");
//                        tv_read_book.setText("0");
                        tv_follow.setText("0人");
                        tv_follower.setText("0人");

                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.REFRESH_USER_MINE_TAG) {//切换到此页面或者账号信息变更
            refreshView();
        } else if (event.getTag() == Constant.EventTag.LOGIN_OUT_REFRESH_TAG) {//退出登录成功
            RxLogTool.e("NewMineFragment LOGIN_OUT_REFRESH_TAG");
//            iv_vip.setVisibility(View.INVISIBLE);
            tv_username.setText("登录/注册");
            iv_logo.setImageResource(R.mipmap.default_fangxing_logo);
            tv_user_intro.setText("登录后就可以追看喜爱的小说啦！");
            ly_user_information.setVisibility(View.GONE);
            tv_read_time.setText("0时");
//            tv_read_book.setText("0");
            tv_follow.setText("0人");
            tv_follower.setText("0人");
        }
    }


}
