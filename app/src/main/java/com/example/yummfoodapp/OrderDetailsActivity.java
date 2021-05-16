package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.OrderAdapter;
import com.example.yummfoodapp.Adapter.OrderDetailAdapter;
import com.example.yummfoodapp.Model.Details;
import com.example.yummfoodapp.Model.OrderItem;
import com.example.yummfoodapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderDetailAdapter orderAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Loading loading;
    List<OrderItem> detailsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        recyclerView=findViewById(R.id.recycler);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        loading=new Loading(this);
        getAllOrder();
    }
    private void getAllOrder() {
        loading.showLoading();
        db.collection("Orders").document(auth.getUid()).collection("Items")
                .document(getIntent().getStringExtra("id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){


                    DocumentSnapshot documentSnapshot=task.getResult();


                        List<Map> a= (List<Map>) documentSnapshot.get("orders");

                        /*for(Map bb:a){
                            Set<Map.Entry<String, Integer>> entrySet
                                    = bb.entrySet();

                            // Creating an ArrayList of Entry objects
                            ArrayList<Map.Entry<String, String>> listOfEntry
                                    = new ArrayList<Map.Entry<String, String>>((Collection<? extends Map.Entry<String, String>>) entrySet);
                            detailsList.add(listOfEntry);
                        }*/
                    /*Set<Entry<String, Integer> > entrySet
                            = map.entrySet();

                    // Creating an ArrayList of Entry objects
                    ArrayList<Entry<String, Integer> > listOfEntry
                            = new ArrayList<Entry<String, Integer> >(entrySet);

                        detailsList.addAll(a);*/

                    if(a.size()>0){
                        setAapter(a);
                    }else {
                        Toast.makeText(OrderDetailsActivity.this, "No Orders", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.hideLoading();
                    Toast.makeText(OrderDetailsActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAapter(List<Map> a) {
        orderAdapter=new OrderDetailAdapter(a);
        recyclerView.setAdapter(orderAdapter);
        loading.hideLoading();
    }

}