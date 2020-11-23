package com.lianzai.reader.utils;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ReadTimeUploadBean;
import com.lianzai.reader.bean.ReadabilityBean;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.local.CloudRecordRepository;
import com.lianzai.reader.model.local.ReadTimeRepository;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.view.RxToast;

import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by vonde on 2017/1/13
 */

public class RxReadTimeUtils {

    //暂无实际作用
    //图片转字符可以上这个  传送门

    /**　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
     *　　　　　　　　　瓦瓦　　　　　　　　　　　　十　　　　　　　　　　　　　
     *　　　　　　　　十齱龠己　　　　　　　　　亅瓦車己　　　　　　　　　　　　
     *　　　　　　　　乙龍龠毋日丶　　　　　　丶乙己毋毋丶　　　　　　　　　　　
     *　　　　　　　　十龠馬鬼車瓦　　　　　　己十瓦毋毋　　　　　　　　　　　　
     *　　　　　　　　　鬼馬龠馬龠十　　　　己己毋車毋瓦　　　　　　　　　　　　
     *　　　　　　　　　毋龠龠龍龠鬼乙丶丶乙車乙毋鬼車己　　　　　　　　　　　　
     *　　　　　　　　　乙龠龍龍鬼龍瓦　十瓦毋乙瓦龠瓦亅　　　　　　　　　　　　
     *　　　　　　　　　　馬齱龍馬鬼十丶日己己己毋車乙丶　　　　　　　　　　　　
     *　　　　　　　　　　己齱馬鬼車十十毋日乙己己乙乙　　　　　　　　　　　　　
     *　　　　　　　　　　　車馬齱齱日乙毋瓦己乙瓦日亅　　　　　　　　　　　　　
     *　　　　　　　　　　　亅車齺龖瓦乙車龖龍乙乙十　　　　　　　　　　　　　　
     *　　　　　　　　　　　　日龠龠十亅車龍毋十十　　　　　　　　　　　　　　　
     *　　　　　　　　　　　　日毋己亅　己己十亅亅　　　　　　　　　　　　　　　
     *　　　　　　　　　　　丶己十十乙　　丶丶丶丶丶　　　　　　　　　　　　　　
     *　　　　　　　　　　　亅己十龍龖瓦　　丶　丶　乙十　　　　　　　　　　　　
     *　　　　　　　　　　　亅己十龠龖毋　丶丶　　丶己鬼鬼瓦亅　　　　　　　　　
     *　　　　　　　　　　　十日十十日亅丶亅丶　丶十日毋鬼馬馬車乙　　　　　　　
     *　　　　　　　　　　　十日乙十亅亅亅丶　　十乙己毋鬼鬼鬼龍齺馬乙　　　　　
     *　　　　　　　　　　　丶瓦己乙十十亅丶亅乙乙乙己毋鬼鬼鬼龍齱齺齺鬼十　　　
     *　　　　　　　　　　　　乙乙十十十亅乙瓦瓦己日瓦毋鬼鬼龠齱齱龍龍齱齱毋丶　
     *　　　　　　　　　　　　亅十十十十乙瓦車毋瓦瓦日車馬龠龍龍龍龍龍龠龠龠馬亅
     *　　　　　　　　　　　　　十十十十己毋車瓦瓦瓦瓦鬼馬龠龍龠龠龍龠龠龠馬龠車
     *　　　　　　　　　　　　　　亅十十日毋瓦日日瓦鬼鬼鬼龠龠馬馬龠龍龍龠馬馬車
     *　　　　　　　　　　　　　　亅亅亅乙瓦瓦毋車車車馬龍龠鬼鬼馬龠龍龍龠馬馬鬼
     *　　　　　　　　　　　　丶丶乙亅亅乙車鬼鬼鬼毋車龍龍龠鬼馬馬龠龍齱齱龍馬鬼
     *　　　　　　　　　　　亅己十十己十日鬼鬼車瓦毋龠龍龠馬馬龠龠龠齱齺齺齱龠鬼
     *　　　　　　　　　　　　亅乙乙乙十車馬車毋馬齱齱龍龠龠龠馬龠龍齱龍龠龠鬼瓦
     *　　　　　　　　　　　　　　　　丶毋龠鬼車瓦車馬龠龍龠龠龍齱齱龠馬馬鬼毋日
     *　　　　　　　　　　　　　　　　十乙己日十　　丶己鬼龍齱齺齱龍馬馬馬車毋己
     *　　　　　　　　　　　　　　丶十己乙亅丶　　　　　　亅瓦馬龠龍龠龠馬毋瓦乙
     *　　　　　　　　　　　　　丶十十乙亅十　　　　　　　　亅己瓦車馬龠鬼車瓦乙
     *　　　　　　　　　　　　　丶十乙十十丶　　　　　　　　　丶丶亅十瓦鬼車瓦己
     *　　　　　　　　　　　　　　丶亅亅丶　　　　　　　　　　　　　　　亅日瓦日
     *　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　丶
     */


