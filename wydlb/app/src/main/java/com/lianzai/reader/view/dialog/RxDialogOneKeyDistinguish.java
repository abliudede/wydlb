package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;


/**
 * 识别好友确认
 */
public class RxDialogOneKeyDistinguish extends RxDialog {


    private TextView tv_sure;
    private TextView tv_nomore;

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_nomore() {
        return tv_nomore;
    }

    public void setTv_nomore(TextView tv_nomore) {
        this.tv_nomore = tv_nomore;
    }

    public RxDialogOneKeyDistinguish(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogOneKeyDistinguish(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogOneKeyDistinguish(Context context) {
        super(context);
        initView();
    }

    public RxDialogOneKeyDistinguish(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogOneKeyDistinguish(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }



    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_one_key_distinguish, null);
        tv_sure = dialogView.findViewById(R.id.tv_sure);
        tv_nomore = dialogView.findViewById(R.id.tv_nomore);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
