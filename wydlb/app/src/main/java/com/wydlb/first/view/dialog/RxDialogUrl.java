package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.view.SelectableRoundedImageView;


public class RxDialogUrl extends Dialog  {

    SelectableRoundedImageView iv_book_cover;
    TextView tv_book_title;
    TextView tv_book_intro;

    RelativeLayout rl_web_book;
    TextView tv_only_url;

    TextView tv_read;
    TextView tv_share;
    TextView tv_cancel;


    public RxDialogUrl(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogUrl(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogUrl(Context context) {
        super(context);
        initView();
    }

    public RxDialogUrl(Activity context) {
        super(context);
        initView();
    }

    public RelativeLayout getRl_web_book() {
        return rl_web_book;
    }

    public void setRl_web_book(RelativeLayout rl_web_book) {
        this.rl_web_book = rl_web_book;
    }

    public TextView getTv_only_url() {
        return tv_only_url;
    }

    public void setTv_only_url(TextView tv_only_url) {
        this.tv_only_url = tv_only_url;
    }

    public SelectableRoundedImageView getIv_book_cover() {
        return iv_book_cover;
    }

    public void setIv_book_cover(SelectableRoundedImageView iv_book_cover) {
        this.iv_book_cover = iv_book_cover;
    }

    public TextView getTv_book_title() {
        return tv_book_title;
    }

    public void setTv_book_title(TextView tv_book_title) {
        this.tv_book_title = tv_book_title;
    }

    public TextView getTv_book_intro() {
        return tv_book_intro;
    }

    public void setTv_book_intro(TextView tv_book_intro) {
        this.tv_book_intro = tv_book_intro;
    }

    public TextView getTv_read() {
        return tv_read;
    }

    public void setTv_read(TextView tv_read) {
        this.tv_read = tv_read;
    }

    public TextView getTv_share() {
        return tv_share;
    }

    public void setTv_share(TextView tv_share) {
        this.tv_share = tv_share;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_url, null);
        iv_book_cover = dialogView.findViewById(R.id.iv_book_cover);
        tv_book_title = dialogView.findViewById(R.id.tv_book_title);
        rl_web_book = dialogView.findViewById(R.id.rl_web_book);
        tv_book_intro = dialogView.findViewById(R.id.tv_book_intro);
        tv_only_url = dialogView.findViewById(R.id.tv_only_url);
        tv_read = dialogView.findViewById(R.id.tv_read);
        tv_share = dialogView.findViewById(R.id.tv_share);
        tv_cancel= dialogView.findViewById(R.id.tv_cancel);


        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

    }


}
