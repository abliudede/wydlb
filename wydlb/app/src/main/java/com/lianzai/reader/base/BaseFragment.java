/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lianzai.reader.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSure;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements  EasyPermissions.PermissionCallbacks {

    protected View parentView;
    protected FragmentActivity activity;
    protected LayoutInflater inflater;

//    protected Context mContext;


    protected View notLoginView;

    protected TextView  notLoginTipTextView;

    protected TextView  toLoginTextView;
    RxDialogSure networkDialog;//网络不可用提示框

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    protected static final int RC_PERM = 123;

    protected static int reSting = R.string.ask_again;//默认提示语句


    @LayoutRes
    public abstract int getLayoutResId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
//        mContext = activity;
        this.inflater = inflater;
        return parentView;
    }
    public void showNetworkDialog(){
//        getNetworkDialog().show();
        RxToast.custom(getResources().getString(R.string.network_is_not_available),Constant.ToastType.TOAST_NETWORK_DISCONNECT).show();
    }
    public void dismissNetworkdDialog(){
        if (null!=networkDialog){
            networkDialog.hide();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            try{
                fetchData();
            }catch (Exception e ){

            }
            isDataInitiated = true;
            return true;
        }
        return false;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupActivityComponent(BuglyApplicationLike.getsInstance().getAppComponent());
        attachView();
        initDatas();
        configViews(savedInstanceState);
    }

    public abstract void attachView();

    public abstract void initDatas();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews(Bundle savedInstanceState);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    public Context getApplicationContext() {
        return this.activity == null ? (getActivity() == null ? null : getActivity()
                .getApplicationContext()) : this.activity.getApplicationContext();
    }

    protected LayoutInflater getMyLayoutInflater() {
        return inflater;
    }

    protected View getParentView() {
        return parentView;
    }

    public RxDialogSure getNetworkDialog(){
        if (networkDialog==null){
            networkDialog=new RxDialogSure(getActivity(),R.style.OptionDialogStyle);
            networkDialog.setContent(getResources().getString(R.string.disconnect_network_title));
            networkDialog.setCancelable(false);
            networkDialog.setCanceledOnTouchOutside(false);
            networkDialog.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    networkDialog.dismiss();
                }
            });
        }
        return networkDialog;
    }


    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    //登录刷新
    public  void updateViews(){

        RxLogTool.e("updateViews------");
    }


    /**
     * 权限回调接口
     */
    private PermissionActivity.CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(PermissionActivity.CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(getContext(), mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel_btn, null, perms);
    }

    /**
     * 服务器不可用提示
     * @param message
     */
    public void showSeverErrorDialog(String message){
        if (message.equals(Constant.NetWorkStatus.SERVER_ERROR)){
//            getNetworkDialog().setContent(getResources().getString(R.string.dialog_server_error));
//            showNetworkDialog();
            RxToast.custom(getResources().getString(R.string.dialog_server_error),Constant.ToastType.TOAST_ERROR).show();
        }
    }


}