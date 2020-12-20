package com.wydlb.first.ui.activity;

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
import androidx.fragment.app.FragmentManager;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountDetailBean;
import com.wydlb.first.bean.AccountTokenBean;
import com.wydlb.first.bean.AwardsResponse;
import com.wydlb.first.bean.BookStoreResponse;
import com.wydlb.first.bean.CheckUpdateResponse;
import com.wydlb.first.bean.DataSynEvent;
import com.wydlb.first.bean.GetCloudRecordBean;
import com.wydlb.first.bean.GetReceiveFlagBean;
import com.wydlb.first.bean.GetTaskCenterShowDictByKeywordBean;
import com.wydlb.first.bean.GetUpgradeNoticeBean;
import com.wydlb.first.bean.ReadSettingsResponse;
import com.wydlb.first.bean.ReceiveWhiteBookCouponBean;
import com.wydlb.first.bean.UrlRecognitionBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.model.bean.BookStoreBeanN;
import com.wydlb.first.model.local.BookStoreRepository;
import com.wydlb.first.receiver.NetworkReceiver;
import com.wydlb.first.ui.contract.AccountContract;
import com.wydlb.first.ui.fragment.New30FindFragment;
import com.wydlb.first.ui.presenter.AccountPresenter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxClipboardTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxReadTimeUtils;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.DragPointView;
import com.wydlb.first.view.ItemLaobaiReward;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogBindPhone;
import com.wydlb.first.view.dialog.RxDialogCheckUpdate;
import com.wydlb.first.view.dialog.RxDialogLaobaiReward;
import com.wydlb.first.view.dialog.RxDialogSureCancelNew;
import com.wydlb.first.view.dialog.RxDialogUrl;
import com.wydlb.first.view.dialog.RxDialogVotingRules;

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

public class MainActivity extends BaseActivity {

    @Bind(R.id.frame_layout)
    FrameLayout frame_layout;


    New30FindFragment newFindFragment;
    private long mClickTime;

    private NetworkReceiver netWorkChangReceiver;
    private AccountDetailBean accountDetailBean;


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

    String webJson = "";

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

    public static void startActivity(Activity context, String webJson) {
        RxActivityTool.removeMainActivity();
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
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
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //任务-沉浸式状态栏
        SystemBarUtils.fullScreen(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "notice";
            String channelName = "圈子回复通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }

        fm = getSupportFragmentManager();

        if (null != getIntent().getExtras()) {
            webJson = getIntent().getExtras().getString(WEB_JSON);
        }
        //网页唤醒处理
        checkWebOpenAction();
        //注册网络状态监听广播
        registerNetworkReceiver();

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

        //检测剪切板数据
        checkClipBord();

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
            needRefreshBookStore = true;
            red_packet_iv.setVisibility(View.GONE);
        } else {
            if (index == 0) {
                red_packet_iv.setVisibility(View.VISIBLE);
            }
            noget_number_award.setVisibility(View.GONE);
            unread_number_tip.setVisibility(View.INVISIBLE);
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



    @OnClick(R.id.red_packet_iv)
    void red_packet_ivClick() {
        red_packet_iv.setVisibility(View.GONE);
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
              if (action.equals(Constant.WebOpenAction.GETREWARD)) {
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
            List<BookStoreBeanN> list = BookStoreRepository.getInstance().getBookStoreRedByUserId(accountTokenBean.getData().getId());
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
                RxLogTool.e("refresh getAccountDetail.......");
            }
        } else if (event.getTag() == Constant.EventTag.HOME_EXIT) {//退出
            finish();
        } else if (event.getTag() == Constant.EventTag.REFRESH_HOME_RED_DOT_TAG) {//底部红点
            int unreadNum = 0;
            if (null != event.getObj())
                unreadNum = Integer.parseInt(event.getObj().toString());
            getUnreadCount(unreadNum);//获取未读消息
        }  else if (event.getTag() == Constant.EventTag.WEB_JSON_TAG) {//web push 消息数据处理
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
        }else if (event.getTag() == Constant.EventTag.URL_IDENTIFICATION) {//识别链接
            if(index == 0) {//只在首页识别。
                checkClipBord();
            }
        } else if (event.getTag() == Constant.EventTag.NETWORK_CONNECT_TAG) {//网络已连接
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

        }
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

}
