package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxBindWalletDialog extends RxDialog {



    ImageView tv_cancel;


    TextView tv_sure;


    public RxBindWalletDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxBindWalletDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxBindWalletDialog(Context context) {
        super(context);
        initView();
    }

    public RxBindWalletDialog(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public ImageView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(ImageView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {

        Window window = getWindow();
        window.getDecorView().setPadding(0, 40, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER_HORIZONTAL|Gravity.TOP;
        window.setAttributes(attributes);


        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bind_wallet, null);
        tv_cancel=(ImageView) dialogView.findViewById(R.id.tv_cancel);
        tv_sure=dialogView.findViewById(R.id.tv_sure);


        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

    }

}
