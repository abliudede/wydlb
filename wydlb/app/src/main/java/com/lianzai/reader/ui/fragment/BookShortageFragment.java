package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookShortageSearchBean;
import com.lianzai.reader.bean.SearchNovelsBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.chat.PublicNumberDetailActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.BooksSearchAdapter;
import com.lianzai.reader.ui.adapter.BooksShortageSearchAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 搜索--书荒神器
 */
public class BookShortageFragment extends BaseFragment  {

    @Bind(R.id.recyclerView)
    RecyclerView rv_search_book;

    BooksShortageSearchAdapter booksShortageSearchAdapter;

    List<BookShortageSearchBean.DataBean> booksBeanList=new ArrayList<>();

    ActivitySearch2 activitySearch2;

    View emptyView;

    View headView;
    LinearLayout ly_content;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitySearch2 =(ActivitySearch2)activity;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_shortage;
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
        initBookAdapter();
    }


    private void initBookAdapter(){
        booksShortageSearchAdapter =new BooksShortageSearchAdapter(booksBeanList,getContext());

        emptyView=getLayoutInflater().inflate(R.layout.view_search_book_list_record_empty,null);


        booksShortageSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                BookShortageSearchBean.DataBean bean= booksShortageSearchAdapter.getData().get(i);
                //区分跳名片页还是圈子首页
                if(bean.getPlatformId() > 0){
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(bean.getPlatformId()));
                }
            }
        });

        headView = getLayoutInflater().inflate(R.layout.view_header_bookshortage,null);
        ly_content = headView.findViewById(R.id.ly_content);
        booksShortageSearchAdapter.addHeaderView(headView);

        RxLinearLayoutManager manager = new RxLinearLayoutManager(getContext());
        rv_search_book.setLayoutManager(manager);
//        rv_search_book.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        rv_search_book.setAdapter(booksShortageSearchAdapter);

    }


    @Override
    public void fetchData() {

    }



    public void bookListRequest(String keyword){
        Map<String,String> map=new HashMap<>();
        map.put("bookName",keyword);
        map.put("num","20");

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/h5/bookShortageByBookName", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try{
                    BookShortageSearchBean bookShortageSearchBean= GsonUtil.getBean(response,BookShortageSearchBean.class);
                    if (bookShortageSearchBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        booksBeanList.clear();
                        if (null!=bookShortageSearchBean.getData()){
                            booksBeanList.addAll(bookShortageSearchBean.getData());
                            if(!booksBeanList.isEmpty()){
                                if(!TextUtils.isEmpty(booksBeanList.get(0).getMsg())) {
                                    ly_content.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        booksShortageSearchAdapter.replaceData(booksBeanList);
                    }

                    if(booksBeanList.isEmpty()) {
                        booksShortageSearchAdapter.setEmptyView(emptyView);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
