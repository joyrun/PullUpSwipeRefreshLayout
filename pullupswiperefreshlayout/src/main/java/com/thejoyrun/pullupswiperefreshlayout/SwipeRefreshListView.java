package com.thejoyrun.pullupswiperefreshlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Wiki on 16/7/27.
 */

public class SwipeRefreshListView extends RelativeLayout {
    private EmptyView mEmptyView;
    private ListView mListView;
    private PullUpSwipeRefreshLayout mPullUpSwipeRefreshLayout;

    public SwipeRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_swipe_refresh_list_view, null);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(view, layoutParams);
        mEmptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mPullUpSwipeRefreshLayout = (PullUpSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mPullUpSwipeRefreshLayout.setListViewAndEmptyView(mListView, mEmptyView);
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public ListView getListView() {
        return mListView;
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
        mPullUpSwipeRefreshLayout.setLoading(b);
    }
}
