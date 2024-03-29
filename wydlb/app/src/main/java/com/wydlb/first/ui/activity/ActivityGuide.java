package com.wydlb.first.ui.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.ui.SplashActivity;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lrz on 2018/11/5
 * 引导广告页
 */

public class ActivityGuide extends BaseActivity {


    @Bind(R.id.viewpager_images)
    ViewPager viewpager_images;

    @Bind(R.id.ll_dots)
    LinearLayout ll_dots;//底部光标索引

    @Bind(R.id.enter_tv)
    ImageView enter_tv;



    private List<ImageView> mImageList;
    private ViewPagerAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
    }

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context, ActivityGuide.class);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        初始化页面数组
        mImageList = new ArrayList<ImageView>();

        //添加引导图页面
            ImageView x1 = new ImageView(ActivityGuide.this);
            x1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            x1.setLayoutParams(new ViewPager.LayoutParams());
            x1.setImageResource(R.mipmap.guide1);
            mImageList.add(x1);

        //添加引导图页面
        ImageView x2 = new ImageView(ActivityGuide.this);
        x2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        x2.setLayoutParams(new ViewPager.LayoutParams());
        x2.setImageResource(R.mipmap.guide2);
        mImageList.add(x2);

        //添加引导图页面
        ImageView x3 = new ImageView(ActivityGuide.this);
        x3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        x3.setLayoutParams(new ViewPager.LayoutParams());
        x3.setImageResource(R.mipmap.guide3);
        mImageList.add(x3);


        mAdapter=new ViewPagerAdapter(mImageList);
        viewpager_images.setAdapter(mAdapter);
        //初始指示器位置
        viewpager_images.setCurrentItem(0);
        initDots(0);
        //页面滑动监听
        viewpager_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                initDots(position);
                if(position == mImageList.size() - 1){
                    enter_tv.setVisibility(View.VISIBLE);
                }else {
                    enter_tv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        enter_tv.setOnClickListener(
                v -> {
                    enterMainActivity();
                }
        );

    }

    public class ViewPagerAdapter extends PagerAdapter {

        List<ImageView> mImageViews;

        public ViewPagerAdapter(List<ImageView> lists)
        {
            mImageViews = lists;
        }

        @Override
        public int getCount() {                                                                 //获得size
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public ImageView instantiateItem(ViewGroup view, int position)                                //实例化Item
        {

            // 预防负值
            if(position < mImageViews.size()) {
                ImageView imageView = mImageViews.get(position);
                view.addView(imageView);
                return imageView;
            }else {
                enterMainActivity();
                ImageView imageView = mImageViews.get(position%mImageViews.size());
                view.addView(imageView);
                return imageView;
            }

        }

    }

    //没登录不能进入主页
    private void enterMainActivity() {
        if(RxLoginTool.isLogin()){
            MainActivity.startActivity(ActivityGuide.this);
        }else {
            ActivityLoginNew.startActivity(ActivityGuide.this);
        }
        finish();
    }


    private void initDots(int currentPosition){
        if (mImageList.size() <= 1){
            return;
        }
        ll_dots.removeAllViews();
        int dotSize=RxImageTool.dip2px(7);
        int marginLeft=RxImageTool.dip2px(5);
        for(int i=0;i<mImageList.size();i++){
            if(i == currentPosition){
                SelectableRoundedImageView dotImgView=new SelectableRoundedImageView(this);
                dotImgView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                dotImgView.setOval(true);
                dotImgView.setBackgroundResource(R.drawable.circle_dot_focus);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(dotSize,dotSize);
                layoutParams.setMargins(marginLeft,0,0,0);
                dotImgView.setLayoutParams(layoutParams);
                ll_dots.addView(dotImgView);
            }else {
                SelectableRoundedImageView dotImgView = new SelectableRoundedImageView(this);
                dotImgView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                dotImgView.setOval(true);
                dotImgView.setBackgroundResource(R.drawable.circle_dot_normal);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
                layoutParams.setMargins(marginLeft, 0, 0, 0);
                dotImgView.setLayoutParams(layoutParams);
                ll_dots.addView(dotImgView);
            }
        }
        ll_dots.postInvalidate();
    }


    @Override
    public void gc() {
        RxToast.gc();
    }


}
