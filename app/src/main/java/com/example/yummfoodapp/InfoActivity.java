package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yummfoodapp.Database.OrderContract;

public class InfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //init views

    ImageView imageView;
    ImageButton plusquantity, minusquantity;
    TextView quantitynumber, ProductInfo, Price;
    CheckBox addFine, addExtraLarge;
    Button addtoCart;
    int quantity=0;
    public Uri mCurrentCartUri;
    boolean hasAllRequiredValues = false;
    int price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        imageView = findViewById(R.id.imageViewInfo);
        plusquantity = findViewById(R.id.addquantity);
        minusquantity  = findViewById(R.id.subquantity);
        quantitynumber = findViewById(R.id.quantity);
        ProductInfo = findViewById(R.id.ProductInfo);
        Price = findViewById(R.id.Price);
        addFine = findViewById(R.id.addToppings);
        addtoCart = findViewById(R.id.addtocart);
        addExtraLarge = findViewById(R.id.addCream);


        // setting the name of product


     //   drinnkName.setText("Japanese Rice");
       ProductInfo.setText(getIntent().getStringExtra("productName"));
       imageView.setImageResource(  getIntent().getIntExtra("imageView",0));
       Price.setText(getIntent().getStringExtra("productPrice"));

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, SummaryActivity.class);
                startActivity(intent);
                // once this button is clicked , we will save our values in the database and send those values
                // right away to summary activity where we display the order info

                SaveCart();
            }
        });

        //


        plusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rice price
                int basePrice=3;
                price= getIntent().getIntExtra("price",0);
               //  price=pro.charAt(1);
                Toast.makeText(InfoActivity.this, "price"+price, Toast.LENGTH_SHORT).show();
                quantity++;

                displayQuantity();

                int ricePrice = price * quantity;


                String setnewPrice = String.valueOf(ricePrice);
                Price.setText("€"+setnewPrice);

                // checkBoxes functionality
                boolean isAddExtraLarge = addExtraLarge.isChecked();
                boolean isAddFine = addFine.isChecked();

                int ifCheckBox = CalculatePrice(Integer.parseInt(String.valueOf(price)), quantity, isAddExtraLarge, isAddFine);
              Price.setText("€ " + ifCheckBox);


            }
        });minusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  int basePrice=3;
                if(quantity == 0){

                    Toast.makeText(InfoActivity.this, "Can't decrease anymore", Toast.LENGTH_SHORT).show();
                }
                else{
                    quantity--;
                    displayQuantity();

                    int ricePrice = Integer.parseInt(String.valueOf(price)) * quantity;
                    String setnewPrice = String.valueOf(ricePrice);
                   Price.setText(setnewPrice);

                    // checkBoxes functionality

                    boolean isAddExtraLarge = addExtraLarge.isChecked();
                    boolean isAddFine = addFine.isChecked();

                    int ifCheckBox = CalculatePrice(Integer.parseInt(String.valueOf(price)), quantity, isAddExtraLarge, isAddFine);

                   Price.setText("€ " + ifCheckBox);

                }

            }
        });




    }

    private boolean SaveCart() {

        //getting values from our views
        String name = ProductInfo.getText().toString();
        String price = Price.getText().toString();
        String quantity = quantitynumber.getText().toString();

        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderEntry.COLUMN_NAME, name);
        values.put(OrderContract.OrderEntry.COLUMN_PRICE, price);
        values.put(OrderContract.OrderEntry.COLUMN_QUANTITY, quantity);

        if (addExtraLarge.isChecked()) {
            values.put(OrderContract.OrderEntry.COLUMN_EXTRASECOND, "Add Extra: YES");
        } else {
            values.put(OrderContract.OrderEntry.COLUMN_EXTRASECOND, "Add Extra: NO");

        }

        if (addFine.isChecked()) {
            values.put(OrderContract.OrderEntry.COLUMN_EXTRAFIRST, "Add Fine: YES");
        } else {
            values.put(OrderContract.OrderEntry.COLUMN_EXTRAFIRST, "Add Fine: NO");

        }

        if (mCurrentCartUri == null) {
            Uri newUri = getContentResolver().insert(OrderContract.OrderEntry.CONTENT_URI, values);
            if (newUri==null) {
                Toast.makeText(this, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Success  adding to Cart", Toast.LENGTH_SHORT).show();

            }
        }

        hasAllRequiredValues = true;
        return hasAllRequiredValues;


    }

    public int CalculatePrice(int price, int quantity, boolean addExtraLarge, boolean addFine) {

        int basePrice = price;

        if (addExtraLarge) {
            // add the fine cost $1
            basePrice  ++;
        }

        if (addFine) {
            // add extra large cost is $1
            basePrice = basePrice + 1;
        }

        return basePrice * quantity;
    }


    private void displayQuantity() {
        quantitynumber.setText(String.valueOf(quantity));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {OrderContract.OrderEntry._ID,
                OrderContract.OrderEntry.COLUMN_NAME,
                OrderContract.OrderEntry.COLUMN_PRICE,
                OrderContract.OrderEntry.COLUMN_QUANTITY,
                OrderContract.OrderEntry.COLUMN_EXTRAFIRST,
                OrderContract.OrderEntry.COLUMN_EXTRASECOND
        };

        return new CursorLoader(this, mCurrentCartUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int name = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_NAME);
            int price = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_PRICE);
            int quantity = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_QUANTITY);
            int hasCream = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_EXTRASECOND);
            int hasTopping = cursor.getColumnIndex(OrderContract.OrderEntry.COLUMN_EXTRAFIRST);


            String nameofdrink = cursor.getString(name);
            String priceofdrink = cursor.getString(price);
            String quantityofdrink = cursor.getString(quantity);
            String yeshasCream = cursor.getString(hasCream);
            String yeshastopping = cursor.getString(hasTopping);

            ProductInfo.setText(nameofdrink);
            Price.setText(priceofdrink);
            quantitynumber.setText(quantityofdrink);
        }
        }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ProductInfo.setText("");
        Price.setText("");
        quantitynumber.setText("");
    }
}