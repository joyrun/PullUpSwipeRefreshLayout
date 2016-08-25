package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.thejoyrun.pullupswiperefreshlayout.EmptyView;
import com.thejoyrun.pullupswiperefreshlayout.PullUpSwipeRefreshLayout;
import com.thejoyrun.pullupswiperefreshlayout.R;

/**
 * Created by keven on 16/8/24.
 */

public class SwipeRefreshRecyclerView extends RelativeLayout {

    private EmptyView mEmptyView;
    private ListRecyclerView mRecyclerView;
    private PullUpSwipeRefreshLayout mPullUpSwipeRefreshLayout;

    public SwipeRefreshRecyclerView(Context context) {
        this(context,null,0);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_swipe_refresh_recyclerview, null);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(view, layoutParams);
        mEmptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mRecyclerView = (ListRecyclerView) view.findViewById(R.id.recyclerview);
        mPullUpSwipeRefreshLayout = (PullUpSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

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

    public PullUpSwipeRefreshLayout getPullUpSwipeRefreshLayout() {
        return mPullUpSwipeRefreshLayout;
    }

    public void setOnLoadListener(PullUpSwipeRefreshLayout.OnLoadListener loadListener) {
        mPullUpSwipeRefreshLayout.setOnLoadListener(loadListener);
    }

    public void setRefreshing(boolean refreshing) {
        mPullUpSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mPullUpSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setRefreshEnabled(boolean mRefreshEnabled) {
        mPullUpSwipeRefreshLayout.setRefreshEnabled(mRefreshEnabled);
    }

    public boolean isRefreshEnabled() {
        return mPullUpSwipeRefreshLayout.isRefreshEnabled();
    }

    public void setLoadEnabled(boolean mLoadEnabled) {
        mPullUpSwipeRefreshLayout.setLoadEnabled(mLoadEnabled);
    }

    public boolean isLoadEnabled() {
        return mPullUpSwipeRefreshLayout.isLoadEnabled();
    }

    public void setLoadAutoEnabled(boolean loadAutoEnabled) {
        mPullUpSwipeRefreshLayout.setLoadAutoEnabled(loadAutoEnabled);
    }

    public boolean isLoadAutoEnabled() {
        return mPullUpSwipeRefreshLayout.isLoadAutoEnabled();
    }

    public void setLoading(boolean b) {
        mPullUpSwipeRefreshLayout.
                setLoading(b);
    }

}