    /**
     *          .,:,,,                                        .::,,,::.
     *        .::::,,;;,                                  .,;;:,,....:i:
     *        :i,.::::,;i:.      ....,,:::::::::,....   .;i:,.  ......;i.
     *        :;..:::;::::i;,,:::;:,,,,,,,,,,..,.,,:::iri:. .,:irsr:,.;i.
     *        ;;..,::::;;;;ri,,,.                    ..,,:;s1s1ssrr;,.;r,
     *        :;. ,::;ii;:,     . ...................     .;iirri;;;,,;i,
     *        ,i. .;ri:.   ... ............................  .,,:;:,,,;i:
     *        :s,.;r:... ....................................... .::;::s;
     *        ,1r::. .............,,,.,,:,,........................,;iir;
     *        ,s;...........     ..::.,;:,,.          ...............,;1s
     *       :i,..,.              .,:,,::,.          .......... .......;1,
     *      ir,....:rrssr;:,       ,,.,::.     .r5S9989398G95hr;. ....,.:s,
     *     ;r,..,s9855513XHAG3i   .,,,,,,,.  ,S931,.,,.;s;s&BHHA8s.,..,..:r:
     *    :r;..rGGh,  :SAG;;G@BS:.,,,,,,,,,.r83:      hHH1sXMBHHHM3..,,,,.ir.
     *   ,si,.1GS,   sBMAAX&MBMB5,,,,,,:,,.:&8       3@HXHBMBHBBH#X,.,,,,,,rr
     *   ;1:,,SH:   .A@&&B#&8H#BS,,,,,,,,,.,5XS,     3@MHABM&59M#As..,,,,:,is,
     *  .rr,,,;9&1   hBHHBB&8AMGr,,,,,,,,,,,:h&&9s;   r9&BMHBHMB9:  . .,,,,;ri.
     *  :1:....:5&XSi;r8BMBHHA9r:,......,,,,:ii19GG88899XHHH&GSr.      ...,:rs.
     *  ;s.     .:sS8G8GG889hi.        ....,,:;:,.:irssrriii:,.        ...,,i1,
     *  ;1,         ..,....,,isssi;,        .,,.                      ....,.i1,
     *  ;h:               i9HHBMBBHAX9:         .                     ...,,,rs,
     *  ,1i..            :A#MBBBBMHB##s                             ....,,,;si.
     *  .r1,..        ,..;3BMBBBHBB#Bh.     ..                    ....,,,,,i1;
     *   :h;..       .,..;,1XBMMMMBXs,.,, .. :: ,.               ....,,,,,,ss.
     *    ih: ..    .;;;, ;;:s58A3i,..    ,. ,.:,,.             ...,,,,,:,s1,
     *    .s1,....   .,;sh,  ,iSAXs;.    ,.  ,,.i85            ...,,,,,,:i1;
     *     .rh: ...     rXG9XBBM#M#MHAX3hss13&&HHXr         .....,,,,,,,ih;
     *      .s5: .....    i598X&&A&AAAAAA&XG851r:       ........,,,,:,,sh;
     *      . ihr, ...  .         ..                    ........,,,,,;11:.
     *         ,s1i. ...  ..,,,..,,,.,,.,,.,..       ........,,.,,.;s5i.
     *          .:s1r,......................       ..............;shs,
     *          . .:shr:.  ....                 ..............,ishs.
     *              .,issr;,... ...........................,is1s;.
     *                 .,is1si;:,....................,:;ir1sr;,
     *                    ..:isssssrrii;::::::;;iirsssssr;:..
     *                         .,::iiirsssssssssrri;;:.
     */


