package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;

/**
 * Created by lrz on 2018/1/8.
 */

public class PostItemPopWindow extends PopupWindow {
    private Context context;
    private View conentView;

    TextView tv_reply;
    TextView tv_copy;
    TextView tv_report;
    TextView tv_delete;

    int pos;

    PostItemOptionListener postItemOptionListener;

    public PostItemPopWindow(Context context) {
        this.context = context;
        initView();

    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public PostItemOptionListener getPostItemOptionListener() {
        return postItemOptionListener;
    }

    public void setPostItemOptionListener(PostItemOptionListener postItemOptionListener) {
        this.postItemOptionListener = postItemOptionListener;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.popup_post_item_bg, null);
        tv_reply=(TextView)conentView.findViewById(R.id.tv_reply);
        tv_copy=(TextView)conentView.findViewById(R.id.tv_copy);
        tv_report=(TextView)conentView.findViewById(R.id.tv_report);
        tv_delete=(TextView)conentView.findViewById(R.id.tv_delete);

        tv_reply.setOnClickListener(
                v->getPostItemOptionListener().replyClick()
        );

        tv_copy.setOnClickListener(
                v->getPostItemOptionListener().copyClick()
        );

        tv_report.setOnClickListener(
                v->getPostItemOptionListener().shareClick()
        );
        tv_delete.setOnClickListener(
                v->getPostItemOptionListener().deleteClick()
        );

        this.setContentView(conentView);
        this.setWidth(RxImageTool.dip2px(170));
        this.setHeight(RxImageTool.dip2px(56));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.Popup_Anim);
    }

    public void showDelOption(boolean showDel){
        if (showDel){
            tv_delete.setVisibility(View.VISIBLE);
            tv_report.setVisibility(View.GONE);
        }else{
            tv_report.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.GONE);
        }

    }
    public interface PostItemOptionListener{
        void replyClick();
        void copyClick();
        void shareClick();
        void deleteClick();
    }
}