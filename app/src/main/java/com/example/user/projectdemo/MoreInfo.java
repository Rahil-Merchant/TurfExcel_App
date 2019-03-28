package com.example.user.projectdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MoreInfo extends AppCompatActivity {
ViewFlipper v_flipper;
ArrayAdapter<String> adapter;
String[] slots = new String[18];
ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info);
        int images[] = {R.drawable.urban1,R.drawable.urban2};
        v_flipper= findViewById(R.id.flipper);

        String[] slots = new String[] {"6-7 AM          Rs 800", "7-8 AM          Rs 1000", "8-9 AM          Rs 1600", "9-10 AM          Rs 1600", "10-11 AM          Rs 1600", "11-12 Noon          Rs 1600", "12-1 PM          Rs 1600", "1-2 PM          Rs 1600", "2-3 PM          Rs 1600", "3-4 PM          Rs 2000", "4-5 PM          Rs 2000", "5-6 PM          Rs 2000", "6-7 PM          Rs 2000", "7-8 PM          Rs 2500", "8-9 PM          Rs 2500", "9-10 PM          Rs 2500", "10-11 PM          Rs 2500", "11-12 AM          Rs 2500"};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, slots);

        /*for(int i=0;i< images.length;i++)
        {
            flipperImages(images[i]);
        }*/

        for(int image: images)
        {
            flipperImages(image);
        }

    }

    public void flipperImages(int image){
        imageview = new ImageView(this);
        imageview.setBackgroundResource(image);

        v_flipper.addView(imageview);
        v_flipper.setFlipInterval(2500); //2.5 seconds (2500 ms)
        v_flipper.setAutoStart(true);

        //Animation
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);

    }
    public void onClick(View w) {
        new AlertDialog.Builder(this)
                .setTitle("Available Slots:")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: user specific action

                        String amt = null;
                        switch (which)
                        {
                            case 0:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 800", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "800";
                                i.putExtra("booking_amount",amt);
                                startActivity(i);
                                break;
                            case 1:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1000", Toast.LENGTH_LONG).show();
                                Intent i1 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1000";
                                i1.putExtra("booking_amount",amt);
                                startActivity(i1);
                                break;
                            case 2:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i2 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i2.putExtra("booking_amount",amt);
                                startActivity(i2);
                                break;
                            case 3:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i3 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i3.putExtra("booking_amount",amt);
                                startActivity(i3);
                                break;
                            case 4:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i4 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i4.putExtra("booking_amount",amt);
                                startActivity(i4);
                                break;
                            case 5:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i5 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i5.putExtra("booking_amount",amt);
                                startActivity(i5);
                                break;
                            case 6:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i6 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i6.putExtra("booking_amount",amt);
                                startActivity(i6);
                                break;
                            case 7:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i7 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i7.putExtra("booking_amount",amt);
                                startActivity(i7);
                                break;
                            case 8:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 1600", Toast.LENGTH_LONG).show();
                                Intent i8 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "1600";
                                i8.putExtra("booking_amount",amt);
                                startActivity(i8);
                                break;
                            case 9:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i9 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2000";
                                i9.putExtra("booking_amount",amt);
                                startActivity(i9);
                                break;
                            case 10:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i10 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2000";
                                i10.putExtra("booking_amount",amt);
                                startActivity(i10);
                                break;
                            case 11:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i11 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2000";
                                i11.putExtra("booking_amount",amt);
                                startActivity(i11);
                                break;
                            case 12:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2000", Toast.LENGTH_LONG).show();
                                Intent i12 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2000";
                                i12.putExtra("booking_amount",amt);
                                startActivity(i12);
                                break;
                            case 13:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i13 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2500";
                                i13.putExtra("booking_amount",amt);
                                startActivity(i13);
                                break;
                            case 14:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i14 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2500";
                                i14.putExtra("booking_amount",amt);
                                startActivity(i14);
                                break;
                            case 15:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i15 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2500";
                                i15.putExtra("booking_amount",amt);
                                startActivity(i15);
                                break;
                            case 16:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i16 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2500";
                                i16.putExtra("booking_amount",amt);
                                startActivity(i16);
                                break;
                            case 17:
                                Toast.makeText(MoreInfo.this, "Amount Payable = Rs 2500", Toast.LENGTH_LONG).show();
                                Intent i17 = new Intent(MoreInfo.this,PaymentActivity.class);
                                amt = "2500";
                                i17.putExtra("booking_amount",amt);
                                startActivity(i17);
                                break;
                        }

                        dialog.dismiss();
                    }
                }).create().show();
    }
}