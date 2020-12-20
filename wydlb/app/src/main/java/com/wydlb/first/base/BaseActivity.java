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
package com.wydlb.first.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.wydlb.first.R;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.ui.activity.PermissionActivity;
import com.wydlb.first.utils.RxBarTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.RxToolBar;
import com.wydlb.first.view.dialog.RxDialogSureCancelNew;
import com.wydlb.first.view.loadding.CustomDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends PermissionActivity {

//    protected Context mContext;
    private CustomDialog dialog;//进度条

    public RxToolBar mCommonToolbar;

    RxDialogSureCancelNew networkDialog;//网络不可用提示框

    protected CompositeDisposable mDisposable;


    protected String currentTime;//服务器当前时间

    public static final int LOGIN_CODE=1000;//登录检查


    protected boolean isDestroy=false;//页面是否已销毁


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

//        mContext = getApplicationContext();
//        RxTool.init(mContext);
        ButterKnife.bind(this);

        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (mCommonToolbar != null) {
            supportActionBar(mCommonToolbar);
            initToolBar();
            mCommonToolbar.setBackClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backClick();
                }
            });
        }

        setupActivityComponent(BuglyApplicationLike.getsInstance().getAppComponent());
        configViews(savedInstanceState);

//        transparent19and20();

        if(!RxNetTool.isAvailable()){
            RxToast.custom(getResources().getString(R.string.network_is_not_available), Constant.ToastType.TOAST_NETWORK_DISCONNECT).show();
        }


    }


    protected ActionBar supportActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mCommonToolbar.setNavigationOnClickListener(
                (v) -> finish()
        );
        return actionBar;
    }

    protected void addDisposable(Disposable d){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    /**
     * 退出当前页面
     */
    protected void backClick(){
        finish();//结束当前
    }

    protected void transparent19and20() {
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
//            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);

            // 自定义颜色
            tintManager.setTintColor(Color.parseColor("#000000"));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initDatas();

        MobclickAgent.onResume(this);

        //在app内不允许推送消息
//        NIMClient.toggleNotification(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public abstract void initToolBar();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy=true;
        ButterKnife.unbind(this);
        dismissDialog();
        gc();
        if (mDisposable != null){
            mDisposable.clear();
            RxLogTool.e("request cancel....");
        }

    }

    /**
     * 内存回收
     */
    public abstract  void gc();

    public abstract int getLayoutId();

    protected abstract void setupActivityComponent(AppComponent appComponent);


    public abstract void initDatas();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews(Bundle savedInstanceState);

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

    // dialog
    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(this);
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public RxDialogSureCancelNew getNetworkDialog(){
        if (networkDialog==null){
            networkDialog=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
            networkDialog.setContent(getResources().getString(R.string.disconnect_network_title));
            networkDialog.setButtonText("确定","取消");
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

    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showNetworkDialog(){
//        getNetworkDialog().show();
        RxToast.custom(getResources().getString(R.string.dialog_server_error),Constant.ToastType.TOAST_NETWORK_DISCONNECT).show();
    }
    public void dismissNetworkdDialog(){
        if (null!=networkDialog){
            networkDialog.hide();
        }
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
    public void showDialog() {
        getDialog().show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        RxBarTool.hideStatusBar(this);
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        RxBarTool.setStatusBarColor(this,R.color.colorAccent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backClick();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


}
