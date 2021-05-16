package com.example.yummfoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummfoodapp.Model.OrderItem;
import com.example.yummfoodapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.Views> {

    public OrderDetailAdapter(List<Map> detailsList) {
        this.detailsList = detailsList;
    }

    List<Map> detailsList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist,parent,false);
        context=parent.getContext();
        return new Views(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Views holder, int position) {
        //final OrderItem details=detailsList.get(position);
        holder.drinkName.setText(detailsList.get(position).get("nameofdrink").toString());
        holder.price.setText(detailsList.get(position).get("pricesofdrink").toString());
        holder.yesCream.setText(detailsList.get(position).get("yeshasCream").toString());
        holder.yesTopping.setText(detailsList.get(position).get("yeshastopping").toString());
        holder.quantity.setText(detailsList.get(position).get("quantitysofdrink").toString());
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class Views extends RecyclerView.ViewHolder {
        TextView drinkName, yesCream, yesTopping, price, quantity;



        public Views(@NonNull View view) {
            super(view);
            drinkName = view.findViewById(R.id.drinkNameinOrderSummary);
            price = view.findViewById(R.id.priceinOrderSummary);
            yesCream = view.findViewById(R.id.hasCream);
            yesTopping = view.findViewById(R.id.hasTopping);
            quantity = view.findViewById(R.id.quantityinOrderSummary);
        }
    }
}
