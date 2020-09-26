package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.GoldCoinBean;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * 钱包金币充值
 */
public class WalletCoinRechargeAdapter extends BaseQuickAdapter<GoldCoinBean,BaseViewHolder> {


    int selectionPos=-1;

    int imageWidth=0;

    Context context;
    public WalletCoinRechargeAdapter(@Nullable List<GoldCoinBean> data, Context mContext) {
        super(R.layout.item_recharge_coin, data);
        this.context=mContext;
        imageWidth= (RxDeviceTool.getScreenWidth(context)- RxImageTool.dp2px(40))/3;
    }

    public int getSelectionPos() {
        return selectionPos;
    }

    public void setSelectionPos(int selectionPos) {
        this.selectionPos = selectionPos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,GoldCoinBean bean) {
        LinearLayout ll_view=baseViewHolder.getView(R.id.ll_view);
        TextView tv_coin_price=baseViewHolder.getView(R.id.tv_coin_price);
        TextView tv_coin_amount=baseViewHolder.getView(R.id.tv_coin_amount);

        if (getSelectionPos()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            ll_view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.v2_recharge_coin_check_bg));
            tv_coin_price.setTextColor(mContext.getResources().getColor(R.color.white));
            tv_coin_amount.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            ll_view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.v2_recharge_coin_not_check_bg));
            tv_coin_price.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            tv_coin_amount.setTextColor(mContext.getResources().getColor(R.color.v2_black_color));
        }
        tv_coin_price.setText(bean.getPrice()+"元");
        tv_coin_amount.setText(bean.getAmount()+"金币");

        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(imageWidth,2*imageWidth/3);
        ll_view.setLayoutParams(layoutParams1);


    }

}
