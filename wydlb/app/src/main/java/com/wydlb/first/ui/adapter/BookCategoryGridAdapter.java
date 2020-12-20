package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.TagsResponse;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookCategoryGridAdapter extends BaseQuickAdapter<TagsResponse.DataBean,BaseViewHolder> {


    Context context;

    int imageWidth=0;

    HashMap<Integer,Boolean>map=new HashMap<>();

    boolean isBoy=true;//是否是男生

    public BookCategoryGridAdapter(@Nullable List<TagsResponse.DataBean> data, Context mContext) {
        super(R.layout.item_book_category_grid, data);
        this.context=mContext;

        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(60))/3;

        initMap();
    }

    public void initMap(){
        getMap().clear();

        for(int i=0;i<getData().size();i++){
            getMap().put(i,false);
        }

    }

    public boolean isBoy() {
        return isBoy;
    }

    public void setBoy(boolean boy) {
        isBoy = boy;
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public int getSelectCount(){
        int count=0;

        for(int i=0;i<getMap().size();i++){
            if (getMap().get(i)){
                count++;
            }
        }
        return count;
    }

    /**
     * 获取选中的ID
     * @return
     */
    public String getSelectIds(){

        StringBuffer tags=new StringBuffer("");

        if (getData().size()>0){
            for(int i=0;i<getMap().size();i++){
                if (getMap().get(i)){
                    tags.append(String.valueOf(getData().get(i).getId()));
                    tags.append(",");
                }
            }
        }
        String ids="";
        if (tags.indexOf(",")>0){
            ids=tags.deleteCharAt(tags.toString().lastIndexOf(",")).toString();;
        }
        return ids;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,TagsResponse.DataBean bean) {
        TextView tv_category=baseViewHolder.getView(R.id.tv_category);
        ImageView iv_status=baseViewHolder.getView(R.id.iv_status);

        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(imageWidth,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.MarginLayoutParams marginLayoutParams=new RelativeLayout.MarginLayoutParams(layoutParams);
//        marginLayoutParams.rightMargin=RxImageTool.dip2px(10);
//        marginLayoutParams.topMargin=RxImageTool.dip2px(10);

        layoutParams.setMargins(RxImageTool.dip2px(8),RxImageTool.dip2px(8),0,0);
        tv_category.setLayoutParams(layoutParams);


        if (getMap().get(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())){
            iv_status.setVisibility(View.VISIBLE);

            if (isBoy){
                tv_category.setBackgroundResource(R.drawable.sex_corner_bg_selected);
            }else{
                tv_category.setBackgroundResource(R.drawable.girl_corner_bg);
            }

            tv_category.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            iv_status.setVisibility(View.GONE);

            tv_category.setBackgroundResource(R.drawable.sex_corner_bg);
            tv_category.setTextColor(context.getResources().getColor(R.color.color_black_ff666666));
        }

        tv_category.setText(bean.getName());


    }

}