    /**
     *               ii.                                         ;9ABH,
     *              SA391,                                    .r9GG35&G
     *              &#ii13Gh;                               i3X31i;:,rB1
     *              iMs,:,i5895,                         .5G91:,:;:s1:8A
     *               33::::,,;5G5,                     ,58Si,,:::,sHX;iH1
     *                Sr.,:;rs13BBX35hh11511h5Shhh5S3GAXS:.,,::,,1AG3i,GG
     *                .G51S511sr;;iiiishS8G89Shsrrsh59S;.,,,,,..5A85Si,h8
     *               :SB9s:,............................,,,.,,,SASh53h,1G.
     *            .r18S;..,,,,,,,,,,,,,,,,,,,,,,,,,,,,,....,,.1H315199,rX,
     *          ;S89s,..,,,,,,,,,,,,,,,,,,,,,,,....,,.......,,,;r1ShS8,;Xi
     *        i55s:.........,,,,,,,,,,,,,,,,.,,,......,.....,,....r9&5.:X1
     *       59;.....,.     .,,,,,,,,,,,...        .............,..:1;.:&s
     *      s8,..;53S5S3s.   .,,,,,,,.,..      i15S5h1:.........,,,..,,:99
     *      93.:39s:rSGB@A;  ..,,,,.....    .SG3hhh9G&BGi..,,,,,,,,,,,,.,83
     *      G5.G8  9#@@@@@X. .,,,,,,.....  iA9,.S&B###@@Mr...,,,,,,,,..,.;Xh
     *      Gs.X8 S@@@@@@@B:..,,,,,,,,,,. rA1 ,A@@@@@@@@@H:........,,,,,,.iX:
     *     ;9. ,8A#@@@@@@#5,.,,,,,,,,,... 9A. 8@@@@@@@@@@M;    ....,,,,,,,,S8
     *     X3    iS8XAHH8s.,,,,,,,,,,...,..58hH@@@@@@@@@Hs       ...,,,,,,,:Gs
     *    r8,        ,,,...,,,,,,,,,,.....  ,h8XABMMHX3r.          .,,,,,,,.rX:
     *   :9, .    .:,..,:;;;::,.,,,,,..          .,,.               ..,,,,,,.59
     *  .Si      ,:.i8HBMMMMMB&5,....                    .            .,,,,,.sMr
     *  SS       :: h@@@@@@@@@@#; .                     ...  .         ..,,,,iM5
     *  91  .    ;:.,1&@@@@@@MXs.                            .          .,,:,:&S
     *  hS ....  .:;,,,i3MMS1;..,..... .  .     ...                     ..,:,.99
     *  ,8; ..... .,:,..,8Ms:;,,,...                                     .,::.83
     *   s&: ....  .sS553B@@HX3s;,.    .,;13h.                            .:::&1
     *    SXr  .  ...;s3G99XA&X88Shss11155hi.                             ,;:h&,
     *     iH8:  . ..   ,;iiii;,::,,,,,.                                 .;irHA
     *      ,8X5;   .     .......                                       ,;iihS8Gi
     *         1831,                                                 .,;irrrrrs&@
     *           ;5A8r.                                            .:;iiiiirrss1H
     *             :X@H3s.......                                .,:;iii;iiiiirsrh
     *              r#h:;,...,,.. .,,:;;;;;:::,...              .:;;;;;;iiiirrss1
     *             ,M8 ..,....,.....,,::::::,,...         .     .,;;;iiiiiirss11h
     *             8B;.,,,,,,,.,.....          .           ..   .:;;;;iirrsss111h
     *            i@5,:::,,,,,,,,.... .                   . .:::;;;;;irrrss111111
     *            9Bi,:,,,,......                        ..r91;;;;;iirrsss1ss1111
     */


