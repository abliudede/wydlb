package com.lianzai.reader.ui.adapter.holder;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.base.adapter.ViewHolderImpl;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;



public class PageStyleHolder extends ViewHolderImpl<Drawable> {

    private SelectableRoundedImageView read_bg_button;

    View read_bg_view;

    @Override
    public void initView() {
        read_bg_button = findById(R.id.read_bg_button);
        read_bg_view=findById(R.id.read_bg_view);
    }

    @Override
    public void onBind(Drawable data, int pos) {
        RxImageTool.loadReadBgStyleImage(getContext(),data,read_bg_button);

        read_bg_view.setBackgroundDrawable(null);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_read_bg;
    }

    public void setChecked(){

        read_bg_view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.shape_btn_read_setting_checked_solid));
    }
}
