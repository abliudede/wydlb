package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wydlb.first.R;


/**
 * 规则弹窗
 */
public class RxDialogVotingRules extends Dialog  {


    Context mContext;
    private TextView tv_des;
    TextView tv_sure;
    TextView tv_title;


    public RxDialogVotingRules(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogVotingRules(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogVotingRules(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogVotingRules(Activity context) {
        super(context);
        mContext=context;
        initView();
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public void setTv_sure(TextView tv_sure) {
        this.tv_sure = tv_sure;
    }

    public TextView getTv_des() {
        return tv_des;
    }

    public void setTv_des(TextView tv_des) {
        this.tv_des = tv_des;
    }

    private void initView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_voting_rule, null);
        tv_title = (TextView) dialogView.findViewById(R.id.title);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_des = (TextView)dialogView.findViewById(R.id.tv_des);

        tv_sure.setOnClickListener(
                v->dismiss()
        );
        setCanceledOnTouchOutside(false);

        setContentView(dialogView);

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
