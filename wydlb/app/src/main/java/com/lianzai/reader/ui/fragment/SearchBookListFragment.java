package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.adapter.BookListListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 搜索--书单
 */
public class SearchBookListFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView rv_search_book_list;

    CustomLoadMoreView customLoadMoreView;

    @Bind(R.id.loading_view)
    View loading_view;


    BookListListAdapter bookListListAdapter;

    int pageNum=1;
    int pageSize=10;

    List<BookListResponse.DataBean.ListBean> bookListData=new ArrayList<>();


    ActivitySearch2 activitySearch2;

    String mKeyWord;

    View emptyView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitySearch2 =(ActivitySearch2)activity;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview_not_refresh;
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
    public void configViews(Bundle savedInstanceState) {

        initBookListAdapter();

    }


    private void initBookListAdapter(){
        emptyView=getLayoutInflater().inflate(R.layout.view_search_book_list_record_empty,null);

        customLoadMoreView = new CustomLoadMoreView();

        bookListListAdapter=new BookListListAdapter(bookListData,getContext());

        bookListListAdapter.setLoadMoreView(customLoadMoreView);
        bookListListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                bookListRequest(mKeyWord,false);
            }
        },rv_search_book_list);

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(getActivity());
        rv_search_book_list.setLayoutManager(rxLinearLayoutManager);
        rv_search_book_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));



        bookListListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityBookListDetail.startActivity(getActivity(),String.valueOf(bookListListAdapter.getData().get(position).getId()));
            }
        });

        rv_search_book_list.setAdapter(bookListListAdapter);
    }


    @Override
    public void fetchData() {
//        if (!TextUtils.isEmpty(activitySearch2.ed_search_keyword.getText().toString().trim())){
//            bookListRequest(activitySearch2.ed_search_keyword.getText().toString());
//        }

        RxLogTool.e("SearchBookListFragment fetchData.....");
    }


    BookListResponse.DataBean updateLatestBean;


    public void bookListRequest(String keyword,boolean isRefresh){

        mKeyWord=keyword;
        if (isRefresh){
            pageNum=1;

            if (null!=loading_view){
                loading_view.setVisibility(View.VISIBLE);
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("pageNum",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/search/booklist/"+keyword, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    if (null!=loading_view){
                        loading_view.setVisibility(View.GONE);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    if (null!=loading_view){
                        loading_view.setVisibility(View.GONE);
                    }
                    BookListResponse bookListResponse= GsonUtil.getBean(response,BookListResponse.class);
                    if (bookListResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        updateLatestBean=bookListResponse.getData();
                        if (pageNum==1){
                            bookListData.clear();
                        }

                        if (null!=updateLatestBean.getList()){
                            //填充数据
                            bookListData.addAll(updateLatestBean.getList());
                        }
                        bookListListAdapter.search(keyword);
                        bookListListAdapter.replaceData(bookListData);

                        //是否允许加载更多
                        if (null == updateLatestBean.getList() || updateLatestBean.getList().isEmpty()){//最后一页
                            bookListListAdapter.loadMoreEnd();
                        }else{
                            bookListListAdapter.loadMoreComplete();
                        }


                    }else{//加载失败
                        bookListListAdapter.loadMoreFail();
                    }

                    if(bookListData.isEmpty()) {
                        bookListListAdapter.setEmptyView(emptyView);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
