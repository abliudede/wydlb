package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.FirstBookReviewBean;
import com.lianzai.reader.bean.StartVersionBean;
import com.lianzai.reader.bean.UploadImageResponse;
import com.lianzai.reader.bean.UrlKeyValueBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.SplashActivity;
import com.lianzai.reader.ui.activity.ActivityAdvertisement;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityRealNameAuthFirst;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.location.ActivitySelectLocation;
import com.lianzai.reader.ui.adapter.AddImageAdapter;
import com.lianzai.reader.ui.contract.PostPublishContract;
import com.lianzai.reader.ui.presenter.PostPublishPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxPhotoTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CommentEditText;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lrz on 2017/10/14.
 * 发布动态
 */

public class ActivityPostCircle extends BaseActivity implements PostPublishContract.View{

    @Bind(R.id.tv_common_title)
    TextView tv_common_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    AddImageAdapter addImageAdapter;

    @Bind(R.id.rv_add_pic)
    RecyclerView rv_add_pic;

    @Bind(R.id.tv_choose_location)
    TextView tv_choose_location;
    @Inject
    PostPublishPresenter postPublishPresenter;

    @Bind(R.id.ed_content)
    CommentEditText ed_content;

    //书籍相关视图
//    @Bind(R.id.rl_book)
//    RelativeLayout rl_book;
//    @Bind(R.id.iv_book_cover)
//    SelectableRoundedImageView iv_book_cover;
//    @Bind(R.id.tv_book_chapter_name)
//    TextView tv_book_chapter_name;
//    @Bind(R.id.tv_book_source)
//    TextView tv_book_source;
//    @Bind(R.id.tv_book_name)
//    TextView tv_book_name;
//
//    @Bind(R.id.tv_tip)
//    TextView tv_tip;


    private String circleId;
    private String type;//动态类型，2,3内站书外站书书评，30普通图文动态


    private String bookId;
    private String bookName;
    private String chapterId;
    private String chapterName;
    private String cover;
    private String source;
    private String url;
    private String address;
    private double latitude;
    private double longitude;

