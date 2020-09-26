package com.lianzai.reader.model.local;


import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.gen.CloudRecordBeanDao;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.model.gen.ReadTimeBeanDao;
import com.lianzai.reader.utils.CrashSaver;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.RxLogTool;

import org.greenrobot.greendao.query.LazyList;

import java.util.List;


public class CloudRecordRepository {
    private static final String TAG = "CloudRecordRepository";
    private static volatile CloudRecordRepository sInstance;
    private DaoSession mSession;
    private CloudRecordBeanDao cloudRecordBeanDao;
    private CloudRecordRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        cloudRecordBeanDao = mSession.getCloudRecordBeanDao();
    }

    public static CloudRecordRepository getInstance(){
        if (sInstance == null){
            synchronized (CloudRecordRepository.class){
                if (sInstance == null){
                    sInstance = new CloudRecordRepository();
                }
            }
        }
        return sInstance;
    }


    /**
     * 获取对应阅读记录
     * @param bookId
     * @return
     */
    public CloudRecordBean getCloudRecord( long bookId){ //long userId,
//        LazyList<CloudRecordBean> list = cloudRecordBeanDao.queryBuilder().where(CloudRecordBeanDao.Properties.UserId.eq(userId), CloudRecordBeanDao.Properties.BookId.eq(bookId)).listLazy();
        //只根据书id来
        LazyList<CloudRecordBean> list = cloudRecordBeanDao.queryBuilder().where(CloudRecordBeanDao.Properties.BookId.eq(bookId)).listLazy();
        if(null != list && !list.isEmpty()){
            return list.get(0);
        }else {
            return null;
        }
    }


    /**
     * 存入阅读记录
     * @param beans
     */
    public void addCloudRecord (List<CloudRecordBean> beans){
        //记录下什么时候改变了阅读记录
        CrashSaver.saveLog("《云端" + GsonUtil.toJsonString(beans) + "云端》");
        cloudRecordBeanDao.insertOrReplaceInTx(beans);
    }

    /**
     * 存入阅读记录
     * @param bean
     */
    public void addCloudRecord (CloudRecordBean bean){
        //记录下什么时候改变了阅读记录
        CrashSaver.saveLog("《云端" + GsonUtil.toJsonString(bean) + "云端》");
        cloudRecordBeanDao.insertOrReplace(bean);
    }


//    /**
//     * 删除此用户的所有记录
//     * @param uid
//     */
//    public void deleteCloudRecordByUid(long uid){
//        cloudRecordBeanDao.getDatabase().execSQL("delete from CLOUD_RECORD_BEAN where USER_ID="+uid);
//        RxLogTool.e("deleteReadTimeByUid....uid:"+uid);
//    }
//
//    /**
//     * 删除此用户的某本书的阅读记录
//     * @param uid
//     */
//    public void deleteCloudRecordByUid(long uid,long bookId){
//        cloudRecordBeanDao.getDatabase().execSQL("delete from CLOUD_RECORD_BEAN where USER_ID="+uid +" and BOOK_ID="+ bookId);
//        RxLogTool.e("deleteReadTimeByUid....uid:"+uid);
//    }


    public DaoSession getSession(){
        return mSession;
    }
}