    /**
     *            .,,       .,:;;iiiiiiiii;;:,,.     .,,
     *          rGB##HS,.;iirrrrriiiiiiiiiirrrrri;,s&##MAS,
     *         r5s;:r3AH5iiiii;;;;;;;;;;;;;;;;iiirXHGSsiih1,
     *            .;i;;s91;;;;;;::::::::::::;;;;iS5;;;ii:
     *          :rsriii;;r::::::::::::::::::::::;;,;;iiirsi,
     *       .,iri;;::::;;;;;;::,,,,,,,,,,,,,..,,;;;;;;;;iiri,,.
     *    ,9BM&,            .,:;;:,,,,,,,,,,,hXA8:            ..,,,.
     *   ,;&@@#r:;;;;;::::,,.   ,r,,,,,,,,,,iA@@@s,,:::;;;::,,.   .;.
     *    :ih1iii;;;;;::::;;;;;;;:,,,,,,,,,,;i55r;;;;;;;;;iiirrrr,..
     *   .ir;;iiiiiiiiii;;;;::::::,,,,,,,:::::,,:;;;iiiiiiiiiiiiri
     *   iriiiiiiiiiiiiiiii;;;::::::::::::::::;;;iiiiiiiiiiiiiiiir;
     *  ,riii;;;;;;;;;;;;;:::::::::::::::::::::::;;;;;;;;;;;;;;iiir.
     *  iri;;;::::,,,,,,,,,,:::::::::::::::::::::::::,::,,::::;;iir:
     * .rii;;::::,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,::::;;iri
     * ,rii;;;::,,,,,,,,,,,,,:::::::::::,:::::,,,,,,,,,,,,,:::;;;iir.
     * ,rii;;i::,,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,,::i;;iir.
     * ,rii;;r::,,,,,,,,,,,,,:,:::::,:,:::::::,,,,,,,,,,,,,::;r;;iir.
     * .rii;;rr,:,,,,,,,,,,,,,,:::::::::::::::,,,,,,,,,,,,,:,si;;iri
     *  ;rii;:1i,,,,,,,,,,,,,,,,,,:::::::::,,,,,,,,,,,,,,,:,ss:;iir:
     *  .rii;;;5r,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,sh:;;iri
     *   ;rii;:;51,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.:hh:;;iir,
     *    irii;::hSr,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.,sSs:;;iir:
     *     irii;;:iSSs:.,,,,,,,,,,,,,,,,,,,,,,,,,,,..:135;:;;iir:
     *      ;rii;;:,r535r:...,,,,,,,,,,,,,,,,,,..,;sS35i,;;iirr:
     *       :rrii;;:,;1S3Shs;:,............,:is533Ss:,;;;iiri,
     *        .;rrii;;;:,;rhS393S55hh11hh5S3393Shr:,:;;;iirr:
     *          .;rriii;;;::,:;is1h555555h1si;:,::;;;iirri:.
     *            .:irrrii;;;;;:::,,,,,,,,:::;;;;iiirrr;,
     *               .:irrrriiiiii;;;;;;;;iiiiiirrrr;,.
     *                  .,:;iirrrrrrrrrrrrrrrrri;:.
     *                        ..,:::;;;;:::,,.
     */


    /**
     * ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
     * │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
     * └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
     * ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
     * │~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
     * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
     * │ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
     * ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
     * │ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
     * ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
     * │ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
     * ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
     * │ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
     * └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
     */


    /**
     *                    _ooOoo_
     *                   o8888888o
     *                   88" . "88
     *                   (| -_- |)
     *                    O\ = /O
     *                ____/`---'\____
     *              .   ' \\| |// `.
     *               / \\||| : |||// \
     *             / _||||| -:- |||||- \
     *               | | \\\ - /// | |
     *             | \_| ''\---/'' | |
     *              \ .-\__ `-` ___/-. /
     *           ___`. .' /--.--\ `. . __
     *        ."" '< `.___\_<|>_/___.' >'"".
     *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
     *         \ \ `-. \_ __\ /__ _/ .-` / /
     * ======`-.____`-.___\_____/___.-`____.-'======
     *                    `=---='
     *
     * .............................................
     *          佛祖保佑             永无BUG
     */


    /**
     *  佛曰:
     *          写字楼里写字间，写字间里程序员；
     *          程序人员写程序，又拿程序换酒钱。
     *          酒醒只在网上坐，酒醉还来网下眠；
     *          酒醉酒醒日复日，网上网下年复年。
     *          但愿老死电脑间，不愿鞠躬老板前；
     *          奔驰宝马贵者趣，公交自行程序员。
     *          别人笑我忒疯癫，我笑自己命太贱；
     *          不见满街漂亮妹，哪个归得程序员？
     */


    /**
     * _ooOoo_
     * o8888888o
     * 88" . "88
     * (| -_- |)
     *  O\ = /O
     * ___/`---'\____
     * .   ' \\| |// `.
     * / \\||| : |||// \
     * / _||||| -:- |||||- \
     * | | \\\ - /// | |
     * | \_| ''\---/'' | |
     * \ .-\__ `-` ___/-. /
     * ___`. .' /--.--\ `. . __
     * ."" '< `.___\_<|>_/___.' >'"".
     * | | : `- \`.;`\ _ /`;.`/ - ` : | |
     * \ \ `-. \_ __\ /__ _/ .-` / /
     * ======`-.____`-.___\_____/___.-`____.-'======
     * `=---='
     *          .............................................
     *           佛曰：bug泛滥，我已瘫痪！
     */


