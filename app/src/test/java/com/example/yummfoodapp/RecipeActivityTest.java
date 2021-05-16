package com.example.yummfoodapp;

import com.example.yummfoodapp.Model.Recipes;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RecipeActivityTest {

    @Test
    public void filterFunctionTest() {
        ArrayList<Recipes> list = new ArrayList();
        Recipes item1 = new Recipes (1, "Fish pie", "Level:easy time:40mins", R.drawable.fish,"Method Details:\n"+
                "Ingredients:butter, milk,butter,egg ,flour,mixed fish,parsley\n"+
                "STEP 1\n");
        Recipes item2 = new Recipes (2, "Fish Fry", "Level:easy time:30mins", R.drawable.fish,"Method Details:\n"+
                "Ingredients:spices,mixed fish,parsley\n"+
                "STEP 1\n");
        list.add(item1);
        list.add(item2);
        RecipeActivity recipeActivity = new RecipeActivity();
        ArrayList<Recipes>  filteredList = recipeActivity.filter("pie",list);
        assertEquals(filteredList.contains(item1),true);
        assertNotEquals(filteredList.contains(item2),true);
        ArrayList<Recipes>  filteredList2nd = recipeActivity.filter("fry",list);
        assertEquals(filteredList2nd.contains(item2),true);
        assertEquals(filteredList2nd.contains(item1),false);

    }

}