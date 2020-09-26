package com.lianzai.reader.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.AwardsResponse;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.CheckUpdateResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetCloudRecordBean;
import com.lianzai.reader.bean.GetReceiveFlagBean;
import com.lianzai.reader.bean.GetTaskCenterShowDictByKeywordBean;
import com.lianzai.reader.bean.GetUpgradeNoticeBean;
import com.lianzai.reader.bean.ParsingCountersignNewBean;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.bean.ReceiveWhiteBookCouponBean;
import com.lianzai.reader.bean.UrlRecognitionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.model.local.CloudRecordRepository;
import com.lianzai.reader.receiver.NetworkReceiver;
import com.lianzai.reader.ui.SplashActivity2;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.ActivityTeamChoose;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityMyNotice;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.activity.taskCenter.ActivityGetRedEnvelope;
import com.lianzai.reader.ui.activity.taskCenter.ActivityNewRedEnvelope;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketRecord;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
import com.lianzai.reader.ui.contract.AccountContract;
import com.lianzai.reader.ui.fragment.BookStoreFragment;
import com.lianzai.reader.ui.fragment.HomePageSwitchFragment;
import com.lianzai.reader.ui.fragment.New30FindFragment;
import com.lianzai.reader.ui.fragment.NewMineFragment;
import com.lianzai.reader.ui.presenter.AccountPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxReadTimeUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.DragPointView;
import com.lianzai.reader.view.ItemLaobaiReward;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogCheckUpdate;
import com.lianzai.reader.view.dialog.RxDialogLaobaiReward;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogUrl;
import com.lianzai.reader.view.dialog.RxDialogVotingRules;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/13.
 * 首页
 */

public class MainActivity extends BaseActivity implements AccountContract.View {

    @Bind(R.id.frame_layout)
    FrameLayout frame_layout;

    RecentContactsFragment recentContactsFragment;
    NewMineFragment mineFragment;

    BookStoreFragment bookStoreFragment;

    New30FindFragment newFindFragment;
    private long mClickTime;

    private NetworkReceiver netWorkChangReceiver;
    private AccountDetailBean accountDetailBean;
    private boolean isShowAd = false;


    HomePageSwitchFragment homePageSwitchFragment;//首页动态

    @Inject
    AccountPresenter accountPresenter;


    @Bind(R.id.rb_book_list)
    RadioButton rb_book_list;

    @Bind(R.id.rb_chasing)
    RadioButton rb_chasing;


    @Bind(R.id.rb_find)
    RadioButton rb_find;


    @Bind(R.id.rb_home)
    RadioButton rb_home;

    @Bind(R.id.rb_my)
    RadioButton rb_my;

    Drawable bookListDrawable;
    Drawable homeDrawable;
    Drawable bookStoreDrawable;
    Drawable findDrawable;
    Drawable myProfileDrawable;

    @Bind(R.id.tb_rg)
    LinearLayout tb_rg;

    @Bind(R.id.unread_number_tip)
    DragPointView unread_number_tip;

    @Bind(R.id.red_packet_iv)
    ImageView red_packet_iv;


    AccountTokenBean accountTokenBean;

    boolean canDrag = true;

    public static int totalUnreadCount = 0;

    @Bind(R.id.rl_chasing)
    RelativeLayout rl_chasing;

    @Bind(R.id.rl_lianzai)
    RelativeLayout rl_lianzai;

    @Bind(R.id.rl_find)
    RelativeLayout rl_find;

    @Bind(R.id.rl_my)
    RelativeLayout rl_my;

    @Bind(R.id.noget_number_award)
    DragPointView noget_number_award;

    @Bind(R.id.noget_number_chasing)
    DragPointView noget_number_chasing;

    //引导页相关视图
    @Bind(R.id.bookstore_yindaoye_iv)
    LinearLayout bookstore_yindaoye_iv;


    RxDialogCheckUpdate updateDialog;

    FragmentManager fm;

    String pushJson = "";
    String webJson = "";

    public static final String PUSH_JSON = "pushJson";
    public static final String WEB_JSON = "webJson";

    private int index = 0;
    //书架是否切到书籍页面
    private boolean isBookStoreTab1 = false;

    private boolean needRefreshBookStore = false;

    private String userId;
    private String bqtKey;
    public static boolean needDelay1;
    public static boolean needDelay2;

    public boolean isBookStoreTab1() {
        return isBookStoreTab1;
    }

    public void setBookStoreTab1(boolean bookStoreTab1) {
        isBookStoreTab1 = bookStoreTab1;
    }

    private RxDialogSureCancelNew rxDialogLogoutTip;//账号被挤下线或者token失效
   //连载口令弹窗
    private RxDialogSureCancelNew rxDialogJoin;
    private RxDialogUrl rxDialogUrl;

    //升级维护弹窗
    RxDialogVotingRules rxDialogVotingRules;//规则弹窗

    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    private RxDialogLaobaiReward rxDialogLaobaiReward;

    public static void startActivity(Activity context, String pushJson, String webJson) {
        RxActivityTool.removeMainActivity();
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PUSH_JSON, pushJson);
        bundle.putString(WEB_JSON, webJson);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        if (RxLoginTool.isLogin()) {
            isShowRed();
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //任务-沉浸式状态栏
        SystemBarUtils.fullScreen(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "连载听书";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);



            channelId = "notice";
            channelName = "圈子回复通知";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }

        accountPresenter.attachView(this);

        fm = getSupportFragmentManager();

        if (null != getIntent().getExtras()) {
            pushJson = getIntent().getExtras().getString(PUSH_JSON);
            webJson = getIntent().getExtras().getString(WEB_JSON);
        }
        //推送点击处理
        checkOpenAction();
        //网页唤醒处理
        checkWebOpenAction();

        //注册网络状态监听广播
        registerNetworkReceiver();

        initTab();

//        setSelected(2);
        if (RxNetTool.isAvailable()) {
            setSelected(0);//默认初始化首页
        } else {
            changeTab(1);//没网络时跳到书架页面
        }

        //拖曳红点
        initUnreadCover();
        //检查更新
        if (Constant.appMode == Constant.AppMode.RELEASE || Constant.appMode == Constant.AppMode.BETA) {//正式环境或测试环境才检查更新 ||Constant.appMode==Constant.AppMode.BETA
            checkUpdateVersion();//检查更新
        }
//
//        //获取金钻广告配置
//        getJinZuanRequest();
        //检查登录前有做什么操作
        //checkLoginBeforeOption();
        refreshIsLoginView();
        //查询任务中心显示值配置
        getTaskCenterShowDictByKeyword();
        //阅读配置读取
        loadSettingsRequest();
        //是否发送消息
        isShowWelcomeMessage();
        //监听用户在线状态
        observerOnline();

        //检测剪切板数据
        checkClipBord();

