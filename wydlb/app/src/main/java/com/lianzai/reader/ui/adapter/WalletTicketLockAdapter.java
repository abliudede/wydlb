package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.UserLockListResponse;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTimeTool;

import java.util.List;

/**
 * 阅券解锁
 */
public class WalletTicketLockAdapter extends BaseQuickAdapter<UserLockListResponse.DataBean.ListBean,BaseViewHolder> {


    Context context;
    public WalletTicketLockAdapter(@Nullable List<UserLockListResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.view_read_ticket_lock_detail, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UserLockListResponse.DataBean.ListBean bean) {
        ProgressBar progress_bar=baseViewHolder.getView(R.id.progress_bar);
        baseViewHolder.setText(R.id.tv_date, RxTimeTool.getDate(bean.getCreateTime())+" 申请");

        baseViewHolder.setText(R.id.tv_lock_amount, "已解锁 "+ RxDataTool.format2Decimals(String.valueOf(bean.getUnLockAmt())));

        baseViewHolder.setText(R.id.tv_total_amount, "总量 "+RxDataTool.format2Decimals(String.valueOf(bean.getInitAmt())));

        baseViewHolder.setText(R.id.tv_percent, RxDataTool.getPercentValue(bean.getUnLockAmt()/bean.getInitAmt(),3)+"%");

        progress_bar.setMax(10000);
        double progress=Double.parseDouble(RxDataTool.getPercentValue(bean.getUnLockAmt()/bean.getInitAmt(),3))*100;

        progress_bar.setProgress((int)(progress));
        RxLogTool.e("progress_bar max:"+progress_bar.getMax());
        RxLogTool.e("progress_bar progress:"+progress_bar.getProgress());

    }

}
