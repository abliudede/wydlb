package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.UserRelaListBean;
import com.lianzai.reader.model.bean.ContactsBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 好友item
 */

public class MyFriendsItemAdapter extends BaseQuickAdapter<UserRelaListBean.DataBean.UserPageListBean.ListBean, BaseViewHolder> {

    Context context;


    int currentPos=1;
    private int type;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public MyFriendsItemAdapter(@Nullable List<UserRelaListBean.DataBean.UserPageListBean.ListBean> data, Context mContext) {
        super(R.layout.item_friend, data);
        this.context = mContext;
    }

    private static ContactsBeanDao getContactsBeanDao() {
        return DaoDbHelper.getInstance().getSession().getContactsBeanDao();
    }

    /**
     * 查询指定用户
     */
    public static List<ContactsBean> SearchContactsBean(String mobile)
    {
        //惰性加载
        List<ContactsBean> list =  getContactsBeanDao().queryBuilder().where(ContactsBeanDao.Properties.Mobile.eq(mobile)).list();
        return list;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UserRelaListBean.DataBean.UserPageListBean.ListBean bean) {


        SelectableRoundedImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_desc = baseViewHolder.getView(R.id.tv_desc);
        TextView tv_amount1 = baseViewHolder.getView(R.id.tv_amount1);
        TextView tv_amount2 = baseViewHolder.getView(R.id.tv_amount2);
        TextView tv_amount3 = baseViewHolder.getView(R.id.tv_amount3);
        TextView click_tv = baseViewHolder.getView(R.id.click_tv);

        RxImageTool.loadLogoImage(context,bean.getHeadImg(),iv_logo);
        tv_desc.setText(bean.getNickName());
        //从数据库查找匹配,并设置上昵称
        List<ContactsBean> list = SearchContactsBean(bean.getMobile());
        if(null != list){
            if(!list.isEmpty()){
                tv_desc.setText(list.get(0).getNick());
            }
        }


        int third_color = context.getResources().getColor(R.color.third_text_color);
        int white = context.getResources().getColor(R.color.white_text_color);


        if(type == 2){
            //唤醒列表样式
            tv_amount1.setVisibility(View.VISIBLE);
            tv_amount2.setVisibility(View.GONE);
            tv_amount3.setVisibility(View.GONE);
            click_tv.setText("唤醒TA");
            click_tv.setOnClickListener(
                    v -> {
                        onFriendItemClickListener.inviteClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
            );
            //0否 (未完成任务),1是(完成待领取),2：(已完成并且已经领取奖励)
            switch (bean.getIsAwaken()){
                case 0:
                    tv_amount1.setText("7天未登录");
                    tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount1.setTextColor(third_color);
                    tv_amount1.setAlpha(1f);
                    break;
                case 1:
                    tv_amount1.setText("");
                    tv_amount1.setBackgroundResource(R.mipmap.get_reward);
                    tv_amount1.setAlpha(1f);
                    tv_amount1.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.awakeClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                            }
                    );
                    startShakeByViewAnim(tv_amount1);
                    break;
                case 2:
                    tv_amount1.setText("已领取奖励");
                    tv_amount1.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    tv_amount1.setTextColor(white);
                    tv_amount1.setAlpha(0.2f);
                    break;

                default:
                    tv_amount1.setText("7天未登录");
                    tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount1.setTextColor(third_color);
                    tv_amount1.setAlpha(1f);
                    break;
            }


        }else{
            //好友列表样式
            tv_amount1.setVisibility(View.VISIBLE);
            tv_amount2.setVisibility(View.VISIBLE);
            tv_amount3.setVisibility(View.VISIBLE);
            click_tv.setText("提醒TA");
            click_tv.setOnClickListener(
                    v -> {
                        onFriendItemClickListener.awakeClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
            );

            //0否 (未完成任务),1是(完成待领取),2：(已完成并且已经领取奖励)
            switch (bean.getIsSign()){
                case 0:
                    tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount1.setTextColor(third_color);
                    tv_amount1.setAlpha(1f);
                    tv_amount1.setText("未签到");
                    break;
                case 1:
                    tv_amount1.setText("");
                    tv_amount1.setBackgroundResource(R.mipmap.get_reward);
                    tv_amount1.setAlpha(1f);
                    tv_amount1.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.signClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                            }
                    );
                    startShakeByViewAnim(tv_amount1);
                    break;
                case 2:
                    tv_amount1.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    tv_amount1.setTextColor(white);
                    tv_amount1.setAlpha(0.2f);
                    tv_amount1.setText("已领取奖励");
                    break;

                default:
                    tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount1.setTextColor(third_color);
                    tv_amount1.setAlpha(1f);
                    tv_amount1.setText("未签到");
                    break;
            }