        //当api版本大于19时，主动判断是否打开通知，未打开则引导跳往设置页面
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Boolean isopenNotification = RxNotificationUntils.isNotificationEnabled(this);
//            if (!isopenNotification && RxSharedPreferencesUtil.getInstance().getBoolean("openNotificationDialog", true)) {
//                rxDialogJoin = new RxDialogSureCancelNew(this, R.style.OptionDialogStyle);
//                rxDialogJoin.setTitle("未开启通知提示");
//                rxDialogJoin.setContent("检测到您未打开通知权限,将收不到系统推送。是否前往设置页面？");
//                rxDialogJoin.getSureView().setText("确定");
//                rxDialogJoin.getCancelView().setText("不再提示");
//                rxDialogJoin.setCanceledOnTouchOutside(true);
//
//                rxDialogJoin.setSureListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
//                            // 进入设置系统应用权限界面
//                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
//                            startActivity(intent);
//                            return;
//                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
//                            // 进入设置系统应用权限界面
//                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
//                            startActivity(intent);
//                            return;
//                        }
//                        rxDialogJoin.dismiss();
//                    }
//                });
//                rxDialogJoin.setCancelListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RxSharedPreferencesUtil.getInstance().putBoolean("openNotificationDialog", false);
//                        rxDialogJoin.dismiss();
//                    }
//                });
//                rxDialogJoin.show();
//            }
//        }


