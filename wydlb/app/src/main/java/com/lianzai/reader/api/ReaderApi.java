/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lianzai.reader.api;


import android.support.v4.util.ArrayMap;

import com.google.gson.JsonObject;
import com.lianzai.reader.api.support.GsonConverterFactory;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountBalanceList;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.AuthRealNamesSubmitBean;
import com.lianzai.reader.bean.AuthStatusResponse;
import com.lianzai.reader.bean.BannerResponseBean;
import com.lianzai.reader.bean.BarDetailListResponse;
import com.lianzai.reader.bean.BarInfoBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BindAccountListResponse;
import com.lianzai.reader.bean.BookCatalogResponse;
import com.lianzai.reader.bean.BookCategoryBean;
import com.lianzai.reader.bean.BookCategoryListResponse;
import com.lianzai.reader.bean.BookChapterDetailBean;
import com.lianzai.reader.bean.BookListCategoryBean;
import com.lianzai.reader.bean.BookShopBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.CaptchaBean;
import com.lianzai.reader.bean.ChapterInfoResponse;
import com.lianzai.reader.bean.ChasingBookListBean;
import com.lianzai.reader.bean.CheckRedPacketBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.GateWayResponse;
import com.lianzai.reader.bean.GetGoldDrillBean;
import com.lianzai.reader.bean.GetOrderByOrderNo;
import com.lianzai.reader.bean.GetRewaredPoolDetailBean;
import com.lianzai.reader.bean.GetTaskCenterShowDictByKeywordBean;
import com.lianzai.reader.bean.GetTaskIsCompleteByUserIdBean;
import com.lianzai.reader.bean.HongBaoDetailBean;
import com.lianzai.reader.bean.HotSearchBean;
import com.lianzai.reader.bean.InsideChapterBean;
import com.lianzai.reader.bean.LuckInfoBean;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.bean.LuckLaunchBean;
import com.lianzai.reader.bean.MessageBean;
import com.lianzai.reader.bean.MinerGameResponse;
import com.lianzai.reader.bean.MiningBookListBean;
import com.lianzai.reader.bean.MyTokensResponse;
import com.lianzai.reader.bean.OtherAccountBean;
import com.lianzai.reader.bean.RealNameAuthBean;
import com.lianzai.reader.bean.ReceiveBean;
import com.lianzai.reader.bean.RewardBooksBean;
import com.lianzai.reader.bean.SaveReceiptAccountResponse;
import com.lianzai.reader.bean.SearchNovelsBean;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.bean.UnBindWxBean;
import com.lianzai.reader.bean.UnReadCountBean;
import com.lianzai.reader.bean.UploadFileBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.bean.UserLockListResponse;
import com.lianzai.reader.bean.UserRelaListBean;
import com.lianzai.reader.bean.WalletAccountBean;
import com.lianzai.reader.bean.WalletAddressBean;
import com.lianzai.reader.bean.WalletBindAccountResponse;
import com.lianzai.reader.bean.WalletConditionsBean;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.bean.WalletListSumResponse;
import com.lianzai.reader.bean.WxLoginResponse;

