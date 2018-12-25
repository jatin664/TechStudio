package com.example.jatin.techstudio;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder{
    public ImageView newsImage;
    public TextView newsTitle;
    public LinearLayout linearLayout;
    public View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        newsImage= itemView.findViewById(R.id.main_image_id);
        newsTitle= itemView.findViewById(R.id.main_title_id);
        linearLayout= itemView.findViewById(R.id.linearlayout_id);
        view= itemView.findViewById(R.id.view_id);

    }
}
