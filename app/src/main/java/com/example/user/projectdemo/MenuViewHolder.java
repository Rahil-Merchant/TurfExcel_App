package com.example.user.projectdemo;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView turfrating;
    public ImageView turfimage;
    public TextView turfname;
    public TextView address;
    public TextView distance;


    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/montserrat.ttf");


        turfrating = (TextView)itemView.findViewById(R.id.textViewRating);
        turfname = (TextView)itemView.findViewById(R.id.textViewTitle);
        address = (TextView)itemView.findViewById(R.id.textViewShortDesc);
        turfimage = (ImageView)itemView.findViewById(R.id.turfimg);
        turfimage.setScaleType(ImageView.ScaleType.FIT_XY);
        distance=(TextView)itemView.findViewById(R.id.turfdistance);

        turfname.setTypeface(font);
        address.setTypeface(font);
        turfrating.setTypeface(font);




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