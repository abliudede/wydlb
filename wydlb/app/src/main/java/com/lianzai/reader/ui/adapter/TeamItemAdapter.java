package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 已加入群item
 */

public class TeamItemAdapter extends BaseQuickAdapter<Team, BaseViewHolder> {

    Context context;


    public TeamItemAdapter(@Nullable List<Team> data, Context mContext) {
        super(R.layout.item_team_lianzaihao, data);
        this.context = mContext;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,Team bean) {

        SelectableRoundedImageView iv_user_head = baseViewHolder.getView(R.id.iv_user_head);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);

        RxImageTool.loadLogoImage(context,bean.getIcon(),iv_user_head);
        tv_nickname.setText(bean.getName());

    }


}
