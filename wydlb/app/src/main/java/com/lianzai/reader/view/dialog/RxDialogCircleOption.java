package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxDialogCircleOption extends Dialog {
    TextView tv_top;
    TextView tv_push;
    TextView tv_circle;
    TextView tv_enter_exit;
    private TextView tv_cancel;


    public RxDialogCircleOption(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogCircleOption(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogCircleOption(Context context) {
        super(context);
        initView();
    }

    public RxDialogCircleOption(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_enter_exit() {
        return tv_enter_exit;
    }

    public void setTv_enter_exit(TextView tv_enter_exit) {
        this.tv_enter_exit = tv_enter_exit;
    }

    public TextView getTv_circle() {
        return tv_circle;
    }

    public void setTv_circle(TextView tv_circle) {
        this.tv_circle = tv_circle;
    }

    public TextView getTv_top() {
        return tv_top;
    }

    public void setTv_top(TextView tv_top) {
        this.tv_top = tv_top;
    }

    public TextView getTv_push() {
        return tv_push;
    }

    public void setTv_push(TextView tv_push) {
        this.tv_push = tv_push;
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
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_circle_option, null);
        tv_top = (TextView) dialogView.findViewById(R.id.tv_top);
        tv_push = (TextView) dialogView.findViewById(R.id.tv_push);
        tv_circle = (TextView) dialogView.findViewById(R.id.tv_circle);
        tv_enter_exit = (TextView) dialogView.findViewById(R.id.tv_enter_exit);

        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(
                v->dismiss()
        );

        setCanceledOnTouchOutside(true);
        setContentView(dialogView);
    }
}