        //授权检测
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
            }

            @Override
            public void noPermission() {

            }
        }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);


        //升级公告获取接口
        getUpgradeNotice();

        //每次开机扫描阅读时长
        RxReadTimeUtils.scanReadTime();

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        channel.enableVibration(false);
        channel.setVibrationPattern(new long[]{0});
        channel.setSound(null, null);
        notificationManager.createNotificationChannel(channel);
    }

    private void refreshIsLoginView() {
        if (RxLoginTool.isLogin()) {
            accountPresenter.getAccountDetail();
            needRefreshBookStore = true;
            String account = RxLoginTool.getLoginAccountToken().getData().getImAccount();
            String token = RxLoginTool.getLoginAccountToken().getData().getImToken();
            RxLogTool.e("getLoginInfo--account:" + account);
            RxLogTool.e("getLoginInfo--token:" + token);
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
                DemoCache.setAccount(account.toLowerCase());
            }
            red_packet_iv.setVisibility(View.GONE);
        } else {
            if (index == 0) {
                red_packet_iv.setVisibility(View.VISIBLE);
            }
            noget_number_award.setVisibility(View.GONE);
            unread_number_tip.setVisibility(View.INVISIBLE);
        }
        //刷新首页数据
        if (null != homePageSwitchFragment) {
            try {
                homePageSwitchFragment.refresh();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 查询升级公告信息
     * 状态 1开启升级公告 0关闭升级公告
     */
    private void getUpgradeNotice() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/startup/getUpgradeNotice", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetUpgradeNoticeBean getUpgradeNoticeBean = GsonUtil.getBean(response, GetUpgradeNoticeBean.class);
                    if (getUpgradeNoticeBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != getUpgradeNoticeBean.getData()) {
                            if(getUpgradeNoticeBean.getData().getStatus() == 1){
                                //展示弹窗
                                rxDialogVotingRules=new RxDialogVotingRules(MainActivity.this,R.style.OptionDialogStyle);
                                rxDialogVotingRules.getTv_title().setText(getUpgradeNoticeBean.getData().getTitle());
                                rxDialogVotingRules.getTv_des().setText(getUpgradeNoticeBean.getData().getContent());
                                rxDialogVotingRules.show();

                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 同步云端阅读记录
     */
    public void getCloudRecord() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/read/progress/query", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e(e.toString());
                } catch (Exception e2) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetCloudRecordBean getCloudRecordBean = GsonUtil.getBean(response, GetCloudRecordBean.class);
                    if (null != getCloudRecordBean) {
                        if (getCloudRecordBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            if (getCloudRecordBean.getData() != null && !getCloudRecordBean.getData().isEmpty()) {
                                RxSharedPreferencesUtil.getInstance().putBoolean(Constant.NEEDGETCLOUDRECORD, false);
                                //同步云端阅读记录。先清除本地记录
//                                CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(accountDetailBean.getData().getUid()));
                                CloudRecordRepository.getInstance().addCloudRecord(getCloudRecordBean.getData());
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private int nogetNumberAwardStr;
    private boolean noticeFlag;

    public int getNogetNumberAwardStr() {
        return nogetNumberAwardStr;
    }

    public boolean isNoticeFlag() {
        return noticeFlag;
    }

    /**
     * 获取任务中心和我的通知红点状态
     */
    public void isShowRed() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/userNoticeFlag", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e(e.toString());
                } catch (Exception e2) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetReceiveFlagBean getReceiveFlagBean = GsonUtil.getBean(response, GetReceiveFlagBean.class);
                    if (null != getReceiveFlagBean) {
                        nogetNumberAwardStr = getReceiveFlagBean.getData().getTaskFlag();
                        noticeFlag = getReceiveFlagBean.getData().isNoticeFlag();

                        if (noticeFlag &&  nogetNumberAwardStr == 0) {
                            //需要显示小红点
                            noget_number_award.setVisibility(View.VISIBLE);
                            noget_number_award.setText("1");
                        } else if(!noticeFlag && nogetNumberAwardStr > 0) {
                            //需要显示大红点
                            noget_number_award.setVisibility(View.VISIBLE);
                            noget_number_award.setText(String.valueOf(nogetNumberAwardStr));
                        }else if(noticeFlag &&  nogetNumberAwardStr > 0){
                            //需要显示大红点
                            noget_number_award.setVisibility(View.VISIBLE);
                            noget_number_award.setText(String.valueOf(nogetNumberAwardStr + 1));
                        }else {
                            //不需要显示红点
                            noget_number_award.setVisibility(View.GONE);
                            noget_number_award.setText("");
                        }


                        try {
                            mineFragment.reSetRed(nogetNumberAwardStr, noticeFlag);
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @OnClick(R.id.red_packet_iv)
    void red_packet_ivClick() {
        red_packet_iv.setVisibility(View.GONE);
        RxActivityTool.skipActivity(this, ActivityNewRedEnvelope.class);
    }


    private void checkWebOpenAction() {

        if (!TextUtils.isEmpty(webJson)) {//webJson不为空，操作
            try {
                JSONObject pushObject = new JSONObject(webJson);
                String action = "";
                if (!pushObject.isNull("action")) {
                    action = pushObject.getString("action");
                }
                int id = 0;
                if (!pushObject.isNull("id")) {
                    id = pushObject.getInt("id");
                }
                if (action.equals(Constant.WebOpenAction.ENTER_LIANZAIHAO)) {
                    ActivityCircleDetail.startActivity(this, String.valueOf(id));
                } else if (action.equals(Constant.WebOpenAction.ENTER_BOOK_LIST_DETAIL)) {
                    ActivityBookListDetail.startActivity(this, String.valueOf(id));
                } else if (action.equals(Constant.WebOpenAction.GETREWARD)) {
                    if (RxLoginTool.isLogin()) {
                        if (!pushObject.isNull("param")) {
                            String param = pushObject.getString("param");
                            //用获取到的param去请求奖励接口
                            receiveWhiteBookCoupon(param);
                        }
                    }else {
                        //未登录跳登录页
                        RxActivityTool.skipActivity(MainActivity.this, ActivityLoginNew.class);
                    }
                }else {

                }

            } catch (Exception e) {
                RxLogTool.e("checkWebOpenAction Exception:" + e.getMessage());
//                e.printStackTrace();
            }
        }
    }


    /**
     * 提交补充网址
     */
    private void receiveWhiteBookCoupon(String param){
        HashMap map=new HashMap();
        map.put("code",param);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/taskCenter/receiveWhiteBookCoupon",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxLogTool.e("receiveWhiteBookCoupon" + e.toString());
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    RxLogTool.e("receiveWhiteBookCoupon" + response);
                    ReceiveWhiteBookCouponBean receiveWhiteBookCouponBean=GsonUtil.getBean(response,ReceiveWhiteBookCouponBean.class);
                    if (receiveWhiteBookCouponBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                        if(receiveWhiteBookCouponBean.getData().isIsSuccess()){

                            //显示弹窗
                            List<ReceiveWhiteBookCouponBean.DataBean.BalancesBean> list = receiveWhiteBookCouponBean.getData().getBalances();
                            if(null != list && !list.isEmpty()){
                                if(null == rxDialogLaobaiReward){
                                    rxDialogLaobaiReward = new RxDialogLaobaiReward(MainActivity.this);
                                }
                                rxDialogLaobaiReward.getLy_item().removeAllViews();
                                for(ReceiveWhiteBookCouponBean.DataBean.BalancesBean item : list ){
                                    ItemLaobaiReward temp = new ItemLaobaiReward(MainActivity.this);
                                    temp.bindData(item);
                                    rxDialogLaobaiReward.getLy_item().addView(temp);
                                }
                                rxDialogLaobaiReward.show();
                            }
                        }else {
                            RxToast.custom(receiveWhiteBookCouponBean.getData().getShowText()).show();
                        }
                    }else{
                        RxToast.custom(receiveWhiteBookCouponBean.getMsg()).show();
                    }
                }catch (Exception e){
                    RxLogTool.e("receiveWhiteBookCoupon" + e.toString());
                }
            }
        });
    }


    private void checkOpenAction() {

        if (!TextUtils.isEmpty(pushJson)) {//pushJson不为空，操作

            //直接跳到我的通知页面
            if ("notice".equals(pushJson)) {
                ActivityMyNotice.startActivity(this);
                return;
            }


            //艾特和引用消息切换
            if ("at".equals(pushJson) || "quote".equals(pushJson)) {
                changeTab(2);
                return;
            }

            if ("skipUrl".equals(pushJson) && !TextUtils.isEmpty(webJson)) {
                isShowAd = true;
                //取消跳转到内部网页
//                ActivityWebView.startActivity(MainActivity.this,webJson);
                startBrowserActivity(webJson);
            }

            if ("skipUrlOwn".equals(pushJson) && !TextUtils.isEmpty(webJson)) {
                isShowAd = true;
                //跳转到内部网页
                ActivityWebView.startActivity(MainActivity.this,webJson);
            }

            try {
                JSONObject jsonObject = new JSONObject(pushJson);
                JSONObject pushObject = jsonObject.getJSONObject("ext");
                String action = "";

                if (!pushObject.isNull("action")) {
                    action = pushObject.getString("action");
                }
                int newsId = 0;
                if (!pushObject.isNull("newsId")) {
                    newsId = pushObject.getInt("newsId");
                }
                if (action.equals(Constant.PushOpenAction.ENTER_INTER_READ)) {//内站书阅读，可以用章节id跳
                    String bookId = pushObject.getString("bookId");
                    String chapterId = pushObject.getString("chapterId");
                    UrlReadActivity.startActivityInsideRead(this, bookId,"",false, chapterId ,"",0,false);
                } else if (action.equals(Constant.PushOpenAction.ENTER_EXTERAL_READ)) {
//                    String bookId = pushObject.getString("bookId");
//                    String chapterId = pushObject.getString("chapterId");
                    String platformId = pushObject.getString("platformId");
                    ActivityCircleDetail.startActivity(this, platformId);
                }
//                else if (action.equals(Constant.PushOpenAction.ENTER_CIRCLE)) {//进入聊天室
//                    String roomId = pushObject.getString("roomId");
//                    ChatRoomActivity.startActivity(this, roomId);
//                }
                else if (action.equals(Constant.PushOpenAction.ENTER_SESSION)) {//进入会话页
                    String sessionId = pushObject.getString("sessionId");
                    int sessionType = pushObject.getInt("sessionType");

                    if (sessionType == SessionTypeEnum.P2P.getValue()) {//连载号详情
                        NimUIKit.startP2PSession(this, sessionId);
                    }
                } else if (action.equals(Constant.PushOpenAction.ENTER_URL)) {
                    String url = pushObject.getString("url");
                    ActivityWebView.startActivity(this, url);
                }else if (action.equals(Constant.PushOpenAction.ENTERMYINFORM)) {
                    //进入我的通知页面
                    ActivityMyNotice.startActivity(this);
//                    String url = pushObject.getString("url");
//                    ActivityWebView.startActivity(this, url);
                }else if (action.equals(Constant.PushOpenAction.ENTER_DYNAMIC)) {
                    //进入动态详情
                    String dynamicId = pushObject.getString("dynamicId");
                    ActivityPostDetail.startActivity(this,dynamicId);
                }else if (action.equals("account-home")) {
                    //钱包首页页面
                    if (RxLoginTool.isLogin()) {
                        AccountDetailBean accountDetailBean = RxTool.getAccountBean();
                        if (null != accountDetailBean && !TextUtils.isEmpty(accountDetailBean.getData().getMobile())) {
                            ActivityWalletMain.startActivity(MainActivity.this);
                        } else {
                            //绑定手机号弹窗
                            if (null == rxDialogBindPhone) {
                                rxDialogBindPhone = new RxDialogBindPhone(MainActivity.this, R.style.OptionDialogStyle);
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
                                        RxActivityTool.skipActivity(MainActivity.this, ActivityBindPhone.class, bundle);
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
                        //直接跳登录页，不关闭之前页面
                        RxActivityTool.skipActivity(MainActivity.this, ActivityLoginNew.class);
                    }
                }else if (action.equals(Constant.ParseUrl.VOTINGRECORDS)) {//自动投票记录
                    ActivityAutoTicketRecord.startActivity(MainActivity.this);
                }
                else {//打开app

                }
                if (!TextUtils.isEmpty(action) && newsId > 0) {//延迟2S，调用奖励接口，跳转到对应的页面后就能弹出奖励接口
                    showawardsRequest(action, newsId, 1500);
                }

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    private void startBrowserActivity(String url) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);
        } catch (Exception e) {
            RxToast.custom("打开第三方浏览器失败", Constant.ToastType.TOAST_ERROR).show();
        }
    }

    private void observerOnline() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        if (status.getValue() == 7) {//被踢
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                            RxActivityTool.returnMainActivity();
                            RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
                            showLogoutDialog(true, getResources().getString(R.string.force_logout_tip));
                        }
                    }
                }, true);
    }

    private void initTab() {
        bookListDrawable = RxImageTool.getDrawable(27, R.drawable.cb_tab_book_list);

        bookStoreDrawable = RxImageTool.getDrawable(27, R.drawable.cb_tab_chasing);

        homeDrawable = RxImageTool.getDrawable(27, R.drawable.cb_tab_home);

        findDrawable = RxImageTool.getDrawable(27, R.drawable.cb_tab_find);

        myProfileDrawable = RxImageTool.getDrawable(27, R.drawable.cb_tab_myprofile);


        rb_book_list.setCompoundDrawables(null, bookListDrawable, null, null);
        rb_chasing.setCompoundDrawables(null, bookStoreDrawable, null, null);
        rb_home.setCompoundDrawables(null, homeDrawable, null, null);
        rb_find.setCompoundDrawables(null, findDrawable, null, null);
        rb_my.setCompoundDrawables(null, myProfileDrawable, null, null);

//        rl_lianzai.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction()==MotionEvent.ACTION_DOWN){
//                    mClickTime=System.currentTimeMillis();
//                }
//                return false;
//            }
//        });

        rl_lianzai.setOnClickListener(
                v -> {
                    if (System.currentTimeMillis() - mClickTime < 800) {
                        //此处做双击具体业务逻辑
                        NIMClient.getService(MsgService.class).clearAllUnreadCount();
                        unread_number_tip.setVisibility(View.INVISIBLE);
                        Badger.updateBadgerCount(0);
                        //会话列表刷新
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_RED_DOT_TAG);
                        RxLogTool.e("rl_lianzai click....double");
                    } else {
                        //表示单击，此处也可以做单击的操作
                        mClickTime = System.currentTimeMillis();
                        changeTab(2);
                        RxLogTool.e("rl_lianzai click....");
                    }
                }
        );
        rl_find.setOnClickListener(
                v -> {
                    changeTab(3);
                    RxLogTool.e("rl_find click....");
                }
        );

        rl_my.setOnClickListener(
                v -> {
                    changeTab(4);
                    RxLogTool.e("rl_my click....");
                }
        );

        rl_chasing.setOnClickListener(
                v -> {
                    rb_chasing.setChecked(true);
                    RxLogTool.e(rl_chasing);
                }
        );

        rb_book_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RxLogTool.e("rb_book_list boolean:" + b);
                if (b) {
                    rb_home.setChecked(false);
                    rb_chasing.setChecked(false);
                    rb_find.setChecked(false);
                    rb_my.setChecked(false);
                    setSelected(0);

                }
            }
        });

        rb_chasing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb_book_list.setChecked(false);
                    rb_home.setChecked(false);
                    rb_find.setChecked(false);
                    rb_my.setChecked(false);
                    RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_REQUEST);
                    setSelected(1);
                }
            }
        });

        rb_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb_book_list.setChecked(false);
                    rb_chasing.setChecked(false);
                    rb_find.setChecked(false);
                    rb_my.setChecked(false);
                    setSelected(2);
                }
            }
        });

        rb_find.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    if (RxLoginTool.isLogin()) {
                    rb_book_list.setChecked(false);
                    rb_home.setChecked(false);
                    rb_chasing.setChecked(false);
                    rb_my.setChecked(false);
                    RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_NEW_FIND);
                    setSelected(3);
