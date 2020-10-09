package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.CopyrightAssetsBean;
import com.lianzai.reader.bean.WebSocketBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.CopyrightAssetsListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;
import com.zhangke.websocket.response.ErrorResponse;

import org.java_websocket.framing.Framedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 版权资产主页面
 */
public class ActivityCopyrightAssets extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_amount)
    TextView tv_amount;

    @Bind(R.id.search_ed)
    EditText search_ed;

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    CopyrightAssetsListAdapter copyrightAssetsListAdapter;
    RxLinearLayoutManager manager;
    List<CopyrightAssetsBean.DataBean.CopyrightListBean> dataBeanListAll = new ArrayList<>();
    List<CopyrightAssetsBean.DataBean.CopyrightListBean> showDataBeanList = new ArrayList<>();


    private String searchKey = "";

    WebSocketManager wsManager;
    private SocketListener listener;

    public static void startActivity(Context context) {
        RxActivityTool.skipActivity(context, ActivityCopyrightAssets.class);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_copyright_assets;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        onRefresh();
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }

//ws3
        WebSocketSetting setting = new WebSocketSetting();
        //连接地址，必填，例如 wss://localhost:8080
        String connectUrl = Constant.appMode == Constant.AppMode.RELEASE ? "wss://ws.lianzai.com" : "wss://ws.bendixing.net";
        setting.setConnectUrl(connectUrl);
        //设置连接超时时间
        setting.setConnectTimeout(10 * 1000);
//设置心跳间隔时间
        setting.setConnectionLostTimeout(60);
//设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(40);
//设置 Headers
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Accept-Encoding", "gzip");
//        httpHeaders.put("Content-Encoding", "gzip");
        setting.setHttpHeaders(httpHeaders);
//设置消息分发器，接收到数据后先进入该类中处理，处理完再发送到下游
//        setting.setResponseProcessDispatcher(new AppResponseDispatcher());
//接收到数据后是否放入子线程处理，只有设置了 ResponseProcessDispatcher 才有意义
        setting.setProcessDataOnBackground(true);
