package com.example.yummfoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummfoodapp.Model.ProductCategory;
import com.example.yummfoodapp.R;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductViewHolder> {
    Context context;
    List<ProductCategory> productCategoryList;

    public ProductCategoryAdapter(Context context, List<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.categoryName.setText(productCategoryList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return productCategoryList.size() ;
    }

    public static final class  ProductViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.cat);
        }
    }
}
