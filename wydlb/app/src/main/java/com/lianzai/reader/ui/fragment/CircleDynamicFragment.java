package com.lianzai.reader.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.lianzai.reader.bean.CircleInfoBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.PlacedTopListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityInsideBookMulu;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.activity.circle.ActivityRelatedLinks;
import com.lianzai.reader.ui.adapter.CircleDynamicAdapter;
import com.lianzai.reader.ui.adapter.CircleTopAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogMuteOptions;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 圈子-动态页面
 */
public class CircleDynamicFragment extends BaseFragment implements NativeExpressAD.NativeExpressADListener{

    @Bind(R.id.recyclerView)
    RecyclerView rv_list;

    CustomLoadMoreView customLoadMoreView;
    CircleDynamicAdapter circleDynamicAdapter;

    int pageNum = 1;
    int pageSize=10;

    String nextTime = "";

    List<Object> mdatalist=new ArrayList<>();

    View headerView;
    RelativeLayout rl_chapter_view;
    private TextView tv_circle_update_chapter;
    private TextView tv_circle_chapter_date;

    //置顶模块
    LinearLayout recyclerView;

//    OverlapLogoView overlapLogoView;
    private String circleId;
    private int type;

    /*广点通腾讯广告联盟区域*/
    private NativeExpressAD mADManager;
    private List<NativeExpressADView> mAdViewList;
    private HashMap<NativeExpressADView, Integer> mAdViewIdMap = new HashMap<NativeExpressADView, Integer>();

    MoreOptionPopup moreOptionPopup;
    private RxLinearLayoutManager manager;
    private List<BannerBean.DataBean> attrs;
    private List<BannerBean.DataBean> ownAttrs;
    private List<BannerBean.DataBean> sdkAttrs;

    private int adCount = 0;


    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_circle_dynamic;
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
        // 使用完了每一个NativeExpressADView之后都要释放掉资源。
        if (mAdViewList != null) {
            for (NativeExpressADView view : mAdViewList) {
                view.destroy();
            }
        }
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

        circleDynamicAdapter=new CircleDynamicAdapter(mdatalist,getContext());
        circleDynamicAdapter.setDynamicClickListener(dynamicClickListener);


        customLoadMoreView = new CustomLoadMoreView();
        circleDynamicAdapter.setLoadMoreView(customLoadMoreView);
        circleDynamicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(type == 1) {
                    pageNum++;
                    circleDynamicRequest(false);
                }else {
                    pageNum++;
                    hotDynamicRequest(false);
                }
            }
        }, rv_list);

        manager = new RxLinearLayoutManager(getContext());
        rv_list.setLayoutManager(manager);
