package com.wydlb.first.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.utils.RxDataTool;

/**
 * Created by lrz on 2017/10/25.
 */

public class RxToolBar extends Toolbar {

    private TextView mTitleView;

    private ImageView mBackImageView;

    private TextView mOptionsTextView;

    private View mContentView;

    public RxToolBar(Context context) {
        super(context);
    }

    public RxToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView=findViewById(R.id.content_view);
        mBackImageView=(ImageView)findViewById(R.id.img_back);
        mTitleView=(TextView)findViewById(R.id.tv_commom_title);
        mOptionsTextView=(TextView)findViewById(R.id.tv_options);
    }

    public View getmContentView() {
        return mContentView;
    }

    public void setmContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    /**
     * 设置标题以及右边操作
     * @param titleId
     * @param optionsId
     */
    public void initToolbarData(int titleId,int optionsId){
        if (titleId>0){
            mTitleView.setText(getResources().getString(titleId));
        }
        if (optionsId>0){
            mOptionsTextView.setText(getResources().getString(optionsId));
            mOptionsTextView.setVisibility(View.VISIBLE);
        }else{
            mOptionsTextView.setVisibility(View.GONE);
        }
    }

    public void initToolbarData(String title,int optionsId){
        if (!RxDataTool.isEmpty(title)){
            mTitleView.setText(title);
        }
        if (optionsId>0){
            mOptionsTextView.setText(getResources().getString(optionsId));
            mOptionsTextView.setVisibility(View.VISIBLE);
        }else{
            mOptionsTextView.setVisibility(View.GONE);
        }
    }

    public void initToolbarData(String title,String option){
        if (!RxDataTool.isEmpty(title)){
            mTitleView.setText(title);
        }
        if (!RxDataTool.isEmpty(option)){
            mOptionsTextView.setText(option);
            mOptionsTextView.setVisibility(View.VISIBLE);
        }else{
            mOptionsTextView.setVisibility(View.GONE);
        }
    }



    public void setOptionsClickListener(OnRepeatClickListener listener){
        mOptionsTextView.setOnClickListener(listener);
    }

    public TextView getmOptionsTextView() {
        return mOptionsTextView;
    }

    public void setmOptionsTextView(TextView mOptionsTextView) {
        this.mOptionsTextView = mOptionsTextView;
    }

    public void setBackClickListener(OnClickListener listener){
        mBackImageView.setOnClickListener(listener);
    }


    public TextView getmTitleView() {
        return mTitleView;
    }

    public void setmTitleView(TextView mTitleView) {
        this.mTitleView = mTitleView;
    }

    public ImageView getmBackImageView() {
        return mBackImageView;
    }

    public void setmBackImageView(ImageView mBackImageView) {
        this.mBackImageView = mBackImageView;
    }
}
