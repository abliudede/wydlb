package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wydlb.first.R;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.bean.WalletConditionsBean;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.adapter.WalletConditionsAdapter;
import com.wydlb.first.ui.contract.WalletConditionsContract;
import com.wydlb.first.ui.presenter.WalletConditionsPresenter;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxRecyclerViewDividerTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


public class RxDialogWalletPicker extends Dialog implements  WalletConditionsContract.View{
    RecyclerView rv_conditions;
    Context mContext;

    TextView tv_date_picker;

    Spinner spinner_date_select;

    TextView tv_list_description;

    TextView tv_query;

    QueryCallback queryCallback;

    List<String>dateList=new ArrayList<>();

    View view_date;

    @Inject
    WalletConditionsPresenter walletConditionsPresenter;

    List<WalletConditionsBean.DataBean>dataBeans=new ArrayList<>();


    WalletConditionsAdapter walletConditionsAdapter;

    ArrayAdapter<String> adapter;


    String conditionType="";

    public RxDialogWalletPicker(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        initView();
    }

    public RxDialogWalletPicker(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        initView();
    }

    public RxDialogWalletPicker(Context context) {
        super(context);
        mContext=context;
        initView();
    }

    public RxDialogWalletPicker(Activity context) {
        super(context);
        mContext=context;
        initView();
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;

        for(int i=0;i<dataBeans.size();i++){
            if (conditionType.equals(String.valueOf(dataBeans.get(i).getValue()))){
                walletConditionsAdapter.setSelectionPos(i);
                walletConditionsAdapter.notifyDataSetChanged();
            }
        }
    }

    public QueryCallback getQueryCallback() {
        return queryCallback;
    }

    public void setQueryCallback(QueryCallback queryCallback) {
        this.queryCallback = queryCallback;
    }

    public TextView getTv_list_description() {
        return tv_list_description;
    }


    public Spinner getSpinner_date_select() {
        return spinner_date_select;
    }



    public void setDateList() {
         adapter= new ArrayAdapter<String>(mContext, R.layout.date_spinner_item);

         Calendar calendar=Calendar.getInstance();
        dateList.add("全部");
        adapter.add("全部");
        for(int i=0;i<6;i++){

            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH);
            if (month<9){
                dateList.add(year+"0"+(month+1));
                adapter.add(year+"年0"+(month+1)+"月");
            }else{
                dateList.add(year+""+(month+1));
                adapter.add(year+"年"+(month+1)+"月");
            }

            calendar.add(Calendar.MONTH,-1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner_date_select.setDropDownVerticalOffset(RxImageTool.dip2px(5));
        }
        adapter.setDropDownViewResource(R.layout.item_date_select);
        spinner_date_select.setAdapter(adapter);
    }

    public View getView_date() {
        return view_date;
    }

    public TextView getTv_date_picker() {
        return tv_date_picker;
    }

    private void initView() {
        DaggerAccountComponent.builder().appComponent(BuglyApplicationLike.getsInstance().getAppComponent()).build().inject(this);
        walletConditionsPresenter.attachView(this);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_wallet_date_picker, null);
        rv_conditions=(RecyclerView) dialogView.findViewById(R.id.rv_conditions);
        tv_query=(TextView)dialogView.findViewById(R.id.tv_query);

        view_date=dialogView.findViewById(R.id.view_date);

        spinner_date_select=(Spinner) dialogView.findViewById(R.id.spinner_date_select);
        tv_list_description=(TextView)dialogView.findViewById(R.id.tv_list_description);

        tv_date_picker=(TextView)dialogView.findViewById(R.id.tv_date_picker);
        walletConditionsAdapter=new WalletConditionsAdapter(dataBeans,mContext);

        walletConditionsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (i==walletConditionsAdapter.getSelectionPos()){//再次选择，取消
                    walletConditionsAdapter.setSelectionPos(-1);
                    walletConditionsAdapter.notifyDataSetChanged();
                }else{
                    walletConditionsAdapter.setSelectionPos(i);
                    walletConditionsAdapter.notifyDataSetChanged();
                }

            }
        });

        rv_conditions.setLayoutManager(new GridLayoutManager(mContext,3));
        rv_conditions.addItemDecoration(new RxRecyclerViewDividerTool(0,0, RxImageTool.dip2px(10),0));
        rv_conditions.setAdapter(walletConditionsAdapter);

        setCanceledOnTouchOutside(true);

        setContentView(dialogView);

        //请求
        walletConditionsPresenter.getBalanceInOutType();

        tv_query.setOnClickListener(
                v->{
                    String date=dateList.get(spinner_date_select.getSelectedItemPosition());
                    int type=walletConditionsAdapter.getSelectionPos()>=0?walletConditionsAdapter.getData().get(walletConditionsAdapter.getSelectionPos()).getValue():-1;
                    getQueryCallback().queryClick(type,date);
                }
        );

        setDateList();

    }




    @Override
    public void getBalanceInOutTypeSuccess(WalletConditionsBean bean) {
        dataBeans.clear();
        dataBeans.addAll(bean.getData());
        walletConditionsAdapter.setNewData(dataBeans);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }

    public interface  QueryCallback{
        void queryClick(int type,String date);
    }

}
