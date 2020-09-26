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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.PermissionActivityForTranslucent;
import com.lianzai.reader.utils.RxBarTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToolBar;
import com.lianzai.reader.view.dialog.RxDialogSure;
import com.lianzai.reader.view.loadding.CustomDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

//透明无权限页面继承此Activity

public abstract class BaseActivityForTranslucent extends PermissionActivityForTranslucent {

    private CustomDialog dialog;//进度条

    public RxToolBar mCommonToolbar;

    RxDialogSure networkDialog;//网络不可用提示框

    protected CompositeDisposable mDisposable;


    protected String currentTime;//服务器当前时间

    public static final int LOGIN_CODE=1000;//登录检查


    protected boolean isDestroy=false;//页面是否已销毁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

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

        transparent19and20();

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

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initDatas();

        MobclickAgent.onResume(this);
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
            mDisposable.dispose();
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

    public RxDialogSure getNetworkDialog(){
        if (networkDialog==null){
            networkDialog=new RxDialogSure(this,R.style.OptionDialogStyle);
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

    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showNetworkDialog(){
        getNetworkDialog().show();
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
            getNetworkDialog().setContent(getResources().getString(R.string.dialog_server_error));
            showNetworkDialog();
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

}
