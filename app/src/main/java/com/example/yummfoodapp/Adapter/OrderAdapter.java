package com.example.yummfoodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummfoodapp.Model.Details;
import com.example.yummfoodapp.Model.Order;
import com.example.yummfoodapp.OrderDetailsActivity;
import com.example.yummfoodapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Views> {

    public OrderAdapter(List<Details> detailsList) {
        this.detailsList = detailsList;
    }

    List<Details> detailsList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orders,parent,false);
        context=parent.getContext();
        return new Views(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Views holder, int position) {
        final Details details=detailsList.get(position);
        holder.name.setText(details.getName());
        holder.address.setText(details.getAddress());
        holder.email.setText(details.getEmail());
        holder.num.setText(details.getNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("id",String.valueOf(details.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class Views extends RecyclerView.ViewHolder {
        TextView name,num,email,address;
        public Views(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            num=itemView.findViewById(R.id.num);
            email=itemView.findViewById(R.id.email);
            address=itemView.findViewById(R.id.address);
        }
    }
}
