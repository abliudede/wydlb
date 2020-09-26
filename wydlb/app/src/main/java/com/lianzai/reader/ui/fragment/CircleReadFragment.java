package com.lianzai.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.CircleReadBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.CircleReadAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.ItemCircleRead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 圈子-书圈页面
 */
public class CircleReadFragment extends BaseFragment{

    @Bind(R.id.recyclerView)
    RecyclerView rv_list;

    @Bind(R.id.hsc_head)
    HorizontalScrollView hsc_head;

    @Bind(R.id.recyclerView_head)
    LinearLayout recyclerView_head;

    CustomLoadMoreView customLoadMoreView;
    CircleReadAdapter circleReadAdapter;

    List<CircleReadBean.DataBean.BookCirclePageBean.ListBean> mdatalist=new ArrayList<>();
    List<CircleReadBean.DataBean.BookCirclePageBean.ListBean> showList=new ArrayList<>();

    View headerView;
    LinearLayout recyclerView;

    private RxLinearLayoutManager manager;


    private static final String all = "全部";
    String choose = all;
    private String circleId;


    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_circle_read;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){

        circleReadAdapter=new CircleReadAdapter(showList,getContext());

        customLoadMoreView = new CustomLoadMoreView();
        circleReadAdapter.setLoadMoreView(customLoadMoreView);
        circleReadAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        }, rv_list);


        circleReadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try{
                    //跳转到圈子
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(showList.get(i).getPlatformId()),1);
                }catch (Exception e){

                }
            }
        });

        manager = new RxLinearLayoutManager(getContext());
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(circleReadAdapter);
//        rv_list.setPadding(0,0,0,RxImageTool.dp2px(20));

        circleReadAdapter.setEmptyView(R.layout.view_dynamic_empty,rv_list);
        circleReadAdapter.setHeaderAndEmpty(true);

        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager.findFirstVisibleItemPosition() >= 1) {
                    hsc_head.setVisibility(View.VISIBLE);
                } else {
                    hsc_head.setVisibility(View.GONE);
                }
            }
        });

        if(!TextUtils.isEmpty(circleId)) {
            circleDynamicRequest();
        }
    }


    @Override
    public void fetchData() {


    }

    /**
     * 章节更新头部
     */
    private void initUpdateView(List<String> bean){
        if(null == bean){
            if(null != headerView) {
                headerView.setVisibility(View.GONE);
            }
            return;
        }

        if(null == headerView){
            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycle_head, null);
            recyclerView = headerView.findViewById(R.id.recyclerView);
            if (null!=circleReadAdapter){
                circleReadAdapter.addHeaderView(headerView);
            }
        }

        if(null != bean && !bean.isEmpty()) {
            recyclerView_head.removeAllViews();
            recyclerView.removeAllViews();
            bean.add(0,all);
            for(String item : bean){
                ItemCircleRead itemCircleRead1 = new ItemCircleRead(getActivity());
                itemCircleRead1.bindData(item);
                itemCircleRead1.setChoose(choose);
                itemCircleRead1.setOnClickListener(
                        v -> {
                            setChoose(item);
                        }
                );
                ItemCircleRead itemCircleRead2 = new ItemCircleRead(getActivity());
                itemCircleRead2.bindData(item);
                itemCircleRead2.setChoose(choose);
                itemCircleRead2.setOnClickListener(
                        v -> {
                            setChoose(item);
                        }
                );
                recyclerView_head.addView(itemCircleRead1);
                recyclerView.addView(itemCircleRead2);
            }
        }
    }

    private void setChoose(String item) {
        choose = item;

        //刷新选择控件
        int size = recyclerView.getChildCount();
        if(size > 0){
            for(int i = 0 ; i < size ; i++){
                View viewTemp = recyclerView.getChildAt(i);
                if(viewTemp instanceof ItemCircleRead){
                    ((ItemCircleRead) viewTemp).setChoose(choose);
                }
            }
        }

        //刷新选择控件
        int size2 = recyclerView_head.getChildCount();
        if(size2 > 0){
            for(int i = 0 ; i < size2 ; i++){
                View viewTemp = recyclerView_head.getChildAt(i);
                if(viewTemp instanceof ItemCircleRead){
                    ((ItemCircleRead) viewTemp).setChoose(choose);
                }
            }
        }

        refreshData();
    }

    public void circleDynamicRequest(){
        Map<String, String> map = new HashMap<>();
        map.put("pageNumber", "1");
        map.put("pageSize", "100");
        map.put("platformId", circleId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/bookCircle", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxLogTool.e(e.toString());
                }catch (Exception ex){
//                    ex.printStackTrace();
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    CircleReadBean circleReadBean = GsonUtil.getBean(response, CircleReadBean.class);
                    if (circleReadBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        mdatalist.clear();
                        //只有刷新的时候更新
                        initUpdateView(circleReadBean.getData().getSecondCateSet());
                        List<CircleReadBean.DataBean.BookCirclePageBean.ListBean> listBeans = circleReadBean.getData().getBookCirclePage().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            mdatalist.addAll(listBeans);
                        }
                        refreshData();
                    }
                }catch (Exception e){
                    RxLogTool.e(e.toString());
                }
            }
        });
    }

    //分类筛选功能
    private void refreshData(){
        showList.clear();
        if(all.equals(choose)){
            showList.addAll(mdatalist);
        }else {
            for(CircleReadBean.DataBean.BookCirclePageBean.ListBean item : mdatalist){
                if(choose.equals(item.getCategoryName())){
                    showList.add(item);
                }
            }
        }

        circleReadAdapter.replaceData(showList);
        circleReadAdapter.loadMoreComplete();
        circleReadAdapter.loadMoreEnd();
        rv_list.smoothScrollToPosition(0);
    }



}
