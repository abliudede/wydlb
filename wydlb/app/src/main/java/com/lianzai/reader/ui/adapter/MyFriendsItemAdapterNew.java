package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

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

public class MyFriendsItemAdapterNew extends RecyclerView.Adapter<MyFriendsItemAdapterNew.ViewHolder> {

    Context context;
    protected List<UserRelaListBean.DataBean.UserPageListBean.ListBean> mDatas;
    protected LayoutInflater mInflater;

    int currentPos=1;
    private int type;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public MyFriendsItemAdapterNew(List<UserRelaListBean.DataBean.UserPageListBean.ListBean> data, Context mContext) {
        this.mDatas = data;
        this.context = mContext;
        mInflater = LayoutInflater.from(mContext);
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_friend, parent, false));
    }

    public List<UserRelaListBean.DataBean.UserPageListBean.ListBean> getData() {
        return mDatas;
    }

    public void replaceData(List<UserRelaListBean.DataBean.UserPageListBean.ListBean> booksBeanList4,Boolean isRefresh,int count) {
        if(isRefresh) {
            //刷新时，直接全部重置
            notifyDataSetChanged();
        }else{
            //加载更多时，一个个增加
            int size = mDatas.size();
            for(int i = 1 ; i <= count ; i++){
                notifyItemChanged(size-i);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SelectableRoundedImageView iv_logo;
        TextView tv_desc;
        TextView tv_amount1;
        TextView tv_amount2;
        TextView tv_amount3;
        TextView click_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_logo = itemView.findViewById(R.id.iv_logo);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_amount1 = itemView.findViewById(R.id.tv_amount1);
            tv_amount2 = itemView.findViewById(R.id.tv_amount2);
            tv_amount3 = itemView.findViewById(R.id.tv_amount3);
            click_tv = itemView.findViewById(R.id.click_tv);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRelaListBean.DataBean.UserPageListBean.ListBean bean = mDatas.get(position);
        RxImageTool.loadLogoImage(context,bean.getHeadImg(),holder.iv_logo);

        //头像点击
        holder.iv_logo.setOnClickListener(
                v -> {
                    onFriendItemClickListener.avatorClick(position);
                }
        );

        holder.tv_desc.setText(bean.getNickName());
        //从数据库查找匹配,并设置上昵称
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
        //进行自己的操作
//                List<ContactsBean> list = SearchContactsBean(bean.getMobile());
//                if(null != list){
//                    if(!list.isEmpty()){
//                        holder.tv_desc.setText(list.get(0).getNick());
//                    }
//                }
//            }
//        }.start();



        int third_color = context.getResources().getColor(R.color.third_text_color);
        int white = context.getResources().getColor(R.color.white_text_color);


        if(type == 2){
            //唤醒列表样式
            holder.tv_amount1.setVisibility(View.VISIBLE);
            holder.tv_amount2.setVisibility(View.GONE);
            holder.tv_amount3.setVisibility(View.GONE);
            holder.click_tv.setText("唤醒TA");
            holder.click_tv.setOnClickListener(
                    v -> {
                        onFriendItemClickListener.inviteClick(position);
                    }
            );
            //0否 (未完成任务),1是(完成待领取),2：(已完成并且已经领取奖励)
            switch (bean.getIsAwaken()){
                case 0:
                    holder.tv_amount1.setText("7天未登录");
                    holder.tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount1.setTextColor(third_color);
                    holder.tv_amount1.setAlpha(1f);
                    break;
                case 1:
                    holder.tv_amount1.setText("");
                    holder.tv_amount1.setBackgroundResource(R.mipmap.get_reward);
                    holder.tv_amount1.setAlpha(1f);
                    holder.tv_amount1.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.awakeClick(position);
                            }
                    );
                    startShakeByViewAnim(holder.tv_amount1);
                    break;
                case 2:
                    holder.tv_amount1.setText("已领取奖励");
                    holder.tv_amount1.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    holder.tv_amount1.setTextColor(white);
                    holder.tv_amount1.setAlpha(0.2f);
                    break;

                default:
                    holder.tv_amount1.setText("7天未登录");
                    holder.tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount1.setTextColor(third_color);
                    holder.tv_amount1.setAlpha(1f);
                    break;
            }


        }else{
            //好友列表样式
            holder.tv_amount1.setVisibility(View.VISIBLE);
            holder.tv_amount2.setVisibility(View.VISIBLE);
            holder.tv_amount3.setVisibility(View.VISIBLE);
            holder.click_tv.setText("提醒TA");
            holder.click_tv.setOnClickListener(
                    v -> {
                        onFriendItemClickListener.awakeClick(position);
                    }
            );

            //0否 (未完成任务),1是(完成待领取),2：(已完成并且已经领取奖励)
            switch (bean.getIsSign()){
                case 0:
                    holder.tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount1.setTextColor(third_color);
                    holder.tv_amount1.setAlpha(1f);
                    holder.tv_amount1.setText("未签到");
                    break;
                case 1:
                    holder.tv_amount1.setText("");
                    holder.tv_amount1.setBackgroundResource(R.mipmap.get_reward);
                    holder.tv_amount1.setAlpha(1f);
                    holder.tv_amount1.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.signClick(position);
                            }
                    );
                    startShakeByViewAnim(holder.tv_amount1);
                    break;
                case 2:
                    holder.tv_amount1.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    holder.tv_amount1.setTextColor(white);
                    holder.tv_amount1.setAlpha(0.2f);
                    holder.tv_amount1.setText("已领取奖励");
                    break;

                default:
                    holder.tv_amount1.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount1.setTextColor(third_color);
                    holder.tv_amount1.setAlpha(1f);
                    holder.tv_amount1.setText("未签到");
                    break;
            }

            switch (bean.getIsRead()){
                case 0:
                    holder.tv_amount2.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount2.setTextColor(third_color);
                    holder.tv_amount2.setAlpha(1f);
                    holder.tv_amount2.setText("未阅读");
                    break;
                case 1:
                    holder.tv_amount2.setText("");
                    holder.tv_amount2.setBackgroundResource(R.mipmap.get_reward);
                    holder.tv_amount2.setAlpha(1f);
                    holder.tv_amount2.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.readClick(position);
                            }
                    );
                    startShakeByViewAnim(holder.tv_amount2);
                    break;
                case 2:
                    holder.tv_amount2.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    holder.tv_amount2.setTextColor(white);
                    holder.tv_amount2.setAlpha(0.2f);
                    holder.tv_amount2.setText("已领取奖励");
                    break;

                default:
                    holder.tv_amount2.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount2.setTextColor(third_color);
                    holder.tv_amount2.setAlpha(1f);
                    holder.tv_amount2.setText("未阅读");
                    break;
            }

            switch (bean.getIsInvitationFriends()){
                case 0:

                    holder.tv_amount3.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount3.setTextColor(third_color);
                    holder.tv_amount3.setAlpha(1f);
                    if(bean.getInvitedNumber() == 0) {
                        holder.tv_amount3.setText("未邀请好友");
                    }else {
                       if(context instanceof ActivityTaskCenter){
                           int upLimit = 5;
                           try {
                               upLimit = ((ActivityTaskCenter) context).getFragment().getUpLimit();
                           }catch (Exception e){}
                            holder.tv_amount3.setText("邀请好友" + bean.getInvitedNumber() + "/" + upLimit);
                        }
                    }
                    break;
                case 1:
                    holder.tv_amount3.setText("");
                    holder.tv_amount3.setBackgroundResource(R.mipmap.get_reward);
                    holder.tv_amount3.setAlpha(1f);
                    holder.tv_amount3.setOnClickListener(
                            v -> {
                                onFriendItemClickListener.inviteClick(position);
                            }
                    );
                    startShakeByViewAnim(holder.tv_amount3);
                    break;
                case 2:
                    holder.tv_amount3.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_9dp);
                    holder.tv_amount3.setTextColor(white);
                    holder.tv_amount3.setAlpha(0.2f);
                    holder.tv_amount3.setText("已邀请好友");
                    break;

                default:
                    holder.tv_amount3.setBackgroundResource(R.drawable.f5f5f5_corner_card);
                    holder.tv_amount3.setTextColor(third_color);
                    holder.tv_amount3.setAlpha(1f);
                    if(bean.getInvitedNumber() == 0) {
                        holder.tv_amount3.setText("未邀请好友");
                    }else {
                        if(context instanceof ActivityTaskCenter){
                            int upLimit = 5;
                            try {
                                upLimit = ((ActivityTaskCenter) context).getFragment().getUpLimit();
                            }catch (Exception e){}
                            holder.tv_amount3.setText("邀请好友" + bean.getInvitedNumber() + "/" + upLimit);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public interface OnFriendItemClickListener{
        void signClick(int pos);
        void readClick(int pos);
        void inviteClick(int pos);
        void awakeClick(int pos);
        void avatorClick(int pos);
    }

    OnFriendItemClickListener onFriendItemClickListener;

    public OnFriendItemClickListener getOnFriendItemClickListener() {
        return onFriendItemClickListener;
    }

    public void setOnFriendItemClickListener(OnFriendItemClickListener onFriendItemClickListener) {
        this.onFriendItemClickListener = onFriendItemClickListener;
    }
}
