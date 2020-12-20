package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class SortItemAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    HashMap<Integer,Boolean> hashMap=new HashMap<>();

    Context context;
    public SortItemAdapter(@Nullable List<String> data, Context mContext) {
        super(R.layout.item_sort, data);
        this.context=mContext;
        initMaps();
    }

    public HashMap<Integer, Boolean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, Boolean> hashMap) {
        this.hashMap = hashMap;
    }

    public void initMaps(){
        for(int i=0;i<getData().size();i++){
            hashMap.put(i,false);
        }
    }
    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String str) {

        TextView tvSort=baseViewHolder.getView(R.id.tv_sort);
        tvSort.setText(str);
        if (hashMap.get(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())){
            tvSort.setTextColor(mContext.getResources().getColor(R.color.blue_color));
        }else{
            tvSort.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
        }

    }

}
