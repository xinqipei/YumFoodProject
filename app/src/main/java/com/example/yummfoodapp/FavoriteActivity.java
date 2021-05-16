package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.RecipeAdapter;
import com.example.yummfoodapp.Model.Recipes;
import com.example.yummfoodapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    Loading loading;
    RecipeAdapter recipeAdapter;
    RecyclerView recyclerView;
    List<Recipes> recipeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        auth=FirebaseAuth.getInstance();
        loading=new Loading(this);
        recyclerView=findViewById(R.id.recycler);
        db=FirebaseFirestore.getInstance();
        getAllFav();

    }

    private void getAllFav() {
        loading.showLoading();
        db.collection("Favorites").document(auth.getUid()).collection("Recipe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        recipeList.add(new Recipes(2,snapshot.getString("name"),snapshot.getString("desc"),Integer.parseInt(snapshot.get("img").toString())
                                ,snapshot.getString("details")));

                    }
                    if(recipeList.size()>0){
                        setUpAdapter(recipeList);
                    }else {
                        Toast.makeText(FavoriteActivity.this, "No Recipe added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.hideLoading();
                    Toast.makeText(FavoriteActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setUpAdapter(List<Recipes> recipeList) {
        recipeAdapter=new RecipeAdapter(this,recipeList,true);
        recyclerView.setAdapter(recipeAdapter);
        loading.hideLoading();
    }
}