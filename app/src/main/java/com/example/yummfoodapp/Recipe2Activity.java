package com.example.yummfoodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Recipe2Activity extends AppCompatActivity {
    ImageView BackButton, RecipeImage ;
    TextView LongRecipeDescription,shortRecipeDescription,RecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe2);
        RecipeName = findViewById(R.id.recipe_Name);
        BackButton = findViewById(R.id.backbtn);
        RecipeImage= findViewById(R.id.recipe_img);
        LongRecipeDescription= findViewById(R.id.description);
        shortRecipeDescription = findViewById(R.id.recipe_description);

        RecipeName.setText(getIntent().getStringExtra("recipeName"));
        RecipeImage.setImageResource(  getIntent().getIntExtra("imageUri",0));
        shortRecipeDescription.setText(getIntent().getStringExtra("description"));
    }
}