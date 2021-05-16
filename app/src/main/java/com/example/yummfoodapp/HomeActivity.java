package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.PostAdapter;
import com.example.yummfoodapp.Model.PostModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private AdvanceDrawerLayout drawer;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    PostAdapter postadapter;
    List<PostModel> postModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        navBar();

        //init
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

         postModelList = new ArrayList<>();
         //retrieve the data from firebase
      loadPosts();

      //searchView to search posts by post title/description
      //  MenuItem item = menu.findItem(R.id.action_search)

    }

    private void loadPosts() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
            postModelList.clear();
            for (DataSnapshot ds: datasnapshot.getChildren()){
                PostModel postModel = ds.getValue(PostModel.class);
                postModelList.add(postModel);
                postadapter = new PostAdapter(HomeActivity.this , postModelList);
                recyclerView.setAdapter(postadapter);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPosts(String searchQuery){



    }

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = auth.getCurrentUser();

        if(user !=null) {
           // email = user.getEmail();
            //uid = user.getUid();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
            finish();
        }
    }
    private void navBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Home Activity");
        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setBackgroundColor( getResources().getColor( R.color.colorPrimary ) ); 
         navigationView.setNavigationItemSelectedListener(HomeActivity.this);
         drawer.setViewScale( Gravity.START, 0.9f);
         drawer.setViewElevation(Gravity.START, 20);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:

                Intent intToHome = new Intent(this, HomeActivity.class);
               startActivity(intToHome);
               break;
            case R.id.add_post:

                Intent add = new Intent(this, AddPostActivity.class);
                startActivity(add);
                break;
            case R.id.profile:

                Intent p = new Intent(this, ProfileActivity.class);
                startActivity(p);
                break;

            case R.id.shop:

                Intent shop = new Intent(this,ShopActivity.class);
                startActivity(shop);
                break;
            case R.id.cart:

                Intent re = new Intent(this,RecipeActivity.class);
                startActivity(re);
                break;

            case R.id.favourite:

                Intent ref = new Intent(this,FavoriteActivity.class);
                startActivity(ref);
                break;
            case R.id.orders:

                Intent order = new Intent(this,OrderActivity.class);
                startActivity(order);
                break;
          //  case R.id.action_search:

           //     Intent search = new Intent(this,SearchActivity.class);
            //    startActivity(search);
            //    break;
            case R.id.logout:
                auth=FirebaseAuth.getInstance();
                auth.signOut();
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent log = new Intent(this, LoginActivity.class);
                startActivity(log);
                break;



        }


        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}