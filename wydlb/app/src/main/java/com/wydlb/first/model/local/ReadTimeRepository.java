package com.wydlb.first.model.local;


import com.wydlb.first.model.bean.BookStoreBeanN;
import com.wydlb.first.model.bean.ReadTimeBean;
import com.wydlb.first.model.gen.BookStoreBeanNDao;
import com.wydlb.first.model.gen.DaoSession;
import com.wydlb.first.model.gen.ReadTimeBeanDao;
import com.wydlb.first.utils.RxLogTool;

import java.util.List;


public class ReadTimeRepository {
    private static final String TAG = "ReadTimeRepository";
    private static volatile ReadTimeRepository sInstance;
    private DaoSession mSession;
    private ReadTimeBeanDao readTimeDao;
    private ReadTimeRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        readTimeDao = mSession.getReadTimeBeanDao();
    }

    public static ReadTimeRepository getInstance(){
        if (sInstance == null){
            synchronized (ReadTimeRepository.class){
                if (sInstance == null){
                    sInstance = new ReadTimeRepository();
                }
            }
        }
        return sInstance;
    }


    /**
     * 获取阅读时长
     * @param userId
     * @return
     */
    public List<ReadTimeBean> getReadTimeList(String userId){
//        return readTimeDao.queryBuilder().where(ReadTimeBeanDao.Properties.UserId.eq(userId)).limit(2000).listLazy();//最多取10000条,防止本地数据过多造成上传失败
        return readTimeDao.queryBuilder().where(ReadTimeBeanDao.Properties.UserId.eq(userId)).orderAsc(ReadTimeBeanDao.Properties.StartTime).limit(500).listLazy();//orderAsc,orderDesc
    }


    /**
     * 存入阅读时长
     * @param beans
     */
    public void addReadTimes(List<ReadTimeBean> beans){
        readTimeDao.insertInTx(beans);
    }

    /**
     * 存入阅读时长
     * @param bean
     */
    public void addReadTime(ReadTimeBean bean){
        readTimeDao.insert(bean);
    }


    public void deleteReadTime(){
        readTimeDao.deleteAll();
    }

    public void deleteReadTimeByUid(String uid){
        readTimeDao.getDatabase().execSQL("delete from READ_TIME_BEAN where USER_ID="+uid);
        RxLogTool.e("deleteReadTimeByUid....uid:"+uid);
    }

    public void clear(List<ReadTimeBean> beans){
        readTimeDao.deleteInTx(beans);
    }


    public DaoSession getSession(){
        return mSession;
    }
}
