package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.CircleBookBean;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.PayForBookBean;
import com.lianzai.reader.bean.PostDetailBean;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.EllipsizeTextView;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.HashMap;
import java.util.List;

/**
 * 百度腾讯广告加上圈子动态
 */
public class CircleDynamicAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {


    int contentWidth;

    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap = new HashMap<NativeExpressADView, Integer>();

    DynamicClickListener dynamicClickListener;

    static final int TYPE_DATA = 0;
    static final int TYPE_AD1 = 1;
    static final int TYPE_AD2 = 2;

    public CircleDynamicAdapter(@Nullable List<Object> data, Context mContext) {
        super(R.layout.item_circle_dynamic_content_view, data);
        contentWidth = RxDeviceTool.getScreenWidth(mContext) - RxImageTool.dip2px(24);
    }

    public HashMap<NativeExpressADView, Integer> getmAdViewPositionMap() {
        return mAdViewPositionMap;
    }

    public void setmAdViewPositionMap(HashMap<NativeExpressADView, Integer> mAdViewPositionMap) {
        this.mAdViewPositionMap = mAdViewPositionMap;
    }

    public DynamicClickListener getDynamicClickListener() {
        return dynamicClickListener;
    }

    public void setDynamicClickListener(DynamicClickListener dynamicClickListener) {
        this.dynamicClickListener = dynamicClickListener;
    }

    public int getItemViewType(Object obj) {
        if (obj instanceof NativeExpressADView) {
            return TYPE_AD1;
        } else if (obj instanceof BannerBean.DataBean) {
            return TYPE_AD2;
        } else {
            return TYPE_DATA;
        }
    }

    // 把返回的BannerBean添加到数据集里面去
    public void addADToPosition(int position, BannerBean.DataBean data) {
        if (position >= 0 && null != data) {
            if( position < mData.size()) {
                mData.add(position, data);
            }else {
                mData.add(data);
            }
        }
    }

    // 把返回的NativeExpressADView添加到数据集里面去
    public void addADViewToPosition(int position, NativeExpressADView adView) {
        if (position >= 0  && adView != null) {
            if( position < mData.size()) {
                mData.add(position, adView);
            }else {
                mData.add(adView);
            }
        }
    }

    // 移除NativeExpressADView的时候是一条一条移除的
    public void removeADView(int position) {
        mData.remove(position);
        notifyItemRemoved(position + getHeaderLayoutCount()); // position为adView在当前列表中的位置
//        notifyItemRangeChanged(0, mData.size() - 1);
    }


