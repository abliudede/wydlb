package com.lianzai.reader.ui.adapter;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.MyPraisedBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2019/3/21.
 * 收到的赞
 */

public class MyPraisedItemAdapter extends BaseQuickAdapter<MyPraisedBean.DataBean.ListBean,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public MyPraisedItemAdapter(@Nullable List<MyPraisedBean.DataBean.ListBean> data) {
        super(R.layout.item_my_notice, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, MyPraisedBean.DataBean.ListBean bean) {
        View view_content=baseViewHolder.getView(R.id.view_content);
        CircleImageView iv_logo= baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname= baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_expandable_text= baseViewHolder.getView(R.id.tv_expandable_text);
        tv_expandable_text.setVisibility(View.INVISIBLE);


        TextView tv_dynamic= baseViewHolder.getView(R.id.tv_dynamic);
        SelectableRoundedImageView iv_dynamic=baseViewHolder.getView(R.id.iv_dynamic);

        TextView tv_date=baseViewHolder.getView(R.id.tv_date);

        //主体区域点击
        view_content.setOnClickListener(
                v -> {
                    getContentClickListener().contentClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );

        iv_logo.setOnClickListener(
                v -> {
                    getContentClickListener().headNickClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );


        RxImageTool.loadLogoImage(mContext,bean.getUserPic(),iv_logo);
        StringBuilder sb = new StringBuilder();
        if(!TextUtils.isEmpty(bean.getUserName())){
            if(bean.getUserName().length() > 5){
                sb.append(bean.getUserName().substring(0,5));
                sb.append("...");
            }else {
                sb.append(bean.getUserName());
            }
        }
        sb.append(" 赞了你");
//        if(bean.getType() ==1 ){
//            sb.append(" 评论了你");
//        }else {
//            sb.append(" 回复了你");
//        }
        tv_nickname.setText(sb.toString());

        tv_date.setText(TimeFormatUtil.getInterval(bean.getCreateTime()) + " | " + bean.getPlatformName());

        if(TextUtils.isEmpty(bean.getPicturesShow())){
            tv_dynamic.setText(bean.getPostContent());
            iv_dynamic.setVisibility(View.GONE);
        }else {
            tv_dynamic.setText("");
            iv_dynamic.setVisibility(View.VISIBLE);
            RxImageTool.loadImage(mContext,bean.getPicturesShow(),iv_dynamic);
        }

    }


    public interface ContentClickListener{
        void headNickClick(View v, int pos);
        void praiseClick(View v, int pos);
        void replyClick(View v, int pos);
        void contentClick(View v, int pos);
    }
}
