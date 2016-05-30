package com.twirlingvr.www.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.twirlingvr.www.R;
import com.twirlingvr.www.adapter.MainAdapter;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class FragmentDownload extends Fragment {
    private MainAdapter mAdapter = null;
    private XRecyclerView mRecyclerView = null;
    private List<VideoItem> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
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
                loadData();
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
//                page += 1;
            }
        });
        mAdapter = new MainAdapter(datas, true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        datas.clear();
        datas.addAll(RealmHelper.getIns().selectVideoList());
        mAdapter.notifyDataSetChanged();
    }
}