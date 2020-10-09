package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.ReportAuditListBean;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 圈子审核举报内容item
 */

public class CircleReportAuditItemAdapter extends BaseQuickAdapter<ReportAuditListBean.DataBean.ListBean, BaseViewHolder> {


    Context context;
    int contentWidth;

    private OnPersonClickListener onPersonClickListener;

    public OnPersonClickListener getOnPersonClickListener() {
        return onPersonClickListener;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }

    public CircleReportAuditItemAdapter(@Nullable List<ReportAuditListBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_report_audit, data);
        this.context = mContext;
        contentWidth = RxDeviceTool.getScreenWidth(mContext) - RxImageTool.dip2px(32);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,ReportAuditListBean.DataBean.ListBean bean) {

        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_mute = baseViewHolder.getView(R.id.tv_mute);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);

//        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);

        TextView report_count = baseViewHolder.getView(R.id.report_count);
        TextView comment_count = baseViewHolder.getView(R.id.comment_count);
        TextView praise_count = baseViewHolder.getView(R.id.praise_count);
        TextView ignore_tv = baseViewHolder.getView(R.id.ignore_tv);
        TextView delete_comment_tv = baseViewHolder.getView(R.id.delete_comment_tv);


//        if(bean.getGender() == 0){
//            iv_gender.setImageDrawable(nanDrawable);
//        }else {
//            iv_gender.setImageDrawable(nvDrawable);
//        }

        RxImageTool.loadLogoImage(context,bean.getHeadPortrait(),iv_logo);
        tv_nickname.setText(bean.getNikeName());
        tv_des.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getCreateTime())));
        report_count.setText(String.valueOf(bean.getBeReportCount()) + " >");
        comment_count.setText(String.valueOf(bean.getBeCommentCount()));
        praise_count.setText(String.valueOf(bean.getBeLikeCount()));

        iv_logo.setOnClickListener(
                v->onPersonClickListener.avatorClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );

        tv_mute.setOnClickListener(
                v->onPersonClickListener.muteClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        ignore_tv.setOnClickListener(
                v->onPersonClickListener.ignoreClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        delete_comment_tv.setOnClickListener(
                v->onPersonClickListener.deleteClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        report_count.setOnClickListener(
                v->onPersonClickListener.detailClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );


        TextView tv_dynamic_content_text = baseViewHolder.getView(R.id.tv_dynamic_content_text);
        //图片展示区域
        LinearLayout ll_dynamic_images = baseViewHolder.getView(R.id.ll_dynamic_images);

        //文字内容
        if(!TextUtils.isEmpty(bean.getContent())) {
            tv_dynamic_content_text.setVisibility(View.VISIBLE);
            URLUtils.replaceBook(bean.getContent(),mContext,tv_dynamic_content_text,bean.getUrlTitle());
        }else {
            tv_dynamic_content_text.setVisibility(View.GONE);
            tv_dynamic_content_text.setText("");
        }

            ll_dynamic_images.setVisibility(View.VISIBLE);
            if (null != bean.getThumbnailImages() && !bean.getThumbnailImages().isEmpty()) {
                showDynamicImage(ll_dynamic_images, bean.getThumbnailImages(), baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            } else {
                ll_dynamic_images.setVisibility(View.GONE);
            }


    }

    public interface OnPersonClickListener{
        void muteClick(int pos);
        void avatorClick(int pos);
        void ignoreClick(int pos);
        void deleteClick(int pos);
        void detailClick(int pos);
        void imageClick(int index,int pos);
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

                        if (null != getOnPersonClickListener()) {
                            imgView.setOnClickListener(
                                    v -> {
                                        getOnPersonClickListener().imageClick(0, position);
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v -> getOnPersonClickListener().imageClick(imagePosition, position)
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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
                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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

                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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

                            if (null != getOnPersonClickListener()) {
                                imgView.setOnClickListener(
                                        v ->
                                                getOnPersonClickListener().imageClick(imagePosition, position)
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

}
