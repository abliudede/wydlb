package com.wydlb.first.view.dialog;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.utils.RxDataTool;
import com.wydlb.first.utils.RxRegTool;
import com.wydlb.first.utils.RxTextTool;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSure extends RxDialog {

    private TextView mTvContent;
    private TextView mTvSure;

    TextView mTvTitle;

    public RxDialogSure(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSure(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSure(Context context) {
        super(context);
        initView();
    }

    public RxDialogSure(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }


    public TextView getSureView() {
        return mTvSure;
    }

    public void setSureListener(View.OnClickListener listener) {
        mTvSure.setOnClickListener(listener);
    }

    public TextView getContentView() {
        return mTvContent;
    }


    public void setSure(String content) {
        mTvSure.setText(content);
    }

    public void setTitle(String title){
        if (!RxDataTool.isEmpty(title)){
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
        }
    }
    public void setContent(String str) {
        if (RxRegTool.isURL(str)) {
            // 响应点击事件的话必须设置以下属性
            mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
            mTvContent.setText(RxTextTool.getBuilder("").setBold().append(str).setUrl(str).create());//当内容为网址的时候，内容变为可点击
        } else {
            mTvContent.setText(str);
        }

    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        mTvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
        setContentView(dialogView);
    }

}
