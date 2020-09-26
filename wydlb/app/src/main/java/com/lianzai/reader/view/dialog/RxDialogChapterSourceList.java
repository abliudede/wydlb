package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.FeedBackTypeResponse;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.adapter.BookChapterSourceAdapter;
import com.lianzai.reader.ui.adapter.FeedbackTypesAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.page.UrlsVo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class RxDialogChapterSourceList extends RxDialog {

    private ImageView iv_cancel;

    private RecyclerView rv_source_list;

    BookChapterSourceAdapter bookChapterSourceAdapter;


    List<UrlsVo>dataBeanList=new ArrayList<>();

    ChapterChangeSourceListener chapterChangeSourceListener;

    Activity mActivity;

    ReadSettingsResponse readSettingsResponse;

    int currentSourcePosition=0;//智能源


    public RxDialogChapterSourceList(Activity activity, int themeResId) {
        super(activity, themeResId);
        mActivity=activity;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


    public ChapterChangeSourceListener getChapterChangeSourceListener() {
        return chapterChangeSourceListener;
    }

    public void setChapterChangeSourceListener(ChapterChangeSourceListener chapterChangeSourceListener) {
        this.chapterChangeSourceListener = chapterChangeSourceListener;
    }

    private void initView() {

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_source_list, null);

        rv_source_list=dialogView.findViewById(R.id.rv_source_list);

        iv_cancel = dialogView.findViewById(R.id.iv_cancel);

        bookChapterSourceAdapter=new BookChapterSourceAdapter(dataBeanList,mActivity);

        bookChapterSourceAdapter.addFooterView(mActivity.getLayoutInflater().inflate(R.layout.view_footer_chapter_source,null));

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(mActivity);
        rv_source_list.setLayoutManager(rxLinearLayoutManager);

        rv_source_list.setAdapter(bookChapterSourceAdapter);

        bookChapterSourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position!=currentSourcePosition){
                    if (null!=getChapterChangeSourceListener()){
                        getChapterChangeSourceListener().changeChapterSourceClick(bookChapterSourceAdapter.getData().get(position).getSource());
                    }
                    dismiss();
                }else{
                    dismiss();
                }

            }
        });

        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        iv_cancel.setOnClickListener(
                v->dismiss()
        );

        setFullScreen();

        skipTools();

    }

    @Override
    protected void onStart() {
        super.onStart();
        applyCompat();
    }


    /**
     * 转码配置读取
     */
    private void loadSettingsRequest() {
        OKHttpUtil.okHttpGet(Constant.BOOK_SOURCE_SETTINGS_URL, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                ReadSettingsResponse readSettingsResponse = GsonUtil.getBean(response, ReadSettingsResponse.class);
                RxSharedPreferencesUtil.getInstance().putObject(Constant.READ_SETTINGS_KEY, readSettingsResponse);
            }
        });
    }


    public void setData(List<UrlsVo>list,String source){
        RxLogTool.e("RxDialogChapterSourceList source:"+source);

        //转码配置获取
        loadSettingsRequest();

        List<UrlsVo> dataList=new ArrayList<>();

        UrlsVo initUrlsVo=new UrlsVo();
        initUrlsVo.setSourceName("智能源");
        dataList.add(initUrlsVo);

        dataList.addAll(list);

        readSettingsResponse= RxSharedPreferencesUtil.getInstance().getObject(Constant.READ_SETTINGS_KEY,ReadSettingsResponse.class);

        if (null!=bookChapterSourceAdapter&&null!=dataList){
            for(int i=1;i<dataList.size();i++){
                UrlsVo urlsVo=dataList.get(i);
                if (null!=readSettingsResponse){
                    for(ReadSettingsResponse.ReadSettingsBean readSettingsBean:readSettingsResponse.getReadSettings()){
                        if (urlsVo.getSource().equals(readSettingsBean.getSource())){
                            urlsVo.setSourceName(readSettingsBean.getSourceName());
                        }
                    }
                }

                if (!TextUtils.isEmpty(source)&&source.equals(urlsVo.getSource())){//选中
                    bookChapterSourceAdapter.setCurrentPos(i);
                    currentSourcePosition=i;
                }
            }

            bookChapterSourceAdapter.replaceData(dataList);
        }
    }

    public interface ChapterChangeSourceListener{
        void changeChapterSourceClick(String source);
    }



}
