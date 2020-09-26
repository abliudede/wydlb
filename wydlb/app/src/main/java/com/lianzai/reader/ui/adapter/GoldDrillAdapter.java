package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.GetGoldDrillBean;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

/**
 * 钱包交易记录
 */
public class GoldDrillAdapter extends BaseQuickAdapter<com.lianzai.reader.bean.UserDailyTicketStreamBean.DataBean.ListBean,BaseViewHolder> {


    Drawable inComeDrawable;
    Drawable outComeDrawable;
    Context context;
    public GoldDrillAdapter(@Nullable List<com.lianzai.reader.bean.UserDailyTicketStreamBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_wallet_transaction2, data);
        this.context=mContext;
        int size= RxImageTool.dp2px(15);
        inComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_in);
        inComeDrawable.setBounds(0,0, size,size);

        outComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_out);
        outComeDrawable.setBounds(0,0, size,size);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,com.lianzai.reader.bean.UserDailyTicketStreamBean.DataBean.ListBean bean) {

        View view_content=baseViewHolder.getView(R.id.view_content);
        TextView tv_description=baseViewHolder.getView(R.id.tv_description);
        TextView tv_amount=baseViewHolder.getView(R.id.tv_amount);
        TextView tv_date=baseViewHolder.getView(R.id.tv_date);
        TextView tv_status=baseViewHolder.getView(R.id.tv_status);


        view_content.setVisibility(View.VISIBLE);
                //0收入 1支出
            if(bean.getInOrOut() == 0){
                tv_amount.setText("+" + String.valueOf(bean.getAmt()));
                tv_description.setCompoundDrawables(inComeDrawable,null,null,null);
            }else {
                tv_amount.setText("-" + String.valueOf(bean.getAmt()));
                tv_description.setCompoundDrawables(outComeDrawable,null,null,null);
            }

            tv_description.setText(bean.getExRemark());
            tv_date.setText(RxTimeTool.getDate(Long.parseLong(bean.getCreateTime())));

//            if(bean.getStatus() == 2){
//                tv_status.setVisibility(View.GONE);
//            }else {
//                tv_status.setVisibility(View.VISIBLE);
//            }

    }

}
