package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxDialogExchange extends Dialog {
    ImageView iv_close;
    ImageView iv_clear;
    EditText ed_search_keyword;
    TextView tv_day_count;
    TextView tv_save;
    TextView tv_all;

    int bookId;


    public RxDialogExchange(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogExchange(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogExchange(Context context) {
        super(context);
        initView();
    }

    public RxDialogExchange(Activity context) {
        super(context);
        initView();
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public ImageView getIv_clear() {
        return iv_clear;
    }

    public void setIv_clear(ImageView iv_clear) {
        this.iv_clear = iv_clear;
    }

    public EditText getEd_search_keyword() {
        return ed_search_keyword;
    }

    public void setEd_search_keyword(EditText ed_search_keyword) {
        this.ed_search_keyword = ed_search_keyword;
    }

    public TextView getTv_day_count() {
        return tv_day_count;
    }

    public void setTv_day_count(TextView tv_day_count) {
        this.tv_day_count = tv_day_count;
    }

    public TextView getTv_save() {
        return tv_save;
    }

    public void setTv_save(TextView tv_save) {
        this.tv_save = tv_save;
    }

    public TextView getTv_all() {
        return tv_all;
    }

    public void setTv_all(TextView tv_all) {
        this.tv_all = tv_all;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exchange, null);
        iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);
        iv_clear = (ImageView) dialogView.findViewById(R.id.iv_clear);
        ed_search_keyword  = (EditText) dialogView.findViewById(R.id.ed_search_keyword);
        tv_day_count = (TextView) dialogView.findViewById(R.id.tv_day_count);
        tv_save = (TextView) dialogView.findViewById(R.id.tv_save);
        tv_all = (TextView) dialogView.findViewById(R.id.tv_all);
        iv_close.setOnClickListener(
                v->dismiss()
        );
        iv_clear.setOnClickListener(
                v -> {
                    ed_search_keyword.setText("");
                }
        );

        ed_search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    iv_clear.setVisibility(View.GONE);
                }else {
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        });

        setCanceledOnTouchOutside(true);
        setContentView(dialogView);
    }
}
