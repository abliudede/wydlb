package com.wydlb.first.ui.activity.profile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountDetailBean;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.bean.UploadImageResponse;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.activity.PermissionActivity;
import com.wydlb.first.ui.contract.AccountContract;
import com.wydlb.first.ui.presenter.AccountPresenter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxFileTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxPhotoTool;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogSelectImage;
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
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lrz on 2017/10/14.
 * 个人信息
 */

public class ActivityUserProfile extends BaseActivity implements AccountContract.View{


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;


    @Inject
    AccountPresenter accountPresenter;


    @Bind(R.id.iv_user_logo)
    ImageView iv_user_logo;

    @Bind(R.id.tv_user_nickname)
    TextView tv_user_nickname;

    @Bind(R.id.tv_user_sex)
    TextView tv_user_sex;

    @Bind(R.id.tv_user_intro)
    TextView tv_user_intro;

    @Bind(R.id.sb_read_like)
    CheckBox sb_read_like;

    @Bind(R.id.sb_recently_read)
    CheckBox sb_recently_read;


    RxDialogSelectImage rxDialogSelectSex;

    RxDialogSelectImage rxDialogSelectImage;
    private AccountDetailBean accountDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);

    }

    @Override
    public void initDatas() {
        accountPresenter.getAccountDetail();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        tv_commom_title.setText("个人信息");
        accountPresenter.attachView(this);

        rxDialogSelectImage=new RxDialogSelectImage(ActivityUserProfile.this,R.style.BottomDialogStyle);
        rxDialogSelectSex=new RxDialogSelectImage(ActivityUserProfile.this,R.style.BottomDialogStyle);
        rxDialogSelectSex.getTv_camera().setText("男");
        rxDialogSelectSex.getTv_file().setText("女");
        rxDialogSelectSex.getTv_camera().setOnClickListener(
                v2->{
                    showDialog();
                    editSexRequest(0);
                    rxDialogSelectSex.dismiss();
                }
        );
        rxDialogSelectSex.getTv_file().setOnClickListener(
                v1->{
                    showDialog();
                    editSexRequest(1);
                    rxDialogSelectSex.dismiss();
                }
        );

        if (null!=accountDetailBean){
            tv_user_nickname.setText(accountDetailBean.getData().getNickName());
            RxImageTool.loadLogoImage(this, accountDetailBean.getData().getAvatar(),iv_user_logo);
            if(accountDetailBean.getData().getGender() == 0) {
                tv_user_sex.setText("男");
            }else {
                tv_user_sex.setText("女");
            }
            tv_user_intro.setText(accountDetailBean.getData().getIntroduce());
            if(accountDetailBean.getData().getIsHideReadLike() == 0) {
                sb_read_like.setChecked(false);
            }else {
                sb_read_like.setChecked(true);
            }
            if(accountDetailBean.getData().getIsHideRecentRead() == 0) {
                sb_recently_read.setChecked(false);
            }else {
                sb_recently_read.setChecked(true);
            }
        }

    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }


    @Override
    public void getAccountDetailSuccess(AccountDetailBean bean) {
        accountDetailBean=bean;
        RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_ID,accountDetailBean.getData().getMobile());

        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);

        tv_user_nickname.setText(accountDetailBean.getData().getNickName());
        RxImageTool.loadLogoImage(this, accountDetailBean.getData().getAvatar(),iv_user_logo);
        if(accountDetailBean.getData().getGender() == 0) {
            tv_user_sex.setText("男");
        }else {
            tv_user_sex.setText("女");
        }
        tv_user_intro.setText(accountDetailBean.getData().getIntroduce());
        if(accountDetailBean.getData().getIsHideReadLike() == 0) {
            sb_read_like.setChecked(false);
        }else {
            sb_read_like.setChecked(true);
        }
        if(accountDetailBean.getData().getIsHideRecentRead() == 0) {
            sb_recently_read.setChecked(false);
        }else {
            sb_recently_read.setChecked(true);
        }
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }

    @OnClick(R.id.rl_edit_nickname)void editNickNameClick(){
        RxActivityTool.skipActivity(this,ActivityEditProfile.class);
    }

    @OnClick(R.id.rl_edit_sex)void editSexClick(){
        if("男".equals(tv_user_sex.getText().toString())){
            rxDialogSelectSex.getTv_camera().setTextColor(getResources().getColor(R.color.bluetext_color));
            rxDialogSelectSex.getTv_file().setTextColor(getResources().getColor(R.color.normal_text_color));
        }else {
            rxDialogSelectSex.getTv_camera().setTextColor(getResources().getColor(R.color.normal_text_color));
            rxDialogSelectSex.getTv_file().setTextColor(getResources().getColor(R.color.bluetext_color));
        }
        rxDialogSelectSex.show();
    }


    @OnClick(R.id.rl_edit_intro)void editIntroClick(){
        RxActivityTool.skipActivity(this,ActivityEditIntro.class);
    }

    @OnClick(R.id.rl_upload_logo)void uploadLogoClick(){
        rxDialogSelectImage.show();
        rxDialogSelectImage.getTv_camera().setOnClickListener(
                v2->{
                    //请求Camera权限
                    //授权
                    checkPermission(new PermissionActivity.CheckPermListener() {
                        @Override
                        public void superPermission() {
                            RxPhotoTool.openCameraImage(ActivityUserProfile.this);
                        }
                        @Override
                        public void noPermission() {

                        }
                    },R.string.ask_again, Manifest.permission.CAMERA);


                }
        );
        rxDialogSelectImage.getTv_file().setOnClickListener(
                v1->{
                    checkPermission(new PermissionActivity.CheckPermListener() {
                        @Override
                        public void superPermission() {
                            RxPhotoTool.openLocalImage(ActivityUserProfile.this);
                        }
                        @Override
                        public void noPermission() {

                        }
                    },R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                }
        );
    }

    @OnCheckedChanged(R.id.sb_read_like)void sb_read_likeChanged(boolean isChecked){
        if(isChecked){
            editReadLikeRequest(1);
        }else {
            editReadLikeRequest(0);
        }
    }

    @OnCheckedChanged(R.id.sb_recently_read)void sb_recently_readChanged(boolean isChecked){
        if(isChecked){
            editReccentReadRequest(1);
        }else {
            editReccentReadRequest(0);
        }
    }

    String filePath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RxLogTool.e("NewMineFragment onActivityResult");
        RxLogTool.e("requestCode:" + requestCode);
        if (resultCode == RESULT_OK && requestCode == RxPhotoTool.GET_IMAGE_BY_CAMERA) {
            if (null != RxPhotoTool.imageUriFromCamera) {
                filePath= RxPhotoTool.getImageAbsolutePath(this, RxPhotoTool.imageUriFromCamera);
                RxLogTool.e("filePath:" + filePath);

                String dirPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER;
                RxFileTool.createOrExistsDir(dirPath);

                int degree = RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题

                if (degree > 0) {//旋转图片
                    filePath = RxImageTool.amendRotatePhoto(filePath, filePath,this);
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
                                RxImageTool.loadLogoImage(ActivityUserProfile.this,file,iv_user_logo);
                                uploadFileRequest(new File(filePath));//上传图片

                            }

                            @Override
                            public void onError(Throwable e) {
//                                RxToast.custom("图片压缩失败,将上传原图");
                                // TODO 当压缩过程出现问题时调用 上传原图

                                File originFile=new File(filePath);
                                RxImageTool.loadLogoImage(ActivityUserProfile.this,originFile,iv_user_logo);
                                uploadFileRequest(new File(filePath));//上传图片
                            }
                        }).launch();


            }

        }else if (resultCode==RESULT_OK&&requestCode==RxPhotoTool.GET_IMAGE_FROM_PHONE){
            filePath=RxPhotoTool.getImageAbsolutePath(this,data.getData());
            RxLogTool.e("filePath:"+filePath);

            String dirPath= RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
            RxFileTool.createOrExistsDir(dirPath);

            int degree=RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题

            if (degree>0){//旋转图片
                filePath=RxImageTool.amendRotatePhoto(filePath,filePath,this);
                RxLogTool.e("degree new path:"+filePath);
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
                            RxImageTool.loadLogoImage(ActivityUserProfile.this,file,iv_user_logo);
                            uploadFileRequest(new File(filePath));//上传图片

                        }

                        @Override
                        public void onError(Throwable e) {
//                            RxToast.custom("图片压缩失败,将上传原图");
                            // TODO 当压缩过程出现问题时调用 上传原图
                            File originFile=new File(filePath);
                            RxImageTool.loadLogoImage(ActivityUserProfile.this,originFile,iv_user_logo);
                            uploadFileRequest(new File(filePath));//上传图片
                        }
                    }).launch();



        }
    }

    /**
     * 图片上传请求
     *
     * @param
     */
    private void uploadFileRequest(File file) {
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

//        if (!StorageUtil.isInvalidVideoFile(file)) {
//            Toast.makeText(activity, R.string.im_choose_video, Toast.LENGTH_SHORT).show();
//            return false;
//        }


        showDialog();
        getDialog().setCanceledOnTouchOutside(false);

        Map map = new HashMap();
        map.put(Params.BUCKET, Constant.UPAI_BUCKET);
        int lastIndex = file.getName().lastIndexOf(".");
        String suffix = file.getName().substring(lastIndex, file.getName().length());
        map.put(Params.SAVE_KEY, "avatar/android_" + System.currentTimeMillis() + "" + suffix);
        UploadEngine.getInstance().formUpload(file, map, Constant.UPAI_OPERATER, UpYunUtils.md5(Constant.UPAI_PASSWORD), new UpCompleteListener() {
            @Override
            public void onComplete(boolean b, String s) {
                try{
                    rxDialogSelectImage.dismiss();
                    RxLogTool.e("onComplete:" + s);
                    UploadImageResponse uploadImageResponse = GsonUtil.getBean(s, UploadImageResponse.class);
                    if (uploadImageResponse.getCode() == 200) {
//                    RxToast.custom(getResources().getString(R.string.upload_file_success)).show();
                        //上传头像
                        uploadAvatarRequest(uploadImageResponse.getUrl());

                    } else {
                        RxToast.custom(uploadImageResponse.getMessage()).show();
                    }
                }catch (Exception e){

                }
            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long l, long l1) {
                RxLogTool.e("onRequestProgress:" + l);
            }
        });

    }

    private void uploadAvatarRequest(String url){
        Map map=new HashMap();
        map.put("avatar",url);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updatePic", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("头像上传成功").show();
                        RxLogTool.e("img:"+url);
                        RxImageTool.loadLogoImage(ActivityUserProfile.this,url,iv_user_logo);
                        //刷新本页面
                        accountPresenter.getAccountDetail();
                    }else{
                        RxToast.custom("头像上传失败").show();
                    }
                }catch (Exception e){

                }
                RxLogTool.e("uploadAvatarRequest response:"+response);
            }
        });
    }

    private void editSexRequest(int gender){
        Map map=new HashMap();
        map.put("gender",String.valueOf(gender));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updateMemberInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("性别修改成功",Constant.ToastType.TOAST_SUCCESS).show();
                        //通知刷新
                        accountPresenter.getAccountDetail();
                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){

                }
                RxLogTool.e("editProfileRequest response:"+response);
            }
        });
    }

    private void editReadLikeRequest(int ishidden){
        Map map=new HashMap();
        map.put("isHideReadLike",String.valueOf(ishidden));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updateMemberInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                    if(ishidden == 1){
                        sb_read_like.setChecked(false);
                    }else {
                        sb_read_like.setChecked(true);
                    }
                }catch (Exception ee){

                }

            }

            @Override
            public void onResponse(String response) {
                try{
                    dismissDialog();
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //存入本地数据
                        accountDetailBean.getData().setIsHideReadLike(ishidden);
                    }else{
                        if(ishidden == 1){
                            sb_read_like.setChecked(false);
                        }else {
                            sb_read_like.setChecked(true);
                        }
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }

    private void editReccentReadRequest(int ishidden){
        Map map=new HashMap();
        map.put("isHideRecentRead",String.valueOf(ishidden));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updateMemberInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                    if(ishidden == 1){
                        sb_recently_read.setChecked(false);
                    }else {
                        sb_recently_read.setChecked(true);
                    }
                }catch (Exception ee){

                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    dismissDialog();
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //存入本地数据
                        accountDetailBean.getData().setIsHideRecentRead(ishidden);
                    }else{
                        if(ishidden == 1){
                            sb_recently_read.setChecked(false);
                        }else {
                            sb_recently_read.setChecked(true);
                        }
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }


}
