package com.wydlb.first.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.CheckUpdateResponse;
import com.wydlb.first.bean.DataSynEvent;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.model.bean.BannerBean;
import com.wydlb.first.receiver.NetworkReceiver;
import com.wydlb.first.ui.adapter.NewFindAdapter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogCheckUpdate;
import com.wydlb.first.view.dialog.RxDialogSureCancelNew;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/13.
 * 首页
 */

public class MainActivity extends BaseActivity {

    @Bind(R.id.background)
    LinearLayout background;

    private NetworkReceiver netWorkChangReceiver;

    RxDialogCheckUpdate updateDialog;

    private RxDialogSureCancelNew rxDialogSureCancelNew;//修改人员的通用弹框


    //项目列表
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    List<BannerBean.DataBean> bannerBeanList = new ArrayList<>();
    NewFindAdapter newFindAdapter;
    RxLinearLayoutManager manager;


    public static void startActivity(Activity context) {
        RxActivityTool.removeMainActivity();
        Intent intent = new Intent(context, MainActivity.class);
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
            String channelName = "我要当老板运营通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
        //注册网络状态监听广播
        registerNetworkReceiver();


        //检查更新
//        if (Constant.appMode == Constant.AppMode.RELEASE || Constant.appMode == Constant.AppMode.BETA) {//正式环境或测试环境才检查更新 ||Constant.appMode==Constant.AppMode.BETA
//            checkUpdateVersion();//检查更新
//        }

        newFindAdapter = new NewFindAdapter(bannerBeanList,this);
        manager=new RxLinearLayoutManager(this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(newFindAdapter);

        newFindAdapter.setOnItemClickListener(
                (adapter, view, position) -> {
                    if(null != bannerBeanList && !bannerBeanList.isEmpty()) {
                        BannerBean.DataBean bannerBean = bannerBeanList.get(position);
                    }
                }
        );
        //获取数据
        findRequest();

    }

    private void findRequest() {
        Map<String, String> map=new HashMap<>();
        map.put("putPosition",String.valueOf(4));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("gameSwitchRequest:" + response);
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(null != bannerBean.getData() && !bannerBean.getData().isEmpty()){
                            bannerBeanList.clear();
                            bannerBeanList.addAll(bannerBean.getData());
                            newFindAdapter.replaceData(bannerBeanList);
                        }
                    } else {
                        RxToast.custom(bannerBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        RxLogTool.e("eventBusNotification.......");
        if (event.getTag() == Constant.EventTag.REFRESH_ACCOUNT) {
            if (RxLoginTool.isLogin()) {
                RxLogTool.e("refresh getAccountDetail.......");
            }
        } else if (event.getTag() == Constant.EventTag.HOME_EXIT) {//退出
            finish();
        } else if (event.getTag() == Constant.EventTag.NETWORK_CONNECT_TAG) {//网络已连接
           //每次断网重连进行扫描
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 弹出人员管理弹窗
     * 1、修改项目投入人员，2、解雇公司人员，3、招聘公司人员
     */
    private void showLogoutDialog(int type, Long max, Long gold) {
        try {

            if (null == rxDialogSureCancelNew) {
                rxDialogSureCancelNew = new RxDialogSureCancelNew(this,type,max,gold);
            }else {
                rxDialogSureCancelNew.reset(type,max,gold);
                rxDialogSureCancelNew.setParams();
            }
            rxDialogSureCancelNew.show();
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
