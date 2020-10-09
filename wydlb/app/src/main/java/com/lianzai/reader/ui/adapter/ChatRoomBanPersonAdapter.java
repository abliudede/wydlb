package com.lianzai.reader.ui.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.ChatRoomBanPersonList;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/04/27.
 */

public class ChatRoomBanPersonAdapter extends BaseQuickAdapter<ChatRoomBanPersonList.DataBean,BaseViewHolder> {


    Activity mActivity;
    private String mtype;
    private View.OnClickListener mlistener;
    private OnEXItemClickListener onEXItemClickListener;

    public ChatRoomBanPersonAdapter(@Nullable List<ChatRoomBanPersonList.DataBean> data, Activity activity) {
        super(R.layout.item_ban_person, data);
        this.mActivity=activity;
        mContext=mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ChatRoomBanPersonList.DataBean bean) {
        SelectableRoundedImageView iv_user_head = baseViewHolder.getView(R.id.iv_user_head);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);
        TextView tv_toupiao = baseViewHolder.getView(R.id.tv_toupiao);

        RxImageTool.loadFangLogoImage(mContext, bean.getHead(), iv_user_head);
        tv_nickname.setVisibility(View.VISIBLE);
        tv_nickname.setText(bean.getNickName());
        if("jin".equals(mtype)){
            tv_des.setText("已被永久禁言");
        }else if("hei".equals(mtype)){
            tv_des.setText("已被永久拉黑");
        }

        tv_toupiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getOnEXItemClickListener().jiechuClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }
        });

    }

    public OnEXItemClickListener getOnEXItemClickListener() {
        return onEXItemClickListener;
    }

    public void setOnEXItemClickListener(OnEXItemClickListener onEXItemClickListener) {
        this.onEXItemClickListener = onEXItemClickListener;
    }

    public interface OnEXItemClickListener{
        void jiechuClick(int pos);
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }
}
