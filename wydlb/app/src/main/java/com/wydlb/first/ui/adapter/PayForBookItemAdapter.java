package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.GetBookRewardConfigsBean;
import com.wydlb.first.utils.RxDataTool;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 好友item
 */

public class PayForBookItemAdapter extends BaseQuickAdapter<GetBookRewardConfigsBean.DataBean, BaseViewHolder> {

    private final int imageWidth;
    Context context;

    String chooseCode;

    public String getChooseCode() {
        return chooseCode;
    }

    public void setChooseCode(String chooseCode) {
        this.chooseCode = chooseCode;
    }

    public PayForBookItemAdapter(@Nullable List<GetBookRewardConfigsBean.DataBean> data, Context mContext) {
        super(R.layout.item_pay_for_book, data);
        this.context = mContext;
        imageWidth= (RxDeviceTool.getScreenWidth(context)- RxImageTool.dp2px(15))/3;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,GetBookRewardConfigsBean.DataBean bean) {
        RelativeLayout view_content = baseViewHolder.getView(R.id.view_content);
        ViewGroup.LayoutParams param = view_content.getLayoutParams();
        param.height = imageWidth;
        param.width = imageWidth;
        view_content.setLayoutParams(param);

        ImageView iv_icon = baseViewHolder.getView(R.id.iv_icon);
        TextView tv_name = baseViewHolder.getView(R.id.tv_name);
        TextView tv_amount = baseViewHolder.getView(R.id.tv_amount);
        View view_biankuang = baseViewHolder.getView(R.id.view_biankuang);


        RxImageTool.loadImage(mContext,bean.getIcon(),iv_icon);
        tv_name.setText(bean.getName());

        tv_amount.setText( RxDataTool.format2Decimals(String.valueOf(bean.getAmt())) + "金币");

        if(bean.getCode().equals(chooseCode)){
            view_biankuang.setVisibility(View.VISIBLE);
        }else {
            view_biankuang.setVisibility(View.GONE);
        }


    }


}
