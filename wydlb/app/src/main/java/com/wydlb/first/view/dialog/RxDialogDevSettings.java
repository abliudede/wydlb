package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.ui.SplashActivity;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.view.RxToast;


public class RxDialogDevSettings extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;

    Button btn_reset;

    EditText ed_ip;
    public RxDialogDevSettings(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogDevSettings(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogDevSettings(Context context) {
        super(context);
        initView();
    }

    public RxDialogDevSettings(Activity context) {
        super(context);
        initView();
    }

    public RxDialogDevSettings(Context context, float alpha, int gravity) {
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

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_dev_settings, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        ed_ip = (EditText) dialogView.findViewById(R.id.ed_ip);
        btn_reset=(Button)dialogView.findViewById(R.id.btn_reset);

        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );
        if (!TextUtils.isEmpty(RxSharedPreferencesUtil.getInstance().getString("api"))){
            ed_ip.setText(RxSharedPreferencesUtil.getInstance().getString("api"));
        }
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSharedPreferencesUtil.getInstance().putString("api","https://beta.m.lianzai.com");
                RxToast.custom("地址重置成功").show();
                dismiss();
            }
        });
        setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ed_ip.getText().toString())){
                    Constant.API_BASE_URL=ed_ip.getText().toString().contains("http")?ed_ip.getText().toString():("https://"+ed_ip.getText().toString());
                    RxToast.custom("地址切换成功,重启后自动生效").show();
                    RxSharedPreferencesUtil.getInstance().putString("api",Constant.API_BASE_URL);
                    dismiss();
                    //启动页
                    Intent intent = new Intent(mContext, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
    }
}
