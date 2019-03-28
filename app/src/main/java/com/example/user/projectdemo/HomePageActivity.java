package com.example.user.projectdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import static android.view.View.*;


public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static long backPressed;
    public static int TIME_LIMIT= 1500;
    private Toast backToast;

    private DrawerLayout drawer;
    GoogleSignInClient mGoogleAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /*if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_profile);
        }*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAuth = GoogleSignIn.getClient(this,gso);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bookings:
                finish();
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TournamentFragment()).commit();
                break;
            case R.id.nav_logout:
                //for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {  //checking if user is logged in with email-password or google sign in
                  //  if (user.getProviderId().equals("password")) {
                        FirebaseAuth.getInstance().signOut();
                   // }
                    //else
                   // {
                        mGoogleAuth.signOut();
                    //}
                //}
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {

        if(backPressed + TIME_LIMIT > System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else
        {
           if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
            else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
        }
        backPressed = System.currentTimeMillis();
    }


}
