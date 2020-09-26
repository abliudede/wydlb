package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.GetRewaredPoolDetailBean;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2018/7/24.
 */

public class BountyItemAdapter extends RecyclerView.Adapter<BountyItemAdapter.ViewHolder> {


    protected Context mContext;
    protected List<GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean> mData;
    protected LayoutInflater mInflater;

    public BountyItemAdapter(Context mContext, List<GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean> mDatas) {
        this.mContext = mContext;
        this.mData = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean> getDatas() {
        return mData;
    }

    public BountyItemAdapter setDatas(List<GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean> datas) {
        mData = datas;
        notifyDataSetChanged();
        return this;
    }


    @Override
    public BountyItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BountyItemAdapter.ViewHolder(mInflater.inflate(R.layout.item_hunter, parent, false));
    }

    @Override
    public void onBindViewHolder(final BountyItemAdapter.ViewHolder holder, final int position) {
        if(null == mData) return;
        if(mData.isEmpty()) return;
        else{
            final GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean areaCodeBean =  mData.get(position%mData.size());
            RxImageTool.loadLogoImage(mContext,areaCodeBean.getAvatar(),holder.headiv);
            String nickstr = "";
            if(!TextUtils.isEmpty(areaCodeBean.getNickName())){
              nickstr = areaCodeBean.getNickName().substring(0,1);
            }
            StringBuilder descstr = new StringBuilder();
            descstr.append(nickstr);
            descstr.append("***   ");
            descstr.append(TimeFormatUtil.getInterval(String.valueOf(Long.parseLong(areaCodeBean.getPayedTime()))));
            descstr.append("打赏");
            holder.desc.setText(descstr.toString());
            holder.amount.setText(RxDataTool.format2Decimals(String.valueOf(areaCodeBean.getTransAmt())) + mContext.getString(R.string.goldcoin));
        }

//        holder.rl_content.setOnClickListener(
//                v->{
//                    v.setTag(holder.getAdapterPosition());
//                }
//        );
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headiv;
        TextView desc;
        TextView amount;

        public ViewHolder(View itemView) {
            super(itemView);
            headiv=(CircleImageView)itemView.findViewById(R.id.iv_logo);
            desc = (TextView) itemView.findViewById(R.id.tv_desc);
            amount = (TextView) itemView.findViewById(R.id.amount_tv);
        }
    }



    @Override
    public int getItemCount() {
        // 返回足够多的item
        if(null == mData){
            return 0;
        }else if(mData.isEmpty()){
            return 0;
        }else if(mData.size() < 5){
            return mData.size();
        }else{
            return Integer.MAX_VALUE;
        }
    }
}
