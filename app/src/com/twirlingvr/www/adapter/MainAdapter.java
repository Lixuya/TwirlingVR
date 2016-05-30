package com.twirlingvr.www.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twirlingvr.www.R;
import com.twirlingvr.www.activity.PlayLoadActivity;
import com.twirlingvr.www.model.VideoItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    // 初始列表项数量
    private int count = 20;
    private String imagePath = "http://www.twirlingvr.com/App_Media/videos/";
    private String videoPath = "http://www.twirlingvr.com/App_Media/videos/";

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
        final VideoItem item = new VideoItem(datas.get(position));
        String imageName = item.getImageName();
        String title = item.getTitle();
        Glide.with(holder.itemView.getContext()).load(imagePath + imageName).into(holder.iv_background);
        holder.tv_title.setText(title);
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                Intent intent = new Intent(holder.itemView.getContext(), PlayLoadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoItem", (Serializable) item);
                intent.putExtra("videoItem", bundle);
                //
                ActivityOptions transitionActivityOptions = null;
                String ti = holder.itemView.getContext().getString(R.string.ti);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), holder.iv_background, ti);
                    holder.itemView.getContext().startActivity(intent, transitionActivityOptions.toBundle());
                } else {
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
//        holder.btn_down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new FileUtil().down( videoPath + videoUri, videoUri, null);
//                    }
//                }).start();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_background;
        private final CardView cv_card;
        private final TextView tv_title;
        private final Button btn_down;

        public ViewHolder(View view) {
            super(view);
            iv_background = (ImageView) view.findViewById(R.id.iv_background);
            cv_card = (CardView) view.findViewById(R.id.cv_card);
//            cv_card.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    tv_title.setVisibility(View.GONE);
//                    btn_down.setVisibility(View.VISIBLE);
//                    return false;
//                }
//            });
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            btn_down = (Button) view.findViewById(R.id.btn_down);
        }
    }
}