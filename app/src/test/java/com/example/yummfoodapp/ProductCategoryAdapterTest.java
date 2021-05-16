package com.example.yummfoodapp;

import com.example.yummfoodapp.Adapter.ProductCategoryAdapter;
import com.example.yummfoodapp.Model.ProductCategory;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ProductCategoryAdapterTest {
    @Test
    public void getItemCounTest()
    {
        ArrayList<ProductCategory> list = new ArrayList();
        MainActivity mainActivity = new MainActivity();
        list.add(new ProductCategory(2,"item2"));
        list.add(new ProductCategory(3,"item3"));
        ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(mainActivity,list);
        int sie = productCategoryAdapter.getItemCount();
        assertEquals(sie, 2);

    }

}