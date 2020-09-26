package com.lianzai.reader.view;


import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.lianzai.reader.R;


public final class ChapterLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_chapter_load_more;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
