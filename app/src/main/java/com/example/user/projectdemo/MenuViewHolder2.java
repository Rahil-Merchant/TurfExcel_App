package com.example.user.projectdemo;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public ImageView tourneyimg;
    private ItemClickListener itemClickListener;

    public MenuViewHolder2(@NonNull View itemView) {
        super(itemView);
        tourneyimg = (ImageView)itemView.findViewById(R.id.tourney);
        tourneyimg.setScaleType(ImageView.ScaleType.FIT_XY);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}