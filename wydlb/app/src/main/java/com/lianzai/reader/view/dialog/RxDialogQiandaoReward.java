package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.inner.MyCountDownTimerForAD;
import com.lianzai.reader.inner.MyCountDownTimerForAD2;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.ui.activity.ActivityBountyHunter;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityOneKeyReward;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 签到奖励，大图版本
 */
public class RxDialogQiandaoReward extends RxDialog {

    TextView tv_description;

    private ImageView iv_content;
    private FrameLayout ly_ad;


    TextView tv_know_it;
    TextView tv_more;
    TextView tv_second;
    private View dialogView;
    private TextView qiandao_des_tv;

    private NativeExpressADView nativeExpressADView;
    private BannerBean.DataBean bannerBean;
    private MyCountDownTimerForAD2 myCountDownTimerForAD;

    public RxDialogQiandaoReward(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogQiandaoReward(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogQiandaoReward(Activity context) {
        super(context,R.style.NoneDialogStyle);
        initView();
    }

    public RxDialogQiandaoReward(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public TextView getTv_more() {
        return tv_more;
    }

    public void setTv_more(TextView tv_more) {
        this.tv_more = tv_more;
    }

    public TextView getTv_second() {
        return tv_second;
    }

    public void setTv_second(TextView tv_second) {
        this.tv_second = tv_second;
    }

    public TextView getTv_know_it() {
        return tv_know_it;
    }

    public void setTv_know_it(TextView tv_know_it) {
        this.tv_know_it = tv_know_it;
    }

    public TextView getTv_description() {
        return tv_description;
    }

    public void setTv_description(TextView tv_description) {
        this.tv_description = tv_description;
    }

    public TextView getQiandao_des_tv() {
        return qiandao_des_tv;
    }

    public void setQiandao_des_tv(TextView qiandao_des_tv) {
        this.qiandao_des_tv = qiandao_des_tv;
    }

    private void initView() {
//        Window window = getWindow(); //得到对话框
//        window.setWindowAnimations(R.style.BottomDialogStyle); //设置窗口弹出动画

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_qiandao_reward, null);
        dialogView.setVisibility(View.INVISIBLE);

        iv_content = dialogView.findViewById(R.id.iv_content);
        ly_ad = dialogView.findViewById(R.id.ly_ad);

        tv_description=dialogView.findViewById(R.id.tv_description);
        qiandao_des_tv=dialogView.findViewById(R.id.qiandao_des_tv);
        //字体设置
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"DINMedium.ttf");
        tv_description.setTypeface(typeface);

        tv_know_it=dialogView.findViewById(R.id.tv_know_it);
        tv_more = dialogView.findViewById(R.id.tv_more);
        tv_second = dialogView.findViewById(R.id.tv_second);

        tv_more.setOnClickListener(
                v -> {
                    ActivityTaskCenter.startActivity(mContext,0);
                }
        );

        tv_know_it.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(null != myCountDownTimerForAD)
                    myCountDownTimerForAD.cancel();

                if(null != nativeExpressADView){
                    nativeExpressADView.destroy();
                }
                dialogView.setVisibility(View.INVISIBLE);
            }
        });

    }

    /*广点通腾讯广告联盟区域*/
    private NativeExpressAD mADManager;

    /**
     * 如果选择支持视频的模版样式，请使用
     * {Constants#NativeExpressSupportVideoPosID}
     */
    private void initNativeExpressAD() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(null != nativeExpressADView){
                    nativeExpressADView.destroy();
                }
                ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
                mADManager = new NativeExpressAD(mContext, adSize, Constant.TENCENTAPPID, Constant.TENCENTPOSTID3, new NativeExpressAD.NativeExpressADListener() {
                    @Override
                    public void onADLoaded(List<NativeExpressADView> list) {
                        try{
                            for (int i = 0; i < list.size(); i++) {
                                nativeExpressADView = list.get(i);
                                //放置广告
                                if (ly_ad.getChildCount() > 0) {
                                    ly_ad.removeAllViews();
                                }
                                ly_ad.addView(nativeExpressADView);
                                nativeExpressADView.render();
                            }
                        }catch (Exception  e){

                        }
                    }

                    @Override
                    public void onRenderFail(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADExposure(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADClicked(NativeExpressADView nativeExpressADView) {
                        saveStartAdvertisementStatistics("2");
                    }

                    @Override
                    public void onADClosed(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onNoAD(AdError adError) {

                    }
                });
                mADManager.loadAD(1);
            }
        }, 10);

    }


    @Override
    public void show() {
        super.show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    startShakeByViewAnim(dialogView);
                } catch (Exception e) {
                }

            }
        }, mContext.getResources().getInteger(R.integer.refresh_delay));
    }

    //掉落动画效果
    private void startShakeByViewAnim(View view) {
        if (view == null) {
            return;
        }
        //从上到下
        float h = view.getMeasuredHeight() * 2;
        Animation transAnim1 = new TranslateAnimation(0,0,-h,240);
        transAnim1.setDuration(450);
        transAnim1.setRepeatMode(Animation.REVERSE);
        transAnim1.setRepeatCount(0);
        Animation scaleAnim1 = new RotateAnimation(348f,366f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnim1.setDuration(450);
        scaleAnim1.setRepeatMode(Animation.REVERSE);
        scaleAnim1.setRepeatCount(0);


        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(transAnim1);
        smallAnimationSet.addAnimation(scaleAnim1);
        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                dialogView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation transAnim2 = new TranslateAnimation(0,0,240,-100);
                transAnim2.setDuration(200);
                transAnim2.setRepeatMode(Animation.REVERSE);
                transAnim2.setRepeatCount(0);
                Animation scaleAnim2 = new RotateAnimation(366f,359f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnim2.setDuration(200);
                scaleAnim2.setRepeatMode(Animation.REVERSE);
                scaleAnim2.setRepeatCount(0);

                AnimationSet smallAnimationSet = new AnimationSet(false);
                smallAnimationSet.addAnimation(transAnim2);
                smallAnimationSet.addAnimation(scaleAnim2);
                smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation transAnim3 = new TranslateAnimation(0,0,-100,0);
                        transAnim3.setDuration(100);
                        transAnim3.setRepeatMode(Animation.REVERSE);
                        transAnim3.setRepeatCount(0);
                        Animation scaleAnim3 = new RotateAnimation(359f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        scaleAnim3.setDuration(100);
                        scaleAnim3.setRepeatMode(Animation.REVERSE);
                        scaleAnim3.setRepeatCount(0);

                        AnimationSet smallAnimationSet = new AnimationSet(false);
                        smallAnimationSet.addAnimation(transAnim3);
                        smallAnimationSet.addAnimation(scaleAnim3);
                        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        view.startAnimation(smallAnimationSet);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(smallAnimationSet);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(smallAnimationSet);

    }

    /**
     * 请求统计接口
     * 类型 1浏览 2点击
     */
    private void saveStartAdvertisementStatistics(String type) {
        HashMap map = new HashMap();
        map.put("configId", String.valueOf(bannerBean.getId()));
        map.put("type", type);
        map.put("num", "1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/banner/saveStatistics", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }


    public void setBannerBean(BannerBean.DataBean bannerBean) {
        if(null == bannerBean){
            //无广告情况
            ly_ad.setVisibility(View.INVISIBLE);
            tv_second.setVisibility(View.GONE);
            tv_know_it.setVisibility(View.VISIBLE);
            tv_more.setVisibility(View.VISIBLE);
            return;
        }

        this.bannerBean = bannerBean;

        if(bannerBean.getType() == 7){
            //sdk广告
            ly_ad.setVisibility(View.VISIBLE);
            if("tenxun".equals(bannerBean.getSdkChannel())){
                initNativeExpressAD();
                saveStartAdvertisementStatistics("1");
            }
        }else {
            //图片广告
            ly_ad.setVisibility(View.INVISIBLE);
            RxImageTool.loadAwardImage(mContext,bannerBean.getBannerPhoto(),iv_content);
            iv_content.setOnClickListener(
                    v -> {
                        saveStartAdvertisementStatistics("2");
                        RxAppTool.adSkip(bannerBean,mContext);
                    }
            );
        }

        //倒计时
        if(bannerBean.getCountdown() > 0 ){
            //有倒计时的情况
            tv_second.setVisibility(View.VISIBLE);
            tv_know_it.setVisibility(View.GONE);
            tv_more.setVisibility(View.GONE);

            if(null != myCountDownTimerForAD)
                myCountDownTimerForAD.cancel();
            myCountDownTimerForAD=new MyCountDownTimerForAD2(bannerBean.getCountdown() * 1000,1000l,mContext,tv_second,"倒计时");
            myCountDownTimerForAD.setCall(new MyCountDownTimerForAD2.finishCall() {
                @Override
                public void onFinish() {
                    tv_second.setVisibility(View.GONE);
                    tv_know_it.setVisibility(View.VISIBLE);
                    tv_more.setVisibility(View.VISIBLE);
                }
            });
            myCountDownTimerForAD.start();

        }else {
            tv_second.setVisibility(View.GONE);
            tv_know_it.setVisibility(View.VISIBLE);
            tv_more.setVisibility(View.VISIBLE);
        }




    }
}
