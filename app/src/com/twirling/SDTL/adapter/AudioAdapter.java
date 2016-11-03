package com.twirling.SDTL.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.activity.SimpleVrVideoActivity;
import com.twirling.SDTL.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    //
    private List<VideoItem> datas = new ArrayList<VideoItem>();

    public AudioAdapter(List<VideoItem> datas) {
        this.datas = datas;
    }

    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ll_item_audio, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AudioAdapter.ViewHolder holder, int position) {
        final VideoItem item = datas.get(position);
        String path = Constants.PATH_RESOURCE + item.getFolder() + Constants.PAPH_IMAGE + item.getImage();
        Glide.with(holder.itemView.getContext()).load(path).into(holder.iv_background);
        holder.tv_title.setText(item.getName());
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                int vrAudio = item.getVrAudio();
                if (vrAudio == -1) {
                    Intent intent = new Intent(holder.itemView.getContext(), SimpleVrVideoActivity.class);
                    intent.putExtra("videoItem", Constants.PAPH_DOWNLOAD_LOCAL + item.getAppAndroidOnline());
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
        @BindView(R.id.iv_background)
        ImageView iv_background;
        @BindView(R.id.iv_play)
        ImageView iv_play;
        @BindView(R.id.cv_card)
        CardView cv_card;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Drawable icon = new IconicsDrawable(view.getContext())
                    .icon(FontAwesome.Icon.faw_trash_o)
                    .color(Color.parseColor("#FFFFFF"))
                    .sizeDp(25);
            iv_play.setImageDrawable(icon);
        }
    }
}