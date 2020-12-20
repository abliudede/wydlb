package com.wydlb.first.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;

/**
 * Created by lrz on 2019/3/19.
 */

public class CommentOptionPopup extends PopupWindow implements View.OnClickListener {

    TextView tv_reply;
    TextView tv_copy;
    TextView tv_report;
    TextView tv_delete;
    TextView tv_mute;


    Activity mContext;

    View contentView;
    private LinearLayout popup_anima;

    public CommentOptionPopup(Activity context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        contentView = mContext.getLayoutInflater().inflate(R.layout.view_comment_options, null);
        popup_anima = (LinearLayout) contentView.findViewById(R.id.popup_anima);
        tv_reply = (TextView) contentView.findViewById(R.id.tv_reply);
        tv_copy = (TextView) contentView.findViewById(R.id.tv_copy);
        tv_report = (TextView) contentView.findViewById(R.id.tv_report);
        tv_delete = (TextView) contentView.findViewById(R.id.tv_delete);
        tv_mute = (TextView) contentView.findViewById(R.id.tv_mute);

        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.setFocusable(true);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public TextView getTv_report() {
        return tv_report;
    }

    public TextView getTv_delete() {
        return tv_delete;
    }

    public TextView getTv_reply() {
        return tv_reply;
    }

    public TextView getTv_copy() {
        return tv_copy;
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

    public void showPopupWindow(Context context, View view) {
        int screenWidth = RxDeviceTool.getScreenWidth(context);
        int contentWidth = contentView.getMeasuredWidth();
        int x = (screenWidth - contentWidth) / 2;
        int hight = view.getHeight();
        this.setAnimationStyle(R.style.Popup_Anim);
        this.showAsDropDown(view, x, RxImageTool.dip2px(25) - hight);
    }

    public void showPopupWindow(Context context, View view, boolean head) {
        int screenWidth = RxDeviceTool.getScreenWidth(context);
        int contentWidth = contentView.getMeasuredWidth();
        int contentHight = 198;
        int x = (screenWidth - contentWidth) / 2;
        int hight = view.getHeight();
        this.setAnimationStyle(R.style.Popup_Anim);
        this.showAsDropDown(view, x, - contentHight - hight);
    }

    public void showPopupWindow(View view) {
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.showAsDropDown(view, 0, -RxImageTool.dip2px(10));
    }

    public void showDelOption(boolean show) {
        if (show) {
            tv_delete.setVisibility(View.VISIBLE);
            tv_report.setVisibility(View.GONE);
        } else {
            tv_delete.setVisibility(View.GONE);
            tv_report.setVisibility(View.VISIBLE);
        }

    }
}
