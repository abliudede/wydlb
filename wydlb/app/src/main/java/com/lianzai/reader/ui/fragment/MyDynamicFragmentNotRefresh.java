package com.lianzai.reader.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CircleBookBean;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.adapter.MyDynamicAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 我的动态fragment
 */
public class MyDynamicFragmentNotRefresh extends BaseFragment{

    @Bind(R.id.rv_data)
    RecyclerView rv_list;

    CustomLoadMoreView customLoadMoreView;

    MyDynamicAdapter adForExampleAdapter;

    int pageNum=1;
    int pageSize=10;

    List<Object> mdatalist=new ArrayList<>();

    View emptyView;
    TextView tv_empty_content;

    MoreOptionPopup moreOptionPopup;
    private String userId;


    public static final MyDynamicFragmentNotRefresh newInstance( String userId)
    {
        MyDynamicFragmentNotRefresh fragment = new MyDynamicFragmentNotRefresh();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_public_number_history;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Bundle args=getArguments();
            userId=args.getString("userId");
        }catch (Exception e){

        }

    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){
        adForExampleAdapter=new MyDynamicAdapter(mdatalist,getContext());
        adForExampleAdapter.setDynamicClickListener(dynamicClickListener);

        emptyView=getLayoutInflater().inflate(R.layout.view_dynamic_empty,null);
        tv_empty_content=emptyView.findViewById(R.id.tv_empty_content);

        customLoadMoreView = new CustomLoadMoreView();
        adForExampleAdapter.setLoadMoreView(customLoadMoreView);
        adForExampleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                circleDynamicRequest(false);
            }
        }, rv_list);


        RxLinearLayoutManager manager = new RxLinearLayoutManager(getContext());
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0,0 ,RxImageTool.dip2px(10),0));
        rv_list.setAdapter(adForExampleAdapter);

        adForExampleAdapter.setEmptyView(R.layout.view_dynamic_empty,rv_list);

        circleDynamicRequest(true);
    }


    @Override
    public void fetchData() {
    }



    public void circleDynamicRequest(boolean isRefresh){
        if (isRefresh){
            pageNum=1;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pageNumber",pageNum);
        jsonObject.addProperty("pageSize", pageSize);
        jsonObject.addProperty("dynamicUserId", userId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/userDynamic", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/userDynamic").show();
                    adForExampleAdapter.loadMoreFail();
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    CircleDynamicBean circleDynamicBean = GsonUtil.getBean(response, CircleDynamicBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE && null != circleDynamicBean.getData()) {
                        if(isRefresh){
                            mdatalist.clear();
                            if(getActivity() instanceof  PerSonHomePageActivity){
                                if(null != circleDynamicBean.getData() && null != circleDynamicBean.getData().getList() && !circleDynamicBean.getData().getList().isEmpty()){
                                    ((PerSonHomePageActivity)getActivity()).setCircleDynamicText("动态(" + circleDynamicBean.getData().getTotal() + ")");
                                }else {
                                    ((PerSonHomePageActivity)getActivity()).setCircleDynamicText("动态");
                                }
                            }
                        }
                        List<CircleDynamicBean.DataBean.DynamicRespBean> listBeans = circleDynamicBean.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            mdatalist.addAll(listBeans);
                            adForExampleAdapter.replaceData(mdatalist);
                            adForExampleAdapter.setEnableLoadMore(true);
                            adForExampleAdapter.loadMoreComplete();
                        } else {
                            adForExampleAdapter.replaceData(mdatalist);
                            adForExampleAdapter.loadMoreEnd();
                        }
                    }else {
                        adForExampleAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    MyDynamicAdapter.DynamicClickListener dynamicClickListener=new MyDynamicAdapter.DynamicClickListener() {

        @Override
        public void circleClick(int position) {
            //跳到圈子首页
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityCircleDetail.startActivity(getActivity(),String.valueOf(listBean.getPlatformId()));
            }catch (Exception e){

            }
        }

        @Override
        public void contentDynamicClick(int position) {
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityPostDetail.startActivity(getActivity(),String.valueOf(listBean.getId()));
            }catch (Exception e){

            }
        }

        @Override
        public void headClick(int position) {
            //跳往个人主页
            try {
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                PerSonHomePageActivity.startActivity(getActivity(), String.valueOf(listBean.getUserId()));
            }catch (Exception e){

            }
        }

        @Override
        public void praiseClick(int position) {
            try {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(getActivity());
                    return;
                }
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                if(!listBean.isIsLike())
                praiseRequest(String.valueOf(listBean.getId()),position,String.valueOf(listBean.getPlatformId()));
            }catch (Exception e){

            }
        }

        @Override
        public void commentClick(int position) {
            //跳往动态详情，并且定位到评论区域
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityPostDetail.startActivity(getActivity(),String.valueOf(listBean.getId()),"comment");
            }catch (Exception e){

            }
        }

        @Override
        public void imageDynamicClick(int index,int pos) {

            //不同类型跳往不同页面
            //普通动态跳往图片预览，书籍跳往网页走url识别，周报跳往周报网页
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(pos);
                if( listBean.getPostType() == 3){
                    //书籍页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        ActivityWebView.startActivity(getActivity(),circleBookBean.getUrl(),Integer.parseInt(circleBookBean.getBookId()));
                    }catch (Exception e){
                    }
                }else if( listBean.getPostType() == 2){
                    //内站书籍页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        UrlReadActivity.startActivityInsideRead(getActivity(), String.valueOf(circleBookBean.getBookId()),circleBookBean.getSource(),false,"",circleBookBean.getChapterName(),0,false);
                    }catch (Exception e){
                    }
                }else if(listBean.getPostType() == 4 || listBean.getPostType() == 5 || listBean.getPostType() == 6|| listBean.getPostType() == 7){
                    //周报等页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        ActivityWebView.startActivity(getActivity(),circleBookBean.getUrl(),listBean.getTitleShow(),listBean.getContentShow(),circleBookBean.getCover(),2);
                    }catch (Exception e){
                    }
                }else {
                    //普通动态
                    ArrayList<String> listtemp = new ArrayList<String>();
                    listtemp.addAll(listBean.getImages());
                    ActivityImagesPreview.startActivity(getActivity(),listtemp,index);
                }
            }catch (Exception e){
            }
        }

        @Override
        public void moreOptionClick(int position,View view) {

            if(!RxLoginTool.isLogin()){
                ActivityLoginNew.startActivity(getActivity());
                return;
            }

            if(null == moreOptionPopup) {
                moreOptionPopup = new MoreOptionPopup(getActivity());
            }
            moreOptionPopup.getTv_report().setOnClickListener(
                    v -> {
                        try {
                            //举报5：圈子动态 6：圈子评论 7：圈子回复
                            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                            againstCircleUser("5",String.valueOf(listBean.getId()));
                        }catch (Exception e){

                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.getTv_delete().setOnClickListener(
                    v -> {
                        try {
                            //删除
                            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("contentType", 1);
                            jsonObject.addProperty("objectId", listBean.getId());
                            requestDelete(jsonObject.toString(),position);
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
            //显示蒙层
            if(getActivity() instanceof PerSonHomePageActivity){
                //圈子详情页
                ((PerSonHomePageActivity)getActivity()).getMask_view().setVisibility(View.VISIBLE);
            }

            try {
                //区分是自己还是别人的动态
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                AccountDetailBean account = RxTool.getAccountBean();
                if(null != account){
                    if(account.getData().getUid().equals(String.valueOf(listBean.getUserId()))){
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                    }else {
                        moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                        moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                    }
                }else {
                    moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                    moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            moreOptionPopup.showPopupWindow(getActivity(),view);
        }
    };

    /*动态点赞*/
    public void praiseRequest(String objectId,int postion,String circleId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("likeType",1);
        jsonObject.addProperty("objectId", objectId);
        jsonObject.addProperty("platformId", circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/like", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            //此处展示金钻消耗toast
                            RxToast.showJinzuanCharge(circleDynamicBean);

                        //更新点赞状态
                        CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)  mdatalist.get(postion);
                        int count = listBean.getLikeCount();count++;
                        listBean.setIsLike(true);
                        listBean.setLikeCount(count);
                        mdatalist.set(postion,listBean);
                        adForExampleAdapter.replaceData(mdatalist);
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除动态
     */
    private void requestDelete(String json,int postion) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/delete",json, new CallBackUtil.CallBackString() {
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
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        adForExampleAdapter.remove(postion);
                        notifyAll();
                        RxToast.custom("删除成功").show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 举报圈子用户
     */
    private void againstCircleUser(String source,String userId){
        HashMap map=new HashMap();
        map.put("source",source);
        map.put("targetId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/against/circleUser",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("举报成功").show();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
