package com.lianzai.reader.ui.adapter.teamholder;

import android.text.TextUtils;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.DonutProgress;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

public class IMPictureOutViewHolder extends IMBaseViewHolder {


    public IMPictureOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    SelectableRoundedImageView im_iv_picture;

    DonutProgress dp_progress;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        im_iv_picture=holder.getView(R.id.im_iv_picture);
        dp_progress=holder.getView(R.id.dp_progress);
        dp_progress.setMax(100);


        ImageAttachment imageAttachment=(ImageAttachment)data.getAttachment();
        String path = imageAttachment.getPath();

        if(TextUtils.isEmpty(path)){
            AttachStatusEnum status = data.getAttachStatus();
            switch (status) {
                case def:
                    dp_progress.setVisibility(View.GONE);
                    break;
                case transferring:
                    int progress=(int) teamChatAdapter.getProgress(data)*100;
                    dp_progress.setDonut_progress(String.valueOf(progress));
                    if (progress>=1){
                        dp_progress.setVisibility(View.GONE);
                    }else{
                        dp_progress.setVisibility(View.VISIBLE);
                    }

                    break;
                case transferred:
                    dp_progress.setVisibility(View.GONE);
                    break;
                case fail:
                    dp_progress.setVisibility(View.GONE);
                    break;
            }
        }

        //显示图片
        if (null!=imageAttachment) {
//            RxLogTool.e("IMPictureOutViewHolder getThumbUrl:" + imageAttachment.getThumbUrl());
            if(null!=imageAttachment.getPath()){//本地文件
                RxImageTool.loadImage(holder.getContext(),new File(imageAttachment.getPath()),im_iv_picture);
            }else {
                if (null!=imageAttachment.getThumbUrl())
                    RxImageTool.loadImage(holder.getContext(), imageAttachment.getThumbUrl(), im_iv_picture);
            }
            RxLogTool.e("IMPictureOutViewHolder getPath:" + imageAttachment.getPath());
            RxLogTool.e("IMPictureOutViewHolder getThumbUrl:" + imageAttachment.getUrl());

            im_iv_picture.setOnClickListener(
                    v->teamChatAdapter.getChatItemClickListener().showImageClick(im_iv_picture,position,data)
            );

            im_iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    teamChatAdapter.getChatItemClickListener().contentLongClick(im_iv_picture,position,data);
                    return false;
                }
            });
//            RxImageTool.loadImage(holder.getContext(), imageAttachment.getThumbUrl(), im_iv_picture);
        }
    }

}
