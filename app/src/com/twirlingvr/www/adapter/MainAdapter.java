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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twirlingvr.www.App;
import com.twirlingvr.www.R;
import com.twirlingvr.www.activity.PlayLoadActivity;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //
    private List<VideoItem> datas = new ArrayList<VideoItem>();
    // true 有删除按钮
    private boolean flag = false;

    public MainAdapter(List<VideoItem> datas) {
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
        final VideoItem item = datas.get(position);
        String imageName = item.getImage();
        String title = item.getName();
        Glide.with(holder.itemView.getContext()).load(Constants.PAPH_IMAGE + imageName).into(holder.iv_background);
        holder.tv_title.setText(title);
//        holder.iv_delete.setBackgroundResource(R.drawable.download);
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                Intent intent = new Intent(holder.itemView.getContext(), PlayLoadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("videoItem", item);
                intent.putExtras(bundle);
                //
                ActivityOptions transitionActivityOptions = null;
                String ti = holder.itemView.getContext().getString(R.string.ti);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Activity activity = App.getInst().getCurrentShowActivity();
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, holder.iv_background, ti);
                    holder.itemView.getContext().startActivity(intent, transitionActivityOptions.toBundle());
                } else {
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_background,
                iv_delete;
        private CardView cv_card = null;
        private TextView tv_title = null;

        public ViewHolder(View view) {
            super(view);
            iv_background = (ImageView) view.findViewById(R.id.iv_background);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            cv_card = (CardView) view.findViewById(R.id.cv_card);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}