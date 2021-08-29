package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    Context context;

    public NewFindAdapter(@Nullable List<BannerBean.DataBean> data, Context mContext) {
        super(R.layout.item_find, data);
        this.context = mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BannerBean.DataBean bannerBean) {
        LinearLayout ly_content = baseViewHolder.getView(R.id.ly_content);
        if(baseViewHolder.getAdapterPosition()%2 == 0){
            ly_content.setBackgroundResource(R.color.white);
        }else {
            ly_content.setBackgroundResource(R.color.color_78ffdf80);
        }
        //项目描述
        TextView tv_name = baseViewHolder.getView(R.id.tv_name);
        TextView tv_fengxian_des = baseViewHolder.getView(R.id.tv_fengxian_des);
        TextView tv_renjun_des = baseViewHolder.getView(R.id.tv_renjun_des);
        TextView tv_edit = baseViewHolder.getView(R.id.tv_edit);


    }
}