    /**
     *                    .::::.
     *                  .::::::::.
     *                 :::::::::::  Come On!
     *             ..:::::::::::'
     *           '::::::::::::'
     *             .::::::::::
     *        '::::::::::::::..
     *             ..::::::::::::.
     *           ``::::::::::::::::
     *            ::::``:::::::::'        .:::.
     *           ::::'   ':::::'       .::::::::.
     *         .::::'      ::::     .:::::::'::::.
     *        .:::'       :::::  .:::::::::' ':::::.
     *       .::'        :::::.:::::::::'      ':::::.
     *      .::'         ::::::::::::::'         ``::::.
     *  ...:::           ::::::::::::'              ``::.
     * ```` ':.          ':::::::::'                  ::::..
     *                    '.:::::'                    ':'````..
     */


    /**
     *   ┏┓　　　┏┓
     * ┏┛┻━━━┛┻┓
     * ┃　　　　　　　┃
     * ┃　　　━　　　┃
     * ┃　＞　　　＜　┃
     * ┃　　　　　　　┃
     * ┃...　⌒　...　┃
     * ┃　　　　　　　┃
     * ┗━┓　　　┏━┛
     *     ┃　　　┃　
     *     ┃　　　┃
     *     ┃　　　┃
     *     ┃　　　┃  神兽保佑
     *     ┃　　　┃  代码无bug　　
     *     ┃　　　┃
     *     ┃　　　┗━━━┓
     *     ┃　　　　　　　┣┓
     *     ┃　　　　　　　┏┛
     *     ┗┓┓┏━┳┓┏┛
     *       ┃┫┫　┃┫┫
     *       ┗┻┛　┗┻┛
     */


    /**
     *　　┏┓　　　┏┓+ +
     *　┏┛┻━━━┛┻┓ + +
     *　┃　　　　　　　┃ 　
     *　┃　　　━　　　┃ ++ + + +
     * ████━████ ┃+
     *　┃　　　　　　　┃ +
     *　┃　　　┻　　　┃
     *　┃　　　　　　　┃ + +
     *　┗━┓　　　┏━┛
     *　　　┃　　　┃　　　　　　　　　　　
     *　　　┃　　　┃ + + + +
     *　　　┃　　　┃
     *　　　┃　　　┃ +  神兽保佑
     *　　　┃　　　┃    代码无bug　　
     *　　　┃　　　┃　　+　　　　　　　　　
     *　　　┃　 　　┗━━━┓ + +
     *　　　┃ 　　　　　　　┣┓
     *　　　┃ 　　　　　　　┏┛
     *　　　┗┓┓┏━┳┓┏┛ + + + +
     *　　　　┃┫┫　┃┫┫
     *　　　　┗┻┛　┗┻┛+ + + +
     */


    /**
     * ━━━━━━神兽出没━━━━━━
     * 　　　┏┓　　　┏┓
     * 　　┏┛┻━━━┛┻┓
     * 　　┃　　　　　　　┃
     * 　　┃　　　━　　　┃
     * 　　┃　┳┛　┗┳　┃
     * 　　┃　　　　　　　┃
     * 　　┃　　　┻　　　┃
     * 　　┃　　　　　　　┃
     * 　　┗━┓　　　┏━┛
     * 　　　　┃　　　┃  神兽保佑
     * 　　　　┃　　　┃  代码无bug　　
     * 　　　　┃　　　┗━━━┓
     * 　　　　┃　　　　　　　┣┓
     * 　　　　┃　　　　　　　┏┛
     * 　　　　┗┓┓┏━┳┓┏┛
     * 　　　　　┃┫┫　┃┫┫
     * 　　　　　┗┻┛　┗┻┛
     * ━━━━━━感觉萌萌哒━━━━━━
     */

