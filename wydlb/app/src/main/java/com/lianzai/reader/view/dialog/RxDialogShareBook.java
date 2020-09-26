package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.ui.activity.chat.PublicNumberDetailActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;


public class RxDialogShareBook extends Dialog {
    TextView tv_share_wx_circle;
    TextView tv_share_wx;
    TextView tv_share_copy;
    TextView tv_cancel;

    SelectableRoundedImageView iv_book_cover;
    TextView tv_book_title;
    TextView tv_book_author;
    TextView tv_descrition;

    View view_book_detail;


    public RxDialogShareBook(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogShareBook(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogShareBook(Context context) {
        super(context);
        initView();
    }

    public RxDialogShareBook(Activity context) {
        super(context);
        initView();
    }

    public TextView getTv_share_wx_circle() {
        return tv_share_wx_circle;
    }

    public void setTv_share_wx_circle(TextView tv_share_wx_circle) {
        this.tv_share_wx_circle = tv_share_wx_circle;
    }

    public TextView getTv_share_wx() {
        return tv_share_wx;
    }

    public void setTv_share_wx(TextView tv_share_wx) {
        this.tv_share_wx = tv_share_wx;
    }

    public TextView getTv_share_copy() {
        return tv_share_copy;
    }

    public void setTv_share_copy(TextView tv_share_copy) {
        this.tv_share_copy = tv_share_copy;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    public void setTv_cancel(TextView tv_cancel) {
        this.tv_cancel = tv_cancel;
    }

    private void initView() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_book, null);
        tv_share_wx_circle = (TextView) dialogView.findViewById(R.id.tv_share_wx_circle);
        view_book_detail=dialogView.findViewById(R.id.view_book_detail);
        tv_share_wx = (TextView) dialogView.findViewById(R.id.tv_share_wx);
        tv_share_copy = (TextView) dialogView.findViewById(R.id.tv_share_copy);
        tv_cancel=(TextView)dialogView.findViewById(R.id.tv_cancel);

        iv_book_cover=dialogView.findViewById(R.id.iv_book_cover);
        tv_book_title=(TextView)dialogView.findViewById(R.id.tv_book_title);
        tv_book_author=(TextView)dialogView.findViewById(R.id.tv_book_author);

        tv_descrition=(TextView)dialogView.findViewById(R.id.tv_descrition);


        tv_cancel.setOnClickListener(
                v->dismiss()
        );
        tv_share_copy.setOnClickListener(
                v->{
                    dismiss();
                }
        );
        setCanceledOnTouchOutside(true);

        setContentView(dialogView);
    }

    public void initBookData(Activity context,CollBookBean collBookBean,String barId,int source){
        RxImageTool.loadImage(context,collBookBean.getCover(),iv_book_cover);
        tv_book_title.setText(collBookBean.getTitle());
        tv_book_author.setText((TextUtils.isEmpty(collBookBean.getGenre())?"":collBookBean.getGenre())+"|"+collBookBean.getAuthor());
        tv_descrition.setText(collBookBean.getShortIntro());

        view_book_detail.setOnClickListener(
                v->{
                    dismiss();
//                    ActivityBarDetail.startActivity(context,barId,source);
                    ActivityCircleDetail.startActivity(context,barId);
                }
        );
    }
}