//                    } else {
//                        rb_find.setChecked(false);
//                        ActivityLoginNew.startActivity(MainActivity.this);
//                    }
                }
            }
        });

        rb_my.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb_book_list.setChecked(false);
                    rb_home.setChecked(false);
                    rb_chasing.setChecked(false);
                    rb_find.setChecked(false);
                    //因为请求个人信息后会刷新一次，所以此处先不刷
//                    RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
                    setSelected(4);
                }
            }
        });
    }

    private void isShowRecommendSetting() {//是否需要弹出喜好设置界面
        if (!isShowAd) {
            RxActivityTool.skipActivity(this, ActivityGetRedEnvelope.class);
            BookRecommendSettingActivity.startActivity(true, this);
        }
    }


    /**
     * 检查更新
     */
    private void checkUpdateVersion() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/appVersion/check?clientType=1", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("checkUpdateVersion:" + response);
                    CheckUpdateResponse checkUpdateResponse = GsonUtil.getBean(response, CheckUpdateResponse.class);
                    boolean isTest = false;
                    int versionCode = RxAppTool.getAppVersionCode(getApplicationContext());
                    if (isTest || versionCode < checkUpdateResponse.getData().getVersionCode()) {
                        showInstallDialog(checkUpdateResponse);
                    } else {
                        RxLogTool.e("versionCode:" + versionCode + "当前已是最新版本....");
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void showInstallDialog(CheckUpdateResponse checkUpdateResponse) {
        try{
            if(null == MainActivity.this){
                return;
            }
            if (null == updateDialog) {
                updateDialog = new RxDialogCheckUpdate(this, R.style.OptionDialogStyle);
            }
            updateDialog.getTv_update_content().setText(checkUpdateResponse.getData().getIntro());
            if (checkUpdateResponse.getData().getStatus() == 1) {//强制更新
                updateDialog.setCancelable(false);
                updateDialog.setCanceledOnTouchOutside(false);
                updateDialog.getTv_cancel().setVisibility(View.GONE);
                updateDialog.getTv_sure().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
            } else {
                updateDialog.setCancelable(true);
                updateDialog.setCanceledOnTouchOutside(true);
                updateDialog.getTv_cancel().setVisibility(View.VISIBLE);
                updateDialog.getTv_sure().setBackgroundResource(R.drawable.shape_blue_leftbottomyuanjiao);
            }
            updateDialog.show();
        }catch (Exception e){

        }
    }

//    private void installApk(File file) {
//        try {
//            //授权
//            if (Build.VERSION.SDK_INT >= 26) {//8.0以上
//                Uri packageURI = Uri.parse("package:" + getPackageName());
//                if (getPackageManager().canRequestPackageInstalls()) {
//                    RxAppTool.installApk(MainActivity.this, file);
//                    updateDialog.dismiss();
//                } else {
//                    RxToast.custom("安装应用需要打开未知来源权限，请去设置中开启权限", Constant.ToastType.TOAST_NORMAL).show();
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
//                    startActivityForResult(intent, 10086);
//                }
//            } else {
//                RxAppTool.installApk(MainActivity.this, file);
//                updateDialog.dismiss();
//            }
//        } catch (Exception e) {
//
//        }
//    }


    private void getUnreadCount(int unreadNum) {
        RxLogTool.e("unreadNum:" + unreadNum);
        totalUnreadCount = unreadNum;
        if (unreadNum > 0) {
            unread_number_tip.setText(String.valueOf(unreadNum > 99 ? 99 : unreadNum));
            unread_number_tip.setVisibility(View.VISIBLE);
        } else {
            unread_number_tip.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //每次返回到首页的时候如果有未提交的数据，提交数据(非游客登录情况下)
        accountTokenBean = RxLoginTool.getLoginAccountToken();
        if (null != accountTokenBean) {
            //直接获取本地红点
            List<BookStoreBeanN> list = BookStoreRepository.getInstance().getBookStoreRedByUserId(accountTokenBean.getData().getUid());
            if(null != list && !list.isEmpty()){
                //显示书架红点
                noget_number_chasing.setVisibility(View.VISIBLE);
            }else {
                //隐藏书架红点
                noget_number_chasing.setVisibility(View.GONE);
            }
        }else {
            //隐藏书架红点
            noget_number_chasing.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (index!=0){
//                rb_book_list.setChecked(true);
//                setSelected(0);
//            }else{
//
//            }
            try{
                moveTaskToBack(true);
            }catch (Exception e){

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void gc() {
        RxEventBusTool.unRegisterEventBus(this);
//        registerDropCompletedListener(false);
    }


    @Override
    public void getAccountDetailSuccess(AccountDetailBean bean) {
        accountDetailBean = bean;
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID, accountDetailBean.getData().getMobile());
        RxSharedPreferencesUtil.getInstance().putObject(Constant.ACCOUNT_CACHE, accountDetailBean);//缓存账户信息

        //刷新我的fragment
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
        //先存储跳红包页面的动作
        if (accountDetailBean.getData().isIsShowRed()) {
            isShowRecommendSetting();
        }

        //最后，假如已登录请求年度账单接口
        //限制时间请求年度账单接口
//        long startTime = RxTimeTool.string2Milliseconds("2019-01-10 00:00:00");
//        long endTime = RxTimeTool.string2Milliseconds("2019-03-10 23:59:59");
//        long currentTime = RxTimeTool.getCurTimeMills();
//        if(currentTime > startTime && currentTime < endTime) {
//            //处于时间区间内，则请求接口
//            annualPopRequest();
//        }

        //假如需要刷新书架，则请求书架接口
        if (needRefreshBookStore) {
            needRefreshBookStore = false;
            userId = String.valueOf(bean.getData().getUid());
            bqtKey = userId + Constant.BQT_KEY;
            requestBookStore();
        }
        if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.NEEDGETCLOUDRECORD, false)) {

            getCloudRecord();
        }
    }

    //同步书架数据
    private void requestBookStore() {
        ArrayMap map = new ArrayMap();
        long timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
        if (timestamp > 0) {//
            map.put("timestamp", String.valueOf(timestamp));
        }
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/shelf/refresh", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    BookStoreResponse bookStoreResponse = GsonUtil.getBean(response, BookStoreResponse.class);
                    if (bookStoreResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<BookStoreBeanN> bookStoreBeanList = new ArrayList<>();
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getDelete_List() && bookStoreResponse.getData().getDelete_List().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getDelete_List()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,旧书模式
                                    BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(Integer.parseInt(userId), bookStoreBean.getPlatformId());
                                }
                            }
                        }
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,新url模式
                                    BookStoreRepository.getInstance().deleteAllByBookIdAndUid(Integer.parseInt(userId), bookStoreBean.getBookId());
                                }
                            }
                        }
                        //给每本书指定用户ID
                        int i = 0;
                        long time = System.currentTimeMillis();
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(userId));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //给每本书指定用户ID
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(userId));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //缓存这次的请求时间
                        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookStoreResponse.getData().getTimestamp());
                        //显示数据-缓存数据
                        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_TAG);

                    } else {//加载失败
                    }

                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void showError(String message) {
        if (RxNetTool.showNetworkUnavailable(message)) {//网络不可用
            if (null != RxTool.getAccountBean()) {//显示缓存数据
                accountDetailBean = RxTool.getAccountBean();
                RxEventBusTool.sendEvents(accountDetailBean);//读缓存数据
            }
            return;
        }
        RxToast.custom(message, Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void complete(String message) {
        showSeverErrorDialog(message);
    }


    @Override
    public void initToolBar() {

    }

//    int tryNum = 0;//重试次数
//
//    private void requestVisitorBook() {
//        //生成唯一设备号
//        String deviceNo = RxSharedPreferencesUtil.getInstance().getString("deviceNo");
//        if (TextUtils.isEmpty(deviceNo)) {
//            int num = (int) ((Math.random() * 9 + 1) * 100000);
//            deviceNo = RxTimeTool.getCurTimeString() + "Android" + num;
//            deviceNo = RxEncryptTool.encryptMD5ToString(deviceNo, "AndroidSalt");
//            RxSharedPreferencesUtil.getInstance().putString("deviceNo", deviceNo);
//        }
//        //加密操作
//        Map map = new HashMap();
//        map.put("deviceNo", deviceNo);
//        map.put("terminal", String.valueOf(Constant.Channel.ANDROID));
//        map.put("pageSize", "9");
//        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//        parameters.putAll(map);
//        String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
//
//        Map map2 = new HashMap();
//        map2.put("pageSize", "9");
//        map2.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
//
//        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/touristLogin", map2, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("touristLogin:" + response);
//                    BookStoreResponseForVisitor bookStoreResponseforvisitor = GsonUtil.getBean(response, BookStoreResponseForVisitor.class);
//                    if (bookStoreResponseforvisitor.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {//成功
//                        //登录云信。
//                        imLogin(bookStoreResponseforvisitor.getData().getImInfo().getYxAccid(), bookStoreResponseforvisitor.getData().getImInfo().getYxToken());
//                    } else {
//                        if (tryNum < 4) {
//                            requestVisitorBook();//游客重新登录
//                            tryNum++;
//                        }
//                    }
//                } catch (Exception e) {
//                    if (tryNum < 4) {
//                        requestVisitorBook();//游客重新登录
//                        tryNum++;
//                    }
//                }
//            }
//        });
//    }

    /**
     * 此处提供游客登录
//     *
//     * @param imaccount
//     * @param imtoken
     */
//    private void imLogin(String imaccount, String imtoken) {
//        NimUIKit.login(new LoginInfo(imaccount, imtoken), new RequestCallback<LoginInfo>() {
//            @Override
//            public void onSuccess(LoginInfo loginInfo) {
//                DemoCache.setAccount(imaccount);
//                RxTool.saveLoginInfo(imaccount, imtoken);
//                RxLogTool.e("NimUIKit.....onSuccess");
//                RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_OUT_REFRESH_TAG);
//            }
//
//            @Override
//            public void onFailed(int i) {
//                RxLogTool.e("NimUIKit.....onFailed--code" + i);
//                //重新登录IM
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                RxLogTool.e("NimUIKit.....onException-----" + throwable.getMessage());
//
//            }
//        });
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        RxLogTool.e("eventBusNotification.......");
        if (event.getTag() == Constant.EventTag.REFRESH_ACCOUNT) {
            if (RxLoginTool.isLogin()) {
                accountPresenter.getAccountDetail();
                RxLogTool.e("refresh getAccountDetail.......");
            }
        } else if (event.getTag() == Constant.EventTag.TOKEN_FAILURE) {//token 失效 账号退出
            RxLogTool.e("Constant.EventTag.TOKEN_FAILURE....");
            if (RxLoginTool.isLogin()) {
                RxActivityTool.returnMainActivity();
                RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
                showLogoutDialog(false, getResources().getString(R.string.token_invalid_logout_tip));
            }
        } else if (event.getTag() == Constant.EventTag.DISABLE_ACCOUNT) {//账户被禁用
            if (RxLoginTool.isLogin()) {
                RxActivityTool.returnMainActivity();
                RxEventBusTool.sendEvents(Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG);
                showLogoutDialog(true, getResources().getString(R.string.token_disable_logout_tip));
            }
        } else if (event.getTag() == Constant.EventTag.HOME_EXIT) {//退出
            finish();
        } else if (event.getTag() == Constant.EventTag.REFRESH_HOME_RED_DOT_TAG) {//底部红点
            int unreadNum = 0;
            if (null != event.getObj())
                unreadNum = Integer.parseInt(event.getObj().toString());
            getUnreadCount(unreadNum);//获取未读消息
        } else if (event.getTag() == Constant.EventTag.PUSH_JSON_TAG) {//push 消息数据处理
            pushJson = event.getObj().toString();
            checkOpenAction();
        } else if (event.getTag() == Constant.EventTag.WEB_JSON_TAG) {//web push 消息数据处理
            webJson = event.getObj().toString();
            checkWebOpenAction();
        } else if (event.getTag() == Constant.EventTag.SWITCH_BOOK_LIST) {
            //切换到书单
            changeTab(0);
        } else if (event.getTag() == Constant.EventTag.SWITCH_LIANZAI_TAB) {
            //切换到连载
            changeTab(2);
        } else if (event.getTag() == Constant.EventTag.HOBBY_SETTING_CLOSE_TAG) {
            //关闭阅读喜好和为您推荐的书单页面，这个也要切换到书单
            changeTab(0);
        } else if (event.getTag() == Constant.EventTag.SWITCH_BOOK_SHELF) {
            //切换到书架
            isBookStoreTab1 = true;
            changeTab(1);
        } else if (event.getTag() == Constant.EventTag.SHOW_RED_PACKET) {
            //显示小红包
            if (!RxLoginTool.isLogin() && index == 0) {
                red_packet_iv.setVisibility(View.VISIBLE);
            }
        } else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录刷新
            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.NEEDGETCLOUDRECORD, true);
            refreshIsLoginView();
            //重新登录需判断，是否发送消息
            isShowWelcomeMessage();
        } else if (event.getTag() == Constant.EventTag.MAIN_LOGIN_OUT_REFRESH_TAG) {
            RxLogTool.e("MAIN_LOGIN_OUT_REFRESH_TAG");
            //IM 退出
            NimUIKit.logout();
            NIMClient.getService(AuthService.class).logout();
            RxTool.saveLoginInfo("","");//保存imtoken
            DemoCache.clear();

            RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_CACHE);//清除账号相关信息
            RxLoginTool.removeToken();
            accountDetailBean = null;

            refreshIsLoginView();

            RxEventBusTool.sendEvents(Constant.EventTag.LOGIN_OUT_REFRESH_TAG);
        } else if (event.getTag() == Constant.EventTag.URL_IDENTIFICATION) {//识别链接
            if(index == 0) {//只在首页识别。
                checkClipBord();
            }
        } else if (event.getTag() == Constant.EventTag.REOPEN_AD) {//展示开屏广告
            needDelay1 = true;
            SplashActivity2.startActivity(this);
        }else if (event.getTag() == Constant.EventTag.NETWORK_CONNECT_TAG) {//网络已连接
           //每次断网重连进行扫描
            RxReadTimeUtils.scanReadTime();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 10086) {//应用内授权成功，开始安装
//            //安装
//            installApk(file);
//        }
    }


    private void checkClipBord() {
        // 获取剪贴板数据
        String content = null;
        content = RxClipboardTool.getText(MainActivity.this).toString();
        if (!TextUtils.isEmpty(content)) {
            // 执行我们的操作,请求识别链接接口
            String tempUrl = URLUtils.getUrlInContent(content);
            if (!TextUtils.isEmpty(tempUrl)) {
                urlRecognitionRequest(tempUrl);
            } else {
                tempUrl = URLUtils.getKouLingInContent(content);
                if (!TextUtils.isEmpty(tempUrl)) {
                    parsingCountersignRequest(tempUrl.replace("≡", ""));
                }
            }
        }
    }

    /**
     * 检测口令接口
     */
    private void parsingCountersignRequest(String url) {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/parsingCountersignNew/" + url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    ParsingCountersignNewBean urlRecognitionBean = GsonUtil.getBean(response, ParsingCountersignNewBean.class);
                    if (urlRecognitionBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null == rxDialogJoin) {
                            rxDialogJoin = new RxDialogSureCancelNew(MainActivity.this, R.style.OptionDialogStyle);
                        }
                        rxDialogJoin.setTitle("检测到复制的连载口令");
                        rxDialogJoin.getSureView().setText("立即查看");
                        rxDialogJoin.getCancelView().setText("取消");
                        rxDialogJoin.setCanceledOnTouchOutside(true);

                        switch (urlRecognitionBean.getData().getType()){
                            case 1:
                                rxDialogJoin.setContent("你的朋友分享了一本好看的小说给你，快去看看吧~");
                                rxDialogJoin.setSureListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (urlRecognitionBean.getData().getObjectId() > Constant.bookIdLine) {
                                            SkipReadUtil.normalRead(MainActivity.this, String.valueOf(urlRecognitionBean.getData().getObjectId()),"",false);
                                        } else {
                                            //根据源来跳页面,章节随便传一个
                                            SkipReadUtil.normalRead(MainActivity.this, String.valueOf(urlRecognitionBean.getData().getObjectId()), urlRecognitionBean.getData().getSource(),false);
                                        }
                                        rxDialogJoin.dismiss();
                                    }
                                });
                                break;
                            case 2:
                                rxDialogJoin.setContent("你的朋友分享了一个你喜欢的书单给你，快去看看吧～");
                                rxDialogJoin.setSureListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       ActivityBookListDetail.startActivity(MainActivity.this, String.valueOf(urlRecognitionBean.getData().getObjectId()));
                                       rxDialogJoin.dismiss();
                                    }
                                });
                                break;
                            case 3:
                                rxDialogJoin.setContent("你有一个新的书友，ta和你有很多相同的阅读喜好，快去看看吧～");
                                rxDialogJoin.setSureListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PerSonHomePageActivity.startActivity(MainActivity.this, String.valueOf(urlRecognitionBean.getData().getObjectId()));
                                        rxDialogJoin.dismiss();
                                    }
                                });
                                break;
                            case 4:
                                rxDialogJoin.setContent("你的朋友分享了一个很有趣的圈子给你，快去看看吧～");
                                rxDialogJoin.setSureListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCircleDetail.startActivity(MainActivity.this, String.valueOf(urlRecognitionBean.getData().getObjectId()));
                                        rxDialogJoin.dismiss();
                                    }
                                });
                                break;
                        }

                        rxDialogJoin.setCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rxDialogJoin.dismiss();
                            }
                        });
                        rxDialogJoin.show();
                        RxClipboardTool.clearClip(MainActivity.this);
                    }
                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });
    }


    /**
     * url链接识别
     */
    private void urlRecognitionRequest(String url) {

        Map map2 = new HashMap();
        map2.put("bookUrl", url);

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/read/urlRecognition", map2, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                //纯url模式
                if (null == rxDialogUrl) {
                    rxDialogUrl = new RxDialogUrl(MainActivity.this, R.style.OptionDialogStyle);
                }
                //不同类型的页面布局重置,纯url模式
                rxDialogUrl.getTv_read().setVisibility(View.GONE);
                rxDialogUrl.getTv_share().setText("直接打开");
                rxDialogUrl.getRl_web_book().setVisibility(View.GONE);
                rxDialogUrl.getTv_only_url().setVisibility(View.VISIBLE);

                rxDialogUrl.getTv_only_url().setText(url);

                rxDialogUrl.getTv_share().setOnClickListener(
                        v -> {
                            //此处调用查看埋点接口
                            requestCopyTask();
                            ActivityWebView.startActivity(MainActivity.this, url);
                            RxClipboardTool.clearClip(MainActivity.this);
                            rxDialogUrl.dismiss();
                        }
                );
                rxDialogUrl.show();
            }

            @Override
            public void onResponse(String response) {
                try {
                    UrlRecognitionBean urlRecognitionBean = GsonUtil.getBean(response, UrlRecognitionBean.class);
                    if (urlRecognitionBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null == rxDialogUrl) {
                            rxDialogUrl = new RxDialogUrl(MainActivity.this, R.style.OptionDialogStyle);
                        }
                        //布上视图
                        RxImageTool.loadImage(MainActivity.this, urlRecognitionBean.getData().getCover(), rxDialogUrl.getIv_book_cover());
                        rxDialogUrl.getTv_book_title().setText(urlRecognitionBean.getData().getTitle());
                        rxDialogUrl.getTv_book_intro().setText(urlRecognitionBean.getData().getIntro());
                        //不同类型的页面布局重置
                        rxDialogUrl.getTv_read().setVisibility(View.VISIBLE);
                        rxDialogUrl.getTv_share().setText("分享");
                        rxDialogUrl.getRl_web_book().setVisibility(View.VISIBLE);
                        rxDialogUrl.getTv_only_url().setVisibility(View.GONE);

                        rxDialogUrl.getTv_read().setOnClickListener(
                                v -> {
                                    //此处调用查看埋点接口
                                    requestCopyTask();
                                    ActivityWebView.startActivity(MainActivity.this, url, urlRecognitionBean.getData().getTitle(), urlRecognitionBean.getData().getIntro(), urlRecognitionBean.getData().getCover(), 2);
                                    rxDialogUrl.dismiss();
                                }
                        );

                        rxDialogUrl.getTv_share().setOnClickListener(
                                v -> {
                                    if (RxLoginTool.isLogin()) {
                                        ActivityTeamChoose.startActivity(MainActivity.this, urlRecognitionBean.getData().getTitle(), urlRecognitionBean.getData().getIntro(), urlRecognitionBean.getData().getCover(), urlRecognitionBean.getData().getNovel_id(), urlRecognitionBean.getData().getChapter_url());
                                    } else {
                                        ActivityLoginNew.startActivity(MainActivity.this);
                                    }
                                    rxDialogUrl.dismiss();
                                }
                        );
                        rxDialogUrl.show();
                        RxClipboardTool.clearClip(MainActivity.this);
                    } else {
                        //纯url模式
                        if (null == rxDialogUrl) {
                            rxDialogUrl = new RxDialogUrl(MainActivity.this, R.style.OptionDialogStyle);
                        }
                        //不同类型的页面布局重置,纯url模式
                        rxDialogUrl.getTv_read().setVisibility(View.GONE);
                        rxDialogUrl.getTv_share().setText("直接打开");
                        rxDialogUrl.getRl_web_book().setVisibility(View.GONE);
                        rxDialogUrl.getTv_only_url().setVisibility(View.VISIBLE);

                        rxDialogUrl.getTv_only_url().setText(url);
                        rxDialogUrl.getTv_share().setOnClickListener(
                                v -> {
                                    //此处调用查看埋点接口
                                    requestCopyTask();
                                    ActivityWebView.startActivity(MainActivity.this, url);
                                    RxClipboardTool.clearClip(MainActivity.this);
                                    rxDialogUrl.dismiss();
                                }
                        );
                        rxDialogUrl.show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });
    }

    //去复制任务埋点接口
    private void requestCopyTask() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/task/commonMessage/21031", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 转码配置读取
     */
    private void loadSettingsRequest() {
        OKHttpUtil.okHttpGet(Constant.BOOK_SOURCE_SETTINGS_URL, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                ReadSettingsResponse readSettingsResponse = GsonUtil.getBean(response, ReadSettingsResponse.class);
                RxSharedPreferencesUtil.getInstance().putObject(Constant.READ_SETTINGS_KEY, readSettingsResponse);
            }
        });
    }

    /**
     * 年度账单是否弹悬浮窗
     */
