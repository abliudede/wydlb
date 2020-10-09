package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.WalletListSumResponse;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class DateSelectAdapter extends BaseQuickAdapter<WalletListSumResponse.DataBean,BaseViewHolder> {


    Context context;
    public DateSelectAdapter(@Nullable List<WalletListSumResponse.DataBean> data, Context mContext) {
        super(R.layout.item_date_select, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,WalletListSumResponse.DataBean bean) {
        try{
            baseViewHolder.setText(R.id.tv_date_picker,bean.getYearMonth().substring(0,4)+"年"+bean.getYearMonth().substring(4)+"月");
        }catch (Exception e){

        }
    }

}
