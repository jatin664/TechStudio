package com.example.jatin.techstudio;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    List<Data1> data1;

    public RecyclerAdapter(Context context, List<Data1> data1) {
        this.context = context;
        this.data1 = data1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_items,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Data1 data=data1.get(i);

        viewHolder.newsTitle.setText(data.d1_title);

        //viewHolder.view.setVisibility(View.VISIBLE);

        Crashlytics.log(Log.DEBUG, "Recycler Adapter", "Crash in onBindViewHolder method");


        Glide.with(context).load("https://jatinkumar664.000webhostapp.com/images/"+data.d1_newsImage)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w=resource.getIntrinsicWidth();
                        int h=resource.getIntrinsicHeight();
                        if(w<=h){
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(6,6,6,6);
                            viewHolder.newsImage.setLayoutParams(layoutParams);
                            viewHolder.newsImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            viewHolder.newsImage.setAdjustViewBounds(true);
                            viewHolder.newsImage.setImageDrawable(resource);
                        }
                        else{
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
                            layoutParams.setMargins(6,6,6,6);
                            viewHolder.newsImage.setLayoutParams(layoutParams);
                            viewHolder.newsImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            //viewHolder.newsImage.setAdjustViewBounds(true);
                            viewHolder.newsImage.setImageDrawable(resource);
                        }
                    }
                });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,NewsDetail.class);
                intent.putExtra("throwTitle",data.d1_title);
                intent.putExtra("throwId",data.d1_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public void updateList(List<Data1> newList){
        data1=new ArrayList<>();
        data1.addAll(newList);
        notifyDataSetChanged();
    }
}
