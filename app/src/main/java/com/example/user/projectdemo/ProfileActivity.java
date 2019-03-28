package com.example.user.projectdemo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    DrawerLayout drawer;
    FirebaseAuth mAuth;
    ImageView img;
    TextView emailtv,unametv,mobtv,clubtv,agetv;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        Toolbar prof_toolbar =  findViewById(R.id.prof_toolbar);

        setSupportActionBar(prof_toolbar);
        mAuth= FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        FirebaseUser user = mAuth.getCurrentUser();
        img=findViewById(R.id.profile_img);
        emailtv= findViewById(R.id.email_user);
        unametv= findViewById(R.id.username_user);
        mobtv=findViewById(R.id.mobile_user);
        clubtv=findViewById(R.id.club_user);
        agetv=findViewById(R.id.age_user);

       /* drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, prof_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
     //   this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle(/*getResources().getString(R.string.user_name)*/ user.getDisplayName());
        Picasso.get()
                .load(user.getPhotoUrl().toString())
                .placeholder(R.drawable.profileimg)
                .into(img);


        dynamicToolbarColor();




        toolbarTextAppernce();
        //loadUserInfo();
getUserInfo();
    }





    private void dynamicToolbarColor() {



        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),

                R.drawable.profileimg);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override

            public void onGenerated(Palette palette) {

                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));

                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));

            }

        });

    }





    private void toolbarTextAppernce() {

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

    }







    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;

    }



    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        switch (item.getItemId())
        {
            default:
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this,EditProfile.class));
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bookings:
                //finish();
                startActivity(new Intent(this, home.class));
                // startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                startActivity(new Intent(this, ProfileActivity.class));
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
                //     mGoogleAuth.signOut();
                //}
                //}
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getUserInfo()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference dbRef=firebaseDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference profRef= dbRef.child("profile").child("turf_user").child(uid).child("club");
        unametv.setText(user.getDisplayName());
        emailtv.setText(user.getEmail());
        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
                String dbClub = dataSnapshot.getValue(String.class);

               clubtv.setText(dbClub);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });
        profRef= dbRef.child("profile").child("turf_user").child(uid).child("age");
        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
                String dbAge = dataSnapshot.getValue(String.class);
                agetv.setText(dbAge);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( ProfileActivity.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });
        profRef= dbRef.child("profile").child("turf_user").child(uid).child("phone");
        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
                String dbPhone = dataSnapshot.getValue(String.class);
                mobtv.setText(dbPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
              Toast.makeText(ProfileActivity.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
