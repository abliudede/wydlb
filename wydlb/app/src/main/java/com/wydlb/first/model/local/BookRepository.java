package com.wydlb.first.model.local;


import com.wydlb.first.model.bean.BookChapterBean;
import com.wydlb.first.model.bean.CollBookBean;
import com.wydlb.first.model.gen.CollBookBeanDao;
import com.wydlb.first.model.gen.DaoSession;
import com.wydlb.first.utils.BookManager;
import com.wydlb.first.utils.IOUtils;
import com.wydlb.first.utils.RxLogTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by newbiechen on 17-5-8.
 * 存储关于书籍内容的信息(CollBook(收藏书籍),BookChapter(书籍列表),ChapterInfo(书籍章节),BookRecord(记录))
 */

public class BookRepository {
    private static final String TAG = "CollBookManager";
    private static volatile BookRepository sInstance;
    private DaoSession mSession;
    private CollBookBeanDao mCollBookDao;
    private BookRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        mCollBookDao = mSession.getCollBookBeanDao();
    }

    public static BookRepository getInstance(){
        if (sInstance == null){
            synchronized (BookRepository.class){
                if (sInstance == null){
                    sInstance = new BookRepository();
                }
            }
        }
        return sInstance;
    }

    public void saveCollBook(CollBookBean bean){
        RxLogTool.e("saveCollBook:"+bean.getTitle());
        mCollBookDao.insertOrReplace(bean);
    }


    public void deleteBookChapterById(String mBookId){
        mSession.getBookChapterBeanDao().getDatabase().execSQL("delete from BOOK_CHAPTER_BEAN where BOOK_ID='"+mBookId +"'");
    }

    /**
     * 异步存储BookChapter
     * @param beans
     */
    public void saveBookChaptersWithAsync(List<BookChapterBean> beans){
        mSession.startAsyncSession()
                .runInTx(
                        () -> {
                            //存储BookChapterBean--章节ID不能做主键
                            mSession.getBookChapterBeanDao().insertOrReplaceInTx(beans);
                        }
                );
    }

    /**
     * 查询书籍目录
     * @param bookId
     * @return
     */
    public List<BookChapterBean> getChapterByBookId(String bookId){//mSession.getBookChapterBeanDao().queryBuilder().where(BookChapterBeanDao.Properties.BookId.eq("41300")).listLazy()
        return mSession.getBookChapterBeanDao()._queryCollBookBean_BookChapterList(bookId);
    }
    /**
     * 存储章节
     * @param folderName
     * @param fileName
     * @param content
     */
    public void saveChapterInfo(String folderName,String fileName,String content){
        File file = BookManager.getBookFile(folderName, fileName);
        RxLogTool.e("saveChapterInfo--path:"+file.getAbsolutePath());
        //获取流并存储
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.close(writer);
        }
    }



    /*****************************get************************************************/
    public CollBookBean getCollBook(String bookId,String bidstr){
        CollBookBean bean = mCollBookDao.queryBuilder()
                .where(CollBookBeanDao.Properties._id.eq(bookId + bidstr))
                .unique();
        return bean;
    }




    public DaoSession getSession(){
        return mSession;
    }
}
