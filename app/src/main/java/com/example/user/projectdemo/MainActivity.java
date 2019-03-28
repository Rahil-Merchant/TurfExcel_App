package com.example.user.projectdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    FirebaseAuth mAuth;
    EditText inEmailEt, inPasswordEt;
    ProgressBar inPbar;
    FirebaseUser user;
    GoogleSignInClient mGoogleAuth;
    int RC_SIGNIN = 0;
    GoogleApiClient mGoogleApiClient;
    TextView username_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        inEmailEt = (EditText) findViewById(R.id.inEmail);
        inPasswordEt = (EditText) findViewById(R.id.inPassword);
        inPbar = (ProgressBar) findViewById(R.id.inProgressBar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.inSignup).setOnClickListener(this);
        findViewById(R.id.inSignin).setOnClickListener(this);
        findViewById(R.id.inForgot).setOnClickListener(this);
        findViewById(R.id.gSignIn).setOnClickListener(this);
        username_header=findViewById(R.id.uname_header);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleAuth = GoogleSignIn.getClient(this,gso);

    }

    private void gsignIn()
    {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGNIN);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        /*super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGNIN){
            //GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           // GoogleSignInAccount account = null;
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
           // firebaseAuthWithGoogle(account);
           // handleSignInResult(result);
        }*/

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGNIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    public void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount acct=result.getSignInAccount();
            startActivity(new Intent(this,home.class));
            //username_header.setText(acct.getDisplayName());

            Toast.makeText(this, "hello" +acct.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }

    private void userLogin() {
        String email = inEmailEt.getText().toString();
        String password = inPasswordEt.getText().toString();

        if (email.isEmpty()) {
            inEmailEt.setError("Email is required");
            inEmailEt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inEmailEt.setError("Please enter a valid email");
            inEmailEt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inPasswordEt.setError("Password is required");
            inPasswordEt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inPasswordEt.setError("Password must contain at least 6 characters");
            inPasswordEt.requestFocus();
            return;
        }

        inPbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                inPbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();  //so that on pressing back from sign up we dont go back to login screen
                    user = mAuth.getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Intent intent = new Intent(MainActivity.this, /*HomePageActivity*/home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //So that we dont return to login screen after pressing back button
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please log in via a verified e-mail", Toast.LENGTH_SHORT).show();
                        user.delete();  //delete user if mail not verified at time of login
                        Toast.makeText(MainActivity.this, "Account deleted, please sign up again and verify your mail", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        overridePendingTransition(0, 0);  //refresh activity
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());

                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(mAuth.getCurrentUser() != null || acc!=null)  //dont log in again if user is already signed in
        {
            finish();
            startActivity(new Intent(this, /*HomePageActivity*/home.class));
        }
    }

    /*private void gLogin()
    {
        Intent gIntent = mGoogleAuth.getSignInIntent();
        startActivityForResult(gIntent, RC_SIGNIN);
       // Toast.makeText(this, "hnhjg", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGNIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc= completedTask.getResult(ApiException.class);
            startActivity(new Intent(this, HomePageActivityhome.class));
        } catch (ApiException e){
            Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            boolean newuser= task.getResult().getAdditionalUserInfo().isNewUser();
                            if(newuser)
                            {
                                Toast.makeText(MainActivity.this, "Welcome to Turf Excel", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                startActivity(new Intent(getApplicationContext(), EditProfile.class));
                            }
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("TAG", "signInWithCredential:success");
                            else
                            {
                                Toast.makeText(MainActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                startActivity(new Intent(getApplicationContext(), home.class));
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inSignup:
                finish();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                break;

            case R.id.inSignin:
                userLogin();
                break;

            case R.id.inForgot:
                startActivity(new Intent(getApplicationContext(),PasswordActivity.class));
                break;

            case R.id.gSignIn:
                //gLogin();
                gsignIn();
                break;

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
