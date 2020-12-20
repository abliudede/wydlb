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
import com.wydlb.first.utils.RxLogTool;


public class RxDialogMuteOptions extends Dialog {
    TextView tv_des1;
    TextView tv_des2;
    TextView tv_des3;
    TextView tv_des4;

    TextView tv_cancle;


    public RxDialogMuteOptions(Context context) {
        super(context, R.style.BottomDialogStyle);
        initView();
    }

    public RxDialogMuteOptions(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }


    public TextView getTv_des1() {
        return tv_des1;
    }

    public void setTv_des1(TextView tv_des1) {
        this.tv_des1 = tv_des1;
    }

    public TextView getTv_des2() {
        return tv_des2;
    }

    public void setTv_des2(TextView tv_des2) {
        this.tv_des2 = tv_des2;
    }

    public TextView getTv_des3() {
        return tv_des3;
    }

    public void setTv_des3(TextView tv_des3) {
        this.tv_des3 = tv_des3;
    }

    public TextView getTv_des4() {
        return tv_des4;
    }

    public void setTv_des4(TextView tv_des4) {
        this.tv_des4 = tv_des4;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_option_mute, null);
        tv_des1 = (TextView) dialogView.findViewById(R.id.tv_des1);
        tv_des2 = (TextView)dialogView.findViewById(R.id.tv_des2);
        tv_des3 = (TextView) dialogView.findViewById(R.id.tv_des3);
        tv_des4 = (TextView) dialogView.findViewById(R.id.tv_des4);
        tv_cancle=(TextView)dialogView.findViewById(R.id.tv_cancle);


        tv_cancle.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }

}
