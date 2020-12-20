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
package com.wydlb.first.api;


import androidx.collection.ArrayMap;

import com.wydlb.first.base.JinZuanChargeBean;
import com.wydlb.first.bean.AccountBalance;
import com.wydlb.first.bean.AccountBalanceList;
import com.wydlb.first.bean.AccountDetailBean;
import com.wydlb.first.bean.AccountTokenBean;
import com.wydlb.first.bean.AttentionBean;
import com.wydlb.first.bean.AuthRealNamesSubmitBean;
import com.wydlb.first.bean.AuthStatusResponse;
import com.wydlb.first.bean.BannerResponseBean;
import com.wydlb.first.bean.BarDetailListResponse;
import com.wydlb.first.bean.BarInfoBean;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.bean.BindAccountListResponse;
import com.wydlb.first.bean.BookCatalogResponse;
import com.wydlb.first.bean.BookCategoryBean;
import com.wydlb.first.bean.BookCategoryListResponse;
import com.wydlb.first.bean.BookListCategoryBean;
import com.wydlb.first.bean.BookShopBean;
import com.wydlb.first.bean.BookStoreResponse;
import com.wydlb.first.bean.CaptchaBean;
import com.wydlb.first.bean.ChapterInfoResponse;
import com.wydlb.first.bean.ChasingBookListBean;
import com.wydlb.first.bean.CheckRedPacketBean;
import com.wydlb.first.bean.CirclePersonBean;
import com.wydlb.first.bean.GateWayResponse;
import com.wydlb.first.bean.GetOrderByOrderNo;
import com.wydlb.first.bean.GetRewaredPoolDetailBean;
import com.wydlb.first.bean.GetTaskIsCompleteByUserIdBean;
import com.wydlb.first.bean.HongBaoDetailBean;
import com.wydlb.first.bean.HotSearchBean;
import com.wydlb.first.bean.InsideChapterBean;
import com.wydlb.first.bean.LuckInfoBean;
import com.wydlb.first.bean.LuckInfoFollowBean;
import com.wydlb.first.bean.LuckLaunchBean;
import com.wydlb.first.bean.MessageBean;
import com.wydlb.first.bean.MinerGameResponse;
import com.wydlb.first.bean.MiningBookListBean;
import com.wydlb.first.bean.MyTokensResponse;
import com.wydlb.first.bean.OtherAccountBean;
import com.wydlb.first.bean.RealNameAuthBean;
import com.wydlb.first.bean.ReceiveBean;
import com.wydlb.first.bean.RewardBooksBean;
import com.wydlb.first.bean.SaveReceiptAccountResponse;
import com.wydlb.first.bean.SearchNovelsBean;
import com.wydlb.first.bean.SendSmsResponse;
import com.wydlb.first.bean.TaskCenterBean;
import com.wydlb.first.bean.UnBindWxBean;
import com.wydlb.first.bean.UnReadCountBean;
import com.wydlb.first.bean.UploadFileBean;
import com.wydlb.first.bean.UserAttentionBean;
import com.wydlb.first.bean.UserLockListResponse;
import com.wydlb.first.bean.UserRelaListBean;
import com.wydlb.first.bean.WalletAccountBean;
import com.wydlb.first.bean.WalletAddressBean;
import com.wydlb.first.bean.WalletBindAccountResponse;
import com.wydlb.first.bean.WalletConditionsBean;
import com.wydlb.first.bean.WalletDetailListResponse;
import com.wydlb.first.bean.WalletListSumResponse;
import com.wydlb.first.bean.WeiXinAccessTokenResponse;
import com.wydlb.first.bean.WxLoginResponse;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ReaderBookApiService {
    @FormUrlEncoded
    @POST("api/auth/login")
    Single<AccountTokenBean> normalLogin(@FieldMap ArrayMap<String,Object> maps);

//    @FormUrlEncoded
//    @POST("api/auth/register")
//    Single<AccountTokenBean> register(@FieldMap ArrayMap<String,String>maps);


    @GET("api/captcha/{timestamp}")
    Single<CaptchaBean> getCaptcha(@Path("timestamp") long time);

    @FormUrlEncoded
    @POST("api/sms")
    Single<BaseBean> sendSmsCode(@FieldMap ArrayMap<String,String>maps);

    @GET("api/validate/sms")
    Single<BaseBean> validateSms(@QueryMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("api/reset/sms")
    Single<BaseBean> resetSms(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("api/auth/pwd/forget")
    Single<BaseBean> findAccountPassword(@FieldMap ArrayMap<String,String>maps);

    @GET("pay_api/account/detail")
    Single<AccountDetailBean> getAccountDetail();




    @FormUrlEncoded
    @POST("api/ucenter/auth/check")
    Single<RealNameAuthBean> authRealNameCheck(@FieldMap ArrayMap<String,String>maps);


    @FormUrlEncoded
    @POST("pay_api/account/password")
    Single<BaseBean> setPaymentPassword(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("pay_api/account/password/reset")
    Single<BaseBean> resetPaymentPassword(@FieldMap ArrayMap<String,Object>maps);

    // TODO: 2017/10/23 设置登录密码接口--
    @FormUrlEncoded
    @POST("pay_api/account/password")
    Single<BaseBean> editLoginPassword(@FieldMap ArrayMap<String,String>maps);

    @GET("pay_api/red/check/{no}")
    Single<CheckRedPacketBean> checkRedPacket(@Path("no") String no);

    @GET("pay_api/red/head")
    Single<HongBaoDetailBean> queryHongBaoDetail(@QueryMap ArrayMap<String,String>maps);


    @GET("pay_api/address/create")
    Single<WalletAddressBean> queryWalletAddress(@QueryMap ArrayMap<String,String>maps);

    @POST("api/img/upload/authentication")
    Single<UploadFileBean> uploadFile(@Body RequestBody Body);


    @FormUrlEncoded
    @POST("api/ucenter/auth")
    Single<AuthRealNamesSubmitBean> authRealNameSubmit(@FieldMap ArrayMap<String,String>maps);




    @FormUrlEncoded
    @POST("api/auth/pwd/reset")
    Single<BaseBean> resetLoginPassword(@FieldMap ArrayMap<String,String>maps);

    @GET("pay_api/account/check")
    Single<OtherAccountBean> queryAccountByIdOrPhone(@QueryMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("pay_api/arb/create")
    Single<BaseBean> createArbitration(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("pay_api/secured/cancle")
    Single<BaseBean> cancleSecured(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("pay_api/secured/confirm")
    Single<BaseBean> confirmSecured(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("pay_api/secured/pay")//确认转账过了
    Single<BaseBean> confirmTransfer(@FieldMap ArrayMap<String,String>maps);

    @GET("pay_api/arb/form")
    Single<BaseBean> detailArbitration(@QueryMap ArrayMap<String,String>maps);

    @GET("pay_api/arb/list")
    Single<BaseBean> listArbitration(@QueryMap ArrayMap<String,String>maps);

    @GET("api/inform/lists")
    Single<MessageBean> listMessageAndNotice(@QueryMap ArrayMap<String,Object>maps);

    @GET("api/inform/unreadcount?project=1")
    Single<UnReadCountBean> getUnReadCount();

    @FormUrlEncoded
    @POST("api/inform/privateRead")//更新私信消息状态
    Single<BaseBean> updatePrivateMessageStatus(@FieldMap ArrayMap<String,String>maps);

    @FormUrlEncoded
    @POST("api/inform/publicRead")//更新公告消息状态
    Single<BaseBean> updatePublicMessageStatus(@FieldMap ArrayMap<String,String>maps);


    //发现-书城
    @GET("api/v2_verity_success/bar/index")
    Single<BookShopBean> bookShopList();

    //发现-书城-banner
    @GET("api/home/banner?id=7")
    Single<BannerResponseBean> bookShopBanner();


    //追书
    @GET("api/follow/list")
    Single<ChasingBookListBean> bookChasingList(@QueryMap ArrayMap<String,String>maps);


    //分类书籍列表
    @GET("api/v2_verity_success/genres/books")
    Single<BookListCategoryBean> bookListByCategory(@QueryMap ArrayMap<String,Object>maps);


    //吧详情
    @GET("api/v3/bar/info/")
    Single<BarInfoBean> bookBarDetail(@QueryMap ArrayMap<String,String>maps);

    //书籍目录
    @GET("/book/read/catalog/{bookId}")
    Single<BookCatalogResponse> bookDirectoryList(@Path("bookId") String bookId);

    //书籍目录
    @GET("/book/read/catalog/{bookId}")
    Single<BookCatalogResponse> bookDirectoryListBySource(@Path("bookId")String bookId,@Query("source") String source,@Query("isUpdate") boolean isForceRefresh);

//    //章节详情
//    @GET("/book/read/content")
//    Single<BookChapterDetailBean> bookChapterDetail(@QueryMap ArrayMap<String,String>maps);

//    @GET("api/v2_verity_success/book/books")
//    Single<BookChapterDetailBean> bookChapterDetail(@QueryMap ArrayMap<String,String>maps);

    @GET("api/v2_verity_success/genres")
    Single<BookCategoryBean> bookBookCategoryList();


    //首页推荐
    @GET("api/v3/post/recommends")
    Single<BarDetailListResponse> homeRecommendList(@QueryMap ArrayMap<String,Object> maps);

    //我的关注
    @GET("api/v3/post/follows")
    Single<BarDetailListResponse> attentionMineList(@QueryMap ArrayMap<String,Object> maps);

    @PUT("/platforms/{id}/UnConcern")//取消关注贴吧
    Single<BaseBean> barCancelFollow(@Path("id")String id);

    @PUT("/platforms/{id}/Concern")//关注贴吧
    Single<BaseBean> barFollow(@Path("id")String id);


    //搜索书籍
    @GET("/search/{keyword}")
    Single<SearchNovelsBean> searchNovelsByKeyWord(@Path("keyword")String keyword,@QueryMap ArrayMap<String,Object> maps);

    @PUT("/circles/dynamicRelease")//发帖
    Single<JinZuanChargeBean> postPublish(@Body RequestBody Body);


    //热门搜索
    @GET("api/novels/hot")
    Single<HotSearchBean> bookHotSearch();


    //参阅挖矿列表的书籍
    @GET("api/mining/list")
    Single<MiningBookListBean> miningBookList(@QueryMap ArrayMap<String,Object> maps);


    @FormUrlEncoded
    @POST("/api/reward/list")//打赏的书籍列表
    Single<RewardBooksBean> rewardBookList(@FieldMap ArrayMap<String,Object>maps);


    //赏金池接口
    @GET("pay_api/recharge/bounty")
    Single<MiningBookListBean> rechargeBouNty(@QueryMap ArrayMap<String,Object> maps);

    //挖矿
    @GET("/api/mining/account/detail")
    Single<MinerGameResponse> miningHome(@QueryMap ArrayMap<String,Object> maps);



    @FormUrlEncoded
    @POST("/api/auth/phone/set")//绑定手机
    Single<AccountTokenBean> bindPhone(@FieldMap ArrayMap<String,Object>maps);



    //绑定账号列表
    @POST("/user/third/getBindInfo")
    Single<BindAccountListResponse> bindAccountList();

    //章节信息查询
    @GET("/api/v3/post/replyinfo")
    Single<ChapterInfoResponse> getChapterInfo(@QueryMap ArrayMap<String,Object> maps);


    @DELETE("/book/bar/post/{postId}")//删帖
    Single<BaseBean> deletePost(@Path("postId") String id);

    @DELETE("/book/bar/reply/{replyId}")//删回复
    Single<BaseBean> deleteReply(@Path("replyId") String id);


    @GET
    Single<ResponseBody> downloadFile(@Url String path);



    /**---------------新版用户接口-------------------*/


    @POST("/security/genTokens")
    Single<MyTokensResponse> getTokens();

    @POST("/user/getUserInfo")
    Single<AccountDetailBean> getUserInfo();

    @FormUrlEncoded
    @POST("/api/user/login/minigame")
    Single<AccountTokenBean> login(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/fastLogin")
    Single<AccountTokenBean> fastLogin(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/loginout")
    Single<BaseBean> loginOut(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/register")
    Single<AccountTokenBean> register(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/resetPwd")
    Single<BaseBean> resetPwd(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/updatePwd")
    Single<BaseBean> updatePwd(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/updatePayPwd")
    Single<BaseBean> resetPayPwd(@FieldMap ArrayMap<String,Object>maps);


    @FormUrlEncoded
    @POST("/user/sendVerificationCode")
    Single<SendSmsResponse> sendVerificationCode(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/sendBindThirdCode")
    Single<SendSmsResponse> sendBindThirdCode(@FieldMap ArrayMap<String,Object>maps);


    @FormUrlEncoded
    @POST("/user/updateMemberInfo")
    Single<BaseBean> updateMemberInfo(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/updatePic")
    Single<BaseBean> updatePic(@FieldMap ArrayMap<String,Object>maps);


    @FormUrlEncoded
    @POST("/user/third/bindThird")
    Single<AccountTokenBean> bindThird(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/bindMobile")
    Single<BaseBean> bindMobile(@FieldMap ArrayMap<String,Object>maps);

    @POST("/user/loginout")//登出
    Single<BaseBean> logout();

//    @POST("/user/third/thirdLogin")//微信登录
    @FormUrlEncoded
    @POST("/user/third/thirdApploginOrReg")//微信登录,于2.5版本换成此新接口
    Single<WxLoginResponse> wxLogin(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/third/unBind")//解绑
    Single<UnBindWxBean> unBindAccount(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/auth/save")//实名认证
    Single<BaseBean> authSave(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/third/bindAccount")//绑定账户-登录情况下
    Single<BaseBean> bindAccount(@FieldMap ArrayMap<String,Object>maps);


    @GET("/sns/oauth2/access_token")
    Call<WeiXinAccessTokenResponse> getWeixinAccessToken(@QueryMap ArrayMap<String,Object>maps);

    @GET("/sns/userinfo")
    Call<WeiXinAccessTokenResponse> getWeixinInfo(@QueryMap ArrayMap<String,Object>maps);


    @POST("/user/auth/getAuthInfo")//认证信息
    Single<AuthStatusResponse> getAuthInfo();


    @POST("/user/balance/getBalance")//账户余额
    Single<AccountBalance> getAccountBalance(@QueryMap ArrayMap<String,Object>maps);

    @POST("/user/balance/getBalanceInfo")//账户余额
    Single<AccountBalanceList> getBalanceInfo(@QueryMap ArrayMap<String,Object>maps);


    //获取所有余额收入支出类型，用户查询列表时查询条件使用
    @GET("/user/balance/getBalanceInOutType")
    Single<WalletConditionsBean> getBalanceInOutType();

    @FormUrlEncoded
    @POST("/user/balance/getBalancePage")//余额列表
    Single<WalletDetailListResponse> getBalancePage(@FieldMap ArrayMap<String,Object>maps);

    @FormUrlEncoded
    @POST("/user/balance/getUserBalanceSum")//获取用户每月收支总和
    Single<WalletListSumResponse> getUserBalanceSum(@FieldMap ArrayMap<String,Object>maps);

    @POST("/user/balance/getAccount")//获取用户收款账号
    Single<WalletAccountBean> getWalletAccount();


    @FormUrlEncoded
    @POST("/user/third/getReceiptUserAccount")//绑定收款账号授权之后获取第三方信息
    Single<WalletBindAccountResponse> getReceiptUserAccount(@FieldMap ArrayMap<String,Object>maps);


    @FormUrlEncoded
    @POST("/user/balance/saveReceiptAccount")//保存收款账号
    Single<SaveReceiptAccountResponse> saveReceiptAccount(@FieldMap ArrayMap<String,Object> maps);

    @FormUrlEncoded
    @POST("/user/balance/getUserLockList")//获取锁定明细列表
    Single<UserLockListResponse> getUserLockList(@FieldMap ArrayMap<String,Object>maps);


    //--------支付相关

    @FormUrlEncoded
    @POST("/api/gateway")//支付接口
    Single<GateWayResponse> gateway(@FieldMap ArrayMap<String,Object>maps);

    @POST("/api/exchange")//兑换接口
    Single<BaseBean> exchange(@Body RequestBody body);

    @POST("/api/withdraw")//提现接口
    Single<BaseBean> withdraw(@Body RequestBody body);

    //外站章节详情
    @GET
    Single<ResponseBody> outsideBookChapterDetail(@Url String url );

    //内站章节详情
    @GET("/book/shelf/readBook")
    Single<InsideChapterBean> insideBookChapterDetail(@QueryMap ArrayMap<String,Object>maps);


    //赏金池相关增加接口
    //获取赏金池页面详细信息接口
    @GET("/user/balance/getRewaredPoolDetail")
    Single<GetRewaredPoolDetailBean> getRewaredPoolDetail(@QueryMap ArrayMap<String,Object>maps);
    //获取赏金池订单状态接口
    @GET("/user/balance/getOrderByOrderNo")
    Single<GetOrderByOrderNo> getOrderByOrderNo(@QueryMap ArrayMap<String,Object>maps);
    //支付接口
    @FormUrlEncoded
    @POST("/api/placeRewardOrder")
    Single<GateWayResponse> placeRewardOrder(@FieldMap ArrayMap<String,Object>maps);


    @POST("/api/lzTransferwhiteRabbit")//连载转账到白兔子
    Single<BaseBean> lzTransferwhiteRabbit(@Body RequestBody body);

    //待唤醒的好友列表
    @GET("/account/getUserAwakenRelaList")
    Single<UserRelaListBean> getUserAwakenRelaList(@QueryMap ArrayMap<String, Object> params);

    //好友列表
    @GET("/account/getUserRelaList")
    Single<UserRelaListBean> getUserRelaList(@QueryMap ArrayMap<String, Object> params);

    //领取奖励
    @FormUrlEncoded
    @POST("/account/receive")
    Single<ReceiveBean> receive(@FieldMap ArrayMap<String,Object>maps);

    //签到
    @FormUrlEncoded
    @POST("/invitationShare/mineSignIn")
    Single<BaseBean> dailyCheck(@FieldMap ArrayMap<String,Object>maps);

    //每日任务列表
    @GET("/taskCenter/v2.4.1/index")
    Single<TaskCenterBean> todayTaskList();

    //新手大礼包奖励明细
    @GET("/account/getTaskIsCompleteByUserId")
    Single<GetTaskIsCompleteByUserIdBean> getTaskIsCompleteByUserId();

    @GET("/book/shelf/refresh")
    Single<BookStoreResponse> requestBookStore(@QueryMap ArrayMap<String, Object> params);

    //获取发起拼手气信息
    @GET("/games/luck/launch")
    Single<LuckLaunchBean> luckLaunch();

    //发起拼手气
    @POST("/games/luck/publish")
    Single<BaseBean> luckPublish(@Body RequestBody body);

    //获取当前游戏信息
    @GET("/games/luck/info")
    Single<LuckInfoBean> luckInfo(@QueryMap ArrayMap<String, Object> params);

    //获取当前游戏人员信息
    @GET("/games/luck/info/follow")
    Single<LuckInfoFollowBean> luckInfoFollow(@QueryMap ArrayMap<String, Object> params);

    //参与拼手气
    @POST("/games/luck/join")
    Single<BaseBean> luckJoin(@Body RequestBody body);

    //智能目录列表
    @GET("/book/read/catalog/intelligent/{id}")
    Single<BookCategoryListResponse> bookCategoryIntelligent(@Path("id")String bookId, @QueryMap ArrayMap<String,Object>maps);

    //获取关注列表
    @GET("/userAttention/listPageUserAttention")
    Single<UserAttentionBean> listPageUserAttention(@QueryMap ArrayMap<String, Object> params);

    //获取圈子成员
    @GET("/circleManage/memberList")
    Single<CirclePersonBean> listPageCirclePerson(@QueryMap ArrayMap<String, Object> params);

    //获取共享版权成员
    @GET("/copyrights/people/list")
    Single<UserAttentionBean> gxbqCirclePerson(@QueryMap ArrayMap<String, Object> params);



    //url识别书籍目录列表,新接口，智能目录内站外站。
    @GET("/book/read/catalog/advance/{id}")
    Single<BookCategoryListResponse> bookCatalogRecognition(@Path("id")String bookId, @QueryMap ArrayMap<String,Object>maps);

    //用户关注取关
    @FormUrlEncoded
    @POST("/userAttention/userAttention")
    Single<AttentionBean> attentionUnattention(@FieldMap ArrayMap<String,Object>maps);

}