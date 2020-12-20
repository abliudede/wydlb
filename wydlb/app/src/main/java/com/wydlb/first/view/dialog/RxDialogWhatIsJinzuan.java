package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.ui.activity.ActivityWebView;


/**
 * 不能开通聊天室
 */
public class RxDialogWhatIsJinzuan extends RxDialog {

    TextView tv_description;

    TextView tv_know_it;
    private TextView tv_showmore;
    private TextView tv_title;

    public RxDialogWhatIsJinzuan(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogWhatIsJinzuan(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogWhatIsJinzuan(Context context) {
        super(context);
        initView();
    }

    public RxDialogWhatIsJinzuan(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogWhatIsJinzuan(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public TextView getTv_showmore() {
        return tv_showmore;
    }

    public void setTv_showmore(TextView tv_showmore) {
        this.tv_showmore = tv_showmore;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_know_it() {
        return tv_know_it;
    }

    public void setTv_know_it(TextView tv_know_it) {
        this.tv_know_it = tv_know_it;
    }

    public TextView getTv_description() {
        return tv_description;
    }

    public void setTv_description(TextView tv_description) {
        this.tv_description = tv_description;
    }

    private void initView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_what_isjinzuan, null);
        tv_title = dialogView.findViewById(R.id.tv_title);
        tv_description=dialogView.findViewById(R.id.tv_description);
        tv_showmore = dialogView.findViewById(R.id.tv_showmore);

        tv_know_it=dialogView.findViewById(R.id.tv_know_it);

        tv_showmore.setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(mContext,"https://fx.bendixing.net/taskCenter/#/recommend");
                }
        );

        tv_know_it.setOnClickListener(
                v->{
                    dismiss();
                }
        );


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
