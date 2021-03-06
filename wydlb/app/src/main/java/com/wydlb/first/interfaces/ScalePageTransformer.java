package com.wydlb.first.interfaces;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * Created by lrz on 2018/1/3.
 */

public class ScalePageTransformer implements ViewPager.PageTransformer{
    public static final float MAX_SCALE =0.95f;
    public static final float MIN_SCALE = 0.7f;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
    }
}