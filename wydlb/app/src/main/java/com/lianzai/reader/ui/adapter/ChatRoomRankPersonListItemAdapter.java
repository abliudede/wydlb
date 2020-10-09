package com.lianzai.reader.ui.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.ChatRoomPersonListBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/04/27.
 */

public class ChatRoomRankPersonListItemAdapter extends BaseQuickAdapter<ChatRoomPersonListBean.DataBean.NormalListBean,BaseViewHolder> {


    Activity mActivity;

    public ChatRoomRankPersonListItemAdapter(@Nullable List<ChatRoomPersonListBean.DataBean.NormalListBean> data, Activity activity) {
        super(R.layout.item_rank_person, data);
        this.mActivity=activity;
        mContext=mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ChatRoomPersonListBean.DataBean.NormalListBean bean) {

        ImageView iv_rank_num = baseViewHolder.getView(R.id.iv_rank_num);
        TextView tv_rank_num = baseViewHolder.getView(R.id.tv_rank_num);
        SelectableRoundedImageView iv_user_head = baseViewHolder.getView(R.id.iv_user_head);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);


        switch (baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            case 0:
                iv_rank_num.setVisibility(View.VISIBLE);
                iv_rank_num.setImageResource(R.mipmap.diyiming);
                tv_rank_num.setVisibility(View.GONE);
                break;
            case 1:
                iv_rank_num.setVisibility(View.VISIBLE);
                iv_rank_num.setImageResource(R.mipmap.dierming);
                tv_rank_num.setVisibility(View.GONE);
                break;

            case 2:
                iv_rank_num.setVisibility(View.VISIBLE);
                iv_rank_num.setImageResource(R.mipmap.disanming);
                tv_rank_num.setVisibility(View.GONE);
                break;

                default:
                    iv_rank_num.setVisibility(View.GONE);
                    tv_rank_num.setVisibility(View.VISIBLE);
                    tv_rank_num.setText(String.valueOf(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount() + 1));
                    break;

        }


        RxImageTool.loadFangLogoImage(mContext, bean.getHead(), iv_user_head);
        tv_nickname.setText(bean.getNickName());
        tv_des.setText("活跃度：" + bean.getActiveness());



//        tv_nickname.setVisibility(View.VISIBLE);
//        tv_date.setVisibility(View.VISIBLE);
//        RxImageTool.loadLogoImage(mContext, bean.getHeadPic(), ivLogo);
//
//        tv_head.setVisibility(View.GONE);
//        int i = 0;
//        for( ; i < mData.size() ; i++){
//           if(mData.get(i).getRole() == 0) {
//               break;
//           }
//        }
//
//
//
//        switch (bean.getRole()){
//            case 3:
//                ivHuangguan.setVisibility(View.VISIBLE);
//                ivHuangguan.setImageResource(R.mipmap.payicon_choose);
//                tv_des.setVisibility(View.GONE);
//                break;
//            case 1:
//                ivHuangguan.setVisibility(View.VISIBLE);
//                ivHuangguan.setImageResource(R.mipmap.payicon_choose);
//                tv_des.setVisibility(View.VISIBLE);
//                tv_des.setText("嘉宾");
//                break;
//            case 4:
//                ivHuangguan.setVisibility(View.VISIBLE);
//                ivHuangguan.setImageResource(R.mipmap.payicon_choose);
//                tv_des.setVisibility(View.VISIBLE);
//                tv_des.setText("管理员、嘉宾");
//                break;
//            case 2:
//                ivHuangguan.setVisibility(View.GONE);
//                tv_des.setVisibility(View.VISIBLE);
//                tv_des.setText("管理员");
//                break;
//            case 0:
//                ivHuangguan.setVisibility(View.GONE);
//                tv_des.setVisibility(View.GONE);
//                break;
//        }

        //标签是否显示
//        if(bean.getRole() == 3){
//            tv_head.setVisibility(View.VISIBLE);
//            tv_head.setText(mActivity.getResources().getString(R.string.圈主));
//        }else if((bean.getRole() == 2 || bean.getRole() == 1 || bean.getRole() == 4 ) && baseViewHolder.getAdapterPosition() - getHeaderLayoutCount() == 1){
//            tv_head.setVisibility(View.VISIBLE);
//            tv_head.setText(mActivity.getResources().getString(R.string.管理员和嘉宾));
//        }else if(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount() == i){
//            tv_head.setVisibility(View.VISIBLE);
//            tv_head.setText(mActivity.getResources().getString(R.string.成员));
//        }


    }

}
