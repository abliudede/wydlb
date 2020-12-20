package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.model.bean.BannerBean;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 发现页item
 */

public class NewFindAdapter extends BaseQuickAdapter<BannerBean.DataBean, BaseViewHolder> {

    int imageWidth;
    Context context;

    public NewFindAdapter(@Nullable List<BannerBean.DataBean> data, Context mContext) {
        super(R.layout.item_find, data);
        this.context = mContext;
        imageWidth= RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(32);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BannerBean.DataBean bannerBean) {
        SelectableRoundedImageView iv_cover = baseViewHolder.getView(R.id.iv_cover);

        ViewGroup.LayoutParams params = iv_cover.getLayoutParams();
        params.height = imageWidth/3;
        iv_cover.setLayoutParams(params);

        RxImageTool.loadLogoImage(context,bannerBean.getBannerPhoto(),iv_cover);

//        setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });

    }


}
