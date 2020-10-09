package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.WalletConditionsBean;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * 查询条件过滤
 */
public class WalletConditionsAdapter extends BaseQuickAdapter<WalletConditionsBean.DataBean,BaseViewHolder> {


    int selectionPos=-1;

    int imageWidth=0;

    Context context;
    public WalletConditionsAdapter(@Nullable List<WalletConditionsBean.DataBean> data, Context mContext) {
        super(R.layout.item_wallet_conditions, data);
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
    protected void convert(final BaseViewHolder baseViewHolder,WalletConditionsBean.DataBean dataBean) {
        TextView tv_condition=baseViewHolder.getView(R.id.tv_condition);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(imageWidth,LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_condition.setLayoutParams(layoutParams1);
        tv_condition.setText(dataBean.getName());

        if (getSelectionPos()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            tv_condition.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.v2_recharge_coin_check_bg));
            tv_condition.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            tv_condition.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.v2_condition_not_check_bg));
            tv_condition.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
        }
    }

}
