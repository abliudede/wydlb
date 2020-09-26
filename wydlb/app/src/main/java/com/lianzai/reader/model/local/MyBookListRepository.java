package com.lianzai.reader.model.local;


import android.text.TextUtils;

import com.lianzai.reader.model.bean.MyBookListBean;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.model.gen.MyBookListBeanDao;
import com.lianzai.reader.utils.RxLogTool;

import java.util.List;


public class MyBookListRepository {
    private static final String TAG = "MyBookListRepository";
    private static volatile MyBookListRepository sInstance;
    private DaoSession mSession;
    private MyBookListBeanDao myBookListBeanDao;
    private MyBookListRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        myBookListBeanDao = mSession.getMyBookListBeanDao();
    }

    public static MyBookListRepository getInstance(){
        if (sInstance == null){
            synchronized (MyBookListRepository.class){
                if (sInstance == null){
                    sInstance = new MyBookListRepository();
                }
            }
        }
        return sInstance;
    }

    public List<MyBookListBean> getBookListByUserId(int userId){
        return mSession.getMyBookListBeanDao().queryBuilder().where(MyBookListBeanDao.Properties.Uid.eq(userId)).orderDesc(MyBookListBeanDao.Properties.UpdateDate,MyBookListBeanDao.Properties.IsUnread).listLazy();
    }


    //点击阅读，更新排序
    public void updateBookList(MyBookListBean myBookListBean){
        //判空
        if(null == myBookListBean) return;
        myBookListBean.setUpdateDate(System.currentTimeMillis());
        myBookListBean.setUnread(false);
        myBookListBeanDao.update(myBookListBean);
    }


    public void updateBookListById(String id,String uid){
        myBookListBeanDao.getDatabase().execSQL("update  MY_BOOK_LIST_BEAN set UPDATE_DATE="+System.currentTimeMillis() +",IS_UNREAD="+0+" where ID="+id +" and UID="+uid);
    }

    public void updateBookListRead(String bookListId,String uid,int isUnread){
        myBookListBeanDao.getDatabase().execSQL("update  MY_BOOK_LIST_BEAN set UPDATE_DATE="+System.currentTimeMillis() +",IS_UNREAD="+isUnread+" where ID="+bookListId +" and UID="+uid);
    }

    public void saveMyBookList(List<MyBookListBean> beans){
        for(MyBookListBean myBookListBean:beans){
            MyBookListBean bean=hasBookListBean(myBookListBean.getUid(),myBookListBean.getId());
            if (null!=bean){
                RxLogTool.e("query saveMyBookList: "+bean.toString());

                bean.setUpdateDate(System.currentTimeMillis());
                bean.setUnread(myBookListBean.getIsUnread());
                bean.setCover(myBookListBean.getCover());//最新的封面

                if (!TextUtils.isEmpty(myBookListBean.getBooklistName()))//最新的标题
                    bean.setBooklistName(myBookListBean.getBooklistName());
                if (!TextUtils.isEmpty(myBookListBean.getBooklistIntro())){
                    bean.setBooklistIntro(myBookListBean.getBooklistIntro());
                }

                if (!TextUtils.isEmpty(myBookListBean.getBooklistAuthor())){
                    bean.setBooklistAuthor(myBookListBean.getBooklistAuthor());
                }


                myBookListBeanDao.update(bean);
                RxLogTool.e("saveMyBookList update："+bean.toString());
            }else{

                RxLogTool.e("insert saveMyBookList myBookListBean:"+myBookListBean.toString());
                long id=myBookListBeanDao.insert(myBookListBean);
                RxLogTool.e("saveMyBookList id:"+id);
            }
        }
    }

    public void saveBookList(MyBookListBean bean){
        mSession.getMyBookListBeanDao()
                .insertOrReplace(bean);
    }



    public void deleteAllByUid(int uid){
        myBookListBeanDao.getDatabase().execSQL("delete from MY_BOOK_LIST_BEAN where UID="+uid);
    }

    public MyBookListBean hasBookListBean(int uid,int bookListId){
        return myBookListBeanDao.queryBuilder().where(MyBookListBeanDao.Properties.Uid.eq(uid),MyBookListBeanDao.Properties.Id.eq(bookListId)).unique();
    }
    public void deleteByBookListIdAndUid(int uid,String bookListId){
        myBookListBeanDao.getDatabase().execSQL("delete from MY_BOOK_LIST_BEAN where UID="+uid +" and ID="+bookListId);
    }




    public DaoSession getSession(){
        return mSession;
    }
}
