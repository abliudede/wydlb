package com.lianzai.reader.ui.adapter.holder;

import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.chat.ChatRoomActivity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class PublicNumberOpenChatRoomViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, IMMessage> {

    public PublicNumberOpenChatRoomViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    protected TextView tv_timeline;
    protected TextView tv_chapter_title;
    protected TextView tv_chapter_detail;
    protected TextView tv_open_current_chapter;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        tv_timeline=holder.getView(R.id.tv_timeline);
        tv_chapter_title=holder.getView(R.id.tv_chapter_title);

        tv_chapter_detail=holder.getView(R.id.tv_chapter_detail);
        tv_open_current_chapter=holder.getView(R.id.tv_open_current_chapter);

        String timeString = TimeUtil.getTimeShowString(data.getTime(), true);
        tv_timeline.setText(timeString);

        if (null!=data.getAttachment()&&data.getAttachment() instanceof PublicNumberAttachment){
            PublicNumberAttachment attachment=(PublicNumberAttachment)data.getAttachment();

            if(null != attachment.getPublicNumberBean()) {
                tv_chapter_detail.setText(attachment.getPublicNumberBean().getContent());
                tv_open_current_chapter.setOnClickListener(
                        v -> {
//                            ChatRoomActivity.startActivity(holder.getContext(), attachment.getPublicNumberBean().getBookId());
                        }
                );
                holder.getConvertView().setOnClickListener(
                        v -> {
//                            ChatRoomActivity.startActivity(holder.getContext(), attachment.getPublicNumberBean().getBookId());
                        }
                );
            }
        }

    }


}
