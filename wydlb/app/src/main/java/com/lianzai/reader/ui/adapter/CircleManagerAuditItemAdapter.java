package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.ReportAuditListBean;
import com.lianzai.reader.bean.ManagerAuditBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.utils.CustomLinkMovementMethod;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.CenteredImageSpan;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.EllipsizeTextView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 圈子审核管理员item
 */

public class CircleManagerAuditItemAdapter extends BaseQuickAdapter<ManagerAuditBean.DataBean.ListBean, BaseViewHolder> {


    Context context;
    Drawable nanDrawable;
    Drawable nvDrawable;

    private OnPersonClickListener onPersonClickListener;

    public OnPersonClickListener getOnPersonClickListener() {
        return onPersonClickListener;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }

    public CircleManagerAuditItemAdapter(@Nullable List<ManagerAuditBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_manager_audit, data);
        this.context = mContext;
        nanDrawable = RxImageTool.getDrawable(R.mipmap.nan_icon);
        nvDrawable = RxImageTool.getDrawable(R.mipmap.nv_icon);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,ManagerAuditBean.DataBean.ListBean bean) {

        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);

        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);


        TextView read_time_tv = baseViewHolder.getView(R.id.read_time_tv);
        TextView post_count_tv = baseViewHolder.getView(R.id.post_count_tv);
        TextView comment_count_tv = baseViewHolder.getView(R.id.comment_count_tv);

        EllipsizeTextView tv_dynamic_content_text = baseViewHolder.getView(R.id.tv_dynamic_content_text);

        TextView sure_tv = baseViewHolder.getView(R.id.sure_tv);
        TextView delete_tv = baseViewHolder.getView(R.id.delete_tv);


        if(bean.getGender() == 0){
            iv_gender.setImageDrawable(nanDrawable);
        }else {
            iv_gender.setImageDrawable(nvDrawable);
        }
        RxImageTool.loadLogoImage(context,bean.getAvatar(),iv_logo);
        tv_nickname.setText(bean.getNickName());

        SpannableString desStr = new SpannableString("申请成为《"+bean.getCircleName()+"》圈管理员");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ActivityCircleDetail.startActivity(context,String.valueOf(bean.getCircleId()));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
//                    ds.setStrikeThruText(true);
                    ds.setColor(0xFF3865FE);
                }
        };
        desStr.setSpan(clickableSpan, 4, 6 + bean.getCircleName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_des.setText(desStr);
        tv_des.setMovementMethod(CustomLinkMovementMethod.getInstance());

        int minute = bean.getBookReadTime()/60;
        if(minute >= 1) {
            read_time_tv.setText(String.valueOf(minute) + "分钟");
        }else {
            read_time_tv.setText("少于1分钟");
        }
        post_count_tv.setText(String.valueOf(bean.getSendPostCount()));
        comment_count_tv.setText(String.valueOf(bean.getCommentAndReplyCount()));

        tv_dynamic_content_text.setText("申请理由：" + bean.getApplyContent());

        iv_logo.setOnClickListener(
                v->onPersonClickListener.avatorClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );

        tv_dynamic_content_text.setOnClickListener(
                v->onPersonClickListener.detailClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        sure_tv.setOnClickListener(
                v->onPersonClickListener.sureClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        delete_tv.setOnClickListener(
                v->onPersonClickListener.deleteClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );


    }

    public interface OnPersonClickListener{
        void avatorClick(int pos);
        void detailClick(int pos);
        void sureClick(int pos);
        void deleteClick(int pos);

    }


}
