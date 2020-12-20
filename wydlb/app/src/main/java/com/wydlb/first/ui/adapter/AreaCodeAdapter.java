package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.bean.AreaCodeBean;
import com.wydlb.first.interfaces.OnRepeatClickListener;

import java.util.List;



public class AreaCodeAdapter extends RecyclerView.Adapter<AreaCodeAdapter.ViewHolder> {
    protected Context mContext;
    protected List<AreaCodeBean> mDatas;
    protected LayoutInflater mInflater;

    public AreaCodeAdapter(Context mContext, List<AreaCodeBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    OnRepeatClickListener onRepeatClickListener;

    public List<AreaCodeBean> getDatas() {
        return mDatas;
    }

    public AreaCodeAdapter setDatas(List<AreaCodeBean> datas) {
        mDatas = datas;
        return this;
    }

    public OnRepeatClickListener getOnRepeatClickListener() {
        return onRepeatClickListener;
    }

    public void setOnRepeatClickListener(OnRepeatClickListener onRepeatClickListener) {
        this.onRepeatClickListener = onRepeatClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_area_code, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AreaCodeBean areaCodeBean = mDatas.get(position);
        holder.tv_area_name.setText(areaCodeBean.getChinaName());
        holder.tv_area_code.setText("+"+areaCodeBean.getInternaCode());
        holder.rl_content.setOnClickListener(
                v->{
                    v.setTag(holder.getAdapterPosition());
                    getOnRepeatClickListener().onClick(v);
                }
        );
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_area_name;
        TextView tv_area_code;
        View rl_content;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_content=itemView.findViewById(R.id.rl_content);
            tv_area_name = (TextView) itemView.findViewById(R.id.tv_area_name);
            tv_area_code = (TextView) itemView.findViewById(R.id.tv_area_code);
        }
    }
}
