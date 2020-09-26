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


public class RxDialogSelectImage extends Dialog {

    TextView tv_cancel;
    TextView tv_camera;
    TextView tv_file;
    public RxDialogSelectImage(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSelectImage(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSelectImage(Context context) {
        super(context);
        initView();
    }

    public RxDialogSelectImage(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    public TextView getTv_camera() {
        return tv_camera;
    }

    public void setTv_camera(TextView tv_camera) {
        this.tv_camera = tv_camera;
    }

    public TextView getTv_file() {
        return tv_file;
    }

    public void setTv_file(TextView tv_file) {
        this.tv_file = tv_file;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_image, null);
        tv_cancel=dialogView.findViewById(R.id.tv_cancel);
        tv_camera=dialogView.findViewById(R.id.tv_camera);
        tv_file=dialogView.findViewById(R.id.tv_file);

        View rl_root=dialogView.findViewById(R.id.rl_root);

        rl_root.setOnClickListener(
                v-> dismiss()
        );

        tv_cancel.setOnClickListener(
                v->{
                    dismiss();
                }
        );

        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }
}
