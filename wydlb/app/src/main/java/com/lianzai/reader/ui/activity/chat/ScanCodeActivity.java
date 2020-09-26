package com.lianzai.reader.ui.activity.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ShareBitmapAnalysisResp;
import com.lianzai.reader.bean.ShareBitmapBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityBookList;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityTextShow;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostCircle;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.ui.fragment.TeamMessageFragment;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEncodeTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxPhotoTool;
import com.lianzai.reader.utils.RxRegTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.RxToast;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 扫描二维码
 */
public class ScanCodeActivity extends BaseActivity {

    private CaptureFragment captureFragment;
    private String filePath;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ScanCodeActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_code;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.fullScreen(this);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_scan_code);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initSelector();
        setMax();
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    private int REQUEST_LIST_CODE = 1002;
    private ISListConfig config;

    @OnClick(R.id.tv_album)
    void tv_albumClick() {
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                // 跳转到图片选择器
                ISNav.getInstance().toListActivity(ScanCodeActivity.this, config, REQUEST_LIST_CODE);
            }
            @Override
            public void noPermission() {

            }
        }, R.string.im_choose_picture, Manifest.permission.READ_EXTERNAL_STORAGE);
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

    private void setMax(){
        // 自由配置选项
        config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
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
                .cropSize(1, 1, 500, 500)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(1)
                .build();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            if(null != pathList){
                    for (String path : pathList) {
                        dealResult(path);
                    }
                }
            }



//        if (resultCode==RESULT_OK&&requestCode==RxPhotoTool.GET_IMAGE_FROM_PHONE){
//            try {
//                filePath = RxPhotoTool.getImageAbsolutePath(this, data.getData());
//            }catch (Exception e){
//                RxToast.custom("获取图片失败").show();
//                return;
//            }
//
//            if(TextUtils.isEmpty(filePath)){
//                RxToast.custom("获取图片失败").show();
//                return;
//            }
//
//            showDialog();
//
//            String dirPath= RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
//            RxFileTool.createOrExistsDir(dirPath);
//            int degree=RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题
//            if (degree>0){//旋转图片
//                filePath=RxImageTool.amendRotatePhoto(filePath,dirPath+"/"+System.currentTimeMillis()+".jpg",this);
//            }
//
//            //压缩图片
//            Luban.with(this).load(filePath).ignoreBy(100).setTargetDir(dirPath).filter(new CompressionPredicate() {
//                @Override
//                public boolean apply(String path) {
//                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
//                }
//            })
//                    .setCompressListener(new OnCompressListener() {
//                        @Override
//                        public void onStart() {
//                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                        }
//
//                        @Override
//                        public void onSuccess( File file) {
//                            // TODO 压缩成功后调用，返回压缩后的图片文件
//                            check(filePath);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                            RxToast.custom("图片压缩失败,将使用原图识别");
//                            // TODO 当压缩过程出现问题时调用 上传原图
//                            check(filePath);
//                        }
//                    }).launch();
//        }
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
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                           check(file.getAbsolutePath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            //压缩失败就直接传原图吧
//                        // TODO 当压缩过程出现问题时调用
                            check(path);
                        }
                    }).launch();
        }catch (Exception e){
            RxToast.custom("图片过大",Constant.ToastType.TOAST_ERROR).show();
        }
    }

    private void check(String path){
        CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                RxToast.custom("解析结果:" + result).show();
                dismissDialog();
                //请求接口，根据接口返回做操作。
                request(result);
            }

            @Override
            public void onAnalyzeFailed() {
                RxToast.custom("解析二维码失败").show();
                dismissDialog();
            }
        });
    }

    @Override
    public void gc() {

    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            RxLogTool.e("AnalyzeCallback result:"+result);
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            setResult(RESULT_OK, resultIntent);
//            RxToast.custom("解析结果:" +result).show();
//            finish();
            //请求接口，根据接口返回做操作。
            request(result);
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            RxToast.custom("无法识别该图片").show();
            finish();
        }
    };

    /**
     * 接口请求url
     * 类型 1书籍 2书单 3个人主页 4圈子主页 5活动主页 6首页 7登录页 8群聊 9书架
     */
    private void request(String url) {
        if(!RxRegTool.isURL(url)){
            //跳文字展示页面
            RxToast.custom(url).show();
            ActivityTextShow.startActivity(ScanCodeActivity.this,url);
            return;
        }

        Map<String, String> map=new HashMap<>();
        map.put("url", RxEncodeTool.urlEncode(url));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/qrCode/qrCodeAnalysisResp" ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    ActivityWebView.startActivity(ScanCodeActivity.this,url);
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    ShareBitmapAnalysisResp baseBean= GsonUtil.getBean(response,ShareBitmapAnalysisResp.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        switch (baseBean.getData().getType()){
                            case 1:
                                if(baseBean.getData().getObjId() > Constant.bookIdLine){
                                    SkipReadUtil.normalRead(ScanCodeActivity.this,String.valueOf(baseBean.getData().getObjId()),"",false);
                                    finish();
                                }else {
                                    //根据源来跳页面
                                    SkipReadUtil.normalRead(ScanCodeActivity.this,String.valueOf(baseBean.getData().getObjId()),baseBean.getData().getExt(),false);
                                    finish();
                                }
                                break;
                            case 2:
                                ActivityBookListDetail.startActivity(ScanCodeActivity.this,String.valueOf(baseBean.getData().getObjId()));
                                finish();
                                break;
                            case 3:
                                PerSonHomePageActivity.startActivity(ScanCodeActivity.this,String.valueOf(baseBean.getData().getObjId()));
                                finish();
                                break;
                            case 4:
                                ActivityCircleDetail.startActivity(ScanCodeActivity.this,String.valueOf(baseBean.getData().getObjId()));
                                finish();
                                break;
                            case 5:
                                ActivityWebView.startActivity(ScanCodeActivity.this,url);
                                finish();
                                break;

                            case 6:
                                RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_LIST);
                                finish();
                                break;

                            case 7:
                                ActivityLoginNew.startActivity(ScanCodeActivity.this);
                                finish();
                                break;

                            case 8:
                                if(RxLoginTool.isLogin()) {
                                    joinTeamChat(String.valueOf(baseBean.getData().getObjId()));
                                }else {
                                    ActivityLoginNew.startActivity(ScanCodeActivity.this);
                                }
                                break;

                            case 9:
                                RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_SHELF);
                                finish();
                                break;
                                default:
                                    ActivityWebView.startActivity(ScanCodeActivity.this,url);
                                    break;

                        }

//                        ActivityWebView.startActivity(ScanCodeActivity.this,url);
                    }else{
                        ActivityWebView.startActivity(ScanCodeActivity.this,url);
                    }
                }catch (Exception e){
                    ActivityWebView.startActivity(ScanCodeActivity.this,url);
                }
            }
        });
    }

    /**
     * 加入群聊
     *
     * @param teamId
     */
    public void joinTeamChat(String teamId) {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/join" , map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    TeamMessageActivity.startActivity(ScanCodeActivity.this,teamId);
                    finish();
//                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
//                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
//
//                    }else {
//                        RxToast.custom(baseBean.getMsg()).show();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
