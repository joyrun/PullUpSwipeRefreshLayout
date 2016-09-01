package com.thejoyrun.pullupswiperefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by keven on 16/9/1.
 */

public class FooterView extends RelativeLayout {

    private TextView mTextView;
    private ProgressBar mProgressBar;

    public FooterView(Context context) {
        this(context, null, 0);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View inflate = inflate(context, R.layout.view_pus_refresh_loading_footer, this);

        inflate.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextView = (TextView) inflate.findViewById(R.id.pull_to_refresh_load_more_text);
        mProgressBar = (ProgressBar) inflate.findViewById(R.id.pull_to_refresh_load_progress);
//        this.addView(inflate);
    }

    public TextView getTextView(){
        return mTextView;
    }

    public ProgressBar getProgressBar(){
        return mProgressBar;
    }

    /**
     //         * @param visibility  加载圈是否显示
     //         * @param text  显示的文字
     //         */
        public void setProgressAndText(int visibility,String text){
            mProgressBar.setVisibility(visibility);
            mTextView.setText(text);
        }

}
