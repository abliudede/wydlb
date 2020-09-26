package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.bean.RelatedLinksBean2;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.adapter.RelatedLinksAdapter2;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SkipReadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 搜索--外站源列表
 */
public class BookSourceFragment extends BaseFragment {


    @Bind(R.id.content)
    RelativeLayout content;

    @Bind(R.id.recyclerView)
    RecyclerView rv_search_book;

    RelatedLinksAdapter2 relatedLinksAdapter;
    List<RelatedLinksBean2.DataBean> arrayList = new ArrayList<>();
    RxLinearLayoutManager manager;

    ActivitySearch2 activitySearch2;

    View emptyView;

    private ReadSettingsResponse readSettingsResponse;

    View footerView;
    private TextView  tv_book_more;

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
        relatedLinksAdapter =new RelatedLinksAdapter2(arrayList);

        content.setBackgroundResource(R.color.white);

        emptyView=getLayoutInflater().inflate(R.layout.view_search_book_list_record_empty,null);

        relatedLinksAdapter.setContentClickListener(new RelatedLinksAdapter2.ContentClickListener() {
            @Override
            public void muluClick(View v, int pos) {
                SkipReadUtil.linkRead(activitySearch2,String.valueOf(arrayList.get(pos).getBook_id()),arrayList.get(pos).getSource(),true,arrayList.get(pos).getCatalog_url());
            }

            @Override
            public void readClick(View v, int pos) {
                //优先用第一章的链接
                if(!TextUtils.isEmpty(arrayList.get(pos).getFirst_chapter_url())) {
                    SkipReadUtil.linkRead(activitySearch2, String.valueOf(arrayList.get(pos).getBook_id()), arrayList.get(pos).getSource(), false, arrayList.get(pos).getFirst_chapter_url());
                }else {
                    SkipReadUtil.linkRead(activitySearch2, String.valueOf(arrayList.get(pos).getBook_id()), arrayList.get(pos).getSource(), false, arrayList.get(pos).getCatalog_url());
                }
            }
        });

        manager=new RxLinearLayoutManager(activitySearch2);
        rv_search_book.setLayoutManager(manager);
        rv_search_book.addItemDecoration(new RxRecyclerViewDividerTool(0,0 , RxImageTool.dip2px(10),0));
        rv_search_book.setAdapter(relatedLinksAdapter);

        footerView = getLayoutInflater().inflate(R.layout.footer_book_source, null);
        tv_book_more = footerView.findViewById(R.id.tv_book_more);
        relatedLinksAdapter.addFooterView(footerView);
    }


    @Override
    public void fetchData() {

    }



    public void myBookSourceRequest(String bookIds){
        Map<String, String> map=new HashMap<>();
        map.put("bookIds",bookIds);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/bookRelatedLinksNew", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                try{
                    RelatedLinksBean2 relatedLinksBean = GsonUtil.getBean(response, RelatedLinksBean2.class);
                    if (relatedLinksBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        arrayList.clear();

                        RelatedLinksBean.DataBean.SourcesBean sourcesBeanTemp = null;
                        //避免重复读取遍历
                        if(null == readSettingsResponse) {
                            readSettingsResponse = RxSharedPreferencesUtil.getInstance().getObject(Constant.READ_SETTINGS_KEY, ReadSettingsResponse.class);
                        }
                        List<RelatedLinksBean2.DataBean> listBeans = relatedLinksBean.getData();

                        if (null!=relatedLinksAdapter&&null!=listBeans){
                            int i=0;
                            for(;i<listBeans.size();i++){
                                RelatedLinksBean2.DataBean sourcesBean=listBeans.get(i);
                                //首先设置随机码
                                int random = new Random().nextInt(5);
                                sourcesBean.setRandomInt(random);

                                if (null!=readSettingsResponse){
                                    for(ReadSettingsResponse.ReadSettingsBean readSettingsBean:readSettingsResponse.getReadSettings()){
                                        if (sourcesBean.getSource().equals(readSettingsBean.getSource())){
                                            sourcesBean.setSourceName(readSettingsBean.getSourceName());
                                        }
                                    }
                                }
                            }
                        }

                        if (null != listBeans && !listBeans.isEmpty()) {
                            arrayList.addAll(listBeans);
                            relatedLinksAdapter.notifyDataSetChanged();
                        } else {
                            relatedLinksAdapter.notifyDataSetChanged();
                        }

                        if(arrayList.isEmpty()){
                            tv_book_more.setVisibility(View.INVISIBLE);
                            relatedLinksAdapter.setEmptyView(emptyView);
                        }else {
                            tv_book_more.setVisibility(View.VISIBLE);
                        }

                    }else {
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
