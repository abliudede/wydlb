package com.wydlb.first.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.WalletDetailListResponse;
import com.wydlb.first.utils.RxDataTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxTimeTool;
import com.wydlb.first.utils.RxTool;

import java.util.List;

/**
 * 钱包交易记录
 */
public class WalletTransactionByTypeAdapter extends BaseQuickAdapter<WalletDetailListResponse.DataBean.ListBean,BaseViewHolder> {


    Drawable inComeDrawable;
    Drawable outComeDrawable;
    Context context;
    public WalletTransactionByTypeAdapter(@Nullable List<WalletDetailListResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_wallet_record_transaction, data);
        this.context=mContext;
        int size= RxImageTool.dp2px(15);
        inComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_in);
        inComeDrawable.setBounds(0,0, size,size);

        outComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_out);
        outComeDrawable.setBounds(0,0, size,size);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,WalletDetailListResponse.DataBean.ListBean bean) {

        TextView tv_description=baseViewHolder.getView(R.id.tv_description);
        TextView tv_amount=baseViewHolder.getView(R.id.tv_amount);
        TextView tv_date=baseViewHolder.getView(R.id.tv_date);

        if (bean.getInOutType()==0){//收入
            tv_amount.setText("+"+ RxDataTool.format2Decimals(String.valueOf(bean.getAmt())));
            tv_description.setCompoundDrawables(inComeDrawable,null,null,null);
        }else if(bean.getInOutType()==1){//支出
            tv_amount.setText("-"+RxDataTool.format2Decimals(String.valueOf(bean.getAmt())));
            tv_description.setCompoundDrawables(outComeDrawable,null,null,null);
        }
        tv_description.setText(bean.getRemark());
        tv_date.setText(RxTimeTool.getDate(bean.getCreateTime()));
    }

}
