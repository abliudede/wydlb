package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * Created by lrz on 2018/4/21.
 * 选择金额
 */
public class GirdRecyclerViewAdapter2 extends RecyclerView.Adapter<GirdRecyclerViewAdapter2.MyViewHolder> {

    private Context context;
    private List<String> list;

    private OnRecyclerItemClickListener mOnItemClickListener;//单击事件

    private int choosePosition = -1;


    public GirdRecyclerViewAdapter2(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //找到item的布局
        View view= LayoutInflater.from(context).inflate(R.layout.item_chooseamount_type,parent,false);

        RelativeLayout item_layout=view.findViewById(R.id.item_layout);
        int width= RxDeviceTool.getScreenWidth(context)- RxImageTool.dip2px(60);

        item_layout.setLayoutParams(new RelativeLayout.LayoutParams(width/3,RelativeLayout.LayoutParams.WRAP_CONTENT));

        return new MyViewHolder(view);//将布局设置给holder
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 绑定视图到holder,就如同ListView的getView(),但是这里已经把复用实现了,我们只需要填充数据就行,复用的时候都是调用该方法填充数据
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //填充数据
        holder.tv.setText(list.get(position));
        if(position == choosePosition){
                holder.item.setBackgroundResource(R.drawable.shape_blue_yuanjiaobiankuang);
                holder.tv.setTextColor(context.getResources().getColor(R.color.bluetext_color));
        }else{
            holder.item.setBackgroundResource(R.drawable.gray_bg);
            holder.tv.setTextColor(context.getResources().getColor(R.color.normal_text_color));
        }
        //设置单击事件
        if(mOnItemClickListener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里是为textView设置了单击事件,回调出去
                    //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
                    mOnItemClickListener.onItemClick(v,holder.getLayoutPosition());
                }
            });
        }

    }

    public void setChoosingPosition(int position) {

        //item点击
        int old = choosePosition;
        choosePosition = position;
        if(old >= 0){
            notifyItemChanged(old);
        }
        notifyItemChanged(position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

       RelativeLayout item;
       TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            item=(RelativeLayout)itemView.findViewById(R.id.item_layout);
            tv=(TextView)itemView.findViewById(R.id.textview);
        }
    }

    /**
     * 处理item的点击事件,因为recycler没有提供单击事件,所以只能自己写了
     */
    public interface OnRecyclerItemClickListener {
        public void onItemClick(View view, int position);
    }


    /**
     * 暴露给外面的设置单击事件
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }


    /**
     * 向指定位置添加元素
     */
    public void addItem(int position, String value) {
        if(position > list.size()) {
            position = list.size();
        }
        if(position < 0) {
            position = 0;
        }
        /**
         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
         * 而使用notifyDataSetChanged()则没有
         */
        list.add(position, value);//在集合中添加这条数据
        notifyItemInserted(position);//通知插入了数据
    }

    /**
     * 移除指定位置元素
     */
    public String removeItem(int position) {
        if(position > list.size()-1) {
            return null;
        }
        String value = list.remove(position);//所以还需要手动在集合中删除一次
        notifyItemRemoved(position);//通知删除了数据,但是没有删除list集合中的数据
        return value;
    }

}