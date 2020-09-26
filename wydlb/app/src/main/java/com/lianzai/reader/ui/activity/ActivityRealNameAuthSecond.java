package com.lianzai.reader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.UploadImageResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.contract.AuthRealNameContract;
import com.lianzai.reader.ui.presenter.AuthRealNamePresenter;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxPhotoTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogChooseImage;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lrz on 2017/10/14.
 * 实名认证第二步，上传照片
 */

public class ActivityRealNameAuthSecond extends BaseActivity implements AuthRealNameContract.View {

    String backImg = "";
    String frontImg = "";
    String handImg = "";

    RxDialogChooseImage rxDialogChooseImage;

    Bitmap bitmap;

    @Bind(R.id.iv_verified_idcard1)
    ImageView iv_verified_idcard1;

    @Bind(R.id.iv_verified_idcard2)
    ImageView iv_verified_idcard2;


    @Bind(R.id.iv_verified_idcard3)
    ImageView iv_verified_idcard3;


    int position = -1;

    boolean isHome = true;

    @Bind(R.id.view_positive)
    View view_positive;

    @Bind(R.id.tv_verified_idcard1_title)
    TextView tv_verified_idcard1_title;

    @Bind(R.id.tv_verified_idcard2_title)
    TextView tv_verified_idcard2_title;

    @Bind(R.id.tv_verified_idcard3_title)
    TextView tv_verified_idcard3_title;

    @Inject
    AuthRealNamePresenter authRealNamePresenter;


    RxDialogSureCancel rxDialogSureCancel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_safety_real_name_auth_second;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        authRealNamePresenter.attachView(this);

