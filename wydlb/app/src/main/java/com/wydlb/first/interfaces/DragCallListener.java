package com.wydlb.first.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface DragCallListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos,boolean isDelete);

    void onItemDragStatus(boolean isDelete);

}
