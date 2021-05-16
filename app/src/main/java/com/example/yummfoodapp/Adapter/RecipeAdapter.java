package com.example.yummfoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummfoodapp.InfoActivity;
import com.example.yummfoodapp.Model.Products;
import com.example.yummfoodapp.Model.Recipes;
import com.example.yummfoodapp.ProductDetailsActivity;
import com.example.yummfoodapp.R;
import com.example.yummfoodapp.Recipe1Activity;
import com.example.yummfoodapp.ShopActivity;
import com.example.yummfoodapp.util.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    Context context;
    List<Recipes> RecipeList;
    boolean fav;

    public RecipeAdapter(Context context, List<Recipes> RecipeList,boolean fav) {
        this.context = context;
        this.RecipeList = RecipeList;
        this.fav=fav;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipes_row_item, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.recipeImage.setImageResource(RecipeList.get(position).getImageUri());
        holder.recipeName.setText(RecipeList.get(position).getRecipeName());
        holder.recipe_description.setText(RecipeList.get(position).getDescription());
        holder.recipeDetails.setText(RecipeList.get(position).getDetails());
        if(fav){
            holder.like.setVisibility(View.GONE);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFav(RecipeList.get(position).getRecipeName(),RecipeList.get(position).getDescription(),RecipeList.get(position).getImageUri(),
                        RecipeList.get(position).getDetails());
            }
        });

        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(position){
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        Intent i = new Intent(context, Recipe1Activity.class);
                        i.putExtra("recipeName",RecipeList.get(position).getRecipeName());
                        i.putExtra("description",RecipeList.get(position).getDescription());
                        i.putExtra("imageUri",RecipeList.get(position).getImageUri());
                        i.putExtra("details",RecipeList.get(position).getDetails());

                        context.startActivity(i);

                        break;


                }

            }
        });
    }

    private void saveToFav(String recipeName, String description, Integer imageUri, String details) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final Loading loading=new Loading(context);
        loading.showLoading();
        Map<String,Object> map=new HashMap<>();
        map.put("name",recipeName);
        map.put("desc",description);
        map.put("img",imageUri);
        map.put("details",details);
        db.collection("Favorites").document(auth.getUid()).collection("Recipe").document(String.valueOf(System.currentTimeMillis())).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loading.hideLoading();
                    Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show();
                }else {
                    loading.hideLoading();
                    Toast.makeText(context, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return RecipeList.size();
    }

    public void filterList(ArrayList<Recipes> filteredList) {
        RecipeList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView recipeImage;
        TextView recipeName, recipe_description,recipeDetails;
        Button like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.prod_image);
            recipeName = itemView.findViewById(R.id.prod_name);
            recipe_description = itemView.findViewById(R.id.prod_description);
            recipeDetails = itemView.findViewById(R.id.details);
            like=itemView.findViewById(R.id.like);


        }
    }

}
