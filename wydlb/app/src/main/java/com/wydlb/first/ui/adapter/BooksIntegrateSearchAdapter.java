package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.IntegrateSearchBean;
import com.wydlb.first.bean.IntegrateSearchResponse;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2017/10/13.
 * 综合搜索
 */

public class BooksIntegrateSearchAdapter extends BaseQuickAdapter<IntegrateSearchBean,BaseViewHolder> {


    IntegrateClickListener integrateClickListener;

    String mKeyword="";
    Context context;
    public BooksIntegrateSearchAdapter(@Nullable List<IntegrateSearchBean> data, Context mContext) {
        super(R.layout.item_search_integrate, data);
        this.context=mContext;
    }

    public IntegrateClickListener getIntegrateClickListener() {
        return integrateClickListener;
    }

    public void setIntegrateClickListener(IntegrateClickListener integrateClickListener) {
        this.integrateClickListener = integrateClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,IntegrateSearchBean bean) {

        View line1=baseViewHolder.getView(R.id.line1);//分割线

        TextView tv_book_list_more=baseViewHolder.getView(R.id.tv_book_list_more);
        TextView tv_book_more=baseViewHolder.getView(R.id.tv_book_more);

        LinearLayout ll_book_title_view=baseViewHolder.getView(R.id.ll_book_title_view);

        RelativeLayout rl_book_view=baseViewHolder.getView(R.id.rl_book_view);

        LinearLayout ll_book_list_title_view=baseViewHolder.getView(R.id.ll_book_list_title_view);
        RelativeLayout rl_book_list_content_view=baseViewHolder.getView(R.id.rl_book_list_content_view);

        if (bean.getTag()== Constant.IIntegrateSearchType.BOOK_TITLE_TYPE){//书籍标题
            ll_book_title_view.setVisibility(View.VISIBLE);

            line1.setVisibility(View.GONE);
            rl_book_view.setVisibility(View.GONE);
            ll_book_list_title_view.setVisibility(View.GONE);
            rl_book_list_content_view.setVisibility(View.GONE);

        }else if(bean.getTag()==Constant.IIntegrateSearchType.BOOK_LIST_TITLE_TYPE){//书单标题
            ll_book_list_title_view.setVisibility(View.VISIBLE);

            line1.setVisibility(View.GONE);
            rl_book_view.setVisibility(View.GONE);
            ll_book_title_view.setVisibility(View.GONE);
            rl_book_list_content_view.setVisibility(View.GONE);
        }else if(bean.getTag()==Constant.IIntegrateSearchType.BOOK_TYPE){//书籍
            rl_book_view.setVisibility(View.VISIBLE);

            line1.setVisibility(View.GONE);
            ll_book_list_title_view.setVisibility(View.GONE);
            ll_book_title_view.setVisibility(View.GONE);
            rl_book_list_content_view.setVisibility(View.GONE);

            if (null!=bean.getPlatformBean()){
                showBookData(baseViewHolder,bean.getPlatformBean());
            }

        }else if(bean.getTag()==Constant.IIntegrateSearchType.BOOK_LIST_TYPE){//书单
            rl_book_list_content_view.setVisibility(View.VISIBLE);

            line1.setVisibility(View.GONE);
            rl_book_view.setVisibility(View.GONE);
            ll_book_title_view.setVisibility(View.GONE);
            ll_book_list_title_view.setVisibility(View.GONE);

            if (null!=bean.getBooklistBean()){
                showBookListData(baseViewHolder,bean.getBooklistBean());
            }
        }else if(bean.getTag()==Constant.IIntegrateSearchType.LINE_TYPE) {
            line1.setVisibility(View.VISIBLE);

            rl_book_list_content_view.setVisibility(View.GONE);
            rl_book_view.setVisibility(View.GONE);
            ll_book_title_view.setVisibility(View.GONE);
            ll_book_list_title_view.setVisibility(View.GONE);
        }

        tv_book_list_more.setOnClickListener(
                v->{
                    getIntegrateClickListener().bookListMoreClick();
                }
        );
        tv_book_more.setOnClickListener(
                v->{
                    getIntegrateClickListener().bookMoreClick();
                }
        );


        rl_book_list_content_view.setOnClickListener(
                v->{
                    getIntegrateClickListener().bookListItemClick(String.valueOf(bean.getBooklistBean().getId()));
                }
        );

        rl_book_view.setOnClickListener(
                v->{
                    getIntegrateClickListener().bookItemClick(bean.getPlatformBean());
                }
        );



    }

