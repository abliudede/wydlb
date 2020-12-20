package com.wydlb.first.ui.adapter;

import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.wydlb.first.base.adapter.IViewHolder;
import com.wydlb.first.view.ReadBgHolder;



public class ReadBgAdapter extends BaseListAdapter<Drawable> {
    private int currentChecked;

    @Override
    protected IViewHolder<Drawable> createViewHolder(int viewType) {
        return new ReadBgHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IViewHolder iHolder = ((BaseViewHolder) holder).holder;
        ReadBgHolder readBgHolder = (ReadBgHolder) iHolder;
        if (currentChecked == position){
            readBgHolder.setChecked(currentChecked);
        }
    }

    public void setBgChecked(int pos){
        currentChecked = pos;

    }

    @Override
    protected void onItemClick(View v, int pos) {
        super.onItemClick(v, pos);
        currentChecked = pos;
        notifyDataSetChanged();
    }
}
