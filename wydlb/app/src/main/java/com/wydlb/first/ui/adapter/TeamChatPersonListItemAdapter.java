package com.wydlb.first.ui.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.TeamChatInfoBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/04/27.
 */

public class TeamChatPersonListItemAdapter extends BaseQuickAdapter<TeamChatInfoBean.DataBean.UserListBean,BaseViewHolder> {


    Activity mActivity;

    public TeamChatPersonListItemAdapter(@Nullable List<TeamChatInfoBean.DataBean.UserListBean> data, Activity activity) {
        super(R.layout.item_chatroom_person, data);
        this.mActivity=activity;
        mContext=mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, TeamChatInfoBean.DataBean.UserListBean bean) {
        SelectableRoundedImageView ivLogo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_date = baseViewHolder.getView(R.id.tv_date);
        ImageView ivHuangguan = baseViewHolder.getView(R.id.huangguan_iv);
        TextView tv_role = baseViewHolder.getView(R.id.item_quanzi_person_head);


        switch (bean.getRoleType()){
            case Constant.ChatRoomRole.SYSTEM_ACCOUNT:
                ivHuangguan.setVisibility(View.VISIBLE);
                ivHuangguan.setImageResource(R.mipmap.im_chat_room_admin1);
                tv_role.setText("超级管理员");
                break;
            case Constant.ChatRoomRole.HAOZHU_ACCOUNT:
                ivHuangguan.setVisibility(View.VISIBLE);
                ivHuangguan.setImageResource(R.mipmap.im_chat_room_admin1);
                tv_role.setText("号主");
                break;
            case Constant.ChatRoomRole.AUTHOR_ACCOUNT:
                ivHuangguan.setVisibility(View.VISIBLE);
                ivHuangguan.setImageResource(R.mipmap.im_chat_room_admin1);
                tv_role.setText("作者");
                break;

            case Constant.ChatRoomRole.ADMIN_ACCOUNT:
                ivHuangguan.setVisibility(View.VISIBLE);
                ivHuangguan.setImageResource(R.mipmap.im_chat_room_admin2);
                tv_role.setText("管理员");
                break;

            case Constant.ChatRoomRole.NORMAL_ACCOUNT:
                ivHuangguan.setVisibility(View.GONE);
                tv_role.setText("普通成员");
                break;

            case Constant.ChatRoomRole.GUEST_ACCOUNT:
                ivHuangguan.setVisibility(View.GONE);
                tv_role.setText("普通成员");
                break;

            case Constant.ChatRoomRole.BLACK_ACCOUNT:
                ivHuangguan.setVisibility(View.GONE);
                tv_role.setText("被拉黑");
                break;

            case Constant.ChatRoomRole.MUTED_ACCOUNT:
                ivHuangguan.setVisibility(View.GONE);
                tv_role.setText("被禁言");
                break;

            default:
                ivHuangguan.setVisibility(View.GONE);
                tv_role.setText("普通成员");
                break;
        }

        RxImageTool.loadFangLogoImage(mContext, bean.getHead(), ivLogo);
        tv_nickname.setVisibility(View.VISIBLE);
        tv_nickname.setText(bean.getNickName());

    }

}