    /**
     * 书籍数据
     * @param baseViewHolder
     * @param bean
     */
    private void showBookData(BaseViewHolder baseViewHolder,IntegrateSearchResponse.DataBean.PlatformBean bean){
        final SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getPlatformCover(),roundedImageView);

//        TextView tv_is_add=baseViewHolder.getView(R.id.tv_is_add);

        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvUpdateChapter=baseViewHolder.getView(R.id.tv_book_update_chapter);
        TextView tv_author=baseViewHolder.getView(R.id.tv_author);
        ImageView gongxiangbanquan_jiaobiao = baseViewHolder.getView(R.id.gongxiangbanquan_jiaobiao);

        if(bean.isCopyright()){
            gongxiangbanquan_jiaobiao.setVisibility(View.VISIBLE);
        }else {
            gongxiangbanquan_jiaobiao.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(bean.getPenName())) {
            tv_author.setText("");
        }

//        if (bean.getIsConcern()){//已关注
//            tv_is_add.setVisibility(View.VISIBLE);
//        }else {
//            tv_is_add.setVisibility(View.GONE);
//        }

        tvUpdateChapter.setText(bean.getPlatformIntroduce());

        try{
            if (!TextUtils.isEmpty(bean.getPenName())) {
                tv_author.setText("");
                setTextColor(tv_author,mKeyword,new SpannableString(bean.getPenName().toUpperCase()),new SpannableString(bean.getPenName()));
            }

            setTextColor(tvBookTitle,mKeyword,new SpannableString(bean.getPlatformName().toUpperCase()),new SpannableString(bean.getPlatformName()));
        }catch (Exception e){
            tvBookTitle.setText(bean.getPlatformName());

            if (TextUtils.isEmpty(bean.getPenName())) {
                tv_author.setText("");
            }
        }

    }

    private void showBookListData(BaseViewHolder baseViewHolder,IntegrateSearchResponse.DataBean.BooklistBean bean){
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView tv_book_list_name=baseViewHolder.getView(R.id.tv_book_list_name);
        TextView iv_book_list_description=baseViewHolder.getView(R.id.iv_book_list_description);
        TextView iv_book_list_more=baseViewHolder.getView(R.id.iv_book_list_more);
        TextView tv_update=baseViewHolder.getView(R.id.tv_update);

        tv_book_list_name.setText(bean.getBooklistName());
        RxImageTool.loadImage(context,bean.getCover(),iv_book_list_cover);
        iv_book_list_description.setText(bean.getBooklistIntro());

        String moreStr=(TextUtils.isEmpty(bean.getBooklistAuthor())?"":bean.getBooklistAuthor())+(bean.isCollectionNumShow()?" | "+bean.getCollectionNum()+"人收藏":"");
        iv_book_list_more.setText(moreStr);

        if (bean.getIsUnread()){
            tv_update.setVisibility(View.VISIBLE);
        }else{
            tv_update.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mKeyword)){
            try{
                if (!TextUtils.isEmpty(bean.getBooklistName())) {
                    tv_book_list_name.setText("");
                    setTextColor(tv_book_list_name,mKeyword,new SpannableString(bean.getBooklistName().toUpperCase()),new SpannableString(bean.getBooklistName()));
                }

                if (!TextUtils.isEmpty(bean.getPlatformName())){
                    setTextColor(iv_book_list_description,mKeyword,new SpannableString(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName())),new SpannableString(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName())));
                }
//            setTextColor(iv_book_list_description,mKeyword,new SpannableString(bean.getBooklistIntro().toUpperCase()),new SpannableString(bean.getBooklistIntro()));
            }catch (Exception e){
                if (!TextUtils.isEmpty(bean.getPlatformName())){
                    iv_book_list_description.setText(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName()));
                }

                if (TextUtils.isEmpty(bean.getBooklistName())) {
                    tv_book_list_name.setText("");
                }
            }
        }

    }


    public void search(String keyword){
        mKeyword=keyword;
        notifyDataSetChanged();
    }

    private void setTextColor(TextView tv, String keyword, SpannableString ss,SpannableString showss) {
        if (TextUtils.isEmpty(keyword))return;
        Pattern p = Pattern.compile(keyword.toUpperCase());
        RxLogTool.e("setTextColor keyword："+keyword);

        Matcher m = p.matcher(ss);

        RxLogTool.e("setTextColor ss："+ss);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            showss.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(showss);
    }


    public interface IntegrateClickListener{
        void bookMoreClick();
        void bookItemClick(IntegrateSearchResponse.DataBean.PlatformBean platformBean);
        void bookListItemClick(String bookListId);
        void bookListMoreClick();
    }
}
