package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.OrderAdapter;
import com.example.yummfoodapp.Model.Details;
import com.example.yummfoodapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Loading loading;
    List<Details> detailsList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView=findViewById(R.id.recycler);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        loading=new Loading(this);
        getAllOrder();

    }

    private void getAllOrder() {
        loading.showLoading();
        db.collection("Orders").document(auth.getUid()).collection("Items")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshots:task.getResult()){
                        detailsList.add(new Details(snapshots.getId(),snapshots.getString("name"),
                                snapshots.getString("email"), snapshots.getString("address")
                                ,snapshots.getString("number")));
                    }
                    if(detailsList.size()>0){
                        setAapter(detailsList);
                    }else {
                        loading.hideLoading();
                        Toast.makeText(OrderActivity.this, "No Orders", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.hideLoading();
                    Toast.makeText(OrderActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAapter(List<Details> detailsList) {
        orderAdapter=new OrderAdapter(detailsList);
        recyclerView.setAdapter(orderAdapter);
        loading.hideLoading();
    }
}