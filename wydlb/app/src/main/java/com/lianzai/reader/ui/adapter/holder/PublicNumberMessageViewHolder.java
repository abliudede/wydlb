package com.lianzai.reader.ui.adapter.holder;

import android.widget.TextView;

import com.lianzai.reader.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class PublicNumberMessageViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, IMMessage> {

    public PublicNumberMessageViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    protected TextView tv_timeline;
    protected TextView tv_title;
    protected TextView tv_content;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        tv_timeline=holder.getView(R.id.tv_timeline);
        tv_title=holder.getView(R.id.tv_title);

        tv_content=holder.getView(R.id.tv_content);

        String timeString = TimeUtil.getTimeShowString(data.getTime(), true);
        tv_timeline.setText(timeString);

        if (null!=data.getAttachment()&&data.getAttachment() instanceof PublicNumberAttachment){
            PublicNumberAttachment attachment=(PublicNumberAttachment)data.getAttachment();
            if(null != attachment.getPublicNumberBean()) {
                tv_title.setText(attachment.getPublicNumberBean().getTitle());
                tv_content.setText(attachment.getPublicNumberBean().getContent());
            }
        }
    }

}
