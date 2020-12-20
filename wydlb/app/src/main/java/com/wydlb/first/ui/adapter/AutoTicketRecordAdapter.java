package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.AutoDailyTicketStreamBean;
import com.wydlb.first.utils.RxTimeTool;

import java.util.List;

/**
 * 自动投票交易记录
 */
public class AutoTicketRecordAdapter extends BaseQuickAdapter<AutoDailyTicketStreamBean.DataBean.ListBean,BaseViewHolder> {

    Context context;
    public AutoTicketRecordAdapter(@Nullable List<AutoDailyTicketStreamBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_autoticket_record, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,AutoDailyTicketStreamBean.DataBean.ListBean bean) {

        TextView tv_description=baseViewHolder.getView(R.id.tv_description);
        TextView tv_amount=baseViewHolder.getView(R.id.tv_amount);
        TextView tv_date=baseViewHolder.getView(R.id.tv_date);
        TextView tv_status=baseViewHolder.getView(R.id.tv_status);

        tv_amount.setText("-" + String.valueOf(bean.getAmt()));
        tv_description.setText(bean.getExRemark());
        tv_date.setText(RxTimeTool.getDate(Long.parseLong(bean.getCreateTime())));
        //状态 2成功 3失败
        if(bean.getStatus() == 2){
            tv_status.setText("自动投票成功");
            tv_status.setTextColor(mContext.getResources().getColor(R.color.green_text_color));
        }else if(bean.getStatus() == 3) {
            tv_status.setText("自动投票失败");
            tv_status.setTextColor(mContext.getResources().getColor(R.color.pinktext_color));
        }else {
            tv_status.setText("处理中");
            tv_status.setTextColor(mContext.getResources().getColor(R.color.pinktext_color));
        }

    }

}
