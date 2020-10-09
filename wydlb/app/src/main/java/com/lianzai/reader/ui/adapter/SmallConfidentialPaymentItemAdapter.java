package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 小额免密支付额度
 */

public class SmallConfidentialPaymentItemAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    Context context;
    String chooseStr;

    public SmallConfidentialPaymentItemAdapter(@Nullable List<String> data, Context mContext) {
        super(R.layout.item_small_confidential_payment, data);
        this.context=mContext;
    }

    public String getChooseStr() {
        return chooseStr;
    }

    public void setChooseStr(String chooseStr) {
        this.chooseStr = chooseStr;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String str) {
        TextView tv_amount=baseViewHolder.getView(R.id.tv_amount);
        ImageView iv_select_status=baseViewHolder.getView(R.id.iv_select_status);
        if("0".equals(str)){
            tv_amount.setText("需要密码支付");
        }else {
            tv_amount.setText(str);
        }

        if(str.equals(chooseStr)){
            iv_select_status.setImageResource(R.mipmap.dialog_check_box_true);
        }else {
            iv_select_status.setImageResource(R.mipmap.dialog_check_box_false);
        }

    }

}
