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


public class RxDialogPersonOpt extends Dialog {
    TextView tv_share_wx;
    TextView tv_share_qq;
    TextView tv_share_timeline;
    TextView tv_share_weibo;


    private TextView tv_share_kouling;
    private TextView tv_share_jubao;
    private TextView tv_share_heimingdan;

    private TextView tv_cancel;

    public RxDialogPersonOpt(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogPersonOpt(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogPersonOpt(Context context) {
        super(context);
        initView();
    }

    public RxDialogPersonOpt(Activity context) {
        super(context);
        initView();
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

    public TextView getTv_share_kouling() {
        return tv_share_kouling;
    }

    public void setTv_share_kouling(TextView tv_share_kouling) {
        this.tv_share_kouling = tv_share_kouling;
    }

    public TextView getTv_share_jubao() {
        return tv_share_jubao;
    }

    public void setTv_share_jubao(TextView tv_share_jubao) {
        this.tv_share_jubao = tv_share_jubao;
    }

    public TextView getTv_share_heimingdan() {
        return tv_share_heimingdan;
    }

    public void setTv_share_heimingdan(TextView tv_share_heimingdan) {
        this.tv_share_heimingdan = tv_share_heimingdan;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_person_homepage_share, null);
        tv_share_wx = (TextView) dialogView.findViewById(R.id.tv_share_wx);
        tv_share_qq = (TextView) dialogView.findViewById(R.id.tv_share_qq);
        tv_share_timeline = (TextView) dialogView.findViewById(R.id.tv_share_timeline);
        tv_share_weibo=(TextView)dialogView.findViewById(R.id.tv_share_weibo);

        tv_share_kouling = (TextView) dialogView.findViewById(R.id.tv_share_kouling);
        tv_share_jubao = (TextView) dialogView.findViewById(R.id.tv_share_jubao);
        tv_share_heimingdan = (TextView) dialogView.findViewById(R.id.tv_share_heimingdan);

        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(
                v->dismiss()
        );

        setCanceledOnTouchOutside(true);
        setContentView(dialogView);
    }
}
