package com.twirling.SDTL.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.twirling.SDTL.App;
import com.twirling.SDTL.R;
import com.twirling.SDTL.activity.Audio2Activity;
import com.twirling.SDTL.model.AudioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    //
    private List<AudioItem> datas = new ArrayList<AudioItem>();

    public AudioAdapter(List<AudioItem> datas) {
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
        final AudioItem item = datas.get(position);
        String path = item.getCover();
        Glide.with(holder.itemView.getContext()).load(path).into(holder.iv_background);
        holder.tv_title.setText(item.getTitle());
        holder.audio = item.getAudio();
        RxView.clicks(holder.cv_card)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(holder.itemView.getContext(), Audio2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("AudioItem", item);
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