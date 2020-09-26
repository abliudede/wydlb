package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;

import butterknife.OnCheckedChanged;


public class RxDialogCheckBox extends RxDialog {

    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private CheckBox cb_nomore_tip;
    private TextView tv_checkbox;
    private LinearLayout checkbox_ly;


    public RxDialogCheckBox(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogCheckBox(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogCheckBox(Context context) {
        super(context);
        initView();
    }

    public RxDialogCheckBox(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogCheckBox(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }


    public void setTitle(String title) {
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(title);
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public void setButtonText(String confirm,String cancle){
        this.mTvCancel.setText(cancle);
        this.mTvSure.setText(confirm);
    }

    public TextView getTv_checkbox() {
        return tv_checkbox;
    }

    public LinearLayout getCheckbox_ly() {
        return checkbox_ly;
    }

    public void setCheckbox_ly(LinearLayout checkbox_ly) {
        this.checkbox_ly = checkbox_ly;
    }

    public void setTv_checkbox(TextView tv_checkbox) {
        this.tv_checkbox = tv_checkbox;
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public CheckBox getCb_nomore_tip() {
        return cb_nomore_tip;
    }

    public void setCb_nomore_tip(CheckBox cb_nomore_tip) {
        this.cb_nomore_tip = cb_nomore_tip;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_check_box, null);
        mTvTitle=dialogView.findViewById(R.id.tv_title);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel =  dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_des);
        cb_nomore_tip = dialogView.findViewById(R.id.cb_nomore_tip);
        tv_checkbox = dialogView.findViewById(R.id.tv_checkbox);
        checkbox_ly = dialogView.findViewById(R.id.checkbox_ly);



        mTvContent.setTextIsSelectable(true);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );
    }


    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

//    @Override
//    public void show() {
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        super.show();
//        fullScreenImmersive(getWindow().getDecorView());
//        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//    }
}
