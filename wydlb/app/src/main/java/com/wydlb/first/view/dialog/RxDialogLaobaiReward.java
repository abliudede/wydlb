package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wydlb.first.R;


/**
 * 老白导流奖励
 */
public class RxDialogLaobaiReward extends RxDialog {


    private View dialogView;

    private LinearLayout ly_item;
    private TextView tv_know_it;


    public RxDialogLaobaiReward(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogLaobaiReward(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogLaobaiReward(Activity context) {
        super(context,R.style.NoneDialogStyle);
        initView();
    }

    public RxDialogLaobaiReward(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public LinearLayout getLy_item() {
        return ly_item;
    }

    public void setLy_item(LinearLayout ly_item) {
        this.ly_item = ly_item;
    }

    public TextView getTv_know_it() {
        return tv_know_it;
    }

    public void setTv_know_it(TextView tv_know_it) {
        this.tv_know_it = tv_know_it;
    }

    private void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_laobai_reward, null);

        ly_item = dialogView.findViewById(R.id.ly_item);
        tv_know_it=dialogView.findViewById(R.id.tv_know_it);


        tv_know_it.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(true);
        setContentView(dialogView);
    }

}
