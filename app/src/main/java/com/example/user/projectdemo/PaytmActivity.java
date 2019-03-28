package com.example.user.projectdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
//PAYTM

import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaytmActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    String custid="", orderId="", mid="",amt="";
    int amtInt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_turf_details);

        InitPaytm();
    }

    private void InitPaytm(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // create instance of Random class
        Random rand = new Random();
        // Generate random integers in range 0 to 9999999
        int rand_orderid = rand.nextInt(10000000);
        //Intent intent = getIntent();
        orderId = String.valueOf(rand_orderid);
        custid = "124356457814567956";
        Intent intent = getIntent();
        amt = intent.getExtras().getString("amount");
        amtInt=Integer.parseInt(amt);
        mid = "lXJlFV87479245228588"; /// your merchant key
        PaytmActivity.sendUserDetailTOServerdd dl = new PaytmActivity.sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //PAYTM

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(PaytmActivity.this);

        //private String orderId , mid, custid, amt;
        String url ="https://turfexcelapp.000webhostapp.com/generateChecksum.php";
        String varifyurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
        String CHECKSUMHASH ="";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            jsonparse jsonParser = new jsonparse(PaytmActivity.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" +orderId+
                            "&CUST_ID="+custid+
                            "&TXN_AMOUNT="+amtInt+
                            "&CHANNEL_ID=WAP&WEBSITE=DEFAULT"+
                            "&CALLBACK_URL="+varifyurl+"&INDUSTRY_TYPE_ID=Retail";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);


            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {

                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getProductionService();
            // PaytmPGService  Service = PaytmPGService.getProductionService();

            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amt);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(PaytmActivity.this, true, true,
                    PaytmActivity.this  );


        }

    }


    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
        if (ContextCompat.checkSelfPermission(PaytmActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(PaytmActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        finish();
        startActivity(new Intent(this,PaytmActivity.class));

    }


    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,home.class));

    }

    @Override
    public void clientAuthenticationFailed(String s) {
        finish();
        Toast.makeText(this, "Authentication Failure", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,home.class));
    }

    @Override
    public void someUIErrorOccurred(String s) {

        Log.e("checksum ", " ui fail respon  "+ s );
        Toast.makeText(this, "Some UI error occurred", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,home.class));
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
        Toast.makeText(this, "Error loading web page", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,home.class));
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,home.class));
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
        Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this,home.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        finish();
        startActivity(new Intent(this,home.class));
    }
}


