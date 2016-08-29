package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.thejoyrun.pullupswiperefreshlayout.BaseSwipeRefreshView;
import com.thejoyrun.pullupswiperefreshlayout.EmptyView;
import com.thejoyrun.pullupswiperefreshlayout.R;

/**
 * Created by keven on 16/8/24.
 */

public class SwipeRefreshRecyclerView extends BaseSwipeRefreshView {

    private ListRecyclerView mRecyclerView;

    public SwipeRefreshRecyclerView(Context context) {
        this(context, null, 0);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_pus_swipe_refresh_recyclerview;
    }

    @Override
    protected void initView(Context context,View view) {
        super.initView(context, view);
        mRecyclerView = (ListRecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPullUpSwipeRefreshLayout.setListViewAndEmptyView(mRecyclerView, mEmptyView);
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public ListRecyclerView getListRecyclerView() {
        return mRecyclerView;
    }
}
