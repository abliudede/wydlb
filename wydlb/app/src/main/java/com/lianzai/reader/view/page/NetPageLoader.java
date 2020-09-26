package com.lianzai.reader.view.page;


import com.lianzai.reader.base.Constant;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.FileUtils;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-29.
 * 网络页面加载器
 */

public class NetPageLoader extends PageLoader {
    private static final String TAG = "PageFactory";

    public NetPageLoader(PageView pageView,String mBookId) {
        super(pageView,mBookId);
    }

    private List<TxtChapter> convertTxtChapter(List<BookChapterBean> bookChapters) {
        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
        for (BookChapterBean bean : bookChapters) {
            TxtChapter chapter = new TxtChapter();
            chapter.bookId = bean.getBookId();
            chapter.title = bean.getTitle();
            chapter.link = bean.getLink();
            chapter.chapterId=bean.getId();
            chapter.setChapterVip(bean.getIsVip());
            chapter.setSourceKey(bean.getChapterSource());
            txtChapters.add(chapter);
        }
        return txtChapters;
    }

    @Override
    public void refreshChapterList( boolean needReopen,List<BookChapterBean> bookChapters) {
        if (bookChapters == null) return;
        // 将 BookChapter 转换成当前可用的 Chapter
        if(null != bookChapters && !bookChapters.isEmpty()){
//            RxLogTool.e("showLoadingAnimation has mChapterList");
            mChapterList = convertTxtChapter(bookChapters);
        }else {
//            RxLogTool.e("showLoadingAnimation null mChapterList");
        }
        isChapterListPrepare = true;
        // 目录加载完成，执行回调操作。
        if (mPageChangeListener != null) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
        // 如果章节未打开
        if(needReopen){
            // 打开章节
            openChapter();
        }else if (!isChapterOpen()) {
            // 打开章节
            openChapter();
        }
    }

    @Override
    protected BufferedReader getChapterReader(TxtChapter chapter) throws Exception {
        try{
            File file = new File(Constant.BOOK_CACHE_PATH + mBookId + bidStr
                    + File.separator + chapter.chapterId + FileUtils.SUFFIX_NB);
            if (!file.exists()) return null;
            Reader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            if (null!=mChapterList&&mChapterList.get(mCurChapterPos).getChapterId().equals(chapter.getChapterId())&&null!=mPageChangeListener){
                RxLogTool.e("getChapterReader has cache......"+chapter.getTitle());
                mPageChangeListener.currentChapterHasCached(chapter);
            }
            return br;
        }catch (Exception e){

        }
       return  null;
    }

    @Override
    protected boolean hasChapterData(TxtChapter chapter) {
        return BookManager.isChapterCached(mBookId + bidStr, chapter.chapterId);
    }

    // 装载上一章节的内容
    @Override
    boolean parsePrevChapter() {
        boolean isRight = super.parsePrevChapter();
        if (mStatus==STATUS_FINISH){
//            RxLogTool.e("requestPreLoadingChapters parsePrevChapter...");
            requestPreLoadingChapters(5,10);
        }
        else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载当前章内容。
    @Override
    boolean parseCurChapter() {
        boolean isRight = super.parseCurChapter();

        if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    @Override
    public void preLoadingNextChapter() {
        super.preLoadingNextChapter();
//        RxLogTool.e("requestPreLoadingChapters preLoadingNextChapter...");
        requestPreLoadingChapters(5,10);
    }

    // 装载下一章节的内容
    @Override
    boolean parseNextChapter() {
        boolean isRight = super.parseNextChapter();

        if (mStatus==STATUS_FINISH){
//            RxLogTool.e("requestPreLoadingChapters parseNextChapter...");
            requestPreLoadingChapters(5,10);
        }
        else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }

        return isRight;
    }


    /**
     * 加载当前页
     */
    private void loadCurrentChapter() {
        if (mPageChangeListener != null) {

            if (mCurChapterPos>mChapterList.size()){
                mCurChapterPos=mChapterList.size()-1;//防止章节大于当前目录
            }
            if (mCurChapterPos<0){
                mCurChapterPos=0;
            }

            //加载当前章节
            mPageChangeListener.requestCurrentChapter(mChapterList.get(mCurChapterPos));

            //预加载 前三章后五章
//            RxLogTool.e("requestPreLoadingChapters loadCurrentChapter...");
//            requestPreLoadingChapters(3,5);
        }
    }

    /**
     * 预加载章节-
     * @param startPage
     * @param endPage
     */
    private void requestPreLoadingChapters(int startPage,int endPage) {

        int start=mCurChapterPos-startPage ;
        int end=mCurChapterPos+endPage ;

        // 检验输入值
        if (start< 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }


        List<TxtChapter> chapters = new ArrayList<>();

        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            TxtChapter txtChapter = mChapterList.get(i);
            if (i!=mCurChapterPos&&!hasChapterData(txtChapter)) {//未缓存过并且不是当前章节
                chapters.add(txtChapter);
            }
        }

//        RxLogTool.e("requestPreLoadingChapters---start:"+start+"--end:"+end+"--chapters size:"+chapters.size()+"--mCurChapterPos:"+mCurChapterPos);
        if (!chapters.isEmpty()) {
            mPageChangeListener.preLoadingChapters(chapters);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
//        if (mCollBook != null && isChapterListPrepare) {
//            //表示当前CollBook已经阅读
//            mCollBook.setIsUpdate(false);
//            mCollBook.setLastRead(StringUtils.
//                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
//
//            //直接更新
//            BookRepository.getInstance()
//                    .saveCollBook(mCollBook);
//        }
    }
}