    /**
     **************************************************************
     *                                                            *
     *   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
     *    |                     ______                     |      *
     *    |                  .-"      "-.                  |      *
     *    |                 /            \                 |      *
     *    |     _          |              |          _     |      *
     *    |    ( \         |,  .-.  .-.  ,|         / )    |      *
     *    |     > "=._     | )(__/  \__)( |     _.=" <     |      *
     *    |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
     *    |           "=._"(_     ^^     _)"_.="           |      *
     *    |               "=\__|IIIIII|__/="               |      *
     *    |              _.="| \IIIIII/ |"=._              |      *
     *    |    _     _.="_.="\          /"=._"=._     _    |      *
     *    |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
     *    |    > _.="                            "=._ <    |      *
     *    |   (_/                                    \_)   |      *
     *    |                                                |      *
     *    '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
     *                                                            *
     *           LASCIATE OGNI SPERANZA, VOI CH'ENTRATE           *
     **************************************************************
     */


    /**
     *                                         ,s555SB@@&
     *                                      :9H####@@@@@Xi
     *                                     1@@@@@@@@@@@@@@8
     *                                   ,8@@@@@@@@@B@@@@@@8
     *                                  :B@@@@X3hi8Bs;B@@@@@Ah,
     *             ,8i                  r@@@B:     1S ,M@@@@@@#8;
     *            1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S
     *            1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5
     *            ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A
     *             1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1
     *              sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1
     *               .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1
     *               s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981
     *               ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;
     *                .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS
     *                s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,
     *              :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,
     *            .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8
     *          :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:
     *      s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r
     *   ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i
     *  5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;
     *  9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:
     *   SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5
     *     ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B
     *           ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S
     *              ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#:
     *            .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.
     *
     *
     *    产品又改需求，改你妹啊！！
     */


    /**
     *_______________#########_______________________
     *______________############_____________________
     *______________#############____________________
     *_____________##__###########___________________
     *____________###__######_#####__________________
     *____________###_#######___####_________________
     *___________###__##########_####________________
     *__________####__###########_####_______________
     *________#####___###########__#####_____________
     *_______######___###_########___#####___________
     *_______#####___###___########___######_________
     *______######___###__###########___######_______
     *_____######___####_##############__######______
     *____#######__#####################_#######_____
     *____#######__##############################____
     *___#######__######_#################_#######___
     *___#######__######_######_#########___######___
     *___#######____##__######___######_____######___
     *___#######________######____#####_____#####____
     *____######________#####_____#####_____####_____
     *_____#####________####______#####_____###______
     *______#####______;###________###______#________
     *________##_______####________####______________
     */


    /**
     * 頂頂頂頂頂頂頂頂頂　頂頂頂頂頂頂頂頂頂
     * 頂頂頂頂頂頂頂　　　　　頂頂　　　　　
     * 　　　頂頂　　　頂頂頂頂頂頂頂頂頂頂頂
     * 　　　頂頂　　　頂頂頂頂頂頂頂頂頂頂頂
     * 　　　頂頂　　　頂頂　　　　　　　頂頂
     * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂
     * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂
     * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂
     * 　　　頂頂　　　頂頂　　頂頂頂　　頂頂
     * 　　　頂頂　　　　　　　頂頂頂　
     * 　　　頂頂　　　　　　頂頂　頂頂　頂頂
     * 　頂頂頂頂　　　頂頂頂頂頂　頂頂頂頂頂
     * 　頂頂頂頂　　　頂頂頂頂　　　頂頂頂頂
     */

    /**
     * 1只羊 == one sheep
     * 2只羊 == two sheeps
     * 3只羊 == three sheeps
     * 4只羊 == four sheeps
     * 5只羊 == five sheeps
     * 6只羊 == six sheeps
     * 7只羊 == seven sheeps
     * 8只羊 == eight sheeps
     * 9只羊 == nine sheeps
     * 10只羊 == ten sheeps
     * 11只羊 == eleven sheeps
     * 12只羊 == twelve sheeps
     * 13只羊 == thirteen sheeps
     * 14只羊 == fourteen sheeps
     * 15只羊 == fifteen sheeps
     * 16只羊 == sixteen sheeps
     * 17只羊 == seventeen sheeps
     * 18只羊 == eighteen sheeps
     * 19只羊 == nineteen sheeps
     * 20只羊 == twenty sheeps
     * 21只羊 == twenty one sheeps
     * 22只羊 == twenty two sheeps
     * 23只羊 == twenty three sheeps
     * 24只羊 == twenty four sheeps
     * 25只羊 == twenty five sheeps
     * 26只羊 == twenty six sheeps
     * 27只羊 == twenty seven sheeps
     * 28只羊 == twenty eight sheeps
     * 29只羊 == twenty nine sheeps
     * 30只羊 == thirty sheeps
     * 现在瞌睡了吧，好了，不要再改下面的代码了，睡觉咯~~
     */

