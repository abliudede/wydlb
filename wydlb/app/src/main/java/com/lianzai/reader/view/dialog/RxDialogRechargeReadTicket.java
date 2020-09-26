package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxDialogRechargeReadTicket extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;

    public RxDialogRechargeReadTicket(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogRechargeReadTicket(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogRechargeReadTicket(Context context) {
        super(context);
        initView();
    }

    public RxDialogRechargeReadTicket(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogRechargeReadTicket(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public void setButtonText(String confirm,String cancle){
        this.mTvCancel.setText(cancle);
        this.mTvSure.setText(confirm);
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
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recharge_read_ticket, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );
        mTvCancel.setOnClickListener(
                v->dismiss()
        );
    }
}
