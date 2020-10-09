package com.lianzai.reader.ui.presenter;


import androidx.collection.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lianzai.reader.api.ReaderApi;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.RxPresenter;
import com.lianzai.reader.bean.BookCategoryListResponse;
import com.lianzai.reader.bean.InsideChapterBean;
import com.lianzai.reader.bean.OutsideChapterDetail;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.service.ParserService;
import com.lianzai.reader.ui.contract.NewReadContract;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.RxUtils;
import com.lianzai.reader.utils.StringUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.page.ChapterUrlsVo;
import com.lianzai.reader.view.page.PageLoader;
import com.lianzai.reader.view.page.PageParagraphVo;
import com.lianzai.reader.view.page.PageRow;
import com.lianzai.reader.view.page.TxtChapter;
import com.lianzai.reader.view.page.TxtPage;
import com.lianzai.reader.view.page.UrlsVo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class NewReadPresenter extends RxPresenter<NewReadContract.View>
        implements NewReadContract.Presenter {
    private static final String TAG = "NewReadPresenter";

    private Subscription mChapterSub;

    private ReaderApi mReaderApi;

    ReadSettingsResponse readSettingsResponse;

    @Inject
    public NewReadPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
        //配置读取
        readSettingsResponse = RxSharedPreferencesUtil.getInstance().getObject(Constant.READ_SETTINGS_KEY, ReadSettingsResponse.class);
    }

    public ReaderApi getmReaderApi(){
        return mReaderApi;
    }

    //此处重要，池子有可能混乱
    @Override
    public void preLoadingChapter(String bookId, List<TxtChapter> bookChapters) {
        int size = bookChapters.size();
        //取消上次的任务，防止多次加载
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }

        if(size <= 0){
            return;
        }

        TxtChapter bookChapterTemp = bookChapters.get(0);
        //判断是否是内站书
        Boolean isInside = false;
        try {
            if (bookChapterTemp.getLink().contains("lianzai.com") || bookChapterTemp.getLink().contains("bendixing.net")) {
                isInside = true;
            }
        }catch (Exception e){
        }

        if(isInside) {


            //内站书情况
            List<Single<InsideChapterBean>> chapterInfos = new ArrayList<>(bookChapters.size());
            ArrayDeque<TxtChapter> chapterIds = new ArrayDeque<>(bookChapters.size());
            try {
                //预加载
                for (int i = 0; i < size; ++i) {
                    TxtChapter bookChapter = bookChapters.get(i);
                    if (!(BookManager.isChapterCached(bookId, bookChapter.getChapterId()))) {
                        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
                        arrayMap.put("chapterId", bookChapter.getChapterId());
                        Single<InsideChapterBean> chapterInfoSingle = mReaderApi.insideBookChapterDetail(arrayMap);
                        chapterInfos.add(chapterInfoSingle);
                        chapterIds.add(bookChapter);
                    }
                }
            } catch (Exception e) {
                RxLogTool.e("preLoadingChapter:"+e.getMessage());
            }
            Single.concat(chapterInfos)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            new Subscriber<InsideChapterBean>() {
                                TxtChapter txtChapter = chapterIds.poll();
                                @Override
                                public void onSubscribe(Subscription s) {
                                    s.request(Integer.MAX_VALUE);
                                    mChapterSub = s;
                                }
                                @Override
                                public void onNext(InsideChapterBean responseBody) {
                                    try {
                                        if (txtChapter.isChapterVip()) {//预加载不处理
                                        } else {
                                            //当前的源
                                            //当前的源
                                            String currentSource = txtChapter.getSourceKey();
                                            String charset = "UTF-8";
                                            String regContent = null;
                                            String[] illegalWords = null;
                                            String error = "";//
                                            for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                                if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                                    charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                                    regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                                    RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                                    error = readSettingsResponse.getReadSettings().get(i).getError();
                                                    illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                                }
                                            }
                                            String response = responseBody.getData().getContent();
                                            OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromInside(response );
                                            if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                                String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                                //存储数据
                                                BookRepository.getInstance().saveChapterInfo(
                                                        bookId, txtChapter.getChapterId(), content);
                                                //将获取到的数据进行存储
                                                txtChapter = chapterIds.poll();
                                                //预加载成功
//                                            mView.preLoadingChapterSuccess();
                                                RxEventBusTool.sendEvents(Constant.EventTag.PRELOADINGCHAPTERSUCCESS);
                                                if (!TextUtils.isEmpty(error) && content.contains(error)) {//error 异常上报
                                                    BookChapterBean bookChapterBean = new BookChapterBean();
                                                    bookChapterBean.setId(txtChapter.getChapterId());//章节ID
//                                                mView.uploadErrorChapterView(bookChapterBeans);
                                                    RxEventBusTool.sendEvents(new Gson().toJson(bookChapterBean), Constant.EventTag.UPLOADERRORCHAPTERVIEW);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(Throwable t) {
                                    try {
                                        if (null != t.getMessage() && t.getMessage().toLowerCase().contains("timeout")) {//请求超时
                                        } else {
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onComplete() {
                                    try {
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );


        }else {


            //外站情况
            List<Single<ResponseBody>> chapterInfos = new ArrayList<>(bookChapters.size());
            ArrayDeque<TxtChapter> chapterIds = new ArrayDeque<>(bookChapters.size());
            try {
                //预加载
                for (int i = 0; i < size; ++i) {
                    TxtChapter bookChapter = bookChapters.get(i);
                    if (!(BookManager.isChapterCached(bookId, bookChapter.getChapterId()))) {

                        Single<ResponseBody> chapterInfoSingle = mReaderApi.outsideBookChapterDetail(bookChapter.getLink());
                        chapterInfos.add(chapterInfoSingle);
                        chapterIds.add(bookChapter);
                    }
                }
            } catch (Exception e) {
                RxLogTool.e("preLoadingChapter:"+e.getMessage());
            }
            Single.concat(chapterInfos)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            new Subscriber<ResponseBody>() {
                                TxtChapter txtChapter = chapterIds.poll();
                                @Override
                                public void onSubscribe(Subscription s) {
                                    s.request(Integer.MAX_VALUE);
                                    mChapterSub = s;
                                }
                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    try {
                                        if (txtChapter.isChapterVip()) {//预加载不处理
                                        } else {
                                            //当前的源
                                            //当前的源
                                            String currentSource = txtChapter.getSourceKey();
                                            String charset = "UTF-8";
                                            String regContent = null;
                                            String[] illegalWords = null;
                                            String error = "";//
                                            for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                                if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                                    charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                                    regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                                    RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                                    error = readSettingsResponse.getReadSettings().get(i).getError();
                                                    illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                                }
                                            }
                                            byte[] responseBytes = responseBody.bytes();
                                            String response = new String(responseBytes, charset);
                                            OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromHtml(response, regContent, illegalWords);
                                            if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                                String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                                //存储数据
                                                BookRepository.getInstance().saveChapterInfo(
                                                        bookId, txtChapter.getChapterId(), content);
                                                //将获取到的数据进行存储
                                                txtChapter = chapterIds.poll();
                                                //预加载成功
//                                            mView.preLoadingChapterSuccess();
                                                RxEventBusTool.sendEvents(Constant.EventTag.PRELOADINGCHAPTERSUCCESS);
                                                if (!TextUtils.isEmpty(error) && content.contains(error)) {//error 异常上报
                                                    BookChapterBean bookChapterBean = new BookChapterBean();
                                                    bookChapterBean.setId(txtChapter.getChapterId());//章节ID
//                                                mView.uploadErrorChapterView(bookChapterBeans);
                                                    RxEventBusTool.sendEvents(new Gson().toJson(bookChapterBean), Constant.EventTag.UPLOADERRORCHAPTERVIEW);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(Throwable t) {
                                    try {
                                        if (null != t.getMessage() && t.getMessage().toLowerCase().contains("timeout")) {//请求超时
                                        } else {
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onComplete() {
                                    try {
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );


        }
    }

    boolean hasLocalCategory;//是否有本地缓存目录

    /**
     * 是否需要请求最新目录
     *
     * @param bookChapterBeans
     * @param chapterId
     * @return
     */
    private boolean isNeedLoadCategory(List<BookChapterBean> bookChapterBeans, String chapterId) {

        int cid = -1;
        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
            if (txtChapter.getId().equals(chapterId)) {//根据章节ID
                cid = i;
            }
        }
        return cid >= 0 ? false : true;//如果本地没有匹配到对应的章节ID，需要从网络请求
    }

    /**
     * 是否需要请求最新目录
     *
     * @param bookChapterBeans
     * @param chapterId
     * @return
     */
    private int isNeedLoadCategoryForUrl(List<BookChapterBean> bookChapterBeans, String chapterId) {//,String chapterUrl
        int cid = -1;
        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
//            if (txtChapter.getLink().equals(chapterUrl)) {//根据章节ID
//                cid = Integer.parseInt(txtChapter.getId());
//                continue;
//            }
            if (txtChapter.getId().equals(chapterId)) {//根据章节ID
                cid = i;
                break;
            }
        }
        return cid;//如果本地没有匹配到对应的章节ID，需要从网络请求
    }

    /**
     * 是否需要请求最新目录
     *
     * @param bookChapterBeans
     * @param chapterUrl
     * @return
     */
    private int isNeedLoadCategoryForUrl2(List<BookChapterBean> bookChapterBeans, String chapterUrl) {//,String chapterUrl
        int cid = -1;
        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
//            if (txtChapter.getLink().equals(chapterUrl)) {//根据章节ID
//                cid = Integer.parseInt(txtChapter.getId());
//                continue;
//            }
            if (txtChapter.getLink().equals(chapterUrl)) {//根据章节Url
                cid = i;
                break;
            }
        }
        return cid;//如果本地没有匹配到对应的章节ID，需要从网络请求
    }


    @Override
    public void loadCategory(String mBookId, ArrayMap<String, Object> params, String chapterId) {
        hasLocalCategory = false;
        if (!TextUtils.isEmpty(mBookId)) {
            CollBookBean collBookBean = BookRepository.getInstance().getCollBook(mBookId ,"");
            if (null != collBookBean) {//缓存不可用

                List<BookChapterBean> bookChapterBeans = BookRepository.getInstance().getChapterByBookId(mBookId);

                if (null != collBookBean && null != bookChapterBeans && bookChapterBeans.size() > 0) {
                    if (!TextUtils.isEmpty(chapterId) && isNeedLoadCategory(bookChapterBeans, chapterId)) {//直接跳转到某一章节
                        //不显示缓存的数据，需要等服务器目录数据拿到之后才处理
                    } else {
                        hasLocalCategory = true;
                        collBookBean.setBookChapters(bookChapterBeans);
                        mView.getBookDirectoryView(collBookBean, false, false);
                    }
                }
            } else {
                if (!RxNetTool.isAvailable()) {
                    RxToast.custom("离线模式下没有缓存该书", Constant.ToastType.TOAST_ERROR).show();
                }
            }
        }

        if (RxNetTool.isAvailable()) {
            Single<BookCategoryListResponse> single = mReaderApi.bookCategoryIntelligent(mBookId, params);
            Disposable disp = single
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            data -> {
                                try {
                                    List<BookChapterBean> bookChapterBeans = new ArrayList<>();

                                    if (data != null && mView != null && data.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE && null != data.getData()) {
                                        //更新本地缓存的书书籍
                                        CollBookBean collBookBean = new CollBookBean();
                                        if (!TextUtils.isEmpty(data.getData().getNovel_id())) {
                                            collBookBean.set_id(data.getData().getNovel_id());
                                        }else {
                                            collBookBean.set_id(mBookId);
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getAuthor_name())) {
                                            collBookBean.setAuthor(data.getData().getAuthor_name());
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getBook_name())) {
                                            collBookBean.setTitle(data.getData().getBook_name());
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getCover())) {
                                            collBookBean.setCover(data.getData().getCover());
                                        }

                                        ArrayList<ChapterUrlsVo> chapterUrlsVos = new ArrayList<>();//源列表

                                        for (BookCategoryListResponse.DataBean.VolumesBean volumesBean : data.getData().getVolumes()) {
                                            for (BookCategoryListResponse.DataBean.VolumesBean.ChaptersBean chaptersBean : volumesBean.getChapters()) {
                                                ChapterUrlsVo chapterUrlsVo = new ChapterUrlsVo();
                                                chapterUrlsVo.setBookId(mBookId);
                                                chapterUrlsVo.setChapterId(chaptersBean.getChapter_id());
                                                List<UrlsVo> urlsVos = new ArrayList<>();


                                                BookChapterBean bookChapterBean = new BookChapterBean();
                                                bookChapterBean.setId(chaptersBean.getChapter_id());
                                                bookChapterBean.setTitle(chaptersBean.getTitle());//章节名称
                                                if (chaptersBean.getUrls().size() > 0) {
                                                    bookChapterBean.setIsVip(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).isIsvip());
                                                    bookChapterBean.setLink(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).getUrl());
                                                    bookChapterBean.setChapterSource(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).getSource());

                                                    //源列表集合
                                                    for (BookCategoryListResponse.DataBean.VolumesBean.ChaptersBean.UrlBean urlBean : chaptersBean.getUrls()) {
                                                        UrlsVo urlsVo = new UrlsVo();
                                                        urlsVo.setSource(urlBean.getSource());
                                                        urlsVo.setSourceUrl(urlBean.getUrl());

                                                        urlsVos.add(urlsVo);

                                                        //这个源已经切换过了，默认选中这个源
                                                        String cacheSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");

//                                                       //章节源
                                                        String chapterSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_" + chaptersBean.getChapter_id() + "_source");

                                                        if (!TextUtils.isEmpty(chapterSource) && chapterSource.equals(urlBean.getSource())) {//优先取章节的源
                                                            bookChapterBean.setLink(urlBean.getUrl());
                                                            bookChapterBean.setChapterSource(urlBean.getSource());
                                                            bookChapterBean.setIsVip(urlBean.isIsvip());

                                                        } else if (!TextUtils.isEmpty(cacheSource) && cacheSource.equals(urlBean.getSource())) {
                                                            bookChapterBean.setLink(urlBean.getUrl());
                                                            bookChapterBean.setChapterSource(urlBean.getSource());
                                                            bookChapterBean.setIsVip(urlBean.isIsvip());
                                                        }


                                                    }

                                                    //添加-如果这个目录里面一条数据都没有。前端过滤下不显示该章节
                                                    bookChapterBeans.add(bookChapterBean);

                                                } else {//这个章节没有站点,添加到这个集合-统一上报给后台
                                                    RxEventBusTool.sendEvents(new Gson().toJson(bookChapterBean), Constant.EventTag.UPLOADERRORCHAPTERVIEW);
                                                }

                                                chapterUrlsVo.setUrlsVoList(urlsVos);
                                                chapterUrlsVos.add(chapterUrlsVo);
                                            }
                                        }

                                        if (chapterUrlsVos.size() > 0) {//返回当前书的源列表
                                            mView.returnChapterUrlsView(chapterUrlsVos);
                                        }

                                        if (bookChapterBeans.size() > 0) {

                                            collBookBean.setBookChapters(bookChapterBeans);

                                        }

                                        //更新书的相关信息
                                        BookRepository.getInstance().saveCollBook(collBookBean);


                                        boolean needReopen = false;
                                        try {
                                            needReopen = (boolean) params.get("needReopen");
                                        } catch (Exception e) {

                                        }
                                        mView.getBookDirectoryView(collBookBean, true, needReopen);

                                    } else {
                                        if (hasLocalCategory == false) {
                                            mView.getBookDirectoryErrorView(data.getMsg());
                                        }
                                    }
                                } catch (Exception e) {
                                    if (hasLocalCategory == false) {
                                        mView.getBookDirectoryErrorView("");
                                    }
                                }
                            },
                            e -> {
                                try {
                                    if (hasLocalCategory == false) {
                                        if (null != e.getMessage() && (e.getMessage().toLowerCase().contains("timeout") || e.getMessage().toLowerCase().contains("interceptor"))) {//请求超时
                                            mView.getBookDirectoryTimeout();
                                        } else {
                                            mView.getBookDirectoryErrorView("");
                                        }
                                    }


                                } catch (Exception ex) {
                                }
                            }
                    );
            addDisposable(disp);
        }
    }

    @Override
    public void urlLoadCategory(String mBookId, ArrayMap<String, Object> params, String chapterId, String chapterUrl, String chapterTitle,String bidstr) {//,String chapterUrl
        hasLocalCategory = false;
        if (!TextUtils.isEmpty(mBookId)) {
            CollBookBean collBookBean = BookRepository.getInstance().getCollBook(mBookId,bidstr);
            if (null != collBookBean) {//缓存不可用

                List<BookChapterBean> bookChapterBeans = BookRepository.getInstance().getChapterByBookId(mBookId + bidstr);

                if (null != collBookBean && null != bookChapterBeans && bookChapterBeans.size() > 0) {
                    int cidTemp = -1;
                    if (!TextUtils.isEmpty(chapterId)) {//!TextUtils.isEmpty(chapterUrl) ||
                        //有章节需要跳，找出对应章节
                        cidTemp = isNeedLoadCategoryForUrl(bookChapterBeans, chapterId);//,chapterId
                        if (cidTemp < 0) {
                            RxLogTool.e("本地没有对应的章节");
                        } else {
                            hasLocalCategory = true;
                            collBookBean.setBookChapters(bookChapterBeans);
                            mView.getBookDirectoryView2(collBookBean, false);//,String.valueOf(cidTemp)
                        }
                    }else if (!TextUtils.isEmpty(chapterUrl)) {//!TextUtils.isEmpty(chapterUrl) ||
                        //有章节需要跳，找出对应章节
                        cidTemp = isNeedLoadCategoryForUrl2(bookChapterBeans, chapterUrl);//,chapterUrl
                        if (cidTemp < 0) {
                            RxLogTool.e("本地没有对应的章节");
                        } else {
                            hasLocalCategory = true;
                            collBookBean.setBookChapters(bookChapterBeans);
                            mView.getBookDirectoryView2(collBookBean, false);//,String.valueOf(cidTemp)
                        }
                    }else if (!TextUtils.isEmpty(chapterTitle)) {//!TextUtils.isEmpty(chapterUrl) ||
                        //有章节需要跳，找出对应章节
//                        cidTemp = isNeedLoadCategoryForUrl3(bookChapterBeans, chapterTitle);//,chapterUrl
//                        if (cidTemp < 0) {
//                            RxLogTool.e("本地没有对应的章节");
//                        } else {
                            hasLocalCategory = true;
                            collBookBean.setBookChapters(bookChapterBeans);
                            mView.getBookDirectoryView2(collBookBean, false);//,String.valueOf(cidTemp)
//                        }
                    } else {
                        //没有章节需要跳，直接以本地阅读进度为准
                        hasLocalCategory = true;
                        collBookBean.setBookChapters(bookChapterBeans);
                        mView.getBookDirectoryView2(collBookBean, false);//,String.valueOf(cidTemp)
                    }
                }else {
                    if (!RxNetTool.isAvailable()) {
                        RxToast.custom("离线模式下没有缓存该书", Constant.ToastType.TOAST_ERROR).show();
                    }
                }
            } else {
                if (!RxNetTool.isAvailable()) {
                    RxToast.custom("离线模式下没有缓存该书", Constant.ToastType.TOAST_ERROR).show();
                }
            }
        } else {
            return;
        }

        if (RxNetTool.isAvailable()) {
            Single<BookCategoryListResponse> single = mReaderApi.bookCatalogRecognition(mBookId, params);
            Disposable disp = single
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            data -> {
                                try {
                                    List<BookChapterBean> bookChapterBeans = new ArrayList<>();

                                    if (data != null && mView != null && data.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE && null != data.getData()) {
                                        //更新本地缓存的书书籍
                                        CollBookBean collBookBean = new CollBookBean();
                                        //更新书的相关信息
                                        if(Integer.parseInt(mBookId) > Constant.bookIdLine) {
                                            collBookBean.set_id(mBookId);
                                        }else {
                                            collBookBean.set_id(mBookId + bidstr);
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getAuthor_name())) {
                                            collBookBean.setAuthor(data.getData().getAuthor_name());
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getBook_name())) {
                                            collBookBean.setTitle(data.getData().getBook_name());
                                        }

                                        if (!TextUtils.isEmpty(data.getData().getCover())) {
                                            collBookBean.setCover(data.getData().getCover());
                                        }

                                        for (BookCategoryListResponse.DataBean.VolumesBean volumesBean : data.getData().getVolumes()) {
                                            for (BookCategoryListResponse.DataBean.VolumesBean.ChaptersBean chaptersBean : volumesBean.getChapters()) {
                                                BookChapterBean bookChapterBean = new BookChapterBean();
                                                bookChapterBean.setId(chaptersBean.getChapter_id());
                                                bookChapterBean.setTitle(chaptersBean.getTitle());//章节名称
                                                bookChapterBean.setBookId(mBookId + bidstr);
                                                bookChapterBean.setTime(chaptersBean.getTime());
                                                if (chaptersBean.getUrls().size() > 0) {
                                                    bookChapterBean.setIsVip(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).isIsvip());
                                                    bookChapterBean.setLink(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).getUrl());
                                                    bookChapterBean.setChapterSource(chaptersBean.getUrls().get(chaptersBean.getSourceKeyPosition()).getSource());
                                                    //添加-如果这个目录里面一条数据都没有。前端过滤下不显示该章节
                                                    bookChapterBeans.add(bookChapterBean);

                                                } else {//这个章节没有站点,添加到这个集合-统一上报给后台
//                                                mView.uploadErrorChapterView(bookChapterBeans);
                                                    RxEventBusTool.sendEvents(new Gson().toJson(bookChapterBean), Constant.EventTag.UPLOADERRORCHAPTERVIEW);
                                                }
                                            }
                                        }

                                        if (bookChapterBeans.size() > 0) {
                                            collBookBean.setBookChapters(bookChapterBeans);
                                        }

                                        mView.getBookDirectoryView2(collBookBean, true);//,data.getData().getChapter_index_id()

                                    } else {
                                        if (hasLocalCategory == false) {
                                            mView.getBookDirectoryErrorView(data.getMsg());
                                        }
                                    }
                                } catch (Exception e) {
                                    if (hasLocalCategory == false) {
                                        mView.getBookDirectoryErrorView("");
                                    }
                                }
                            },
                            e -> {
                                try {
                                    if (hasLocalCategory == false) {
                                        if (null != e.getMessage() && (e.getMessage().toLowerCase().contains("timeout") || e.getMessage().toLowerCase().contains("interceptor"))) {//请求超时
                                            mView.getBookDirectoryTimeout();
                                        } else {
                                            mView.getBookDirectoryErrorView("");
                                        }
                                    }
                                } catch (Exception ex) {
                                }
                            }
                    );
            addDisposable(disp);
        }
    }


    @Override
    public void prepareListenBookData(PageLoader mPageLoader,boolean enter) {
        if (null == mPageLoader) {
            return;
        }
        //听书
        List<PageParagraphVo> pageParagraphVos = new ArrayList<>();
        pageParagraphVos = getChapterContentByPagePosition(mPageLoader,enter);//数据准备
        //数据返回 String listenData,int startPageWordCount,List<PageParagraphVo> pageParagraphVos
        mView.returnListenBookDataView(pageParagraphVos);
    }

    /**
     * 根据当前页获取本章剩余的数据
     *
     * @return
     */
    private List<PageParagraphVo> getChapterContentByPagePosition(PageLoader mPageLoader,boolean enter) {


        if (null != mPageLoader && null != mPageLoader.getCurPageList()) {
            //清空返回的数据数组
            List<PageParagraphVo> pageParagraphVos = new ArrayList<>();
            pageParagraphVos.clear();
            //载入当前页面的数据
            if (null != mPageLoader.getCurPage()) {
                TxtPage txtPage = mPageLoader.getCurPage();

                int currentParagraphPosition = 1;//当前页段落位置

                if (txtPage.position == 0 && mPageLoader.getCurrentParagraphPosition() == 1) {//第一页之前要加上标题部分
                        PageParagraphVo pageParagraphVo = new PageParagraphVo();
                        pageParagraphVo.pagePosition = txtPage.position;
                        pageParagraphVo.paragraphPosition = currentParagraphPosition;//标题段落-特殊段落，
                        pageParagraphVo.text = txtPage.title;
                        //段落集合
                        pageParagraphVos.add(pageParagraphVo);
                        currentParagraphPosition++;
                    } else if (mPageLoader.getCurrentParagraphPosition() > 1) {
                        currentParagraphPosition = mPageLoader.getCurrentParagraphPosition();
                    }

                //准备下一页的数据
                TxtPage nextTxtPage = null;
                if(null != mPageLoader.getCurPageList() && mPageLoader.getCurPageList().size() > txtPage.position + 1) {
                    nextTxtPage  = mPageLoader.getCurPageList().get(txtPage.position + 1);
                }else {
                    nextTxtPage = null;
                }

                //如果是从指定位置开始，也加入接下来的段落
                for (int k = currentParagraphPosition; k <= txtPage.paraCount; k++) {//当前页有几个段落
                    PageParagraphVo pageParagraphVo = new PageParagraphVo();
                    StringBuffer sbParagraph = new StringBuffer();//把所有行规整到一个段落里面
                    for (PageRow pageRow : txtPage.pageRows) {//行数据
                        if (pageRow.currentParagraphPos == k) {//这一行是属于这个段落的
                            sbParagraph.append(pageRow.line);
                        }
                    }

                    pageParagraphVo.pagePosition = txtPage.position;
                    pageParagraphVo.paragraphPosition = k;//所在段落
                    if(k == txtPage.paraCount){
                        //拼上下一页的第一段的第一个标点符号之前
                        if(null != nextTxtPage){
                            StringBuilder sb = new StringBuilder();
                            for (PageRow pageRow : nextTxtPage.pageRows) {//行数据
                                if (pageRow.currentParagraphPos == 1) {//这一行是属于这个段落的
                                    sb.append(pageRow.line);
                                }
                            }
                            sbParagraph.append(getString(sb.toString(),1));
                        }
                    }

                    //不是enter的情况下，去除掉第一段第一个标点符号之前的文字
                   if(!enter && k == 1){
                       pageParagraphVo.text = getString(StringUtils.fullToHalf(sbParagraph.toString()),2);
                   }else {
                       pageParagraphVo.text = StringUtils.fullToHalf(sbParagraph.toString());
                   }




                    pageParagraphVos.add(pageParagraphVo);//段落坐标集合
                }
            }
            return pageParagraphVos;
        }
        return null;
    }


    private String getString(String str,int oneOrTwo){
        String tempStr = str.replace("、","、$$$").replace("，","，$$$").replace("。","。$$$")
                .replace("；","；$$$").replace("？","？$$$").replace("！","！$$$")
                .replace(",",",$$$").replace(".",".$$$").replace(";",";$$$")
                .replace("?","?$$$").replace("!","!$$$").replace("…","…$$$");

        String[] strs = tempStr.split("[$$$]");
        if(oneOrTwo == 1){
            return strs[0];
        }else {
            if(strs.length > 1){
                StringBuilder sb = new StringBuilder("  ");
                for(int i = 1 ; i < strs.length ; i++){
                    sb.append(strs[i]);
                }
                return sb.toString();
            }else {
                return "  ";
            }
        }
    }


    @Override
    public void reloadChapter(String bookId, final TxtChapter txtChapter) {
        //判断是否是内站书
        Boolean isInside = false;
        try {
            if (txtChapter.getLink().contains("lianzai.com") || txtChapter.getLink().contains("bendixing.net")) {
                isInside = true;
            }
        }catch (Exception e){
        }


        if(isInside) {
            ArrayMap<String, Object> arrayMap = new ArrayMap<>();
            arrayMap.put("chapterId", txtChapter.getChapterId());

            Disposable disposable = mReaderApi.insideBookChapterDetail(arrayMap).compose(RxUtils::toSimpleSingle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            data -> {
                                try {
                                    //当前的源
                                    String currentSource = txtChapter.getSourceKey();

                                    String charset = "UTF-8";
                                    String regContent = null;
                                    String[] illegalWords = null;

                                    for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                        if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                            charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                            regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                            RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                            illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                        }
                                    }
                                    String response = data.getData().getContent();

                                    OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromInside(response );
                                    if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                        String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                        //存储数据
                                        BookRepository.getInstance().saveChapterInfo(
                                                bookId, txtChapter.getChapterId(), content);
                                        RxEventBusTool.sendEvents(new Gson().toJson(txtChapter), Constant.EventTag.RELOADCHAPTERSUCCESSVIEW);
                                    } else {
                                        RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                                }
                            },
                            e -> {
                                RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                            }
                    );
            addDisposable(disposable);
        }else {
            Disposable disposable = mReaderApi.outsideBookChapterDetail(txtChapter.getLink()).compose(RxUtils::toSimpleSingle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            data -> {
                                try {
                                    //当前的源
                                    String currentSource = txtChapter.getSourceKey();

                                    String charset = "UTF-8";
                                    String regContent = null;
                                    String[] illegalWords = null;

                                    for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                        if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                            charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                            regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                            RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                            illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                        }
                                    }
                                    byte[] responseBytes = data.bytes();

                                    String response = new String(responseBytes, charset);

                                    OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromHtml(response, regContent, illegalWords);
                                    if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                        String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                        //存储数据
                                        BookRepository.getInstance().saveChapterInfo(
                                                bookId, txtChapter.getChapterId(), content);
                                        RxEventBusTool.sendEvents(new Gson().toJson(txtChapter), Constant.EventTag.RELOADCHAPTERSUCCESSVIEW);
                                    } else {
                                        RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                                }
                            },
                            e -> {
                                RxEventBusTool.sendEvents(Constant.EventTag.RELOADCHAPTERFAILEDVIEW);
                            }
                    );
            addDisposable(disposable);
        }
    }

    @Override
    public void loadCurrentChapter(String bookId, final TxtChapter txtChapter) {
        //判断是否是内站书
        Boolean isInside = false;
        try {
            if (txtChapter.getLink().contains("lianzai.com") || txtChapter.getLink().contains("bendixing.net")) {
                isInside = true;
            }
        }catch (Exception e){
        }

        if(isInside){
            ArrayMap<String,Object> arrayMap=new ArrayMap<>();
            arrayMap.put("chapterId",txtChapter.getChapterId());

            Disposable disposable = mReaderApi.insideBookChapterDetail(arrayMap).compose(RxUtils::toSimpleSingle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            data -> {
                                try {
                                    if (txtChapter.isChapterVip()) {
                                        mView.vipChapter(txtChapter);
                                    } else {
                                        //当前的源
                                        String currentSource = txtChapter.getSourceKey();

                                        String charset = "UTF-8";
                                        String regContent = null;
                                        String[] illegalWords = null;
                                        for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                            if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                                charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                                regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                                RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                                illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                            }
                                        }
                                        String response = data.getData().getContent();

                                        OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromInside(response );
                                        if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                            String content = RxTool.filterChar(outsideChapterDetail.getContent());

                                            //存储数据
                                            BookRepository.getInstance().saveChapterInfo(
                                                    bookId, txtChapter.getChapterId(), content);
                                            RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERSUCCESS);
                                        } else {
                                            RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                }
                            },
                            e -> {
                                try {
                                    if (null != e.getMessage() && e.getMessage().toLowerCase().contains("timeout")) {//请求超时
                                        RxEventBusTool.sendEvents(new Gson().toJson(txtChapter), Constant.EventTag.LOADCURRENTCHAPTERTIMEOUT);
                                    } else {
                                        RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                    }

                                } catch (Exception ex) {
                                    e.printStackTrace();

                                }
                            }
                    );
            addDisposable(disposable);
        }else {
            Disposable disposable = mReaderApi.outsideBookChapterDetail(txtChapter.getLink()).compose(RxUtils::toSimpleSingle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            data -> {
                                try {
                                    if (txtChapter.isChapterVip()) {
                                        mView.vipChapter(txtChapter);
                                    } else {
                                        //当前的源
                                        String currentSource = txtChapter.getSourceKey();

                                        String charset = "UTF-8";
                                        String regContent = null;
                                        String[] illegalWords = null;
                                        for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                            if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                                charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                                regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                                RxLogTool.e("currentSource:" + currentSource + "       regContent :" + regContent);
                                                illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                            }
                                        }
                                        byte[] responseBytes = data.bytes();

                                        String response = new String(responseBytes, charset);

                                        OutsideChapterDetail outsideChapterDetail = ParserService.getDataBeanFromHtml(response, regContent, illegalWords);
                                        if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                            String content = RxTool.filterChar(outsideChapterDetail.getContent());

                                            //存储数据
                                            BookRepository.getInstance().saveChapterInfo(
                                                    bookId, txtChapter.getChapterId(), content);
                                            RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERSUCCESS);
                                        } else {
                                            RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                }
                            },
                            e -> {
                                try {
                                    if (null != e.getMessage() && e.getMessage().toLowerCase().contains("timeout")) {//请求超时
                                        RxEventBusTool.sendEvents(new Gson().toJson(txtChapter), Constant.EventTag.LOADCURRENTCHAPTERTIMEOUT);
                                    } else {
                                        RxEventBusTool.sendEvents(Constant.EventTag.LOADCURRENTCHAPTERFAILED);
                                    }

                                } catch (Exception ex) {
                                    e.printStackTrace();

                                }
                            }
                    );
            addDisposable(disposable);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }
    }
}
