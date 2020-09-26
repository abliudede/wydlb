package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;


/**
 * 不能开通聊天室
 */
public class RxDialogCanNotOpenChatRoom extends RxDialog {

    TextView tv_description;

    TextView tv_know_it;

    public RxDialogCanNotOpenChatRoom(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogCanNotOpenChatRoom(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogCanNotOpenChatRoom(Context context) {
        super(context);
        initView();
    }

    public RxDialogCanNotOpenChatRoom(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogCanNotOpenChatRoom(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
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
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_not_open_chat_room, null);
        tv_description=dialogView.findViewById(R.id.tv_description);
        tv_know_it=dialogView.findViewById(R.id.tv_know_it);

        tv_know_it.setOnClickListener(
                v->{
                    dismiss();
                }
        );


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
