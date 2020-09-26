package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.lianzai.reader.R;


/**
 * 书架内容筛选
 */
public class RxDialogBookStoreChoose extends RxDialog {

    TextView tv1;
    TextView tv2;
    TextView tv3;

    //选中状态1/2/3
    int choose = 1;

    private View dialogView;

    public RxDialogBookStoreChoose(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogBookStoreChoose(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

//    public RxDialogQiandaoReward(Context context) {
//        super(context);
//        initView();
//    }

    public RxDialogBookStoreChoose(Activity context) {
        super(context,R.style.NoneDialogStyle);
        initView();
    }

    public RxDialogBookStoreChoose(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public TextView getTv1() {
        return tv1;
    }

    public void setTv1(TextView tv1) {
        this.tv1 = tv1;
    }

    public TextView getTv2() {
        return tv2;
    }

    public void setTv2(TextView tv2) {
        this.tv2 = tv2;
    }

    public TextView getTv3() {
        return tv3;
    }

    public void setTv3(TextView tv3) {
        this.tv3 = tv3;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
        switch (choose){
            case 1:
                tv1.setTextColor(getContext().getResources().getColor(R.color.bluetext_color));
                tv2.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                tv3.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                break;
            case 2:
                tv1.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                tv2.setTextColor(getContext().getResources().getColor(R.color.bluetext_color));
                tv3.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                break;
            case 3:
                tv1.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                tv2.setTextColor(getContext().getResources().getColor(R.color.normal_text_color));
                tv3.setTextColor(getContext().getResources().getColor(R.color.bluetext_color));
                break;
        }
    }

    private void initView() {
//        Window window = getWindow(); //得到对话框
//        window.setWindowAnimations(R.style.BottomDialogStyle); //设置窗口弹出动画

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须设置才能不错位。（某些机型上的这个自适配不行）
        setFullScreen();

        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bookstore_choose, null);
        tv1=dialogView.findViewById(R.id.tv1);
        tv2=dialogView.findViewById(R.id.tv2);
        tv3=dialogView.findViewById(R.id.tv3);

        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setContentView(dialogView);

        dialogView.setOnClickListener(
                v -> {
                    dismiss();
                }
        );

    }

}
