package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.view.RxToast;


public class RxDialogPaySuccess extends RxDialog {


    private TextView tv_update_content;
    private TextView tv_sure;

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_update_content() {
        return tv_update_content;
    }

    public void setTv_update_content(TextView tv_update_content) {
        this.tv_update_content = tv_update_content;
    }

    public RxDialogPaySuccess(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogPaySuccess(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogPaySuccess(Context context) {
        super(context);
        initView();
    }

    public RxDialogPaySuccess(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogPaySuccess(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
//        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pay_success, null);
        tv_update_content = dialogView.findViewById(R.id.tv_update_content);
        tv_sure = dialogView.findViewById(R.id.tv_sure);

        setContentView(dialogView);
    }


}
