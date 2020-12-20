package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.LuckInfoFollowBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxTimeTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 好友item
 */

public class FightLuckPersonItemAdapter extends BaseQuickAdapter<LuckInfoFollowBean.DataBean.ListBean, BaseViewHolder> {

    Context context;


    public FightLuckPersonItemAdapter(@Nullable List<LuckInfoFollowBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_fightluck_person, data);
        this.context = mContext;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,LuckInfoFollowBean.DataBean.ListBean bean) {


        SelectableRoundedImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView time_tv = baseViewHolder.getView(R.id.time_tv);

        RxImageTool.loadLogoImage(context,bean.getHead(),iv_logo);
        tv_nickname.setText(bean.getNickName());
        String timestr = RxTimeTool.milliseconds2HHMMSSString(Long.parseLong(bean.getJoinTime()));
        time_tv.setText(timestr+"参与");

    }


}