//网络状态发生变化后是否重连，
//需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        wsManager = WebSocketHandler.init(setting);

        listener = new SocketListener() {
            @Override
            public void onConnected() {
                wsManager.send("[{\"process\": \"latestPrice\"}]");
            }

            @Override
            public void onConnectFailed(Throwable e) {
                RxLogTool.e("连接断开" + e);
            }

            @Override
            public void onDisconnect() {
                RxLogTool.e("连接断开");
            }

            @Override
            public void onSendDataError(ErrorResponse errorResponse) {
                RxLogTool.e("消息发送失败");
            }

            @Override
            public <T> void onMessage(String message, T data) {
                RxLogTool.e(message);
            }

            @Override
            public <T> void onMessage(ByteBuffer bytes, T data) {
                Charset cs = Charset.forName("UTF-8");
                cs.decode(bytes).array().toString();
                String json = uncompressToStringZlib(bytes.array());
                RxLogTool.e(json);
                dealJson(json);
            }

            @Override
            public void onPing(Framedata framedata) {

            }

            @Override
            public void onPong(Framedata framedata) {

            }
        };
        wsManager.addListener(listener);
        //启动连接
        wsManager.start();


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        copyrightAssetsListAdapter = new CopyrightAssetsListAdapter(showDataBeanList, this);
        copyrightAssetsListAdapter.setOnItemClickListener(
                (adapter, view, position) -> {
                    CopyrightAssetsBean.DataBean.CopyrightListBean item = copyrightAssetsListAdapter.getData().get(position);
                    ActivityCopyrightDetail.startActivity(this, item.getBookId());
                }
        );

        manager = new RxLinearLayoutManager(ActivityCopyrightAssets.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, RxImageTool.dip2px(12), 0));
        rv_list.setAdapter(copyrightAssetsListAdapter);
        copyrightAssetsListAdapter.setEmptyView(R.layout.view_records_empty, rv_list);

        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    searchKey = "";
                } else {//模糊匹配
                    searchKey = str;
                }
                useSearchKey();
            }
        });


    }

    private void dealJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        try {
            WebSocketBean webSocketBean = GsonUtil.getBean(json, WebSocketBean.class);
            if (null != webSocketBean) {
                String symbol = webSocketBean.getData().getSymbol();
                String price = webSocketBean.getData().getPrice();
                if (!TextUtils.isEmpty(symbol) && !TextUtils.isEmpty(price)) {
                    for (CopyrightAssetsBean.DataBean.CopyrightListBean item : dataBeanListAll) {
                        if (symbol.equals(item.getSymbols())) {
                            double count = item.getCoinBookBalance() + item.getCouponBookBalance();
                            double readBalance = count * Double.parseDouble(price);
                            item.setReadBalance(readBalance);
                        }
                    }
                    useSearchKey();
                }
            }
        } catch (Exception e) {
        }
    }

    private void useSearchKey() {
        showDataBeanList.clear();
        if (TextUtils.isEmpty(searchKey)) {
            showDataBeanList.addAll(dataBeanListAll);
        } else {
            for (CopyrightAssetsBean.DataBean.CopyrightListBean item : dataBeanListAll) {
                if (item.getBookName().contains(searchKey)) {
                    showDataBeanList.add(item);
                }
            }
        }
        copyrightAssetsListAdapter.replaceData(showDataBeanList);
    }

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    /**
     * Gzip  byte[] 解压成字符串
     *
     * @param bytes
     * @return
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }


    /*zlib解压*/
    public static String uncompressToStringZlib(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        //定义byte数组用来放置解压后的数据
        Inflater decompresser = new Inflater();
        decompresser.reset();
        //设置当前输入解压
        decompresser.setInput(bytes, 0, bytes.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(bytes.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        decompresser.end();
        return o.toString();
    }


    /**
     * Gzip  byte[] 解压成字符串
     *
     * @param bytes
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];

            StringBuffer strBuffer = new StringBuffer();
            int len = 0;
            while ((len = ungzip.read(buffer)) != -1) {
                strBuffer.append(new String(buffer, 0, len, encoding));
            }
            return strBuffer.toString();

        } catch (IOException e) {
            RxLogTool.e("gzip compress error.", e.getMessage());
        }
        return null;
    }

    /**
     * 判断byte[]是否是Gzip格式
     *
     * @param data
     * @return
     */
    public static boolean isGzip(byte[] data) {
        int header = (int) ((data[0] << 8) | data[1] & 0xFF);
        return header == 0x1f8b;
    }

    @Override
    public void gc() {
        wsManager.removeListener(listener);
        wsManager.disConnect();
    }

    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)
    void closeClick() {
        finish();
    }

    @OnClick(R.id.tv_history)
    void tv_historyClick() {
        ActivityCopyrightHistoryList.startActivity(this);
    }



    @Override
    public void onRefresh() {
        //请求版权资产
        requestData();
    }


    public void requestData() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/getUserAllCopyrightBalance", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/user/bookBalance/getUserAllCopyrightBalance").show();
                    copyrightAssetsListAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    CopyrightAssetsBean copyrightAssetsBean = GsonUtil.getBean(response, CopyrightAssetsBean.class);
                    if (copyrightAssetsBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (copyrightAssetsBean.getData().getReadAllBalance() > 0) {
                            tv_amount.setText(RxDataTool.format2Decimals(String.valueOf(copyrightAssetsBean.getData().getReadAllBalance())));
                        } else {
                            tv_amount.setText("--");
                        }

                        dataBeanListAll.clear();
                        List<CopyrightAssetsBean.DataBean.CopyrightListBean> templist = copyrightAssetsBean.getData().getCopyrightList();
                        if (null == templist) {
                            templist = new ArrayList<>();
                        }
                        dataBeanListAll.addAll(templist);

                        useSearchKey();

                        if (templist.isEmpty()) {
                            copyrightAssetsListAdapter.setEnableLoadMore(false);
                            copyrightAssetsListAdapter.loadMoreEnd();
                        } else {
                            copyrightAssetsListAdapter.setEnableLoadMore(true);
                            copyrightAssetsListAdapter.loadMoreComplete();
                        }

                    } else {
                        RxToast.custom(copyrightAssetsBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    private void showOrCloseRefresh(boolean isShow) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                } catch (Exception e) {
                    RxLogTool.e("showOrCloseRefresh:" + e.getMessage());
                }
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }


}
