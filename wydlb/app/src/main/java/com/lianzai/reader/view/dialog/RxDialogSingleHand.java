package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxDialogSingleHand extends RxDialog {

    private TextView mTvSure;

    public RxDialogSingleHand(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSingleHand(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSingleHand(Context context) {
        super(context);
        initView();
    }

    public RxDialogSingleHand(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogSingleHand(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }





    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }


    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }


    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_single_hand, null);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        mTvSure.setOnClickListener(
                v->dismiss()
        );
    }
}
