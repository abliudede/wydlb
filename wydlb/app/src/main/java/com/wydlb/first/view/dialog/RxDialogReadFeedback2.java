package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;

/**
 * 阅读反馈弹框
 */
public class RxDialogReadFeedback2 extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;


    EditText ed_feed_content;

    RelativeLayout rl_dialog_parent;


    private TextView tv_title;
    private TextView tv_des;


    public RxDialogReadFeedback2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogReadFeedback2(Context context) {
        super(context);
        initView();
    }

    public RxDialogReadFeedback2(Activity context) {
        super(context,R.style.BottomDialogStyle);
        initView();
    }

    public RxDialogReadFeedback2(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_des() {
        return tv_des;
    }

    public void setTv_des(TextView tv_des) {
        this.tv_des = tv_des;
    }

    public EditText getEd_feed_content() {
        return ed_feed_content;
    }

    public void setEd_feed_content(EditText ed_feed_content) {
        this.ed_feed_content = ed_feed_content;
    }

    public void setButtonText(String confirm, String cancle){
        this.mTvCancel.setText(cancle);
        this.mTvSure.setText(confirm);
    }


    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_read_feedback, null);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel =  dialogView.findViewById(R.id.tv_cancel);
        ed_feed_content=dialogView.findViewById(R.id.ed_feed_content);
        ed_feed_content.setHint("在此粘贴网址");

        tv_title =dialogView.findViewById(R.id.tv_title);
        tv_des =dialogView.findViewById(R.id.tv_des);

        rl_dialog_parent=dialogView.findViewById(R.id.rl_dialog_parent);

        mTvSure.setEnabled(false);
        mTvSure.setTextColor(mContext.getResources().getColor(R.color.white_05_alpha_color));

        ed_feed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSubmitStatus();
            }
        });

        rl_dialog_parent.setOnClickListener(
                v->dismiss()
        );

        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );

        setFullScreen();

    }


    private void checkSubmitStatus(){
        if (!TextUtils.isEmpty(ed_feed_content.getText().toString().trim())){
            mTvSure.setEnabled(true);
            mTvSure.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            mTvSure.setEnabled(false);
            mTvSure.setTextColor(mContext.getResources().getColor(R.color.white_05_alpha_color));
        }
    }


}
