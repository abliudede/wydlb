package com.lianzai.reader.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.MainThread;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.StartVersionBean;
import com.lianzai.reader.inner.MyCountDownTimerForAD;
import com.lianzai.reader.ui.activity.ActivityAdvertisement;
import com.lianzai.reader.ui.activity.ActivityGuide;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.TTAdManagerHolder;
import com.lianzai.reader.utils.WeakHandler;
import com.lianzai.reader.view.RxToast;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import org.hashids.Hashids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Copyright (C), 2018
 * FileName: SplashActivity
 * Author: lrz
 * Date: 2018/9/28 22:08
 * Description: ${DESCRIPTION}
 */
public class SplashActivity extends PermissionActivity implements SplashADListener,WeakHandler.IHandler{

    Hashids hashids;

    String pushJson = "";//通过推送唤醒的

    String webOpenJson = "";//通过浏览器唤醒的
    private StartVersionBean startVersionBean;

    private ImageView app_logo;
    private ViewGroup container;
    private ImageView splashHolder;
    //开屏广告是否已经加载
    private boolean mHasLoaded;
    public boolean canJump = false;
    public boolean toutiaoCanJump = false;
    private SplashAD splashAD;
    private String configId;
    //sdk渠道，从接口获得后暂存
    private String channel;
    //开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 2000;

    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    private static final int MSG_GO_MAIN = 1;

    private final String CONSTANT_TENGXUN = "tenxun";
    private final String CONSTANT_TOUTIAO = "toutiao";
    private TextView skip_view;
    private MyCountDownTimerForAD myCountDownTimerForAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemBarUtils.readStatusBar1(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        skip_view = (TextView) findViewById(R.id.skip_view);
        app_logo = (ImageView) findViewById(R.id.app_logo);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);

        hashids = new Hashids("ds$#SDa", 8);

        //分析推送数据，并获取到pushJson
        onIntent();
        //处理通过网页打开app的动作，获取webOpenJson
        processWebView();
        if (!TextUtils.isEmpty(pushJson) || !TextUtils.isEmpty(webOpenJson) && !isTaskRoot()) {
            //应用已在后台
            RxEventBusTool.sendEvents(pushJson, Constant.EventTag.PUSH_JSON_TAG);
            RxEventBusTool.sendEvents(webOpenJson, Constant.EventTag.WEB_JSON_TAG);
            finish();
            return;
        }
        if (!isTaskRoot()) {//5.0以上机器
            RxLogTool.e("SplashActivity isTaskRoot....");
            finish();
            return;
        }
        //加载数据开始
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        RxLogTool.e("onNewIntent......");
        setIntent(intent);


