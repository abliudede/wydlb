package com.lianzai.reader.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import android.text.TextUtils;

import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAppComponent;
import com.lianzai.reader.mixpush.DemoMixPushMessageHandler;
import com.lianzai.reader.mixpush.DemoPushContentProvider;
import com.lianzai.reader.module.AppModule;
import com.lianzai.reader.module.ReaderApiModule;
import com.lianzai.reader.utils.AppCrashHandler;
import com.lianzai.reader.utils.AppUtils;
import com.lianzai.reader.utils.ChatRoomSessionHelper;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.NimSDKOptionConfig;
import com.lianzai.reader.utils.Preferences;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.TTAdManagerHolder;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.extension.CustomAttachParser;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


public class BuglyApplicationLike extends Application {

    private AppComponent appComponent;

    public static IWXAPI api;

    private static BuglyApplicationLike sInstance;


//    private RefWatcher refWatcher;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    public static BuglyApplicationLike getsInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return BuglyApplicationLike.getsInstance().getApplicationContext();
    }


    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .readerApiModule(new ReaderApiModule())
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

//        initX5Core();
        TTAdManagerHolder.init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int activityCount;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                activityCount++;
                if (activityCount == 1) { // 应用回到前台
                    //说明从后台回到了前台
                    long lastShowAdvertisementTimeStamp = RxSharedPreferencesUtil.getInstance().getLong(Constant.EXCHANGE_TIME, 0L);
                    long curTime = System.currentTimeMillis();
                    final long advertisementInternalTime = RxSharedPreferencesUtil.getInstance().getInt(Constant.ADVERTISEMENT_INTERNAL_TIME,10) * 60 * 1000;
//                    final long ADVERTISEMENT_INTERNAL_TIME = 10 * 1000;//五秒钟
                    if ((curTime - lastShowAdvertisementTimeStamp > advertisementInternalTime)) {
                        //重新展示开机广告
                        RxEventBusTool.sendEvents(Constant.EventTag.REOPEN_AD);
                    }
                    RxEventBusTool.sendEvents(Constant.EventTag.URL_IDENTIFICATION);

                    //在app内不允许推送消息
                    NIMClient.toggleNotification(false );
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityCount--;
                if(activityCount == 0){
                    //应用切到后台
                    long curTime = System.currentTimeMillis();
                    RxSharedPreferencesUtil.getInstance().putLong(Constant.EXCHANGE_TIME, curTime);


                    //在app内不允许推送消息
                     NIMClient.toggleNotification(true );
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });



        DemoCache.setContext(this);

        RxTool.init(this);
        AppUtils.init(this);


        //去掉安卓9不兼容弹窗
//           if(BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 28){
//               StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//           }
//        LeakCanary.install(this);

        //微信api初始化
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
        //微博sdk初始化
        WbSdk.install(this, new AuthInfo(this, Constant.WEIBO_APP_KEY, Constant.WEIBO_REDIRECT_URL, Constant.WEIBO_SCOPE));

        RxSharedPreferencesUtil.init(this, "lianzaishenqi", Context.MODE_PRIVATE);
        initComponent();

        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // crash handler
        AppCrashHandler.getInstance(this);

        //二维码初始化
        ZXingLibrary.initDisplayOpinion(this);

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {

            //只有生产环境才注册友盟
            if (Constant.appMode == Constant.AppMode.RELEASE) {
                UMConfigure.init(this, Constant.UMENG_APPKEY, Constant.AppMode.RELEASE == Constant.appMode ? null : RxAppTool.getAppVersionName(this), UMConfigure.DEVICE_TYPE_PHONE, null);
                UMConfigure.setLogEnabled(false);
            }

            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());

            // 初始化UIKit模块
            initUIKit();
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);

            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); // 监听的注册，必须在主进程中。
        }
    }

//    private void initX5Core() {
//        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                //x5內核初始化完成的回调，为true表示x5内核加载成功
//                //否则表示x5内核加载失败，会自动切换到系统内核。
//            }
//            @Override
//            public void onCoreInitFinished() {
//            }
//        };
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(), cb);
//    }


    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();


        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

}
