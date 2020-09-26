package com.lianzai.reader.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.adapter.SendCardAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 发送名片
 */
public class SendCardActivity extends BaseActivity {

    public static final int REQUEST_SEND_CARD=2000;

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_empty)
            TextView tv_empty;

    SendCardAdapter sendCardAdapter;

    List<BookStoreBeanN>bookStoreBeanList=new ArrayList<>();


    View searchView;

    EditText ed_search;

    BookStoreResponse bookStoreResponse;
    private AccountDetailBean accountDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_card;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("发送名片");

        accountDetailBean= RxTool.getAccountBean();

        if (null==accountDetailBean)return;

        //load data，只显示圈子的书籍
        List<BookStoreBeanN>tempList=new ArrayList<>();
        tempList.addAll(BookStoreRepository.getInstance().getBookStoreBooksByUserId(Integer.parseInt(accountDetailBean.getData().getUid())));
        for(BookStoreBeanN item: tempList){
            if(Integer.parseInt(item.getBookId()) <= Constant.bookIdLine){
                bookStoreBeanList.add(item);
            }
        }
//        bookStoreBeanList.addAll(BookStoreRepository.getInstance().getBookStoreBooksByUserId(Integer.parseInt(accountDetailBean.getData().getUid())));


        searchView=getLayoutInflater().inflate(R.layout.view_header_search_card,null);
        ed_search=searchView.findViewById(R.id.ed_search);

        sendCardAdapter=new SendCardAdapter(bookStoreBeanList);
        sendCardAdapter.addHeaderView(searchView);

        rv_data.setAdapter(sendCardAdapter);
        rv_data.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_data.setLayoutManager(new RxLinearLayoutManager(this));

        sendCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("book",sendCardAdapter.getData().get(position));
                Intent intent=new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        isShowEmptyView();

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())){
                    bookStoreBeanList=BookStoreRepository.getInstance().getBookStoreBooksByUserId(Integer.parseInt(accountDetailBean.getData().getUid()));
                    sendCardAdapter.replaceData(bookStoreBeanList);
                    sendCardAdapter.search("");
                }else{
                    bookStoreBeanList=BookStoreRepository.getInstance().getBookStoreBooksByUserIdLike(Integer.parseInt(accountDetailBean.getData().getUid()),editable.toString());
                    sendCardAdapter.replaceData(bookStoreBeanList);
                    sendCardAdapter.search(editable.toString());
                }
                isShowEmptyView();
            }
        });

        //本地缓存为空的时候请求网络书架
        if (bookStoreBeanList.size()==0){
            requestBookStore();
        }


        //滑动到顶部
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_data.scrollToPosition(0);
            }
        },200);
    }

    private void isShowEmptyView(){
        if (bookStoreBeanList.size()==0){
            tv_empty.setVisibility(View.VISIBLE);
        }else{
            tv_empty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @Override
    public void gc() {

    }


    //请求书架
    private void requestBookStore() {
        Map map = new HashMap();

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/shelf/refresh", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {


                try {
                    RxLogTool.e("requestBookStore:"+response);

                    bookStoreResponse = GsonUtil.getBean(response, BookStoreResponse.class);
                    if (bookStoreResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {//成功
                        //清空之前记录
                        bookStoreBeanList.clear();
                        bookStoreBeanList.addAll(bookStoreResponse.getData().getList());
                        sendCardAdapter.notifyDataSetChanged();

                        sendCardAdapter.replaceData(bookStoreBeanList);


                        //存储到本地
                        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);

                        isShowEmptyView();

                    }
                } catch (Exception e) {

                    RxLogTool.e("requestBookStore Exception:" + e.getMessage());
                }
            }
        });
    }
}
