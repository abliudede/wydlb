package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.utils.RxKeyboardTool;


/**
 * 支付密码弹框
 */
public class RxDialogPayment extends Dialog  {
    TextView tv_sure;
    ImageView tv_cancel;

    EditText ed_pay_password;

    Context mContext;

    public EditText getEd_pay_password() {
        return ed_pay_password;
    }

    public RxDialogPayment(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogPayment(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogPayment(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogPayment(Activity context) {
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

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment, null);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_cancel=(ImageView)dialogView.findViewById(R.id.tv_cancel);


        ed_pay_password=(EditText)dialogView.findViewById(R.id.ed_pay_password);
        ed_pay_password.setFocusable(true);
        ed_pay_password.setFocusableInTouchMode(true);
        ed_pay_password.requestFocus();

        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RxKeyboardTool.showSoftInput(BuglyApplicationLike.getContext(),ed_pay_password);
                        }catch (Exception e){

                        }

                    }
                },100);
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ed_pay_password.setText("");
            }
        });

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
