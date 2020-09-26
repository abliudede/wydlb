package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

/**
 * 钱包交易记录
 */
public class WalletTransactionAdapter extends BaseQuickAdapter<WalletDetailListResponse.DataBean.ListBean,BaseViewHolder> {


    Drawable inComeDrawable;
    Drawable outComeDrawable;
    Context context;
    public WalletTransactionAdapter(@Nullable List<WalletDetailListResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_wallet_transaction, data);
        this.context=mContext;
        int size= RxImageTool.dp2px(15);
        inComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_in);
        inComeDrawable.setBounds(0,0, size,size);

        outComeDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.v2_icon_wallet_cash_out);
        outComeDrawable.setBounds(0,0, size,size);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,WalletDetailListResponse.DataBean.ListBean bean) {

        View view_content=baseViewHolder.getView(R.id.view_content);
        View rl_conditions=baseViewHolder.getView(R.id.rl_conditions);

        if (bean.isShowMonth()){
            view_content.setVisibility(View.GONE);
            rl_conditions.setVisibility(View.VISIBLE);

            TextView tv_date_picker=baseViewHolder.getView(R.id.tv_date_picker);
            TextView tv_list_description=baseViewHolder.getView(R.id.tv_list_description);

            tv_date_picker.setText(bean.getShowDate());
            tv_list_description.setText(bean.getDescription());

        }else{
            view_content.setVisibility(View.VISIBLE);
            rl_conditions.setVisibility(View.GONE);

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

}