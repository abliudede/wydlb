package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogBindReceiptAccount extends Dialog  {
    TextView tv_sure;
    TextView tv_cancel;

    private String openId;
    private String userId;

    EditText ed_real_name;
    EditText ed_pay_password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public EditText getEd_real_name() {
        return ed_real_name;
    }

    public EditText getEd_pay_password() {
        return ed_pay_password;
    }

    public RxDialogBindReceiptAccount(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogBindReceiptAccount(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogBindReceiptAccount(Context context) {
        super(context);
        initView();
    }

    public RxDialogBindReceiptAccount(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bind_receipt_account, null);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        ed_real_name=(EditText)dialogView.findViewById(R.id.ed_real_name);
        ed_pay_password=(EditText)dialogView.findViewById(R.id.ed_pay_password);

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

    }


}
