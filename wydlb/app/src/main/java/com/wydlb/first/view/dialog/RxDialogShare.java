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


public class RxDialogShare extends Dialog {
    TextView tv_share_wx_circle;
    TextView tv_share_wx;
    TextView tv_share_copy;
    TextView tv_cancel;
    public RxDialogShare(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogShare(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogShare(Context context) {
        super(context);
        initView();
    }

    public RxDialogShare(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_share_wx_circle() {
        return tv_share_wx_circle;
    }

    public void setTv_share_wx_circle(TextView tv_share_wx_circle) {
        this.tv_share_wx_circle = tv_share_wx_circle;
    }

    public TextView getTv_share_wx() {
        return tv_share_wx;
    }

    public void setTv_share_wx(TextView tv_share_wx) {
        this.tv_share_wx = tv_share_wx;
    }

    public TextView getTv_share_copy() {
        return tv_share_copy;
    }

    public void setTv_share_copy(TextView tv_share_copy) {
        this.tv_share_copy = tv_share_copy;
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

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, null);
        tv_share_wx_circle = (TextView) dialogView.findViewById(R.id.tv_share_wx_circle);
        tv_share_wx = (TextView) dialogView.findViewById(R.id.tv_share_wx);
        tv_share_copy = (TextView) dialogView.findViewById(R.id.tv_share_copy);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        tv_share_copy.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }
}
