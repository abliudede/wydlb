package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.PublicNumberAdapter;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class PublicNumberChapterViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, IMMessage> {

    public PublicNumberChapterViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    protected TextView tv_timeline;
    protected HeadImageView iv_book_cover;
    protected TextView tv_chapter_title;
    protected TextView tv_chapter_detail;
    protected TextView tv_open_current_chapter;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        tv_timeline=holder.getView(R.id.tv_timeline);
        iv_book_cover=holder.getView(R.id.iv_book_cover);
        tv_chapter_title=holder.getView(R.id.tv_chapter_title);

        tv_chapter_detail=holder.getView(R.id.tv_chapter_detail);
        tv_open_current_chapter=holder.getView(R.id.tv_open_current_chapter);

        String timeString = TimeUtil.getTimeShowString(data.getTime(), true);
        tv_timeline.setText(timeString);

        iv_book_cover.loadBuddyAvatar(data.getSessionId());

        if (null!=data.getAttachment()&&data.getAttachment() instanceof PublicNumberAttachment) {
            PublicNumberAttachment attachment = (PublicNumberAttachment) data.getAttachment();

            if (null != attachment.getPublicNumberBean()) {
                tv_chapter_title.setText("章节有更新");
                tv_chapter_detail.setText(attachment.getPublicNumberBean().getTitle() + " " + attachment.getPublicNumberBean().getContent());
                tv_open_current_chapter.setOnClickListener(
                        v -> {
                            //打开指定章节
                            ((PublicNumberAdapter) getAdapter()).getPublicNumberOptionCallback().openChapterByChapterTitle(attachment.getPublicNumberBean().getBookId(), attachment.getPublicNumberBean().getTitle());

                        }
                );
                holder.getConvertView().setOnClickListener(
                        v -> {
                            //打开指定章节
                            ((PublicNumberAdapter) getAdapter()).getPublicNumberOptionCallback().openChapterByChapterTitle(attachment.getPublicNumberBean().getBookId(), attachment.getPublicNumberBean().getTitle());
                        }
                );
            }

        }
    }


}
