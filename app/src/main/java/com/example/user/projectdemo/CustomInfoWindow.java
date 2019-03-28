package com.example.user.projectdemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.example.user.projectdemo.MapsActivity;
import com.example.user.projectdemo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import org.w3c.dom.Text;

public class CustomInfoWindow extends MapsActivity implements GoogleMap.InfoWindowAdapter, View.OnClickListener {
    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView title = (TextView) view.findViewById(R.id.turftitle);
        title.setText(marker.getTitle());
        TextView dist = (TextView) view.findViewById(R.id.distance);
        dist.setText(marker.getSnippet());
        return view;
    }





    @Override
    public void onClick(View v) {

    }


}