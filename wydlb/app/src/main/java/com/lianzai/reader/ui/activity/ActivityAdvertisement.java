package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.inner.MyCountDownTimerForAD;
import com.lianzai.reader.inner.MyCountDownTimerForGame;
import com.lianzai.reader.ui.SplashActivity;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import me.panpf.sketch.SketchImageView;
import okhttp3.Call;

/**
 * Created by lrz on 2018/11/5
 * 广告页
 */

public class ActivityAdvertisement extends BaseActivity {


    @Bind(R.id.iv_ad)
    SketchImageView iv_ad;

    @Bind(R.id.tv_jump)
    TextView tv_jump;



    String pushJson="";
    String webOpenJson="";//通过浏览器唤醒的
    private String configId;
    private String imgUrl;
    private String jumpUrl;
    private boolean isoutside;

    private MyCountDownTimerForAD myCountDownTimerForAD;

    @Override
    public int getLayoutId() {
        return R.layout.activity_advertise;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
    }

    public static void startActivity(Context context,String configId,String imgUrl,String jumpUrl,String push,String web,boolean isoutside){
        Bundle bundle = new Bundle();
        bundle.putString("configId", configId);
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("jumpUrl", jumpUrl);
        bundle.putString("push", push);
        bundle.putString("web", web);
        bundle.putBoolean("isoutside", isoutside);
        RxActivityTool.skipActivity(context, ActivityAdvertisement.class, bundle);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configId = getIntent().getExtras().getString("configId");
        imgUrl = getIntent().getExtras().getString("imgUrl");
        jumpUrl = getIntent().getExtras().getString("jumpUrl");
        pushJson = getIntent().getExtras().getString("push");
        webOpenJson = getIntent().getExtras().getString("web");
        isoutside = getIntent().getExtras().getBoolean("isoutside");

        //进来先调用统计浏览接口
        //saveStartAdvertisementStatistics("1");

        iv_ad.setZoomEnabled(false);
//        iv_ad.getZoomer().setReadMode(true);
        iv_ad.getOptions().setDecodeGifImage(true);

        //加载图片
        RxImageTool.displayImageBySketch(ActivityAdvertisement.this,imgUrl,iv_ad);

        iv_ad.setOnClickListener(
                v -> {
                    //请求统计点击接口
                    saveStartAdvertisementStatistics("2");
                    //取消倒计时
                    if(null != myCountDownTimerForAD)
                        myCountDownTimerForAD.cancel();
                    if(isoutside) {
                        MainActivity.startActivity(ActivityAdvertisement.this, "skipUrl", jumpUrl);
                    }else {
                        MainActivity.startActivity(ActivityAdvertisement.this, "skipUrlOwn", jumpUrl);
                    }
                    finish();
                }
        );

        if(null != myCountDownTimerForAD)
            myCountDownTimerForAD.cancel();
        myCountDownTimerForAD=new MyCountDownTimerForAD(3100,1000l,this,tv_jump,"跳过 >");
        myCountDownTimerForAD.setCall(new MyCountDownTimerForAD.finishCall() {
            @Override
            public void onFinish() {
                enterMainActivity();
            }
        });
        myCountDownTimerForAD.start();

        tv_jump.setOnClickListener(
                v -> {
                    enterMainActivity();
                }
        );
    }


    /**
     * 请求统计接口
     * 类型 1浏览 2点击
     */
    private void saveStartAdvertisementStatistics(String type){
        HashMap map=new HashMap();
        map.put("configId",configId);
        map.put("type",type);
        map.put("num","1");


        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/startup/saveStartAdvertisementStatistics",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }


    private void enterMainActivity() {
        //取消倒计时
        if(null != myCountDownTimerForAD)
            myCountDownTimerForAD.cancel();
        MainActivity.startActivity(ActivityAdvertisement.this,pushJson,webOpenJson);
        RxLogTool.e("SplashActivity enterMainActivity push json:" + pushJson);
        finish();
    }

    @Override
    public void gc() {
        RxToast.gc();
        if(null != myCountDownTimerForAD)
            myCountDownTimerForAD.cancel();
    }


    @Override
    public void initToolBar() {
    }


}
