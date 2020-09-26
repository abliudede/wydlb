package com.lianzai.reader.ui.adapter.teamholder;

import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMUrlBookInViewHolder extends IMBaseViewHolder {

    TextView im_tv_nickname;

    TextView im_tv_lianzaihao_name;
    TextView im_tv_lianzaihao_penname;
    SelectableRoundedImageView im_iv_lianozaihao_cover;

    RelativeLayout rl_content;

    public IMUrlBookInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_nickname=holder.getView(R.id.im_tv_nickname);

        im_tv_lianzaihao_name=holder.getView(R.id.im_tv_lianzaihao_name);
        im_tv_lianzaihao_penname=holder.getView(R.id.im_tv_lianzaihao_penname);
        im_iv_lianozaihao_cover=holder.getView(R.id.im_iv_lianozaihao_cover);

        rl_content=holder.getView(R.id.rl_content);

        String nickName = TeamHelper.getTeamMemberDisplayName(data.getSessionId(), data.getFromAccount());
        im_tv_nickname.setText(nickName);

        if (null!=data.getAttachment()){
            UrlBookBean urlBookBean=(UrlBookBean)data.getAttachment();
            if(!TextUtils.isEmpty(urlBookBean.getPlatformName()))
                im_tv_lianzaihao_name.setText(urlBookBean.getPlatformName());
            if(!TextUtils.isEmpty(urlBookBean.getUrl())) {
                im_tv_lianzaihao_penname.setText(urlBookBean.getUrl());
            } else {
                im_tv_lianzaihao_penname.setText(urlBookBean.getIntro());
            }
            if(!TextUtils.isEmpty(urlBookBean.getPlatformCover()))
                RxImageTool.loadImage(teamChatAdapter.getContext(),urlBookBean.getPlatformCover(),im_iv_lianozaihao_cover);

            rl_content.setOnClickListener(
                    v->{
                        if (urlBookBean.getBookId() != 0){
                            int bookId = urlBookBean.getBookId();
                            if(bookId > Constant.bookIdLine){
                                //包含网址的消息直接跳往网页，不包含的则只能跳到阅读页，兼容老数据
                                if(TextUtils.isEmpty(urlBookBean.getUrl())){
                                    SkipReadUtil.normalRead(teamChatAdapter.getContext(),String.valueOf(bookId),"",false);
                                }else {
                                    ActivityWebView.startActivityForReadNormal(teamChatAdapter.getContext(),urlBookBean.getUrl(),String.valueOf(urlBookBean.getBookId()),"",false);
                                }
                            }else {
                                SkipReadUtil.normalRead(teamChatAdapter.getContext(),String.valueOf(bookId),"",false);
                            }
                        }
                    }
            );
        }


    }

}
