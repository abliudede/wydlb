package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lianzai.reader.R;
import com.lianzai.reader.interfaces.ViewHolder;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;

/**
 * Created by lrz on 2018/1/2.
 */

public class BannerViewHolder implements ViewHolder<BannerBean.DataBean> {
    private ImageView mImageView;

    @Override
    public View createView(Context context, int position) {
        // 返回页面布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        //19:9
        int width=RxDeviceTool.getScreenWidth(context)-RxImageTool.dip2px(10);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(width,9*width/19);
        mImageView.setLayoutParams(layoutParams);
        return view;
    }

    /**
     * @param context  context
     * @param data     实体类对象
     * @param position 当前位置
     * @param size     页面个数
     */
    @Override
    public void onBind(Context context, BannerBean.DataBean data, int position, int size) {
        // 数据绑定
        RxLogTool.e("BannerViewHolder url:"+data.getBannerPhoto());
        RxImageTool.loadNormalImage(context,data.getBannerPhoto(),mImageView);
    }
}
