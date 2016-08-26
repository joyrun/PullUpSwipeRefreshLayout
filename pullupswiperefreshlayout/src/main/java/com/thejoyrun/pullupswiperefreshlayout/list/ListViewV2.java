package com.thejoyrun.pullupswiperefreshlayout.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.thejoyrun.pullupswiperefreshlayout.EmptyView;
import com.thejoyrun.pullupswiperefreshlayout.ListViewInter;

/**
 * Created by keven on 16/8/25.
 */

public class ListViewV2 extends ListView implements ListViewInter {
    public ListViewV2(Context context) {
        this(context, null, 0);
    }

    public ListViewV2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isAdapterExist() {
        return null != getAdapter() ? true : false;
    }

    @Override
    public int getListLastVisiblePosition() {
        return getLastVisiblePosition();
    }

    @Override
    public int getListItemCount() {
        return getCount();
    }

    @Override
    public void addFooterItem(View v) {
        addFooterView(v);
    }

    @Override
    public View getListChildAt(int index) {
        return getChildAt(index);
    }

    @Override
    public int getListHeaderViewsCount() {
        return getHeaderViewsCount();
    }

    @Override
    public int getListFooterViewsCount() {
        return getFooterViewsCount();
    }

    @Override
    public void setListEmptyView(View v) {
        setEmptyView(v);
    }
}
