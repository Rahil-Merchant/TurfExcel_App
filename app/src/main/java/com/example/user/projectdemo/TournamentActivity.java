package com.example.user.projectdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class TournamentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;
    private DrawerLayout drawer;
    private static long backPressed;
    NavigationView navigationView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Tournaments");
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadMenu();
        loadNav();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth= FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void loadMenu() {

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Tournaments");

        FirebaseRecyclerOptions<tourneylist> options =
                new FirebaseRecyclerOptions.Builder<tourneylist>()
                        .setQuery(query, tourneylist.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<tourneylist, MenuViewHolder2>(options) {
            protected void onBindViewHolder(@NonNull MenuViewHolder2 holder, int position, @NonNull final tourneylist model) {

                Picasso.get().load(model.getTourneyimg())
                        .into(holder.tourneyimg);

                final tourneylist clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(model.getTourneygoogledoc()));
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.tourneycard, viewGroup, false);

                return new MenuViewHolder2(view);
            }
        };

        recyclerView.setAdapter(adapter);
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
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TournamentFragment()).commit();
                startActivity(new Intent(this,TournamentActivity.class));
                break;
            case R.id.nav_logout:
                //for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {  //checking if user is logged in with email-password or google sign in
                //  if (user.getProviderId().equals("password")) {
                FirebaseAuth.getInstance().signOut();
                //mGoogleAuth.signOut();
                // }
                //else
                // {
//
                //}
                //}
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void loadNav()
    {
        //GoogleSignInAccount acct= result.getSignInAccount();
        final FirebaseUser user = mAuth.getCurrentUser();
        View hView =  navigationView.getHeaderView(0);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageView_header);
        TextView tv = (TextView)hView.findViewById(R.id.uname_header);
        if(user==null)
        {
            tv.setText("Rahil Merchant");
            imgvw.setImageDrawable(getResources().getDrawable(R.drawable.profileimg));
        }
        else{

            tv.setText( user.getDisplayName());
            Picasso.get()
                    .load(user.getPhotoUrl().toString())
                    .placeholder(R.drawable.profileimg)
                    .into(imgvw);
        }

    }

}
