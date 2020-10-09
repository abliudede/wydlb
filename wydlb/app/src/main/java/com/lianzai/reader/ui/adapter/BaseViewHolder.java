package com.lianzai.reader.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.lianzai.reader.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-17.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder{
    public IViewHolder<T> holder;

    public BaseViewHolder(View itemView, IViewHolder<T> holder) {
        super(itemView);
        this.holder = holder;
        holder.initView();
    }
}
