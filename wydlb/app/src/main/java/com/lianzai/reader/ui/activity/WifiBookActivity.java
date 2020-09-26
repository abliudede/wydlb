package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.utils.NetworkUtils;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;
import com.lianzai.reader.wifitransfer.Defaults;
import com.lianzai.reader.wifitransfer.ServerRunner;

import butterknife.Bind;
import butterknife.OnClick;
public class WifiBookActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WifiBookActivity.class));
    }
    @Bind(R.id.mTvWifiIp)
    TextView mTvWifiIp;

    @Bind(R.id.tv_wifi_status)
            TextView tv_wifi_status;

    @Bind(R.id.tv_content)
            TextView tv_content;

    RxDialogSureCancel rxDialogSureCancel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wifi_book;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
        String wifiname = NetworkUtils.getConnectWifiSsid(this);

        String wifiIp = NetworkUtils.getConnectWifiIp(this);
        if (!TextUtils.isEmpty(wifiIp)) {
            tv_wifi_status.setText("WiFi服务已开启\n上传过程请勿离开此页");
            mTvWifiIp.setText("http://" + NetworkUtils.getConnectWifiIp(this) + ":" + Defaults.getPort());
            mTvWifiIp.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.VISIBLE);
            // 启动wifi传书服务器
            ServerRunner.startServer();
        } else {
            mTvWifiIp.setVisibility(View.INVISIBLE);
            tv_content.setVisibility(View.GONE);
            tv_wifi_status.setText("WiFi服务不可用\n请确认已打开并连接到您的WiFi");
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

    }

    @Override
    protected void backClick() {
        if (!TextUtils.isEmpty(NetworkUtils.getConnectWifiIp(this)))
            showInterruptExitDialog();
        else
            super.backClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerRunner.stopServer();
    }

    /**
     * 内存回收
     */
    @Override
    public void gc() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    /**
     * 中断操作弹框提示
     */
    private void showInterruptExitDialog(){
        if (null==rxDialogSureCancel){
            rxDialogSureCancel=new RxDialogSureCancel(this);
            rxDialogSureCancel.setContent("退出该页面Wifi传书将会中断");
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
    void finishClick() {
        backClick();
    }

    @OnClick(R.id.tv_copy_url)void copyUrlClick(){
        RxClipboardTool.copyText(this,mTvWifiIp.getText().toString());
        RxToast.custom("地址复制成功").show();
    }
}