//    private void annualPopRequest() {
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/annual/popop", new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                RxLogTool.e("popopRequest:" + e.toString());
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("popopRequest:" + response);
//                    AnnualPopBean annualPopBean = GsonUtil.getBean(response, AnnualPopBean.class);
//                    if (annualPopBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                        if (annualPopBean.getData().isIsPopup()) {
//                            if (null == dialogAnnualBill) {
//                                dialogAnnualBill = new RxDialogAnnualBill(MainActivity.this, R.style.OptionDialogStyle);
//                                dialogAnnualBill.getIv_annual().setOnClickListener(
//                                        v -> {
//                                            ActivityWebView.startActivity(MainActivity.this, annualPopBean.getData().getUrl());
//                                            dialogAnnualBill.dismiss();
//                                        }
//                                );
//                            }
//                            dialogAnnualBill.show();
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//    }


    /**
     * 任务中心配置读取
     */
    private void getTaskCenterShowDictByKeyword() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/invitationShare/getTaskCenterShowDictByKeyword", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    GetTaskCenterShowDictByKeywordBean readSettingsResponse = GsonUtil.getBean(response, GetTaskCenterShowDictByKeywordBean.class);
                    if (null != readSettingsResponse) {
                        StringBuffer sloganSb = new StringBuffer();//加载提示文案
                        for (GetTaskCenterShowDictByKeywordBean.DataBean item : readSettingsResponse.getData()) {

                            if (item.getKeyword() == 10001) {
                                RxSharedPreferencesUtil.getInstance().putString(Constant.TASK_SETTINGS_NEW_AMOUNT, item.getValue());
                            } else if (item.getKeyword() == 10002) {
                                RxSharedPreferencesUtil.getInstance().putString(Constant.TASK_SETTINGS_INVITE_AMOUNT, item.getValue());
                            } else if (item.getKeyword() == 10007) {//loading slogan
                                sloganSb.append(item.getValue());
                                sloganSb.append("&&&&");
                            }else if (item.getKeyword() == 10008) {//阅读界面下载按钮配置，1显示，0关闭
                                RxSharedPreferencesUtil.getInstance().putString(Constant.READ_SHOW_DOWNLOAD, item.getValue());
                            }else if (item.getKeyword() == 10010) {//闪兑按钮显示配置，1显示，0关闭
                                if("1".equals(item.getValue())) {
                                    RxSharedPreferencesUtil.getInstance().putBoolean(Constant.SHANDUI_ANNIU, true);
                                }else {
                                    RxSharedPreferencesUtil.getInstance().putBoolean(Constant.SHANDUI_ANNIU, false);
                                }
                            }
                        }
                        if (null != sloganSb && !TextUtils.isEmpty(sloganSb.toString())) {
                            RxSharedPreferencesUtil.getInstance().putString(Constant.READ_LOADING_SLOGAN, sloganSb.toString());
                        }
                        //不自动弹出红包了
//                        if (!RxLoginTool.isLogin() && !isShowAd) {
//                            red_packet_iv.setVisibility(View.GONE);
//                            RxActivityTool.skipActivity(MainActivity.this, ActivityNewRedEnvelope.class);
//                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    /**
     * 初始化未读红点动画
     */
    private void initUnreadCover() {
        unread_number_tip.setDragListencer(new DragPointView.OnDragListencer() {
            @Override
            public void onDragOut() {
                NIMClient.getService(MsgService.class).clearAllUnreadCount();
                unread_number_tip.setVisibility(View.INVISIBLE);
                Badger.updateBadgerCount(0);
                //会话列表刷新
                RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_RED_DOT_TAG);
                RxLogTool.e("rl_lianzai click....double");
            }
        });

        noget_number_chasing.setDragListencer(new DragPointView.OnDragListencer() {
            @Override
            public void onDragOut() {
                noget_number_chasing.setVisibility(View.INVISIBLE);
                //清除此用户书架所有红点
                BookStoreRepository.getInstance().updateBookStoreBooksByUserId(RxLoginTool.getLoginAccountToken().getData().getUid());
                RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_TAG);
            }
        });

    }

    /**
     * 首次进入发送欢迎消息，？是否需要区分账户？暂放
     */
    private void isShowWelcomeMessage() {
        if (RxSharedPreferencesUtil.getInstance().getBoolean("enterFirst", true) && RxLoginTool.isLogin()) {
            //发送首次进入消息
            OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/platforms/backNews", new CallBackUtil.CallBackString() {
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
                        RxLogTool.e(response);
                    } catch (Exception ee) {

                    }
                }
            });
            //已经启动过
            RxSharedPreferencesUtil.getInstance().putBoolean("enterFirst", false);
        }
    }


    private void setSelected(int i) {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        hideTransaction(fragmentTransaction);//自定义一个方法，来隐藏所有的fragment

        //切换到别的tab之后，把主页的刷新标签置为true
        if (i != 0) {
            if (homePageSwitchFragment != null) {
                homePageSwitchFragment.setNeedRefresh(true);
            }
        }

        switch (i) {
            case 0:
                //只有在首页才显示红包按钮
                if (RxLoginTool.isLogin()) {
                    red_packet_iv.setVisibility(View.GONE);
                } else {
                    red_packet_iv.setVisibility(View.VISIBLE);
                }
                if (homePageSwitchFragment == null) {
                    homePageSwitchFragment = new HomePageSwitchFragment();
                    fragmentTransaction.add(R.id.frame_layout, homePageSwitchFragment);
                }
                fragmentTransaction.show(homePageSwitchFragment);
                break;
            case 1:
                red_packet_iv.setVisibility(View.GONE);
                //引导页逻辑
                if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.YINDAOYE_BOOKSTORE, true)) {
                    RxSharedPreferencesUtil.getInstance().putBoolean(Constant.YINDAOYE_BOOKSTORE, false);
                    bookstore_yindaoye_iv.setVisibility(View.VISIBLE);
                    bookstore_yindaoye_iv.setOnClickListener(
                            v -> {
                                bookstore_yindaoye_iv.setVisibility(View.GONE);
                            }
                    );
                }

                if (bookStoreFragment == null) {
                    bookStoreFragment = new BookStoreFragment();
                    fragmentTransaction.add(R.id.frame_layout, bookStoreFragment);
                }
                fragmentTransaction.show(bookStoreFragment);
                break;
            case 2:
                red_packet_iv.setVisibility(View.GONE);
                if (recentContactsFragment == null) {
                    recentContactsFragment = new RecentContactsFragment();
                    fragmentTransaction.add(R.id.frame_layout, recentContactsFragment);
                }
                fragmentTransaction.show(recentContactsFragment);
                break;
            case 3:
                red_packet_iv.setVisibility(View.GONE);
                if (newFindFragment == null) {
                    newFindFragment = new New30FindFragment();
                    fragmentTransaction.add(R.id.frame_layout, newFindFragment);
                }
                fragmentTransaction.show(newFindFragment);

                break;
            case 4:
                red_packet_iv.setVisibility(View.GONE);
                if (mineFragment == null) {
                    mineFragment = new NewMineFragment();
                    fragmentTransaction.add(R.id.frame_layout, mineFragment);
                }
                fragmentTransaction.show(mineFragment);

                if (RxLoginTool.isLogin()) {
                    isShowRed();
                }

                //每次切换到我的页面，刷新一下账户信息
                if (RxLoginTool.isLogin()) {
                    accountPresenter.getAccountDetail();
                    RxLogTool.e("refresh getAccountDetail.......");
                }
                break;
        }
        index = i;
        fragmentTransaction.commitAllowingStateLoss();//最后千万别忘记提交事务
    }

    //隐藏fragment
    private void hideTransaction(FragmentTransaction ftr) {

        if (homePageSwitchFragment != null) {
            ftr.hide(homePageSwitchFragment);//隐藏该fragment
        }
        if (bookStoreFragment != null) {
            ftr.hide(bookStoreFragment);
        }
        if (recentContactsFragment != null) {
            ftr.hide(recentContactsFragment);
        }
        if (newFindFragment != null) {
            ftr.hide(newFindFragment);
        }
        if (mineFragment != null) {
            ftr.hide(mineFragment);
        }
    }

    private void showawardsRequest(String appEnterType, int newsId, int time) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(needDelay1 || needDelay2){
                    showawardsRequest(appEnterType,newsId,time);
                }else {
                    awardsRequest(appEnterType, newsId);
                }
            }
        }, time);
    }

    /**
     * 推送点击的奖励接口
     */
    private void awardsRequest(String appEnterType, int newsId) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("appEnterType", appEnterType);
                    jsonObject.put("newsId", newsId);
                    RxLogTool.e("awardsRequest:" + jsonObject.toString());
                    OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/awards", jsonObject.toString(), new CallBackUtil.CallBackString() {
                        @Override
                        public void onFailure(Call call, Exception e) {
                        }

                        @Override
                        public void onResponse(String response) {
                            try {
                                AwardsResponse awardsResponse = GsonUtil.getBean(response, AwardsResponse.class);
                                if (awardsResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    if (awardsResponse.getData().isIsAward()) {//奖励
                                        if (null != awardsResponse.getData().getAward()) {
                                                int amount = (int) awardsResponse.getData().getAward().doubleValue();
                                                RxToast.customAward(awardsResponse.getData().getTip(), amount);
                                        }
                                    }
                                } else {
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
//                    e.printStackTrace();
                }
    }

    public void changeTab(int pos) {
        if (pos == 0) {
            rb_book_list.setChecked(true);
        } else if (pos == 1) {
            rb_chasing.setChecked(true);
        } else if (pos == 2) {
            rb_home.setChecked(true);
        } else if (pos == 3) {
            rb_find.setChecked(true);
        } else if (pos == 4) {
            rb_my.setChecked(true);
        }
    }

    /**
     * 账号强制下线后者token失效弹框
     *
     * @param forceLogout
     * @param message
     */
    private void showLogoutDialog(boolean forceLogout, String message) {
        try {

            if (null == rxDialogLogoutTip) {
                rxDialogLogoutTip = new RxDialogSureCancelNew(this, R.style.OptionDialogStyle);
            }
            rxDialogLogoutTip.setTitle("账号退出提示");
            rxDialogLogoutTip.setContent(message);
            if (forceLogout) {
                rxDialogLogoutTip.setButtonText("我知道了", "");
                rxDialogLogoutTip.getCancelView().setVisibility(View.GONE);
                rxDialogLogoutTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
            } else {
                rxDialogLogoutTip.getCancelView().setVisibility(View.VISIBLE);
                rxDialogLogoutTip.setButtonText("立即登录", "取消");
            }

            rxDialogLogoutTip.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogLogoutTip.dismiss();
                    if (!forceLogout) {//跳转至登录
                        ActivityLoginNew.startActivity(MainActivity.this);
                    }
                }
            });

            rxDialogLogoutTip.setCancelListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogLogoutTip.dismiss();
                }
            });

            rxDialogLogoutTip.show();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * 网络监听
     */
    private void registerNetworkReceiver() {
        netWorkChangReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != netWorkChangReceiver) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }

    public HomePageSwitchFragment getHomePageSwitchFragment() {
        return homePageSwitchFragment;
    }
}
