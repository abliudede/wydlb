package com.lianzai.reader.view.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.lianzai.reader.view.animation.BottomBehaviorAnim;

/**
 * Created by lrz on 2018/1/18.
 */

public class BottomBehavior extends CommonBehavior {

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        if (isInit) {
            mCommonAnim = new BottomBehaviorAnim(child);
            isInit = false;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
}