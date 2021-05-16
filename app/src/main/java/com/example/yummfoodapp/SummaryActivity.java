package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.CartAdapter;
import com.example.yummfoodapp.Database.OrderContract;
import com.example.yummfoodapp.Model.Order;
import com.example.yummfoodapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public CartAdapter mAdapter;
    public static final int LOADER = 0;
    public static int totalPrice=0;
    EditText email,name,num,address;
    FirebaseAuth auth;
    Loading loading;

    private static final int PAYPAL_REQUEST_CODE = 7777;
    //Create a PayPalConfigration Object
    //Start with mock environment. When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Config.PAYPAL_CLIENT_ID);


    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        name=findViewById(R.id.name);
        num=findViewById(R.id.number);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        loading=new Loading(this);

       Button clearthedata = findViewById(R.id.clearthedatabase);

       clearthedata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CartAdapter.totalPrice=0;
               int deletethedata = getContentResolver().delete(OrderContract.OrderEntry.CONTENT_URI, null, null);
           }
       });

      getSupportLoaderManager().initLoader(LOADER, null, this);

       ListView listView = findViewById(R.id.list);
       mAdapter = new CartAdapter(this, null);
       listView.setAdapter(mAdapter);
        Toast.makeText(this, "Total Price"+totalPrice, Toast.LENGTH_SHORT).show();

       findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!name.getText().toString().isEmpty() &&
                       !name.getText().toString().isEmpty()&&
                       !name.getText().toString().isEmpty()&&
                       !name.getText().toString().isEmpty()){
                   Intent intent = new Intent(SummaryActivity.this,PayPalService.class);
                   intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                   startService(intent);
                   processPayment();

               }else {
                   Toast.makeText(SummaryActivity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {OrderContract.OrderEntry._ID,
                OrderContract.OrderEntry.COLUMN_NAME,
                OrderContract.OrderEntry.COLUMN_PRICE,
                OrderContract.OrderEntry.COLUMN_QUANTITY,
                OrderContract.OrderEntry.COLUMN_EXTRASECOND,
                OrderContract.OrderEntry.COLUMN_EXTRAFIRST
        };

        return new CursorLoader(this, OrderContract.OrderEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }

//Create the PayPalPayment object and launch the PaymentActivity intent

    private void processPayment() {
      //  amount = "10";
        int a=SummaryActivity.totalPrice;

        // Change PAYMENT_INTENT_SALE to
        // – PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        // – PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        // later via calls from your server.
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(a)),"USD",
                "Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

//Implement onActivityResult():
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                        saveDetails();

                        //  send ‘confirm’ to your server for verification.
                        //saveOrder();

                       /* startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("Payment Details", paymentDetails)
                                .putExtra("Amount", amount));*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void saveDetails() {
        loading.showLoading();
        Map<String,Object> map=new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("email",email.getText().toString());
        map.put("number",num.getText().toString());
        map.put("address",address.getText().toString());
        map.put("orders",mAdapter.orderList);
        db.collection("Orders").document(auth.getUid())
                .collection("Items").document(String.valueOf(System.currentTimeMillis())).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        loading.hideLoading();
                        Toast.makeText(SummaryActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }else {
                        loading.hideLoading();
                        Toast.makeText(SummaryActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }

    FirebaseFirestore db;
    private void saveOrder() {
        /*Map<String, List<Order>> map=new HashMap<>();
        map.put("orders",mAdapter.orderList);

        db.collection("Orders").child("Orders").child(String.valueOf(System.currentTimeMillis()))
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SummaryActivity.this, "Added to Firebase", Toast.LENGTH_SHORT).show();
            }
        });*/

    }


    private void showDetails(JSONObject response, String paymentAmount) {

            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
          //  txtId.setText(response.getString("id"));
          //  txtStatus.setText(response.getString("state"));
          //  txtAmount.setText("$"+paymentAmount);

    }
}
