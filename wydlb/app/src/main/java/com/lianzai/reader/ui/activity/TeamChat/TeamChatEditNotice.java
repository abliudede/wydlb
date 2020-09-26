package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 修改群公告
 */

public class TeamChatEditNotice extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;


    @Bind(R.id.iv_user_head)
    SelectableRoundedImageView iv_user_head;

    @Bind(R.id.tv_nickname)
    TextView tv_nickname;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.fengexian)
    View fengexian;

    @Bind(R.id.tv_content)
    TextView tv_content;

    @Bind(R.id.ed_content)
    EditText ed_content;

    String teamId;
    String nickName;
    private String head;
    private long creattime;
    private String notice;

    private int nowType = 1;
    private int mrole = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chatroom_editnotice;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    public static void startActivity(Context context,String teamId, String nickName, String head, Long time, String notice,int role){
        Bundle bundle=new Bundle();
        bundle.putString("teamId",teamId);
        bundle.putString("nickName",nickName);
        bundle.putString("head",head);
        bundle.putLong("creattime",time);
        bundle.putString("notice",notice);
        bundle.putInt("role",role);
        RxActivityTool.skipActivity(context,TeamChatEditNotice.class,bundle);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("群公告");

        nowType = 1;
        tv_options.setText("编辑");

        teamId=getIntent().getExtras().getString("teamId");
        nickName=getIntent().getExtras().getString("nickName");
        head=getIntent().getExtras().getString("head");
        creattime=getIntent().getExtras().getLong("creattime");
        notice=getIntent().getExtras().getString("notice");
        mrole=getIntent().getExtras().getInt("role");

        //初始显示数据
        RxImageTool.loadFangLogoImage(this,head,iv_user_head);
        tv_nickname.setText(nickName);

        java.util.Date createAt = new java.util.Date(creattime);
        String interval = TimeFormatUtil.getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        tv_des.setText(interval);

        tv_content.setText(notice);
        ed_content.setText(notice);

        //输入限制功能。此功能经测试完整可用。
        ed_content.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        //首字符不能是空格或者换行符
                        if (dstart==0&&source.toString().contentEquals("\n")||dstart==0&&source.toString().contentEquals(" ")){
                            //
                            return "";
                        }else{
                            return null;
                        }

                    }
                },new InputFilter.LengthFilter(300)
        });
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int index = ed_content.getSelectionStart();
                if (index>=3&&s.toString().substring(index-3,index).equals("\n\n\n")){
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0,index-1));
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
                if (index>=3&&s.toString().substring(index-3,index).equals("\n\n ")){
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0,index-1));
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
                if(index >= 1 &&s.length() >= index+2 && s.toString().substring(index-1,index+2).equals("\n\n\n")){
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
                if(index >= 1 &&s.length() >= index+2 && s.toString().substring(index-1,index+2).equals(" \n\n")){
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
                if(index >= 2 && s.length() >= index+1 && s.toString().substring(index-2,index+1).equals("\n\n\n")){
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
                if(index >= 2 && s.length() >= index+1 && s.toString().substring(index-2,index+1).equals("\n \n")){
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_content.setText(sb.toString());
                    ed_content.setSelection(index-1);
                }
            }
        });


        if(mrole > Constant.ChatRoomRole.AUTHOR_ACCOUNT ){
                       tv_options.setVisibility(View.GONE);
        }
        //防点击重复
        tv_options.setOnClickListener(
                new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {

                        if (mrole > Constant.ChatRoomRole.AUTHOR_ACCOUNT ) {
                            RxToast.custom("没有权限").show();
                            return;
                        }
                        if (nowType == 1) {
                            nowType = 2;
                            tv_options.setText("完成");
                            iv_user_head.setVisibility(View.GONE);
                            tv_nickname.setVisibility(View.GONE);
                            tv_des.setVisibility(View.GONE);
                            fengexian.setVisibility(View.GONE);
                            tv_content.setVisibility(View.GONE);
                            ed_content.setVisibility(View.VISIBLE);
                        } else {
                            requestChatRoomNotice(ed_content.getText().toString());
                        }
                    }
                }
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



    private void requestChatRoomNotice(String notice){
        Map<String,String> hashMap=new HashMap();
        hashMap.put("notice",notice);
        hashMap.put("teamId",teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/notice",hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
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