    private int REQUEST_LIST_CODE = 1002;
    private ISListConfig config;
    //还没处理的图片数
    private int toDoCount = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_dynamic_post;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);

    }

    public static void startActivity(Context context,String circleId){
        Intent intent=new Intent(context,ActivityPostCircle.class);
        intent.putExtra("circleId", circleId);
        intent.putExtra("type", "30");
        context.startActivity(intent);
    }

    public static void startActivity(Context context,String circleId,String bookId,String bookName,String chapterId,String chapterName
    ,String cover,String source,String url){
        Intent intent=new Intent(context,ActivityPostCircle.class);
        intent.putExtra("circleId", circleId);
        //判断是否是内站书
            if(source.contains("lianzai.com")){
                intent.putExtra("type", "2");
            }else {
                intent.putExtra("type", "3");
            }
        intent.putExtra("bookId", bookId);
        intent.putExtra("bookName", bookName);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("chapterName", chapterName);
        intent.putExtra("cover", cover);
        intent.putExtra("source", source);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void initDatas() {

    }
    ArrayList<String>imgs=new ArrayList<>();
    ArrayList<String>imgsUp=new ArrayList<>();
    @Override
    public void configViews(Bundle savedInstanceState) {
        postPublishPresenter.attachView(this);

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tv_common_title.setText("发表动态");
        tv_common_title.setCompoundDrawablePadding(RxImageTool.dp2px(4f));
        tv_common_title.setCompoundDrawables(null,null,RxImageTool.getDrawable(R.mipmap.icon_circle_question),null);
        tv_options.setText("发表");
        tv_options.setTextColor(Color.WHITE);
        tv_options.setBackground(getResources().getDrawable(R.drawable.post_btn_gray_corner_bg));


        try {
            circleId = getIntent().getStringExtra("circleId");
            type = getIntent().getStringExtra("type");
        }catch (Exception e){
        }


        if(!"30".equals(type)){
            //是书评的情况
            try{
                bookId = getIntent().getStringExtra("bookId");
                bookName = getIntent().getStringExtra("bookName");
                chapterId = getIntent().getStringExtra("chapterId");
                chapterName = getIntent().getStringExtra("chapterName");
                cover = getIntent().getStringExtra("cover");
                source = getIntent().getStringExtra("source");
                url = getIntent().getStringExtra("url");
                //增加书籍内容显示
//                rl_book.setVisibility(View.VISIBLE);
//                rv_add_pic.setVisibility(View.GONE);
//                RxImageTool.loadImage(ActivityPostCircle.this,cover,iv_book_cover);
//                tv_book_chapter_name.setText(chapterName);
//                tv_book_source.setText("来源：" + source);
//                tv_book_name.setText("书名：" + bookName);
//                requestFirstPost();
                //不显示内站书来源
//                if(TextUtils.isEmpty(source) || source.contains("lianzai.com") || source.contains("bendixing.net") ){
//                    tv_book_source.setText("来源：连载阅读");
//                }
            }catch (Exception e){
            }
        }else {
            initSelector();
            setMax(9);

            //增加图片区域显示
//            rl_book.setVisibility(View.GONE);
            rv_add_pic.setVisibility(View.VISIBLE);
            imgs.add("add");
            imgsUp.add("add");
            int columnCount=3;
            int width=(RxDeviceTool.getScreenWidth(ActivityPostCircle.this)- RxImageTool.dip2px(32))/columnCount;
            addImageAdapter=new AddImageAdapter(imgs,this,width);
            addImageAdapter.setOnImageClickListener(new AddImageAdapter.OnImageClickListener() {
                @Override
                public void closeImgClick(int pos) {
                    imgs.remove(pos);
                    imgsUp.remove(pos);
                    addImageAdapter.replaceData(imgs);
                    if (!imgs.contains("add")){
                        imgs.add("add");
                        addImageAdapter.replaceData(imgs);
                    }
                    if (!imgsUp.contains("add")){
                        imgsUp.add("add");
                    }
                }

                @Override
                public void previewImgClick(int pos) {
                    ArrayList<String>previewList=new ArrayList<>();
                    previewList.addAll(imgs);
                    previewList.remove("add");
                    ActivityImagesPreview.startActivity(ActivityPostCircle.this,previewList,pos);
                }

                @Override
                public void addImgClick(int i) {
                    if (addImageAdapter.getItemCount()>9){
                        imgs.remove("add");
                        imgsUp.remove("add");
                        addImageAdapter.replaceData(imgs);
                        return;
                    }
                    if (addImageAdapter.getItem(i).equals("add")){
                        //打开图片选择器,设置最大选择张数
                        int hasSize = imgs.size();
                        if(imgs.contains("add")){
                            hasSize--;
                        }
                        if(hasSize < 9){
                            setMax(9 - hasSize);
                            // 跳转到图片选择器
                            ISNav.getInstance().toListActivity(ActivityPostCircle.this, config, REQUEST_LIST_CODE);
                        }
                    }
                }
            });
            GridLayoutManager manager = new GridLayoutManager(this,3);
            rv_add_pic.setLayoutManager(manager);
            rv_add_pic.addItemDecoration(new RxRecyclerViewDividerTool(0,0,0,RxImageTool.dip2px(10)));
            rv_add_pic.setAdapter(addImageAdapter);
        }


        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = ed_content.getSelectionStart();
                if (index >= 3 && s.toString().substring(index - 3, index).equals("\n\n\n")) {
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0, index - 1));
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }
                if (index >= 3 && s.toString().substring(index - 3, index).equals("\n\n ")) {
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0, index - 1));
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }
                if (index >= 1 && s.length() >= index + 2 && s.toString().substring(index - 1, index + 2).equals("\n\n\n")) {
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }
                if (index >= 1 && s.length() >= index + 2 && s.toString().substring(index - 1, index + 2).equals(" \n\n")) {
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }
                if (index >= 2 && s.length() >= index + 1 && s.toString().substring(index - 2, index + 1).equals("\n\n\n")) {
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }
                if (index >= 2 && s.length() >= index + 1 && s.toString().substring(index - 2, index + 1).equals("\n \n")) {
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index - 1);
                }

                //控制发表按钮是否可用
                if ((ed_content.getText().toString().length()<1 && imgsUp.size() <= 1) || toDoCount > 0){
                    tv_options.setEnabled(false);
                    tv_options.setBackground(getResources().getDrawable(R.drawable.post_btn_gray_corner_bg));
                }else{
                    tv_options.setEnabled(true);
                    tv_options.setBackground(getResources().getDrawable(R.drawable.post_btn_normal_corner_bg));
                }
            }
        });
        //防止发帖点击太快，发重复贴
        tv_options.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                if (ed_content.getPublishText().length()<1 && imgsUp.size() <= 1){
                    RxToast.custom("动态内容不能不少于1个字符").show();
                    return;
                }

                showDialog();
                getOneUrlResult(ed_content.getPublishText());
            }
        });
    }

    //实时解析网页链接
    public void getOneUrlResult(String content) {
        if (!TextUtils.isEmpty(content)) {
            //获取链接数组和新拼接的带空格字符串
            List<String> urlList = URLUtils.getUrlsInContent(content);
            //有链接时。
            if (urlList != null && !urlList.isEmpty()) {
                List<UrlKeyValueBean.DataBean> mList = new ArrayList<>();
                for (int i = 0 ; i <  urlList.size() ; i++) {
                    //从后往前的顺序显示。
                    String temp = urlList.get(i);
                    //去解析
                    //解析网址，解析成功则显示解析后的结果
                    Observable.create(new ObservableOnSubscribe<Map>() {
                        @Override
                        public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                            Map map = null;
                            try {
                                //这里开始是做一个解析，需要在非UI线程进行
                                Document document = Jsoup.parse(new URL(temp), 5000);
                                String title = document.head().getElementsByTag("title").text();
                                map = new HashMap();
                                map.put("code", "1");
                                map.put("title", title);
                                emitter.onNext(map);
                            } catch (IOException e) {
                                map = new HashMap();
                                map.put("code", "0");
                                emitter.onNext(map);
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Map>() {
                                @Override
                                public void accept(Map map) throws Exception {

                                    if (map.get("code").equals("1") && !TextUtils.isEmpty(map.get("title").toString())) {
                                        String title = map.get("title").toString();
                                        if (title.length() > 20) title = title.substring(0, 19) + "...";
                                        //存入列表
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle(title);
                                        mList.add(info);
                                    } else {
//                                        String[] str = temp.split("\\.");
//                                        if (str.length > 1) {
//                                            //存入数据库
//                                            String str1 = str[str.length - 2];
//                                            String str2 = str[str.length - 1];
//                                            String[] strtemp = str2.split("\\/");
//                                            if(strtemp.length > 1){
//                                                str2 = strtemp[0];
//                                            }
//                                            StringBuilder sb = new StringBuilder();
//                                            sb.append(str1);
//                                            sb.append(".");
//                                            sb.append(str2);
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = sb.toString();
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        } else {
//                                            //存入数据库
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = temp;
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        }
                                        //存入数据库
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle("网页链接");
                                        mList.add(info);
                                    }

                                    //判断是不是所有的链接都有了结果
                                    if(mList.size() == urlList.size()){
                                        UrlKeyValueBean uploadBean = new UrlKeyValueBean();
                                        uploadBean.setList(mList);
                                        String uploadStr = GsonUtil.toJsonString(uploadBean);
                                        upload(uploadStr);
                                    }
                                }
                            });
                }
            }else {
                upload("");
            }
        }else {
            upload("");
        }
    }

    private void upload(String uploadStr){

        JsonArray jsonArray = new JsonArray();
        for(String str:imgsUp){
            if (!str.equals("add") && !TextUtils.isEmpty(str)) {
                jsonArray.add(str);
            }
        }
        String content=ed_content.getPublishText();
        JSONObject jsonObject=new JSONObject();
        //定位信息不为空时，加入定位信息
        if(!TextUtils.isEmpty(address) && latitude != 0 && longitude != 0 ){
            jsonObject.put("address",address);
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);
        }
        jsonObject.put("content",content);
        jsonObject.put("urlTitle",uploadStr);
        jsonObject.put("platformId",circleId);
        jsonObject.put("postType",type);
        //普通动态上传图片
        if("30".equals(type)){
            if(jsonArray.size() > 0)
                jsonObject.put("pictures",jsonArray.toString());
        }else {
            //书评动态上传书评信息
            JSONObject bookJson = new JSONObject();
            bookJson.put("bookId",bookId);
            bookJson.put("bookName",bookName);
            bookJson.put("chapterId",chapterId);
            bookJson.put("chapterName",chapterName);
            bookJson.put("cover",cover);
            bookJson.put("source",source);
            bookJson.put("url",url);
            jsonObject.put("pictures",bookJson.toJSONString());
            jsonObject.put("chapterId",chapterId);
        }
        postPublishPresenter.postPublish(jsonObject.toJSONString());

    }

    private void initSelector() {
        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                //普通图片方法
                RxImageTool.loadNormalImage(context, path, imageView);
            }
        });
    }

    private void setMax(int k){
        // 自由配置选项
        config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                // “确定”按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3865FE"))
                // 返回图标ResId
                .backResId(R.mipmap.icon_back_white)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3865FE"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(k)
                .build();

    }

    private void removeAdd(){
        if (addImageAdapter.getItemCount()>9){
            imgs.remove("add");
            imgsUp.remove("add");
            addImageAdapter.replaceData(imgs);
        }
    }

    /**
     * 图片上传请求
     * @param
     */
    private void uploadFileRequest(File file,int pos){
        if (null==file){
            RxToast.custom("文件不存在").show();
            dealUpLoad("",pos);
            return;
        }
        if (null!=file&&!file.exists()){
            RxToast.custom("文件不存在").show();
            dealUpLoad("",pos);
            return;
        }

        if (file.length() > 10 * 1024 * 1024) {
            RxToast.custom("文件过大").show();
            dealUpLoad("",pos);
            return;
        }

        //此处检查图片是否达到9张
        removeAdd();

        getDialog().setCanceledOnTouchOutside(false);
        Map map=new HashMap();
        map.put(Params.BUCKET,Constant.UPAI_BUCKET);
        int lastIndex=file.getName().lastIndexOf(".");
        String suffix=file.getName().substring(lastIndex,file.getName().length());
        map.put(Params.SAVE_KEY,"bar_post/android_"+System.currentTimeMillis()+ pos +""+suffix);
        UploadEngine.getInstance().formUpload(file, map, Constant.UPAI_OPERATER, UpYunUtils.md5(Constant.UPAI_PASSWORD), new UpCompleteListener() {
            @Override
            public void onComplete(boolean b, String s) {
                dealUpLoad(s,pos);
            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long l, long l1) {
                RxLogTool.e("onRequestProgress:"+l);
            }
        });

    }

    private void dealUpLoad(String s,int pos) {
        try{
            UploadImageResponse uploadImageResponse = GsonUtil.getBean(s, UploadImageResponse.class);
            //先处理数字
            toDoCount--;
            if (toDoCount <= 0) {
                dismissDialog();
            }
            //再处理结果
            if(null != uploadImageResponse) {
                if (uploadImageResponse.getCode() == 200) {
                    imgsUp.set(pos, uploadImageResponse.getUrl());
                } else {
                    imgs.set(pos, "");
                    imgsUp.set(pos, "");
                    RxToast.custom(uploadImageResponse.getMessage()).show();
                }
            }else {
                imgs.set(pos, "");
                imgsUp.set(pos, "");
                RxToast.custom(uploadImageResponse.getMessage()).show();
            }
        }catch (Exception e){

        }

        //发布按钮状态判断
        try{
            if ((ed_content.getText().toString().length()<1 && imgsUp.size() <= 1) || toDoCount > 0){
                tv_options.setEnabled(false);
                tv_options.setBackground(getResources().getDrawable(R.drawable.post_btn_gray_corner_bg));
                //去掉空字符串
                imgs.remove("");
                imgsUp.remove("");
                addImageAdapter.replaceData(imgs);
            }else{
                tv_options.setEnabled(true);
                tv_options.setBackground(getResources().getDrawable(R.drawable.post_btn_normal_corner_bg));
            }
        }catch (Exception e){
        }
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }

    @Override
    public void showError(String message) {
        RxToast.custom(message).show();
        dismissDialog();
    }

    @Override
    public void postPublish(JinZuanChargeBean bean) {
        dismissDialog();
        //刷新圈子列表
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_SWITCH_CIRCLE_LIST);
        //此处展示金钻消耗toast
        RxToast.showJinzuanCharge(bean);
        finish();
    }

    @Override
    public void complete(String message) {
        RxToast.custom(message).show();
        dismissDialog();
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @OnClick(R.id.tv_common_title)
    void tv_common_titleClick(){
        if(Constant.appMode == Constant.AppMode.RELEASE){
            ActivityWebView.startActivity(ActivityPostCircle.this,"https://fx.bendixing.net/taskCenter/#/circle");
        }else {
            ActivityWebView.startActivity(ActivityPostCircle.this,"https://beta.fx.bendixing.net/taskCenter/#/circle");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            //多图片选择结果
            List<String> pathList = data.getStringArrayListExtra("result");
            if(null != pathList){
                toDoCount = pathList.size();
                int hasSize = imgs.size();
                if(imgs.contains("add")){
                    hasSize--;
                }
                if(hasSize + toDoCount > 9){
                    //假如超过9条限定数字
                    int more = hasSize + toDoCount - 9;
                    toDoCount = toDoCount - more;
                    List<String> tempList = pathList.subList(0, toDoCount);
                    for (String path : tempList) {
                        dealResult(path);
                    }
                }else {
                    //假如在限定之内
                    for (String path : pathList) {
                        dealResult(path);
                    }
                }
            }

        }
      else if(resultCode==RESULT_OK&&requestCode==1001){//位置选择返回
            address =data.getStringExtra("address");
            latitude =data.getDoubleExtra("latitude",0d);
            longitude =data.getDoubleExtra("longitude",0d);
            Drawable drawable;
            if (!TextUtils.isEmpty(address)){
                drawable=RxImageTool.getDrawable(18,R.mipmap.icon_dynamic_post_selected_location);
                tv_choose_location.setText(address);
                tv_choose_location.setTextColor(Color.parseColor("#3865FE"));

            }else{
                drawable=RxImageTool.getDrawable(18,R.mipmap.icon_dynamic_not_select_location);
                tv_choose_location.setTextColor(Color.parseColor("#999999"));
                tv_choose_location.setText("所在位置");
            }
            tv_choose_location.setCompoundDrawables(drawable,null,RxImageTool.getDrawable(R.mipmap.icon_dynamic_arrow_right),null);

        }
    }

    private void dealResult(String path){
        try{
            String filePath = path;
            String dirPath= RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
            int degree=RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题
            if (degree>0){//旋转图片
                filePath=RxImageTool.amendRotatePhoto(filePath,filePath,this);
            }
            //压缩图片
            Luban.with(this).load(filePath).ignoreBy(100).setTargetDir(dirPath).filter(new CompressionPredicate() {
                @Override
                public boolean apply(String path) {
                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                }
            })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            showDialog();
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                            imgs.add(file.getAbsolutePath());
                            imgsUp.add(file.getAbsolutePath());
                            Collections.swap(imgs,imgs.size()-1,imgs.size()-2);
                            Collections.swap(imgsUp,imgsUp.size()-1,imgsUp.size()-2);
                            addImageAdapter.replaceData(imgs);
                            rv_add_pic.smoothScrollToPosition(imgs.size()-1);

                            int pos=imgs.size()-2;
                            uploadFileRequest(file,pos);//上传图片
                        }

                        @Override
                        public void onError(Throwable e) {
//                        RxToast.custom("图片压缩失败",Constant.ToastType.TOAST_ERROR).show();
                            //压缩失败就直接传原图吧
//                        // TODO 当压缩过程出现问题时调用
                            imgs.add(path);
                            imgsUp.add(path);
                            Collections.swap(imgs,imgs.size()-1,imgs.size()-2);
                            Collections.swap(imgsUp,imgsUp.size()-1,imgsUp.size()-2);
                            addImageAdapter.replaceData(imgs);
                            rv_add_pic.smoothScrollToPosition(imgs.size()-1);

                            int pos=imgs.size()-2;
                            uploadFileRequest(new File(path),pos);//上传图片
                        }
                    }).launch();
        }catch (Exception e){
            RxToast.custom("图片过大",Constant.ToastType.TOAST_ERROR).show();
        }
    }

