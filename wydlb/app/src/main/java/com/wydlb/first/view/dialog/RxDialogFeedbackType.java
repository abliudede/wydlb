package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wydlb.first.R;
import com.wydlb.first.bean.FeedBackTypeResponse;
import com.wydlb.first.ui.adapter.FeedbackTypesAdapter;
import com.wydlb.first.utils.DividerItemDecoration;
import com.wydlb.first.utils.RxLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class RxDialogFeedbackType extends RxDialog {

    private TextView tv_cancel;

    private RecyclerView rv_feedback_types;

    RelativeLayout rl_dialog_parent;

    FeedbackTypesAdapter feedbackTypesAdapter;


    List<FeedBackTypeResponse.DataBean>dataBeanList=new ArrayList<>();

    FeedbackTypeCallback feedbackTypeCallback;

    public RxDialogFeedbackType(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogFeedbackType(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogFeedbackType(Context context) {
        super(context);
        initView();
    }


    public RxDialogFeedbackType(Activity context) {
        super(context,R.style.ReadOptionDialogStyle);
        initView();
    }

    public RxDialogFeedbackType(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


    public FeedbackTypeCallback getFeedbackTypeCallback() {
        return feedbackTypeCallback;
    }

    public void setFeedbackTypeCallback(FeedbackTypeCallback feedbackTypeCallback) {
        this.feedbackTypeCallback = feedbackTypeCallback;
    }

    private void initView() {

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_feedback_types, null);

        rv_feedback_types=dialogView.findViewById(R.id.rv_feedback_types);

        tv_cancel = dialogView.findViewById(R.id.tv_cancel);

        rl_dialog_parent=dialogView.findViewById(R.id.rl_dialog_parent);

        feedbackTypesAdapter=new FeedbackTypesAdapter(dataBeanList);
        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(mContext);
        rv_feedback_types.setLayoutManager(rxLinearLayoutManager);
        rv_feedback_types.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        rv_feedback_types.setAdapter(feedbackTypesAdapter);

        feedbackTypesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null!=getFeedbackTypeCallback()){
                    getFeedbackTypeCallback().feedBackTypeClick(feedbackTypesAdapter.getItem(position));
                }
                dismiss();
            }
        });

        rl_dialog_parent.setOnClickListener(
                v->dismiss()
        );


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        setFullScreen();

    }

    public void setData(List<FeedBackTypeResponse.DataBean>list){
        if (null!=feedbackTypesAdapter&&null!=list){
            feedbackTypesAdapter.replaceData(list);
        }
    }

    public interface FeedbackTypeCallback{
        void feedBackTypeClick(FeedBackTypeResponse.DataBean dataBean);
    }


}
