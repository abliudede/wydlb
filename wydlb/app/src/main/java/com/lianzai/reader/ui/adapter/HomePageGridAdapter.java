package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.HomePageSettingBean;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class HomePageGridAdapter extends BaseQuickAdapter<HomePageSettingBean.DataBean.HeadBookListBean.DetailListBean,BaseViewHolder> {


    Context context;

    int imageWidth=0;

    public HomePageGridAdapter(@Nullable List<HomePageSettingBean.DataBean.HeadBookListBean.DetailListBean> data, Context mContext) {
        super(R.layout.item_homepage_setting_grid, data);
        this.context=mContext;

        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(48))/3;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,HomePageSettingBean.DataBean.HeadBookListBean.DetailListBean bean) {
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView tv_book_list_name=baseViewHolder.getView(R.id.tv_book_list_name);
        ImageView icon_gongxiangbanquan = baseViewHolder.getView(R.id.icon_gongxiangbanquan);

        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) iv_book_list_cover.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = (int) (imageWidth*1.3);
        iv_book_list_cover.setLayoutParams(layoutParams);


        tv_book_list_name.setText(bean.getBookTitle());
        RxImageTool.loadSquareImage(context,bean.getPlatformCover(),iv_book_list_cover);
        if(bean.isIsCopyright()){
            icon_gongxiangbanquan.setVisibility(View.VISIBLE);
        }else {
            icon_gongxiangbanquan.setVisibility(View.GONE);
        }
    }

}
