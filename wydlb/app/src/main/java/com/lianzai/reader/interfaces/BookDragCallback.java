package com.lianzai.reader.interfaces;

import android.graphics.Canvas;
import android.os.Build;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxLogTool;

public class BookDragCallback extends ItemTouchHelper.Callback {
    private static final String TAG = BookDragCallback.class.getSimpleName();


    DragCallListener onItemDragListener;


    private BaseItemDraggableAdapter mAdapter;

    private float mMoveThreshold = 0.1f;
    private float mSwipeThreshold = 0.7f;

    private int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

    public BookDragCallback(BaseItemDraggableAdapter adapter,DragCallListener onItemDragListener) {
        this.mAdapter=adapter;
        this.onItemDragListener=onItemDragListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            onItemDragListener.onItemDragStart(viewHolder,viewHolder.getAdapterPosition());
            viewHolder.itemView.setScaleX(1.05f);
            viewHolder.itemView.setScaleY(1.05f);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                viewHolder.itemView.setBackground(BuglyApplicationLike.getContext().getDrawable(R.drawable.grid_shadow));
//            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RxLogTool.e("clearView......isDelete:"+isDelete+"--viewHolder getAdapterPosition:"+viewHolder.getAdapterPosition());
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }

        if (viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support)) {
            if (null!=onItemDragListener){
                onItemDragListener.onItemDragEnd(viewHolder,viewHolder.getAdapterPosition(),isDelete);
            }
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, false);
            viewHolder.itemView.setScaleX(1f);
            viewHolder.itemView.setScaleY(1f);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.itemView.setBackground(null);
            }

            isDelete=false;
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    //    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        boolean flag=isViewCreateByAdapter(viewHolder);
//        if (flag) {
//            return makeMovementFlags(0, 0);
//        }
//        RxLogTool.e("flag:"+flag);
//        return makeMovementFlags(mDragMoveFlags, 0);
//    }

    boolean isDelete=false;

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        RxLogTool.e("onMove.....");

        if (null!=onItemDragListener){
            onItemDragListener.onItemDragMoving(source,source.getAdapterPosition(),target,target.getAdapterPosition());
        }
        mAdapter.onItemDragMoving(source, target);
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);

        RxLogTool.e("onMoved????????????");
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        onItemDragListener.onItemDragStatus(isDelete);
//        if (isDelete){
//            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
        RxLogTool.e("onChildDrawOver isDelete:"+isDelete);

    }

    private boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        RxLogTool.e("isViewCreateByAdapter:"+type);
        return type == BaseQuickAdapter.LOADING_VIEW
                || type == BaseQuickAdapter.FOOTER_VIEW || type == BaseQuickAdapter.EMPTY_VIEW;
    }
}
