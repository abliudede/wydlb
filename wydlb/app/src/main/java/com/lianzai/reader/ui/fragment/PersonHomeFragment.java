package com.lianzai.reader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.GetUserPersonalBookListInfoBean;
import com.lianzai.reader.bean.GetUserPersonalInfoBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.RecentBookGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.RxToast;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 个人主页切换fragment
 */
public class PersonHomeFragment extends BaseFragment{

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    MoreOptionPopup moreOptionPopup;

    RecentBookGridAdapter recentBookGridAdapter;
    List<BookStoreBeanN> listBeans=new ArrayList<>();

    @Bind(R.id.read_like_tv)
    TextView read_like_tv;
    @Bind(R.id.read_like_empty)
    TextView read_like_empty;
    @Bind(R.id.id_flowlayout_hot)
    TagFlowLayout mFlowHotLayout;

    @Bind(R.id.tv_empty)
    TextView tv_empty;

    @Bind(R.id.exchange_tv)
    TextView exchange_tv;

    //排序方式 1阅读时长 2书架加入时间(默认1)
    String type = "1";

    @Override
    public int getLayoutResId() {
        return R.layout.view_person_home_header;
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
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){
        recentBookGridAdapter = new RecentBookGridAdapter(listBeans,getActivity());

        recentBookGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position >= 0 && position < recentBookGridAdapter.getItemCount()){
                    openBookClick(recentBookGridAdapter.getItem(position));
                }
            }
        });

        rv_data.setAdapter(recentBookGridAdapter);
        rv_data.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(16), RxImageTool.dip2px(16),0));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_data.setLayoutManager(gridLayoutManager);

        tv_empty.setVisibility(View.VISIBLE);

    }

    //打开阅读
    private void openBookClick(BookStoreBeanN bookStoreBean) {
        SkipReadUtil.normalRead(getActivity(),bookStoreBean.getBookId(),"",false);
    }

    public void bindData( GetUserPersonalInfoBean baseBean){
        //阅读喜好添加
        ArrayList<String> interestingResult=new ArrayList<>();
        if(null != baseBean.getData().getReadLikeTags()){
            for(GetUserPersonalInfoBean.DataBean.ReadLikeTagsBean item:baseBean.getData().getReadLikeTags()){
                interestingResult.add(item.getName());
            }
        }
        if(interestingResult.isEmpty()){
            read_like_empty.setVisibility(View.VISIBLE);
        }else {
            read_like_empty.setVisibility(View.GONE);
        }
        //阅读喜好流式布局
        mFlowHotLayout.setAdapter(new TagAdapter<String>(interestingResult) {
            @Override
            public View getView(FlowLayout parent, int position, String str) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.tv_flow,
                        mFlowHotLayout, false);
                if(baseBean.getData().getReadLikeGender() == 1){
                    tv.setBackgroundResource(R.drawable.hot_search_bg1);
                    tv.setTextColor(getResources().getColor(R.color.bluetext_color));
                }else {
                    tv.setBackgroundResource(R.drawable.hot_search_bg3);
                    tv.setTextColor(getResources().getColor(R.color.pinktext_color));
                }
                tv.setText(str);
                return tv;
            }
        });
        //最近阅读
        requestUserPersonalInfo();
    }


    private void requestUserPersonalInfo() {
        Map<String, String> map = new HashMap<>();
        if(getActivity() instanceof  PerSonHomePageActivity){
            String userId = ((PerSonHomePageActivity) getActivity()).getUserId();
            map.put("otherUserId", userId);
        }
        map.put("bookShelfLimit", "12");
        map.put("type", type);//排序方式 1阅读时长 2书架加入时间(默认1)
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/userAttention/getUserPersonalBookListInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetUserPersonalBookListInfoBean baseBean = GsonUtil.getBean(response, GetUserPersonalBookListInfoBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        listBeans.clear();
                        if(null != baseBean.getData())
                            listBeans.addAll(baseBean.getData());
                        recentBookGridAdapter.notifyDataSetChanged();
                        if(listBeans.isEmpty()) {
                            rv_data.setVisibility(View.GONE);
                            tv_empty.setVisibility(View.VISIBLE);
                        }else {
                            rv_data.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);
                        }
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestHistory e:" + e.getMessage());
                }
            }
        });
    }



    @Override
    public void fetchData() {
    }

    @OnClick(R.id.exchange_tv)
    void exchange_tvClick(){
        if(null == moreOptionPopup) {
            moreOptionPopup = new MoreOptionPopup(getActivity());
            moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
            moreOptionPopup.getTv_report().setText("最常阅读");
            moreOptionPopup.getTv_report().setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_read_longest),null,null,null);
            moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
            moreOptionPopup.getTv_delete().setText("最近添加");
            moreOptionPopup.getTv_delete().setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_read_recent),null,null,null);
            moreOptionPopup.getTv_report().setOnClickListener(
                    v -> {
                        try {
                            exchange_tv.setText("最常阅读");
                            type = "1";
                            requestUserPersonalInfo();
                        }catch (Exception e){
                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.getTv_delete().setOnClickListener(
                    v -> {
                        try {
                            exchange_tv.setText("最近添加");
                            type = "2";
                            requestUserPersonalInfo();
                        }catch (Exception e){
                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //隐藏蒙层
                    if(getActivity() instanceof PerSonHomePageActivity){
                        //圈子详情页
                        ((PerSonHomePageActivity)getActivity()).getMask_view().setVisibility(View.GONE);
                    }
                }
            });
        }

        //显示蒙层
        if(getActivity() instanceof PerSonHomePageActivity){
            //圈子详情页
            ((PerSonHomePageActivity)getActivity()).getMask_view().setVisibility(View.VISIBLE);
        }
        moreOptionPopup.showPopupWindow(getActivity(),exchange_tv);
    }

}
