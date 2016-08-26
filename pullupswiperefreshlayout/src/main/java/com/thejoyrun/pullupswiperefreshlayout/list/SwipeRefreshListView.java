package com.thejoyrun.pullupswiperefreshlayout.list;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.thejoyrun.pullupswiperefreshlayout.BaseSwipeRefreshView;
import com.thejoyrun.pullupswiperefreshlayout.EmptyView;
import com.thejoyrun.pullupswiperefreshlayout.PullUpSwipeRefreshLayout;
import com.thejoyrun.pullupswiperefreshlayout.R;
import com.thejoyrun.pullupswiperefreshlayout.list.ListViewV2;

/**
 * Created by Wiki on 16/7/27.
 */

public class SwipeRefreshListView extends BaseSwipeRefreshView {

    private ListViewV2 mListView;

    public SwipeRefreshListView(Context context) {
        this(context, null, 0);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_swipe_refresh_list_view;
    }

    @Override
    protected void initView(Context context,View view) {
        super.initView(context, view);
        mListView = (ListViewV2) view.findViewById(R.id.list_view);
        mPullUpSwipeRefreshLayout.setListViewAndEmptyView(mListView, mEmptyView);
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public ListView getListView() {
        return mListView;
    }
}