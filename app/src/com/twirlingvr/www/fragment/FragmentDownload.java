package com.twirlingvr.www.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

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

/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class FragmentDownload extends Fragment {
    private MainAdapter mAdapter = null;
    XRecyclerView mRecyclerView;
    private List<List<String>> datas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_download, null);
        initView(rootView);
    }

    private void initView(View view) {
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.mRecyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
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
                loadData(1);
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
                datas.clear();
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
}