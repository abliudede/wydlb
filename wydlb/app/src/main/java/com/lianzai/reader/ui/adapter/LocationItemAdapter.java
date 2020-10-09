package com.lianzai.reader.ui.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;

import java.util.List;


public class LocationItemAdapter extends BaseQuickAdapter<PoiItem,BaseViewHolder> {
    public LocationItemAdapter(@Nullable List<PoiItem> data) {
        super(R.layout.view_location_item, data);
    }
    int currentSelectPosition=0;


    public void setCurrentSelectPosition(int currentSelectPosition) {
        this.currentSelectPosition = currentSelectPosition;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, PoiItem item) {
        TextView tv_location1=holder.getView(R.id.tv_location1);
        TextView tv_location2=holder.getView(R.id.tv_location2);
        TextView tv_no_location=holder.getView(R.id.tv_no_location);

        if (holder.getAdapterPosition() - getHeaderLayoutCount()==0){
            tv_no_location.setVisibility(View.VISIBLE);

            tv_location1.setVisibility(View.GONE);
            tv_location2.setVisibility(View.GONE);
        }else{
            tv_no_location.setVisibility(View.GONE);

            tv_location1.setVisibility(View.VISIBLE);
            tv_location2.setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.tv_location1, !TextUtils.isEmpty(item.getSnippet())?item.getSnippet():item.getTitle());
        holder.setText(R.id.tv_location2,item.getCityName()+item.getAdName());

        ImageView iv_select_status=holder.getView(R.id.iv_select_status);
        if (currentSelectPosition==holder.getAdapterPosition() - getHeaderLayoutCount()){
            iv_select_status.setVisibility(View.VISIBLE);
            holder.getConvertView().setBackgroundColor(Color.parseColor("#193865FE"));
        }else{
            holder.getConvertView().setBackgroundColor(Color.parseColor("#FFFFFF"));
            iv_select_status.setVisibility(View.GONE);
        }
    }
}
