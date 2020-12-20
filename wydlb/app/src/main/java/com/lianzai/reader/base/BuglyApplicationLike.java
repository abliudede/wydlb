package com.lianzai.reader.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.multidex.MultiDex;

import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAppComponent;
import com.lianzai.reader.module.AppModule;
import com.lianzai.reader.module.ReaderApiModule;
import com.lianzai.reader.utils.AppCrashHandler;
import com.lianzai.reader.utils.AppUtils;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.TTAdManagerHolder;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });


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

        // crash handler
        AppCrashHandler.getInstance(this);

        //二维码初始化
        ZXingLibrary.initDisplayOpinion(this);

    }

}