    /**
     * For the brave souls who get this far: You are the chosen ones,
     * the valiant knights of programming who toil away, without rest,
     * fixing our most awful code. To you, true saviors, kings of men,
     * I say this: never gonna give you up, never gonna let you down,
     * never gonna run around and desert you. Never gonna make you cry,
     * never gonna say goodbye. Never gonna tell a lie and hurt you.
     *
     * 致终于来到这里的勇敢的人：
     * 你是被上帝选中的人，是英勇的、不敌辛苦的、不眠不休的来修改我们这最棘手的代码的编程骑士。
     * 你，我们的救世主，人中之龙，我要对你说：永远不要放弃，永远不要对自己失望，永远不要逃走，辜负了自己，
     * 永远不要哭啼，永远不要说再见，永远不要说谎来伤害自己。
     */


    /**
     * When I wrote this, only God and I understood what I was doing
     * Now, God only knows
     *
     * 写这段代码的时候，只有上帝和我知道它是干嘛的
     * 现在，只有上帝知道
     */


    /**
     * Here be dragons
     * 前方高能
     */

    public static boolean isScan = false;
    public static final long MAX_TIME = 40;


    /*扫描阅读时长数据，根据现有数据进行上传*/
    public static void scanReadTime(){
        AccountTokenBean accountTokenBean = RxLoginTool.getLoginAccountToken();
        if(isScan){
//            RxLogTool.e("扫描数据：正在扫描不执行");
            return;
        }
        if (null != accountTokenBean) {
            if (RxNetTool.isAvailable()) {
                String uid = String.valueOf(accountTokenBean.getData().getId());
                List<ReadTimeBean> readTimeBeans = ReadTimeRepository.getInstance().getReadTimeList(uid);
                if(null == readTimeBeans || readTimeBeans.isEmpty()) {
                    return;
                }

                //开启线程执行上传操作
                new Thread() {
                    public void run() {
                        try {
//                            RxLogTool.e("初始数据" + String.valueOf(new Gson().toJson(readTimeBeans.size())));
                            isScan = true;
                            getNeedUpload(readTimeBeans,0,uid);
                        } catch (Exception e) {
//                            RxLogTool.e("扫描数据：数据错误结束");
                            isScan = false;
                            return;
                        }
                    }
                }.start();

            }
        }

    }


    //取得五分钟
    public static void getNeedUpload(List<ReadTimeBean> readTimeBeans , int index,String uid){
        long total = 0;
        int end = index;
        int size = readTimeBeans.size();
        if(index >= size){
//            RxLogTool.e("扫描数据：无更多数据结束");
            isScan = false;
            return;
        }
        for(int i = index; i < size; i ++){
            ReadTimeBean item = readTimeBeans.get(i);
            long itemTime = (item.getEndTime() - item.getStartTime())/1000;
            //数据最大40秒
            if(itemTime > MAX_TIME){
                itemTime = MAX_TIME;
            }
            total += itemTime;
            if(total >= 300){
               //满足条件
               end = i;
               break;
            }
        }
        if(end > index){
            List<ReadTimeBean> uploadList = readTimeBeans.subList(index, end + 1);
            index = end + 1;
            //进行上传
            upload(uploadList,readTimeBeans,uid,index);
        }else {
//            RxLogTool.e("扫描数据：剩余时长不足五分钟结束");
            //满数据且不足五分钟，视为刷子，清库
            if(index == 0 && readTimeBeans.size() == 500){
                ReadTimeRepository.getInstance().clear(readTimeBeans);
            }

            isScan = false;
            return;
        }
    }

