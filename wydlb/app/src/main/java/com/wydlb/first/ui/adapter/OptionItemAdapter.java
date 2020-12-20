package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;

import java.util.List;

/**
 * Created by lrz on 2018/11/7.
 */

public class OptionItemAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {


    Context context;
    private int choosestr;

    public OptionItemAdapter(@Nullable List<Integer> data, Context mContext,int str) {
        super(R.layout.item_sort_option, data);
        this.context=mContext;
        choosestr = str;
    }
    @Override
    protected void convert(final BaseViewHolder baseViewHolder,Integer str) {
        TextView tvSort=baseViewHolder.getView(R.id.tv_sort);
        tvSort.setText(String.valueOf(str) + "阅券");
        if(choosestr == str ){
            tvSort.setTextColor(0xff3865fe);
        }else{
            tvSort.setTextColor(0xff333333);
        }

    }

    public void setChoosestr(int str) {
        choosestr = str;
        notifyDataSetChanged();
    }
}
