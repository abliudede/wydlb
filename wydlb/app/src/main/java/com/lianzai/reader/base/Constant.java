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
package com.lianzai.reader.base;

import com.lianzai.reader.utils.RxFileTool;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constant {

    //基础环境配置
    public static int appMode = AppMode.BETA;//是否是生产环境

    //接口地址配置
    public static String API_BASE_URL = getAppBaseUrl();//测试 http://lz-api.bendixing.net dev:
    public static String GATEWAY_API_BASE_URL = getGatewayApiBaseUrl();//测试 http://lz-pay.bendixing.net dev http://192.168.31.84:10001

    //链接地址配置
    public static String H5_BASE_URL = appMode == AppMode.RELEASE ? "https://h5.lianzai.com" : "https://h5.test.lianzai.com";
    public static String H5_HELP_BASE_URL = appMode == AppMode.RELEASE ? "https://help.lianzai.com" : "http://help2.bendixing.net";

    //存储文件夹配置
    public static String APP_FOLDER = "/LianzaiReader";
    public static String IMAGES_FOLDER = "/images";
    public static String APK_FOLDER = "/apk";
    public static String BOOK_CACHE_FOLDER = "/book_cache";
    public static String BOOK_WIFI_CACHE_FOLDER = "/book_wifi_cache";

    //又拍云配置
    public static final String UPAI_BUCKET = "lianzai-static";
    public static final String UPAI_OPERATER = "lianzaiupyun8888";
    public static final String UPAI_PASSWORD = "XuanChain@#";
    public static final String UPAI_HTTP_HEADER = "https://static.lianzai.com/";

    //转码配置文件下载地址
    public static String BOOK_SOURCE_SETTINGS_URL = appMode == AppMode.RELEASE ? (Constant.UPAI_HTTP_HEADER + "json/iOSReadSettings.json?t=" + System.currentTimeMillis()) : (Constant.UPAI_HTTP_HEADER + "json/iOSReadSettingsTest.json?t=" + System.currentTimeMillis());


    public static String CURRENT_LANGUAGE_KEY = "language";

    public static final String ACCOUNT_TOKEN = "token2";
    public static final String ACCOUNT_TOKEN3 = "token3";

    public static final String SMS_TIME = "smsTime";

    public static final Long SMS_TIME_MAX = 60000l;

    public static final String LOGIN_ID = "loginId";

    public static final String ACCOUNT_CACHE = "account25";

    public static final String SHOW_SEARCH_BOOK_GUIDE = "showSearchBooKGuide";//搜索图层引导

    public static final String BAIDUAPPID = "15549933";
    public static final String BAIDUAPPKEY = "RvGl93RnDvOnjepjb4Hxw6wl";
    public static final String BAIDUAPPSECRECT = "RGravRZjOzNBmnwoUpUE4G3K8tGL7KNh";


//    腾讯广告联盟，appid和postid
    public static final String TENCENTAPPID = "1108059246";
    public static final String TENCENTPOSTID = "4010740807476879";//开屏广告位
    public static final String TENCENTPOSTID2 = "8010056621389834";//信息流广告位
    public static final String TENCENTPOSTID3 = "2040955893723402";//信息流广告位


    //  连载appid  public static final String APP_ID = "wxb80774036b502d7a";
    //小v圈测试appid：wx6bb24cbb53333920
    public static final String APP_ID = "wxb80774036b502d7a";
    public static final String APP_SECRET = "wxb80774036b502d7a";
    //微博常量
    public static final String WEIBO_APP_KEY = "2077922365";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WEIBO_SCOPE = null;


    public static String UMENG_APPKEY = "5a581e398f4a9d4c5400001b";
    public static String UMENG_PUSH_SECRET = "660aed25a6d1a34802f721279f25a0c7";

    public static final String PUSH_APP_KEY = "1R80DWfC84AV8uQ8wM19KGHq";

    public static final String SMS_KEY = "i18b1HxmQQVHjbZlcLzLrlFEhJot1umV";

    public static final String SIGN_KEY = "mdSr8zzRsoh9pRMf4XDXMYLtMgPr1DdX";

    /*SharedPreference*/
    public static final String SHOW_FIGHT_LUCK_RED = "show_fight_luck_red";
    public static final String YINDAOYE_BOOKSTORE = "yindaoye_bookstore";
    public static final String COULD_VIBRATOR = "could_vibrator";
    public static final String AUTO_PAGE_TIME = "auto_page_time";
    public static final String START_PAGE_VERSION = "start_page_version";
    public static final String AUTO_EXCHANGE = "auto_exchange";
    public static final String NO_MORE_TIPS_WX = "no_more_tips_wx";//是否不再提示绑定微信
    public static final String FIRST_POST_KEY = "first_post_key";//是否是第一次发表
    public static final String EXCHANGE_TIME = "exchange_time";//前后台切换时间戳
    public static final String FIRST_AUTO_EXCHANGE = "first_auto_exchange";//第一次自动转码必弹框
    public static final String FIRST_WHAT_IS_CIRCLE = "first_what_is_circle";//第一次打开什么是圈子
    public static final String NEEDGETCLOUDRECORD = "needGetCloudRecord";//需要获取云端记录
    public static final String FIRST_RED_DONGTAI = "first_red_dongtai";//首页动态红点
    public static final String READ_TIP_IMG = "read_tip_img2";//阅读页提示
    public static final String ADVERTISEMENT_INTERNAL_TIME = "advertisement_internal_time";//切屏广告展示的默认时间（分钟）
    public static final String READ_SETTINGS_KEY = "readSettings";
    public static final String TASK_SETTINGS_NEW_AMOUNT = "tasksettingsnewamount";
    public static final String TASK_SETTINGS_INVITE_AMOUNT = "tasksettingsinviteamount";
    public static final String READ_LOADING_SLOGAN = "readloadingslogan";
    public static final String READ_SHOW_DOWNLOAD = "read_show_download";
    public static final String FIRST_CIRCLE_READ = "first_circle_read";//圈子共读红点
    public static final String LAST_REQUEST_TIME = "last_request_time";//上次请求共享版权的时间
    public static final String SHANDUI_ANNIU = "shandui_anniu";//闪兑按钮是否显示
    public static final String WALLET_FIRST_CLICK = "wallet_first_click";//钱包页面的红点是否显示
    public static final String SETTING_FIRST_CLICK = "setting_first_click";//账号安全设置的红点是否显示
    public static final String CIRCLE_TOUZI_TIP = "circle_touzi_tip";//账号安全设置的红点是否显示

    /*Intent*/
    public static final String PENNAME = "penname";
    public static final String STATUS = "status";

    /*URL_BASE*/
    //book type
    public static final String BOOK_TYPE_COMMENT = "normal";
    public static final String BOOK_TYPE_VOTE = "vote";
    //book state
    public static final String BOOK_STATE_NORMAL = "normal";
    public static final String BOOK_STATE_DISTILLATE = "distillate";
    //Book Date Convert Format
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";
    //RxBus
    public static final int MSG_SELECTOR = 1;
    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = RxFileTool.getDiskCacheDir() + File.separator
            + "bookcache2" + File.separator;


    public static final String LOGIN_BEFORE_OPTION_KEY = "loginBeforeOptionKey";

//    public static String PATH_DATA = FileUtils.createRootPath(AppUtils.getAppContext()) + "/temp_cache";

    //听书和自动翻页相关
    public static boolean isListenBook = false;//是否是听书模式=默认不是
    public static boolean isAutoPage = false;//是否是自动翻页模式=默认不是
    //加载进度静态变量
    public static int percent = 0;

    public static final int bookIdLine = 50000000;//智能目录和url识别的书id分界线

    public static final String BQT_KEY = "_BQTNEW";

    //换源计数
    public static int changesource = 0;


    public static String getWSBaseUrl() {
        String url = "";
        if (appMode == AppMode.BETA) {
            url = "wss://ws.bendixing.net/";
//            url="http://192.168.31.90:9009";
        } else if (appMode == AppMode.DEV) {
            url = "wss://ws.bendixing.net/";
        } else if (appMode == AppMode.RELEASE){//生产
            url = "wss://ws.bendixing.net/";
        }
        return url;
    }

    public static String getAppBaseUrl() {
        String url = "";
        if (appMode == AppMode.BETA) {
            url = "https://www.onela.cn";
//            url="http://192.168.28.55:16010";
        } else if (appMode == AppMode.DEV) {
            url = "http://192.168.31.84:10001";
        } else if (appMode == AppMode.RELEASE){//生产
            url = "https://gate-api.lianzai.com";
        }
        return url;
    }

    public static String getGatewayApiBaseUrl() {
        String url = "";
        if (appMode == AppMode.BETA) {
            url = "http://lz-pay.bendixing.net";//"http://lz-pay.bendixing.net""http://192.168.31.82:16110"
        } else if (appMode == AppMode.DEV) {
            url = "http://192.168.31.84:10001";
        } else if (appMode == AppMode.RELEASE) {//生产
            url = "https://pay-api.lianzai.com";
        }
        return url;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface ParseUrl {
        String LIANZAIHAO = "/lianzaihao/s/pf";
        String BOOK_READ2 = "/scan/s/pf";
        String BOOK_LIST = "/booklist/s/bl";
        String BOOK_READ = "bookread";
        String LIANZAI_CODE = "lzcode";
        String PERSONAL = "/app/personal";
        String VOTINGRECORDS = "votingrecords";
        String CIRCLE = "/app/circle/";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SmsType {
        String REGISTER_SMS = "register";

        String FORGET_SMS = "forget";

        String BIND_PHONE_SMS = "setPhone";

        String RESET_PAY_PASSWORD_SMS = "resetPayPwd";

        String RESET_LOGIN_PASSWORD_SMS = "resetLoginPwd";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AppMode {
        int RELEASE = 1;//生产环境
        int DEV = 2;//开发环境
        int BETA = 3;//测试环境
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Channel {
        int WEB = 1;
        int WEIXIN = 2;
        int ANDROID = 3;
        int IOS = 4;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Platform {
        int WEB = 1;
        int H5 = 2;
        int APP = 3;
        int QQ = 4;
        int WX = 5;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RegisterOrPassword {
        int REGISTER = 1;
        int ForgetPassword = 2;
        int LoginPassword = 3;
        int PayPassword = 4;
        int BindAccount = 5;
        int BindPhone = 6;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponseCodeStatus {
        int SUCCESS_CODE = 0;//请求成功
        int TOKEN_INVALID = -2;//登录失效
        int NO_PAY_PASSWORD = 477;//未设置支付密码
        int NO_REAL_NAME_AUTHENTICATION = 433;//未实名认证

        int BIND_PHONE = 16018;//需要绑定手机
        int BIND_PHONE2 = 10062;//需要绑定手机
        int DISABLE_ACCOUNT = 10061;//账户禁用
        int DISABLE_CIRCLE = 18015;//圈子禁用
        int DISABLE_PURE = 18048;//纯净模式功能已关闭
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DataEventType {
        int ACCOUNT_DETAIL_EVENT = 0;
    }



    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthType {//认证类型
        String IDCARD_TYPE = "0";
        String PASSPORT_TYPE = "1";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EventTag {//事件
        /**
         * 刷新账号信息
         */
        int REFRESH_ACCOUNT = 1;
        int HOME_FRAGMENT_INIT = 2;
        int MINE_FRAGMENT_INIT = 3;
        int WX_PAY = 4;
        int Close_oldPage = 5; // 关闭之前的页面
        int Forget_Password_Success = 6; // 忘记密码成功，关闭之前的页面

        int BIND_PHONE = 4004;//需要绑定手机
        int DISABLE_ACCOUNT = 4006;//账户禁用

        int CHECK_LOGIN_STATUS = 10001;

        int TOKEN_FAILURE = 40000;//TOKEN失效退出账号
        int LOGOUT = 40001;//普通退出
        int BIND_PHONE_SUCCESS = 40004;//绑定成功

        int WX_LOGIN = 50001;

        int PUSH_JSON_TAG = 310000;//push json传递
        int WEB_JSON_TAG = 310001;//push json传递
        int LOGIN_REFRESH_TAG = 310002;//登录成功-刷新数据
        int LOGIN_OUT_REFRESH_TAG = 310003;//退出登录-刷新数据
        int MAIN_LOGIN_OUT_REFRESH_TAG = 310004;//退出登录-首页刷新数据
        int URL_IDENTIFICATION = 310006;//url识别事件，目前做应用切换到前台和应用内复制书链接
        int MESSAGE_REFRESH = 310007;//消息免打扰开关刷新-刷新数据
        int REOPEN_AD = 310008;//重新展示广告页

        int NETWORK_CONNECT_TAG = 410000;//网络连接上
        int NETWORK_DISCONNECT_TAG = 410001;//网络已断开




        int CALL_STATE_IDLE_TAG = 686860;//电话挂断
        int CALL_STATE_RINGING = 686861;//来电

        int READ_ACTIVITY_STOP_LISTEN_TAG = 686866;//听书暂停


        int HOME_MASK_SHOW_TAG = 800001;//首页遮罩
        int HOME_EXIT = 800002;//首页退出
        int CONTACT_TOP_TAG = 800003;//首页置顶操作
        int REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG = 800004;//移除会话
        int CLOSE_CLOSE_CHAT_BY_ACCID_TAG = 800005;//关闭聊天页面
        int CONTACT_CALCLE_TOP_TAG = 800006;//首页取消置顶操作
        int REFRESH_RED_DOT_TAG = 800007;//刷新红点
        int REFRESH_HOME_RED_DOT_TAG = 800008;//刷新首页底部红点
        int EDIT_MODE_TAG = 800009;//编辑模式
        int DELETE_BOOK_STORE_TAG = 800010;//编辑模式
        int GET_BOOK_STORE_COUNT_TAG = 800011;//编辑模式
        int SWITCH_BOOK_SHELF = 800012;//首页切换到书架TAB
        int SWITCH_BOOK_LIST = 800013;//首页切换到书单TAB
        int SHOW_RED_PACKET = 800014;//首页显示小红包
        int DANMU_ECENT = 800017;//弹幕消息到达
        int READTIME_REFRESH_TAG = 800018;//阅读时长刷新
        int FIGHT_LUCK_SUCCESS = 800019;//发起拼手气成功
        int SHOW_SHARE = 800020;//弹出分享弹框
        int LOADCURRENTCHAPTERSUCCESS = 800021;//当前章节加载成功
        int LOADCURRENTCHAPTERFAILED = 800022;//当前章节加载失败
        int LOADCURRENTCHAPTERTIMEOUT = 800023;//当前章节请求超时
        int RELOADCHAPTERSUCCESSVIEW = 800024;//重新加载成功
        int RELOADCHAPTERFAILEDVIEW = 800025;//重新加载失败
        int LISTEN_INIT_SUCCESS = 800026;//听书初始化成功
        int LISTEN_NEXT = 800027;//下一段
        int LISTEN_EXIT = 800028;//退出听书模式
        int DELETE_POST = 800029;//删除动态，通知上一个页面去掉此项
        int REFRESH_CIRCLE_LIST = 800030;//发布动态之后刷新圈子列表
        int SWITCH_LIANZAI_TAB = 800031;//首页切换到连载TAB
        int LISTEN_START = 800032;//开始播放
        int PRELOADINGCHAPTERSUCCESS = 800033;//预加载成功
        int UPLOADERRORCHAPTERVIEW = 800034;//异常上报
        int REFRESH_SWITCH_CIRCLE_LIST = 800035;//发布动态之后刷新圈子列表并切换到最新
        int ERROR_WEB_JSOUP = 800036;//网页获取失败
        int WX_PAY_REFRESH_WEB = 800037;//微信支付成功之后刷新网页
        int SHOW_SHARE_H5 = 800038;//H5主动调用，特殊情况弹出分享弹框


        int CLOSE_READ_TAG = 900001;//关闭阅读界面
        int REFRESH_BOOK_STORE_TAG = 900003;//刷新书架
        int REFRESH_USER_MINE_TAG = 900004;//刷新用户信息
        int REFRESH_BOOK_STORE_BOOK_LIST_TAG = 900005;//刷新书单列表
        int REFRESH_BOOK_INFO = 900006;//刷新书籍信息
        int REFRESH_BOOK_STORE_REQUEST = 900007;//刷新书架请求接口
        int REFRESH_NEW_FIND = 900008;//刷新发现页
        int SKIP_TO_CIRCLE = 900009;//跳转到圈子
        int REFRESH_LIKE_INFO = 900010;//告诉阅读页面仅仅刷新书籍偏好显示

        int RECHARGE_GOLD_COIN_SUCCESS_TAG = 900100;//金币充值成功
        int INSTALL_NEW_APP_TAG = 1000100;//新版本安装

        int CHAT_ROOM_MEMBER_IN_TAG = 8000100;//聊天室成员进入
        int CHAT_ROOM_MEMBER_OUT_TAG = 8000101;//聊天室成员离开
        int NETWORK_BROKEN_TAG = 8000102;//网络端口
        int CHAT_ROOM_LOGINED_TAG = 8000103;//连接成功
        int CHAT_ROOM_LOGINING_TAG = 8000104;//正在连接
        int CHAT_ROOM_EXIT = 8000105;//退出群聊成功

        int HOBBY_SETTING_CLOSE_TAG = 20000100;//阅读喜好设置页面关闭


    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PageSize {//每页数据数
        int MaxPageSize = 10;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetWorkStatus {//网络状态
        String NETWORK_UNAVAILABLE = "网络连接失败，请检查你的网络设置";
        String SERVER_ERROR = "系统繁忙，再试一次吧";//服务器异常
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface Language {//语言
        String CHINESE = "zh";
        String ENGLISH = "en";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AwardType {//语言
        int JINZUAN = 20000;
        int YUEJUAN = 20001;
        int YUEDIAN = 20002;
        int JINBI = 20003;
        int TUIJIANPIAO = 20004;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface SecuredStatus {//担保状态
        int HANDLING = 1;
        int FINISHED = 2;
        int CLOSED = -2;
        int INARBITRATION = 3;//仲裁中
        int TRANSFERED = 4;//已转账
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthStatus {//认证状态
        int NO_CERTIFIED = 0;
        int IN_REVIEW = 1;
        int CERTIFIED_SUCCESS = 2;//已认证-2级认证
        int PRIMARY_CERTIFIED = 3;//初级认证
        int CERTIFIED_FAILED = 4;//认证失败
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {//消息类型
        int NOTICE_TYPE = 7;//公告
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface BarPostType {//帖子类型
        int MINE_RECOMMEND = 1;//我的推荐
        int MINE_ATTENTION = 2;//我的关注
        int BOOK_BAR_DISCUSSES = 3;//书吧讨论
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PraiseType {//帖子类型
        String PRIASE_POST = "post";//贴吧点赞
        String PRIASE_REPLY = "reply";//回复点赞
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CHASING_SORT_TYPE {//更新时间
        String ORDER_BY_UPDATE_TIME = "shelfTime";//我的推荐
        String ORDER_BY_TOTAL_WORD = "wordNumber";//总字数
        String ORDER_BY_ATTENTION_TIME = "followTime";//关注时间
        String ORDER_BY_MEMBER_AMOUNT = "memberNumber";//关注人数
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ReadBottomSettingType {//
        int SETTING_CATEGORY = 1;//目录
        int SETTING_SCHEDULE = 2;//进度
        int SETTING_BRIGHTNESS = 3;//显示brightness
        int SETTING_FONT = 4;//字体
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BarListType {
        int Bar_Post_Message_Type = 1;//公告
        int Bar_Chapter_Update_Type = 2;//章节有更新
        int Bar_Chapter_Type = 3;//章节贴
        int Bar_Normal_Type = 4;//普通贴
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface RegisterSmsType {
        String REGISTER_SMS = "1";


        String EDIT_PASSWORD_SMS = "3";

        String EDIT_PAY_PASSWORD_SMS = "7";

        String QUICK_LOGIN_SMS = "9";

        String RESET_LOGIN_PASSWORD_SMS = "2";

        String BIND_ACCOUNT_SMS = "6";

        String INVITE_SMS = "4";

        String BIND_BTZ = "101";

    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WalletAccoutType {
        String READ_COIN = "阅点";//阅点
        String READ_TICKET = "阅券";//阅券
        String GOLD_COIN = "金币";//金币
        String GOLD_DRILL = "金钻";//金钻
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface TokenType {
        int READ_TOKEN = 1;//阅点
        int PRO_TOKEN = 2;//PRO
        int MCC_TOKEN = 3;//MCC
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderType {
        int GOLD_RECHARGE_TYPE = 1;//金币充值
        int READ_RECHARGE_TYPE = 2;//READ充值
        int GOLD_TIP_TYPE = 3;//金币打赏
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Terminal {
        int H5 = 1;
        int ANDROID = 2;
        int IOS = 3;
        int PC = 4;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WithdrawType {
        String GOLD_TYPE = "1";
        String READ_TOKEN_TYPE = "3";
        String READ_TICKET_TYPE = "4";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConditionType {
        String RECHARGE_TYPE = "1";
        String TRANSFER_TYPE = "2";
        String EXCHANGE_TYPE = "3";
        String WITHDRAW_TYPE = "5";
        String LOCK_TYPE = "6";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DetailType {
        String GOLD_RECHARGE_DETAIL = "1";
        String GOLD_WITHDRAW_DETAIL = "13";
        String READ_TICKET_EXCHARGE_DETAIL = "8";

        String READ_TOKEN_RECHARGE_DETAIL = "6";
        String READ_TOKEN_WITHDRAW_DETAIL = "2";
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface UserIdentity {
        int NORMAL_USER = 0;//普通用户
        int OFFICAL_PUBLIC_NUMBER = 1;//官方号
        int BOOK_PUBLIC_NUMBER = 2;//普通号
        int BOOK_OUTSIED_NUMBER = 3;//外站号
        int CAN_ATTENTION_NUMBER = 4;//可以关注取关的
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Role {
        int ADMIN_ROLE = 1000;//圈主
        int MANAGE_ROLE = 2000;//代圈主
        int MANAGE2_ROLE = 3000;//管理员
        int FANS_ROLE = 4000;//关注用户
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BookType {
        int BOOK_MINE = 1;//本站
        int BOOK_OUT = 2;//外站
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EXTENDFIELD {
        int OFFICIAL_COMMUNICATION_TYPE = 1;//作家编辑团队，连载安全助手，官方连载号-通知
        int LOCAL_CHAPTER_UPADTE_TYPE = 2;//本站书章节更新
        int OUTSIDE_CHAPTER_UPADTE_TYPE = 3;//外站书章节更新
        int OFFICIAL_NOTICE = 4;//官方连载号公告
        int OFFICIAL_WEEKLY = 5;//官方周报
        int OFFICIAL_NOSHARE = 6;//没封面没有分享
        int OPEN_CHAT_ROOM_MESSAGE = 10;//开通聊天室
        int JOIN_TEAM_MESSAGE = 20;//加入群聊
        int TEAM_MANAGE = 21;//群聊管理员消息

        /*1：title content
          2：title content img bookId chapterid
          3：title content img bookId chapterid
          4：title content url
          5：title content url img
          6：title content url
         10：title content bookId
         20：title content teamId
         21：title content */
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SwipeType {
        int TOP = 1;//置顶
        int CANCEL_TOP = 3;//取消置顶
        int DELETE = 2;//删除
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface BookStoreOptinoType {
        int ADD = -1;//添加关注
        int DELETE = -3;//取消关注
        int REFRESH = -2;//普通刷新
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BookListType {
        int RECOMMEND_TYPE = 1;//精品书单
        int UPDATE_TYPE = 2;//最新更新
        int RECOMMEND_FOR_TOURIST_TYPE = 3;//推荐给游客
        int RECOMMEND_FOR_YOU_TYPE = 4;//为你推荐
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface ChatRoomRole {
        int SYSTEM_ACCOUNT = 1;
        int HAOZHU_ACCOUNT = 2;
        int AUTHOR_ACCOUNT = 3;
        int ADMIN_ACCOUNT = 4;
        int NORMAL_ACCOUNT = 5;
        int GUEST_ACCOUNT = 6;
        int BLACK_ACCOUNT = 10;
        int MUTED_ACCOUNT = 11;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface BeforeLoginOption {//登录前的最后一次操作
        String Collection_Book_List = "收藏书单";
        String Add_Book = "添加书籍";
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastType {
        int TOAST_NORMAL = 1;
        int TOAST_ERROR = 2;
        int TOAST_SUCCESS = 3;
        int TOAST_NETWORK_DISCONNECT = 4;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface IIntegrateSearchType {
        int BOOK_TITLE_TYPE = 1;
        int BOOK_LIST_TITLE_TYPE = 2;
        int BOOK_TYPE = 3;
        int BOOK_LIST_TYPE = 4;
        int LINE_TYPE = 5;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface PushOpenAction {//推送打开行为
        String ENTER_APP = "enterApp";//打开app
        String ENTER_SESSION = "enterSession";//跳转到会话页面
        String ENTER_INTER_READ = "enterInterRead";//内站书阅读
        String ENTER_EXTERAL_READ = "enterExternalRead";//外站书阅读
        String ENTER_URL = "enterNews";//webview
        String ENTER_CHAT_ROOM = "enterChatroom";//进入聊天室
        String ENTER_CIRCLE = "enterCircle";//进入圈子
        String ENTERMYINFORM = "enterMyInform";//进入我的通知
        String ENTER_DYNAMIC = "enterDynamic";//进入动态详情
    }



    @Retention(RetentionPolicy.SOURCE)
    public @interface WebOpenAction {//浏览器唤醒打开行为
        String ENTER_APP = "enterApp";//打开app
        String ENTER_LIANZAIHAO = "enterLianzaiHao";//连载号详情
        String ENTER_BOOK_LIST_DETAIL = "enterBookListDetail";//书单详情
        String GETREWARD = "getReward";//获取老白导流奖励
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface ReadPaddingStyle {//边距
        int PADDING_SMALL = 1;//小边距
        int PADDING_MEDIUM = 2;//中边距
        int PADDING_BIG = 3;//大边距
        int PADDING_DEFAULT = 4;//默认
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface ReadFontStyle {//字体
        int KAITI_FONT = 1;//楷体
        int SONGTI_FONT = 2;//宋体
        int HEITI_FONT = 3;//黑体
        int SYSTEM_FONT = 4;//系统字体
    }


}