            switch (bean.getIsRead()){
                case 0:
                    tv_amount2.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount2.setTextColor(third_color);
                    tv_amount2.setAlpha(1f);
                    tv_amount2.setText("未阅读");
                    break;
                case 1:
                    tv_amount2.setText("");
                    tv_amount2.setBackgroundResource(R.mipmap.get_reward);
                    tv_amount2.setAlpha(1f);
                    tv_amount2.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.readClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                            }
                    );
                    startShakeByViewAnim(tv_amount2);
                    break;
                case 2:
                    tv_amount2.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    tv_amount2.setTextColor(white);
                    tv_amount2.setAlpha(0.2f);
                    tv_amount2.setText("已领取奖励");
                    break;

                default:
                    tv_amount2.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount2.setTextColor(third_color);
                    tv_amount2.setAlpha(1f);
                    tv_amount2.setText("未阅读");
                    break;
            }

            switch (bean.getIsInvitationFriends()){
                case 0:

                    tv_amount3.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount3.setTextColor(third_color);
                    tv_amount3.setAlpha(1f);
                    if(bean.getInvitedNumber() == 0) {
                        tv_amount3.setText("未邀请好友");
                    }else {
                        if(context instanceof ActivityTaskCenter){
                            int upLimit = ((ActivityTaskCenter)context).getFragment().getUpLimit();
                            tv_amount3.setText("邀请好友" + bean.getInvitedNumber() + "/" + upLimit);
                        }

                    }
                    break;
                case 1:
                    tv_amount3.setText("");
                    tv_amount3.setBackgroundResource(R.mipmap.get_reward);
                    tv_amount3.setAlpha(1f);
                    tv_amount3.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.inviteClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                            }
                    );
                    startShakeByViewAnim(tv_amount3);
                    break;
                case 2:
                    tv_amount3.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    tv_amount3.setTextColor(white);
                    tv_amount3.setAlpha(0.2f);
                    tv_amount3.setText("已邀请好友");
                    break;

                default:
                    tv_amount3.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    tv_amount3.setTextColor(third_color);
                    tv_amount3.setAlpha(1f);
                    if(bean.getInvitedNumber() == 0) {
                        tv_amount3.setText("未邀请好友");
                    }else {
                       if(context instanceof ActivityTaskCenter){
                            int upLimit = ((ActivityTaskCenter)context).getFragment().getUpLimit();
                            tv_amount3.setText("邀请好友" + bean.getInvitedNumber() + "/" + upLimit);
                        }
                    }
                    break;
            }
        }

    }

    //脉动动画效果
    private void startShakeByViewAnim(View view) {
        if (view == null) {
            return;
        }
        //由小变大
        Animation scaleAnim = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnim.setDuration(700);
        scaleAnim.setRepeatMode(Animation.REVERSE);
        scaleAnim.setRepeatCount(Animation.INFINITE);
        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        view.startAnimation(smallAnimationSet);
    }

    public void setType(int i) {
        this.type = i;
    }

    public interface OnFriendItemClickListener{
        void signClick(int pos);
        void readClick(int pos);
        void inviteClick(int pos);
        void awakeClick(int pos);
    }

    OnFriendItemClickListener onFriendItemClickListener;

    public OnFriendItemClickListener getOnFriendItemClickListener() {
        return onFriendItemClickListener;
    }

    public void setOnFriendItemClickListener(OnFriendItemClickListener onFriendItemClickListener) {
        this.onFriendItemClickListener = onFriendItemClickListener;
    }
}
