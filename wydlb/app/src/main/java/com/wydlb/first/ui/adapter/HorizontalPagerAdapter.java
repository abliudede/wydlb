package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wydlb.first.R;
import com.wydlb.first.model.bean.BannerBean;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrz on 2018/1/9.
 */

public class HorizontalPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<BannerBean.DataBean>banner=new ArrayList<>();

    int width=0;
    public HorizontalPagerAdapter(final Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        //19:9
        width= RxDeviceTool.getScreenWidth(context)-RxImageTool.dip2px(20);

    }

    public List<BannerBean.DataBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean.DataBean> banner) {
        this.banner = banner;
    }

    @Override
    public int getCount() {
        int size = getBanner().size();
        if(size > 0) {
            return size;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.banner_item, container, false);
        ImageView iv=(ImageView)view.findViewById(R.id.banner_image);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        int size = getBanner().size();
        BannerBean.DataBean bannerBean;
            if (position >= size) {
                bannerBean = getBanner().get(position % size);
            } else {
                bannerBean = getBanner().get(position);
            }

        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(width,width/2);
        view.setLayoutParams(layoutParams);
        RxImageTool.loadAwardImage(mContext,bannerBean.getBannerPhoto(),iv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxAppTool.adSkip(bannerBean,mContext);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
