package com.twirling.SDTL.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.AudioItem;
import com.twirling.audio.player.OpenMXPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    //
    private List<AudioItem> datas = new ArrayList<AudioItem>();
    private OpenMXPlayer openMXPlayer = null;
    private boolean isPaused = true;
    private boolean isPlaying = false;

    public AudioAdapter(List<AudioItem> datas) {
        this.datas = datas;
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setProfileId(0);
        openMXPlayer.setAudioIndex(0);
    }

    private void togglePause() {
        if (isPaused) {
            openMXPlayer.play();
            isPaused = false;
        } else {
            openMXPlayer.stop();
            isPaused = true;
        }
    }

    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ll_item_audio, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AudioAdapter.ViewHolder holder, int position) {
        final AudioItem item = datas.get(position);
        String path = item.getCover();
        Glide.with(holder.itemView.getContext()).load(path).into(holder.iv_background);
        holder.tv_title.setText(item.getTitle());
        holder.audio = item.getAudio();
        RxView.clicks(holder.iv_play)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return isPlaying == false;
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isPlaying = true;
                        holder.iv_play.setVisibility(View.INVISIBLE);
                        holder.iv_stop.setVisibility(View.VISIBLE);
                        openMXPlayer.setDataSource(holder.audio);
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
        RxView.clicks(holder.iv_stop)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return isPlaying == true;
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isPlaying = false;
                        holder.iv_stop.setVisibility(View.INVISIBLE);
                        holder.iv_play.setVisibility(View.VISIBLE);
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
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
        @BindView(R.id.iv_stop)
        ImageView iv_stop;
        @BindView(R.id.cv_card)
        CardView cv_card;
        @BindView(R.id.tv_title)
        TextView tv_title;
        public String audio = "";

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Drawable icon = new IconicsDrawable(view.getContext())
                    .icon(FontAwesome.Icon.faw_play_circle)
                    .color(Color.parseColor("#FFFFFF"))
                    .sizeDp(25);
            iv_play.setImageDrawable(icon);
            Drawable icon2 = new IconicsDrawable(view.getContext())
                    .icon(FontAwesome.Icon.faw_pause)
                    .color(Color.parseColor("#FFFFFF"))
                    .sizeDp(25);
            iv_stop.setImageDrawable(icon2);
        }
    }
}