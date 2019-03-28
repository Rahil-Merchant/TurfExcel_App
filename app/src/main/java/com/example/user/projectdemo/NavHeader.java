package com.example.user.projectdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class NavHeader extends AppCompatActivity {
FirebaseAuth mAuth;
ImageView iv;
TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);
        iv=findViewById(R.id.imageView_header);
        tv=findViewById(R.id.uname_header);
        loadUserInfo();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });
    }

    private void loadUserInfo()
    {
        final FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            if(user.getPhotoUrl()!= null)
            {
                // Glide.with(this).load(user.getPhotoUrl().toString()).into(iv);
                Picasso.get()
                        .load(user.getPhotoUrl().toString())
                        .placeholder(R.drawable.profileimg)
                        .into(iv);
            }
            if(user.getDisplayName()!= null)
            {
                tv.setText(user.getDisplayName());
            }

            /*if(user.isEmailVerified())
            {
                tv.setText("Email has been verified");
                tv.setTextColor(Color.BLACK);
            }
            else
            {
                tv.setText("Click to verify your email");
                tv.setTextColor(Color.RED);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Verification mail sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }*/
        }

        else
        {
            tv.setText("ABC");
        }
    }
}
