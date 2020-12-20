package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogBookFeedback extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;

    private ImageView iv_clear_author_name;
    private ImageView iv_clear_book_name;

    EditText ed_author_name;
    EditText ed_book_name;

    public RxDialogBookFeedback(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogBookFeedback(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogBookFeedback(Context context) {
        super(context);
        initView();
    }

    public RxDialogBookFeedback(Activity context) {
        super(context,R.style.OptionDialogStyle);
        initView();
    }

    public RxDialogBookFeedback(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }




    public void setButtonText(String confirm,String cancle){
        this.mTvCancel.setText(cancle);
        this.mTvSure.setText(confirm);
    }


    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_book_feedback, null);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel =  dialogView.findViewById(R.id.tv_cancel);
        iv_clear_author_name=dialogView.findViewById(R.id.iv_clear_author_name);
        iv_clear_book_name=dialogView.findViewById(R.id.iv_clear_book_name);
        ed_author_name=dialogView.findViewById(R.id.ed_author_name);

        ed_book_name=dialogView.findViewById(R.id.ed_book_name);

        iv_clear_author_name.setOnClickListener(
                view -> ed_author_name.setText("")
        );

        iv_clear_book_name.setOnClickListener(
                view -> ed_book_name.setText("")
        );

        ed_author_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")){
                    iv_clear_author_name.setVisibility(View.GONE);
                }else{
                    iv_clear_author_name.setVisibility(View.VISIBLE);

                }
                checkSubmitStatus();
            }
        });

        ed_book_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")){
                    iv_clear_book_name.setVisibility(View.GONE);
                }else{
                    iv_clear_book_name.setVisibility(View.VISIBLE);
                }
                checkSubmitStatus();
            }
        });


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        setCancelListener(
                v->dismiss()
        );

    }

    public EditText getEd_author_name() {
        return ed_author_name;
    }

    public EditText getEd_book_name() {
        return ed_book_name;
    }

    private void checkSubmitStatus(){
        if (!TextUtils.isEmpty(ed_book_name.getText().toString())){
            mTvSure.setEnabled(true);
            mTvSure.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            mTvSure.setEnabled(false);
            mTvSure.setTextColor(mContext.getResources().getColor(R.color.white_05_alpha_color));
        }
    }
}
