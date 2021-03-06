package com.wydlb.first.ui.adapter;

import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.interfaces.OnRepeatClickListener;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class PicturesAdapter extends BaseQuickAdapter<BaseBean,BaseViewHolder> {


    public PicturesAdapter(@Nullable List<BaseBean> data) {
        super(R.layout.item_upload_file, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, BaseBean bean) {
        baseViewHolder.getView(R.id.iv_delete_photo).setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                remove(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }
        });
    }

}
