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


public class RxDialogImageOption extends Dialog {
    TextView tv_save_pic;
    TextView tv_cancel;
    public RxDialogImageOption(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogImageOption(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogImageOption(Context context) {
        super(context);
        initView();
    }

    public RxDialogImageOption(Activity context) {
        super(context);
        initView();
    }


    public TextView getTv_save_pic() {
        return tv_save_pic;
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

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image_options, null);
        tv_save_pic = (TextView) dialogView.findViewById(R.id.tv_save_pic);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        tv_save_pic.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }
}
