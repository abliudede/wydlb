package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.DonutProgress;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;

import java.io.File;

public class IMPictureInViewHolder extends IMBaseViewHolder{

    public IMPictureInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        chatRoomAdapter=(ChatRoomAdapter) adapter;
    }

    TextView im_tv_nickname;
    SelectableRoundedImageView im_iv_picture;
    DonutProgress dp_progress;

    ChatRoomAdapter chatRoomAdapter;
    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        im_tv_nickname=holder.getView(R.id.im_tv_nickname);
        im_iv_picture=holder.getView(R.id.im_iv_picture);

        dp_progress=holder.getView(R.id.dp_progress);
        dp_progress.setMax(100);

        ImageAttachment imageAttachment=(ImageAttachment)data.getAttachment();

        //显示图片
        if (null!=imageAttachment) {
//            RxLogTool.e("IMPictureOutViewHolder getThumbUrl:" + imageAttachment.getThumbUrl());
            if(null!=imageAttachment.getPath()){//本地文件
                RxImageTool.loadImage(holder.getContext(),new File(imageAttachment.getPath()),im_iv_picture);
                int progress=(int) chatRoomAdapter.getProgress(data)*100;
                RxLogTool.e("IMPictureOutViewHolder --progress:"+progress);

                if (null!=dp_progress){
                    dp_progress.setDonut_progress(String.valueOf(progress));
                    dp_progress.setProgress(progress);
                    if (progress>=100){
                        dp_progress.setVisibility(View.GONE);
                    }else{
                        dp_progress.setVisibility(View.VISIBLE);
                    }
                }


            }else {
                if (null!=dp_progress){
                    dp_progress.setVisibility(View.GONE);
                }

                RxLogTool.e("pic size width:"+imageAttachment.getWidth()+"--height-"+imageAttachment.getHeight());

                if (null!=imageAttachment.getThumbUrl()) {
                    RxImageTool.loadImage(holder.getContext(), imageAttachment.getThumbUrl(), im_iv_picture);
                }

            }
            RxLogTool.e("IMPictureOutViewHolder getPath:" + imageAttachment.getPath());
            RxLogTool.e("IMPictureOutViewHolder getThumbUrl:" + imageAttachment.getUrl());
//            RxImageTool.loadImage(holder.getContext(), imageAttachment.getThumbUrl(), im_iv_picture);

            im_iv_picture.setOnClickListener(
                    v->chatRoomAdapter.getChatItemClickListener().showImageClick(im_iv_picture,position,data)
            );

            im_iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    chatRoomAdapter.getChatItemClickListener().contentLongClick(im_iv_picture,position,data);
                    return false;
                }
            });
        }

        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data))) {
            ChatRoomViewHolderHelper.setNameText(data,im_tv_nickname,roleType,holder.getContext());
        }



    }

}
