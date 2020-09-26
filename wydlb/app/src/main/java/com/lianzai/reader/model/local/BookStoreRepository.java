package com.lianzai.reader.model.local;


import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.gen.BookStoreBeanNDao;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.utils.RxLogTool;

import java.util.List;


public class BookStoreRepository {
    private static final String TAG = "BookStoreRepository";
    private static volatile BookStoreRepository sInstance;
    private DaoSession mSession;
    private BookStoreBeanNDao bookStoreBeanDao;
    private BookStoreRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        bookStoreBeanDao = mSession.getBookStoreBeanNDao();
    }

    public static BookStoreRepository getInstance(){
        if (sInstance == null){
            synchronized (BookStoreRepository.class){
                if (sInstance == null){
                    sInstance = new BookStoreRepository();
                }
            }
        }
        return sInstance;
    }

    //清除此用户书架所有红点
    public void updateBookStoreBooksByUserId(int userId){
        List<BookStoreBeanN> list = mSession.getBookStoreBeanNDao().queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(userId)).orderDesc(BookStoreBeanNDao.Properties.UpdateDate).listLazy();
        for(BookStoreBeanN item : list){
            item.setIsUnread(false);
        }
        bookStoreBeanDao.updateInTx(list);
    }

    public List<BookStoreBeanN> getBookStoreBooksByUserId(int userId){
        return mSession.getBookStoreBeanNDao().queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(userId)).orderDesc(BookStoreBeanNDao.Properties.UpdateDate).listLazy();
    }

    //获取书架中所有的内站书
    public List<BookStoreBeanN> getBookStoreInsideBooksByUserId(int userId){
        return mSession.getBookStoreBeanNDao().queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(userId),BookStoreBeanNDao.Properties.PlatformType.eq(2)).orderDesc(BookStoreBeanNDao.Properties.UpdateDate).listLazy();
    }

    public List<BookStoreBeanN> getBookStoreRedByUserId(int userId){
        return mSession.getBookStoreBeanNDao().queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(userId),BookStoreBeanNDao.Properties.IsUnread.eq(true)).listLazy();
    }

    public List<BookStoreBeanN> getBookStoreBooksByUserIdLike(int userId,String name){
        RxLogTool.e("getBookStoreBooksByUserIdLike name："+name);
        return mSession.getBookStoreBeanNDao().queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(userId),BookStoreBeanNDao.Properties.PlatformName.like("%"+name+"%")).orderDesc(BookStoreBeanNDao.Properties.UpdateDate).listLazy();
    }

    //清空共享版权状态
    public void clearIsCopyrightBooks(List<BookStoreBeanN> list){
        if (null != list && !list.isEmpty()) {
            for(BookStoreBeanN bean : list) {
//                RxLogTool.e("update query BookStoreBean: " + bean.toString());
                bean.setIsCopyright(false);
                bookStoreBeanDao.update(bean);
            }
        }
    }

    //更新共享版权状态
    public void updateIsCopyrightBooks(int uid ,int bookId){
        BookStoreBeanN bean= hasBookBean(uid,bookId);
        if (null!=bean) {
//            RxLogTool.e("update query BookStoreBean: " + bean.toString());
            bean.setIsCopyright(true);
            bookStoreBeanDao.update(bean);
        }
    }


    //点击阅读，更新排序
    public void updateBooks(int uid ,int bookId){
        BookStoreBeanN bean= hasBookBean(uid,bookId);
        if (null!=bean) {
//            RxLogTool.e("update query BookStoreBean: " + bean.toString());
            //时间更新，否则会导致排序不对,最近阅读的时间戳加上一年。优先排在最前。
            bean.setUpdateDate(System.currentTimeMillis() + 31104000000L);
            bookStoreBeanDao.update(bean);
//            RxLogTool.e("saveBooks update：" + bean.toString());
        }
    }

    public void saveBooks(List<BookStoreBeanN> beans){
        RxLogTool.e("update  BookStoreBean size: "+beans.size());

        for(BookStoreBeanN bookStoreBean:beans){
            BookStoreBeanN bean=hasBookBean(bookStoreBean.getUid(),Integer.parseInt(bookStoreBean.getBookId()));
            if (null!=bean){
//                RxLogTool.e("update query BookStoreBean: "+bean.toString());
                //时间不能更新，否则会导致排序不对
//                int isUnRead= bookStoreBean.getIsUnread()?1:0;
//                int uid=bookStoreBean.getUid();
                bean.setIsUnread(bookStoreBean.getIsUnread());
                bean.setUid(bookStoreBean.getUid());
                bean.setPlatformName(bookStoreBean.getPlatformName());
                bean.setPlatformCover(bookStoreBean.getPlatformCover());
                //从接口更新时，避免覆盖最近阅读的时间。只更新之前接口返回的时间。
                if(bean.getUpdateDate() < bookStoreBean.getUpdateDate()) {
                    bean.setUpdateDate(bookStoreBean.getUpdateDate());
                }
                bookStoreBeanDao.update(bean);
            }else{
                long time = bookStoreBean.getUpdateDate();
                bookStoreBean.setUpdateDate(time + 31104000000L);
                bookStoreBeanDao.insertOrReplace(bookStoreBean);
            }
        }
    }

    public void saveBook(BookStoreBeanN bean){
        mSession.getBookStoreBeanNDao()
                .insertOrReplace(bean);
    }



    public void deleteAllByUid(int uid){
        bookStoreBeanDao.getDatabase().execSQL("delete from BOOK_STORE_BEAN_N where UID="+uid);
    }

    public BookStoreBeanN hasBookBean(int uid,int bookId) {
        try {
            return bookStoreBeanDao.queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(uid), BookStoreBeanNDao.Properties.BookId.eq(bookId)).unique();
        }catch (Exception e){
            deleteAllByBookIdAndUid(uid,String.valueOf(bookId));
            return null;
        }
    }
    public BookStoreBeanN queryBookBeanByPlatformId(String uid,String platformId){
        return bookStoreBeanDao.queryBuilder().where(BookStoreBeanNDao.Properties.Uid.eq(uid),BookStoreBeanNDao.Properties.PlatformId.eq(platformId)).unique();
    }

    public void deleteAllByPlatformIdAndUid(int uid,String platformId){
        bookStoreBeanDao.getDatabase().execSQL("delete from BOOK_STORE_BEAN_N where UID="+uid +" and PLATFORM_ID="+platformId);
    }

    public void deleteAllByBookIdAndUid(int uid,String bookId){
        bookStoreBeanDao.getDatabase().execSQL("delete from BOOK_STORE_BEAN_N where UID="+uid +" and BOOK_ID="+bookId);
    }


    public void delete(BookStoreBeanN bookStoreBean){
        bookStoreBeanDao.delete(bookStoreBean);
    }


    public DaoSession getSession(){
        return mSession;
    }
}
