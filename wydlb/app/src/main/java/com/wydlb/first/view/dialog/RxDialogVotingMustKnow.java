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
import com.wydlb.first.utils.RxSharedPreferencesUtil;


/**
 * 支付密码弹框
 */
public class RxDialogVotingMustKnow extends Dialog  {


    Context mContext;
    private TextView tv_des;
    TextView tv_sure;
    TextView tv_nomore;


    public RxDialogVotingMustKnow(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogVotingMustKnow(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogVotingMustKnow(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogVotingMustKnow(Activity context) {
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

    public TextView getTv_des() {
        return tv_des;
    }

    public void setTv_des(TextView tv_des) {
        this.tv_des = tv_des;
    }

    public TextView getTv_nomore() {
        return tv_nomore;
    }

    public void setTv_nomore(TextView tv_nomore) {
        this.tv_nomore = tv_nomore;
    }

    private void initView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_voting_mustknow, null);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        tv_nomore = (TextView) dialogView.findViewById(R.id.tv_nomore);
        tv_des = (TextView)dialogView.findViewById(R.id.tv_des);

        tv_sure.setOnClickListener(
                v->dismiss()
        );
        tv_nomore.setOnClickListener(
                v->{
                    RxSharedPreferencesUtil.getInstance().putBoolean("votingmustknow",false);
                    dismiss();
                }
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
