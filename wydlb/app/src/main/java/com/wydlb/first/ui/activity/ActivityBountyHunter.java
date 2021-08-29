package com.wydlb.first.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.Time;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.DataSynEvent;
import com.wydlb.first.bean.GateWayResponse;
import com.wydlb.first.bean.GetOrderByOrderNo;
import com.wydlb.first.bean.GetRewaredPoolDetailBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.adapter.BountyItemAdapter;
import com.wydlb.first.ui.contract.BountyHunterContract;
import com.wydlb.first.ui.presenter.BountyHunterPresenter;
import com.wydlb.first.utils.DividerItemDecoration;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxAutoLinearLayoutManager;
import com.wydlb.first.utils.RxDataTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxTimeTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.OverlapLogoView;
import com.wydlb.first.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 一键赏页面
 */

public class ActivityBountyHunter extends BaseActivity implements BountyHunterContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.amount_tv)
    TextView amount_tv;

    @Bind(R.id.readamount_des_tv)
    TextView readamount_des_tv;

    @Bind(R.id.headview_counttv)
    OverlapLogoView headview_counttv;

    @Bind(R.id.join_button)
    ImageButton join_button;

    @Bind(R.id.bounty_count_tv)
    TextView bounty_count_tv;

    @Bind(R.id.my_record_tv)
    TextView my_record_tv;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    //赏金猎人动态视图
    @Bind(R.id.bounty_hunter_rl)
    RelativeLayout bounty_hunter_rl;

    @Bind(R.id.jinridashang_tv)
    TextView jinridashang_tv;

    @Bind(R.id.amount_des_tv)
    TextView amount_des_tv;

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.nestedscroll)
    NestedScrollView nestedscroll;



    BountyItemAdapter mAdapter;
    List<GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean> booksBeanList = new ArrayList<>();



    @Inject
    BountyHunterPresenter bountyHunterPresenter;

    private GetRewaredPoolDetailBean mdata;
    private int JOIN_CODE = 100;
    //打赏金额字符串
    private String amount;

    //今日或者昨日，今日为1，昨日为2
    private int todayorYesterday = 1;


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_bounty_hunter;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
        refresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //沉浸式状态栏
        SystemBarUtils.fullScreen(this);
//        SystemBarUtils.expandStatusBar(this);
//        SystemBarUtils.transparentStatusBar(this);
//        // 4.4及以上版本开启
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
//            setTranslucentStatus(true);
//        }

        bountyHunterPresenter.attachView(this);
        mAdapter = new BountyItemAdapter(this , booksBeanList);

        RxAutoLinearLayoutManager manager = new RxAutoLinearLayoutManager(ActivityBountyHunter.this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(mAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));


        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果自动滑动到最后一个位置，则此处状态为SCROLL_STATE_IDLE
                    RxAutoLinearLayoutManager lm = (RxAutoLinearLayoutManager) recyclerView
                            .getLayoutManager();

                    int position = lm.findLastCompletelyVisibleItemPosition();
                    int count = lm.getItemCount();
                    if(position == count-1 && null != recyclerview ){
                        lm.scrollToPosition(0);
                        recyclerview.smoothScrollToPosition(mAdapter.getItemCount());
                    }
                }

            }
        });

        //加载数据之后开始平滑滑动，一直到数据结尾，不考虑再次循环的事情。
//        booksBeanList.add(new BookListCategoryBean.DataBeanX.BookListBean.DataBean());
//        booksBeanList.add(new BookListCategoryBean.DataBeanX.BookListBean.DataBean());
//        booksBeanList.add(new BookListCategoryBean.DataBeanX.BookListBean.DataBean());
//        booksBeanList.add(new BookListCategoryBean.DataBeanX.BookListBean.DataBean());
//        mAdapter.setDatas(booksBeanList);
//        recyclerview.smoothScrollToPosition(mAdapter.getItemCount());
        recyclerview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!mAdapter.getDatas().isEmpty()) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    recyclerview.stopScroll();
                    if(event.getAction() == MotionEvent.ACTION_UP)
                    recyclerview.smoothScrollToPosition(mAdapter.getItemCount());
                }
                return true;
            }

        });

    }

    private void refresh(){
        showDialog();

        boolean boolcould = isCurrentInTimeScope(23, 55, 24, 00);
        if(boolcould){
            join_button.setBackgroundResource(R.mipmap.bounty_button_gray);
            join_button.setEnabled(false);
        }else {
            join_button.setBackgroundResource(R.mipmap.bounty_button);
            join_button.setEnabled(true);
        }
        //请求页面详情
        if(todayorYesterday == 1) {
            join_button.setVisibility(View.VISIBLE);
            bounty_hunter_rl.setVisibility(View.VISIBLE);
            jinridashang_tv.setText(getString(R.string.todaybounty));
            amount_des_tv.setText(getString(R.string.todayamount));
            tv_options.setText(getString(R.string.viewyesterday));
            ArrayMap map=new ArrayMap();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            map.put("dateStr", dateStr);
            bountyHunterPresenter.getRewaredPoolDetail(map);
        }else{
            join_button.setVisibility(View.INVISIBLE);
            bounty_hunter_rl.setVisibility(View.GONE);
            jinridashang_tv.setText(getString(R.string.yesterdaybounty));
            amount_des_tv.setText(getString(R.string.yesterdayamount));
            tv_options.setText(getString(R.string.returntoday));
            ArrayMap map=new ArrayMap();
            String dateStr = RxTimeTool.getYestoryDate("yyyy-MM-dd");
            map.put("dateStr", dateStr);
            bountyHunterPresenter.getRewaredPoolDetail(map);
        }
    }

