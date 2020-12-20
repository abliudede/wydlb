package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.inner.MyCountDownTimerForAD2;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxImageTool;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 签到奖励，小图版本
 */
public class RxDialogQiandaoRewardMore extends RxDialog {

    private TextView tv_second;
    TextView tv_know_it;
    private View dialogView;

    private ImageView iv_content;
    private FrameLayout ly_ad;

    private LinearLayout ly_1;
    private TextView amount_tv1;
    private TextView tv_1;

    private LinearLayout ly_2;
    private TextView amount_tv2;
    private TextView tv_2;

    private LinearLayout ly_3;
    private TextView amount_tv3;
    private TextView tv_3;

    private NativeExpressADView nativeExpressADView;
    private BannerBean.DataBean bannerBean;
    private MyCountDownTimerForAD2 myCountDownTimerForAD;


    public RxDialogQiandaoRewardMore(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogQiandaoRewardMore(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogQiandaoRewardMore(Context context) {
        super(context);
        initView();
    }

    public RxDialogQiandaoRewardMore(Activity context) {
        super(context,R.style.NoneDialogStyle);
        initView();
    }

    public RxDialogQiandaoRewardMore(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public LinearLayout getLy_1() {
        return ly_1;
    }

    public void setLy_1(LinearLayout ly_1) {
        this.ly_1 = ly_1;
    }

    public TextView getAmount_tv1() {
        return amount_tv1;
    }

    public void setAmount_tv1(TextView amount_tv1) {
        this.amount_tv1 = amount_tv1;
    }

    public TextView getTv_1() {
        return tv_1;
    }

    public void setTv_1(TextView tv_1) {
        this.tv_1 = tv_1;
    }

    public LinearLayout getLy_2() {
        return ly_2;
    }

    public void setLy_2(LinearLayout ly_2) {
        this.ly_2 = ly_2;
    }

    public TextView getAmount_tv2() {
        return amount_tv2;
    }

    public void setAmount_tv2(TextView amount_tv2) {
        this.amount_tv2 = amount_tv2;
    }

    public TextView getTv_2() {
        return tv_2;
    }

    public void setTv_2(TextView tv_2) {
        this.tv_2 = tv_2;
    }

    public LinearLayout getLy_3() {
        return ly_3;
    }

    public void setLy_3(LinearLayout ly_3) {
        this.ly_3 = ly_3;
    }

    public TextView getAmount_tv3() {
        return amount_tv3;
    }

    public void setAmount_tv3(TextView amount_tv3) {
        this.amount_tv3 = amount_tv3;
    }

    public TextView getTv_3() {
        return tv_3;
    }

    public void setTv_3(TextView tv_3) {
        this.tv_3 = tv_3;
    }

    public TextView getTv_know_it() {
        return tv_know_it;
    }

    public void setTv_know_it(TextView tv_know_it) {
        this.tv_know_it = tv_know_it;
    }

    private void initView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

         dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_qiandao_reward_more, null);
        dialogView.setVisibility(View.INVISIBLE);
        tv_know_it = dialogView.findViewById(R.id.tv_know_it);
        tv_second = dialogView.findViewById(R.id.tv_second);



        tv_know_it.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        iv_content = dialogView.findViewById(R.id.iv_content);
        ly_ad = dialogView.findViewById(R.id.ly_ad);

        ly_1 = dialogView.findViewById(R.id.ly_1);
        amount_tv1 = dialogView.findViewById(R.id.amount_tv1);
        tv_1 = dialogView.findViewById(R.id.tv_1);

        ly_2 = dialogView.findViewById(R.id.ly_2);
        amount_tv2 = dialogView.findViewById(R.id.amount_tv2);
        tv_2 = dialogView.findViewById(R.id.tv_2);

        ly_3 = dialogView.findViewById(R.id.ly_3);
        amount_tv3 = dialogView.findViewById(R.id.amount_tv3);
        tv_3 = dialogView.findViewById(R.id.tv_3);

        //字体设置
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"DINMedium.ttf");
        amount_tv1.setTypeface(typeface);
        amount_tv2.setTypeface(typeface);
        amount_tv3.setTypeface(typeface);


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
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
        map.put("configId",String.valueOf(bannerBean.getId()));
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

            if(null != myCountDownTimerForAD)
                myCountDownTimerForAD.cancel();
            myCountDownTimerForAD=new MyCountDownTimerForAD2(bannerBean.getCountdown() * 1000,1000l,mContext,tv_second,"倒计时");
            myCountDownTimerForAD.setCall(new MyCountDownTimerForAD2.finishCall() {
                @Override
                public void onFinish() {
                    tv_second.setVisibility(View.GONE);
                    tv_know_it.setVisibility(View.VISIBLE);
                }
            });
            myCountDownTimerForAD.start();

        }else {
            tv_second.setVisibility(View.GONE);
            tv_know_it.setVisibility(View.VISIBLE);
        }
    }

}
