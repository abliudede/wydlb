package com.lianzai.reader.ui.adapter;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.ReportDetailBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 举报详情列表
 */

public class ReportDetailItemAdapter extends BaseQuickAdapter<ReportDetailBean.DataBean.ListBean,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public ReportDetailItemAdapter(@Nullable List<ReportDetailBean.DataBean.ListBean> data) {
        super(R.layout.item_report_detail, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ReportDetailBean.DataBean.ListBean bean) {
        CircleImageView iv_logo= baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname= baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_expandable_text=baseViewHolder.getView(R.id.tv_expandable_text);

        iv_logo.setOnClickListener(
                v -> {
                    getContentClickListener().headNickClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );
        tv_nickname.setOnClickListener(
                v -> {
                    getContentClickListener().headNickClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );


        RxImageTool.loadLogoImage(mContext,bean.getAvatar(),iv_logo);
        tv_nickname.setText(bean.getNickName());
        tv_expandable_text.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getCreateTime())) + "举报了这条动态");

    }


    public interface ContentClickListener{
        void headNickClick(View v, int pos);
    }
}
