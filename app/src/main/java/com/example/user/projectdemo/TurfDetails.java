package com.example.user.projectdemo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TurfDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView turf_name, turf_link, contact, phone_no;
    ImageView turf_image, turf_amenities;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton bookbtn;
    String turfID="";
    String bookingDate;
    String timestamp;
    String slotTime,turfName;


    FirebaseDatabase database;
    DatabaseReference turfs;

    ArrayAdapter<String> adapter;
    String[] slots = new String[18];

    SimpleDateFormat s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_details);
// custom font
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/montserrat.ttf");
        s= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        database = FirebaseDatabase.getInstance();
        turfs = database.getReference("Turfs");

        bookbtn = (FloatingActionButton)findViewById(R.id.book);
        turf_name = (TextView)findViewById(R.id.turf_name);
        turf_name.setTypeface(font);
        turf_image = (ImageView)findViewById(R.id.imgturf);
        turf_image.setScaleType(ImageView.ScaleType.FIT_XY);
        turf_amenities = (ImageView)findViewById(R.id.turfamenities);
        turf_amenities.setScaleType(ImageView.ScaleType.FIT_XY);
        turf_link = (TextView)findViewById(R.id.turflink);
        turf_link.setClickable(true);
        turf_link.setMovementMethod(LinkMovementMethod.getInstance());
        contact=(TextView)findViewById(R.id.contact);
        contact.setTypeface(font);
        turf_link.setTypeface(font);
        phone_no = (TextView)findViewById(R.id.phoneno);
        phone_no.setTypeface(font);


        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);

        if(getIntent()!= null)
        {
            turfID = getIntent().getStringExtra("TurfID");

        }
        if(!turfID.isEmpty())
        {
            getdetailsTurf(turfID);
        }

        String[] slots = new String[] {"6-7 AM          Rs 800", "7-8 AM          Rs 1000", "8-9 AM          Rs 1600", "9-10 AM          Rs 1600", "10-11 AM          Rs 1600", "11-12 PM          Rs 1600", "12-1 PM          Rs 1600", "1-2 PM          Rs 1600", "2-3 PM          Rs 1600", "3-4 PM          Rs 2000", "4-5 PM          Rs 2000", "5-6 PM          Rs 2000", "6-7 PM          Rs 2000", "7-8 PM          Rs 2500", "8-9 PM          Rs 2500", "9-10 PM          Rs 2500", "10-11 PM          Rs 2500", "11-12 AM          Rs 2500"};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, slots);



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        bookingDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Toast.makeText(this, ""+bookingDate, Toast.LENGTH_SHORT).show();
    }

    private void getdetailsTurf(String turfID)
    {
        turfs.child(turfID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Turf turf = dataSnapshot.getValue(Turf.class);

                Picasso.get().load(turf.getTurfimage()).into(turf_image);
                Picasso.get().load(turf.getTurfamenities()).into(turf_amenities);
                turfName= turf.getTurfname();
                turf_name.setText(turf.getTurfname());
                turf_link.setText(Html.fromHtml(turf.getTurfLink()));
                phone_no.setText(turf.getTurfphone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View w) {
        DialogFragment datePicker =new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(),"Date Picker");
        new AlertDialog.Builder(this)
                .setTitle("Available Slots:")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: user specific action

                        String amt;
                        timestamp= s.format(new Date());
                        String slot;
                        switch (which)
                        {
                            case 0:
                                //Toast.makeText(TurfDetails.this, "Amount Payable = Rs 800", Toast.LENGTH_LONG).show();
                                //Toast.makeText(TurfDetails.this, timestamp, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "800";
                                slot="6-7 AM";
                                i.putExtra("booking_amount",amt);
                                i.putExtra("currentDate",bookingDate);
                                i.putExtra("timeStamp",timestamp);
                                i.putExtra("slot",slot);
                                i.putExtra("turf",turfName);
                                startActivity(i);
                                break;
                            case 1:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1000", Toast.LENGTH_LONG).show();
                                Intent i1 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1000";
                                slot="7-8 AM";
                                i1.putExtra("slot",slot);
                                i1.putExtra("booking_amount",amt);
                                i1.putExtra("currentDate",bookingDate);
                                i1.putExtra("timeStamp",timestamp);
                                i1.putExtra("turf",turfName);
                                startActivity(i1);
                                break;
                            case 2:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i2 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="8-9 AM";
                                i2.putExtra("slot",slot);
                                i2.putExtra("booking_amount",amt);
                                i2.putExtra("currentDate",bookingDate);
                                i2.putExtra("timeStamp",timestamp);
                                i2.putExtra("turf",turfName);
                                startActivity(i2);
                                break;
                            case 3:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i3 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="9-10 AM";
                                i3.putExtra("slot",slot);
                                i3.putExtra("booking_amount",amt);
                                i3.putExtra("currentDate",bookingDate);
                                i3.putExtra("timeStamp",timestamp);
                                i3.putExtra("turf",turfName);
                                startActivity(i3);
                                break;
                            case 4:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i4 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="10-11 AM";
                                i4.putExtra("slot",slot);
                                i4.putExtra("booking_amount",amt);
                                i4.putExtra("currentDate",bookingDate);
                                i4.putExtra("timeStamp",timestamp);
                                i4.putExtra("turf",turfName);
                                startActivity(i4);
                                break;
                            case 5:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i5 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="11-12 PM";
                                i5.putExtra("slot",slot);
                                i5.putExtra("booking_amount",amt);
                                i5.putExtra("currentDate",bookingDate);
                                i5.putExtra("timeStamp",timestamp);
                                i5.putExtra("turf",turfName);
                                startActivity(i5);
                                break;
                            case 6:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i6 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="12-1 PM";
                                i6.putExtra("slot",slot);
                                i6.putExtra("booking_amount",amt);
                                i6.putExtra("currentDate",bookingDate);
                                i6.putExtra("timeStamp",timestamp);
                                i6.putExtra("turf",turfName);
                                startActivity(i6);
                                break;
                            case 7:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i7 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="1-2 PM";
                                i7.putExtra("slot",slot);
                                i7.putExtra("booking_amount",amt);
                                i7.putExtra("currentDate",bookingDate);
                                i7.putExtra("timeStamp",timestamp);
                                i7.putExtra("turf",turfName);
                                startActivity(i7);
                                break;
                            case 8:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i8 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "1600";
                                slot="2-3 PM";
                                i8.putExtra("slot",slot);
                                i8.putExtra("booking_amount",amt);
                                i8.putExtra("currentDate",bookingDate);
                                i8.putExtra("timeStamp",timestamp);
                                i8.putExtra("turf",turfName);
                                startActivity(i8);
                                break;
                            case 9:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i9 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2000";
                                slot="3-4 PM";
                                i9.putExtra("slot",slot);
                                i9.putExtra("booking_amount",amt);
                                i9.putExtra("currentDate",bookingDate);
                                i9.putExtra("timeStamp",timestamp);
                                i9.putExtra("turf",turfName);
                                startActivity(i9);
                                break;
                            case 10:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i10 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2000";
                                slot="4-5 PM";
                                i10.putExtra("slot",slot);
                                i10.putExtra("booking_amount",amt);
                                i10.putExtra("currentDate",bookingDate);
                                i10.putExtra("timeStamp",timestamp);
                                i10.putExtra("turf",turfName);
                                startActivity(i10);
                                break;
                            case 11:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i11 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2000";
                                slot="5-6 PM";
                                i11.putExtra("slot",slot);
                                i11.putExtra("booking_amount",amt);
                                i11.putExtra("currentDate",bookingDate);
                                i11.putExtra("timeStamp",timestamp);
                                i11.putExtra("turf",turfName);
                                startActivity(i11);
                                break;
                            case 12:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i12 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2000";
                                slot="6-7 PM";
                                i12.putExtra("slot",slot);
                                i12.putExtra("booking_amount",amt);
                                i12.putExtra("currentDate",bookingDate);
                                i12.putExtra("timeStamp",timestamp);
                                i12.putExtra("turf",turfName);
                                startActivity(i12);
                                break;
                            case 13:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i13 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2500";
                                slot="7-8 PM";
                                i13.putExtra("slot",slot);
                                i13.putExtra("booking_amount",amt);
                                i13.putExtra("currentDate",bookingDate);
                                i13.putExtra("timeStamp",timestamp);
                                i13.putExtra("turf",turfName);
                                startActivity(i13);
                                break;
                            case 14:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i14 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2500";
                                slot="8-9 PM";
                                i14.putExtra("slot",slot);
                                i14.putExtra("booking_amount",amt);
                                i14.putExtra("currentDate",bookingDate);
                                i14.putExtra("timeStamp",timestamp);
                                i14.putExtra("turf",turfName);
                                startActivity(i14);
                                break;
                            case 15:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i15 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2500";
                                slot="9-10 PM";
                                i15.putExtra("slot",slot);
                                i15.putExtra("booking_amount",amt);
                                i15.putExtra("currentDate",bookingDate);
                                i15.putExtra("timeStamp",timestamp);
                                i15.putExtra("turf",turfName);
                                startActivity(i15);
                                break;
                            case 16:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i16 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2500";
                                slot="10-11 PM";
                                i16.putExtra("slot",slot);
                                i16.putExtra("booking_amount",amt);
                                i16.putExtra("currentDate",bookingDate);
                                i16.putExtra("timeStamp",timestamp);
                                i16.putExtra("turf",turfName);
                                startActivity(i16);
                                break;
                            case 17:
                                Toast.makeText(TurfDetails.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i17 = new Intent(TurfDetails.this,PaymentActivity.class);
                                amt = "2500";
                                slot="11-12 AM";
                                i17.putExtra("slot",slot);
                                i17.putExtra("booking_amount",amt);
                                i17.putExtra("currentDate",bookingDate);
                                i17.putExtra("timeStamp",timestamp);
                                i17.putExtra("turf",turfName);
                                startActivity(i17);
                                break;
                        }

                        dialog.dismiss();
                    }
                }).create().show();
    }
}
