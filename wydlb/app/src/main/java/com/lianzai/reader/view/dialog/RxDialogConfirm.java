package com.lianzai.reader.view.dialog;

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

import com.lianzai.reader.R;


public class RxDialogConfirm extends Dialog {
    TextView tv_title;
    TextView tv_sure;
    TextView tv_cancel;
    public RxDialogConfirm(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogConfirm(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogConfirm(Context context) {
        super(context);
        initView();
    }

    public RxDialogConfirm(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
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
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        tv_sure.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

    }
}
