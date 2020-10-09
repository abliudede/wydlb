package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.adapter.CategoryAdapterNew;
import com.lianzai.reader.ui.contract.NewReadContract;
import com.lianzai.reader.ui.presenter.NewReadPresenter;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.page.ChapterUrlsVo;
import com.lianzai.reader.view.page.PageParagraphVo;
import com.lianzai.reader.view.page.TxtChapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2019/05/14
 * 内站书目录
 */

public class ActivityInsideBookMulu extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener,NewReadContract.View {

    @Bind(R.id.tv_book_total_chapter)//总共章节
            TextView tv_book_total_chapter;

    @Bind(R.id.iv_sort_category)//排序
            ImageView iv_sort_category;
    private boolean isSortDesc = true;//书本目录

    //目录列表
    @Bind(R.id.read_iv_category)
    ListView mLvCategory;

    CategoryAdapterNew mCategoryAdapter;
    List<BookChapterBean> categoryChaptersList = new ArrayList<>();//目录列表

    @Inject
    NewReadPresenter mPresenter;

    private String bookId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inside_bookmulu;
    }

    public static void startActivity(Context context,String bookId){
        RxActivityTool.removeActivityInsideBookMulu();
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        RxActivityTool.skipActivity(context,ActivityInsideBookMulu.class,bundle);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
        onRefresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mPresenter.attachView(this);

        try {
            bookId = getIntent().getStringExtra("bookId");
        }catch (Exception e){
        }

        //目录列表初始化
        mCategoryAdapter = new CategoryAdapterNew();
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);

        //书籍章节切换
        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if (!isSortDesc) {//倒序
                        position = categoryChaptersList.size() - position - 1;
                    }
                    //跳到指定章节
                    UrlReadActivity.startActivityInsideRead(ActivityInsideBookMulu.this,bookId,"",false,"",categoryChaptersList.get(position).getTitle(),0,false);
                }
        );

//        onRefresh();
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    @OnClick(R.id.begin_read_tv)
    void begin_read_tvClick() {
       SkipReadUtil.normalRead(ActivityInsideBookMulu.this,bookId,"",false);
    }



    /**
     * 排序
     */
    @OnClick(R.id.iv_sort_category)
    void sortCategoryClick() {
        if (isSortDesc) {//顺序排序
            iv_sort_category.setImageResource(R.mipmap.icon_category_asc);
        } else {
            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);
        }
        isSortDesc = !isSortDesc;
        //数据倒序
        sortCategory();
    }

    @Override
    public void gc() {
        RxToast.gc();
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        request();
    }

    private void sortCategory() {
        if (null != categoryChaptersList) {
            Collections.reverse(categoryChaptersList);
            mCategoryAdapter.setSortDesc(isSortDesc);
            mCategoryAdapter.refreshItems(categoryChaptersList);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isSortDesc) {

                        //先判断云端记录
                        CloudRecordBean bean = SkipReadUtil.getCloudRecord(bookId);
                        if(null != bean){
                            //遍历目录
                            String title = bean.getChapterTitle();
                            for(int i = 0;i <categoryChaptersList.size();i++ ){
                                if(categoryChaptersList.get(i).getTitle().equals(title)){
                                    mLvCategory.setSelection(i);
                                    break;
                                }
                            }
                        }


                    } else {
                        mLvCategory.setSelection(0);
                    }

                }
            }, 100);
        }

    }


   private void request(){
       ArrayMap<String, Object> map = new ArrayMap();
       //大于50000000的书籍，没有源的概念
       String bidStr = "";
       if(Integer.parseInt(bookId) <= Constant.bookIdLine){
           //获取源字符串
           bidStr = SkipReadUtil.getSource(bookId);
           //获取本地存储的source
           String localSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
           if(!TextUtils.isEmpty(localSource)){
               map.put("source", localSource);
           }
       }

       mPresenter.urlLoadCategory(bookId, map, "","","",bidStr);//,chapterUrl
   }


    @Override
    public void getBookDirectoryView(CollBookBean collBookBean, boolean isUpdateCategory, boolean needReopen) {

    }

    @Override
    public void getBookDirectoryView2(CollBookBean collBookBean, boolean isUpdateCategory) {
        //总共章节数
        if (null != collBookBean.getBookChapterList()) {
            tv_book_total_chapter.setText("共" + collBookBean.getBookChapters().size() + "章");
        } else {
            tv_book_total_chapter.setText("共0章");
        }

        //置为顺序排序
        iv_sort_category.setImageResource(R.mipmap.icon_category_desc);
        isSortDesc = true;
        //目录数据初始化
        categoryChaptersList.clear();
        categoryChaptersList.addAll(collBookBean.getBookChapterList());
        mCategoryAdapter.refreshItems(categoryChaptersList);
        if (mCategoryAdapter.getCount() > 0) {
            int position = 0;
            //先判断云端记录
            CloudRecordBean bean = SkipReadUtil.getCloudRecord(bookId);
            if(null != bean){
                //遍历目录
                String title = bean.getChapterTitle();
                for(int i = 0;i <categoryChaptersList.size();i++ ){
                    if(categoryChaptersList.get(i).getTitle().equals(title)){
                        position = i;
                        break;
                    }
                }
            }

            mLvCategory.setSelection(position);
            mCategoryAdapter.setChapter(position);
        }

    }

    @Override
    public void getBookDirectoryErrorView(String message) {

    }

    @Override
    public void getBookDirectoryTimeout() {

    }

    @Override
    public void vipChapter(TxtChapter txtChapter) {

    }


    @Override
    public void returnListenBookDataView(List<PageParagraphVo> pageParagraphVos) {

    }

    @Override
    public void returnChapterUrlsView(List<ChapterUrlsVo> chapterUrlsVos) {

    }


    @Override
    public void showError(String message) {
    }

    @Override
    public void complete(String message) {
    }
}
