package com.lianzai.reader.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.BarrageBean;
import com.lianzai.reader.utils.RxImageTool;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: BarrageChatRoomView
 * Author: lrz
 * Date: 2018/9/19 09:08
 * Description: ${DESCRIPTION}
 */
public class BarrageChatRoomView extends FrameLayout {
    LayoutInflater layoutInflater;

    Animation animation;
    Animation outAnimation;

    int width;    //控件宽
    int height;  //控件高

    List<BarrageBean> barrageBeanList = new ArrayList<>();


    public BarrageChatRoomView(Context context) {
        super(context);
        init();
    }

    public BarrageChatRoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarrageChatRoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public List<BarrageBean> getBarrageBeanList() {
        return barrageBeanList;
    }

    public void setBarrageBeanList(List<BarrageBean> barrageBeanList) {
        this.barrageBeanList = barrageBeanList;
    }

    private void init() {
        removeAllViews();

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.chat_room_out_top_scale_in);
        animation.setDuration(200);

        outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.chat_room_out_top_scale_out);
        outAnimation.setDuration(200);

        layoutInflater = LayoutInflater.from(getContext());


    }


    public void addAnimationView(BarrageBean barrageBean) {
    //每次移动的距离
        int step = ScreenUtil.dip2px(38);
        View childView = layoutInflater.inflate(R.layout.item_barrage_view, null);
        CircleImageView logoImg = childView.findViewById(R.id.iv_logo);
        TextView tv_barrage_text = childView.findViewById(R.id.tv_barrage_text);

        RxImageTool.loadLogoImage(getContext(), barrageBean.getLogoUrl(), logoImg);
        if (!TextUtils.isEmpty(barrageBean.getText())) {
//            tv_barrage_text.setText(barrageBean.getText());
            MoonUtil.identifyRecentVHFaceExpressionAndTags(getContext(), tv_barrage_text, barrageBean.getText(), -1, 0.65f,0);
        }
        childView.setY(height);//初始坐标Y值
        addView(childView);


        ObjectAnimator iniAnim = ObjectAnimator.ofFloat(childView, "translationY", childView.getY(),height -  step);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(childView, "scaleX", 0, 1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(childView, "scaleY", 0, 1f);

        AnimatorSet animatorSetAnim=new AnimatorSet();
        animatorSetAnim.play(iniAnim).with(scaleXAnim).with(scaleYAnim);

        animatorSetAnim.setDuration(400);
        animatorSetAnim.start();

        for(int i=0;i<getChildCount();i++){
            View view=getChildAt(i);

            if (view.getY()>0){//需要往上移动

                ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", view.getY(),view.getY()-step);
                ObjectAnimator alphaAnim=ObjectAnimator.ofFloat(view, "alpha", 1, 1f);

                if(view.getY()-2*step==0){
                    alphaAnim= ObjectAnimator.ofFloat(view, "alpha",  0.6f);
                }else if (view.getY()-step==0){
                    alphaAnim= ObjectAnimator.ofFloat(view, "alpha", 0.3f);
                }

                AnimatorSet animatorSet=new AnimatorSet();
                animatorSet.play(anim).with(alphaAnim);

                animatorSet.setDuration(500);
                animatorSet.start();

            }else{//隐藏-往右边消失
//                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1, 0f);
                ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view, "translationX", width);
                AnimatorSet animatorSet=new AnimatorSet();
                animatorSet.play(translateAnim);

                animatorSet.setDuration(200);
                animatorSet.start();
            }
        }


    }

    public int getViewCount() {
        return getChildCount();
    }


    public void removeFirstView(BarrageBean barrageBean) {
        View view = getChildAt(0);

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0.4f, 0f);
        alphaAnim.setDuration(500);
        alphaAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                addAnimationView(barrageBean);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alphaAnim.start();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth(); //宽度
        height = getHeight();   //高度
    }
}
