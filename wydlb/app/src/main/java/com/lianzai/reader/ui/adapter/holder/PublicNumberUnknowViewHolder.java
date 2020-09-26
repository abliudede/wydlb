package com.lianzai.reader.ui.adapter.holder;

import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.PublicNumberAdapter;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class PublicNumberUnknowViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, IMMessage> {

    public PublicNumberUnknowViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    protected TextView tv_timeline;
    protected HeadImageView iv_logo;
    protected TextView tv_comment;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        tv_timeline=holder.getView(R.id.tv_timeline);
        iv_logo=holder.getView(R.id.iv_logo);
        tv_comment=holder.getView(R.id.tv_comment);

        String timeString = TimeUtil.getTimeShowString(data.getTime(), true);
        tv_timeline.setText(timeString);
//        tv_comment.setText(data.getContent());
        iv_logo.loadBuddyAvatar(data.getSessionId());

        iv_logo.setOnClickListener(
                v->((PublicNumberAdapter)getAdapter()).getPublicNumberOptionCallback().avatarClick()
        );
    }

}
