package com.lianzai.reader.model.local;


import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.bean.WebHistoryBean;
import com.lianzai.reader.model.gen.BookStoreBeanNDao;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.model.gen.ReadTimeBeanDao;
import com.lianzai.reader.model.gen.WebHistoryBeanDao;
import com.lianzai.reader.utils.RxLogTool;

import java.util.List;


public class WebHistoryRepository {
    private static final String TAG = "WebHistoryRepository";
    private static volatile WebHistoryRepository sInstance;
    private DaoSession mSession;
    private WebHistoryBeanDao webHistoryBeanDao;
    private WebHistoryRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        webHistoryBeanDao = mSession.getWebHistoryBeanDao();
    }

    public static WebHistoryRepository getInstance(){
        if (sInstance == null){
            synchronized (WebHistoryRepository.class){
                if (sInstance == null){
                    sInstance = new WebHistoryRepository();
                }
            }
        }
        return sInstance;
    }


    /**
     * 获取网页足迹的最近一千条
     * @param userId
     * @return
     */
    public List<WebHistoryBean> getWebHistoryList(String userId){
        return webHistoryBeanDao.queryBuilder().where(WebHistoryBeanDao.Properties.UserId.eq(userId)).orderDesc(WebHistoryBeanDao.Properties.Creattime).limit(1000).listLazy();//最多取1000条,防止本地数据过多造成上传失败
    }


    /**
     * 存入阅读时长
     * @param bean
     */
    public void addWebHistory(WebHistoryBean bean){
        webHistoryBeanDao.insertOrReplace(bean);
    }



    public void deleteReadTimeByUid(String uid){
        webHistoryBeanDao.getDatabase().execSQL("delete from WEB_HISTORY_BEAN where USER_ID="+uid);
        RxLogTool.e("deleteReadTimeByUid....uid:"+uid);
    }


    public DaoSession getSession(){
        return mSession;
    }
}