    @Override
    protected void convert(final BaseViewHolder holder, Object obj) {
        int type = getItemViewType(obj);
        LinearLayout ll_dynamic_root = holder.getView(R.id.ll_dynamic_root);//data根节点
        ViewGroup container = (ViewGroup) holder.getView(R.id.express_ad_container);//ad1根节点
        RelativeLayout native_outer_view = (RelativeLayout) holder.getView(R.id.native_outer_view);//ad2根节点
        if (TYPE_AD1 == type) {
            container.setVisibility(View.VISIBLE);
            native_outer_view.setVisibility(View.GONE);
            ll_dynamic_root.setVisibility(View.GONE);
            final NativeExpressADView adView = (NativeExpressADView) obj;
            if (container.getChildCount() > 0
                    && container.getChildAt(0) == adView) {
                return;
            }
            if (container.getChildCount() > 0) {
                container.removeAllViews();
            }

            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            mAdViewPositionMap.put(adView, holder.getPosition()); // 把每个广告在列表中位置记录下来

            container.addView(adView);
            adView.render(); // 调用render方法后sdk才会开始展示广告
        } else if (TYPE_AD2 == type) {
            container.setVisibility(View.GONE);
            native_outer_view.setVisibility(View.VISIBLE);
            ll_dynamic_root.setVisibility(View.GONE);

            BannerBean.DataBean listBean = (BannerBean.DataBean) obj;
            ImageView native_close = holder.getView(R.id.native_close);
            ImageView native_image = holder.getView(R.id.native_image);

            /*新增头部标语和底部标语*/
            TextView native_title = holder.getView(R.id.native_title);
            TextView native_des = holder.getView(R.id.native_des);
            native_title.setText(listBean.getHeadSlogan());
            if(!TextUtils.isEmpty(listBean.getTailSlogan())) {
                native_des.setVisibility(View.VISIBLE);
                native_des.setText(listBean.getTailSlogan());
            }else {
                native_des.setVisibility(View.GONE);
            }
            /*新增头部标语和底部标语*/

            //根据屏幕宽度设置视图宽度
            ViewGroup.LayoutParams layoutParams = native_image.getLayoutParams();
            layoutParams.height = (int) (contentWidth/2.3);
            native_image.setLayoutParams(layoutParams);


            RxImageTool.loadImage(mContext,listBean.getBannerPhoto(),native_image);
            native_close.setOnClickListener(
                    v -> {
                        getDynamicClickListener().closeClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                    }
            );

            native_image.setOnClickListener(
                    v -> {
                        RxAppTool.adSkip(listBean,mContext);
                    }
            );


        } else {
            container.setVisibility(View.GONE);
            native_outer_view.setVisibility(View.GONE);
            ll_dynamic_root.setVisibility(View.VISIBLE);

            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) obj;

            //用户信息
            CircleImageView civ_dynamic_user_logo = holder.getView(R.id.civ_dynamic_user_logo);
            TextView tv_dynamic_user_name = holder.getView(R.id.tv_dynamic_user_name);
            TextView tv_dynamic_create_date = holder.getView(R.id.tv_dynamic_create_date);
            TextView tv_dynamic_create_location = holder.getView(R.id.tv_dynamic_create_location);

            //动态正文内容-纯文字，文字带图片,文字带书，文字带圈子,文字带网页
            EllipsizeTextView tv_dynamic_content_text = holder.getView(R.id.tv_dynamic_content_text);

            //显示打赏额外信息
            RelativeLayout rl_payforbook= holder.getView(R.id.rl_payforbook);
            TextView tv_payforbook = holder.getView(R.id.tv_payforbook);
            ImageView iv_payforbook = holder.getView(R.id.iv_payforbook);


            //动态操作相关
            RelativeLayout ly_operation= holder.getView(R.id.ly_operation);
            LinearLayout ll_operation_praise = holder.getView(R.id.ll_operation_praise);
            ImageView iv_operation_praise = holder.getView(R.id.iv_operation_praise);
            TextView tv_operation_praise = holder.getView(R.id.tv_operation_praise);

            LinearLayout ll_operation_comment = holder.getView(R.id.ll_operation_comment);
            ImageView iv_operation_comment = holder.getView(R.id.iv_operation_comment);
            TextView tv_operation_comment = holder.getView(R.id.tv_operation_comment);


            ImageView iv_operation_share = holder.getView(R.id.iv_operation_share);
            ImageView iv_operation_more = holder.getView(R.id.iv_operation_more);

            //评论相关
            LinearLayout ll_dynamic_comment_list = holder.getView(R.id.ll_dynamic_comment_list);

            //周报展示区域
            LinearLayout ly_week = holder.getView(R.id.ly_week);
            SelectableRoundedImageView iv_week_cover = holder.getView(R.id.iv_week_cover);
            TextView tv_week_title = holder.getView(R.id.tv_week_title);

            //书籍展示区域
//            RelativeLayout rl_book = holder.getView(R.id.rl_book);
//            SelectableRoundedImageView iv_book_cover = holder.getView(R.id.iv_book_cover);
//            TextView tv_book_chapter_name = holder.getView(R.id.tv_book_chapter_name);
//            TextView tv_book_source = holder.getView(R.id.tv_book_source);
//            TextView tv_book_name = holder.getView(R.id.tv_book_name);
            //书架图片
//            ImageView iv_book_shelf = holder.getView(R.id.iv_book_shelf);

            //图片展示区域
            LinearLayout ll_dynamic_images = holder.getView(R.id.ll_dynamic_images);

            //假书评没有操作栏
            if(listBean.isCounterfeit()){
                ly_operation.setVisibility(View.GONE);
            }else {
                ly_operation.setVisibility(View.VISIBLE);
            }

            //点击事件设置
            if (null != getDynamicClickListener()) {
                ll_dynamic_root.setOnClickListener(
                        v -> {
                            //假书评不能进
                            if(!listBean.isCounterfeit())
                            getDynamicClickListener().contentDynamicClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );
                civ_dynamic_user_logo.setOnClickListener(
                        v -> {
                            getDynamicClickListener().headClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );
                tv_dynamic_user_name.setOnClickListener(
                        v -> {
                            getDynamicClickListener().headClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );
//                rl_book.setOnClickListener(
//                        v -> {
//                            getDynamicClickListener().imageDynamicClick(0, holder.getAdapterPosition() - getHeaderLayoutCount());
//                        }
//                );

                ly_week.setOnClickListener(
                        v -> {
                            getDynamicClickListener().imageDynamicClick(0, holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );

                ll_operation_praise.setOnClickListener(
                        v -> {
                            //假书评不能进
                            if(!listBean.isCounterfeit())
                            getDynamicClickListener().praiseClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );
                ll_operation_comment.setOnClickListener(
                        v -> {
                            //假书评不能进
                            if(!listBean.isCounterfeit())
                            getDynamicClickListener().commentClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );

                ll_dynamic_comment_list.setOnClickListener(
                        v -> {
                            //假书评不能进
                            if(!listBean.isCounterfeit())
                            getDynamicClickListener().commentClick(holder.getAdapterPosition() - getHeaderLayoutCount());
                        }
                );
                iv_operation_more.setOnClickListener(
                        v -> {
                            //假书评不能进
                            if(!listBean.isCounterfeit())
                            getDynamicClickListener().moreOptionClick(holder.getAdapterPosition() - getHeaderLayoutCount(), iv_operation_more);
                        }
                );
            }


            /*开始数据加载*/
            /*通用部分*/
            RxImageTool.loadLogoImage(mContext, listBean.getUserPic(), civ_dynamic_user_logo);
            tv_dynamic_user_name.setText(listBean.getUserName());
            tv_dynamic_create_date.setText(TimeFormatUtil.getInterval(listBean.getCreateTime()));
            if (!TextUtils.isEmpty(listBean.getAddress())) {
                tv_dynamic_create_location.setVisibility(View.VISIBLE);
                tv_dynamic_create_location.setText(listBean.getAddress());
            } else {
                tv_dynamic_create_location.setVisibility(View.GONE);
            }
            //文字内容
            if(!TextUtils.isEmpty(listBean.getContentShow())) {
                tv_dynamic_content_text.setVisibility(View.VISIBLE);
                URLUtils.replaceBook(listBean.getContentShow().replace("\n"," "),mContext,tv_dynamic_content_text,listBean.getUrlTitle());
            }else {
                tv_dynamic_content_text.setVisibility(View.GONE);
                tv_dynamic_content_text.setText("");
            }

            if (listBean.isIsLike()) {
                iv_operation_praise.setImageResource(R.mipmap.icon_dynamic_praised);
            } else {
                iv_operation_praise.setImageResource(R.mipmap.icon_dynamic_not_praise);
            }
            //点赞数显示
            tv_operation_praise.setText(RxDataTool.getLikeCount(listBean.getLikeCount()));

            //评论数显示
            tv_operation_comment.setText(RxDataTool.getLikeCount(listBean.getCommentCount()));

            //评论数据显示
            showDynamicCommentData(ll_dynamic_comment_list, listBean.getCommentResp(),holder.getAdapterPosition() - getHeaderLayoutCount());

//            if(listBean.isInBookShelf()){
//                iv_book_shelf.setVisibility(View.VISIBLE);
//            }else {
//                iv_book_shelf.setVisibility(View.GONE);
//            }
            //接下来判断是什么类型的动态
            /*2,3.书籍类型4,5.周报类型30.普通类型*/
            if(listBean.getPostStatus() == 5){
                //举报类型
                tv_dynamic_content_text.setText("--该内容举报过多，连载客服正在审核中--");
                ly_week.setVisibility(View.GONE);
                ll_dynamic_images.setVisibility(View.GONE);
                rl_payforbook.setVisibility(View.GONE);
            }else if (listBean.getPostType() == 2 || listBean.getPostType() == 3) {
                ly_week.setVisibility(View.GONE);
//                rl_book.setVisibility(View.VISIBLE);
                ll_dynamic_images.setVisibility(View.GONE);
                rl_payforbook.setVisibility(View.GONE);
//                try {
//                    CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(), CircleBookBean.class);
//                    RxImageTool.loadImage(mContext, circleBookBean.getCover(), iv_book_cover);
//                    tv_book_chapter_name.setText(circleBookBean.getChapterName());
//                    String source = circleBookBean.getSource();
//                    tv_book_source.setText("来源：" + source);
//                    tv_book_name.setText("书名：" + circleBookBean.getBookName());
//                    //不显示内站书来源
//                    if (TextUtils.isEmpty(source) || source.contains("lianzai.com") || source.contains("bendixing.net")) {
//                        tv_book_source.setText("来源：连载阅读");
//                    }
//                } catch (Exception e) {
//                }
            } else if (listBean.getPostType() == 4 || listBean.getPostType() == 5 || listBean.getPostType() == 6 || listBean.getPostType() == 7) {
                ly_week.setVisibility(View.VISIBLE);
//                rl_book.setVisibility(View.GONE);
                ll_dynamic_images.setVisibility(View.GONE);
                rl_payforbook.setVisibility(View.GONE);
                try {
                    CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(), CircleBookBean.class);
                    RxImageTool.loadImage(mContext, circleBookBean.getCover(), iv_week_cover);
                    tv_week_title.setText(listBean.getTitleShow());
                } catch (Exception e) {
                }
            } else {
                String pictureShow = listBean.getPicturesShow();
                if(!TextUtils.isEmpty(pictureShow) && pictureShow.startsWith("{")){//打赏发布的动态
                    ly_week.setVisibility(View.GONE);
                    ll_dynamic_images.setVisibility(View.GONE);
                    rl_payforbook.setVisibility(View.VISIBLE);
                    try {
                        PayForBookBean payForBookBean = GsonUtil.getBean(pictureShow, PayForBookBean.class);
                        RxImageTool.loadImage(mContext, payForBookBean.getRewardImg(), iv_payforbook);
                        tv_payforbook.setText(payForBookBean.getRewardContent());
                    } catch (Exception e) {
                        RxLogTool.e(e.toString());
                    }
                }else {
                    ly_week.setVisibility(View.GONE);
                    ll_dynamic_images.setVisibility(View.VISIBLE);
                    rl_payforbook.setVisibility(View.GONE);
                    if (null != listBean.getThumbnailImages() && !listBean.getThumbnailImages().isEmpty()) {
                        showDynamicImage(ll_dynamic_images, listBean.getThumbnailImages(), holder.getAdapterPosition() - getHeaderLayoutCount());
                    } else {
                        ll_dynamic_images.setVisibility(View.GONE);
                    }
                }
            }

        }
    }

    private void  addEllipsisAndAllAtEnd(TextView tv){
        try{
            Layout ll = tv.getLayout();
            if(null == ll){
                RxLogTool.e("注意复用问题，复用时ll为空" + tv.getText().toString());
                return;
            }
            int line = ll.getLineCount();
            int count = ll.getEllipsisCount(line-1);
            if(count > 0){
                //表示文字被折叠，此时需显示更多
                String sourceStr = tv.getText().toString();
                CharSequence temp = tv.getText().subSequence(0, ll.getLineEnd(line - 1) - count - 2);
                SpannableString mString = new SpannableString(temp+ "...更多");
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.MoreTextStyle), mString.length()-5, mString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(mString);
            }
        }catch (Exception e){

        }
    }

    /**
     * 显示当前动态的评论数据
     */
    private void showDynamicCommentData(LinearLayout ll_dynamic_comment_list, PostDetailBean.DataBean.CommentPageBean.ListBean bean,int position) {
        if (null == bean) {
            if (null != ll_dynamic_comment_list) {
                ll_dynamic_comment_list.setVisibility(View.GONE);
            }
            return;
        }
        ll_dynamic_comment_list.setVisibility(View.VISIBLE);
        if (null != ll_dynamic_comment_list) {
            ll_dynamic_comment_list.setVisibility(View.VISIBLE);
            //清除
            ll_dynamic_comment_list.removeAllViews();

            RelativeLayout comment_view = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_comment_view, null);
            CircleImageView civ_dynamic_comment_user_logo = comment_view.findViewById(R.id.civ_dynamic_comment_user_logo);
            EllipsizeTextView tv_dynamic_user_comment = comment_view.findViewById(R.id.tv_dynamic_user_comment);

            //判断状态，删除状态显示该内容已被删除
            if (bean.getAuditStatus() == 4) {
                SpannableString mString = new SpannableString(bean.getComUserName() + "：" + "该内容已被删除");
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName), 0, bean.getComUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.DeleteReplyContent), bean.getComUserName().length(), mString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_dynamic_user_comment.setText(mString);
            } else if (bean.getAuditStatus() == 5) {
                SpannableString mString = new SpannableString(bean.getComUserName() + "：" + "--该内容举报过多，连载客服正在审核中--");
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName), 0, bean.getComUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.DeleteReplyContent), bean.getComUserName().length(), mString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_dynamic_user_comment.setText(mString);
            }else {
//                SpannableString mString = new SpannableString(bean.getComUserName() + "：" + bean.getContentShow());
//                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName), 0, bean.getComUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tv_dynamic_user_comment.setText(mString);
                URLUtils.replaceBookForNick(bean.getComUserName(),bean.getContentShow().replace("\n"," "),mContext,tv_dynamic_user_comment,bean.getUrlTitle());
            }

            RxImageTool.loadLogoImage(mContext, bean.getComUsrePic(), civ_dynamic_comment_user_logo);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 0);

