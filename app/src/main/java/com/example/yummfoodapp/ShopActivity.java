package com.example.yummfoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


import com.example.yummfoodapp.Adapter.ProductAdapter;
import com.example.yummfoodapp.Adapter.ProductCategoryAdapter;
import com.example.yummfoodapp.Model.ProductCategory;
import com.example.yummfoodapp.Model.Products;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {


private ImageButton logoutBtn, addProductsBtn;
//private RelativeLayout productsRl,ordersRl;

private FirebaseAuth firebaseAuth;

    ProductCategoryAdapter productCategoryAdapter;
    RecyclerView productCatRecycler, prodItemRecycler;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        addProductsBtn = findViewById(R.id.addProductsBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        //productsRl = findViewById(R.id.productsRl);
       // ordersRl = findViewById(R.id.ordersRl);



        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(new ProductCategory(1,"Most Popular"));
        productCategoryList.add(new ProductCategory(2,"Grocery"));
        productCategoryList.add(new ProductCategory(3,"Cookers"));
        productCategoryList.add(new ProductCategory(4,"Accessories"));

        setProductRecycler(productCategoryList);


        List<Products> productsList = new ArrayList<>();
        productsList.add(new Products (1, "Rice", "€3/500g", R.drawable.product1,3));
        productsList.add(new Products (1, "Potato", "€2/500g", R.drawable.potatoes,2));
        productsList.add(new Products (1, "Coriander", "€3/500", R.drawable.cori,3));
        productsList.add(new Products (1, "Pepper", "€5/500g", R.drawable.pepp,5));
        productsList.add(new Products (1, "Ginger", "€2/500g", R.drawable.ginger,2));
        productsList.add(new Products (1, "Carrot", "€3/500g", R.drawable.carrot,3));
        productsList.add(new Products (1, "Tomato", "€1/500g", R.drawable.tomato,1));
        productsList.add(new Products (1, "Chicken", "€12/500g", R.drawable.chicken,12));
        productsList.add(new Products (1, "Pork", "€10/500g", R.drawable.pork,10));
        productsList.add(new Products (1, "Duck Leg", "€9/500g", R.drawable.duck,9));
        productsList.add(new Products (1, "Rice Cooker", "€39", R.drawable.cooker,39));
        productsList.add(new Products (1, "Frying Pan", "€20", R.drawable.fryingpan,20));
        productsList.add(new Products (1, "Kettle", "€22", R.drawable.kettle,22));



        setProdItemRecycler(productsList);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        addProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit add product activity
                startActivity(new Intent(ShopActivity.this, SummaryActivity.class));
            }
        });


    }
    private  void setProductRecycler(List<ProductCategory> productCategoryList){


        productCatRecycler = findViewById(R.id.cat_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);
    }

    private  void setProdItemRecycler(List<Products> productsList){


        prodItemRecycler = findViewById(R.id.productRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        prodItemRecycler.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this, productsList);
        prodItemRecycler.setAdapter(productAdapter);
    }




}