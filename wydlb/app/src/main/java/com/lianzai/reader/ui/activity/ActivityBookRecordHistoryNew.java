package com.lianzai.reader.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.ui.fragment.BookRecordFragment;
import com.lianzai.reader.ui.fragment.MyCommentFragment;
import com.lianzai.reader.ui.fragment.MyDynamicFragment;
import com.lianzai.reader.ui.fragment.WebRecordFragment;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Copyright (C), 2019
 * FileName: ActivityCircleDetail
 * Author: lrz
 * Date: 2019/3/5 15:21
 * Description: ${DESCRIPTION}
 */
public class ActivityBookRecordHistoryNew extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_circle_dynamic)
    TextView tv_circle_dynamic;

    @Bind(R.id.tv_circle_comment)
    TextView tv_circle_comment;

    @Bind(R.id.view_slide)
    View view_slide;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩



    int pageIndex = 0;

    TabFragmentAdapter tabFragmentAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    BookRecordFragment bookRecordFragment;
//    WebRecordFragment webRecordFragment;

    public static void startActivity(Context context) {
        RxActivityTool.skipActivity(context,ActivityBookRecordHistoryNew.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_dynamic;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        tv_commom_title.setText("阅读足迹");
        tv_circle_dynamic.setText("书籍");
        tv_circle_comment.setText("网页");
        tv_circle_comment.setVisibility(View.GONE);

        initTabViews();

        bookRecordFragment = new BookRecordFragment();
//        webRecordFragment = new WebRecordFragment();
        fragmentList.add(bookRecordFragment);
//        fragmentList.add(webRecordFragment);
        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        tabFragmentAdapter.setFragments(fragmentList);
        viewPager.setAdapter(tabFragmentAdapter);

    }

    public View getMask_view() {
        return mask_view;
    }

    public void setMask_view(View mask_view) {
        this.mask_view = mask_view;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    private void initTabViews() {

        tv_circle_dynamic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //此处回调之后，里面对象可能为空

                    tv_circle_dynamic.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(null == tv_circle_dynamic) return;
                                if(null == view_slide) return;
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tv_circle_dynamic.getMeasuredWidth(), RxImageTool.dip2px(2f));
                                layoutParams.setMargins(RxImageTool.dp2px(16), tv_circle_dynamic.getTop(), 0, 0);
                                view_slide.setLayoutParams(layoutParams);
                            }catch (Exception e){

                            }
                        }
                    });
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv_circle_dynamic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                } catch (Exception e) {

                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clickTabByIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_circle_dynamic.setOnClickListener(
                v -> {
                    clickTabByIndex(0);
                }
        );

        tv_circle_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTabByIndex(1);
            }
        });
    }

    /**
     * 导航栏切换
     *
     * @param index
     */
    private void clickTabByIndex(int index) {
        if (pageIndex == index) return;

        pageIndex = index;
        int translateX = 0;

        if (pageIndex == 0) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_comment.setTextColor(getResources().getColor(R.color.color_black_ff999999));
        } else if (pageIndex == 1) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_comment.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            translateX = tv_circle_dynamic.getMeasuredWidth() + RxImageTool.dip2px(24);
        }

        viewPager.setCurrentItem(pageIndex);

        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view_slide, "translationX", translateX);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateAnim);
        animatorSet.setDuration(300);
        animatorSet.start();
    }


    @OnClick(R.id.img_back)
    void circleBackClick() {
        finish();
    }

}