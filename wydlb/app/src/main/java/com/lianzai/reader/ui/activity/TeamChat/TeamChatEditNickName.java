package com.lianzai.reader.ui.activity.TeamChat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.RxToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 修改聊天室昵称
 */

public class TeamChatEditNickName extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;


    @Bind(R.id.ed_nickname)
    EditText ed_nickname;

    @Bind(R.id.iv_clear)
    ImageView iv_clear;


    String teamId;

    String nickName;
    @Override
    public int getLayoutId() {
        return R.layout.activity_chat_room_nickname;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("我在本群的昵称");
        tv_options.setText("保存");

        teamId=getIntent().getExtras().getString("teamId");
        nickName=getIntent().getExtras().getString("nickName");

        ed_nickname.setText(nickName);
        tv_options.setOnClickListener(
                v->{
                    if (!TextUtils.isEmpty(ed_nickname.getText().toString().trim())){
                        requestMyChatRoomNickName(ed_nickname.getText().toString());
                    }else{
                        RxToast.custom("昵称不允许为空").show();
                    }
                }
        );

        ed_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(null == editable) return;
                String etstr = editable.toString();

                if (TextUtils.isEmpty(etstr)){
                    iv_clear.setVisibility(View.GONE);
                }else{
                    if(etstr.contains("\n")){
                        String temp = etstr.replace("\n", "");
                        ed_nickname.setText(temp);
                        ed_nickname.setSelection(temp.length());
                    }
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_clear.setOnClickListener(
                v->ed_nickname.setText("")
        );



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



    private void requestMyChatRoomNickName(String nickName){
        Map<String,String> hashMap=new HashMap();
        hashMap.put("nickName",nickName);
        hashMap.put("teamId",teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/nick",hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:"+response);
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("修改成功").show();
                        finish();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){

                }

            }
        });
    }

}
