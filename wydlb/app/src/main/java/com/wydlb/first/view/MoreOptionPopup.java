package com.wydlb.first.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;

/**
 * Created by lrz on 2018/3/19.
 */

public class MoreOptionPopup extends PopupWindow implements View.OnClickListener {

    TextView tv_report;
    TextView tv_delete;
    TextView tv_mute;


    Activity mContext;

    View contentView;

    public MoreOptionPopup(Activity context) {
        super(context);
        mContext=context;
        initView();
    }

    private void initView(){
        contentView=mContext.getLayoutInflater().inflate(R.layout.view_more_options,null);
        tv_report=(TextView)contentView.findViewById(R.id.tv_report);
        tv_delete=(TextView)contentView.findViewById(R.id.tv_delete);
        tv_mute=(TextView)contentView.findViewById(R.id.tv_mute);

        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.setFocusable(true);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public TextView getTv_report() {
        return tv_report;
    }

    public TextView getTv_delete() {
        return tv_delete;
    }

    public TextView getTv_mute() {
        return tv_mute;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    public void showPopupWindow(Context context,  View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int screenHeight = RxDeviceTool.getScreenHeights(context);
        int bottom = screenHeight - location[1];

            if (bottom > RxImageTool.dip2px(180)){//点击区域显示的区域大于弹框内容高度 ，默认在下面弹出，否则在上面弹出
                this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
                this.showAsDropDown(view,0,-RxImageTool.dip2px(10));
            }else{
                this.setAnimationStyle(R.style.More_Options_Anim_Direction_Top);
                this.showAsDropDown(view,0,-RxImageTool.dip2px(190));
            }
    }

    public void showPopupWindow(View view){
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.showAsDropDown(view,0,-RxImageTool.dip2px(10));

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
        if (show){
            tv_delete.setVisibility(View.VISIBLE);
            tv_report.setVisibility(View.GONE);
        }else{
            tv_delete.setVisibility(View.GONE);
            tv_report.setVisibility(View.VISIBLE);
        }

    }
}
