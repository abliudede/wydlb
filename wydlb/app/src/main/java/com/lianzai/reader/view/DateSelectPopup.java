package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.WalletListSumResponse;
import com.lianzai.reader.ui.adapter.DateSelectAdapter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;

import java.util.List;

/**
 * Created by lrz on 2018/3/19.
 */

public class DateSelectPopup extends PopupWindow  {


    Context mContext;

    View contentView;

    RecyclerView recyclerView;

    DateSelectAdapter dateSelectAdapter;

    RxLinearLayoutManager manager;


    public DateSelectPopup(Context context) {
        super(context);
        mContext=context;
    }

    public void initView(View view,List<WalletListSumResponse.DataBean> dataBeanList){
        contentView= LayoutInflater.from(mContext).inflate(R.layout.popup_date_select,null);
        setContentView(contentView);

        this.setOutsideTouchable(true);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);

//        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        recyclerView=(RecyclerView)contentView.findViewById(R.id.rv_list);
        dateSelectAdapter= new DateSelectAdapter(dataBeanList,mContext);

        manager=new RxLinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(dateSelectAdapter);

        showAtLocation(view, Gravity.BOTTOM,0,0);

    }

}