//    public class mRecyclerView extends RecyclerView{
//
//        public mRecyclerView(Context context) {
//            super(context);
//        }
//        // 拦截事件；
//        public boolean onInterceptTouchEvent(MotionEvent e) {
//            // 拦截触摸事件；
//            return true;
//        }
//
//        public boolean onTouchEvent(MotionEvent e) {
//            // 消费事件；
//            return true;
//        }
//
//    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour
     * 开始小时，例如22
     * @param beginMin
     * 开始小时的分钟数，例如30
     * @param endHour
     * 结束小时，例如 8
     * @param endMin
     * 结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
// 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
// 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }



    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    @OnClick(R.id.tv_options)void yesterdayClick(){
     //昨日今日切换
        //请求页面详情
        if(todayorYesterday == 1) {
            todayorYesterday = 0;
        }else{
            todayorYesterday = 1;
        }

        //初始化显示
        amount_tv.setText("0.00");
        String[] arrString = new String[0];
        headview_counttv.initViewData(arrString,"0",String.format(getString(R.string.等X人正在参加),"0"));
        bounty_count_tv.setText("0.00" + getString(R.string.goldcoin));
        my_record_tv.setText(getString(R.string.none));
        booksBeanList = new ArrayList<>();
        mAdapter.setDatas(booksBeanList);


        refresh();

    }

    @OnClick(R.id.rule_tv)void ruleClick(){
        //参与规则点击事件
        RxActivityTool.skipActivity(this, ActivityJoinRule.class);
    }

    @OnClick(R.id.join_button)void joinClick(){
        //加入的点击事件
        RxActivityTool.skipActivityForResult(this, ActivityEnterAmount.class,JOIN_CODE);
    }



    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }


    @Override
    public void complete(String message) {
        showSeverErrorDialog(message);
    }



    @Override
    public void getRewaredPoolDetailSuccess(GetRewaredPoolDetailBean bean) {
        dismissDialog();

        amount_tv.setText(RxDataTool.format2Decimals(String.valueOf(bean.getData().getSumAmt())));
        String desstr = String.format(getString(R.string.bountyhunterdes),String.valueOf(bean.getData().getReadPool()));
        SpannableString contentSpan=new SpannableString(desstr);
        contentSpan.setSpan(new TextAppearanceSpan(ActivityBountyHunter.this,R.style.StyleBountyWhite),0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contentSpan.setSpan(new TextAppearanceSpan(ActivityBountyHunter.this,R.style.StyleBountyYellow),9,desstr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        readamount_des_tv.setText(contentSpan);

//        ArrayList<String> list = new ArrayList<String>();
//        ArrayList<Integer> listid = new ArrayList<Integer>();
//
//        int count = 0;
//        for(int i = 0; i < bean.getData().getUserDetailResps().size()&& count < 10 ; i++){
//            GetRewaredPoolDetailBean.DataBean.UserDetailRespsBean temp = bean.getData().getUserDetailResps().get(i);
//            if(!listid.contains(temp.getUserId())){
//                listid.add(temp.getUserId());
//                list.add(temp.getAvatar());
//                count ++ ;
//            }
//        }
        //修改字段未heads，根据接口的排序直接显示。
        List<String> list = bean.getData().getHeads();
        if(null != list && !list.isEmpty()){
            String[] arrString = (String[])list.toArray(new String[list.size()]);
            headview_counttv.initViewData(arrString,String.valueOf(bean.getData().getCountUsers()),String.format(getString(R.string.等X人正在参加),String.valueOf(bean.getData().getCountUsers())));
        }



        String todaystr = RxDataTool.format2Decimals(String.valueOf(bean.getData().getUserSumAmt())) + getString(R.string.goldcoin);
        SpannableString todaystrSpan=new SpannableString(todaystr);
        todaystrSpan.setSpan(new TextAppearanceSpan(ActivityBountyHunter.this,R.style.StyleBountyRedBig),0,todaystr.length() - 2 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        todaystrSpan.setSpan(new TextAppearanceSpan(ActivityBountyHunter.this,R.style.StyleBountyRedSmall),todaystr.length() - 2,todaystr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bounty_count_tv.setText(todaystrSpan);

        if(bean.getData().getUserRank() > 0){
            my_record_tv.setText(String.valueOf(bean.getData().getUserRank()));
        }else {
            my_record_tv.setText(getString(R.string.none));
        }


        if(null != bean.getData().getUserDetailResps() && !bean.getData().getUserDetailResps().isEmpty()){
            booksBeanList = bean.getData().getUserDetailResps();
            mAdapter.setDatas(booksBeanList);
            recyclerview.smoothScrollToPosition(mAdapter.getItemCount());
        }else {
            booksBeanList = new ArrayList<>();
            mAdapter.setDatas(booksBeanList);
        }

        //滚到开头
        if(null != nestedscroll)
        nestedscroll.scrollTo(0,0);


    }

    @Override
    public void getOrderByOrderNoSuccess(GetOrderByOrderNo bean) {
        dismissDialog();
    }

    @Override
    public void placeRewardOrderSuccess(GateWayResponse bean) {
        dismissDialog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_CODE && resultCode == RESULT_OK) {
            //回复话题
            amount = data.getStringExtra("amount");
            ActivityChoosePayWay.startActivity(this,amount,"3");
        }
    }

    //事件监听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.Close_oldPage) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

}
