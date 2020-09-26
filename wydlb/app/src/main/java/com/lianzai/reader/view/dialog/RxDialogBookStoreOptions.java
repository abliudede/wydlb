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


public class RxDialogBookStoreOptions extends Dialog {
    TextView tv_book_delete;

    TextView tv_close;



    public RxDialogBookStoreOptions(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogBookStoreOptions(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogBookStoreOptions(Context context) {
        super(context);
        initView();
    }

    public RxDialogBookStoreOptions(Activity context) {
        super(context);
        initView();
    }


    public TextView getTv_book_delete() {
        return tv_book_delete;
    }

    public void setTv_book_delete(TextView tv_book_delete) {
        this.tv_book_delete = tv_book_delete;
    }

    public TextView getTv_close() {
        return tv_close;
    }

    public void setTv_close(TextView tv_close) {
        this.tv_close = tv_close;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_option_book_store, null);
        tv_book_delete =  dialogView.findViewById(R.id.tv_book_delete);
        tv_close=dialogView.findViewById(R.id.tv_close);


        tv_close.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }

}
