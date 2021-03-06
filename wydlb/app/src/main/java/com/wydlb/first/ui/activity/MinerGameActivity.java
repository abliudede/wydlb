package com.wydlb.first.ui.activity;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.bean.MinerGameResponse;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.adapter.MinerFriendsAdapter;
import com.wydlb.first.ui.contract.GameMiningContract;
import com.wydlb.first.ui.presenter.GameMiningPresenter;
import com.wydlb.first.utils.RxBeepTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxRecyclerViewDividerTool;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.view.miner.DrawBackground;
import com.wydlb.first.view.miner.DrawMineral;
import com.wydlb.first.view.miner.MinerMainView;
import com.wydlb.first.view.miner.entity.MineralEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lrz on 2017/10/14.
 * 挖矿页面
 */

public class MinerGameActivity extends PermissionActivity implements DrawMineral.MineralClickListner,DrawBackground.OptionClickListener,GameMiningContract.View {

    CopyOnWriteArrayList<MineralEntity> mineralEntities=new CopyOnWriteArrayList<>();

    @Bind(R.id.miner_view)
    MinerMainView miner_view;
    @Bind(R.id.tv_mineral)
    TextView tv_mineral;

    @Bind(R.id.tv_miner_car)
    TextView tv_miner_car;

    @Bind(R.id.tv_calculating_force)
    TextView tv_calculating_force;

    @Bind(R.id.rv_friends)
    RecyclerView rv_friends;
    MinerFriendsAdapter minerFriendsAdapter;

    List<Object>objects=new ArrayList<>();

    MediaPlayer mediaPlayer;

    @Inject
    GameMiningPresenter gameMiningPresenter;

    boolean isMusicOpen;


    float total=0;//总计
    float currentTotal=0;//当前总计
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //无title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miner_game);
        ButterKnife.bind(this);

        DaggerAccountComponent.builder().appComponent(BuglyApplicationLike.getsInstance().getAppComponent()).build().inject(this);
        gameMiningPresenter.attachView(this);

        miner_view.setMineralClickListner(this);
        miner_view.setOptionClickListener(this);

        for(int i=0;i<20;i++){
            objects.add(""+i);
        }
        minerFriendsAdapter=new MinerFriendsAdapter(objects);

        RxLinearLayoutManager manager = new RxLinearLayoutManager(this);
        rv_friends.setLayoutManager(manager);
        rv_friends.setAdapter(minerFriendsAdapter);
        rv_friends.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));


        rv_friends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                event.setLocation(event.getX()+rv_friends.getLeft(),event.getY()+rv_friends.getTop());
                miner_view.onTouch(v,event);

                RxLogTool.e("rv_friends x:"+(event.getX()+rv_friends.getLeft())+"--y:"+(event.getY()+rv_friends.getTop()));
                return false;
            }
        });

        isMusicOpen= RxSharedPreferencesUtil.getInstance().getBoolean("musicOpen");
        //播放背景音效
        initBackgroundMediaPlayer();

        if (!isMusicOpen){
            mediaPlayer.pause();
        }

        requestData();
    }

    private  void requestData(){
        //加载数据
        ArrayMap map=new ArrayMap();
        map.put("mineType",1);
        gameMiningPresenter.miningHome(map);
    }

    private void initBackgroundMediaPlayer(){
        if (null==mediaPlayer){
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });
        }

        try {
            mediaPlayer.reset();
            AssetFileDescriptor file =getAssets().openFd("sound/backmusic.mp3");
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0.8f, 0.8f);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        } catch (IOException e) {
        }
    }

    @Override
    public void receiveMineralClick(MineralEntity entity) {
        RxLogTool.e("receiveMineralClick:"+entity.toString());
        currentTotal+=entity.getRealValue();
        total+=entity.getRealValue();
        tv_mineral.setText(Html.fromHtml("<font color='#F8E71C'>+"+ RxTool.getMineralNumFormat(String.valueOf(currentTotal))+"</font><font color='#cccccc'>/"+ RxTool.getMineralNumFormat(String.valueOf(total))+"</font>"));

        if (!isMusicOpen)return;

        if (entity.getTag()==1){
            RxBeepTool.playKuangJingBeep(this);
        }else{
            RxBeepTool.playDiamondsBeep(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=mediaPlayer){
            mediaPlayer.release();
        }
        miner_view.setPause(true);
        miner_view.onDestroy();
    }

    @Override
    public void optionClick(int tag) {
        if (tag==0){//邀请人

        }else if(tag==1){//任务列表

        }else if(tag==2){//音乐开关
            isMusicOpen= RxSharedPreferencesUtil.getInstance().getBoolean("musicOpen");
            if (isMusicOpen){//播放音乐
                mediaPlayer.start();
            }else{
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void miningHome(MinerGameResponse bean) {
        currentTotal=Float.parseFloat(bean.getData().getHeaderNum().getMining().getCurrent());
        total=Float.parseFloat(bean.getData().getHeaderNum().getMining().getAll());
        tv_mineral.setText(Html.fromHtml("<font color='#F8E71C'>+"+ RxTool.getMineralNumFormat(String.valueOf(currentTotal))+"</font><font color='#cccccc'>/"+ RxTool.getMineralNumFormat(String.valueOf(total))+"</font>"));
        tv_miner_car.setText(Html.fromHtml("<font color='#F8E71C'>+"+bean.getData().getHeaderNum().getProbe().getCurrent()+"</font><font color='#cccccc'>/"+bean.getData().getHeaderNum().getProbe().getAll()+"</font>"));
        tv_calculating_force.setText(String.valueOf(bean.getData().getHeaderNum().getChilun().getCurrent()));

        for(int i=0;i<bean.getData().getAccountMineList().size();i++){
            MinerGameResponse.DataBean.AccountMineListBean accountMineListBean=bean.getData().getAccountMineList().get(i);
            if (i==0||i==10){
                accountMineListBean.setDisplay(2);
            }
            if (accountMineListBean.getDisplay()==1){//普通矿
                MineralEntity entity=new MineralEntity(1,accountMineListBean.getQuantity()+"矿晶",Float.parseFloat(accountMineListBean.getQuantity()));
                mineralEntities.add(entity);
            }else if (accountMineListBean.getDisplay()==2){//钻石矿
                MineralEntity entity=new MineralEntity(2,accountMineListBean.getQuantity()+"矿晶",Float.parseFloat(accountMineListBean.getQuantity()));
                mineralEntities.add(entity);
            }
        }

        miner_view.getDrawBackgound().setMinerName(bean.getData().getUserInfo().getNickName()+"的矿场");
        miner_view.getDrawBackgound().setMineLevel("Level "+bean.getData().getUserInfo().getMineLevel());
        miner_view.setMineralEntities(mineralEntities);
    }

    @Override
    public void complete(String message) {

    }
}
