package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.bean.CaptchaBean;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogCaptcha extends RxDialog {

    private ImageView ivCaptcha;
    private TextView mTvSure;
    private TextView mTvCancel;
    private EditText edCaptcha;



    public RxDialogCaptcha(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogCaptcha(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogCaptcha(Context context) {
        super(context);
        initView();
    }

    public RxDialogCaptcha(Activity context) {
        super(context);
        initView();
    }

    public RxDialogCaptcha(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }


    public void setButtonText(String confirm,String cancle){
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

    public void setRefreshListener(OnRepeatClickListener onRepeatClickListener){
        ivCaptcha.setOnClickListener(onRepeatClickListener);
    }

    public  void refreshCaptcha(CaptchaBean bean){
        if (null!=ivCaptcha&&null!=bean){
            RxImageTool.base64StringToBitmap(bean.getData().getCaptchaBase64(),ivCaptcha);
            RxLogTool.e(bean.getData().getCaptchaBase64());
        }
    }

    public String getCaptchaCode(){
        return edCaptcha.getText().toString();
    }

    /**
     * 清除验证码
     */
    public void clearCaptcha(){
        if (null!=edCaptcha){
            edCaptcha.setText("");
        }
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_captcha, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        ivCaptcha=(ImageView)dialogView.findViewById(R.id.iv_captcha);
        edCaptcha=(EditText)dialogView.findViewById(R.id.ed_captcha);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
