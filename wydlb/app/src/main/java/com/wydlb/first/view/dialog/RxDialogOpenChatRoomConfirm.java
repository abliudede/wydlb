package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wydlb.first.R;


/**
 * 开通聊天室确认
 */
public class RxDialogOpenChatRoomConfirm extends RxDialog {


    public RxDialogOpenChatRoomConfirm(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogOpenChatRoomConfirm(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogOpenChatRoomConfirm(Context context) {
        super(context);
        initView();
    }

    public RxDialogOpenChatRoomConfirm(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogOpenChatRoomConfirm(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }



    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_open_chat_room_confirm, null);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }
}
