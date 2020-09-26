package com.netease.nim.uikit.business.session.actions;

import android.content.Intent;

import com.lianzai.reader.R;
import com.netease.nim.uikit.business.session.constant.RequestCode;

/**
 *
 */
public class BusinessCardAction extends BaseAction {

    public BusinessCardAction() {
        super(R.drawable.nim_message_plus_photo_selector, R.string.send_business_card_name);
    }


    @Override
    public void onClick() {
        //点击调往选择名片页面
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_BUSINESS_CARD) {//选择完名片后返回
        }
    }


}
