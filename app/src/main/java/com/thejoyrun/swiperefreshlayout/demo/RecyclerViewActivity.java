package com.thejoyrun.swiperefreshlayout.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thejoyrun.pullupswiperefreshlayout.PullUpSwipeRefreshLayout;
import com.thejoyrun.pullupswiperefreshlayout.list.SwipeRefreshListView;
import com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerView;
import com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerViewAdapter;
import com.thejoyrun.pullupswiperefreshlayout.recycler.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by keven on 16/8/25.
 */

public class RecyclerViewActivity extends Activity {

    private SwipeRefreshRecyclerView refresh_layout;
    private RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        refresh_layout = (SwipeRefreshRecyclerView) findViewById(R.id.swipe_refresh_list_view);

        mRecyclerAdapter = new RecyclerAdapter(this);

        refresh_layout.getListRecyclerView().setRecyclerAdapter(mRecyclerAdapter);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerAdapter.clear();
                        addData();
                        refresh_layout.setRefreshing(false);
                    }
                },2000);
            }
        });
        refresh_layout.setOnLoadListener(new PullUpSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        refresh_layout.setLoading(false);
                        refresh_layout.setLoadEnabled(false);
                    }
                },2000);
            }
        });
        refresh_layout.setLoadAutoEnabled(false);



        refresh_layout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerAdapter.clear();
                addData();
                refresh_layout.setRefreshing(false);
            }
        },5000);
    }


    private void addData(){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("item : " + new Random().nextInt(555));
        }
        mRecyclerAdapter.addData(strings);
    }

    public class RecyclerAdapter extends ListRecyclerViewAdapter{

        private ArrayList<String> stringDatas = new ArrayList<>();


        public RecyclerAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder onCreateViewContentHolder(ViewGroup parent, int viewType) {

            View inflate = getLayoutInflater().inflate(R.layout.item_sample, parent, false);
            return new BaseViewHolder(inflate);
        }

        @Override
        public void onBindViewContentHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textview = (TextView) holder.itemView.findViewById(R.id.text);
            textview.setText(stringDatas.get(position));
        }

        @Override
        public int getItemType(int position) {
            return 1;
        }

        @Override
        public int getListCount() {
            return stringDatas.size();
        }

        public void addData(ArrayList<String> strings){
            stringDatas.addAll(strings);
            notifyDataSetChanged();
        }

        public void clear(){
            stringDatas.clear();
            notifyDataSetChanged();
        }
    }
}
