package com.twirlingvr.www.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twirlingvr.www.R;
import com.twirlingvr.www.activity.ListShowActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    // 初始列表项数量
    private int count = 20;
    private String imagePath = "http://www.twirlingvr.com/App_Media/videos/";
    //
    private List<List<String>> datas = new ArrayList<List<String>>();

    public MainAdapter(List<List<String>> datas) {
        this.datas = datas;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ll_item_movie, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, int position) {
        final List<String> item = datas.get(position);
        String imageName = item.get(6);
        String title = item.get(2);
        Glide.with(holder.itemView.getContext()).load(imagePath + imageName).into(holder.iv_background);
        holder.tv_title.setText(title);
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ListShowActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_background;
        private final CardView cv_card;
        private final TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            iv_background = (ImageView) view.findViewById(R.id.iv_background);
            cv_card = (CardView) view.findViewById(R.id.cv_card);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}