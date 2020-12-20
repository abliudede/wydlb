package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 好友item
 */

public class CirclePersonItemAdapter extends BaseQuickAdapter<UserAttentionBean.DataBean.ListBean, BaseViewHolder> {


    Context context;
    Drawable addDrawable;
    Drawable huxiangDrawable;

    private Drawable nanDrawable;
    private Drawable nvDrawable;

    private OnPersonClickListener onPersonClickListener;

    public OnPersonClickListener getOnPersonClickListener() {
        return onPersonClickListener;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }

    public CirclePersonItemAdapter(@Nullable List<UserAttentionBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_person, data);
        this.context = mContext;

        int size= RxImageTool.dp2px(22);
        addDrawable = RxTool.getContext().getResources().getDrawable(R.mipmap.guanzhu_jiahao);
        huxiangDrawable = RxTool.getContext().getResources().getDrawable(R.mipmap.huxiangguanzhu);
        addDrawable.setBounds(0,0, size,size);
        huxiangDrawable.setBounds(0,0, size,size);
        nanDrawable = RxImageTool.getDrawable(R.mipmap.nan_icon);
        nvDrawable = RxImageTool.getDrawable(R.mipmap.nv_icon);
        
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UserAttentionBean.DataBean.ListBean bean) {

        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView attention_tv = baseViewHolder.getView(R.id.attention_tv);

        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);

        if(bean.getGender() == 0){
            iv_gender.setImageDrawable(nanDrawable);
        }else {
            iv_gender.setImageDrawable(nvDrawable);
        }

        RxImageTool.loadLogoImage(context,bean.getAvatar(),iv_logo);
        tv_nickname.setText(bean.getNickName());


        if(bean.getAttentionStatus() == 0){
            attention_tv.setBackgroundResource(R.drawable.blue_15dpconor_bg);
            attention_tv.setText("关注");
            attention_tv.setCompoundDrawables(addDrawable,null,null,null);
        }else if(bean.getAttentionStatus() == 1){
            attention_tv.setBackgroundResource(R.drawable.cccccc_15dpconor_bg);
            attention_tv.setText("已关注");
            attention_tv.setCompoundDrawables(null,null,null,null);
        }else if(bean.getAttentionStatus() == 2){
            attention_tv.setBackgroundResource(R.drawable.cccccc_15dpconor_bg);
            attention_tv.setText("互相关注");
            attention_tv.setCompoundDrawables(huxiangDrawable,null,null,null);
        }else {
            attention_tv.setVisibility(View.GONE);
        }

        iv_logo.setOnClickListener(
                v->onPersonClickListener.avatorClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        attention_tv.setOnClickListener(
                v->onPersonClickListener.attentionClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
    }

    public interface OnPersonClickListener{
        void attentionClick(int pos);
        void avatorClick(int pos);
    }


}
