package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.ui.activity.ActivityBountyHunter;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityOneKeyReward;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 发现页item
 */

public class NewFindAdapter extends BaseQuickAdapter<BannerBean.DataBean, BaseViewHolder> {

    int imageWidth;
    Context context;

    public NewFindAdapter(@Nullable List<BannerBean.DataBean> data, Context mContext) {
        super(R.layout.item_find, data);
        this.context = mContext;
        imageWidth= RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(32);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BannerBean.DataBean bannerBean) {
        SelectableRoundedImageView iv_cover = baseViewHolder.getView(R.id.iv_cover);

        ViewGroup.LayoutParams params = iv_cover.getLayoutParams();
        params.height = imageWidth/3;
        iv_cover.setLayoutParams(params);

        RxImageTool.loadLogoImage(context,bannerBean.getBannerPhoto(),iv_cover);

//        setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });

    }


}
