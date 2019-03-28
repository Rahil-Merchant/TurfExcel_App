package com.example.user.projectdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    String amt,booking_date,timestamp,slot;
    String turf;
    int orderNo;
    TextView amtEt,bookDateEt,slotEt,timestampEt,headingEt,cashtv,paytmtv;
    EditText orderEt, custEt;
    int orderID_int;
    String orderID;
    DatabaseReference dbPayment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth=FirebaseAuth.getInstance();
        dbPayment = FirebaseDatabase.getInstance().getReference("profile");
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/montserrat.ttf");
        amtEt = findViewById(R.id.amt_payment);
        amtEt.setTypeface(font);
        bookDateEt = findViewById(R.id.dob_payment);
        bookDateEt.setTypeface(font);
        slotEt = findViewById(R.id.slot_payment);
        slotEt.setTypeface(font);
        timestampEt = findViewById(R.id.timestamp_payment);
        timestampEt.setTypeface(font);
        headingEt = findViewById(R.id.textView9);
        headingEt.setTypeface(font);
        cashtv = findViewById(R.id.cash_payment);
        paytmtv=findViewById(R.id.paytm_payment);
        //orderEt = findViewById(R.id.orderid);
        //custEt = findViewById(R.id.custid);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                amt = null;
                booking_date=null;
                timestamp=null;
                slot=null;
                turf=null;
            } else {
                amt = extras.getString("booking_amount");
                booking_date = extras.getString("currentDate");
                timestamp = extras.getString("timeStamp");
                slot=extras.getString("slot");
                turf=extras.getString("turf");
            }
        } else {
            amt = (String) savedInstanceState.getSerializable("booking_amount");
        }
        amtEt.setText(amt);
        bookDateEt.setText(booking_date);
        timestampEt.setText(timestamp);
        slotEt.setText(slot);
        //orderNo = randNo();
//        custEt.setVisibility(View.GONE);
        //custEt.setText("101");
 //       orderEt.setVisibility(View.GONE);
        //orderEt.setText("250");


        if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);


        }

        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Button btn = (Button) findViewById(R.id.start_transaction);
        paytmtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PaymentActivity.this, checksum.class);
                Intent intent = new Intent(PaymentActivity.this, PaytmActivity.class);
    //            intent.putExtra("orderid", orderEt.getText().toString());
  //              intent.putExtra("custid", custEt.getText().toString());
                intent.putExtra("slot",slot);
                intent.putExtra("booking_amount",amt);
                intent.putExtra("currentDate",booking_date);
                intent.putExtra("timeStamp",timestamp);
                  intent.putExtra("amount", amtEt.getText().toString());
                startActivity(intent);
            }
        });
        cashtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(PaymentActivity.this, "Slot Successfully Booked", Toast.LENGTH_LONG).show();
                putBookingData();
                finish();
                startActivity(new Intent(getApplicationContext(),home.class));
            }
        });
    }

    public void putBookingData()
    {
        final FirebaseUser user;
        user = mAuth.getCurrentUser();
        Random rand = new Random();
        orderID_int = rand.nextInt(10000000);
        orderID=Integer.toString(orderID_int).trim();
        paymentDatabaseWrite pDb = new paymentDatabaseWrite(amt,booking_date,slot,timestamp,turf);
        dbPayment.child("turf_user")
                .child(user.getUid())
                .child("bookings")
                .child(orderID)
                .setValue(pDb)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Booking Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Booking Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}