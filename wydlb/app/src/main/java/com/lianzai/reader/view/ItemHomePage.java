package com.lianzai.reader.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.bean.HomePageSettingBean;
import com.lianzai.reader.ui.activity.ActivityBookList;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.BookListGridAdapter;
import com.lianzai.reader.ui.adapter.HomePageGridAdapter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;

import java.util.ArrayList;
import java.util.List;

public class ItemHomePage extends RelativeLayout{

	private Context context;
	private HomePageSettingBean.DataBean.HeadBookListBean mdata;

	public ItemHomePage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemHomePage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemHomePage(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	TextView tit_tv2;
	RecyclerView rv_grid_recommend2;
	TextView tv_more_book_grid2;
	HomePageGridAdapter homePageGridAdapter;
	List<HomePageSettingBean.DataBean.HeadBookListBean.DetailListBean> recommendList2 = new ArrayList<>();

	protected void initView() {
		inflate(getContext(), R.layout.header_book_list_recommend, this);
		tit_tv2 = findViewById(R.id.tit_tv);
		tv_more_book_grid2 = findViewById(R.id.tv_more_book_grid);
		rv_grid_recommend2 = findViewById(R.id.rv_grid_recommend);

		homePageGridAdapter=new HomePageGridAdapter(recommendList2,getContext());
		GridLayoutManager gridLayoutManager2=new GridLayoutManager(getContext(),3);
		rv_grid_recommend2.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(8), RxImageTool.dip2px(10),0));
		rv_grid_recommend2.setLayoutManager(gridLayoutManager2);
		rv_grid_recommend2.setAdapter(homePageGridAdapter);
		homePageGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				ActivityCircleDetail.startActivity(getContext(),String.valueOf(homePageGridAdapter.getData().get(position).getPlatformId()));
			}
		});
		tv_more_book_grid2.setOnClickListener(
				v -> {
					ActivityBookListDetail.startActivity(getContext(),String.valueOf(mdata.getBookListId()));
				}
		);

	}

	public void bindData(HomePageSettingBean.DataBean.HeadBookListBean bean) {
		mdata = bean;
		tit_tv2.setText(bean.getModuleName());
		recommendList2 = bean.getDetailList();
		homePageGridAdapter.replaceData(recommendList2);
	}

}