import java.io.File;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ReaderApi {

    public static ReaderApi instance;

    private ReaderBookApiService service;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public ReaderApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(ReaderBookApiService.class);
    }

    public static ReaderApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new ReaderApi(okHttpClient);
        return instance;
    }

    public Single<AccountTokenBean> normalLogin(ArrayMap<String,Object> maps) {
        return service.login(maps);
    }
    public Single<AccountTokenBean> fastLogin(ArrayMap<String,Object> maps) {
        return service.fastLogin(maps);
    }


    public Single<AccountTokenBean> register(ArrayMap<String,Object> maps) {
        return service.register(maps);
    }

    public Single<CaptchaBean> getCaptcha(long time) {
        return service.getCaptcha(time);
    }

    public Single<SendSmsResponse> sendSmsCode(ArrayMap<String,Object> maps) {
        return service.sendVerificationCode(maps);
    }

    public Single<SendSmsResponse> sendBindThirdCode(ArrayMap<String,Object> maps) {
        return service.sendBindThirdCode(maps);
    }

    public Single<BaseBean> validateSms(ArrayMap<String,String> maps) {
        return service.validateSms(maps);
    }

    public Single<BaseBean> resetSms(ArrayMap<String,String> maps) {
        return service.resetSms(maps);
    }

    public Single<BaseBean> findAccountPassword(ArrayMap<String,Object> maps) {
        return service.updatePwd(maps);
    }

    public Single<BaseBean> resetPayPwd(ArrayMap<String,Object> maps) {
        return service.resetPayPwd(maps);
    }

    public Single<AccountDetailBean> getAccountDetail() {
        return service.getUserInfo();
    }


    public Single<RealNameAuthBean> authRealNameCheck(ArrayMap<String,String> maps) {
        return service.authRealNameCheck(maps);
    }


    public Single<BaseBean> setPaymentPassword(ArrayMap<String,String> maps) {
        return service.setPaymentPassword(maps);
    }
    public Single<BaseBean> resetPaymentPassword(ArrayMap<String,Object> maps) {
        return service.resetPaymentPassword(maps);
    }

    public Single<BaseBean> resetLoginPassword(ArrayMap<String,Object> maps) {
        return service.resetPwd(maps);
    }

    public Single<HongBaoDetailBean> queryHongBaoDetail(ArrayMap<String,String> maps) {
        return service.queryHongBaoDetail(maps);
    }


    public Single<CheckRedPacketBean> checkRedPacket(String no) {
        return service.checkRedPacket(no);
    }


    public Single<WalletAddressBean> queryWalletAddress(ArrayMap<String,String> maps) {
        return service.queryWalletAddress(maps);
    }

    public Single<UploadFileBean> uploadFile(File file) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        return service.uploadFile(requestBody);
    }

    public Single<AuthRealNamesSubmitBean> authRealNameSubmit(ArrayMap<String,String> maps) {
        return service.authRealNameSubmit(maps);
    }


    public Single<OtherAccountBean> queryAccountByIdOrPhone(ArrayMap<String,String>maps) {
        return service.queryAccountByIdOrPhone(maps);
    }

    public Single<BaseBean> confirmSecured(ArrayMap<String,String>maps) {
        return service.confirmSecured(maps);
    }


    public Single<BaseBean> confirmTransfer(ArrayMap<String,String>maps) {
        return service.confirmTransfer(maps);
    }

    public Single<BaseBean> cancleSecured(ArrayMap<String,String>maps) {
        return service.cancleSecured(maps);
    }

    public Single<BaseBean> createArbitration(ArrayMap<String,String>maps) {
        return service.createArbitration(maps);
    }

    public Single<BaseBean> detailArbitration(ArrayMap<String,String>maps) {
        return service.detailArbitration(maps);
    }

    public Single<BaseBean> listArbitration(ArrayMap<String,String>maps) {
        return service.listArbitration(maps);
    }

    public Single<MessageBean> listMessageAndNotice(ArrayMap<String,Object> maps) {
        return service.listMessageAndNotice(maps);
    }

    public Single<UnReadCountBean> getUnReadCount() {
        return service.getUnReadCount();
    }


    public Single<BaseBean> updateMessageStatus(ArrayMap<String,String>maps,boolean isPrivate) {
        return isPrivate?service.updatePrivateMessageStatus(maps):service.updatePublicMessageStatus(maps);
    }


    public Single<BaseBean> logout() {
        return service.logout();
    }


    public Single<BookShopBean> bookShopList() {
        return service.bookShopList();
    }

    public Single<BannerResponseBean> bookShopBanner() {
        return service.bookShopBanner();
    }

    public Single<ChasingBookListBean> bookChasingList(ArrayMap<String,String>maps) {
        return service.bookChasingList(maps);
    }

    public Single<BarInfoBean> bookBarDetail(ArrayMap<String,String>maps) {
        return service.bookBarDetail(maps);
    }

    public Single<BookCatalogResponse> bookDirectoryList(String id) {
        return service.bookDirectoryList(id);
    }
    public Single<BookCatalogResponse> bookDirectoryListBySource(String id,String source,boolean isForceRefresh) {
        return service.bookDirectoryListBySource(id,source,isForceRefresh);
    }

