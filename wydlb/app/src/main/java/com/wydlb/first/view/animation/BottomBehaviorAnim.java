package com.wydlb.first.view.animation;

import android.animation.ValueAnimator;
import android.view.View;

import com.wydlb.first.view.behavior.CommonAnim;

/**
 * Created by lrz on 2018/1/18.
 */

public class BottomBehaviorAnim extends CommonAnim {

    private View mBottomView;
    private float mOriginalY;

    public BottomBehaviorAnim(View bottomView) {
        mBottomView = bottomView;
        mOriginalY = mBottomView.getY();
    }

    @Override
    public void show() {
        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mOriginalY);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void hide() {
        ValueAnimator animator = ValueAnimator.ofFloat(mBottomView.getY(), mOriginalY + mBottomView.getHeight());
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }
}