        if (!isHome) {
            view_positive.setVisibility(View.GONE);
            tv_verified_idcard1_title.setText(RxTool.getResourceString(R.string.passport_positive_title, this));
            tv_verified_idcard3_title.setText(RxTool.getResourceString(R.string.passport_hand_title, this));
        }
    }


    @Override
    public void gc() {
        RxToast.gc();
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }

    }

    @OnClick(R.id.btn_submit)
    void submitClick() {
        if (RxDataTool.isEmpty(frontImg)) {
            String message = String.format(getResources().getString(R.string.upload_tip), tv_verified_idcard1_title.getText().toString());
            RxToast.custom(message).show();
            return;
        }
        if (isHome && RxDataTool.isEmpty(backImg)) {
            String message = String.format(getResources().getString(R.string.upload_tip), tv_verified_idcard2_title.getText().toString());
            RxToast.custom(message).show();
            return;
        }
        if (RxDataTool.isEmpty(handImg)) {
            String message = String.format(getResources().getString(R.string.upload_tip), tv_verified_idcard3_title.getText().toString());
            RxToast.custom(message).show();
            return;
        }

        ArrayMap<String, Object> map = new ArrayMap();
        map.put("cardType", isHome ? Constant.AuthType.IDCARD_TYPE : Constant.AuthType.PASSPORT_TYPE);
        map.put("type", "1");
        map.put("facePic", frontImg);
        map.put("backPic", backImg);
        map.put("handPic", handImg);

        authRealNamePresenter.advancedCertification(map);

        showDialog();
    }

    @Override
    public void initToolBar() {
    }


    @OnClick(R.id.iv_verified_idcard1)
    void verifiedIdCardClick1() {
        //照相机授权
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                RxPhotoTool.openCameraImage(ActivityRealNameAuthSecond.this);
                position = 1;
            }
            @Override
            public void noPermission() {

            }
        }, R.string.permission_camera_auth, Manifest.permission.CAMERA);


    }

    @OnClick(R.id.iv_verified_idcard2)
    void verifiedIdCardClick2() {
        //照相机授权
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                RxPhotoTool.openCameraImage(ActivityRealNameAuthSecond.this);
                position = 2;
            }
            @Override
            public void noPermission() {

            }

        }, R.string.permission_camera_auth, Manifest.permission.CAMERA);
    }

    @OnClick(R.id.iv_verified_idcard3)
    void verifiedIdCardClick3() {
        //照相机授权
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                RxPhotoTool.openCameraImage(ActivityRealNameAuthSecond.this);
                position = 3;
            }
            @Override
            public void noPermission() {

            }
        }, R.string.permission_camera_auth, Manifest.permission.CAMERA);
    }

    /**
     * 图片上传请求
     *
     * @param
     */
    private void uploadFileRequest(File file, int pos) {
        if (null==file){
            RxToast.custom("文件不存在").show();
            return;
        }
        if (null!=file&&!file.exists()){
            RxToast.custom("文件不存在").show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            RxToast.custom("文件过大").show();
            return;
        }

        showDialog();
        getDialog().setCanceledOnTouchOutside(false);

        Map map = new HashMap();
        map.put(Params.BUCKET, Constant.UPAI_BUCKET);
        int lastIndex = file.getName().lastIndexOf(".");
        String suffix = file.getName().substring(lastIndex, file.getName().length());
        map.put(Params.SAVE_KEY, "auth/android_" + System.currentTimeMillis() + "" + suffix);
        UploadEngine.getInstance().formUpload(file, map, Constant.UPAI_OPERATER, UpYunUtils.md5(Constant.UPAI_PASSWORD), new UpCompleteListener() {
            @Override
            public void onComplete(boolean b, String s) {
                RxLogTool.e("onComplete:" + s);
                try{
                    UploadImageResponse uploadImageResponse = GsonUtil.getBean(s, UploadImageResponse.class);
                    if (uploadImageResponse.getCode() == 200) {
                        RxToast.custom(getResources().getString(R.string.upload_file_success)).show();
                        if (position == 1) {
                            frontImg = uploadImageResponse.getUrl();
                        } else if (position == 2) {
                            backImg = uploadImageResponse.getUrl();
                        } else if (position == 3) {
                            handImg = uploadImageResponse.getUrl();
                        }
                    } else {
                        RxToast.custom(uploadImageResponse.getMessage()).show();
                    }
                }catch ( Exception e){
                    e.printStackTrace();
                }
                dismissDialog();
            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long l, long l1) {
                RxLogTool.e("onRequestProgress:" + l);
            }
        });

    }

    String filePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RxLogTool.e("requestCode:" + requestCode);
        if (resultCode == RESULT_OK && requestCode == RxPhotoTool.GET_IMAGE_BY_CAMERA) {
            if (null != RxPhotoTool.imageUriFromCamera) {
                filePath= RxPhotoTool.getImageAbsolutePath(this, RxPhotoTool.imageUriFromCamera);
                if(null == filePath){
                    //有可能会取不到，直接返回。
                    return;
                }
                RxLogTool.e("filePath:" + filePath);

                String dirPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER;
                RxFileTool.createOrExistsDir(dirPath);

                int degree = RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题

                if (degree > 0) {//旋转图片
                    filePath = RxImageTool.amendRotatePhoto(filePath, filePath, this);
                    RxLogTool.e("degree new path:" + filePath);
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
                            public void onSuccess( File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                if (position==1){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,file,iv_verified_idcard1);
//                                        iv_verified_idcard1.setim
                                }else if (position==2){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,file,iv_verified_idcard2);
                                }else if (position==3){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,file,iv_verified_idcard3);
                                }
                                uploadFileRequest(new File(filePath),position);//上传图片
                            }

                            @Override
                            public void onError(Throwable e) {
//                                RxToast.custom("图片压缩失败,将上传原图",Constant.ToastType.TOAST_ERROR).show();
                                // TODO 当压缩过程出现问题时调用 上传原图
                                File originFile=new File(filePath);
                                if (position==1){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,originFile,iv_verified_idcard1);
//                                        iv_verified_idcard1.setim
                                }else if (position==2){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,originFile,iv_verified_idcard2);
                                }else if (position==3){
                                    RxImageTool.loadNormalImage(ActivityRealNameAuthSecond.this,originFile,iv_verified_idcard3);
                                }
                                uploadFileRequest(new File(filePath),position);//上传图片
                            }
                        }).launch();
            }
        }
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)) {
            showNetworkDialog();
            return;
        }

        RxToast.custom(message).show();
    }

    @Override
    public void primaryCertificationSuccess(BaseBean bean) {
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }

    @Override
    public void advancedCertificationSuccess(BaseBean bean) {
        dismissDialog();
        if (bean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
            RxToast.custom("安全等级2已提交审核,请耐心等待",Constant.ToastType.TOAST_NORMAL).show();
            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_ACCOUNT);
            finish();
        }

    }


    /**
     * 中断操作弹框提示
     */
    private void showInterruptExitDialog() {
        if (null == rxDialogSureCancel) {
            rxDialogSureCancel = new RxDialogSureCancel(this, R.style.OptionDialogStyle);
            rxDialogSureCancel.setContent(getResources().getString(R.string.auth_real_name_interrupt_dialog_tip));
            rxDialogSureCancel.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogSureCancel.dismiss();
                    finish();
                }
            });
            rxDialogSureCancel.setCancelListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    rxDialogSureCancel.dismiss();
                }
            });
        }
        rxDialogSureCancel.show();
    }

    @OnClick(R.id.img_back)
    void closeClick() {
        backClick();
    }
}
