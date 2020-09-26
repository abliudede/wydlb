package com.lianzai.reader.ui.contract;


import android.support.v4.util.ArrayMap;

import com.lianzai.reader.base.BaseContract;
import com.lianzai.reader.bean.ChapterGroupBean;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.view.page.ChapterUrlsVo;
import com.lianzai.reader.view.page.PageLoader;
import com.lianzai.reader.view.page.PageParagraphVo;
import com.lianzai.reader.view.page.TxtChapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-16.
 */

public interface NewReadContract extends BaseContract {
    interface View extends BaseContract.BaseView {

        void getBookDirectoryView(CollBookBean collBookBean,boolean isUpdateCategory,boolean needReopen);
        void getBookDirectoryView2(CollBookBean collBookBean, boolean isUpdateCategory);//,String ChapterId
        void getBookDirectoryErrorView(String message);
        void getBookDirectoryTimeout();
        void vipChapter(TxtChapter txtChapter);
//        void preLoadingChapterSuccess();

        void returnListenBookDataView(List<PageParagraphVo> pageParagraphVos);

        void returnChapterUrlsView(List<ChapterUrlsVo>chapterUrlsVos);

//        void uploadErrorChapterView(List<BookChapterBean>errorBookChapterBeans);

    }

    interface Presenter extends BaseContract.BasePresenter<NewReadContract.View>{
        void loadCategory(String bookId,ArrayMap<String, Object> params,String chapterId);
        void urlLoadCategory(String bookId,ArrayMap<String, Object> params,String chapterId, String chapterUrl, String chapterTitle,String bidstr);//,String chapterUrl
        void preLoadingChapter(String bookId, List<TxtChapter> bookChapters);//预加载章节

        void loadCurrentChapter(String bookId, TxtChapter txtChapter);//加载当前章节

        void reloadChapter(String bookId, TxtChapter txtChapter);

        void prepareListenBookData(PageLoader mPageLoader,boolean enter);
    }
}