    public static String prepareData(List<ReadTimeBean> readTimeBeans,String uid){
        ReadTimeUploadBean readTimeUploadBean = new ReadTimeUploadBean();
        readTimeUploadBean.setDate(String.valueOf(System.currentTimeMillis()));
        readTimeUploadBean.setUid(Integer.parseInt(uid));
        readTimeUploadBean.setChange(false);

        String angle = null;

        int size = readTimeBeans.size();
        for(int i = 0; i < size; i ++){
            ReadTimeBean item = readTimeBeans.get(i);
            long itemTime = (item.getEndTime() - item.getStartTime())/1000;
            //数据最大40秒
            if(itemTime > MAX_TIME){
                itemTime = MAX_TIME;
            }
            //每次都更新主体数据
            readTimeUploadBean.setDeviceCode(item.getDeviceCode());
            readTimeUploadBean.setPhoneModel(item.getPhoneModel());
            readTimeUploadBean.setReadType(item.getReadType());
            if(!TextUtils.isEmpty(angle)){
                int nowAngle = 0;
                int recentAngle = 0;
                try{
                    nowAngle = Integer.parseInt(item.getAngle());
                }catch (Exception e){
                }
                try{
                    recentAngle = Integer.parseInt(angle);
                }catch (Exception e){
                }

                if(Math.abs(nowAngle - recentAngle) > 3 ){
                    readTimeUploadBean.setChange(true);
                }
            }
            angle = item.getAngle();

            ReadTimeUploadBean.ReadInfoDtoSon contain = readTimeUploadBean.contain(item.getBookId());
            if(null != contain){
                contain.setChapterId(item.getChapterId());
                int pageCount = contain.getPageCount();
                pageCount++;
                contain.setPageCount(pageCount);
                contain.setPageNum(item.getPageNum());
                contain.setStartTime(String.valueOf(item.getStartTime()));
                int second = contain.getSecond();
                second += itemTime;
                contain.setSecond(second);
            }else {
                contain = new ReadTimeUploadBean.ReadInfoDtoSon();
                contain.setBookId(item.getBookId());
                contain.setChapterId(item.getChapterId());
                int pageCount = contain.getPageCount();
                pageCount++;
                contain.setPageCount(pageCount);
                contain.setPageNum(item.getPageNum());
                contain.setStartTime(String.valueOf(item.getStartTime()));
                int second = contain.getSecond();
                second += itemTime;
                contain.setSecond(second);
            }
            readTimeUploadBean.add(contain);
        }
//"{\"date\":\"Aug 7, 2019 11:04:12\",\"deviceCode\":\"866401035105264\",\"isChange\":false,\"phoneModel\":\"1713-A01\",\"readInfoDtoSons\":[{\"bookId\":6,\"chapterId\":66,\"pageCount\":1,\"pageNum\":1,\"second\":2,\"startTime\":\"Aug 7, 2019 10:54:56\"}],\"readType\":1,\"uid\":499384}"
        String jsonParam=new Gson().toJson(readTimeUploadBean);//集合转为json串
        try {
            jsonParam= AESCipher.aesEncryptString(jsonParam);
        }catch (Exception e){
//            RxLogTool.e("operateReadTime aesDecryptString error:"+e.getMessage());
        }

//        try {
//            String temp = AESCipher.aesDecryptString(jsonParam);
//        }catch (Exception e){
//            RxLogTool.e("operateReadTime aesDecryptString error:"+e.getMessage());
//        }

        return jsonParam;
    }


    private static void upload(List<ReadTimeBean> uploadList ,List<ReadTimeBean> readTimeBeans,String uid,int index){
        //进行上传，上传成功则迭代执行，上传失败则直接退出，不再重复
        JsonObject jsonObject = new JsonObject();
        try{
            String json = prepareData(uploadList,uid);
            jsonObject.addProperty("content", json);
//            RxLogTool.e("上传数据：" + json);
        }catch (Exception e){
//            RxLogTool.e("扫描数据：数据错误结束");
            isScan = false;
            return;
        }

        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/read", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxLogTool.e("扫描数据：接口错误结束");
                    isScan = false;
                } catch (Exception ex) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE ) {
//                        RxLogTool.e("扫描数据：上传完成继续扫描");
                        //清除掉上传成功的部分
                        ReadTimeRepository.getInstance().clear(uploadList);
                        //迭代执行
                        getNeedUpload(readTimeBeans,index,uid);
                    } else  {
//                        RxLogTool.e("扫描数据：返回错误码结束");
                        isScan = false;
                    }
                } catch (Exception e) {
//                    RxLogTool.e("扫描数据：处理异常结束");
                    isScan = false;
                }
            }
        });
    }


    //根据书id获取当前阅读到的进度
    public static String getSignUrl(String bookId){
        CloudRecordBean cloudRecordBean = CloudRecordRepository.getInstance()
                .getCloudRecord(Long.parseLong(bookId));
        if (cloudRecordBean == null) {
            return "?bookId=" + bookId;
        }else if(cloudRecordBean.getChapterId() <= 0){
            return "?bookId=" + bookId;
        }else {
            return "?bookId=" + bookId + "&chapterId=" + cloudRecordBean.getChapterId();
        }
    }



}