            tv_dynamic_user_comment.setOnClickListener(
                    v -> {
                        getDynamicClickListener().commentClick(position);
                    }
            );

            ll_dynamic_comment_list.addView(comment_view, layoutParams);
        }
    }

    /**
     * 动态图片显示
     * 规则-1-3张图片，按显示区域等比例显示 4张图：每行显示两张 5张图：第一行显示两张 第二行显示三张 6张图：每行显示三张 7张图：第一行显示四张 第二行显示三张 8张图：每行显示四张 9张图：每行显示三张
     *
     * @param ll_dynamic_images
     */
    private void showDynamicImage(LinearLayout ll_dynamic_images, List<String> images, int position) {
        if (null != ll_dynamic_images) {

            int imageCount = images.size();
            int imageSize = 0;//图片尺寸
            int radius = 4;

            RxLogTool.e("imageCount:" + imageCount);

            if (imageCount == 0) {
                ll_dynamic_images.setVisibility(View.GONE);
            } else {
                ll_dynamic_images.setVisibility(View.VISIBLE);
                ll_dynamic_images.removeAllViews();

                int imageLine = 1;//默认一行

                if (imageCount == 4 || imageCount == 5 || imageCount == 6 || imageCount == 7 || imageCount == 8) {//两行 4张图：每行显示两张 6张图：每行显示三张 8张图：每行显示四张 9张图 每行显示三张
                    imageLine = 2;
                } else if (imageCount == 9) {//三行
                    imageLine = 3;
                }

                for (int i = 0; i < imageLine; i++) {
                    LinearLayout lineView = new LinearLayout(mContext);
                    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lineView.setOrientation(LinearLayout.HORIZONTAL);
                    lineParams.setMargins(0, 6, 0, 0);
                    lineView.setLayoutParams(lineParams);

                    int lineImageCount = 0;

                    if (imageCount == 1) {
                        imageSize = contentWidth / 2 + 30;
                        SelectableRoundedImageView imgView = getRoundImageView(radius, radius, radius, radius, imageSize, false);
                        ;
                        RxImageTool.loadImage(mContext, images.get(0), imgView);
                        lineView.addView(imgView, 0);

                        if (null != getDynamicClickListener()) {
                            imgView.setOnClickListener(
                                    v -> {
                                        getDynamicClickListener().imageDynamicClick(0, position);
                                    }
                            );
                        }
                    } else if (imageCount == 2) {
                        lineImageCount = 2;
                        imageSize = contentWidth / 2;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = i + j;
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }

                    } else if (imageCount == 3) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }

                    } else if (imageCount == 4) {
                        lineImageCount = 2;
                        imageSize = contentWidth / (lineImageCount + 1);

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }

                    } else if (imageCount == 5) {
                        if (i == 0) {//第一行
                            lineImageCount = 2;
                            imageSize = contentWidth / lineImageCount + 3;//第二行比第一行多一个间距 6，间距分配到图片宽度里面，故取3
                        } else if (i == 1) {
                            lineImageCount = 3;
                            imageSize = contentWidth / lineImageCount;
                        }

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition;
                            if (i == 0) {
                                imagePosition = j * (i + 1);
                            } else {
                                imagePosition = j + 2;
                            }
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxLogTool.e("imagePosition:" + imagePosition);
                            //加载图片
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);

                            //添加到父容器中
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }
                    } else if (imageCount == 6) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 5) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }

                    } else if (imageCount == 7) {
                        if (i == 0) {//第一行
                            lineImageCount = 4;
                            imageSize = contentWidth / lineImageCount - 1;
                        } else if (i == 1) {
                            lineImageCount = 3;
                            imageSize = contentWidth / lineImageCount;//第二行比第一行多一个间距 6，间距分配到图片宽度里面，每个图片宽度增加2
                        }

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition;
                            if (i == 0) {
                                imagePosition = j;
                            } else {
                                imagePosition = j + 4;
                            }
                            RxLogTool.e("-----imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 2 || imagePosition == 5) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, false);
                            }
                            //加载图片
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);

                            //添加到父容器中
                            lineView.addView(imgView, j);
                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }
                    } else if (imageCount == 8) {
                        lineImageCount = 4;
                        imageSize = contentWidth / 4 - 3;
                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*4)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 2 || imagePosition == 5 || imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 7) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);

                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }
                    } else if (imageCount == 9) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;
                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 3 || imagePosition == 4 || imagePosition == 5 || imagePosition == 7) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 8) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(mContext, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);

                            if (null != getDynamicClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getDynamicClickListener().imageDynamicClick(imagePosition, position)
                                );
                            }
                        }

                    }

                    ll_dynamic_images.addView(lineView);
                }
            }

        }

    }

    /**
     * @param leftTop
     * @param rightTop
     * @param leftBottom
     * @param rightBottom
     * @param imageSize
     * @param isLastItem  是否是当前行的最后一个元素
     * @return
     */
    private SelectableRoundedImageView getRoundImageView(float leftTop, float rightTop, float leftBottom, float rightBottom, int imageSize, boolean isLastItem) {
        SelectableRoundedImageView imgView = new SelectableRoundedImageView(mContext);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setBackgroundResource(R.drawable.v2_add_pic_bg_corner);
        imgView.setBorderColor(Color.TRANSPARENT);
        imgView.setCornerRadiiDP(leftTop, rightTop, leftBottom, rightBottom);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
        if (!isLastItem) {
            params.setMargins(0, 0, 6, 0);
        } else {
            params.setMargins(0, 0, 0, 0);
        }

        imgView.setLayoutParams(params);
        return imgView;
    }


    public interface DynamicClickListener {
        void contentDynamicClick(int position);

        void headClick(int position);

        void praiseClick(int position);

        void commentClick(int position);

        void imageDynamicClick(int currentIndex, int position);//书籍或周报跳网页的动作

        void moreOptionClick(int position, View view);

        void closeClick(int position);
    }

}