//        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0,0 , RxImageTool.dip2px(1),0));
        rv_list.setAdapter(circleDynamicAdapter);


        circleDynamicAdapter.setEmptyView(R.layout.view_dynamic_empty,rv_list);
        circleDynamicAdapter.setHeaderAndEmpty(true);

        onRefresh();
    }


    @Override
    public void fetchData() {
    }

    public void onRefresh(){
        if(type == 1) {
            circleDynamicRequest(true);
        }else {
            hotDynamicRequest(true);
        }
    }

    /**
     * 章节更新头部
     */
    private void initUpdateView(CircleDynamicBean.DataBean.ExtObjBean bean){
        if(null == headerView ){
            headerView= LayoutInflater.from(getActivity()).inflate(R.layout.item_circle_update_dynamic,null);
//            overlapLogoView=headerView.findViewById(R.id.olv_users_logo);
            rl_chapter_view = headerView.findViewById(R.id.rl_chapter_view);
            tv_circle_update_chapter=headerView.findViewById(R.id.tv_circle_update_chapter);
            tv_circle_chapter_date = headerView.findViewById(R.id.tv_circle_chapter_date);
            headerView.setOnClickListener(
                    v -> {
                        //如果本地记录也是空，那么进入相关链接页面
                        if(getActivity() instanceof ActivityCircleDetail){
                            //圈子详情页
                            CircleInfoBean circleInfo = ((ActivityCircleDetail) getActivity()).getCircleInfoBean();
                            if(null != circleInfo) {
                                if(circleInfo.getData().getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER ) {
                                    //内站书跳章节列表
                                    ActivityInsideBookMulu.startActivity(getActivity(),String.valueOf(circleInfo.getData().getBookId()));
                                }else if(circleInfo.getData().getPlatformType() == Constant.UserIdentity.BOOK_OUTSIED_NUMBER){
                                    //外站书跳相关链接
                                    ActivityRelatedLinks.startActivity(getActivity(), String.valueOf(circleInfo.getData().getBookId()), circleInfo.getData().getPlatformIntroduce());
                                }
                            }
                        }
                    }
            );
            recyclerView = headerView.findViewById(R.id.recyclerView);
            if (null!=circleDynamicAdapter){
                circleDynamicAdapter.addHeaderView(headerView,0);
            }
        }


        if(null == bean){
            rl_chapter_view.setVisibility(View.GONE);
        }else {
            rl_chapter_view.setVisibility(View.VISIBLE);
            try{
                ArrayList<String> listTemp = new ArrayList<>();
                int size = 0;
                if(null != bean.getHeadList() && !bean.getHeadList().isEmpty()){
                    size = bean.getHeadList().size();
//                List<String> listTemp1 = bean.getHeadList();
//                Collections.reverse(listTemp1);
                    if(size > 3){
                        listTemp.addAll(bean.getHeadList().subList(0,3));
                    }else {
                        listTemp.addAll(bean.getHeadList());
                    }
                    Collections.reverse(listTemp);
                }
                tv_circle_update_chapter.setText(bean.getChapterName());
                tv_circle_chapter_date.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getCreateTime())) + "更新");
            }catch (Exception e){

            }
        }

    }


    public void circleDynamicRequest(boolean isRefresh){
        if(isRefresh){
            adCount = 0;
            pageNum=1;
            nextTime = "";
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pageNumber",String.valueOf(pageNum));
        jsonObject.addProperty("nextTime",nextTime);
        jsonObject.addProperty("pageSize", String.valueOf(pageSize));
        jsonObject.addProperty("platformId", circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/circleDynamicNew", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxLogTool.e(e.toString());
                    circleDynamicAdapter.loadMoreEnd();
                }catch (Exception ex){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    CircleDynamicBean circleDynamicBean = GsonUtil.getBean(response, CircleDynamicBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            mdatalist.clear();
                            //只有刷新的时候更新
                            initUpdateView(circleDynamicBean.getData().getExtObj());
                            //增加头部置顶模块，刷新之后请求接口
                            if(getActivity() instanceof ActivityCircleDetail){
                                //圈子详情页
                                ((ActivityCircleDetail)getActivity()).placedTopListRequest("2");
                            }
                            //假书评只取第一页
                            List<CircleDynamicBean.DataBean.DynamicRespBean> tempListBeans = circleDynamicBean.getData().getLastChapterResp();
                            if(null != tempListBeans && !tempListBeans.isEmpty()){
                                //补充的假书评，假如存在，则插入列表中。并给每一个假书评添加一个标识符。
                                for(CircleDynamicBean.DataBean.DynamicRespBean item : tempListBeans){
                                    item.setCounterfeit(true);
                                    mdatalist.add(item);
                                }
                            }
                        }
                        List<CircleDynamicBean.DataBean.DynamicRespBean> listBeans = circleDynamicBean.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            mdatalist.addAll(listBeans);
                            circleDynamicAdapter.replaceData(mdatalist);
                            circleDynamicAdapter.setEnableLoadMore(true);
                            circleDynamicAdapter.loadMoreComplete();
                            //保存最后一条的时间
                            nextTime = listBeans.get(listBeans.size() - 1).getCreateTime();
                        } else {
                            circleDynamicAdapter.replaceData(mdatalist);
                            circleDynamicAdapter.loadMoreEnd();
                        }

                        attrs = circleDynamicBean.getData().getAttrs();
                        if(null != attrs) {
                            //广告插入开始
                            initNativeExpressAD();
                        }
                    }else {
                        circleDynamicAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void hotDynamicRequest(boolean isRefresh){
        if (isRefresh){
            adCount = 0;
            pageNum=1;
        }
        Map<String, String> map=new HashMap<>();
        map.put("pageNumber",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/hotDynamicNew/" + circleId, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    circleDynamicAdapter.loadMoreEnd();
                }catch (Exception ex){
//                    ex.printStackTrace();
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    CircleDynamicBean circleDynamicBean = GsonUtil.getBean(response, CircleDynamicBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            mdatalist.clear();
                            //只有刷新的时候更新
                            initUpdateView(circleDynamicBean.getData().getExtObj());
                            //增加头部置顶模块，刷新之后请求接口
                            if(getActivity() instanceof ActivityCircleDetail){
                                //圈子详情页
                                ((ActivityCircleDetail)getActivity()).placedTopListRequest("1");
                            }
                            //假书评只取第一页
                            List<CircleDynamicBean.DataBean.DynamicRespBean> tempListBeans = circleDynamicBean.getData().getLastChapterResp();
                            if(null != tempListBeans && !tempListBeans.isEmpty()){
                                //补充的假书评，假如存在，则插入列表中。并给每一个假书评添加一个标识符。
                                for(CircleDynamicBean.DataBean.DynamicRespBean item : tempListBeans){
                                    item.setCounterfeit(true);
                                    mdatalist.add(item);
                                }
                            }
                        }
                        List<CircleDynamicBean.DataBean.DynamicRespBean> listBeans = circleDynamicBean.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            mdatalist.addAll(listBeans);
                            circleDynamicAdapter.replaceData(mdatalist);
                            circleDynamicAdapter.setEnableLoadMore(true);
                            circleDynamicAdapter.loadMoreComplete();
                        } else {
                            circleDynamicAdapter.replaceData(mdatalist);
                            circleDynamicAdapter.loadMoreEnd();
                        }

                        attrs = circleDynamicBean.getData().getAttrs();
                        if(null != attrs) {
                            //广告插入开始
                            initNativeExpressAD();
                        }
                    }else {
                        circleDynamicAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
//                    e.printStackTrace();
                }
            }
        });
    }

    public void placedTopListRequest(String response){
        try{
            PlacedTopListBean placedTopListBean = GsonUtil.getBean(response, PlacedTopListBean.class);
            if (placedTopListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                //根据置顶数据显示
                recyclerView.removeAllViews();
                if (null != placedTopListBean.getData()) {
                    List<PlacedTopListBean.DataBean> tempList = placedTopListBean.getData();
                    if(!tempList.isEmpty()){
                        for(PlacedTopListBean.DataBean item :tempList){
                            CircleTopAdapter viewItem = new CircleTopAdapter(getActivity());
                            viewItem.bindData(item);
                            recyclerView.addView(viewItem);
                        }
                    }
                }
//                        rv_list.scrollToPosition(0);
            }
        }catch (Exception e){
        }
    }


    CircleDynamicAdapter.DynamicClickListener dynamicClickListener=new CircleDynamicAdapter.DynamicClickListener() {

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
                praiseRequest(String.valueOf(listBean.getId()),position);
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
                        //是否开启了自动转码
                        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
                            UrlReadActivity.startActivityOutsideRead(getActivity(),circleBookBean.getBookId(),circleBookBean.getSource(),false,"",circleBookBean.getChapterName(),0,false);
                        }else {
                            ActivityWebView.startActivityForReadTitle(getActivity(),circleBookBean.getUrl(),circleBookBean.getBookId(),circleBookBean.getChapterName(),circleBookBean.getSource(),false,0);
                        }

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
                                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                                AccountDetailBean account = RxTool.getAccountBean();
                                if(null != account) {
                                    if (account.getData().getUid().equals(String.valueOf(listBean.getUserId()))) {
                                        //删除自己的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("contentType", 1);
                                        jsonObject.addProperty("objectId", listBean.getId());
                                        deleteWay(jsonObject.toString(),1,position);
                                    } else {
                                        //删除别人的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("circleId", circleId);
                                        jsonObject.addProperty("objectType", 1);
                                        jsonObject.addProperty("objectId", listBean.getId());
                                        jsonObject.addProperty("objectUserId", listBean.getUserId());
                                        deleteWay(jsonObject.toString(),2,position);
                                    }
                                }else {
                                    ActivityLoginNew.startActivity(getActivity());
                                }
                            }catch (Exception e){

                            }
                            moreOptionPopup.dismiss();
                        }
                );
            moreOptionPopup.getTv_mute().setOnClickListener(
                    v -> {
                        try {
                            //禁言
                            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                            muteWay(listBean.getUserId());
                        }catch (Exception e){

                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //隐藏蒙层
                    if(getActivity() instanceof ActivityCircleDetail){
                        //圈子详情页
                        ((ActivityCircleDetail)getActivity()).getMask_view().setVisibility(View.GONE);
                    }
                }
            });
                //显示蒙层
            int userType = Constant.Role.FANS_ROLE;
                if(getActivity() instanceof ActivityCircleDetail){
                    //圈子详情页
                    ((ActivityCircleDetail)getActivity()).getMask_view().setVisibility(View.VISIBLE);
                    userType =  ((ActivityCircleDetail)getActivity()).getCircleInfoBean().getData().getUserType();
                }

            try {
                //区分是自己还是别人的动态
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                AccountDetailBean account = RxTool.getAccountBean();
                if(null != account){
                    boolean equal = false;
                    if (account.getData().getUid().equals(String.valueOf(listBean.getUserId()))) {
                        equal = true;
                    }else {
                        equal = false;
                    }
                    if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && equal){
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_mute().setVisibility(View.GONE);
                    }else if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && !equal){
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                    }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && equal){
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_mute().setVisibility(View.GONE);
                    }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && !equal){
                        moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                    }else if(equal){
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_mute().setVisibility(View.GONE);
                    }else {
                        moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                        moreOptionPopup.getTv_mute().setVisibility(View.GONE);
                    }

                }else {
                    moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                    moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                    moreOptionPopup.getTv_mute().setVisibility(View.GONE);
                }
            }catch (Exception e){

            }
            moreOptionPopup.showPopupWindow(getActivity(),view);
        }

        @Override
        public void closeClick(int position) {
            if (circleDynamicAdapter != null) {
                circleDynamicAdapter.removeADView(position);
                adCount -- ;
            }
        }
    };

    /*动态点赞*/
    public void praiseRequest(String objectId,int postion){
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
                        int count = listBean.getLikeCount();
                        count++;
                        listBean.setIsLike(true);
                        listBean.setLikeCount(count);
                        mdatalist.set(postion,listBean);
                        circleDynamicAdapter.replaceData(mdatalist);

                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                }catch (Exception e){
//                    e.printStackTrace();
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
                        circleDynamicAdapter.remove(postion);
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
         * 删除动态
         */
        private void requestDeleteOther(String json,int postion) {
            OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/deleteCircleContent",json, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    try{
//                        RxToast.custom("网络错误").show();
                    }catch (Exception ee){
                    }
                }

                @Override
                public void onResponse(String response) {
                    try {
                        ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                        if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            circleDynamicAdapter.remove(postion);
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

    RxDialogMuteOptions rxDialogMuteOptions;//禁言理由选项
    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示
    private void muteWay(int bannedUserId){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(getActivity());
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("禁言");
        rxDialogSureCancelNew.setContent("确定对Ta进行禁言吗？禁言后，Ta将不能再在圈子里发表任何言论。");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                requestMute(4,bannedUserId);
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    private void deleteWay(String json,int type,int pos){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(getActivity());
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("删除");
        rxDialogSureCancelNew.setContent("确定删除该内容吗？");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                if(type == 2){
                    requestDeleteOther(json,pos);
                }else if(type == 1){
                    requestDelete(json,pos);
                }
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    private void showMuteOptionsDialog(int bannedUserId){
        if (null == rxDialogMuteOptions) {
            rxDialogMuteOptions = new RxDialogMuteOptions(getActivity());
        }
        rxDialogMuteOptions.getTv_des1().setOnClickListener(
                v -> {
                    requestMute(1,bannedUserId);
                }
        );
        rxDialogMuteOptions.getTv_des2().setOnClickListener(
                v -> {
                    requestMute(2,bannedUserId);
                }
        );
        rxDialogMuteOptions.getTv_des3().setOnClickListener(
                v -> {
                    requestMute(3,bannedUserId);
                }
        );
        rxDialogMuteOptions.getTv_des4().setOnClickListener(
                v -> {
                    requestMute(4,bannedUserId);
                }
        );

    }

    /**
     * 禁言动态
     */
    private void requestMute(int bannedReplyType ,int bannedUserId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bannedReplyType", bannedReplyType);
        jsonObject.addProperty("bannedUserId", bannedUserId);
        jsonObject.addProperty("circleId", circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/bannedCircleMember",jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                        RxToast.custom("禁言成功").show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }



    /*广点通广告开始*/

    public HashMap<NativeExpressADView, Integer> getmAdViewPositionMap() {
        return circleDynamicAdapter.getmAdViewPositionMap();
    }

    public void setmAdViewPositionMap(HashMap<NativeExpressADView, Integer> mAdViewPositionMap) {
        circleDynamicAdapter.setmAdViewPositionMap(mAdViewPositionMap);
    }

    /**
     * 如果选择支持视频的模版样式，请使用
     * {Constants#NativeExpressSupportVideoPosID}
     */

    private void initNativeExpressAD() {
        if(null == sdkAttrs){
            sdkAttrs = new ArrayList<>();
        }
        if(null == ownAttrs){
            ownAttrs = new ArrayList<>();
        }
        sdkAttrs.clear();
        ownAttrs.clear();

        for(BannerBean.DataBean item :attrs){
            if(!TextUtils.isEmpty(item.getSdkChannel())){
                sdkAttrs.add(item);
            }else {
                ownAttrs.add(item);
            }
        }

        int sdkSize = sdkAttrs.size();
        if(sdkSize > 0){
            ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
            mADManager = new NativeExpressAD(getActivity(), adSize, Constant.TENCENTAPPID,Constant.TENCENTPOSTID2, this);
            mADManager.loadAD(sdkSize);
        }else {
            try{
                for(BannerBean.DataBean item :ownAttrs){
                    int position = item.getPutPosition();
                    circleDynamicAdapter.addADToPosition(position + adCount,item);
                    adCount ++;
                }
                ownAttrs.clear();
            }catch (Exception e){
            }
            circleDynamicAdapter.notifyDataSetChanged();
        }


    }


    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        mAdViewList = list;
        try{
            for (int i = 0; i < mAdViewList.size(); i++) {
                int position = sdkAttrs.get(i).getPutPosition();
                    NativeExpressADView view = mAdViewList.get(i);
                    int configId = sdkAttrs.get(i).getConfigId();
                    if (view.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        view.setMediaListener(mediaListener);
                    }
                    //存储广告视图和id的键值对
                    mAdViewIdMap.put(view,configId);
                if (position  + adCount >  mdatalist.size()) {
                    position  =  mdatalist.size() - adCount;
                }
                    getmAdViewPositionMap().put(view, position + adCount); // 把每个广告在列表中位置记录下来
                    circleDynamicAdapter.addADViewToPosition(position + adCount, view);
                    adCount ++;
            }
        }catch (Exception  e){
            RxLogTool.e("gdt_ad_mob" + e.toString());
        }

        try{
            for(BannerBean.DataBean item :ownAttrs){
                int position = item.getPutPosition();
                circleDynamicAdapter.addADToPosition(position + adCount,item);
                adCount ++;
            }
            ownAttrs.clear();
        }catch (Exception e){
            RxLogTool.e("gdt_ad_mob" + e.toString());
        }
        circleDynamicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        /*此处需要调用接口，告诉后台*/
        if(null != mAdViewIdMap){
            int tempConfigId = mAdViewIdMap.get(nativeExpressADView);
            saveStartAdvertisementStatistics(String.valueOf(tempConfigId));
        }
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        if (circleDynamicAdapter != null) {
            int removedPosition = getmAdViewPositionMap().get(nativeExpressADView);
            circleDynamicAdapter.removeADView(removedPosition);
            adCount -- ;
        }
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {
        try{
            for(BannerBean.DataBean item :ownAttrs){
                int position = item.getPutPosition();
                circleDynamicAdapter.addADToPosition(position + adCount,item);
                adCount ++;
            }
            ownAttrs.clear();
        }catch (Exception e){
        }
        circleDynamicAdapter.notifyDataSetChanged();
    }
    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ")
                        .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }
    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition());
            return videoBuilder.toString();
        }
        return null;
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
        }
    };
    /*广点通广告结束*/


    /**
     * 请求统计点击接口
     * 类型 1浏览 2点击
     */
    private void saveStartAdvertisementStatistics(String configId) {
        HashMap map = new HashMap();
        map.put("configId", configId);
        map.put("type", "2");
        map.put("num", "1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/startup/saveStartAdvertisementStatistics", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
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
//                    e.printStackTrace();
                }
            }
        });
    }

}
