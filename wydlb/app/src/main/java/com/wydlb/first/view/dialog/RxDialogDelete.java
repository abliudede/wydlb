package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.contract.DeleteReplyOrCommentContract;
import com.wydlb.first.ui.presenter.DeleteReplyOrCommentPresenter;

import javax.inject.Inject;


public class RxDialogDelete extends RxDialog implements DeleteReplyOrCommentContract.View{

    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;

    OptionCallback optionCallback;
    @Inject
    DeleteReplyOrCommentPresenter deleteReplyOrCommentPresenter;


    public  boolean isPost=true;
    public String id;
    private int optionPosition=0;

    public RxDialogDelete(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogDelete(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogDelete(Context context) {
        super(context);
        initView();
    }

    public RxDialogDelete(Activity context) {
        super(context);
        initView();
    }

    public RxDialogDelete(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public OptionCallback getOptionCallback() {
        return optionCallback;
    }

    public void setOptionCallback(OptionCallback optionCallback) {
        this.optionCallback = optionCallback;
    }

    public int getOptionPosition() {
        return optionPosition;
    }

    public void setOptionPosition(int optionPosition) {
        this.optionPosition = optionPosition;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public void setButtonText(String confirm,String cancle){
        this.mTvCancel.setText(cancle);
        this.mTvSure.setText(confirm);
    }

    public TextView getContentView() {
        return mTvContent;
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

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private void initView() {
        DaggerAccountComponent.builder().appComponent(BuglyApplicationLike.getsInstance().getAppComponent()).build().inject(this);
        deleteReplyOrCommentPresenter.attachView(this);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        mTvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        mTvSure.setOnClickListener(
                v-> {
                    dismiss();
                    deleteReplyOrCommentPresenter.deleteReplyOrComment(isPost(),getId());
                }
        );
        mTvCancel.setOnClickListener(
                v->dismiss()
        );
    }

    @Override
    public void showError(String message) {
        dismiss();
    }

    @Override
    public void deleteReplyOrComment(BaseBean bean) {
        dismiss();
        if (null!=getOptionCallback()){
            getOptionCallback().optionCallBack(bean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE?true:false,getOptionPosition());
        }
    }

    @Override
    public void complete(String message) {
        dismiss();
    }

    public interface OptionCallback{
        void optionCallBack(boolean flag,int pos);
    }
}
