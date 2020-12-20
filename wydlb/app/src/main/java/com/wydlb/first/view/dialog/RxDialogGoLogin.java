package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.ui.activity.ActivityLoginNew;


/**
 * 支付密码弹框
 */
public class RxDialogGoLogin extends Dialog  {
    TextView tv_sure;
    ImageView tv_cancel;


    TextView tv_register;
    Context mContext;


    public RxDialogGoLogin(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogGoLogin(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogGoLogin(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogGoLogin(Activity context) {
        super(context);
        mContext=context;
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
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_go_login, null);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_cancel=(ImageView)dialogView.findViewById(R.id.tv_cancel);
        tv_register=dialogView.findViewById(R.id.tv_register);


        tv_cancel.setOnClickListener(
                v->dismiss()
        );

        tv_sure.setOnClickListener(
                view -> {
                    ActivityLoginNew.startActivity(mContext);
                    dismiss();
                }
        );
        tv_register.setOnClickListener(
                v->{
                    ActivityLoginNew.startActivity(mContext);
//                    ActivityLogin.startActivity(mContext,false);
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

    }


    public TextView getTv_register() {
        return tv_register;
    }

    public void setTv_register(TextView tv_register) {
        this.tv_register = tv_register;
    }

    /**
     * 设置全屏显示
     */
    public void setFullScreen() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
    }


}
