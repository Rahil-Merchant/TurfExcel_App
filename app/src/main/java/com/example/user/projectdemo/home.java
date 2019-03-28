package com.example.user.projectdemo;

import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;
    private DrawerLayout drawer;
    private static long backPressed;
    public static int TIME_LIMIT = 1500;
    GoogleSignInClient mGoogleAuth;
    FloatingActionButton mapBtn;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    GoogleSignInResult result;
    public static double d1,d2,d3,d4,d5;
    public LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth=FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAuth = GoogleSignIn.getClient(this,gso);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadNav();
        loadMenu();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(home.this, MapsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadMenu() {

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Category");

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.turfname.setText(model.getTurftitle());
                Picasso.get().load(model.getImage())
                        .into(holder.turfimage);
                holder.address.setText(model.getTurfAddress());
                holder.distance.setText(model.getTurfDistance());
                holder.turfrating.setText(model.getTurfRating());
                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent turfdetails = new Intent(home.this, TurfDetails.class);
                        turfdetails.putExtra("TurfID", adapter.getRef(position).getKey());
                        startActivity(turfdetails);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.turfmenu, viewGroup, false);

                return new MenuViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
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
                mGoogleAuth.signOut();
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
    public void onBackPressed() {

        if (backPressed + TIME_LIMIT > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
        }
        backPressed = System.currentTimeMillis();
    }

    public void openMap(View v)
    {
        startActivity(new Intent(this,MapsActivity.class));
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