        //分析推送数据，并获取到pushJson
        onIntent();
        //处理通过网页打开app的动作，获取webOpenJson
        processWebView();
        if (!TextUtils.isEmpty(pushJson) || !TextUtils.isEmpty(webOpenJson) && !isTaskRoot()) {
            //应用已在后台
            RxEventBusTool.sendEvents(pushJson, Constant.EventTag.PUSH_JSON_TAG);
            RxEventBusTool.sendEvents(webOpenJson, Constant.EventTag.WEB_JSON_TAG);
            finish();
            return;
        }

    }

    // 处理收到的Intent
    private void onIntent() {
        RxLogTool.e("onIntent...");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                parseNotifyIntent(intent);
            }else if(intent.hasExtra("pushJson")){
               pushJson = intent.getStringExtra("pushJson");
            }
        }
    }

    private void parseNotifyIntent(Intent intent) {
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages == null || messages.size() > 1) {
//            RxToast.custom("null.....").show();
        } else {
            try {
                IMMessage message = messages.get(0);

                if (null != message) {
                    //清除未读红点
                    NIMClient.getService(MsgService.class).clearUnreadCount(message.getSessionId(), message.getSessionType());
                }

                if (null != message.getRemoteExtension()) {
                    pushJson = (String) message.getRemoteExtension().get("remoteType");
                } else if (null != message.getPushPayload()) {
                    pushJson = JSON.toJSONString(message.getPushPayload(), true);
                    RxLogTool.e("SplashActivity parseNotifyIntent push json:" + pushJson);
                }


            } catch (Exception e) {
//                e.printStackTrace();
                RxLogTool.e("parseNotifyIntent error:" + e.getMessage());
            }

        }
    }


    private void processWebView() {
        try {
            Intent intent = getIntent();
            Uri uri = intent.getData();
            if (null != uri) {
                String type = uri.getQueryParameter("type");

                JSONObject jsonObject = new JSONObject();
                if (type.equals("1")) {//进圈子
                    if (RxLoginTool.isLogin()) {
                        String id = uri.getQueryParameter("id");
                        int platformId = (int) hashids.decode(id)[0];
                        jsonObject.put("action", Constant.WebOpenAction.ENTER_LIANZAIHAO);
                        jsonObject.put("id", platformId);
                    } else {
                        RxToast.custom("请先登录", Constant.ToastType.TOAST_NORMAL).show();
                    }
                } else if (type.equals("2")) {
                    String id = uri.getQueryParameter("id");
                    int bookListId = (int) hashids.decode(id)[0];
                    jsonObject.put("action", Constant.WebOpenAction.ENTER_BOOK_LIST_DETAIL);
                    jsonObject.put("id", bookListId);
                }

                webOpenJson = jsonObject.toJSONString();
                RxLogTool.e("webOpenJson:" + webOpenJson);
            }else if (intent.hasExtra("type")) {

                JSONObject jsonObject = new JSONObject();

                    String type = intent.getStringExtra("type");
                    if (type.equals("3")) {
                        String param = intent.getStringExtra("param");
                        jsonObject.put("action", Constant.WebOpenAction.GETREWARD);
                        jsonObject.put("param", param);
                    }
                webOpenJson = jsonObject.toJSONString();
                RxLogTool.e("webOpenJson:" + webOpenJson);
                }
        } catch (Exception e) {
        }
    }

    //没登录不能进入主页
    private void enterMainActivity() {
//        app_logo.setVisibility(View.INVISIBLE);
//        container.removeAllViews();
        if(RxLoginTool.isLogin()){
            MainActivity.startActivity(SplashActivity.this, pushJson, webOpenJson);
        }else {
            ActivityLoginNew.startActivity(SplashActivity.this);
        }

        finish();
    }

    private void initData() {
        if (Constant.appMode == Constant.AppMode.DEV) {
            RxToast.custom(getResources().getString(R.string.app_name) + "【开发环境】", Constant.ToastType.TOAST_NORMAL).show();
        } else if (Constant.appMode == Constant.AppMode.BETA) {
            RxToast.custom(getResources().getString(R.string.app_name) + "【测试环境】", Constant.ToastType.TOAST_NORMAL).show();
        }

        String versionName = RxAppTool.getAppVersionName(this);
        //此处做分支
        boolean needShowGuide = RxSharedPreferencesUtil.getInstance().getBoolean(Constant.START_PAGE_VERSION + versionName, true);
        if (needShowGuide) {
            //穿山甲SDK为获取更好的广告推荐效果，以及提高激励视频广告、下载类广告等填充率，建议在广告请求前，合适的时机调用 SDK 提供的方法，如在用户第一次启动您的 app 后的主界面时调用如下方法：
            TTAdManagerHolder.get().requestPermissionIfNecessary(SplashActivity.this);

            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.START_PAGE_VERSION + versionName, false);
            if("3.0.3".equals(versionName)) {
                //只有3.0.3版本的首次更新，才会有特殊更新一次云端记录。
                RxSharedPreferencesUtil.getInstance().putBoolean(Constant.NEEDGETCLOUDRECORD, true);
            }
            //跳转页面
            ActivityGuide.startActivity(SplashActivity.this, pushJson, webOpenJson);
            finish();
        } else {
            //加载开屏广告
//            fetchSplashAD(SplashActivity.this, container,skip_view, Constant.TENCENTAPPID, Constant.TENCENTPOSTID, SplashActivity.this);
            checkStartupVersion();
        }
    }


    private void checkStartupVersion() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/startup/getStartAdvertisementSdkConfig", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    enterMainActivity();
                } catch (Exception ee) {
                    RxLogTool.e("getStartVersion-----" + ee.toString());
                }
            }

            @Override
            public void onResponse(String response) {
                RxLogTool.e("getStartVersion-----" + response);
                try {
                    startVersionBean = GsonUtil.getBean(response, StartVersionBean.class);
                    if (startVersionBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != startVersionBean.getData()) {


                            if (startVersionBean.getData().isAdvertisementFlage()) {
                                //切屏广告展示时间
                                if(startVersionBean.getData().getAdvertisement().getAppLimitMinute() > 0) {
                                    RxSharedPreferencesUtil.getInstance().putInt(Constant.ADVERTISEMENT_INTERNAL_TIME,startVersionBean.getData().getAdvertisement().getAppLimitMinute());
                                }

                                if (startVersionBean.getData().getAdvertisement().getAccessMode() == 1) {
                                    //跳广告页
                                    configId = String.valueOf(startVersionBean.getData().getAdvertisement().getConfigId());
                                    String imgUrl = startVersionBean.getData().getAdvertisement().getImgUrl();
                                    String jumpUrl = startVersionBean.getData().getAdvertisement().getJumpUrl();
                                    ActivityAdvertisement.startActivity(SplashActivity.this, configId, imgUrl, jumpUrl, pushJson, webOpenJson,true);
                                    finish();
                                    return;
                                }else if (startVersionBean.getData().getAdvertisement().getAccessMode() == 3) {
                                    //跳广告页
                                    configId = String.valueOf(startVersionBean.getData().getAdvertisement().getConfigId());
                                    String imgUrl = startVersionBean.getData().getAdvertisement().getImgUrl();
                                    String jumpUrl = startVersionBean.getData().getAdvertisement().getJumpUrl();
                                    ActivityAdvertisement.startActivity(SplashActivity.this, configId, imgUrl, jumpUrl, pushJson, webOpenJson,false);
                                    finish();
                                    return;
                                } else if (startVersionBean.getData().getAdvertisement().getAccessMode() == 2) {
                                    configId = String.valueOf(startVersionBean.getData().getAdvertisement().getConfigId());
                                    channel = startVersionBean.getData().getAdvertisement().getSdkChannel();
                                    startSdkAd();
                                    return;
                                }
                            }
                        }
                    }
                    //没有成功跳转的话，还是跳主页
                    enterMainActivity();
                } catch (Exception e) {
//                    e.printStackTrace();
                    enterMainActivity();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
            if (canJump) {
                mHandler.removeCallbacksAndMessages(null);
                next();
            }
            if (toutiaoCanJump) {
            mHandler.removeCallbacksAndMessages(null);
                toutiaoNext();
            }
           canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
       canJump = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        toutiaoCanJump = true;
    }

    private void startSdkAd() {
        //展示腾讯广告流程
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            //获取完权限再继续
            startLoading();
        }
    }

    private void startLoading(){
        //定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
        if (CONSTANT_TENGXUN.equals(channel)) {
            fetchSplashAD(SplashActivity.this, container,skip_view, Constant.TENCENTAPPID, Constant.TENCENTPOSTID, SplashActivity.this);
        }else if(CONSTANT_TOUTIAO.equals(channel)){
            //加载开屏广告
            loadSplashAd();
        }
    }

    /**
     * 加载开屏广告，穿山甲广告联盟
     */
    private void loadSplashAd() {
        //step2:创建TTAdNative对象
        TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("812108681")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1539)//要减去底部遮挡栏的高度。不然广告会变形。
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                mHasLoaded = true;
                enterMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                enterMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                container.removeAllViews();
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                container.addView(view);
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        saveStartAdvertisementStatistics("2");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        app_logo.setVisibility(View.VISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
                    }

                    @Override
                    public void onAdSkip() {
                      enterMainActivity();
                    }

                    @Override
                    public void onAdTimeOver() {
                        enterMainActivity();
                    }
                });
            }
        }, AD_TIME_OUT);
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            //获取完权限再继续
            startLoading();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            //获取完权限再继续
            startLoading();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            enterMainActivity();
        }
    }

    /**腾讯广告联盟
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity    展示广告的activity
     * @param adContainer 展示广告的大容器
     *                    //     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId       应用ID
     * @param posId       广告位ID
     * @param adListener  广告状态监听器
     *                    //     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer,View skipView,
                               String appId, String posId, SplashADListener adListener) {
        splashAD = new SplashAD(activity,skipView, appId, posId, adListener,0);
        splashAD.fetchAndShowIn(adContainer);
    }


    /*广告关闭时调用，可能是用户关闭或者展示时间到。此时一般需要跳过开屏的 Activity，进入应用内容页面*/
    @Override
    public void onADDismissed() {
        next();
    }


    /*广告加载失败，error 对象包含了错误码和错误信息，错误码的详细内容可以参考文档第5章*/
    @Override
    public void onNoAD(AdError adError) {
        mHasLoaded = true;
        enterMainActivity();
    }

    /*广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）*/
    @Override
    public void onADPresent() {
        app_logo.setVisibility(View.VISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
        mHasLoaded = true;
        skip_view.setText("跳过 >");
        myCountDownTimerForAD=new MyCountDownTimerForAD(4100,1000l,this,skip_view,"跳过 >");
        myCountDownTimerForAD.setCall(new MyCountDownTimerForAD.finishCall() {
            @Override
            public void onFinish() {
            }
        });
        myCountDownTimerForAD.start();
    }

    /*广告被点击时调用，不代表满足计费条件（如点击时网络异常）*/
    @Override
    public void onADClicked() {
        saveStartAdvertisementStatistics("2");
    }

    /**
     * 请求统计接口
     * 类型 1浏览 2点击
     */
    private void saveStartAdvertisementStatistics(String type) {
        HashMap map = new HashMap();
        map.put("configId", configId);
        map.put("type", type);
        map.put("num", "1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/startup/saveStartAdvertisementStatistics", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    /*倒计时回调*/
    @Override
    public void onADTick(long l) {

    }

    /*广告曝光时调用，此处的曝光不等于有效曝光（如展示时长未满足）*/
    @Override
    public void onADExposure() {

    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            enterMainActivity();
        } else {
            canJump = true;
        }
    }

    private void toutiaoNext() {
        if (toutiaoCanJump) {
            enterMainActivity();
        } else {
            toutiaoCanJump = true;
        }
    }



    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                enterMainActivity();
            }
        }
    }
}
