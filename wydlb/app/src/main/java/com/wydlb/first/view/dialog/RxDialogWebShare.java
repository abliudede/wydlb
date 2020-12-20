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


public class RxDialogWebShare extends Dialog {
    TextView tv_share_wx;
    TextView tv_share_sms;
    TextView tv_share_qq;
    TextView tv_share_timeline;
    TextView tv_share_weibo;
    private TextView tv_cancel;
    private TextView tv_share_refresh;
    private TextView tv_share_copy;
    private TextView tv_share_browseopen;
    private TextView tv_share_ftf;


    public RxDialogWebShare(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogWebShare(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogWebShare(Context context) {
        super(context);
        initView();
    }

    public RxDialogWebShare(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_share_sms() {
        return tv_share_sms;
    }

    public void setTv_share_sms(TextView tv_share_sms) {
        this.tv_share_sms = tv_share_sms;
    }

    public TextView getTv_share_qq() {
        return tv_share_qq;
    }

    public void setTv_share_qq(TextView tv_share_qq) {
        this.tv_share_qq = tv_share_qq;
    }

    public TextView getTv_share_timeline() {
        return tv_share_timeline;
    }

    public void setTv_share_timeline(TextView tv_share_timeline) {
        this.tv_share_timeline = tv_share_timeline;
    }

    public TextView getTv_share_weibo() {
        return tv_share_weibo;
    }

    public void setTv_share_weibo(TextView tv_share_weibo) {
        this.tv_share_weibo = tv_share_weibo;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    public TextView getTv_share_wx() {
        return tv_share_wx;
    }

    public void setTv_share_wx(TextView tv_share_wx) {
        this.tv_share_wx = tv_share_wx;
    }

    public TextView getTv_share_refresh() {
        return tv_share_refresh;
    }

    public void setTv_share_refresh(TextView tv_share_refresh) {
        this.tv_share_refresh = tv_share_refresh;
    }

    public TextView getTv_share_copy() {
        return tv_share_copy;
    }

    public void setTv_share_copy(TextView tv_share_copy) {
        this.tv_share_copy = tv_share_copy;
    }

    public TextView getTv_share_browseopen() {
        return tv_share_browseopen;
    }

    public void setTv_share_browseopen(TextView tv_share_browseopen) {
        this.tv_share_browseopen = tv_share_browseopen;
    }

    public TextView getTv_share_ftf() {
        return tv_share_ftf;
    }

    public void setTv_share_ftf(TextView tv_share_ftf) {
        this.tv_share_ftf = tv_share_ftf;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_web_share, null);
        tv_share_ftf = (TextView) dialogView.findViewById(R.id.tv_share_ftf);
        tv_share_wx = (TextView) dialogView.findViewById(R.id.tv_share_wx);
        tv_share_sms  = (TextView) dialogView.findViewById(R.id.tv_share_sms);
        tv_share_qq = (TextView) dialogView.findViewById(R.id.tv_share_qq);
        tv_share_timeline = (TextView) dialogView.findViewById(R.id.tv_share_timeline);
        tv_share_weibo=(TextView)dialogView.findViewById(R.id.tv_share_weibo);

        tv_share_refresh = (TextView) dialogView.findViewById(R.id.tv_share_refresh);
        tv_share_copy = (TextView) dialogView.findViewById(R.id.tv_share_copy);
        tv_share_browseopen = (TextView) dialogView.findViewById(R.id.tv_share_browseopen);


        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(
                v->dismiss()
        );

        setCanceledOnTouchOutside(true);
        setContentView(dialogView);
    }
}
