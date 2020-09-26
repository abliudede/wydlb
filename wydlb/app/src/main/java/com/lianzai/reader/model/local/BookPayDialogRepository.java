package com.lianzai.reader.model.local;


import com.lianzai.reader.model.bean.BookPayDialogBean;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.gen.BookPayDialogBeanDao;
import com.lianzai.reader.model.gen.BookStoreBeanNDao;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.utils.RxLogTool;

import java.util.List;


public class BookPayDialogRepository {
    private static final String TAG = "BookPayDialogRepository";
    private static volatile BookPayDialogRepository sInstance;
    private DaoSession mSession;
    private BookPayDialogBeanDao bookPayDialogBeanDao;
    private BookPayDialogRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        bookPayDialogBeanDao = mSession.getBookPayDialogBeanDao();
    }

    public static BookPayDialogRepository getInstance(){
        if (sInstance == null){
            synchronized (BookPayDialogRepository.class){
                if (sInstance == null){
                    sInstance = new BookPayDialogRepository();
                }
            }
        }
        return sInstance;
    }

    //插入数据并获得是否弹窗
    public boolean showDialog(String bookId){
        try{
            BookPayDialogBean temp = bookPayDialogBeanDao.queryBuilder().where(BookPayDialogBeanDao.Properties.Bid.eq(bookId)).unique();
            long currentTime = System.currentTimeMillis();
            long currentEndTime = currentTime + 7*24*3600*1000;
            if(null == temp){
                //没有数据时，创建数据并插入，不弹窗
                temp = new BookPayDialogBean();
                temp.setBid(bookId);
                temp.setStartTime(String.valueOf(currentTime));
                temp.setEndTime(String.valueOf(currentEndTime));
                temp.setCount(1);
                temp.setIsShow(false);
                bookPayDialogBeanDao.insertOrReplace(temp);

                return false;
            }else {
                //有数据时，检测是否在之前的周期内
                String startTime = temp.getStartTime();
                String endTime = temp.getEndTime();
                if(currentTime > Long.parseLong(startTime) && currentTime < Long.parseLong(endTime)){
                    //在周期内根据次数和是否已弹窗来返回
                    int nowCount = temp.getCount() + 1;
                    if(nowCount >= 10 && !temp.getIsShow()){
                        //次数大于等于10并且没弹窗则弹窗
                        temp.setCount(nowCount);
                        temp.setIsShow(true);
                        bookPayDialogBeanDao.insertOrReplace(temp);
                        return true;
                    }else {
                        temp.setCount(nowCount);
                        bookPayDialogBeanDao.insertOrReplace(temp);
                        return false;
                    }
                }else {
                    //不在周期内，创建数据并插入，不弹窗

                    temp = new BookPayDialogBean();
                    temp.setBid(bookId);
                    temp.setStartTime(String.valueOf(currentTime));
                    temp.setEndTime(String.valueOf(currentEndTime));
                    temp.setCount(1);
                    temp.setIsShow(false);
                    bookPayDialogBeanDao.insertOrReplace(temp);

                    return false;
                }
            }
        }catch (Exception e){
            RxLogTool.e(e.toString());
        }
        return false;
    }



    public DaoSession getSession(){
        return mSession;
    }
}
