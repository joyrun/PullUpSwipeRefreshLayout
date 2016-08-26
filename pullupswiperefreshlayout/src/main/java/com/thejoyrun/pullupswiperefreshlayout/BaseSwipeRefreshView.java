package com.thejoyrun.pullupswiperefreshlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.thejoyrun.pullupswiperefreshlayout.list.ListViewV2;

/**
 * Created by keven on 16/8/26.
 */

public abstract class BaseSwipeRefreshView extends RelativeLayout {

    protected EmptyView mEmptyView;
    protected PullUpSwipeRefreshLayout mPullUpSwipeRefreshLayout;

    public BaseSwipeRefreshView(Context context) {
        super(context, null, 0);
    }

    public BaseSwipeRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSwipeRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutId(), null);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(view, layoutParams);
        initView(context, view);
    }

    protected abstract int getLayoutId();


    protected void initView(Context context, View view){
        mEmptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mPullUpSwipeRefreshLayout = (PullUpSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
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
        setFooterViewShow(mLoadEnabled);
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

    public void setFooterViewShow(boolean toShow){
        mPullUpSwipeRefreshLayout.setFooterViewShow(toShow);
    }
}
