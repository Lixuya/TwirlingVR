package com.twirlingvr.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.twirlingvr.www.R;
import com.twirlingvr.www.adapter.MainAdapter;
import com.twirlingvr.www.model.DataArray;
import com.twirlingvr.www.net.Api;
import com.twirlingvr.www.net.RequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainAdapter mAdapter = null;
    //    @Bind(R.id.mRecyclerview)
    XRecyclerView mRecyclerView;
    private int page = 1;
    private List<List<String>> datas = new ArrayList<>();

    //
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, ListShowActivity.class));
                return false;
            }
        });
        //
        mRecyclerView = (XRecyclerView) findViewById(R.id.mRecyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1);
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getBaseContext()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshComplete();
                page = 1;
                loadData(page);
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
//                page += 1;
//                loadData(page);
            }
        });
        mAdapter = new MainAdapter(datas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(final int page) {
        HashMap<String, Object> params = new HashMap<>();
        //HttpParamsHelper.createParams();
        params.put("id", 0);
        params.put("group", "pub");
        params.put("top", 100);
        Api.getRetrofit().getVideoList(params).enqueue(new RequestCallback<DataArray>() {
            @Override
            public void onSuccess(DataArray dataArray) {
//                if (response == null || !TextUtil.isValidate(response.data)) {
//                    return;
//                }
                datas.clear();
//                DataArray dataArray = response.getDataFrist();
                List<List<String>> movieList = dataArray.getContent();
                datas.addAll(movieList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                if (page == 1) {
                    mRecyclerView.refreshComplete();
                } else {
                    mRecyclerView.loadMoreComplete();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}