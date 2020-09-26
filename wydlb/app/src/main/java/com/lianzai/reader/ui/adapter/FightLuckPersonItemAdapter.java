package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

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