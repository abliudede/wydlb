package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogSureCancel extends RxDialog {

    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;

    public RxDialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
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

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        mTvTitle=dialogView.findViewById(R.id.tv_title);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel =  dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );
    }
}
