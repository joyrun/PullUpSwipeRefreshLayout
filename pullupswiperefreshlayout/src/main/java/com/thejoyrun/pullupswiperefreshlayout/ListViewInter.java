package com.thejoyrun.pullupswiperefreshlayout;

import android.view.View;

/**
 * Created by keven on 16/8/25.
 */

public interface ListViewInter {

    boolean isAdapterExist();

    int getListLastVisiblePosition();

    int getListItemCount();

    void addFooterItem(View v);

    View getListChildAt(int index);

    int getListHeaderViewsCount();

    int getListFooterViewsCount();

    void setListEmptyView(View v);

    void removeEmptyView();
}
