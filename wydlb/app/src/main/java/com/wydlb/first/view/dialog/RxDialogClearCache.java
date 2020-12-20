package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogClearCache extends Dialog {
    TextView tv_sure1;
    TextView tv_sure2;
    TextView tv_cancel;
    public RxDialogClearCache(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogClearCache(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogClearCache(Context context) {
        super(context);
        initView();
    }

    public RxDialogClearCache(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_sure1() {
        return tv_sure1;
    }

    public void setTv_sure1(TextView tv_sure1) {
        this.tv_sure1 = tv_sure1;
    }

    public TextView getTv_sure2() {
        return tv_sure2;
    }

    public void setTv_sure2(TextView tv_sure2) {
        this.tv_sure2 = tv_sure2;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_clear_cache, null);
        tv_sure1 = (TextView) dialogView.findViewById(R.id.tv_sure1);
        tv_sure2 = (TextView) dialogView.findViewById(R.id.tv_sure2);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
