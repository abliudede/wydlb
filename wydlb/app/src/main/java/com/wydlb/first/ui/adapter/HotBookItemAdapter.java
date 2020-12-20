package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.HotSearchBean;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class HotBookItemAdapter extends BaseQuickAdapter<HotSearchBean.DataBean,BaseViewHolder> {


    Context context;
    int imageWidth=0;
    public HotBookItemAdapter( @Nullable List<HotSearchBean.DataBean> data, Context mContext) {
        super(R.layout.item_hot_book, data);
        this.context=mContext;
        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(60))/4;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, HotSearchBean.DataBean bean) {
        SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getCover(),roundedImageView);
        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(imageWidth,3*imageWidth/2);
        roundedImageView.setLayoutParams(layoutParams);
        tvBookTitle.setText(bean.getTitle());

        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(imageWidth,LinearLayout.LayoutParams.WRAP_CONTENT);
        tvBookTitle.setLayoutParams(layoutParams1);
    }

}
