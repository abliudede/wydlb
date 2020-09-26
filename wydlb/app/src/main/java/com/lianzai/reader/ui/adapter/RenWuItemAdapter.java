package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.model.bean.ContactsBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 任务列表
 */

public class RenWuItemAdapter extends BaseQuickAdapter<TaskCenterBean.DataBean.DailyTaskListBean, BaseViewHolder> {

    Context context;


    int currentPos=1;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public RenWuItemAdapter(@Nullable List<TaskCenterBean.DataBean.DailyTaskListBean> data, Context mContext) {
        super(R.layout.item_renwu, data);
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
    protected void convert(final BaseViewHolder baseViewHolder,TaskCenterBean.DataBean.DailyTaskListBean bean) {


        ImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_desc = baseViewHolder.getView(R.id.tv_desc);
        TextView tv_amount1 = baseViewHolder.getView(R.id.tv_amount1);
        TextView tv_amount2 = baseViewHolder.getView(R.id.tv_amount2);
        TextView tv_amount3 = baseViewHolder.getView(R.id.tv_amount3);
        ImageView zhankai_iv = baseViewHolder.getView(R.id.zhankai_iv);
        TextView click_tv = baseViewHolder.getView(R.id.click_tv);
        TextView more_tv = baseViewHolder.getView(R.id.more_tv);

        RxImageTool.loadLogoImage(context,bean.getTaskImg(),iv_logo);
        tv_desc.setText(bean.getTaskName());
        if(TextUtils.isEmpty(bean.getTaskIntro())){
            zhankai_iv.setVisibility(View.GONE);
        }else{
            zhankai_iv.setVisibility(View.VISIBLE);
            zhankai_iv.setOnClickListener(
                    v -> {
                        more_tv.setText(bean.getTaskIntro());
                        more_tv.setVisibility(View.VISIBLE);
                    }
            );
        }

        if(bean.getRewardStatus() == 2){
            //未完成状态
            click_tv.setText(bean.getUnfinishedStatusDesc());
            click_tv.setBackgroundResource(R.drawable.blue_15dpconor_bg);
        }else if(bean.getRewardStatus() == 0){
            //已完成状态
            click_tv.setText("领取奖励");
            click_tv.setBackgroundResource(R.drawable.jianbian_chengse_corner_renwu);
        }else{
            //已领取奖励
            click_tv.setText("已领取");
            click_tv.setBackgroundResource(R.drawable.jianbian_chengse_corner_renwu);
        }


        List<TaskCenterBean.DataBean.RewardListBean> listtemp = bean.getRewardList();
        tv_amount1.setVisibility(View.GONE);
        tv_amount2.setVisibility(View.GONE);
        tv_amount3.setVisibility(View.GONE);
        if(null == listtemp){
        }else if(listtemp.isEmpty()){
        }else{
            Drawable drawableJinZuan= RxTool.getContext().getResources().getDrawable(R.mipmap.jinzuan_little);
            drawableJinZuan.setBounds(0,0,drawableJinZuan.getIntrinsicWidth(),drawableJinZuan.getIntrinsicHeight());
            Drawable drawableJinBi= RxTool.getContext().getResources().getDrawable(R.mipmap.jinbi_little);
            drawableJinBi.setBounds(0,0,drawableJinBi.getIntrinsicWidth(),drawableJinBi.getIntrinsicHeight());
            Drawable drawableYueJuan= RxTool.getContext().getResources().getDrawable(R.mipmap.yuejuan_little);
            drawableYueJuan.setBounds(0,0,drawableYueJuan.getIntrinsicWidth(),drawableYueJuan.getIntrinsicHeight());

            for(int i = 0; i < listtemp.size() ; i ++) {
                if(listtemp.get(i).getRewardAmount() > 0){
                switch (i) {
                    case 0:
                        tv_amount1.setVisibility(View.VISIBLE);
                        tv_amount1.setText(String.valueOf((int)listtemp.get(i).getRewardAmount()));
                        if (listtemp.get(i).getRewardType() == 20000) {
                            tv_amount1.setCompoundDrawables(drawableJinZuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20001) {
                            tv_amount1.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20002) {
                            tv_amount1.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20003) {
                            tv_amount1.setCompoundDrawables(drawableJinBi, null, null, null);
                        }
                        break;
                    case 1:
                        tv_amount2.setVisibility(View.VISIBLE);
                        tv_amount2.setText(String.valueOf((int)listtemp.get(i).getRewardAmount()));
                        if (listtemp.get(i).getRewardType() == 20000) {
                            tv_amount2.setCompoundDrawables(drawableJinZuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20001) {
                            tv_amount2.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20002) {
                            tv_amount2.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20003) {
                            tv_amount2.setCompoundDrawables(drawableJinBi, null, null, null);
                        }
                        break;
                    case 2:
                        tv_amount3.setVisibility(View.VISIBLE);
                        tv_amount3.setText(String.valueOf((int)listtemp.get(i).getRewardAmount()));
                        if (listtemp.get(i).getRewardType() == 20000) {
                            tv_amount3.setCompoundDrawables(drawableJinZuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20001) {
                            tv_amount3.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20002) {
                            tv_amount3.setCompoundDrawables(drawableYueJuan, null, null, null);
                        } else if (listtemp.get(i).getRewardType() == 20003) {
                            tv_amount3.setCompoundDrawables(drawableJinBi, null, null, null);
                        }
                        break;
                }
            }
            }
        }

        //跳转逻辑
        //会话页
//        NimUIKit.startTeamSession(getActivity(), recent.getContactId());
        //搜索页面
//        RxActivityTool.skipActivity(mContext, ActivitySearch.class);
        //书单详情页
//        ActivityBookListDetail.startActivity(getContext(),String.valueOf(bookListGridAdapter.getData().get(position).getId()));
        //阅读喜好跳转
//        RxActivityTool.skipActivity(this,BookRecommendSettingActivity.class);
        click_tv.setOnClickListener(
                v -> {
                    if(bean.getRewardStatus() == 2){
                        //跳转到不同页面
                        getOnFriendItemClickListener().goClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }else{
                        //已完成状态，领取任务奖励
                        getOnFriendItemClickListener().getClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                }
        );




    }

    //脉动动画效果
    private void startShakeByViewAnim(View view) {
        if (view == null) {
            return;
        }
        //由小变大
        Animation scaleAnim = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnim.setDuration(700);
        scaleAnim.setRepeatMode(Animation.REVERSE);
        scaleAnim.setRepeatCount(Animation.INFINITE);
        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        view.startAnimation(smallAnimationSet);
    }

    public interface OnRenWuClick{
        void getClick(int pos);
        void goClick(int pos);
    }

    OnRenWuClick onClickListener;

    public OnRenWuClick getOnFriendItemClickListener() {
        return onClickListener;
    }

    public void setOnFriendItemClickListener(OnRenWuClick onFriendItemClickListener) {
        onClickListener = onFriendItemClickListener;
    }
}
