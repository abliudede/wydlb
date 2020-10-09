package com.lianzai.reader.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;


/**
 * Created by lrz on 2018/3/19.
 */

public class PersonOptionPopup extends PopupWindow implements View.OnClickListener {

//    TextView tv_share;
    TextView tv_collect;//黑名单
    TextView tv_delete;//禁言
    TextView tv_cancel;//取消

    Activity mContext;

    View contentView;

    public PersonOptionPopup(Activity context) {
        super(context);
        mContext=context;
        initView();
    }

    private void initView(){
        contentView=mContext.getLayoutInflater().inflate(R.layout.view_person_options,null);
        tv_collect=(TextView)contentView.findViewById(R.id.tv_collect);

        tv_delete=(TextView)contentView.findViewById(R.id.tv_delete);

        tv_cancel=(TextView)contentView.findViewById(R.id.tv_cancel);


        this.setOutsideTouchable(true);
        //必须设置才能无背景
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.setFocusable(true);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

    }



    public TextView getTv_collect() {
        return tv_collect;
    }

    public TextView getTv_delete() {
        return tv_delete;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
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
        this.showAtLocation(view, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, RxImageTool.dip2px(10));
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
        }else{
            tv_delete.setVisibility(View.GONE);
        }

    }
}
