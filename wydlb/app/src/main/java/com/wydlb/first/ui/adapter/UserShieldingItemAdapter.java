package com.wydlb.first.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.UserShieldingBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 黑名单item
 */

public class UserShieldingItemAdapter extends BaseQuickAdapter<UserShieldingBean.DataBean.ListBean, BaseViewHolder> {

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

    public UserShieldingItemAdapter(@Nullable List<UserShieldingBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_person, data);
        this.context = mContext;

        nanDrawable = RxImageTool.getDrawable(R.mipmap.nan_icon);
        nvDrawable = RxImageTool.getDrawable(R.mipmap.nv_icon);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UserShieldingBean.DataBean.ListBean bean) {


        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView attention_tv = baseViewHolder.getView(R.id.attention_tv);


        RxImageTool.loadLogoImage(context,bean.getShieldingUserHeadPortrait(),iv_logo);
        tv_nickname.setText(bean.getShieldingUserName());
        //0男1女
        if(bean.getShieldingUserSex() == 0){
            tv_nickname.setCompoundDrawables(null,null,nanDrawable,null);
        }else {
            tv_nickname.setCompoundDrawables(null,null,nvDrawable,null);
        }

        if(bean.isStatus()){
            attention_tv.setBackgroundResource(R.drawable.blue_light_15dpconor_bg);
            attention_tv.setText("解除");
            attention_tv.setTextColor(mContext.getResources().getColor(R.color.bluetext_color));
            attention_tv.setVisibility(View.VISIBLE);
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
