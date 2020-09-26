package com.lianzai.reader.view;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ViewHolderImpl;
import com.lianzai.reader.utils.RxLogTool;


/**
 * Created by newbiechen on 17-5-19.
 */

public class ReadBgHolder extends ViewHolderImpl<Drawable> {

    private ImageView mReadBg;

    Drawable circle_bg1_checked=null;
    Drawable circle_bg2_checked=null;
    Drawable circle_bg3_checked=null;
    Drawable circle_bg4_checked=null;
    Drawable circle_bg5_checked=null;
    @Override
    public void initView() {
        mReadBg = findById(R.id.read_bg_button);
        circle_bg1_checked=getContext().getResources().getDrawable(R.drawable.circle_bg1_checked);
        circle_bg2_checked=getContext().getResources().getDrawable(R.drawable.circle_bg2_checked);
        circle_bg3_checked=getContext().getResources().getDrawable(R.drawable.circle_bg3_checked);
        circle_bg4_checked=getContext().getResources().getDrawable(R.drawable.circle_bg4_checked);
        circle_bg5_checked=getContext().getResources().getDrawable(R.drawable.circle_bg5_checked);

    }

    @Override
    public void onBind(Drawable data, int pos) {
        RxLogTool.e("pos:"+pos);
        mReadBg.setBackgroundDrawable(data);

    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_read_bg;
    }

    public void setChecked(int pos){
        switch (pos+1){
            case 1:
                mReadBg.setImageDrawable(circle_bg1_checked);
                break;
            case 2:
                mReadBg.setImageDrawable(circle_bg2_checked);
                break;

            case 3:
                mReadBg.setImageDrawable(circle_bg3_checked);
                break;
            case 4:
                mReadBg.setImageDrawable(circle_bg4_checked);
                break;

            case 5:
                mReadBg.setImageDrawable(circle_bg5_checked);
                break;

        }

    }
}
