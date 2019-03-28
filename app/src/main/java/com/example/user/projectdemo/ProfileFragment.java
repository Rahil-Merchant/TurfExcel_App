package com.example.user.projectdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_profile,container,false);

        mAuth=FirebaseAuth.getInstance();
        View profileFragment = inflater.inflate(R.layout.fragment_profile,container, false);

        unameEt= profileFragment.findViewById(R.id.uname_profile);
        mobEt= profileFragment.findViewById(R.id.mob_no_profile);
        clubEt= profileFragment.findViewById(R.id.fav_club_profile);
        iv= profileFragment.findViewById(R.id.imageView_profile);
        Pbar= profileFragment.findViewById(R.id.progressBar_profile);
        tv= profileFragment.findViewById(R.id.verif_profile);
        ageEt=profileFragment.findViewById(R.id.age_profile);
        dbProfile = FirebaseDatabase.getInstance().getReference("profile");

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        loadUserInfo();

        profileFragment.findViewById(R.id.proceed_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

            }
        });

        profileFragment.findViewById(R.id.prof_resetPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_password();
            }
        });
        //return inflater.inflate(R.layout.fragment_profile,container,false);
        return profileFragment;
    }

    private void saveUserInformation()
    {
        //Toast.makeText(getContext(), "el chihuahua", Toast.LENGTH_SHORT).show();
        String uname = unameEt.getText().toString();
        String mob = mobEt.getText().toString();
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

            String id = dbProfile.push().getKey();  //creating unique id and getting it using getKey. Every entry creates a new uid(new node in json tree)
            profileDatabaseWrite pDb = new profileDatabaseWrite(id,mob,club,age);
            dbProfile.child(id).setValue(pDb).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Details Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Details Not Added", Toast.LENGTH_SHORT).show();
                }
            });

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),uriProfileImage);  //check
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
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Pbar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "dsf", Toast.LENGTH_SHORT).show();
            getActivity().finish();   //check if this misbehaves
            startActivity(new Intent(getContext(), MainActivity.class));
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
            if(user.getDisplayName()!= null)
            {
                unameEt.setText(user.getDisplayName());
            }

            if(user.isEmailVerified())
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
            }
        }
    }

    private void reset_password()
    {
        String usermail = mAuth.getCurrentUser().getEmail();
        if(usermail==null)
        {
            Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Password reset e-mail sent", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        startActivity(new Intent(getContext(),MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
