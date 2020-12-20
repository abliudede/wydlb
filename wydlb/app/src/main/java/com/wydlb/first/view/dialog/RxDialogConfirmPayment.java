package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogConfirmPayment extends Dialog {
    TextView tv_confirm_payment;
    TextView tv_payment_way;
    TextView tv_payment_account;

    TextView tv_payment_category;
    ImageView iv_close;

    public RxDialogConfirmPayment(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogConfirmPayment(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogConfirmPayment(Context context) {
        super(context);
        initView();
    }

    public RxDialogConfirmPayment(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_confirm_payment() {
        return tv_confirm_payment;
    }

    public TextView getTv_payment_way() {
        return tv_payment_way;
    }

    public TextView getTv_payment_account() {
        return tv_payment_account;
    }

    public TextView getTv_payment_category() {
        return tv_payment_category;
    }

    public void setPaymentData(String category,String account,String paymentWay,String amount){
        getTv_payment_account().setText(account);
        getTv_payment_category().setText(category);
        getTv_payment_way().setText(paymentWay);
        getTv_confirm_payment().setText("确认锁定"+amount);
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_payment, null);
        tv_confirm_payment = (TextView) dialogView.findViewById(R.id.tv_confirm_payment);
        tv_payment_way=(TextView)dialogView.findViewById(R.id.tv_payment_way);
        tv_payment_account=(TextView)dialogView.findViewById(R.id.tv_payment_account);

        iv_close=(ImageView)dialogView.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(
                v->dismiss()
        );

        tv_payment_category=(TextView)dialogView.findViewById(R.id.tv_payment_category);

        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }
}