//    private void dododo(){
//        filePath=RxPhotoTool.getImageAbsolutePath(this,path);
//
//        String dirPath= RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
//        RxFileTool.createOrExistsDir(dirPath);
//
//        int degree=RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题
//
//        if (degree>0){//旋转图片
//            filePath=RxImageTool.amendRotatePhoto(filePath,dirPath+"/"+System.currentTimeMillis()+".jpg",this);
//            RxLogTool.e("degree new path:"+filePath);
//        }
//
//        //压缩图片
//        Luban.with(this).load(filePath).ignoreBy(100).setTargetDir(dirPath).filter(new CompressionPredicate() {
//            @Override
//            public boolean apply(String path) {
//                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
//            }
//        })
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                        showDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                        imgs.add(file.getAbsolutePath());
//                        Collections.swap(imgs,imgs.size()-1,imgs.size()-2);
//                        addImageAdapter.setNewData(imgs);
//                        rv_add_pic.smoothScrollToPosition(imgs.size()-1);
//
//                        int pos=imgs.size()-2;
//                        uploadFileRequest(file,pos);//上传图片
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        RxToast.custom("图片压缩失败",Constant.ToastType.TOAST_ERROR).show();
//                        // TODO 当压缩过程出现问题时调用
//                        imgs.add(filePath);
//                        Collections.swap(imgs,imgs.size()-1,imgs.size()-2);
//                        addImageAdapter.setNewData(imgs);
//                        rv_add_pic.smoothScrollToPosition(imgs.size()-1);
//
//                        int pos=imgs.size()-2;
//                        uploadFileRequest(new File(filePath),pos);//上传图片
//                    }
//                }).launch();
//        RxLogTool.e("degree:"+degree);
//    }


    /**
     * 定位获取
     */
    @OnClick(R.id.tv_choose_location)void chooeseLocationClick(){
        String str=tv_choose_location.getText().toString();
        ActivitySelectLocation.startActivity(this,str.equals("所在位置")?"":str);
    }

//    private void requestFirstPost(){
//
//            OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/isFirstBookReview", new CallBackUtil.CallBackString() {
//                @Override
//                public void onFailure(Call call, Exception e) {
//                    try{
////                        RxToast.custom("网络错误").show();
//                    }catch (Exception ee){
//                    }
//                }
//
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        FirstBookReviewBean baseBean = GsonUtil.getBean(response, FirstBookReviewBean.class);
//                        if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                            if(baseBean.getData().isFirstBookReviewFlag()){
//                                tv_tip.setVisibility(View.VISIBLE);
//                                SpannableString contentSpan=new SpannableString("首次发表本书的有效书评（大于50字且非灌水），可获得" + baseBean.getData().getRewardAmont() +"金钻额外奖励哦~");
//                                contentSpan.setSpan(new TextAppearanceSpan(ActivityPostCircle.this,R.style.YellowText),28,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                tv_tip.setText(contentSpan);
//                                tv_tip.setOnClickListener(
//                                        v -> {
//                                            tv_tip.setVisibility(View.GONE);
//                                        }
//                                );
//                            }
//                        }else{//加载失败
//                            RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//    }


}

