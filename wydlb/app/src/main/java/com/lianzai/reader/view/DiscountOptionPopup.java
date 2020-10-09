package com.lianzai.reader.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.OptionItemAdapter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;

import java.util.List;

/**
 * Created by lrz on 2018/3/19.
 */

public class DiscountOptionPopup extends PopupWindow implements View.OnClickListener {


    Activity mContext;
    View contentView;

    List<Integer> mDatas;
    RecyclerView recyclerView;
    private OptionItemAdapter adapter;
    private int choosestr;
    private TextView tv_cancel;

    public DiscountOptionPopup(Activity context,List<Integer> mdatas,int str) {
        super(context);
        mContext=context;
        mDatas = mdatas;
        choosestr = str;
        initView();
    }

    private void initView(){
        contentView=mContext.getLayoutInflater().inflate(R.layout.view_discount_options,null);

        this.setOutsideTouchable(true);
        //必须设置才能无背景
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.setFocusable(true);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        recyclerView = (RecyclerView) contentView.findViewById(R.id.list_option);
        tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        adapter =new OptionItemAdapter(mDatas,mContext,choosestr);

        RxLinearLayoutManager manager = new RxLinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        tv_cancel.setOnClickListener(
                v -> {
                    dismiss();
                }
        );
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    public void showPopupWindow(View view){
        this.setAnimationStyle(R.style.Dialog_Bottom_To_Up_Anim);
        this.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//            this.showAsDropDown(view,0,RxImageTool.dip2px(10));
//        }
    }


    public int getLastVisibleHeight(LinearLayoutManager manager) {
        int firstVisibleItem = manager.findFirstVisibleItemPosition();
        int lastVisibleItem = manager.findLastVisibleItemPosition();
        int lastHeight = 0;
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            RxLogTool.e("getLastVisibleHeight--pos:"+(i - firstVisibleItem));
            View view = manager.getChildAt(i - firstVisibleItem);
            if (null == view) {
                continue;
            }
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            Rect localRect = new Rect();
            view.getLocalVisibleRect(localRect);

            lastHeight = localRect.bottom - localRect.top;
            RxLogTool.e("getLastVisibleHeight lastHeight:"+lastHeight);
        }

        return lastHeight;
    }

    public void showDelOption(boolean show){

    }

    public void setChoosestr(int name) {
        choosestr = name;
        adapter.setChoosestr(choosestr);
    }
}
