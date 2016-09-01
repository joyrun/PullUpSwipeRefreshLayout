package com.thejoyrun.pullupswiperefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Wiki on 16/7/27.
 */
public class EmptyView extends RelativeLayout {

    private View empty_refresh;
    private View empty_tips;
    private EmptyViewListener mEmptyViewListener;

    public EmptyView(Context context) {
        this(context, null, 0);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_pus_refresh_empty, null);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(view, layoutParams);
        empty_tips = findViewById(R.id.empty_tips);
        empty_refresh = findViewById(R.id.empty_refresh);
        empty_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmptyViewListener != null) {
                    mEmptyViewListener.onRefresh();
                }
            }
        });
    }

    public void setRefreshing(boolean show) {
        empty_tips.setVisibility(!show ? VISIBLE : GONE);
        empty_refresh.setVisibility(!show ? VISIBLE : GONE);
    }

    public EmptyViewListener getEmptyViewListener() {
        return mEmptyViewListener;
    }

    public void setEmptyViewListener(EmptyViewListener emptyViewListener) {
        this.mEmptyViewListener = emptyViewListener;
    }
}
