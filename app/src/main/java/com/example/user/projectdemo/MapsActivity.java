package com.example.user.projectdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import static java.lang.Math.round;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    public MapView mapView;
    private GoogleMap mMap;
    public LocationManager locationManager;
    public LocationListener locationListener;
    public static final LatLng DreamSportsBorivali = new LatLng(19.240277, 72.847462

    );
    public static LatLng UnitedSportsFieldBorivali = new LatLng(19.243000, 72.843879

    );

    public static LatLng AstroParkVileParle = new LatLng(19.090985, 72.841342

    );

    public static LatLng UrbanSportsVileParle = new LatLng(19.109260, 72.849075

    );

    public static LatLng OrlemLawnTurfMalad = new LatLng(19.195776, 72.837971

    );

    public Marker mDreamSportsBorivali;
    public Marker mUnitedSportsFieldBorivali;
    public Marker mAstroParkVileParle;
    public Marker mUrbanSportsVileParle;
    public Marker mOrlemLawnTurfMalad;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else {
            // Write you code here if permission already given.
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap.setMyLocationEnabled(true);
        Location startLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        if (startLoc != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLoc.getLatitude(), startLoc.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(startLoc.getLatitude(), startLoc.getLongitude()))      // Sets the center of the map to location user
                    .zoom(14)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }



        mDreamSportsBorivali = mMap.addMarker(new MarkerOptions().position(DreamSportsBorivali).title("Dream Sports Borivali").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapturficon2)));
        mDreamSportsBorivali.setTag(0);



        mUnitedSportsFieldBorivali = mMap.addMarker(new MarkerOptions().position(UnitedSportsFieldBorivali).title("United Sports Field Borivali").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapturficon2)));
        mUnitedSportsFieldBorivali.setTag(0);


        mAstroParkVileParle = mMap.addMarker(new MarkerOptions().position(AstroParkVileParle).title("Astro Park - Lions Club Vile Parle").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapturficon2)));
        mAstroParkVileParle.setTag(0);


        mUrbanSportsVileParle = mMap.addMarker(new MarkerOptions().position(UrbanSportsVileParle).title("Urban Sports Vile Parle").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapturficon2)));
        mUrbanSportsVileParle.setTag(0);


        mOrlemLawnTurfMalad = mMap.addMarker(new MarkerOptions().position(OrlemLawnTurfMalad).title("Orlem Lawn Turf Malad").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapturficon2)));
        mOrlemLawnTurfMalad.setTag(0);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // All good!
        } else {
            Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
        }

    }
}