package com.lianzai.reader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 一键赏页面
 */

public class ActivityOneKeyReward extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.gold_coin_foraction)
    ImageView gold_coin_foraction;
    private int JOIN_CODE = 100;
    //打赏金额
    private String amount;
    private Boolean donotShow = true;


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_one_key_reward;
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
        //沉浸式状态栏
        SystemBarUtils.fullScreen(this);
//        SystemBarUtils.expandStatusBar(this);
//        SystemBarUtils.transparentStatusBar(this);
//        // 4.4及以上版本开启
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
//            setTranslucentStatus(true);
//        }

        //开始上下晃动
        startShakeByViewAnim(gold_coin_foraction,0.97f,1.0f,40,4000);
        //第一次启动不重新出现金币
        donotShow = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(donotShow){
            donotShow = false;
        }else{
            startShowViewAnim(gold_coin_foraction,120,400);
        }

    }


    //上下晃动动画效果
    private void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从上到下
        Animation rotateAnim = new TranslateAnimation(0, 0, -shakeDegrees,shakeDegrees);

        rotateAnim.setDuration(duration/4);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        scaleAnim.setDuration(duration/4);
        scaleAnim.setRepeatMode(Animation.REVERSE);
        scaleAnim.setRepeatCount(Animation.INFINITE);
        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);
        view.startAnimation(smallAnimationSet);
    }

    //消失动画
    private void startDissMissViewAnim(View view,float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //由大变小
        Animation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,0.5f*view.getMeasuredWidth(),0.5f*view.getMeasuredHeight());
        //从上到下
        Animation rotateAnim = new TranslateAnimation(0, 0, 0,shakeDegrees);

        rotateAnim.setDuration(duration);
        rotateAnim.setRepeatCount(0);
        scaleAnim.setDuration(duration);
        scaleAnim.setRepeatCount(0);
        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);
        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(smallAnimationSet);
    }

    //重新出现动画
    private void startShowViewAnim(View view, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //由大变小
        Animation scaleAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,0.5f*view.getMeasuredWidth(),0.5f*view.getMeasuredHeight());
        //从上到下
        Animation rotateAnim = new TranslateAnimation(0, 0, shakeDegrees,0);
        rotateAnim.setDuration(duration);
        rotateAnim.setRepeatCount(0);
        scaleAnim.setDuration(duration);
        scaleAnim.setRepeatCount(0);
        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);
        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                //开始上下晃动
                startShakeByViewAnim(gold_coin_foraction,0.97f,1.0f,40,4000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(smallAnimationSet);
    }



    @Override
    public void gc() {
        RxToast.gc();
    }



    @Override
    public void initToolBar() {

    }

    @Override
    protected void backClick() {
        super.backClick();
//        if (save_btn.getVisibility()==View.VISIBLE){//已经进行到了下一步，点了退出就弹框确认下，防止误操作
//            showInterruptExitDialog();
//        }else{
//            super.backClick();
//        }
    }



    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @OnClick(R.id.sure_btn)
    void sure_btnClick() {
        startDissMissViewAnim(gold_coin_foraction,120,400);
        //加入的点击事件
        RxActivityTool.skipActivityForResult(this, ActivityEnterAmount.class,JOIN_CODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_CODE && resultCode == RESULT_OK) {
            //一键赏
            amount = data.getStringExtra("amount");
            ActivityChoosePayWay.startActivity(this,amount,"2");

        }

    }

    //事件监听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.Close_oldPage) {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }


}