//    public Single<BookChapterDetailBean> bookChapterDetail(ArrayMap<String,String>maps) {
//        return service.bookChapterDetail(maps);
//    }

    public Single<BookCategoryBean> bookBookCategoryList() {
        return service.bookBookCategoryList();
    }

    public Single<BarDetailListResponse> homeRecommendList(ArrayMap<String,Object> maps) {
        return service.homeRecommendList(maps);
    }

    public Single<BarDetailListResponse> attentionMineList(ArrayMap<String,Object> maps) {
        return service.attentionMineList(maps);
    }

    public Single<BookListCategoryBean> bookListByCategory(ArrayMap<String,Object> maps) {
        return service.bookListByCategory(maps);
    }

    public Single<SearchNovelsBean> searchNovelsByKeyWord(ArrayMap<String,Object> maps,String keyword) {
        return service.searchNovelsByKeyWord(keyword,maps);
    }

    public Single<BaseBean> barFollow(String id) {
        return service.barFollow(id);
    }

    public Single<BaseBean> barCancelFollow(String id) {
        return service.barCancelFollow(id);
    }

    public Single<JinZuanChargeBean> postPublish(String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return service.postPublish(body);
    }

    public Single<HotSearchBean> bookHotSearch() {
        return service.bookHotSearch();
    }


    public Single<MiningBookListBean> miningBookList(ArrayMap<String,Object> maps) {
        return service.miningBookList(maps);
    }

    public Single<RewardBooksBean> rewardBookList(ArrayMap<String,Object> maps) {
        return service.rewardBookList(maps);
    }

    public Single<MinerGameResponse> miningHome(ArrayMap<String,Object> maps) {
        return service.miningHome(maps);
    }

    public Single<WxLoginResponse> wxLogin(ArrayMap<String,Object> maps) {
        return service.wxLogin(maps);
    }

    public Single<AccountTokenBean> bindPhone(ArrayMap<String,Object> maps) {
        return service.bindThird(maps);
    }

    public Single<BaseBean> bindMobile(ArrayMap<String,Object> maps) {
        return service.bindMobile(maps);
    }

    public Single<UnBindWxBean> unBindAccount(ArrayMap<String,Object> maps) {
        return service.unBindAccount(maps);
    }

    public Single<BindAccountListResponse> bindAccountList() {
        return service.bindAccountList();
    }

    public Single<ChapterInfoResponse> getChapterInfo(ArrayMap<String,Object> maps) {
        return service.getChapterInfo(maps);
    }

    public Single<ResponseBody> downloadFile(String path) {
        return service.downloadFile(path);
    }

    public Single<BaseBean> deletePost(String postId) {
        return service.deletePost(postId);
    }

    public Single<BaseBean> deleteReply(String replyId) {
        return service.deleteReply(replyId);
    }


    public Single<MyTokensResponse> getTokens() {
        return service.getTokens();
    }

    public Single<BaseBean> authSave(ArrayMap<String,Object> maps) {
        return service.authSave(maps);
    }

    public Single<BaseBean> bindAccount(ArrayMap<String,Object> maps) {
        return service.bindAccount(maps);
    }

    public Single<AuthStatusResponse> getAuthInfo() {
        return service.getAuthInfo();
    }


    public Single<AccountBalance> getAccountBalance( ArrayMap<String,Object>maps) {
        return service.getAccountBalance(maps);
    }

    public Single<AccountBalanceList> getAccountBalanceList(ArrayMap<String,Object>maps) {
        return service.getBalanceInfo(maps);
    }

    public Single<WalletConditionsBean> getBalanceInOutType() {
        return service.getBalanceInOutType();
    }

    public Single<WalletDetailListResponse> getBalancePage (ArrayMap<String,Object>maps) {
        return service.getBalancePage(maps);
    }

    public Single<WalletListSumResponse> getUserBalanceSum (ArrayMap<String,Object>maps) {
        return service.getUserBalanceSum(maps);
    }

    public Single<GateWayResponse> gateway(ArrayMap maps) {
        return service.gateway(maps);
    }

    public Single<BaseBean> withdraw(String jsonParam) {
        RequestBody body = RequestBody.create(JSON, jsonParam);
        return service.withdraw(body);
    }

    public Single<BaseBean> exchange(String jsonParam) {
        RequestBody body = RequestBody.create(JSON, jsonParam);
        return service.exchange(body);
    }

    public Single<WalletAccountBean> getWalletAccount () {
        return service.getWalletAccount();
    }

    public Single<WalletBindAccountResponse> thirdBindReceiptUserAccount (ArrayMap maps) {
        return service.getReceiptUserAccount(maps);
    }

    public Single<SaveReceiptAccountResponse> saveReceiptAccount (ArrayMap maps) {
        return service.saveReceiptAccount(maps);
    }

    public Single<UserLockListResponse> getUserLockList (ArrayMap maps) {
        return service.getUserLockList(maps);
    }

    public Single<ResponseBody> outsideBookChapterDetail(String url) {
        return service.outsideBookChapterDetail(url);
    }

    public Single<InsideChapterBean> insideBookChapterDetail (ArrayMap maps) {
        return service.insideBookChapterDetail(maps);
    }

    public Single<BaseBean> lzTransferwhiteRabbit(String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return service.lzTransferwhiteRabbit(body);
    }


    //赏金池相关增加接口
    //获取赏金池页面详细信息接口
    public Single<GetRewaredPoolDetailBean> getRewaredPoolDetail(ArrayMap<String,Object> maps) {
        return service.getRewaredPoolDetail(maps);
    }
    //获取赏金池订单状态接口
    public Single<GetOrderByOrderNo> getOrderByOrderNo(ArrayMap<String,Object> maps) {
        return service.getOrderByOrderNo(maps);
    }
    //支付接口
    public Single<GateWayResponse> placeRewardOrder(ArrayMap<String,Object> maps) {
        return service.placeRewardOrder(maps);
    }

    public Single<UserRelaListBean> getUserAwakenRelaList (ArrayMap<String, Object> params) {
        return service.getUserAwakenRelaList(params);
    }

    public Single<UserRelaListBean> getUserRelaList (ArrayMap<String, Object> params) {
        return service.getUserRelaList(params);
    }

    public Single<ReceiveBean> receive (ArrayMap<String,Object> maps) {
        return service.receive(maps);
    }

    public Single<BaseBean> dailyCheck (ArrayMap<String, Object> params) {
        return service.dailyCheck(params);
    }

    public Single<TaskCenterBean> todayTaskList () {
        return service.todayTaskList();
    }

    public Single<GetTaskIsCompleteByUserIdBean> getTaskIsCompleteByUserId () {
        return service.getTaskIsCompleteByUserId();
    }


    public Single<BookStoreResponse> requestBookStore (ArrayMap<String, Object> params) {
        return service.requestBookStore(params);
    }

    //获取发起拼手气信息
    public Single<LuckLaunchBean> luckLaunch() {
        return service.luckLaunch();
    }

    //发起拼手气
    public Single<BaseBean> luckPublish (String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return service.luckPublish(body);
    }

    //获取当前游戏信息
    public Single<LuckInfoBean> luckInfo (ArrayMap<String, Object> params) {
        return service.luckInfo(params);
    }

    //获取当前游戏人员信息
    public Single<LuckInfoFollowBean> luckInfoFollow (ArrayMap<String, Object> params) {
        return service.luckInfoFollow(params);
    }

    //参与拼手气
    public Single<BaseBean> luckJoin (String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return service.luckJoin(body);
    }

    public Single<BookCategoryListResponse> bookCategoryIntelligent (String bookId,ArrayMap<String, Object> params) {
        return service.bookCategoryIntelligent(bookId,params);
    }

    //获取关注列表
    public Single<UserAttentionBean> listPageUserAttention (ArrayMap<String, Object> params) {
        return service.listPageUserAttention(params);
    }

    //获取圈子成员
    public Single<CirclePersonBean> listPageCirclePerson (ArrayMap<String, Object> params) {
        return service.listPageCirclePerson(params);
    }

    public Single<BookCategoryListResponse> bookCatalogRecognition (String bookId,ArrayMap<String, Object> params) {
        return service.bookCatalogRecognition(bookId,params);
    }

    //获取关注列表
    public Single<AttentionBean> attentionUnattention (ArrayMap<String, Object> params) {
        return service.attentionUnattention(params);
    }

    //获取共享版权成员
    public Single<UserAttentionBean> gxbqCirclePerson (ArrayMap<String, Object> params) {
        return service.gxbqCirclePerson(params);
    }

}
