package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.CopyrightAssetsBean;
import com.lianzai.reader.bean.UserAutoSettingDailyTicketListBean;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketManage;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 版权资产列表
 */
public class CopyrightAssetsListAdapter extends BaseQuickAdapter<CopyrightAssetsBean.DataBean.CopyrightListBean,BaseViewHolder> {


    private Context context;

    public CopyrightAssetsListAdapter(@Nullable List<CopyrightAssetsBean.DataBean.CopyrightListBean> data, Context mContext) {
        super(R.layout.item_copyright, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,CopyrightAssetsBean.DataBean.CopyrightListBean bean) {
        SelectableRoundedImageView iv_book_cover=baseViewHolder.getView(R.id.iv_book_cover);
        TextView tv_book_name=baseViewHolder.getView(R.id.tv_book_name);

        TextView tv_read_coin=baseViewHolder.getView(R.id.tv_read_coin);
        TextView tv_book_coin_name=baseViewHolder.getView(R.id.tv_book_coin_name);
        TextView tv_book_ticket_name=baseViewHolder.getView(R.id.tv_book_ticket_name);
        TextView tv_book_coin=baseViewHolder.getView(R.id.tv_book_coin);
        TextView tv_book_ticket=baseViewHolder.getView(R.id.tv_book_ticket);
        ImageView iv_exchange=baseViewHolder.getView(R.id.iv_exchange);

        //已投资阅点
        TextView tv_corwd_balance=baseViewHolder.getView(R.id.tv_corwd_balance);


        RxImageTool.loadImage(context,bean.getCover(),iv_book_cover);
        tv_book_name.setText(bean.getBookName());
//        StringBuilder sb = new StringBuilder();
//        if(!TextUtils.isEmpty(bean.getBookAuthor())){
//            sb.append(bean.getBookAuthor());
//        }
//        sb.append("|");
//        sb.append(RxDataTool.getFormatNumber(bean.getBookWordNum()));
//        sb.append("字");
//        sb.append("|");
//        sb.append(RxDataTool.getFormatNumber(bean.getBookAttenNum()));
//        sb.append("人关注");
//        tv_book_author.setText(sb.toString());

        tv_book_coin_name.setText(bean.getBookCoinName() + "点");
        tv_book_coin.setText(RxDataTool.format2Decimals(String.valueOf(bean.getCoinBookBalance())));

        tv_book_ticket_name.setText(bean.getBookCoinName() + "券");
        tv_book_ticket.setText(RxDataTool.format2Decimals(String.valueOf(bean.getCouponBookBalance())));

        if(bean.getReadBalance() > 0) {
            tv_read_coin.setText("≈" + RxDataTool.format2Decimals(String.valueOf(bean.getReadBalance())) + "阅点");
            iv_exchange.setVisibility(View.VISIBLE);
        }else {
            tv_read_coin.setText("≈" + RxDataTool.format2Decimals("0") + "阅点");
            iv_exchange.setVisibility(View.GONE);
        }

        if(bean.getCorwdBalance() > 0){
            tv_corwd_balance.setText(RxDataTool.format2Decimals(String.valueOf(bean.getCorwdBalance())));
        }else {
            tv_corwd_balance.setText(RxDataTool.format2Decimals("0"));
        }


        iv_exchange.setOnClickListener(
                v -> {
                    ActivityWebView.startActivity(mContext, Constant.H5_BASE_URL + "/copyright/#/app/exchange/index/" + bean.getBookId(),1);
                }
        );

//        iv_book_cover.setOnClickListener(
//                v -> {
//                    ActivityCircleDetail.startActivity(mContext,bean.getPlatformId());
//                }
//        );

    }


}
