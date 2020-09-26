package com.lianzai.reader.view.dialog;

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

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.view.SelectableRoundedImageView;


/**
 * 年度账单弹窗
 */
public class RxDialogAnnualBill extends RxDialog  {

    SelectableRoundedImageView iv_annual;
    ImageView iv_close;

    Context mContext;


    public RxDialogAnnualBill(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogAnnualBill(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogAnnualBill(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogAnnualBill(Activity context) {
        super(context);
        mContext=context;
        initView();
    }


    private void initView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.activity_annual_pop, null);
        iv_annual = (SelectableRoundedImageView) dialogView.findViewById(R.id.iv_annual);
        iv_close=(ImageView)dialogView.findViewById(R.id.iv_close);


        iv_close.setOnClickListener(
                v->dismiss()
        );

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(dialogView);

    }

    public SelectableRoundedImageView getIv_annual() {
        return iv_annual;
    }

    public void setIv_annual(SelectableRoundedImageView iv_annual) {
        this.iv_annual = iv_annual;
    }

    public ImageView getIv_close() {
        return iv_close;
    }

    public void setIv_close(ImageView iv_close) {
        this.iv_close = iv_close;
    }
}
