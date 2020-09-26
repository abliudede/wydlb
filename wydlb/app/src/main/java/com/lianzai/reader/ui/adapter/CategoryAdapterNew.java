package com.lianzai.reader.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.lianzai.reader.base.adapter.IViewHolder;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.ui.adapter.holder.CategoryHolder;
import com.lianzai.reader.ui.adapter.holder.CategoryHolderNew;
import com.lianzai.reader.utils.RxLogTool;

public class CategoryAdapterNew extends EasyAdapter<BookChapterBean> {
    private int currentSelected = 0;
    public boolean isSortDesc=true;
    @Override
    protected IViewHolder<BookChapterBean> onCreateViewHolder(int viewType) {
        return new CategoryHolderNew();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CategoryHolderNew holder = (CategoryHolderNew) view.getTag();

        if (isSortDesc()){
            if (position == currentSelected){
                holder.setSelectedChapter();
                RxLogTool.e("CategoryAdapter currentSelected:"+currentSelected+"--isSortDesc:"+isSortDesc);
            }
        }else{
            if (getCount()-position-1 == currentSelected){
                holder.setSelectedChapter();
                RxLogTool.e("CategoryAdapter currentSelected:"+currentSelected+"--isSortDesc:"+isSortDesc);
            }
        }
        return view;
    }

    public boolean isSortDesc() {
        return isSortDesc;
    }

    public void setSortDesc(boolean sortDesc) {
        isSortDesc = sortDesc;
    }

    public void setChapter(int pos){
        currentSelected = pos;
        notifyDataSetChanged();
    }

    public int getCurrentSelected() {
        return currentSelected;
    }

}
