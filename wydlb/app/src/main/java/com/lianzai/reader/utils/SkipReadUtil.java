package com.lianzai.reader.utils;

import android.content.Context;
import android.text.TextUtils;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.model.local.CloudRecordRepository;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;

public class SkipReadUtil {

//    public static void startActivity( Context context, String bookId){
//        startActivity(context,bookId,null);
//    }
//
//    public static void startActivity( Context context, String bookId, String chapterId) {
//       if(null == bookId) return;
//       //假如是开启了自动转码，则不再请求接口
////        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
//            firstFailed(context, bookId,chapterId);
////            return;
////        }
////       //先获取到是否禁止阅读
////        bookIsHidden(context,bookId,chapterId);
//    }


    //判断书籍是否被禁止阅读
//    private static void bookIsHidden(Context context, String bookId,String chapterId){
////        Map<String, String> paramsMap=new HashMap<>();
////        paramsMap.put("bookId",bookId);
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL+"/platforms/bookIsHidden/" + bookId , new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                try {
//                    RxLogTool.e("bookIsHidden Exception---:"+e.toString());
//                    //第一轮接口失败
//                     firstFailed(context, bookId,chapterId);
//                }catch (Exception ee){
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                RxLogTool.e("bookIsHidden response---:"+response);
//                try{
//                    BookIsHiddenBean bookIsHiddenBean=GsonUtil.getBean(response,BookIsHiddenBean.class);
//                    if (bookIsHiddenBean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE && bookIsHiddenBean.getData().isIsHidden() && bookIsHiddenBean.getData().getPlatformType() != 2){
//                        //接口请求成功，且这本书被隐藏，且这本书不是外站书，则开始执行禁止阅读逻辑。
//                        //获取源字符串
//                        String bidStr = SkipReadUtil.getSource(bookId);
//                        String chapterIdResult = null;
//                        if(TextUtils.isEmpty(chapterId)){
//                            //先拿本地缓存，再拿云端记录，云端记录优先，覆盖本地缓存
//                            CollBookBean collBookBean=BookRepository.getInstance().getCollBook(bookId, bidStr);
//                            if(null != collBookBean){
//                                //本地有缓存时，获取阅读进度
//                                BookRecordBean mBookRecord = BookRepository.getInstance()
//                                        .getBookRecord(bookId + bidStr);
//                                if(null != mBookRecord && null != collBookBean.getBookChapters() &&  collBookBean.getBookChapters().size() > 0){
//                                    int index = mBookRecord.getChapter();
//                                    //有阅读记录时，从本地缓存里取出章节id
//                                    if(index > collBookBean.getBookChapters().size()) {
//                                        index = collBookBean.getBookChapters().size() - 1;//防止mCurChapterPos索引大于章节目录
//                                    }
//                                    chapterIdResult = collBookBean.getBookChapters().get(index).getId();
//                                    bidStr = collBookBean.getBookChapters().get(index).getChapterSource();
//                                }
//                            }
//                            //假如是登录状态，获取云端记录。
//                            if (RxLoginTool.isLogin()) {
//                                //登录情况时才有云端记录
//                                String uid = String.valueOf(RxLoginTool.getLoginAccountToken().getData().getUid());
//                                CloudRecordBean bean = CloudRecordRepository.getInstance().getCloudRecord(Long.parseLong(uid), Long.parseLong(bookId));
//                                if(null != bean)
//                                chapterIdResult = String.valueOf(bean.getChapterId());
//                            }
//                        }else {
//                            //有章节id时，直接获取到章节id。
//                            chapterIdResult = chapterId;
//                        }
//                        /*此时数据已准备好，开始请求接口,上传bookId，chapterId，source，获取该跳转的url*/
//                        getReadingUrl(context,bookId,chapterIdResult,bidStr);
//
//                    }else {
//                        //第一轮接口失败
//                        firstFailed(context, bookId,chapterId);
//                    }
//                }catch (Exception e){
//                    RxLogTool.e("bookIsHidden:"+e.toString());
//                    //第一轮接口失败
//                    firstFailed(context, bookId,chapterId);
//                }
//
//            }
//        });
//    }

//    private static void firstFailed(Context context, String bookId,String chapterId){
//        //第一轮接口失败
//        if(null != chapterId) {
//            UrlReadActivity.startActivityCid(context, bookId,chapterId,null);
//        }else {
//            UrlReadActivity.startActivityNormal(context, bookId);
//        }
//    }


    //获取禁止阅读书籍的url
//    private static void getReadingUrl( Context context,String bookId,String chapterId,String source){
//        Map<String, String> paramsMap=new HashMap<>();
//        //source为空则不传
//        if(!TextUtils.isEmpty(source))
//        paramsMap.put("source",source);
//
//        if(TextUtils.isEmpty(chapterId)) {
//            chapterId = "0";
//        }
//
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL+"/platforms/getReadingUrl/"+bookId+"/" + chapterId ,paramsMap, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                try {
//                    RxLogTool.e("getReadingUrl Exception---:"+e.toString());
//                    //第二轮接口失败
//                    RxToast.custom("获取阅读链接失败").show();
//                }catch (Exception ee){
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                RxLogTool.e("getReadingUrl response---:"+response);
//                try{
//                    GetReadingUrlBean getReadingUrlBean=GsonUtil.getBean(response,GetReadingUrlBean.class);
//                    if (getReadingUrlBean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE && !TextUtils.isEmpty(getReadingUrlBean.getData().getChapter_url())){
//                        ActivityWebView.startActivityForRead(context,getReadingUrlBean.getData().getChapter_url(),bookId);
//                    }else {
//                        //第二轮接口失败
//                        RxToast.custom("获取阅读链接失败").show();
//                    }
//                }catch (Exception e){
//                    RxLogTool.e("getReadingUrl:"+e.toString());
//                    //第二轮接口失败
//                    RxToast.custom("获取阅读链接失败").show();
//                }
//
//            }
//        });
//    }

    public static String getSource(String mBookId){
//        if(!TextUtils.isEmpty(source)){
//            return source.replace(".","");
//        }else {

        if(Integer.parseInt(mBookId) <= Constant.bookIdLine){
            //获取本地存储的source
            String localSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            if(!TextUtils.isEmpty(localSource)){
                return localSource.replace(".","");
            }else {
                return "";
            }
        }else {
            return "";
        }
//        }
    }


//    public static void deleteRecord(String bookId){
//        AccountDetailBean account = RxTool.getAccountBean();
//        if (null != account) {
//            //登录情况时才有云端记录
//            String uid = String.valueOf(account.getData().getUid());
//            CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(uid), Long.parseLong(bookId));
//        } else {
//            //未登录的情况直接采用uid等于0
//            String uid = "0";
//            CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(uid), Long.parseLong(bookId));
//        }
//    }



    public static CloudRecordBean getCloudRecord(String bookId){
        //此处判断是否有云端记录。
        CloudRecordBean bean = CloudRecordRepository.getInstance().getCloudRecord( Long.parseLong(bookId));
        if (null == bean) {
            return null;
        } else {
            return bean;
        }


//        AccountDetailBean account = RxTool.getAccountBean();
//        if (null != account) {
//            //登录情况时才有云端记录
//            String uid = String.valueOf(account.getData().getUid());
//            CloudRecordBean bean = CloudRecordRepository.getInstance().getCloudRecord(Long.parseLong(uid), Long.parseLong(bookId));
//            if (null == bean) {
//               return null;
//            } else {
////                CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(uid), Long.parseLong(bookId));
//                return bean;
//            }
//        } else {
//            //未登录的情况直接采用uid等于0
//            String uid = "0";
//            CloudRecordBean bean = CloudRecordRepository.getInstance().getCloudRecord(Long.parseLong(uid), Long.parseLong(bookId));
//            if (null == bean) {
//                return null;
//            } else {
////                CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(uid), Long.parseLong(bookId));
//                return bean;
//            }
//        }
    }

    //最普通的跳转模式
    public static void normalRead(Context context,String bookId,String source,boolean openMulu){
        CloudRecordBean bean = SkipReadUtil.getCloudRecord(bookId);
        if(null != bean){
            if(!TextUtils.isEmpty(bean.getChapterTitle())){
                int pagePos = bean.getPos();
                UrlReadActivity.startActivityOutsideRead(context,bookId,TextUtils.isEmpty(source)? bean.getSource() : source,openMulu,"",bean.getChapterTitle(),pagePos,false);
            }else {
                //此模式下无法显示重试页面
                UrlReadActivity.startActivityOutsideRead(context,bookId,TextUtils.isEmpty(source)? bean.getSource() : source,openMulu,String.valueOf(bean.getChapterId()),"",0,false);
            }
        }else {
            UrlReadActivity.startActivityOutsideRead(context,bookId,source,openMulu,"","",0,false);
        }
    }

    public static void linkRead(Context context,String bookId,String source,boolean openMulu,String skipUrl){

            //先判断云端记录
            CloudRecordBean bean = SkipReadUtil.getCloudRecord(bookId);
            //有云端记录就跳云端记录
            if(null != bean){
                if(!TextUtils.isEmpty(bean.getChapterTitle())){
                    int pagePos = bean.getPos();
                    UrlReadActivity.startActivityOutsideRead(context,bookId,TextUtils.isEmpty(source)? bean.getSource() : source,openMulu,"",bean.getChapterTitle(),pagePos,false);
                }else {
                    //此模式下无法显示重试页面
                    UrlReadActivity.startActivityOutsideRead(context,bookId,TextUtils.isEmpty(source)? bean.getSource() : source,openMulu,String.valueOf(bean.getChapterId()),"",0,false);
                }
            }else {
                //开启自动转码的情况
                if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
                    UrlReadActivity.startActivityOutsideRead(context,bookId,source,openMulu,"","",0,false);
                }else if(TextUtils.isEmpty(skipUrl)){
                    //没有可以跳转的网址时，也直接跳阅读页面
                    UrlReadActivity.startActivityOutsideRead(context,bookId,source,openMulu,"","",0,false);
                }else {
                    //没有记录并且没开启转码就直接跳网页
                    ActivityWebView.startActivityForReadNormal(context,skipUrl, bookId, source,openMulu);
                }
            }

    }





}
