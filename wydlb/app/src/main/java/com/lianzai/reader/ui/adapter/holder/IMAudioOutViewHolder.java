package com.lianzai.reader.ui.adapter.holder;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.common.media.audioplayer.Playable;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMAudioOutViewHolder extends IMBaseViewHolder {

    public static final int CLICK_TO_PLAY_AUDIO_DELAY = 500;

    ImageView iv_audio_img;
    TextView tv_audio_time;


    RelativeLayout rl_content;

    ChatRoomAdapter chatRoomAdapter;

    MessageAudioControl audioControl;

    ChatRoomMessage message;

    public IMAudioOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        chatRoomAdapter=(ChatRoomAdapter)adapter;
        audioControl = MessageAudioControl.getInstance(chatRoomAdapter.getContext());
    }


    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        iv_audio_img=holder.getView(R.id.iv_audio_img);
        tv_audio_time=holder.getView(R.id.tv_audio_time);
        rl_content=holder.getView(R.id.rl_content);
        message=data;

        //初始化时间
        controlPlaying();


        rl_content.setOnClickListener(
                v->{
                    if (audioControl != null) {
                        initPlayAnim(); // 设置语音播放动画

                        audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, message, onPlayListener);

                        audioControl.setPlayNext(!NimUIKitImpl.getOptions().disableAutoPlayNextAudio, chatRoomAdapter, message);
                    }
                }
        );

        rl_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                chatRoomAdapter.getChatItemClickListener().contentLongClick(rl_content,position,data);
                return false;
            }
        });
    }


    private void controlPlaying() {
        final AudioAttachment msgAttachment = (AudioAttachment) message.getAttachment();
        long duration = msgAttachment.getDuration();

        tv_audio_time.setTag(message.getUuid());

        if (!isMessagePlaying(audioControl, message)) {
            if (audioControl.getAudioControlListener() != null &&
                    audioControl.getAudioControlListener().equals(onPlayListener)) {
                audioControl.changeAudioControlListener(null);
            }

            updateTime(duration);
            stop();
        } else {
            audioControl.changeAudioControlListener(onPlayListener);
            play();
        }
    }

    private void updateTime(long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);

        if (seconds >= 0) {
            tv_audio_time.setText(seconds + "\"");
        } else {
            tv_audio_time.setText("");
        }
    }

    protected boolean isMessagePlaying(MessageAudioControl audioControl, IMMessage message) {
        if (audioControl.getPlayingAudio() != null && audioControl.getPlayingAudio().isTheSame(message)) {
            return true;
        } else {
            return false;
        }
    }

    private MessageAudioControl.AudioControlListener onPlayListener = new MessageAudioControl.AudioControlListener() {

        @Override
        public void updatePlayingProgress(Playable playable, long curPosition) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            if (curPosition > playable.getDuration()) {
                return;
            }
            updateTime(curPosition);
        }

        @Override
        public void onAudioControllerReady(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            play();
        }

        @Override
        public void onEndPlay(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            updateTime(playable.getDuration());

            stop();
        }


    };

    private void play() {
        if (iv_audio_img.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) iv_audio_img.getBackground();
            animation.start();
        }
    }

    private void stop() {
        if (iv_audio_img.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) iv_audio_img.getBackground();
            animation.stop();

            endPlayAnim();
        }
    }

    private void initPlayAnim() {
        iv_audio_img.setBackgroundResource(R.drawable.play_audio_animation_out);
    }

    private void endPlayAnim() {
        iv_audio_img.setBackgroundResource(R.drawable.out_play_audio2);
    }

    private boolean isTheSame(String uuid) {
        String current = tv_audio_time.getTag().toString();
        return !TextUtils.isEmpty(uuid) && uuid.equals(current);
    }

}
