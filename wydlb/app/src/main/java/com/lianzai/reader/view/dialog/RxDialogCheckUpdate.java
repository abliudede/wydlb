package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.view.RxToast;


public class RxDialogCheckUpdate extends RxDialog {

    private TextView tv_update_title;
    private TextView tv_update_content;


    public RxDialogCheckUpdate(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogCheckUpdate(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogCheckUpdate(Context context) {
        super(context);
        initView();
    }

    public RxDialogCheckUpdate(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogCheckUpdate(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }


    public TextView getTv_update_title() {
        return tv_update_title;
    }

    public void setTv_update_title(TextView tv_update_title) {
        this.tv_update_title = tv_update_title;
    }

    public TextView getTv_update_content() {
        return tv_update_content;
    }

    public void setTv_update_content(TextView tv_update_content) {
        this.tv_update_content = tv_update_content;
    }

    TextView tv_homepage;
    TextView tv_sure;
    TextView tv_cancel;

    public TextView getTv_homepage() {
        return tv_homepage;
    }

    public void setTv_homepage(TextView tv_homepage) {
        this.tv_homepage = tv_homepage;
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {
        Window window = getWindow();
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        attributes.gravity = Gravity.CENTER;
//        window.setAttributes(attributes);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
//        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, null);
        tv_update_title=dialogView.findViewById(R.id.tv_update_title);
        tv_update_content = dialogView.findViewById(R.id.tv_update_content);
        tv_homepage = dialogView.findViewById(R.id.tv_homepage);
        tv_sure = dialogView.findViewById(R.id.tv_sure);
        tv_cancel = dialogView.findViewById(R.id.tv_cancel);

        tv_homepage.setOnClickListener(
                v -> {
                    startBrowserActivity("https://www.lianzai.com/");
                }
        );

        tv_sure.setOnClickListener(
                v -> {
                    try{
                        Uri uri = Uri.parse("market://details?id="+mContext.getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }catch(Exception e){
                        RxToast.custom("您的手机没有安装Android应用市场").show();
                        e.printStackTrace();
                    }
                }
        );

        tv_cancel.setOnClickListener(
                v -> {
                    dismiss();
                }
        );


        setContentView(dialogView);
    }

    private void startBrowserActivity(String url) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            mContext.startActivity(intent);
        } catch (Exception e) {
            RxToast.custom("打开第三方浏览器失败", Constant.ToastType.TOAST_ERROR).show();
        }
    }

}
