package com.example.user.projectdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    ImageView iv;
    EditText unameEt;
    EditText mobEt;
    EditText clubEt;
    EditText ageEt;
    Uri uriProfileImage;
    ProgressBar Pbar;
    String profileImageURL;
    FirebaseAuth mAuth;
    TextView tv;
    DatabaseReference dbProfile;
    String mob;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth= FirebaseAuth.getInstance();

        unameEt= findViewById(R.id.uname_profile);
        mobEt= findViewById(R.id.mob_no_profile);
        clubEt= findViewById(R.id.fav_club_profile);
        iv= findViewById(R.id.imageView_profile);
        Pbar= findViewById(R.id.progressBar_profile);
        tv= findViewById(R.id.verif_profile);
        ageEt=findViewById(R.id.age_profile);
        dbProfile = FirebaseDatabase.getInstance().getReference("profile");

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        //if(mAuth.getCurrentUser()== null)
       // {
         //   Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
        //}
        //else {
            loadUserInfo();
        //}
        findViewById(R.id.proceed_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
               // putPhoneNumber();
            }
        });

        findViewById(R.id.prof_resetPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_password();
            }
        });
    }

    private void saveUserInformation()
    {
        //Toast.makeText(getApplicationContext()(), "el chihuahua", Toast.LENGTH_SHORT).show();
        String uname = unameEt.getText().toString();
        mob = mobEt.getText().toString();
        String club = clubEt.getText().toString();
        String age = ageEt.getText().toString().trim();
        int intAge;

        if(uname.isEmpty())
        {
            unameEt.setError("Set a username");
            unameEt.requestFocus();
            return;
        }
        if(mob.isEmpty()|| !check_mobNo(mob))
        {
            mobEt.setError("Enter a valid mobile number");
            mobEt.requestFocus();
            return;
        }
        if(club.isEmpty())
        {
            clubEt.setError("Enter your favourite club");
            clubEt.requestFocus();
            return;
        }

        if(age.isEmpty())
        {
            ageEt.setError("Enter your age");
            ageEt.requestFocus();
            return;
        }
        else
        {
            intAge = Integer.parseInt(age);
        }

        if(intAge <= 6 || intAge >= 80)
        {
            ageEt.setError("Enter an age between 6  and 80");
            ageEt.requestFocus();
            return;
        }


        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null && profileImageURL!= null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(uname).setPhotoUri(Uri.parse(profileImageURL)).build();

            //.setFavClub(club).setMobNo(mob).build() cant do this. To save additional info use firebase realtime database;

            //String id = dbProfile.push().getKey();  //creating unique id and getting it using getKey. Every entry creates a new uid(new node in json tree)
            profileDatabaseWrite pDb = new profileDatabaseWrite(user.getEmail(),mob,club,age);
            dbProfile.child("turf_user").child(user.getUid()).setValue(pDb).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Details Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Details Not Added", Toast.LENGTH_SHORT).show();
                }
            });
            /*MyAppUser user = new MyAppUser();
            user.setAddressTwo("address_two");
            user.setPhoneOne("phone_one");
        ...

            mDatabaseReference.child("my_app_user").child(firebaseUser.getUid()).setValue(user).
                    addOnCompleteListener(DetailsCaptureActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {*/


            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode== RESULT_OK && data!=null && data.getData()!=null){

            uriProfileImage=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uriProfileImage);  //check
                iv.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage()
    {
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() + ".jpg");
        if(uriProfileImage!=null) {
            Pbar.setVisibility(View.VISIBLE);
            /*profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Pbar.setVisibility(View.GONE);
                    profileImageURL=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Pbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext()(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Pbar.setVisibility(View.GONE);


                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageURL = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Pbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void showImageChooser()
    {
        Intent img_intent = new Intent();
        img_intent.setType("image/*");
        img_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(img_intent,"Select Display Picture"),CHOOSE_IMAGE ); //choose_image is the request code

    }

    public static boolean check_mobNo(String s)
    {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null)
        {
            Toast.makeText(getApplicationContext(), "dsf", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
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
            else
            {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.profileimg));
            }
            if(user.getDisplayName()!= null)
            {
                unameEt.setText(user.getDisplayName());
            }
            else
            {
                unameEt.setText("Enter a username");
                unameEt.setTextColor(Color.RED);
            }

            if(user.isEmailVerified())
            {
//                tv.setText("Email has been verified");
             //   tv.setTextColor(Color.BLACK);
            }
            else
            {
/*                tv.setText("Click to verify your email");
                  tv.setTextColor(Color.RED);
                    tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Verification mail sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            */}
            getUserInfo();


        }
    }

    private void reset_password()
    {
        String usermail = mAuth.getCurrentUser().getEmail();
        if(usermail==null)
        {
            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Password reset e-mail sent", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void putPhoneNumber()
    {
        String pNo= "+91" + mob;
        Intent intent = new Intent(EditProfile.this,mobileActivity.class);
        intent.putExtra("pNo",pNo);
        startActivity(intent);
    }

public void getUserInfo()
{
    firebaseAuth= FirebaseAuth.getInstance();
    firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference dbRef=firebaseDatabase.getReference();
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseReference profRef= dbRef.child("profile").child("turf_user").child(uid).child("club");
    profRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
            String dbClub = dataSnapshot.getValue(String.class);
            clubEt.setText(dbClub);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(EditProfile.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
        }
    });
    profRef= dbRef.child("profile").child("turf_user").child(uid).child("age");
    profRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
            String dbAge = dataSnapshot.getValue(String.class);
            ageEt.setText(dbAge);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText( EditProfile.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
        }
    });
    profRef= dbRef.child("profile").child("turf_user").child(uid).child("phone");
    profRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //profileDatabaseWrite pdw = dataSnapshot.getValue(profileDatabaseWrite.class);
            String dbPhone = dataSnapshot.getValue(String.class);
            mobEt.setText(dbPhone);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(EditProfile.this, "Database Retrieval failed", Toast.LENGTH_SHORT).show();
        }
    });
}
}
